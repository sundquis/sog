#!/bin/bash

echo "'"$2"'" > .arg
curl --include --request POST --header "Content-Type: application/json" --data @.arg http://23.88.147.138:1104$1
echo
