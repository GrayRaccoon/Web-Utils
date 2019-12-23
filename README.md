# Web-Utils
A Simple Spring Web Utils Maven Project for building Spring Cloud based MicroServices.


[![Build Status](https://gitlab.com/GrayRaccoon/grayraccoon.gitlab.io/badges/master/pipeline.svg)](
https://gitlab.com/herychemo/Web-Utils)
[![Release](https://jitpack.io/v/com.grayraccoon/Web-Utils.svg)](
https://jitpack.io/#com.grayraccoon/Web-Utils)


### Artifacts
* Web-Utils: _Spring Web Commons Configuration library._
* Web-Utils-Test: _Spring Web Commons Test Utilities library._
* Cloud-Svc-Parent: _Spring Cloud Pom Project Parent With Common Dependencies._

### Build Library

```bash
mvn compile
``` 

### Test Code and Generate JUnit+Jacoco reports.

```bash
mvn compile verify
```


### Importing into your project

You can directly import Web-Utils as dependency 
or use Cloud-Svc-Parent as parent project 
(which already includes web-utils and web-utils-test).

#### Importing Web-Utils as dependency in pom.xml
```xml
<project>

    <properties>
        <web-utils-version>0.2.6-SNAPSHOT</web-utils-version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.grayraccoon.Web-Utils</groupId>
            <artifactId>web-utils</artifactId>
            <version>${web-utils-version}</version>
        </dependency>

        <dependency>
            <groupId>com.grayraccoon.Web-Utils</groupId>
            <artifactId>web-utils-test</artifactId>
            <version>${web-utils-version}</version>
        </dependency>
    </dependencies>
    
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <name>JitPack</name>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</project>
```

#### Use Cloud-Svc-Parent as parent project in pom.xml
```xml
<project>

    <parent>
        <groupId>com.grayraccoon.Web-Utils</groupId>
        <artifactId>cloud-svc-parent</artifactId>
        <version>0.2.6-SNAPSHOT</version>
        <relativePath/>
    </parent>
    
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <name>JitPack</name>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
</project>
```

Adding Cloud-Svc-Parent as parent project will 
add Web-Utils and Web-Utils-test dependencies to your project
but also will add some common dependencies for Spring Cloud projects.

It works for me since I use those dependencies in my cloud service projects.
Use it if you don't have any problem on having 
those dependencies in your classpath, 
otherwise import only web-utils dependency.



