FROM adoptopenjdk/openjdk15-openj9
WORKDIR /tmp
ADD Main.java /tmp/Main.java
CMD java /tmp/Main.java
