# Web-Utils
A Simple Spring Web Utils Maven Project for building Spring Cloud based MicroServices.


[![Build Status](https://gitlab.com/GrayRaccoon/grayraccoon.gitlab.io/badges/master/pipeline.svg)](
https://gitlab.com/herychemo/Web-Utils)
[![Release](https://jitpack.io/v/com.grayraccoon/Web-Utils.svg)](
https://jitpack.io/#com.grayraccoon/Web-Utils)


### Artifacts
* __Web-Utils:__
_Spring Web Commons Configuration library._
* __Web-Utils-Test:__
_Spring Web Commons Test Utilities library._
* __Cloud-Svc-Parent-No-Persistence:__
_Spring Cloud Pom Project Parent With Common Dependencies._
* __Cloud-Svc-Parent:__
_Spring Cloud Pom Project Parent With Common Dependencies including Jpa._


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
or use _Cloud-Svc-Parent_ or _Cloud-Svc-Parent-No-Persistence_ as parent project 
(that already include __web-utils__ and __web-utils-test__).

#### Importing Web-Utils as dependency in pom.xml
```xml
<project>
    <properties>
        <web-utils.version>0.2.16-SNAPSHOT</web-utils.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>com.grayraccoon.Web-Utils</groupId>
            <artifactId>web-utils</artifactId>
            <version>${web-utils.version}</version>
        </dependency>

        <dependency>
            <groupId>com.grayraccoon.Web-Utils</groupId>
            <artifactId>web-utils-test</artifactId>
            <version>${web-utils.version}</version>
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

#### Use Cloud-Svc-Parent-No-Persistence as parent project in pom.xml
```xml
<project>

    <parent>
        <groupId>com.grayraccoon.Web-Utils</groupId>
        <artifactId>cloud-svc-parent-no-persistence</artifactId>
        <version>0.2.16-SNAPSHOT</version>
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

#### Use Cloud-Svc-Parent as parent project in pom.xml
```xml
<project>

    <parent>
        <groupId>com.grayraccoon.Web-Utils</groupId>
        <artifactId>cloud-svc-parent</artifactId>
        <version>0.2.16-SNAPSHOT</version>
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


Adding __Cloud-Svc-Parent-No-Persistence__ as parent project will 
add _Web-Utils_ and _Web-Utils-test_ dependencies to your project
but also will add some common dependencies for Spring Cloud projects.
__Cloud-Svc-Parent__ does the same but also imports jpa, flyway, h2. 

It works for me since I use those dependencies in my cloud service projects.
Use it if you don't have any problem on having 
those dependencies in your classpath, 
otherwise import only web-utils dependency.

