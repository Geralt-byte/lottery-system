# 基础镜像
FROM openjdk:8-jre-slim

# 作者
MAINTAINER mlei

# 配置
ENV PARAMS=""

# 声明暴露端口
EXPOSE 8091

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD big-market-app.jar /big-market-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /big-market-app.jar $PARAMS"]