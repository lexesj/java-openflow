#!/bin/bash

SWITCH_NUM=$1
#discard first argument
shift

sudo docker run -v /tmp/.X11-unix:/tmp/.X11-unix --name switch$SWITCH_NUM -e DISPLAY --network telecomms -v $(pwd):/cs2031 --rm java java -cp /cs2031/out/production/java-openflow/ ie.tcd.mantiqul.node.Switch $@
