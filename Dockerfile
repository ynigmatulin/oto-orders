FROM java:jdk
MAINTAINER Anton Weiss <ant.weiss@gmail.com>
ADD http://104.199.107.110/job/build-oto-orders/lastSuccessfulBuild/artifact/build/libs/ws-orders-0.1.0.jar /usr/myapp/ws-orders.jar
WORKDIR /usr/myapp
CMD ["java" , "-jar", "/usr/myapp/ws-orders.jar"]
