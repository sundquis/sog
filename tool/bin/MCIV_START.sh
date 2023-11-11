#!/bin/bash

cd /home/sundquis/mciv/sog/bin
java -Xms2g -Xmx6g -Dsystem.name=mciv.xml -Dsystem.dir=/home/sundquis/mciv/sog mciv.server.Server &
echo -n "Server started: "
date

