FROM openjdk:8-jdk
COPY . /
WORKDIR /
RUN ["javac", "HashUtility.java"] 
# ENTRYPOINT ["--window", "--thread"]
CMD ["java", "HashUtility"]
