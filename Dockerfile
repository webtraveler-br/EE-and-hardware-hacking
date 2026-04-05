FROM python:3.12-slim

ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1

WORKDIR /srv/roadmap

COPY requirements.txt /tmp/requirements.txt
RUN python -m pip install --no-cache-dir -r /tmp/requirements.txt

RUN groupadd -r appuser && useradd -r -g appuser -d /srv/roadmap appuser

COPY . /srv/roadmap
RUN mkdir -p /srv/roadmap/data && chown -R appuser:appuser /srv/roadmap/data

USER appuser

EXPOSE 8088

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8088", "--proxy-headers", "--forwarded-allow-ips", "*"]
