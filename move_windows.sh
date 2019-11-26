#!/bin/bash

WIDTH=350
HEIGHT=200
BAR_HEIGHT=30

names=""
for i in $(seq $3); do
  names="$names switch$i"
done
names="$names Alice Bob Controller"

x=$1
((y = $2 + BAR_HEIGHT))
HALF=$(($3 / 2))
j=0
for name in $names
do
  if ((j % HALF == 0 && j != 0)); then
    ((y = y + HEIGHT + BAR_HEIGHT))
  fi
  if ((j % 4 == 0)); then
    x=$1
  else
    ((x = x + WIDTH))
  fi
  wmctrl -r $name -e 0,$x,$y,$WIDTH,$HEIGHT
  ((j++))
done
