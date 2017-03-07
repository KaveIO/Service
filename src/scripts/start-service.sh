#!/bin/bash

# Find the base directory
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )/../" && pwd )

# Move to the directory containing the Service.jar. It has to be run from this directory where the configuration files are also located.

# Change to the base directory so all relative paths make sense
cd $DIR

# Run the Data Service.
java -jar -Dlog4j.configuration=file:$DIR/config/log4j.properties $DIR/service*.jar
