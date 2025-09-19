// A simple user management system in Scala
// All operations return a NEW UserSystem instance (no mutation).
// User IDs must be positive integers, names, emails, and roles must be non-empty strings
case class UserSystem(private val users: Map[Int, User] = Map.empty) {

  // if id not exist, create user otherwise no change
  def addUser(u: User): UserSystem = {
    require(u.id > 0, "User ID must be positive")
    require(u.name.nonEmpty, "User name cannot be empty")
    require(u.email.nonEmpty, "User email cannot be empty")
    require(u.role.nonEmpty, "User role cannot be empty")
    if (users.contains(u.id)) this else copy(users = users + (u.id -> u))
  }

  // Given the user id, remove the user (only if id exists, otherwise no change)
  def removeUser(id: Int): UserSystem =
    if (users.contains(id)) copy(users = users - id) else this

  // Update an existing user by id (only if id exists, otherwise no change)
  def updateUser(u: User): UserSystem = {
    require(u.id > 0, "User ID must be positive")
    require(u.name.nonEmpty, "User name cannot be empty")
    require(u.email.nonEmpty, "User email cannot be empty")
    require(u.role.nonEmpty, "User role cannot be empty")
    if (users.contains(u.id)) copy(users = users + (u.id -> u)) else this
  }

  // Find user by id, return Option[User]
  def findUser(id: Int): Option[User] = users.get(id)

  // Return all users as an immutable Map
  def getAllUsers: Map[Int, User] = users

  // Additional utility methods
  def size: Int = users.size
  def isEmpty: Boolean = users.isEmpty
}
