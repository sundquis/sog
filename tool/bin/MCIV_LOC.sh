#!/bin/bash

echo "'"$2"'" > .arg
curl --include --request POST --header "'Content-Type: application/java'" --data @.arg http://192.168.4.34:1104$1
echo
