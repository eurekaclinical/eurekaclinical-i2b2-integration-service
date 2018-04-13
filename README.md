# Eureka! Clinical I2b2 Integration Service
[Atlanta Clinical and Translational Science Institute (ACTSI)](http://www.actsi.org), [Emory University](http://www.emory.edu), Atlanta, GA

## What does it do?
It provides RESTful APIs for managing i2b2 users and accessing data.

## Version history
Latest release: [![Latest release](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eurekaclinical-i2b2-integration-service/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eurekaclinical/eurekaclinical-i2b2-integration-service)

### Version 1.1
The properties in the application.properties were named in a non-standard way in version 1.0,
starting with eurekaclinical.i2b2service rather than eurekaclinical.i2b2integrationservice.
Version 1.1 added standard property names. It responds to either, and it prioritizes the original
non-standard names if both are present. The non-standard property names are deprecated and will be 
removed in a future release.

### Version 1.0
Initial version.

## Build requirements
* [Oracle Java JDK 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Maven 3.2.5 or greater](https://maven.apache.org)

## Runtime requirements
* [Oracle Java JRE 8](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* [Tomcat 7](https://tomcat.apache.org)
* One of the following relational databases:
  * [Oracle](https://www.oracle.com/database/index.html) 11g or greater
  * [PostgreSQL](https://www.postgresql.org) 9.1 or greater
  * [H2](http://h2database.com) 1.4.193 or greater (for testing)

## REST endpoints

### `/api/protected/users`
Manages registering a user with this service for authorization purposes.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### User object
Properties:
* `id`: unique number identifying the user (set by the server on object creation, and required thereafter).
* `username`: required username string.
* `roles`: array of numerical ids of roles.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/users`
Returns an array of all User objects. Requires the `admin` role.

##### GET `/api/protected/users/{id}`
Returns a specified User object by the value of its id property, which is unique. Requires the `admin` role to return any user record. Otherwise, it will only return the user's own record.

##### GET `/api/protected/users/byname/{username}`
Returns a specified User object by its username, which is unique. Requires the `admin` role to return any user record. Otherwise, it will only return the user's own record.

##### GET `/api/protected/users/me`
Returns the User object for the currently authenticated user.

##### POST `/api/protected/users/`
Creates a new user. The User object is passed in as the body of the request. Returns the URI of the created User object. Requires the `admin` role.

##### PUT `/api/protected/users/{id}`
Updates the user object with the specified id. The User object is passed in as the body of the request. Requires the `admin` role.

##### GET or POST `/api/protected/users/auto`
Will auto-register the user with this service if there is a user template with `autoAuthorize` equal to `true`, and if the template's `criteria` property is either empty, or the user's CAS attributes satisfy the specified criteria. If auto-authorization fails, a 403 status code is returned.

### `/api/protected/roles`
Manages roles for this service. It is read-only.

#### Role-based authorization
No.

#### Requires successful authentication
Yes

#### Role object
Properties:
* `id`: unique number identifying the role.
* `name`: the role's name string.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/roles`
Returns an array of all User objects.

##### GET `/api/protected/roles/{id}`
Returns a specified Role object by the value of its id property, which is unique.

##### GET `/api/protected/roles/byname/{name}`
Returns a specified Role object by the value of its name property, which is unique.

### `/api/protected/i2b2users`
Manages i2b2 user accounts.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### I2b2User object

Properties:
* `username`: the user's unique username.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/i2b2domains/{id}/i2b2users/{username}`
Returns the specified i2b2 user in the i2b2 domain with the specified numerical id.

##### POST `/api/protected/i2b2users/auto`
Automatically creates user and role records in i2b2's PM cell, if the user is authorized for eurekaclinical-i2b2-integration and is a member of a group that has an associated i2b2 project. The request body should be empty. If successful, returns the URI of the created I2b2User object. If not, returns a 403 status code.

### `/api/protected/i2b2roles`
The allowed roles in i2b2. Is read-only.

#### Role-based authorization
No

#### Requires successful authentication
Yes

#### I2b2Role object

Properties:
* `id`: unique number identifying the role.
* `name`: unique name string identifying the role.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/i2b2roles`
Returns an array of all I2b2Role objects.

##### GET `/api/protected/i2b2roles/{id}`
Returns a specified I2b2Role object by the value of its id property, which is unique.

##### GET `/api/protected/i2b2roles/byname/{name}`
Returns a specified I2b2Role object by the value of its name property, which is unique.

### `/api/protected/i2b2domains`
Domains in one or more i2b2 deployments. Is read-only.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### I2b2Domain object

Properties:
* `id`: unique number identifying the i2b2 domain.
* `name`: unique name string identifying the i2b2 domain.
* `proxyUrl`: the URL of the i2b2 web client proxy.
* `redirectHost`: the URL of the i2b2 server.
* `adminUsername`: a username with admin privileges.
* `adminPassword`: that admin user's password.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/i2b2domains`
Returns an array of all I2b2Domain objects. If you do not have the `admin` role, the `adminUsername` and `adminPassword` properties will be `null`.

##### GET `/api/protected/i2b2domains/{id}`
Returns a specified I2b2Domain object by the value of its id property, which is unique. If you do not have the `admin` role, the `adminUsername` and `adminPassword` properties will be `null`.

##### GET `/api/protected/i2b2domains/byname/{name}`
Returns a specified I2b2Domain object by the value of its name property, which is unique. If you do not have the `admin` role, the `adminUsername` and `adminPassword` properties will be `null`.

### `/api/protected/i2b2projects`
Manages i2b2 projects for which users may be assigned during auto-authorization. Is read-only.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### I2b2Project object

Properties:
* `id`: unique number identifying the i2b2 project.
* `name`: unique name string identifying the i2b2 project.
* `groups`: array of group unique id numbers for which members will be authorized for this project during auto-authorization.
* `i2b2Domain`: unique id number of the i2b2 project's domain.

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/i2b2projects`
Returns an array of all I2b2Project objects. Must have the `admin` role.

### `/api/protected/groups`
Manages assignment of users to groups. The groups control which i2b2 projects a user may get assigned to, and the i2b2 roles a user will be assigned during auto-authorization.

#### Role-based authorization
Call-dependent

#### Requires successful authentication
Yes

#### Group object

Properties:
* `id`: unique number identifying the user (set by the server on object creation, and required thereafter).
* `name`: unique name for the group.
* `i2b2Roles`: array of unique identifier numbers for I2b2Role objects.
* `i2b2Projects`: array of unique identifier numbers for I2b2Project objects.

### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/groups`
Returns an array of all Group objects. Requires the `admin` role.

##### GET `/api/protected/groups/{id}`
Returns a specified Group object by the value of its id property, which is unique.

##### GET `/api/protected/groups/byname/{name}`
Returns a specified Group object by its name property, which is unique.

##### POST `/api/protected/groups/`
Creates a new group. The Group object is passed in as the body of the request. Returns the URI of the created Group object.

##### PUT `/api/protected/groups/{id}`
Updates the Group object with the specified id. The Group object is passed in as the body of the request.

### `/api/protected/usertemplates`
Manages templates for auto-registering users and assigning authorization.

#### Role-based authorization
Requires the `admin` role.

#### Requires successful authentication
Yes

#### UserTemplate object
Properties:
* `id`: unique number identifying the template (set by the server on object creation, and required thereafter).
* `name`: required template name string.
* `roles`: array of numerical ids of the Roles to assign users.
* `groups`: array of numerical ids of the Groups to assign users.
* `autoAuthorize`: whether this template should be applied when auto-authorization is requested. There should only be one template that has a value of `true` for this property.
* `criteria`: a [Freemarker](http://freemarker.org) expression that evaluates the user's CAS attributes to return `true` if the user should be auto-authorized and `false` if not. For example, the expression `type != "student" && organization == "Hanford University"` specifies that the user can be auto-authorized only if the person's `type` attribute does not have the value "student" and the person's `organization` attribute has the value "Hanford University".

#### Calls
All calls use standard names, return values and status codes as specified in the [Eureka! Clinical microservice specification](https://github.com/eurekaclinical/dev-wiki/wiki/Eureka%21-Clinical-microservice-specification)

##### GET `/api/protected/usertemplates`
Returns an array of all UserTemplate objects.

##### GET `/api/protected/usertemplates/{id}`
Returns a specified UserTemplate object by the value of its id property, which is unique.

##### POST `/api/protected/usertemplates/`
Creates a new template. The UserTemplate object is passed in as the body of the request. Returns the URI of the created UserTemplate object.

##### PUT `/api/protected/usertemplates/{id}`
Updates the UserTemplate object with the specified id. The UserTemplate object is passed in as the body of the request.

## Building it
The project uses the maven build tool. Typically, you build it by invoking `mvn clean install` at the command line. For simple file changes, not additions or deletions, you can usually use `mvn install`. See https://github.com/eurekaclinical/dev-wiki/wiki/Building-Eureka!-Clinical-projects for more details.

## Performing system tests
You can run this project in an embedded tomcat by executing `mvn tomcat7:run -Ptomcat` after you have built it. It will be accessible in your web browser at https://localhost:8443/eurekaclinical-i2b2-integration-service/. Your username will be `superuser`.

## Installation
### Database schema creation
A [Liquibase](http://www.liquibase.org) changelog is provided in `src/main/resources/dbmigration/` for creating the schema and objects. [Liquibase 3.3 or greater](http://www.liquibase.org/download/index.html) is required.

Perform the following steps:
1) Create a schema in your database and a user account for accessing that schema.
2) Get a JDBC driver for your database and put it the liquibase lib directory.
3) Run the following:
```
./liquibase \
      --driver=JDBC_DRIVER_CLASS_NAME \
      --classpath=/path/to/jdbcdriver.jar:/path/to/eurekaclinical-i2b2-integration-service.war \
      --changeLogFile=dbmigration/changelog-master.xml \
      --url="JDBC_CONNECTION_URL" \
      --username=DB_USER \
      --password=DB_PASS \
      update
```
4) Add the following Resource tag to Tomcat's `context.xml` file:
```
<Context>
...
    <Resource name="jdbc/EurekaClinicalI2b2IntegrationService" auth="Container"
            type="javax.sql.DataSource"
            driverClassName="JDBC_DRIVER_CLASS_NAME"
            factory="org.apache.tomcat.jdbc.pool.DataSourceFactory"
            url="JDBC_CONNECTION_URL"
            username="DB_USER" password="DB_PASS"
            initialSize="3" maxActive="20" maxIdle="3" minIdle="1"
            maxWait="-1" validationQuery="SELECT 1" testOnBorrow="true"/>
...
</Context>
```

The validation query above is suitable for PostgreSQL. For Oracle and H2, use
`SELECT 1 FROM DUAL`.

### Configuration
This service is configured using a properties file located at `/etc/ec-i2b2-integration/application.properties`. It supports the following properties:
* `eurekaclinical.i2b2integrationservice.callbackserver`: https://hostname:port
* `eurekaclinical.i2b2integrationservice.url`: https://hostname:port/eurekaclinical-i2b2-integration-service
* `cas.url`: https://hostname.of.casserver:port/cas-server

A Tomcat restart is required to detect any changes to the configuration file.

### WAR installation
1) Stop Tomcat.
2) Remove any old copies of the unpacked war from Tomcat's webapps directory.
3) Copy the warfile into the tomcat webapps directory, renaming it to remove the version. For example, rename `eurekaclinical-i2b2-integration-webapp-1.0.war` to `eurekaclinical-i2b2-integration-webapp.war`.
4) Start Tomcat.

## Maven dependency
```
<dependency>
    <groupId>org.eurekaclinical</groupId>
    <artifactId>eurekaclinical-i2b2-integration-service</artifactId>
    <version>version</version>
</dependency>
```

## Developer documentation
* [Javadoc for latest development release](http://javadoc.io/doc/org.eurekaclinical/eurekaclinical-i2b2-integration-service) [![Javadocs](http://javadoc.io/badge/org.eurekaclinical/eurekaclinical-i2b2-integration-service.svg)](http://javadoc.io/doc/org.eurekaclinical/eurekaclinical-i2b2-integration-service)

## Getting help
Feel free to contact us at help@eurekaclinical.org.

