FROM openjdk:11-jre-slim as nettools
RUN apt-get update && \
    apt-get install -y net-tools

FROM openjdk:11-jre-slim
RUN useradd -m -u 1000 order

ARG JAR_FILE

RUN  apt-get update && \
     apt-get install -y curl && \
     apt-get clean -y && \
     rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

EXPOSE 9000

COPY --from=nettools /sbin/route /sbin/route

COPY target/${JAR_FILE} /home/order/app.jar

RUN chmod ugo+rx /usr/local/bin/* && \
    chown -R order /home/order

USER order
WORKDIR /home/order

ENTRYPOINT [ "java", "-jar", "app.jar", "--spring.profiles.active=default" ]
