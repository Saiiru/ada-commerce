#!/usr/bin/env bash
set -e
# Requer GitHub CLI autenticado: gh auth login

declare -A LABELS=(
  ["task"]="#0366d6"
  ["story"]="#0e8a16"
  ["bug"]="#d73a4a"
  ["domain"]="#5319e7"
  ["application"]="#1d76db"
  ["infrastructure"]="#a2eeef"
  ["cli"]="#fbca04"
  ["good first issue"]="#7057ff"
)

for name in "${!LABELS[@]}"; do
  color=${LABELS[$name]}
  gh label create "$name" --color "${color#\#}" --force >/dev/null
done

echo "Rótulos criados/atualizados."
