# 基础镜像
FROM openjdk:17-jdk

# 作者
MAINTAINER xiaofuge

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD ./target/big-market-app.jar /big-market-app-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /big-market-app-app.jar $PARAMS"]