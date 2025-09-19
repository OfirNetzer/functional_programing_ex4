
trait Monoid[A] {
  def empty: A
  def combine(a1: A, a2: A): A
}

object UserSystemMonoid extends Monoid[UserSystem] {
  
  def empty: UserSystem = new UserSystem()
  
  def combine(a1: UserSystem, a2: UserSystem): UserSystem = {
    // Start with a1 and add all users from a2
    // In case of conflict, a2 takes precedence
    val allUsers = a1.getAllUsers ++ a2.getAllUsers
    
    // Build the combined system using functional fold
    new UserSystem(allUsers)
  }
}

/*
Open Questions (in English):

a. Why is it better to implement the combine functionality (Monoid) using a separate object 
   rather than inheritance or extending UserSystem?
   
   Answer: Using a separate object follows the principle of composition over inheritance and 
   provides better separation of concerns. It keeps the UserSystem class focused on its core 
   responsibilities while the monoid operations are handled separately. This also allows for 
   multiple different monoid implementations if needed, and makes the code more modular and testable.

b. What are the two laws of a monoid?
   
   Answer: The two fundamental laws of a monoid are:
   1. Associativity: combine(combine(a, b), c) = combine(a, combine(b, c))
   2. Identity: combine(empty, a) = combine(a, empty) = a

c. How do these mathematical laws help us in software development?
   
   Answer: These laws provide guarantees about behavior that enable:
   - Parallelization: Operations can be split and combined in any order
   - Optimization: Compilers and libraries can rearrange operations safely
   - Reasoning: Developers can predict behavior and write more reliable code
   - Composability: Complex operations can be built from simpler ones predictably
*/