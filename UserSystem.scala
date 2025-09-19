
class UserSystem(private val users: Map[Int, User] = Map.empty) {
  
  def addUser(user: User): UserSystem = {
    if (users.contains(user.id)) {
      this // Don't add if user with same ID already exists
    } else {
      new UserSystem(users + (user.id -> user))
    }
  }
  
  def removeUser(id: Int): UserSystem = {
    new UserSystem(users - id)
  }
  
  def updateUser(user: User): UserSystem = {
    if (users.contains(user.id)) {
      new UserSystem(users + (user.id -> user))
    } else {
      this // Don't update if user doesn't exist
    }
  }
  
  def findUser(id: Int): Option[User] = {
    users.get(id)
  }
  
  def getAllUsers: Map[Int, User] = {
    users
  }
  
  // Utility methods for convenience
  def size: Int = users.size
  
  def isEmpty: Boolean = users.isEmpty
  
  override def equals(other: Any): Boolean = other match {
    case that: UserSystem => this.users == that.users
    case _ => false
  }
  
  override def hashCode(): Int = users.hashCode()
}
