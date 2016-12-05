FROM java:jdk
MAINTAINER Anton Weiss <ant.weiss@gmail.com>
ADD http://35.187.0.172/job/build-oto-orders/lastSuccessfulBuild/artifact/build/libs/ws-orders-0.1.0.jar /usr/myapp/ws-orders.jar
WORKDIR /usr/myapp
CMD ["java" , "-jar", "/usr/myapp/ws-orders.jar"]
