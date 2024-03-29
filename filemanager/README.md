# FileManager

This is a [Spark](http://sparkjava.com/) microservice which makes use of [Jetty](https://www.eclipse.org/jetty/) 
embedded web server, so it doesn't need an application container like Tomcat in order to
run it. It exposes a REST API to allow the classic CRUD operations on your music collection.  
Since this is a Maven project, you can obtain a fat executable jar just by typing `mvn clean package` in the root of the
project, so that the build process will create a file `target/filemanager.jar`.  
Please remember to check the dependencies updates before deploying it, it's recommended to do it with 
`mvn versions:display-dependency-updates`.  
Avoid running the scanner inside Docker for a very large collection of files, unless you've plenty of RAM to
assign to Docker.

## Scanner

The app uses the Java high-level concurrency API, more specifically with the [ForkJoin framework](https://docs.oracle.com/javase/tutorial/essential/concurrency/forkjoin.html).
It also watches the specified music directory for changes, so that it can sync the MySQL database.

### Caveats

* this app is deliberately intended to be run on a Solid State Disk. There's also a 
single-threaded implementation that can be used forcing a param. Using this code on a hard disk will defeat the fork-join strategy, worsening the overall
performance compared to a sequential scan (remember we're doing a lot of I/O access to the disk).
* if you get the exception `java.io.IOException: User limit of inotify watches reached`, please consider to increase the
inotify limits, for Debian, RedHat, or another similar Linux distribution, run the following in a terminal:
```bash
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
```

### Issues
* Docker Desktop: the watching utility does not work for DELETE events regarding single files ([see GitHub issue](https://github.com/docker/desktop-linux/issues/99#issuecomment-1416803908)). Could be
interesting trying this with Colima.

## Resources

The music files used for tests are freely downloadable from [Open Music Archive](http://www.openmusicarchive.org/) 
(Creative Commons licences and public domain). 