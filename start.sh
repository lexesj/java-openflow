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

if [ $# -ne 0 ]; then
  NUM_PORTS=$(($1 - 1))
else
  NUM_PORTS=0;
fi

for i in `seq 0 $NUM_PORTS`;
do
  if [ $i -ne $NUM_PORTS ]; then
    ./switch.sh $i switch$(($i + 1)).telecomms &
  else
    ./switch.sh $i endpoint1.telecomms &
  fi
done

./endpoint.sh 0 switch0.telecomms &
./endpoint.sh 1 switch$NUM_PORTS.telecomms &
