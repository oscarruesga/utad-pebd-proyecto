
# DOCKER STACK

This docker compose stack include:

## Stack spark

A `debian:jessie` based [Spark](http://spark.apache.org) container. Use it in a standalone cluster with the accompanying `docker-spark.yml` to launc

To create a standalone cluster with [docker-compose](http://docs.docker.com/compose) up the machines in this order: 

1. Spark Master
```
$ docker-compose --file "compose-spark.yml" up -d master
```
2. Spark Worker
```
$ docker-compose --file "compose-spark.yml" up -d worker
```
To see the logs of Spark docker stack: 
```
$ docker-compose --file "compose-spark.yml" logs
```
To wakeup more workers: 
```
$ docker-compose --file "compose-spark.yml" scale worker=2
```
This set a 2 worker node to the standalone spark cluster.

Once the stack is up you can access to the following endpoints:

Spark Web UI:[http://localhost:8080] (http://localhost/)


##  Stack Elasticsearch/Kibana

This stack get up a [Elasticsearch](https://www.elastic.co/products/elasticsearch) cluster with a master node and four slave nodes. [Kibana](https://www.elastic.co/products/kibana) service also rises with this stack to generate visualizations from elasticsearch. This stack include [Marvel](https://www.elastic.co/products/marvel) to monitoring the health and indices of elasticsearch.
```
$ docker-compose up -d
```

To see the logs of Spark docker stack: 
```
$ docker-compose logs
```

Once the stack is up you can access to the following endpoints:

Elastisearch REST endpoint: [http://localhost:9200](http://localhost:9200)
Kibana:[http://localhost:5601] (http://localhost:5601)
Marvel:[http://localhost:5601/apps/marvel] (http://localhost:5601/apps/marvel)