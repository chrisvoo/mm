# FileManager

This is a [Spark](http://sparkjava.com/) microservice which makes use of [Jetty](https://www.eclipse.org/jetty/) 
embedded web server, so it doesn't need an application container like Tomcat in order to
run it.

## Scanner

The app uses the Java high-level concurrency API, more specifically with the [ForkJoin framework](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html).

### Caveats

This app is deliberately intended to be run on a Solid State Disk and as such, there's no 
single-threaded implementation. Using this code on a hard disk will defeat the forkjoin strategy, worsening the overall
performance compared to a sequential scan (remember we're doing a lot of I/O access to the disk).

## Resources

The music files used for tests are freely downloadable from [Open Music Archive](http://www.openmusicarchive.org/) (Creative Commons licences and public domain).