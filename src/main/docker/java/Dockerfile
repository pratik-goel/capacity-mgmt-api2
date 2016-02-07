FROM java:8
VOLUME /tmp
ADD capacity-mgmt-api-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n","-jar","/app.jar"]