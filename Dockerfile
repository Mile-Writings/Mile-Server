FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./module-api/build/libs/module-api-1.0.0-SNAPSHOT.jar /app/MILE.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "MILE.jar"]
