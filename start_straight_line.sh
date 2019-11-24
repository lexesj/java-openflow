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
  CONNECTIONS=""
  if [ $i -eq 0 ]; then
    CONNECTIONS="${CONNECTIONS} switch$(($i + 1)) endpoint0"
  fi
  if [ $i -ne $NUM_PORTS ]; then
    CONNECTIONS="${CONNECTIONS} switch$(($i - 1)) switch$(($i + 1))"
  fi
  if [ $i -eq $NUM_PORTS ]; then
    CONNECTIONS="${CONNECTIONS} switch$(($i - 1)) endpoint1"
  fi
  TRIMMED=$(echo $CONNECTIONS | xargs)
  ./switch.sh switch$i $CONNECTIONS &
done

./endpoint.sh endpoint0 switch0 &
./endpoint.sh endpoint1 switch$NUM_PORTS &
