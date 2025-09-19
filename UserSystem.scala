
// Immutable user system built on an immutable Map[Int, User].
// All operations return a NEW UserSystem instance (no mutation).
case class UserSystem(private val users: Map[Int, User] = Map.empty) {

  // 1) Add a user if id does not already exist; otherwise return this (no change)
  def addUser(user: User): UserSystem =
    if (users.contains(user.id)) this else copy(users = users + (user.id -> user))

  // 2) Remove a user by id (returns new copy; safe if id not found)
  def removeUser(id: Int): UserSystem =
    if (users.contains(id)) copy(users = users - id) else this

  // 3) Update an existing user by id (only if exists; otherwise no change)
  def updateUser(user: User): UserSystem =
    if (users.contains(user.id)) copy(users = users + (user.id -> user)) else this

  // 4) Find user by id
  def findUser(id: Int): Option[User] = users.get(id)

  // 5) Return all users as an immutable Map[Int, User]
  def getAllUsers: Map[Int, User] = users
  
  // Utility methods for convenience
  def size: Int = users.size
  
  def isEmpty: Boolean = users.isEmpty
}
