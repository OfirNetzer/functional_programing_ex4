
case class User(id: Int, name: String, email: String, role: String) {
  require(id >= 0, "User ID must be non-negative")
  require(name.nonEmpty, "User name cannot be empty")
  require(email.nonEmpty, "User email cannot be empty")
  require(role.nonEmpty, "User role cannot be empty")
}