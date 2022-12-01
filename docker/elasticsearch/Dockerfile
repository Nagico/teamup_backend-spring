# Elasticsearch with analysis-ik
FROM docker.elastic.co/elasticsearch/elasticsearch:8.5.2 as elasticsearch

WORKDIR /usr/share/elasticsearch

#安装中文分词插件
RUN ./bin/elasticsearch-plugin install -b https://github.com/medcl/elasticsearch-analysis-ik/releases/download/v8.5.2/elasticsearch-analysis-ik-8.5.2.zip

HEALTHCHECK --interval=60s --timeout=10s --retries=3 CMD curl -f http://localhost:9200/_cluster/health?pretty || exit 1

EXPOSE 9200
EXPOSE 9300