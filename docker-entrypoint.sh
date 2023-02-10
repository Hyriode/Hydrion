#!/bin/bash

: "${MIN_MEMORY:=256M}}"
: "${MAX_MEMORY:=4G}}"

echo "[init] Copying Hydrion jar"
cp /usr/app/Hydrion.jar /hydrion

echo "[init] Starting process..."
exec java -Xms${MIN_MEMORY} -Xmx${MAX_MEMORY} -jar Hydrion.jar