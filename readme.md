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

![Example](https://raw.githubusercontent.com/balamaci/async-profiler-playground/master/cpu.svg)

Color codes are:
green == Java, yellow == C++, red == user-mode native, orange == kernel

Of interest is **SocketDispatcher.read0** if we look in the JVM [source code](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/classes/sun/nio/ch/SocketDispatcher.java#L79), read0 is just a native method called through JNI, which we can trace to
There is a JNI convention how the native method looks like in the .c file containing package name and class _Java_sun_nio_ch_SocketDispatcher_read0_ 

The native call [SocketDispatcher.c](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/native/libnio/ch/SocketDispatcher.c)
we see it's calling the POSIX **read**
```c
jint n = read(fd, buf, len);
```
Same goes for **SocketDispatcher.write**, just that it delegates first to **FileDispatcherImpl.write0** which delegates to native [FileDispatcherImpl.c](https://github.com/openjdk/jdk/blob/7e42642939c0c3b8b872d72890fbb5aab4c3e507/src/java.base/unix/native/libnio/ch/FileDispatcherImpl.c)

Next in line we notice the jump from user space -> kernel space **do_syscall_64** and then some kernel internal calls to read/write from the socket. 

We've also caught a glimpse of the JVM C2 compiler spending some resources doing JIT optimizing(in the left part) as the JVM notices some methods are invoked often.