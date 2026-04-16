from __future__ import annotations

import logging

from sqlalchemy import create_engine, inspect, text
from sqlalchemy.engine import Engine
from sqlalchemy.orm import declarative_base, sessionmaker

logger = logging.getLogger(__name__)

Base = declarative_base()


def build_engine(database_url: str) -> Engine:
    connect_args = {"check_same_thread": False} if database_url.startswith("sqlite") else {}
    return create_engine(database_url, future=True, connect_args=connect_args)


def build_session_factory(engine: Engine) -> sessionmaker:
    return sessionmaker(bind=engine, autoflush=False, autocommit=False, future=True)


def ensure_columns(engine: Engine) -> None:
    """Add any columns defined in ORM models but missing from the DB.

    Works only for SQLite (simple ALTER TABLE ADD COLUMN). Runs once at startup
    so new fields don't require manual migration.
    """
    insp = inspect(engine)
    for table_name in insp.get_table_names():
        mapper_cls = None
        for cls in Base.__subclasses__():
            if getattr(cls, "__tablename__", None) == table_name:
                mapper_cls = cls
                break
        if mapper_cls is None:
            continue

        existing = {col["name"] for col in insp.get_columns(table_name)}
        for col in mapper_cls.__table__.columns:
            if col.name not in existing:
                col_type = col.type.compile(engine.dialect)
                ddl = f"ALTER TABLE {table_name} ADD COLUMN {col.name} {col_type}"
                with engine.begin() as conn:
                    conn.execute(text(ddl))
                logger.info("Added missing column %s.%s (%s)", table_name, col.name, col_type)
