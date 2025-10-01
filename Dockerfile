FROM amazoncorretto:21

COPY target/githubplayroom.jar githubplayroom.jar

EXPOSE 8082
COPY --chmod=0755 entrypoint.sh /home/
ENTRYPOINT ["sh", "/home/entrypoint.sh"]
