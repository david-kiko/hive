FROM openjdk:8-jre-slim-bullseye

ENV DEBIAN_FRONTEND="noninteractive"

COPY config/pip.conf /tmp/pip.conf

RUN sed -i -E 's#http://(deb|security).debian.org#http://mirrors.aliyun.com#g' /etc/apt/sources.list && \
    apt-get update && \
    apt-get install -o Acquire::Retries=5 --no-install-recommends -y \
      wget \
      curl \
      vim \
      krb5-user \
      iputils-ping \
      python3 \
      python3-pip \
      procps \
      telnet \
      tzdata && \
    apt-get clean && \
    mkdir $HOME/.pip/ && \
    cp -f /tmp/pip.conf $HOME/.pip/ && \
    ln -s /usr/bin/python3 /usr/bin/python

ARG HADOOP_VERSION="3.3.6"
ARG HIVE_VERSION="3.1.3"
ARG SPARK_VERSION="3.3.5-SNAPSHOT"
ENV SPARK_HOME="/opt/spark"
ENV PATH="/opt/spark/bin:${PATH}"
ENV SPARK_DIST_CLASSPATH="/opt/hadoop/etc/hadoop:/opt/hadoop/share/hadoop/common/lib/*:/opt/hadoop/share/hadoop/common/*:/opt/hadoop/share/hadoop/hdfs:/opt/hadoop/share/hadoop/hdfs/lib/*:/opt/hadoop/share/hadoop/hdfs/*:/opt/hadoop/share/hadoop/mapreduce/lib/*:/opt/hadoop/share/hadoop/mapreduce/*:/opt/hadoop/share/hadoop/yarn:/opt/hadoop/share/hadoop/yarn/lib/*:/opt/hadoop/share/hadoop/yarn/*:/opt/hive/lib/*"

WORKDIR /opt/

RUN apt-get update -o Acquire::Retries=5 && \
    apt-get install -o Acquire::Retries=5 -y --no-install-recommends \
    gnupg2 \
    jq && \
    curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add - && \
    echo "deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main" | tee -a /etc/apt/sources.list.d/kubernetes.list && \
    apt-get update -o Acquire::Retries=5 && \
    apt-get install -o Acquire::Retries=5 -y --no-install-recommends \
    kubectl && \
    apt-get clean

ENV HADOOP_HOME="/opt/hadoop"
ENV HIVE_HOME="/opt/hive"
ENV PATH="/opt/hadoop/bin:/opt/hive/bin:${PATH}:/opt/spark/bin:${PATH}"

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

RUN touch 1 && rm 1
COPY apache-hive-3.1.3-bin.tar.gz ./
COPY hadoop-${HADOOP_VERSION}.tar.gz ./

RUN tar -xzf apache-hive-3.1.3-bin.tar.gz -C /opt && \
    rm apache-hive-3.1.3-bin.tar.gz && \
    mv /opt/apache-hive-3.1.3-bin /opt/hive && \
    tar -xzf hadoop-${HADOOP_VERSION}.tar.gz && \
    mv hadoop-${HADOOP_VERSION} /opt/hadoop && \
    rm hadoop-${HADOOP_VERSION}.tar.gz

COPY mysql-connector-java-8.0.15.jar /opt/hive/lib/
COPY config/hive-site.xml /opt/hive/conf
COPY config/hive-log4j2.properties /opt/hive/conf
COPY config/spark-defaults.conf /opt/hive/conf
COPY config/core-site.xml /opt/hadoop/etc/hadoop
COPY config/hdfs-site.xml /opt/hadoop/etc/hadoop

COPY bin/hive-env.sh /opt/hive/conf

# spark without hive
copy spark-3.3.5-SNAPSHOT-bin-3.3.2.tgz ./
RUN tar -xzf spark-3.3.5-SNAPSHOT-bin-3.3.2.tgz -C /opt && \
    rm -rf spark-3.3.5-SNAPSHOT-bin-3.3.2.tgz && \
    mv /opt/spark-3.3.5-SNAPSHOT-bin-3.3.2 /opt/spark && \
    # link spark jars to hive lib
    ln -s /opt/spark/jars/spark-kubernetes_2.12-3.3.0.jar /opt/hive/lib/

EXPOSE 10000

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /root/.kube
COPY config/kube-config /root/.kube/config
ADD bin/startHiveServer2.sh ./
# Define default command.
CMD chmod +x ./*.sh && ./startHiveServer2.sh