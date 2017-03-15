#!/bin/bash

# Find the base directory
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )

PATH_PREFIX=${1:-$DIR/certificates}

KEYSTORE_PATH_SUFFIX=keystore.jks
TRUSTSTORE_PATH_SUFFIX=truststore.jks

KEYSTORE_PATH_SERVICE=$PATH_PREFIX/service-$KEYSTORE_PATH_SUFFIX
TRUSTSTORE_PATH_SERVICE=$PATH_PREFIX/service-$TRUSTSTORE_PATH_SUFFIX
ALIAS_SERVICE=${3:-cert-service}
STOREPASS=${4:-storepass}
KEYPASS=${5:-keypass}
DNAME_CN_SERVICE=${7:-localhost}
DNAME_OU=${8:-}
DNAME_O=${9:-}
DNAME_L=${10:-}
DNAME_S=${11:-}
DNAME_C=${12:-}
KEYALG=${13:-RSA}

mkdir -p $DIR/certificates
"$DIR/scripts/create-certificate.sh" "$KEYSTORE_PATH_SERVICE" "$TRUSTSTORE_PATH_SERVICE" "$ALIAS_SERVICE" "$STOREPASS" "$KEYPASS" "$DNAME_CN_SERVICE" "$DNAME_OU" "$DNAME_O" "$DNAME_L" "$DNAME_S" "$DNAME_C" "$KEYALG" $DIR/certificates


