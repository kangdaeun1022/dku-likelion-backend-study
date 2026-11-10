#!/bin/bash
set -euo pipefail
dnf install -y docker git
systemctl enable --now docker
systemctl enable --now amazon-ssm-agent
mkdir -p /opt/likelion
# Compose plugin installation and application deployment are separate steps in README.
