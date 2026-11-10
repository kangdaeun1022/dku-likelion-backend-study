#!/bin/bash
# Single-container replacement example: this is NOT zero-downtime deployment.
set -euo pipefail
cd /opt/likelion
export IMAGE="${1:?provide an immutable image tag}"
docker compose --profile production pull app
docker compose --profile production up -d --no-deps app
for attempt in {1..30}; do
  if docker compose exec -T proxy curl --fail --silent http://app:8080/actuator/health | grep -q '"UP"'; then
    exit 0
  fi
  sleep 2
done
echo 'Application did not become healthy; redeploy the previous image tag.' >&2
exit 1
