#!/usr/bin/env bash
set -euo pipefail
ROOT="$(CDPATH= cd -- "$(dirname -- "$0")/.." && pwd)"
mkdir -p "$ROOT/logs"
{
  printf 'Student: Suyubek Alisher | Group: IT2-2306 | ID: 37773\n'
  printf 'Date: '; date -Iseconds
  printf 'Username: '; whoami
  printf '\nOperating system:\n'; cat /etc/os-release
  printf '\nKernel:\n'; uname -a
  printf '\nCPU information:\n'; lscpu
  printf '\nMemory:\n'; free -h
  printf '\nDisk usage and available space:\n'; df -h
} | tee "$ROOT/logs/Suyubek_Alisher_system_report.log"
