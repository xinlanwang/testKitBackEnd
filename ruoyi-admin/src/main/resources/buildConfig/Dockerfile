#依赖jdk8环境
FROM openjdk:8

#对外暴露8085
EXPOSE 8085
#复制server-1.0-SNAPSHOT到docker容器中并命名为app.jar
ADD ruoyi-admin.jar app.jar
#执行命令
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=pro"]
