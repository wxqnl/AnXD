# 基础镜像
FROM eclipse-temurin:17
# 作者
MAINTAINER lhz
# 工作目录
WORKDIR /usr/local/java
# 同步docker内部的时间
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 设置时区
ENV TZ=Asia/Shanghai
EXPOSE 8888
# 复制jar包到/user/local/java下
ARG JAR_FILE
ADD ${JAR_FILE} ./daijiaguanli.jar

ENTRYPOINT ["nohup","java","-Dspring.profiles.active=prod","-Dspring.config.location=/usr/local/java/application.yml,/usr/local/java/application-prod.yml","-jar","/usr/local/java/daijiaguanli.jar",">","/usr/local/java/daijiaguanli.log","&>","&"]
