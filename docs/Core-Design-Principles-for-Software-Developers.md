# Core Design Principles for Software Developers

##### What is a good design

- A design if the coast of change it is minimum.
- Could be a good definition, but there is one big problem with that definition.
  - When it comes the time to change the design you realize that this is horrible and that's way too late to say "oh gosh we messed up".
- We need to be able to proactively say that we are creating a better design rather than after the fact say "well that was after all not a good design"

- Clearly:
  - A good design is a design that is easier to change
  - Is almost impossible to get it right the first time.
    - But if we think about a lot oof human activities, we never seen to do things is one time, we take several iterations to get things done.
    - If we look at evidence of some really famous artists for example the greatest of painter, sculptors, etc... they never created those masterpieces in one single shot.
    - There is evidence of several prototypes.
    - So, Why should us, developers have to do the things prefect always in the first interaction.
  - We Should give time to improve on what we do rather than just creating it once and be done with it.
  
  - Software is a never actually written, it is always rewritten.
  - Software has constantly evolve
    - if somebody comes to you and tells you, "we wrote a software once and they never had to change it again" he are telling you that their project got canceled because any software that is relevant has to change.
  - "Soft" is synonym of easy - so, we can also say "software should be easy to change" 



##### How to create good design?
  - Developers are humans, that means, they make mistakes, they are not perfects.
  - To create good design first step is let go of the ego.
  - Be unemotional.

  - There are two kings of people that are dangerous to work with.
    1. who can't follow instructions
    2. who can only follow instructions
  
  - Take time to review the design and the code
    - When we review the code of somebody else, we can:
      - Heep him in getting a better design
      - We can Learn new things we those design
  
##### KISS - Keep it simple

  - KISS - Keep it Simple and Stupid
  
  - It very important to understand that "Simple" really means!!!
  
  - Simple keeps you focused, if some things is simple it doesn't distract us. 
  - Simple solve only real problem we know about.
  - Simple fails less
  - Simple is easier to understand

##### Complexity

  - Inherent Complexity
    - Complexity from the problem domain, there are nothing to do about it.
    
  - Accidental Complexity
    - It comes from the solution we use to solve a particular problem.
    - This solution brought other problems, and we were trying to solve problems after problems, a real messed.
    - The Original problem doesn't required this particular solution.
    - We fail, we should think about a different solution to the problem.
    - So, We should avoid this kind of Complexity. We should not deal with this king of Complexity every single days.
    
  
  - **"A good design is the one that hides Inherent complexity and eliminates the Accidental Complexity."**
  
