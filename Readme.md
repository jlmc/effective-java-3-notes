# Effective Java edition 3

This project contains the code resulting from my reading of the book Effective Java edition 3 

## Summary 

+ Lambdas 
+ Streams 
+ Optionals
+ Default methods in interfaces
+ try-with-resources

+ Factory methods (1)
+ try-with-resources (9)
+ equals (10)
+ hashCode (11)
+ toString (12)
+ Comparable (14)
+ minimize mutability (17)
+ Enums (34)



## Factory methods (1)

Prefer factory methods instead of constructors
  
Advantage:
  
  - We can centralize validations in the factory method. If we think that most of the code bug happens due to data errors, when the instance enters / becomes in an inconsistent state. Then using factory methods we can validate the data input just in the input before any intents.

  - All parameter values must be validated by throwing a `java.lang.IllegalArgumentException` if they are wrong.
  
  - Factory method can have suggestive names (of, valueOf ...) 
  
  - Factory methods may not return a new instance whenever they are instantiated (`flyweight design pattern`). If there are any very common ones to use in our model, we can cache this instance. with this we even have the possibility to use the `== operator`. A good example of this strategy is the `BigDecimal` class that caches numbers between -127 and 127.
  
  - Using Factory methods we can instantiate any instance of the class or its descendant. It can be an agnostic mechanism. **In fact this turns out to be the biggest advantages as we can not do such thing using constructors**.
  
  
 # Minimize mutability (17)
 
 - If we use Value Objects classes we can dramatically increase the level of immutability of the project.
 
 # equals (10) and hashCode (11)
 
 - We need to implement good equals and hashCode methods. This allows you to take advantage of the Collection and Map implementations (`HashSet` and `HashMap`).
 
 - If the field is a primitive, then we can simply use the `== operator` to dispense with the methods `java.util.Objects` class that would make use of the autoBoxing and unboxing mechanisms in this case by unnecessarily resource gaming. So it's a form of optimization for free.
 
 - In the case of hashCode generation, it is possible to use the static method `Objects.hash`, although this is a dirty solution, especially if we are using primitive types. It is dirty because the `java.util.Objects#hash` method requires creating an `Array` whenever it is invoked and this of course has cost collateral.
 
 - The calculation of the hashCode can be an unnecessary processing, especially if we are using Value Objects classes, in other words, in these cases we can use hashCode value caching since the instance is immutable. (initialization hasCode in EAGLE way).

 - The order of the hashCode property must be the same as that used in the equals method.
 
 ```
 // The worst possible legal hashCode implementation - never use!
 @Override public int hashCode() { return 42; }
 ```
 
 ```
 // One-line hashCode method - mediocre performance
 @Override public int hashCode() {
 return Objects.hash(lineNum, prefix, areaCode);
 }
 ```
 
 ## Comparable (14)
 
 - If a class has a natural Order, then the Comparable interface must be implemented. 
 
 ## Enums (34)
 
 - Enums are the simplest and most effective way to create singleton.
 - It allow us to implement as many interfaces as we want and need.
 - The only disadvantage is that it is not possible to extend a class abstract.
 
 
## Functional Interfaces

- Favor strategy over Template Method

- We should use the functional Interfaces provided by the JDK instead of creating our own functional interfaces, because JDK in most cases already provides us good match. If we create new FunctionalInterfaces the other developers will have to undertands them as well, so, extra work.

- As mais usadas e mais conhecidas functional interfaces da JDK são:
    
 - `UnaryOperator<T>` : extends Function where the input type is the same of the return type
 - `BinaryOperator<T>` : extends Function with 2 parameters of T same type and return type T
 - `BiFunction<T, U, R>` : extends Function with 2 parameters of T and U types and return type R
 - `Predicate<T>` : Methods that return a boolean and take was parameter one object 
 - `Function<T, R>` : Transform a parameter of Type T in some other object of type R
 - `Supplier<T>` : Take no arguments and return a object o type T, basically is factory 
 - `Consumer<T>` : Take a object of type T and return void, do some job with the parameter



## Optional

- The major disadvantages of optional is that it is not Serializable, so that we can serialize the value of optional is necessary to use a proxy properties

## keyword var (java 10) when should we use it

- should be used in controlled scopes. 
- should not be used as identifiers of return values of methods.

## Notes:

- For functional programing with check exceptions we can use the library: 
  - [vavr](http://www.vavr.io)

- To properly generate builders, equals, hashCode and others stuff, we can use one of the libraries:: 
    - [goole auto](https://github.com/google/auto)
    - [immutables](https://immutables.github.io)
    
    
    
