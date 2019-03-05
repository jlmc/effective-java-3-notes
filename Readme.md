# Effective Java edition 3

This project contains the code resulting from my reading of the book Effective Java edition 3 

## Summary 

+ Lambdas 
+ Streams 
+ Optionals
+ Default methods in interfaces
+ try-with-resources

### Chapter 2. Creating and Destroying Objects

+ Factory methods (1)
+ try-with-resources (9)
+ equals (10)
+ hashCode (11)
+ toString (12)
+ Comparable (14)
+ minimize mutability (17)
+ Enums (34)


### Chapter 7. Lambdas and Streams

+ prefer lambdas to anonymous classes (42)
+ prefer methods references to lambdas (43)
+ favor standard functional interfaces (44)
+ use streams judiciously (45)
+ use caution when making streams parallel (48)

### Chapter 8. Methods

+ Use Varargs Judiciously (53)

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

- Java has 43 standard functions (functional functions), it should be enough to find a good math that meets our needs.

- The most commonly used and best known functional interfaces of JDK are:
    
 - `UnaryOperator<T>` : extends Function where the input type is the same of the return type
 - `BinaryOperator<T>` : extends Function with 2 parameters of T same type and return type T
 - `BiFunction<T, U, R>` : extends Function with 2 parameters of T and U types and return type R
 - `Predicate<T>` : Methods that return a boolean and take was parameter one object 
 - `Function<T, R>` : Transform a parameter of Type T in some other object of type R
 - `Supplier<T>` : Take no arguments and return a object o type T, basically is factory 
 - `Consumer<T>` : Take a object of type T and return void, do some job with the parameter

| Interface          | Function Signature         | Example               |
| ------------------ |:-------------------------- |:----------------------|
| UnaryOperator<T>   | `T apply(T t)`             | `String::toLowercase` |
| BinaryOperator<T>  | `T apply(T t1, T t2)`      | `BigInteger::add`     |
| Predicate<T>       | `boolean test(T t)`        | `Collection::isEmpty` |
| Function<T,R>      | `R apply(T t)`             | `Arrays::asList`      |
| Supplier<T>        | `T get()`                  | `Instant::now`        |
| Consumer<T>        | v`oid  accept(T t)`        | `System.out::println` |

- Must of the remaining 37 interfaces do the same thing but they do them to the primitive types or at lest two to three of the primitive type instant Long's and doubles. 

   - You could not bother using them, you could like always use the object reference variance but doing that you will be doing lots of auto-boxing and auto auto-unboxing.
   - auto-boxing and auto auto-unboxing is very bad idea, if we do that, we're injecting performance issues into our code.  


## Optional

- The major disadvantages of optional is that it is not Serializable, so that we can serialize the value of optional is necessary to use a proxy properties

## keyword var (java 10) when should we use it

- should be used in controlled scopes. 
- should not be used as identifiers of return values of methods.

## prefer lambdas to anonymous classes (42)

- Lambdas lack names and documentation
 
  - **They should be self-explanatory**
  - **they should not exceed a few lines; one is best**

- If lambda would be long or complex:

  - Extract it to method and use method reference 
  - Or (for enums) use instance-specific class body
  
- Anonymous classes still have a few uses
 
  - Lambdas require functional interfaces
  - Lambdas cannot access themselves; in a lambda, this keyword refers to the enclosing instance
  
## prefer methods references to lambdas (43)

- Lambdas are succinct 

    ```
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        
        // lambdas are succinct
        map.merge("a", 4, (count, incr) -> count + incr);
    
        // but method reference can be more so
        map.merge("a", 4, Integer::sum);
    ```
    
- The more parameters, the bigger the win it is for the method reference

 - but parameters names may provide documentation, the params names can tell you something about what you are doing with them.
 - if you use lambda, choose param name carefully
 
 - Occasionally, lambdas are more succinct, occasionally lambdas are better than method references eg:

    ```
    service.execute(() -> action());
    ``` 

    is preferable to 

    ```
    service.execute(GoshThisClassNameIsHumongours::action);
    ```
    
- Lambdas can beat functions factories too

    ```
    .flatMap(x -> x);
    ```
    is preferable to 
    ```
    .flatMap(Function.identity());
    ```

- All the five types of method reference are preferable to lambdas

| Type               | Example                  | Lambda Equivalent              |
| ------------------ |:------------------------ |:-------------------------------|
| Static             | `Integer::parseInt`      | `str -> Integer.parseInt(str)` |
| Bound              | `Instant.now()::isAfter` | `t -> Instant.now().isAfter(t)`|
| Unbound            | `String::toLowerCase`    | `str -> str.toLowerCase()`     |
| Class Constructor  | `TreeMap<K,V>::new`      | `() -> new TreeMap<k, V>() `   |
| Array Constructor  | `int[]::new`             | `len -> new int[len]`          |


Bound references methods are great, basically you specify an object a object reference at time that you create the lambda and then the parameters on the named method on that object are the parameters.


