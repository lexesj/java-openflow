#!/bin/bash

END_POINT_NAME=$1

sudo docker run -v /tmp/.X11-unix:/tmp/.X11-unix --name $END_POINT_NAME -e DISPLAY --network telecomms -v $(pwd):/cs2031 --rm java java -cp /cs2031/out/production/java-openflow/ ie.tcd.mantiqul.node.EndNode $@
