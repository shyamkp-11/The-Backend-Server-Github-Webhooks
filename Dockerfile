FROM amazoncorretto:21
RUN yum update -y && yum install unzip -y
EXPOSE 8080
# TODO Directly deploying to ebs has lot of pain points. Use dockerhub or some other repository for deployment in future. Use build arguements to pass aws config credentials
COPY --chmod=0755 entrypoint.sh /home/
ENTRYPOINT ["sh", "/home/entrypoint.sh"]
