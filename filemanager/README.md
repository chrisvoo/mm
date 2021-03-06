# FileManager

This is a [Spark](http://sparkjava.com/) microservice which makes use of [Jetty](https://www.eclipse.org/jetty/) 
embedded web server, so it doesn't need an application container like Tomcat in order to
run it. It exposes a REST API to allow the classic CRUD operations on your music collection.  
Since this is a Maven project, you can obtain a fat executable jar just by typing `mvn clean package` in the root of the
project, so that the build process will create a file `target/filemanager.jar`.  
Please remember to check the dependencies updates before deploying it, it's recommended to do it with 
`mvn versions:display-dependency-updates`.

## Scanner

The app uses the Java high-level concurrency API, more specifically with the [ForkJoin framework](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html).

### Caveats

This app is deliberately intended to be run on a Solid State Disk and as such, there's no 
single-threaded implementation. Using this code on a hard disk will defeat the fork-join strategy, worsening the overall
performance compared to a sequential scan (remember we're doing a lot of I/O access to the disk).

## Resources

The music files used for tests are freely downloadable from [Open Music Archive](http://www.openmusicarchive.org/) 
(Creative Commons licences and public domain). 