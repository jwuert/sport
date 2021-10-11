#!/bin/sh

export CLASSPATH=$CLASSPATH:build/libs/sport.jar
export CLASSPATH=$CLASSPATH:../ambitusmodel/build/libs/ambitusmodel-1.0.0-SNAPSHOT.jar
export CLASSPATH=$CLASSPATH:../score/build/libs/score.jar
java org.wuerthner.sport.util.Comparator $1 $2 org.wuerthner.ambitus.model.AmbitusFactory Arrangement Model