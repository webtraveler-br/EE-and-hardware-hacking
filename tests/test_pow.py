import hashlib

from app.anti_bot import ProofOfWorkManager


def solve(prefix: str, difficulty: int) -> str:
    target = "0" * difficulty
    nonce = 0
    while True:
        digest = hashlib.sha256(f"{prefix}:{nonce}".encode("utf-8")).hexdigest()
        if digest.startswith(target):
            return str(nonce)
        nonce += 1


def test_pow_challenge_roundtrip() -> None:
    manager = ProofOfWorkManager(difficulty=2, ttl_seconds=120)
    challenge = manager.create("127.0.0.1")
    nonce = solve(challenge["prefix"], challenge["difficulty"])

    ok, reason = manager.verify(
        challenge_id=challenge["challenge_id"],
        nonce=nonce,
        ip="127.0.0.1",
    )
    assert ok is True
    assert reason == "ok"
