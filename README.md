# Test no depedencies

This is an attempt to prove an application can be written in java without any dependency

Because no dependency is involved, build tools such as maven are not really required and excluded from this demo.
For packaging, docker is used.

- build & run: `docker build . -t test && docker run --rm -it -p 8080:8080 test` && http://localhost:8080/
