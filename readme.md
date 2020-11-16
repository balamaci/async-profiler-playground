## Playground for testing with async-profiler

1. Looking at a simple blocking echo server

## Running the app 
We're using a docker image of async-profiler as a base for jlib maven plugin from [openjdk-x-dbg-asyncprofiler](https://github.com/petrbouda/openjdk-x-dbg-asyncprofiler)

Start container 
```bash
docker run -it --rm --network host --name tested-app -v /tmp/asyncprofiler:/tmp/asyncprofiler --security-opt seccomp=unconfined tested-app:latest
```

start the profiling
```bash
docker exec -ti tested-app profiler.sh -d 30 -s -t -f /tmp/asyncprofiler/cpu.svg 1
```

## What we see

### Profiling the blocking echo Server - Client app 

![Example](https://raw.githubusercontent.com/balamaci/async-profiler-playground/master/cpu.svg)

Color codes are:
green == Java
yellow == C++
red == user-mode native 
orange == kernel

Of interest is **SocketDispatcher.read0** if we look in the JVM [source code](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/classes/sun/nio/ch/SocketDispatcher.java#L79), read0 is just a native method called through JNI, which we can trace to a .c file.
There is a JNI convention how the native method looks like in the .c file containing package name and class _Java_sun_nio_ch_SocketDispatcher_read0_ 

The native call [SocketDispatcher.c](https://github.com/openjdk/jdk/blob/d7a0fb9ebc898e76207c27166b81630e837a064a/src/java.base/unix/native/libnio/ch/SocketDispatcher.c)
we see it's calling the POSIX **read** https://man7.org/linux/man-pages/man2/read.2.html
```c
jint n = read(fd, buf, len);
```
Same goes for **SocketDispatcher.write**, just that it delegates first to **FileDispatcherImpl.write0** which delegates to native [FileDispatcherImpl.c](https://github.com/openjdk/jdk/blob/7e42642939c0c3b8b872d72890fbb5aab4c3e507/src/java.base/unix/native/libnio/ch/FileDispatcherImpl.c)

Next in line we notice the jump from user space -> kernel space **do_syscall_64** and then some kernel internal calls to read/write from the socket. 



We've also caught a glimpse of the JVM C2 compiler spending some resources doing JIT optimizing(in the left part) as the JVM notices some methods are invoked often.

### Profiling a simple TCP Netty NIO Client - Server

![NIO](https://raw.githubusercontent.com/balamaci/async-profiler-playground/master/netty.svg)

We've implemented a simple TCP [Server]() - Client communication via TCP using Netty framework using NIO.
Under the hood Netty is an event based framework allowing to do non blocking IO. 
We can see a call to [epoll_wait](https://man7.org/linux/man-pages/man2/epoll_wait.2.html). 
By using **EPoll** is capable of getting notified when certain sockets(actually file descriptors) have data waiting to be read from them by periodically polling this info, without having to block indefinitely waiting for data to arrive on a specific socket. 

When we want to write data to the socket, Java NIO makes use of **DirectByteBuffers**. 
DirectByteBuffer are **native memory** locations(outside of JVM heap) which the GC doesn't move - when compacting memory-(it does keep track of them and can dispose of them when they're no longer referenced) and can be written directly into the socket. 

We still see the **_do_syscall_64** which means user->kernel context switch, and this reduction on context switching is improved on the new [io_uring](https://unixism.net/loti/what_is_io_uring.html) approach and we'll take a look in the future.

Next we'll also take a look if we can see anything special with using platform specific Epoll implementation through JNI.

### Profiling a simple TCP Netty EPoll Client - Server
