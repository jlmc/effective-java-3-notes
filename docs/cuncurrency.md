# Chapter 11. Concurrency


Use ConcurrentHashMap - but use it rigth

- ConcurrentHashMap is a great class:

- concurrentHashMap collections manage synchronization internally, lock striping, non-blocking algarithms, etc

- combines high concurrency and performance, this way things can happen in parallel with high speed.

	- this is possible because Doug Lea (@author Doug Lea) is very clever and he wrote it very carefully he spend long time writing it and it uses fancy things inside like lock striping and non blocking algorithms and the whole idea behing concurrency utilities. 




- Synchronized collections nearly obsolete 

- Use ConcurrentHashMap, not Collections.synchronizeMap()


- never synchronize on a Concurrent Collection (any implementation of concurrent package) they do their synchronization internally and f we synchronizing on it will have no effect on other ongoing operations.

 syncrhroniztion is not like concurrency. syncrhroniztion is the opposite of concurrency. 
 	- concurrency is Lots of things happening in parallel
 	- syncrhroniztion is me blocking you to a first approximation that's


Summary:

- Synchronized collections are largely obsolete
- Use ConcurrentHashMap and frieds :)
- Never synchronize on a concurrent collection
- Use putIfAbsent (and frieds) methods properly
  - only call putIfAbsent if get methods return null
  - always check if the return value of putIfAbsent
