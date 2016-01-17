FROM debian:jessie

RUN apt-get update \
  && apt-get install -y curl net-tools unzip python \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

# JAVA
ENV JAVA_HOME /usr/jdk1.8.0_31
ENV PATH $PATH:$JAVA_HOME/bin
RUN curl -sL --retry 3 --insecure \
  --header "Cookie: oraclelicense=accept-securebackup-cookie;" \
  "http://download.oracle.com/otn-pub/java/jdk/8u31-b13/server-jre-8u31-linux-x64.tar.gz" \
  | gunzip \
  | tar x -C /usr/ \
  && ln -s $JAVA_HOME /usr/java \
  && rm -rf $JAVA_HOME/man

# SPARK
ENV SPARK_VERSION 1.6.0
ENV HADOOP_VERSION 2.6
ENV SPARK_PACKAGE spark-$SPARK_VERSION-bin-hadoop$HADOOP_VERSION
ENV SPARK_HOME /usr/$SPARK_PACKAGE
ENV PATH $PATH:$SPARK_HOME/bin
RUN curl -sL --retry 3 \
  "http://d3kbcqa49mib13.cloudfront.net/$SPARK_PACKAGE.tgz" \
  | gunzip \
  | tar x -C /usr/ \
  && ln -s $SPARK_HOME /usr/spark

# DEPENDENCIES

COPY ./jar-lib/meetup-rsvps-spark-es-writer-1.0.jar $SPARK_HOME/lib/meetup-rsvps-spark-es-writer-1.0.jar
COPY ./jar-lib/meetup-rsvps-spark-es-writer-1.0.jar $SPARK_HOME/lib/meetup-rsvps-spark-es-writer-1.0-jar-with-dependencies.jar

# CMD /usr/spark/bin/spark-class org.apache.spark.deploy.master.Master
