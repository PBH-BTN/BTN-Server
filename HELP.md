# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.ghostchu.standalone.btn.btn-server' is invalid and this project uses 'com.ghostchu.standalone.btn.btnserver' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.4/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#using.devtools)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#appendix.configuration-metadata.annotation-processor)
* [Docker Compose Support](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#features.docker-compose)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web)
* [Spring Session](https://docs.spring.io/spring-session/reference/)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#web.security)
* [JDBC API](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.sql)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.flyway)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#data.nosql.redis)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#howto.batch)
* [Quartz Scheduler](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#io.quartz)
* [Spring Cache Abstraction](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#io.caching)
* [Spring Shell](https://spring.io/projects/spring-shell)
* [Influx](https://docs.spring.io/spring-boot/docs/3.2.4/reference/htmlsingle/index.html#actuator.metrics.export.influx)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)
* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)

### Docker Compose support
This project contains a Docker Compose file named `compose.yaml`.
In this file, the following services have been defined:

* mariadb: [`mariadb:latest`](https://hub.docker.com/_/mariadb)
* mysql: [`mysql:latest`](https://hub.docker.com/_/mysql)
* redis: [`redis:latest`](https://hub.docker.com/_/redis)

Please review the tags of the used images and set them to the same as you're running in production.

