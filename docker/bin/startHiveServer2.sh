#!/usr/bin/env bash

set -e

cd /opt/hive
cp /opt/hive/conf/*.xml /opt/spark/conf/

echo "Starting the HiveServer2 service"

echo "192.168.49.2	control-plane.minikube.internal" >> /etc/hosts

bin/hive --service hiveserver2

#tail -f /dev/null