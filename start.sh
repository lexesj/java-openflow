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

for i in `seq 1 $1`;
do
  ./switch.sh $i &
done
