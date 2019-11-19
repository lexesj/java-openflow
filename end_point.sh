#!/bin/bash

sudo docker run -v /tmp/.X11-unix:/tmp/.X11-unix --name endpoint$1 -e DISPLAY --network telecomms -v $(pwd):/cs2031 --rm java java -cp /cs2031/out/production/java-openflow/ ie.tcd.mantiqul.node.EndNode
