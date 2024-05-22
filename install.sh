#!/usr/bin/env bash

mvn clean
cd hcatalog
mvn clean
mvn package -DskipTests -Dos.arch=x86_64
cd ..
mvn package -Pdist -DskipTests -Dcheckstyle.skip=true -Dmaven.javadoc.skip=true -Drat.numUnapprovedLicenses=2000 -Dos.arch=x86_64

rm -rf /Users/david/data/bigdata/hive
rm -rf /Users/david/data/bigdata/apache-hive-3.1.3-bin
rm -rf /Users/david/data/github/hive/docker/apache-hive-3.1.3-bin.tar.gz
cp packaging/target/apache-hive-3.1.3-bin.tar.gz /Users/david/data/github/hive/docker/apache-hive-3.1.3-bin.tar.gz

rm -rf /Users/david/data/github/spark/docker/apache-hive-3.1.3-bin.tar.gz
cp packaging/target/apache-hive-3.1.3-bin.tar.gz /Users/david/data/github/spark/docker/apache-hive-3.1.3-bin.tar.gz

tar -xvf packaging/target/apache-hive-3.1.3-bin.tar.gz -C /Users/david/data/bigdata
ln -s /Users/david/data/bigdata/apache-hive-3.1.3-bin /Users/david/data/bigdata/hive

cp /Users/david/data/bigdata/mysql-connector-java-8.0.15.jar /Users/david/data/bigdata/hive/lib/mysql-connector-java-8.0.15.jar
cp docker/config/hdfs-site.xml /Users/david/data/bigdata/hive/conf
cp docker/config/core-site.xml /Users/david/data/bigdata/hive/conf
cp docker/config/hive-site.xml /Users/david/data/bigdata/hive/conf/hive-site.xml
cp hive-log4j2.properties /Users/david/data/bigdata/hive/conf/hive-log4j2.properties
cp spark-defaults.conf /Users/david/data/bigdata/hive/conf/spark-defaults.conf