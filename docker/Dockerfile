FROM adoptopenjdk/openjdk11:latest

ENV LANG=C.UTF-8

ENV spring_profiles_active prod
ENV JDBC_DATABASE_URL jdbc://postgres://cjmtabkaszswiv:93070829f9a6e909740135bc86a5a2010e9964d9835af32a2a4b3e367e5a6b01@ec2-23-21-244-254.compute-1.amazonaws.com:5432/dk5aaf6ji1lq2
ENV JDBC_DATABASE_USERNAME cjmtabkaszswiv
ENV JDBC_DATABASE_PASSWORD 93070829f9a6e909740135bc86a5a2010e9964d9835af32a2a4b3e367e5a6b01

RUN mkdir /opt/app

VOLUME /tmp

EXPOSE 8081

ARG JAR_FILE=ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /opt/app/eclinica-security.jar

WORKDIR /opt/app

ENTRYPOINT ["sh", "-c",  "java -jar eclinica-security.jar"]