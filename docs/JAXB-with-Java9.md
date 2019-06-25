# JAXB With Java 9+


Let's say we have a class an application that runs in Java 8 or earlier, which writes a book object to an OutputStream. For example:

```java
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Book {

    private String isbn;
    private String title;

    Book() {
        // Jax-b requires a not private no-arg default constructor.
    }

    public Book(final String isbn, final String title) {
        this.isbn = isbn;
        this.title = title;
    }

    // Omitted getters and setters
}
```

```java
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class App {

    public static void main(String[] args) throws JAXBException {
        Book book = new Book("978-3-16-148410-0", "Jaxb after Java 8");

        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        marshaller.marshal(book, System.out);
    }
}

```

The previous code uses the JAXB API that is responsible for transforming Java objects into XML and vice versa.

This specification was released in 2006 as part of the Java EE 5 specification. In 2009 it started to be embedded with JRE with the java SE 6.

With Java 8, without any additional JAR, the example code would be compiled and executed successfully.


## JAXB with Java SE 9+


1. If we just run the previous code with java SE 9 or higher we will get the following exception:

`java.lang.ClassNotFoundException: com.sun.xml.internal.bind.v2.ContextFactory`

2. If we try to compile the previous code with Java SE 9 or higher, we will get a lots of compile error related with unfound packaged in the currente JDK 

```
package javax.xml.bind.annotation does not exist
cannot find symbol class XmlRootElement
cannot find symbol class JAXBException
```


With java SE 9 or higher we can get similar error with some very popular 3 parts dependencies for example like Hibernate, Jersey or Spring-boot. 

* Hibernate or Hibernate Validator: `java.lang.ClassNotFoundException: javax.xml.bind.JAXBException`
* Jersey: `java.lang.ClassNotFoundException: javax.xml.bind.Unmarshaller`
* Spring Boot: `java.lang.ClassNotFoundException: javax.xml.bind.ValidationException`


One of the news main features of Java 9 was the Java Platform Module System (JPMS), a system of modules that solves several problems of the Classpath and allows a level of encapsulation above the packages.

The JDK itself was divided into modules. The java.base module contains the most basic packages, such as java.lang, java.util, java.io, and java.time. There are also modules for the various Java APIs: JDBC (java.sql), JNDI (java.naming), Swing (java.desktop), JMX (java.management), among others.

All the modularized application has only the classes of the java.base module available by default. If we need to use JDBC or Swing, for example, the corresponding modules must be explicitly declared as dependencies in the module-info.java file. Library JARs must be placed in the Module Path.

To allow smoother migration, we can still use the old Class Path with JPMS. In this way, we can run classes compiled with Java 8 (or earlier) and develop a non-modularized application.


_The Class Path content in JPMS is also called the unnamed module, the "unnamed module"._

When we use Class Path with the modularized JDK from Java 9 onwards, which classes are available?

If it were just those of the java.base module, then many applications would not run correctly.
For this reason, I have created a Java SE aggregator module that leaves the Class Path contents available to the modules

java.base
java.sql
java.desktop
java.management
... 

The main goal is, Java SE should contain most of the libraries available in earlier versions of java. But not the JAXB.


## JAXB deprecated in Java 9+

The [JEP 320](http://openjdk.java.net/jeps/320) intends to remove from JRE APIs such as JAXB, JAX-WS, JTA, JAF, CORBA and a few others.
According the proposal, they are Java EE APIs that have third-party implementations and can be used as libraries. So they would not need to be in the JRE.
Theses Libraries were removed with Java 11.

* java.xml.ws (JAX-WS, plus the related technologies SAAJ and Web Services Metadata)
* java.xml.bind (JAXB)
* java.activation (JAF)
* java.xml.ws.annotation (Common Annotations)
* java.corba (CORBA)
* java.transaction (JTA)

...

The previous modules have embedded in java 9 and 10 anotted as @Deprecated, but they are not available in Java 11. 

The Java EE modules are not part of the Java SE aggregator module, so they are not available in classpath on Java 9, 10, or in higher versions.

To use the Java EE modules in an SE environment we can temporarily:

With Java 9: Is possible to add thoses modules to the classpath:

If we need all the Java EE Api that are deprecated we can add the module java.se.ee. that aggregator all the above six modules.

```
javac --add-modules java.se.ee App.java
```

If we only need the JAXB:

```
javac --add-modules java.xml.bind App.java
```

### If we are using maven it is necessary to pass these parameters

```xml
<plugin>
  <groupid>org.apache.maven.plugins</groupid>
  <artifactid>maven-compiler-plugin</artifactid>
  <version>3.8.1</version>
  <configuration>
    <compilerargs>
      <arg>--add-modules</arg>
      <arg>java.xml.bind</arg>
    </compilerargs>
  </configuration>
</plugin>
```

### Run Tomcat:

If we are running Tomcat and we are getting some of the errors mentioned above. We can add modules without changes to the application or server. Just define the JAVA_OPTS environment variable.

```
export JAVA_OPTS="--add-modules java.xml.bind"
```

With Jetty the solution is similar:

```
export JAVA_OPTIONS="--add-modules java.xml.bind"
```


## The definitive solution

The [JEP 320](http://openjdk.java.net/jeps/320), recommends that applications include Class JARs in those APIs and some implementation.

```xml
    <dependency>
        <groupId>javax.activation</groupId>
        <artifactId>activation</artifactId>
        <version>1.1.1</version>
    </dependency>
    <dependency>
        <groupId>javax.xml.bind</groupId>
        <artifactId>jaxb-api</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>com.sun.xml.bind</groupId>
        <artifactId>jaxb-core</artifactId>
        <version>2.3.0</version>
    </dependency>
    <dependency>
        <groupId>com.sun.xml.bind</groupId>
        <artifactId>jaxb-impl</artifactId>
        <version>2.3.0</version>
    </dependency>
```