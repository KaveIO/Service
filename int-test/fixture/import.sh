#!/bin/bash

mongoimport -u test -p test -d test -c regionNodes --file regionNodes.json
mongoimport -u test -p test -d test -c heatMapLayer --file heatMapLayer.json
mongoimport -u test -p test -d test -c visitLayer --file visitLayer.json
