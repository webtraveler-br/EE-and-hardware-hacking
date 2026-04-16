#!/usr/bin/env python3
from __future__ import annotations

import json
from pathlib import Path
import sys

ROOT = Path(__file__).resolve().parents[1]
if str(ROOT) not in sys.path:
    sys.path.insert(0, str(ROOT))

from app.config import Settings
from app.database import Base, build_engine, build_session_factory
from app.flashcards import sync_flashcards


def main() -> int:
    settings = Settings.load()
    engine = build_engine(settings.database_url)
    session_factory = build_session_factory(engine)

    Base.metadata.create_all(bind=engine)

    db = session_factory()
    try:
        summary = sync_flashcards(db, settings.content_root)
        db.commit()
    finally:
        db.close()

    print(json.dumps(summary, ensure_ascii=False, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