## favor standard functional interfaces (44)    

 - Before lambdas, Template Methods pattern was common
 
 ```
 public class Cache<K, V> extends LinkedHashmap<K, V> {
    
    final int maxSize; // set by constructor-omitted for brevity
    
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return size() > maxSize;
    }
 }
 ```
 
 - Now, Strategy pattern is generally preferable
 
 ```
 public LinkedHashMap(EldestEntryRemovalFunction<K,V> fn) { ... }
 
 // Unnecessary functional interface; use standard one instead!!!!
 @FunctionalInterface interface EldestEntryRemovalFunction<K,V> {
    boolean remove(Map<K,V> eldest);
 }
 
 Map<K,V> cache = new LinkedHashMap((map, eldestEntrey) -> map.size() > maxSize);
 ```

 - We should be using the standard `BiPredicate<Map<K,V>, Map.Entry<K, V>>` instead of EldestEntryRemovalFunction<K,V>.
 
 - **when shouldn't we use a standard  functional interface?** 
 
   - When none of the standard interfaces apply.
   - But remember that: Interfaces are forever; interfaces should be design very very carefully; 
 
 
# use streams judiciously (45)

- what is a Stream ? 
 - A stream is **a bunch of data objects from one stream generator** (data source it could be a collection, array, input device, etc...), 
 - **for bulk data processing** 
 - I**t is processed by a pipeline**
 - **Zero or more intermediate stream operations** 
 - **One Single Terminal stream operation** that takes all the elements of the stream and does something.

   
 - Good programs tend to combine iterations and streaming's
 - Stream don't direct support for char, if we try to sort of force the square peg into the round the hole the resulting implementation would have been difficult to read difficult to write and probably slower.
 
 ```
     @Test
     public void whatIsTheOutput() {
         "Hello world!!!".chars()
                 .forEach(System.out::print);
 
         // Output: 7210110810811132119111114108100333333
     }
 
     @Test
     public void whatIsTheOutputNow() {
         // LOL: The chars() methods returns a IntStream
         "Hello world!!!".chars()
                 .forEach(x -> System.out.print((char) x));
 
         // output: Hello world!!!
     }
 ```
 
 Moral:
  
  + Stream only for objects ref types, int, long, and double 
  + minor primitive types ate missing
  + types inference can be confusing
  + Avoid using streams for char processing 
  
  
## use caution when making streams parallel (48) 

 - do not parallelize infinity stream, the computer don't not have the minimal idea who to parallelize that.
 - what can we parallelize well?
   - Arrays, ArraysList, HashMap, HashSet, ConcurrentHashMap, int and long ranges...
   - What do there sources have in common? 
     - predictably splittable
     - Good locality of reference
   - Terminal Operation also matters 
     - must be quick, or easily parallelizable
     - Best are reductions, e.g. min, max, count, count, sum 
     - Collectors (AKA mutable reductions) not so good  
   - Intermediate operations also matter
     - Mapping and filtering are good matchers 
     - limit is a very bad matcher  
 


## Use overloading judiciously (52)

- Avoid confusing uses of overloading
- The behavior of this program is counterintuitive because selection among overloaded methods is static, while selection among overridden methods is dynamic.
- A safe, conservative policy is never to export two overloadings with the same number of parameters.
- you can always give methods different names instead of overloading them.

  - ObjectOutputStream class. It has a variant of its write method for every primitive type and for several reference types.
  Rather than overloading the write method, these variants all have different names, such as writeBoolean(boolean), writeInt(int), and writeLong(long). An added benefit of this naming pattern, when compared to overloading, is that it is possible to provide read methods with corresponding names, for example, readBoolean(), readInt(), and readLong()

## Use Varargs Judiciously (53)

- The right way to implements a method with at least um argument is to declare the methods was the following:

    ```
        private static int min(int firstArg, int... remainingArgs) {
            int min = firstArg;
            for (int arg : remainingArgs) {
                if (arg < min) {
                    min = arg;
                }
            }
            return min;
        }
    ```
- premature optimization is the root of all evil!!!

- The probleam with varargs is varargs automatically creates an array, this costs time and garbage collector pressure to create all those arrays, some times we can't afford that in that case we declare the methods as the following:

```
class EnumSet<E extends Enum<E> {
    static <E> enumSet<E> of(E e);
    static <E> enumSet<E> of(E e1, E e2);
    static <E> enumSet<E> of(E e1, E e2, E e3);
    static <E> enumSet<E> of(E e1, E e2, E e3, E e4);
    static <E> enumSet<E> of(E e1, E e2, E e3, E e4, E e5);
    static <E> enumSet<E> of(E first, E... rest);
} 
```

- instead of having only one thing we know to take the case with one argument you have one two three four five and finally if and only if more than five default to the version with varargs.   
- Avoid cost of array allocation if fewer that n args
    
    

## Notes:

- For functional programing with check exceptions we can use the library: 
  - [vavr](http://www.vavr.io)

- To properly generate builders, equals, hashCode and others stuff, we can use one of the libraries:: 
    - [goole auto](https://github.com/google/auto)
    - [immutables](https://immutables.github.io)
    
    
    
