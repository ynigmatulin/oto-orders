FROM java:jdk

ADD ./build/libs/ws-orders-0.1.0.jar /usr/myapp/ws-orders.jar
WORKDIR /usr/myapp
CMD ["java" , "-jar", "/usr/myapp/ws-orders.jar"]
