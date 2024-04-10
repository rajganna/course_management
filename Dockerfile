ARG BUILD_HOME=/course_management_system

FROM gradle:7.6-jdk AS build

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

RUN gradle clean build

FROM openjdk:17.0.1-jdk-slim AS run

RUN adduser --system --group app-user

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
COPY --from=build --chown=app-user:app-user $APP_HOME/build/libs/course_management_system-0.0.1.jar app.jar

EXPOSE 8080
USER app-user

CMD ["java", "-jar", "app.jar"]
