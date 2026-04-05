from __future__ import annotations

from collections import defaultdict, deque
from dataclasses import dataclass
from datetime import datetime, timedelta, timezone
import hashlib
import secrets
import threading
import time


@dataclass
class PowChallenge:
    challenge_id: str
    prefix: str
    ip_hash: str
    expires_at: datetime
    used: bool = False


class ProofOfWorkManager:
    def __init__(self, *, difficulty: int, ttl_seconds: int) -> None:
        self._difficulty = difficulty
        self._ttl_seconds = ttl_seconds
        self._lock = threading.Lock()
        self._challenges: dict[str, PowChallenge] = {}

    @property
    def difficulty(self) -> int:
        return self._difficulty

    def create(self, ip: str) -> dict[str, str | int]:
        challenge_id = secrets.token_urlsafe(18)
        prefix = secrets.token_urlsafe(20)
        challenge = PowChallenge(
            challenge_id=challenge_id,
            prefix=prefix,
            ip_hash=self._hash_ip(ip),
            expires_at=datetime.now(timezone.utc) + timedelta(seconds=self._ttl_seconds),
        )

        with self._lock:
            self._cleanup_locked()
            self._challenges[challenge_id] = challenge

        return {
            "challenge_id": challenge_id,
            "prefix": prefix,
            "difficulty": self._difficulty,
            "ttl_seconds": self._ttl_seconds,
        }

    def verify(self, *, challenge_id: str, nonce: str, ip: str) -> tuple[bool, str]:
        with self._lock:
            self._cleanup_locked()
            challenge = self._challenges.get(challenge_id)
            if challenge is None:
                return False, "challenge_inexistente"
            if challenge.used:
                return False, "challenge_ja_utilizado"
            if challenge.expires_at < datetime.now(timezone.utc):
                return False, "challenge_expirado"
            if challenge.ip_hash != self._hash_ip(ip):
                return False, "ip_invalido"

            digest = hashlib.sha256(f"{challenge.prefix}:{nonce}".encode("utf-8")).hexdigest()
            if not digest.startswith("0" * self._difficulty):
                return False, "pow_invalido"

            challenge.used = True
            return True, "ok"

    @staticmethod
    def _hash_ip(ip: str) -> str:
        return hashlib.sha256(ip.encode("utf-8")).hexdigest()[:24]

    def _cleanup_locked(self) -> None:
        now = datetime.now(timezone.utc)
        expired = [cid for cid, c in self._challenges.items() if c.expires_at < now]
        for cid in expired:
            del self._challenges[cid]


class SimpleRateLimiter:
    def __init__(self) -> None:
        self._lock = threading.Lock()
        self._buckets: dict[tuple[str, int, int], deque[float]] = defaultdict(deque)

    def allow(self, *, key: str, limit: int, window_seconds: int) -> tuple[bool, int]:
        now = time.time()
        bucket_key = (key, limit, window_seconds)

        with self._lock:
            bucket = self._buckets[bucket_key]
            cutoff = now - window_seconds
            while bucket and bucket[0] < cutoff:
                bucket.popleft()

            if len(bucket) >= limit:
                retry_after = max(1, int(window_seconds - (now - bucket[0])))
                return False, retry_after

            bucket.append(now)
            return True, 0
