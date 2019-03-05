# The Moral

- Strange and terrible methods lurk in libraries

  - Some have innocuous sounding names 

- if your code misbehaves 
  
  - Make sure you are calling the rigth methods
  - read the library documentation
  
- For Api designers 

  - Don't violate principle of least astonishment
  - Don't violate the abstraction hierarchy
  - Don't use similar names for wildly different behaviors
  
  
---

- Autoboxing blurs but does not erase distinction between primitive and boxed primitives

- Only four of six comparation operators works on boxed primitives

  - <, >, <= and >=  work !!!
  - == and != do not work !!!
  
  - Note that since the Java 1.5 the jvm have a cache of Integer between -127 and 127 
  
- It's very hard to test for broken comparators 