##### Think YAGNI Principle

  - [YAGNI - You Aren't Gonna Need It](https://martinfowler.com/bliki/Yagni.html)
  
  - When should we implement something?
    - How much do you know about it?
    - Cost of implementing?
  - We know that we are more smarter tomorrow than we are today.
    - We will have more information about many stuffs, as a result, we can make better decisions tomorrow rather today.
    
    | Implementing Now |     Operator   | Implementing Later |  What to do?            |
    |------------------|:--------------:|--------------------|-------------------------|
    | $N               |        >       | $L                 | postpone (do it letter) |
    | $N               |       ==       | $L                 | postpone (do it letter) |
    | $N               |        <       | $L                 | how probable? if high do now, otherwise if low postpone   |
    
    
  - "Don't do a thing until you find real value in the thing." 
  
  
##### Cohesion

- We want software to change, but not too expensive.
- "If a code is Cohesion, it hash to change less frequently".

- A software module/Component (function, class or package) should be focus in a single responsibility.
  - This will limit the number/frequency of changes in that given module.
  
- So, a software Module/Component should have high cohesion.
  
##### Coupling

- Is what software a Module/Component depend on.
- Define the number of the dependencies of a given Module/Component.

NOTE:
 - Inheritance increase the Coupling very much.
   - Because, it is the very rigid.

- What we should do?
  1. try to see if we can remove coupling.
  2. If we can not remove coupling, we should depended of and Interface (Caution)

- So, a software Module/Component should have low cohesion.

- **"A good design has high cohesion and low coupling"**

##### DRY - Dont Repeat yourself

  - Don't duplicate - code and also effort
  - Every piece of knowledge in a system should have a single unambiguous authoritative representation.

  - It reduces the cost of development


---

## SOLID Principles

#### SRP Focus on Single Responsibility
    
  - `A Software module (method, class, component, etc...) should have one, and only one, reason to change`.  
  - Cohesion code have a single responsibility.

###### Lets try to think about places where we can find this Single Responsibility principle violated

  - Long Methods
    - We should avoid long methods.
    - Long methods normally have more that one responsibility.
    - Why long methods are bad?
      - Hard to Test
      - Hard to read 
      - Hard to reuse
      - Leads to duplication
      - More than one reason to change
      - Low cohesion (What do the method? What do not do the method?)
      - High coupling (The code will not came allene, it will take dependencies).
    - How Long is a Long Method?
      - We will never have an unanimously acceptable answer on how to define the long method. Many developers will say that it is due to the number of lines, or the size of the text editor window.
      - [SLAP](https://www.ibm.com/developerworks/library/j-eaed4/index.html) - Single Level of Abstraction principle.
      - Therefore, we can say that the we are not doing the right question. The right question should be, `How high is the method Abstraction Level?`
      - Don't comment what, instead comment why. Don't write a comment if you don't understand some statement of code, instead you should refactoring the method, this way the future will thank you.
      - `Compose method Pattern` - A Code should be compose of the steps you want to take in developing this particular logic/feature. This means that when you read the code the code should tell you a story easy to understand, therefore code is auto documented.
  
  - Classes without a single responsibility
  
  
#### OCP - Open-Close Principle

  - `Software module (method, class, component, etc...), should be open for extension but close for modifications`.
  
    - Open for extension: should be able to do something that it doesn't do before.
    - Close for modifications: without apply any kind of change in code that we wrote previous.
    
  - How can we do this:
  
    - Two words can resolve the dilemma, `Abstraction` and `Polymorphism` are the key to make this happen.
    - See the example: [OCPOpenClosePrincipleTest](../src/test/java/io/costax/solid/OCPOpenClosePrincipleTest.java)
    
  - Important Note: It is not possible to make class infinitely extensibility, we can only extensible for what we design for.
    - And this raises another problem, we need to know when we design the class, what really is extensible. In what sense does our class need to be extensible?
      - In order to answer this problem it is necessary to know about two points:
        1. The first point is to know technical solutions to know how to do it. 
        2. The second point is to know the domain really well, only understanding the domain can we know in which direction it is expected that the software is extensible.
    
  
  
  - When we violate the `SRP Focus on Single Responsibility` we may also are violating the `Open-Close Principle`, because from that moment on this component will also have more reasons to be changed,
    - The Code it will became more difficult to be extensive.


#### LSP - Liskov's Substitution Principle
 
- `Inheritance should be use only for substitutability`.

  - This means that we should use Inheritance only when:
    - If an Object of B should be used anywhere an object of A is use then use Inheritance.
    - If an Object of B should use an Object of A, then use Composition / Delegation.

- why should we not use Inheritance without limits?
  - Inheritance demands more from a developer then composition or delegation does.

- `Services of the derived class should requires no more and promise no less than the correspond services of the base class`.
- The user of a base class should be able to use an instance of a derived class with knowing the difference.

- We can find evidences of the Liskov's Substitution Principle in many places, and those evidences can be God and Bad:

  - Good
    - Method Visibility: `public` vs `protected` in `base` vs `derived`
      - If a base class provide a public method `m` then the derived classes can not override the `m` method with less visibility. 
      - In Fact, if we make the exercise we can see a compilation error: `attempting to assign weaker access privileges ('protected'); was 'public'` 
      - Java is a "generous" language, because it allows a private or protected method in the base class to have greater visibility in a derived class.
      - Therefore, **we can be more generous, but we cannot be less restrictive.**
      
    - Exceptions: Derived functions can't throw any new **Checked Exceptions** not Thrown by the base class (unless the new exceptions extends the old one begin thrown by the base class).
      - The Java allows us not declaring the checked exception in the derived method (if the exceptions is never throws), even if the base method signs the throws of the specified exception.
      - Once again, **we can be more generous, but we cannot be less restrictive.**
    
    - Collection of derived does not extends from Collection of Base class.
      - Type Parameters: consider the following example:
        ```java
        class Example {
      
            @Test void executeProcess() {
                final List<Book> books = new ArrayList<>();
                BigDecimal process = process(books);
      
                // 
                final List<TechBook> techBooks = new ArrayList<>();
                process(techBooks); // compilation error: Error:(45, 21) java: incompatible types
              }
              
            BigDecimal process(List<Book> books) {
                //books.add(new Drama());
      
                return books
                       .stream()
                       .map(Book::price)
                       .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
        }
        ``` 
    - The previous compilation error is caused by the application of a simple principle, the method of the process could add to the List<Book> passed as a parameter some type Wrong for the provided Parameter, and this can be a problem. To avoid this, Java does not allow us to implement logic as in the previous way
      
  - Bad
    - The JVM itself has an example of violations of this principle, for example the class Stack.
      - The `java.util.Stack` class should not extend `java.util.Vector`.
      - To solve this kind of design problems we should **Use composition or delegation instead of inheritance unless you want substitutability**.
      
#### ISP - Interface Segregation principle

  - **"Many client-specific interfaces are better than one general-purpose interface".**

  - Clients should not be forced to depend on methods that they do not use.
  - Interfaces should belong to clients, not to libraries or hierarchies.
  - Application developers should favor thin, focused interfaces to “fat” interfaces that offer more functionality than a particular class or method needs.
    - Developers should no create Fat interfaces with all the methods. 

#### DIP - Dependency Inversion Principle

  - **A class should not depend on another class, they both have to depend on an abstraction (interface)**.
    - Use caution. Otherwise will in inject to many complexity to our code.
    - Use this principle only if you can see a really benefice of him in the solution you need. 
    
  - This is possibly the principle and we use it more often without even realizing it.
    - Every time we are using TDD




## In Summary

- DRY - Don't Repeat yourself
- YAGNI - You Aren't Gonna Need It
-
- S - SRP - Single Responsibility Principle
- O - OCP - Open Close Principle
- L - LSP - Liskov's Substitution Principle
- I - ISP - Interface Segregation Principle
- D - DIP - Dependency Inversion Principle