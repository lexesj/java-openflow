#!/bin/bash

xhost +

./compile.sh

NETWORK_NAME=telecomms
# create telecomms network if not exists
sudo docker inspect $NETWORK_NAME > /dev/null 2>&1 || \
sudo docker network create --subnet 172.0.20.0/16 $NETWORK_NAME

./controller.sh &

# allow controller to initialise
sleep 1

# usage: ./switch.sh <switch name> <connection 1> ... <connection N>
./switch.sh switch1 switch2 switch3 switch4 Alice &
./switch.sh switch2 switch1 switch5 &
./switch.sh switch3 switch1 switch6 &
./switch.sh switch4 switch1 switch6 switch7 &
./switch.sh switch5 switch2 switch7 &
./switch.sh switch6 switch3 switch4 switch8 &
./switch.sh switch7 switch4 switch5 switch8 &
./switch.sh switch8 switch6 switch7 Bob &

# usage: ./endpoint.sh <endpoint name> <default switch connection>
./endpoint.sh Alice switch1 &
./endpoint.sh Bob switch8 &
