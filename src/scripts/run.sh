#!/bin/bash

# Find the base directory
DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )/" && pwd )

# Move to the directory containing the Service.jar. It has to be run from this directory where the configuration files are also located.
cd $DIR
# Run the Data Service.
java -jar  Service.jar

echo -e "\n"
read -p "Press any key to continue..." key

exit 0