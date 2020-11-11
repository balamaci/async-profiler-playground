## Playground for testing with async-profiler

1. Looking at a simple blocking echo server

## Running the app 
We're using a docker image of async-profiler as a base for jlib maven plugin. 

Start container 
```bash
docker run -it --rm --network host --name tested-app -v /tmp/asyncprofiler:/tmp/asyncprofiler --security-opt seccomp=unconfined tested-app:latest
```

start the profiling
```bash
docker exec -ti tested-app profiler.sh 30 -t -f /tmp/asyncprofiler/cpu.svg 1
```

## What we see

### Profiling the blocking echo Server - Client app 

![Example](https://github.com/jvm-profiling-tools/async-profiler/blob/master/demo/SwingSet2.svg)

Color codes are:
green == Java, yellow == C++, red == user-mode native, orange == kernel

Of interest is **SocketDispatcher.read0** if we look in the JVM [source code](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/classes/sun/nio/ch/SocketDispatcher.java#L79), read0 is just a native method called through JNI, which we can trace to
The native call [SocketDispatcher.c](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/native/libnio/ch/SocketDispatcher.c)
we see it's calling the POSIX **read**
```c
jint n = read(fd, buf, len);
```
Same goes for 


We've also caught a glimpse of the C2 compiler JIT optimizing the classes