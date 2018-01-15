# ELK
ELK consists of three independent products:

•	**Elasticsearch** is a distributed, JSON-based search and analytics engine, that allows us to  store the data and search.

•	**Logstash** is a server-side data processing pipeline that gather data, transforms it, and then sends it to Elasticsearch.

•	**Kibana** visualize our Elasticsearch data and navigate the Elastic Stack.

![Elk stack](elk/14-elastic-stack-1.png)

**Resource consumption:**

![Resource consumption](elk/ELK.png)

#JBoss configuration:

**1. Module installation**

 Create module.xml in JBOSS_HOME\modules\org\jboss\logmanager\ext\main.
 
 module.xml:
 ```xml
         <?xml version='1.0' encoding='UTF-8'?>
         
         <module xmlns="urn:jboss:module:1.1" name="org.jboss.logmanager.ext">
         
             <resources>
                 <resource-root path="jboss-logmanager-ext-1.0.0.Alpha3.jar"/>
             </resources>
         
             <dependencies>
                 <module name="org.jboss.logmanager"/>
                 <module name="javax.json.api"/>
                 <module name="javax.xml.stream.api"/>
             </dependencies>
         </module>
   ```
 Add jboss-logmanager-ext-1.0.0.Alpha3.jar in JBOSS_HOME\modules\org\jboss\logmanager\ext\main.
   
**Created module**

![module](elk/moduleImage.png)

**2. Configuration standalone.xml**

  Add the logstash formatter
  ```xml
        <formatter name="logstash">
            <custom-formatter module="org.jboss.logmanager.ext" class="org.jboss.logmanager.ext.formatters.LogstashFormatter"/>
        </formatter>
  ```
  
  Add a socket-handler using the logstash formatter. Replace the hostname and port to the values needed for your logstash install
  ```xml
          <custom-handler name="logstash-handler" class="org.jboss.logmanager.ext.handlers.SocketHandler" module="org.jboss.logmanager.ext">
              <formatter>
                   <named-formatter name="logstash"/>
              </formatter>
                   <properties>
                       <property name="hostname" value="localhost"/>
                       <property name="port" value="4560"/>
                   </properties>
          </custom-handler>
   ```
   Add the new handler to the root-logger
   ```xml
   <handler name="logstash-handler"/>
   ```
#Running ELK container
1.After that, build docker container from our elk folder.

```docker build -t {your image name}```

2.Run our elk container and configure ports.

 ```docker run -it --rm --name {your container name} -p 4560:4560 -p 9200:9200 -p 5601:5601 {your image name}```
