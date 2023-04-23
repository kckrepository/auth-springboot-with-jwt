FROM openjdk:19
ADD target/AuthenticationRestFullWithPostgreeUsingSpringBoot-1.0-SNAPSHOT-exec.jar dani-spring-boot-postgre-jwt.jar
ENTRYPOINT ["java", "-jar", "dani-spring-boot-postgre-jwt.jar"]
EXPOSE 8080