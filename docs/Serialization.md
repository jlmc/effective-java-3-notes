# Chapter 12. Serialization


- serialization is dangerous thing.

  - implementations details leak into public API
    
    - Serialized from derived from implementation
    - this is really bad because, if we implement Serializeble interface, sudden all the private fields became part of the serial persistent representation of this object.
    
  - Instances created without invoking constructors
  
    - constructors may establish invariants, and instance methods maintain them, yet they can be violated
    - Is too magical, when we deserialize an object we call read object, getting a new object without having calling the foobie constructors
    
  - Doesn't combine well with final fields
  
    - we are forced to make then nonfinal or use reflection 
    
  - The result: increased maintenance cost, likelihood of bugs, security problems,
  
  - There is a better way.
    
    - Serialization proxy pattern
    
      - Don't serialize instance of your class; instead, serialize instances of a small struct-like class that concisely represents the current state of it.
      
      - Then reconstitute instances of your class at deserialization time using only its public APIs!
      
    - The Serialization Proxy Pattern  
    
      1. Design a struct-like proxy class that concisely represents logical state of the class to be serialized.
      
      2. Declare the proxy as a static nested class
      
      3. Provide a single constructor for the proxy which takes as its argument a single instance of enclosing class in essence turning an instance of a class into its proxy and there's no need for consestency checks or defencive copies. it is ok if someone serialize a broken instance of the serialization proxy, because the contents of it are just going to be used in calls to public methods and those public methods are going to do the validity checks.
      
      4. Put **writeReplace** method on the enclosing class. writeReplace allow us to intercede method call onto the serialization chain 
      
          ```java 
          private Object writeReplace() {
            return new SerializationProxy(this);
          } 
          ```
      
      5. Put a **readResolve** method on the proxy. Use any method in the public API on the enclosing class to reconstitute the instance. The readResolve methods works is when something is being serialized before we return the serialize stream we pass the object that is about to be serialized to write replace method and instead serializing the object itself we serialize whatever is returned by writeReplace.
      
        

