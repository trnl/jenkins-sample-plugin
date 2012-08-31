#Jenkins Twitter Sample Plugin

A plugin created as a part of Jenkins Training I've given in the EPAM Systems.
Rather basic and will be updated later. 

![Screenshot](http://f.cl.ly/items/0v1z0G1l2n2v43012n0b/Image%202012.08.31%204:57:55%20PM.png)

#Guide

##Prerequisites

Before proceeding with exercise you need to do the following:

- [Java 1.6](http://www.oracle.com/technetwork/java/javase/downloads/jdk6u35-downloads-1836443.html)
- [Maven 2.2.1](http://maven.apache.org/docs/2.2.1/release-notes.html)

### Maven 2.2.1
Maven settings($M2_HOME/conf/settings.xml) needs to be updated with the following values. 

    <settings>
      <pluginGroups>
        <pluginGroup>org.jenkins-ci.tools</pluginGroup>
      </pluginGroups>
      <profiles>
        <!-- Give access to Jenkins plugins -->
        <profile>
          <id>jenkins</id>
          <activation>
            <activeByDefault>true</activeByDefault>
            <!-- change this to false, if you don't like to have it on per default -->
          </activation>
          <repositories>
            <repository>
              <id>repo.jenkins-ci.org</id>
              <url>http://repo.jenkins-ci.org/public/</url>
            </repository>
          </repositories>
          <pluginRepositories>
            <pluginRepository>
              <id>repo.jenkins-ci.org</id>
              <url>http://repo.jenkins-ci.org/public/</url>
            </pluginRepository>
          </pluginRepositories>
        </profile>
      </profiles>
    </settings>

##Step 1
Create a plugin with Maven

    mvn -up hpi:create
  
You will be asked to provide **groupId** and **artifactId**.
Your newly created plugin will be located in the folder with name similar to **artifactId**.

##Step 2
Start a jenkins with plugin.

There are an issues with Maven Central, so you need to use the workaround when 1st time executing **hpi:run**:

    mvn -Dmaven.wagon.provider.http=httpclient hpi:run
    
Next time you can execute command without additional params:

    mvn hpi:run
    
##Step3
Create a new application in http://dev.twitter.com/. You will need the following:

- Consumer key
- Consumer secret
- Access token
- Access token secret

##Step 4
Explore the code