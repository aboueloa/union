# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY homeservices/pom.xml .
RUN mvn dependency:go-offline
COPY homeservices/src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
ARG PROFILE=dev
ARG APP_VERSION=1.0-SNAPSHOT

WORKDIR /app
COPY --from=build /build/target/back-*.jar /app/

# Extract the JAR version
RUN APP_VERSION=$(ls /app | grep *.jar | awk 'NR==2{split($0,a,"-"); print a[3]}' | awk '{sub(/.jar$/,"")}1')\
    && echo "Building container with BACK v-$version"
EXPOSE 8088

ENV MAILDEV_URL=localhost

ENV ACTIVE_PROFILE=${PROFILE}
ENV JAR_VERSION=${APP_VERSION}

CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL} back-${JAR_VERSION}.jar