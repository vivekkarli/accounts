#start with a base image containing java runtime
FROM openjdk:21-jdk

#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="idealbank.com"

#add the application's jar to the imagedo
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#run the application
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar"]

