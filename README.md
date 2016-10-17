# aws-common
AWS library for building cloud native services that leverage AWS services using Spring Boot

Installing the library
clone the repository using following command: 
* git clone https://github.com/rprakashg/aws-common.git

switch to aws-common directory and run command below to install in local maven repository
* mvn clean install

Add snippet below inside the dependencies element within your POM file
```xml
<dependency>
  <groupId>com.rprakashg.cloud.aws.common</groupId>
  <artifactId>aws-common</artifactId>
  <version>1.0-SNAPSHOT</version>
</dependency>
```

## Using AWS SQS as generic repository for persisting Java POJOS in your Spring Boot Application
Add snippet below to your application.yml file.
```yml
aws:
  region: "{specify}"
  credentials:
    keyId: "{specify}"
    secret: "{specify}"
```
Add @ComponentScan annotation as below to your configuration class so spring will automatically locate beans in the package "com.rprakashg.cloud.aws.common"
```java
@ComponentScan("com.rprakashg.cloud.aws.common")
```
Add a public method that returns SqSRepository<T> and annotate this method with @Bean annotation, see sample below
```java
public class ContactSqsMesssage extends SqsMessageBase {
  private String name;
  private String email;
  
  public String getName(){
    return this.name;
  }
  public void setName(String name){
    this.name = name;
  }
  public String getEmail() {
    return this.email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
}

@Configuration
@ComponentScan("com.rprakashg.cloud.aws.common")
public class AppConfig {
  @Bean
  public SqsRepository<ContactSqsMesssage> getContactsRepository(){
    SqsRepository<ContactSqsMesssage> repository = new SqsRepositoryImpl<>(ContactSqsMesssage.class, "contacts", 200);
    repository.ensureExists();
    return repository;
  }
}

```
At this point you can pretty much do dependency injection of this repository into @Service or any other components of your Spring application like below
and not have to worry about how to persist the entity to an SQS queue. Generic repository implementation will handle this under the hood for you freeing
developers to focus on building the service.
```java
@Autowire
private SqsRepository<ContactSqsMesssage> contactsSqsRepository;
```
## Using S3 as generic repository for persisting Java POJOs and Files in your Spring Boot application

