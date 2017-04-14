#!/bin/sh

xinput set-prop 'ADS7846 Touchscreen' 'Coordinate Transformation Matrix' 1 0 0 0 -1 1 0 0 1

/usr/lib/jvm/java-8-openjdk/bin/java -Dfile.encoding=UTF-8 -Duser.country=GB -Duser.language=en -Duser.variant -jar /root/TermostatoSwing/build/libs/TestSwt-0.0.1.jar

unclutter -idle 0
