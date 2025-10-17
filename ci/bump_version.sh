#!/usr/bin/env bash
set -euo pipefail

PROP_FILE="gradle.properties"

get_prop() {
  local key="$1"
  grep -E "^${key}=" "$PROP_FILE" | sed -E "s/^${key}=//"
}

set_prop() {
  local key="$1"
  local value="$2"
  # Replace in-place
  if grep -qE "^${key}=" "$PROP_FILE"; then
    sed -i.bak -E "s#^(${key}=).*#\1${value}#g" "$PROP_FILE"
  else
    echo "${key}=${value}" >> "$PROP_FILE"
  fi
}

BRANCH="${CI_COMMIT_REF_NAME:-unknown}"
VERSION_CODE="$(get_prop VERSION_CODE)"
VERSION_NAME="$(get_prop VERSION_NAME)"

echo "Current: VERSION_CODE=$VERSION_CODE, VERSION_NAME=$VERSION_NAME, branch=$BRANCH"

if [[ "$BRANCH" == "main" ]]; then
  NEW_CODE=$((VERSION_CODE + 1))
  set_prop "VERSION_CODE" "$NEW_CODE"

  # (Optional) auto bump patch version name - comment if you prefer static
  IFS='.' read -r MAJ MIN PATCH <<< "${VERSION_NAME}"
  PATCH=$((PATCH + 1))
  NEW_NAME="${MAJ}.${MIN}.${PATCH}"
  set_prop "VERSION_NAME" "$NEW_NAME"

  echo "Bumped: VERSION_CODE=$NEW_CODE, VERSION_NAME=$NEW_NAME"
else
  echo "Not main branch; no version bump."
fi