#!/usr/bin/env bash
# Descarga las dependencias de Jackson a lib/ (no se versionan: *.jar está en .gitignore).
# Uso, desde la raíz del repo: ./descargar-dependencias.sh
# Verifica el SHA-1 de cada jar contra el publicado en Maven Central.
set -euo pipefail

VERSION="2.17.2"
BASE="https://repo1.maven.org/maven2/com/fasterxml/jackson/core"
MODULOS=("jackson-databind" "jackson-core" "jackson-annotations")

cd "$(dirname "$0")"
mkdir -p lib

for modulo in "${MODULOS[@]}"; do
  jar="$modulo-$VERSION.jar"
  if [ -f "lib/$jar" ]; then
    echo "ya está: lib/$jar"
    continue
  fi
  echo "descargando $jar ..."
  curl -fsSL "$BASE/$modulo/$VERSION/$jar" -o "lib/$jar"
  esperado="$(curl -fsSL "$BASE/$modulo/$VERSION/$jar.sha1" | cut -d' ' -f1)"
  real="$(sha1sum "lib/$jar" | cut -d' ' -f1)"
  if [ "$esperado" != "$real" ]; then
    rm -f "lib/$jar"
    echo "ERROR: el SHA-1 de $jar no coincide" >&2
    exit 1
  fi
done
echo "Listo: dependencias en lib/"
