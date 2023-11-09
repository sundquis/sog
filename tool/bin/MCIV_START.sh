#!/bin/bash

cd /home/sundquis/mciv/sog/bin
java -Xms2g -Xmx6g -Dsystem.dir=/home/sundquis/mciv/sog mciv.server.Server &
date
echo Server started.
