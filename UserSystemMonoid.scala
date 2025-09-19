// Generic Monoid type class
trait Monoid[A] {
  def empty: A
  def combine(x: A, y: A): A
}

// Monoid instance for UserSystem: immutable union, right-biased on id conflicts.
// Laws: see discussion at bottom of file.
object UserSystemMonoid extends Monoid[UserSystem] {
  override val empty: UserSystem = UserSystem()

  override def combine(x: UserSystem, y: UserSystem): UserSystem = {
    // Right-biased union on conflicting ids (keep y's value)
    val merged: Map[Int, User] = x.getAllUsers ++ y.getAllUsers
    UserSystem(merged)
  }
}

/*
Q&A 

A. Why is it preferable to implement the union functionality (monoid) via a separate object 
   rather than through inheritance or extension of UserSystem?
   

   I think implementing the monoid as a separate object is preferable for a few reasons:
   1. It keeps things clean by separating concerns. 
      The UserSystem class should focus on managing user data—its core job—while the monoid handles the logic for combining systems.
      The algebraic structure (monoid) is a separate concern from the data structure (UserSystem).
   2. Going with a separate object makes it way easier to extend. 
      You can define different combination strategies, like a left-biased merge or a set-union approach, without touching UserSystem
   3. It also makes testing and reusing code simpler, since a type class-style monoid avoids the mess of tight coupling or subclassing issues,
      like the fragile base class problem or dealing with tricky constructor constraints
   4. It keeps UserSystem immutable and focused as a pure data structure.
      The monoid injects the behavior we need without bloating the class.
      This aligns nicely with the single responsibility principle: UserSystem manages users, and the monoid takes care of combining systems.

B. What are the two fundamental laws of monoids?

   Monoids come with two key laws that make them predictable and useful:
   
   1. Identity Laws (Left & Right): 
      There’s an "empty" element that acts like a neutral player.
      If you combine it with any value a, you just get a back. 
      Mathematically, it’s combine(empty, a) == a and combine(a, empty) == a. 
      It is like adding a zero or multiplying by one—it doesn’t change the result and the original value remains the same.
   
   2. Associativity Law: 
      The way you group operations doesn’t matter, as long as the sequence stays the same.
      So, combine(a, combine(b, c)) is the same as combine(combine(a, b), c) for any values a, b, and c.
      It’s like saying (1 + 2) + 3 equals 1 + (2 + 3).

C. How do these mathematical laws help us in software development?
   
   The monoid laws are really helpful when you write code. 
   First, they make your code predictable and safe for refactoring. 
   Because of associativity, you can change how you group operations, and the result stays the same. 
   This means you can refactor your code without worrying it will break.

   Also, these laws are great for parallel and distributed systems. 
   You can split your data into pieces (batching), work on them separately, and then combine them, and it will always work right because of associativity.
   This is super useful for things like map-reduce or big data processing.
  
   Another thing is that monoids let you write code that works with many types.
   You can make APIs that use monoids, and they will work with any type that follows the monoid rules. 
   This makes your code reusable and flexible.
   Plus, the laws help with optimization. 
   Compilers or libraries can use associativity to make operations faster, like combining things in a smarter way. 
   
   And finally, these laws let you think about your code logically. 
   They give you rules to check if your program is correct, so you can be sure it works as you expect.
*/

