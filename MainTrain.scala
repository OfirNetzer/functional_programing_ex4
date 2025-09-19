object MainTrain {

  // Test User case class
  def testUser(): Unit = {
    val user1 = User(1, "Alice", "alice@example.com", "admin")
    val user2 = User(2, "Bob", "bob@example.com", "user")
    val user3 = User(1, "Alice", "alice@example.com", "admin")
    
    // Test equality
    if (user1 != user3)
      println("User equality test failed (-5)")
    
    // Test copy functionality
    val user1Updated = user1.copy(name = "Alice Smith")
    if (user1Updated.name != "Alice Smith" || user1Updated.id != 1)
      println("User copy test failed (-5)")
    
    // Test toString (should work by default with case class)
    val userStr = user1.toString
    if (!userStr.contains("Alice") || !userStr.contains("admin"))
      println("User toString test failed (-5)")
  }

  // Test UserSystem functionality
  def testUserSystem(): Unit = {
    val sys1 = new UserSystem()
    val user1 = User(1, "Alice", "alice@example.com", "admin")
    val user2 = User(2, "Bob", "bob@example.com", "user")
    val user3 = User(3, "Charlie", "charlie@example.com", "user")
    
    // Test validation in addUser
    try {
      sys1.addUser(User(-1, "Invalid", "test@test.com", "user"))
      println("UserSystem validation test failed - should reject negative ID (-5)")
    } catch {
      case _: IllegalArgumentException => // Expected
    }
    
    try {
      sys1.addUser(User(1, "", "test@test.com", "user"))
      println("UserSystem validation test failed - should reject empty name (-5)")
    } catch {
      case _: IllegalArgumentException => // Expected
    }
    
    // Test addUser
    val sys2 = sys1.addUser(user1)
    val sys3 = sys2.addUser(user2)
    
    // Test that original system is unchanged (immutability)
    if (sys1.getAllUsers.nonEmpty)
      println("UserSystem immutability test failed - original system changed (-10)")
    
    // Test findUser
    val foundUser = sys3.findUser(1)
    if (foundUser.isEmpty || foundUser.get != user1)
      println("UserSystem findUser test failed (-5)")
    
    // Test findUser for non-existent user
    if (sys3.findUser(999).isDefined)
      println("UserSystem findUser non-existent test failed (-5)")
    
    // Test getAllUsers
    val allUsers = sys3.getAllUsers
    if (allUsers.size != 2 || !allUsers.contains(1) || !allUsers.contains(2))
      println("UserSystem getAllUsers test failed (-5)")
    
    // Test updateUser
    val updatedUser = user1.copy(name = "Alice Smith")
    val sys4 = sys3.updateUser(updatedUser)
    val foundUpdated = sys4.findUser(1)
    if (foundUpdated.isEmpty || foundUpdated.get.name != "Alice Smith")
      println("UserSystem updateUser test failed (-10)")
    
    // Test removeUser
    val sys5 = sys4.removeUser(1)
    if (sys5.findUser(1).isDefined)
      println("UserSystem removeUser test failed (-10)")
    
    // Test that removeUser doesn't affect other users
    if (sys5.findUser(2).isEmpty)
      println("UserSystem removeUser affected other users (-5)")
    
    // Test addUser with existing ID (should not add)
    val sys6 = sys3.addUser(user1) // user1 already exists with id=1
    if (sys6.getAllUsers.size != sys3.getAllUsers.size)
      println("UserSystem addUser existing ID test failed (-10)")
    
    // Test utility methods
    if (sys3.size != 2)
      println("UserSystem size test failed (-5)")
    if (sys1.isEmpty != true || sys3.isEmpty != false)
      println("UserSystem isEmpty test failed (-5)")
  }

  // Test UserSystemMonoid
  def testUserSystemMonoid(): Unit = {
    val user1 = User(1, "Alice", "alice@example.com", "admin")
    val user2 = User(2, "Bob", "bob@example.com", "user")
    val user3 = User(3, "Charlie", "charlie@example.com", "user")
    val user1Updated = User(1, "Alice Smith", "alice@example.com", "admin")
    
    val sys1 = new UserSystem().addUser(user1).addUser(user2)
    val sys2 = new UserSystem().addUser(user3).addUser(user1Updated)
    
    // Test empty
    val empty = UserSystemMonoid.empty
    if (empty.getAllUsers.nonEmpty)
      println("UserSystemMonoid empty test failed (-5)")
    
    // Test combine
    val combined = UserSystemMonoid.combine(sys1, sys2)
    val allUsers = combined.getAllUsers
    
    // Should have all 3 users (1, 2, 3)
    if (allUsers.size != 3)
      println("UserSystemMonoid combine size test failed (-10)")
    
    // User 1 should be from sys2 (conflict resolution)
    val user1FromCombined = combined.findUser(1)
    if (user1FromCombined.isEmpty || user1FromCombined.get.name != "Alice Smith")
      println("UserSystemMonoid combine conflict resolution test failed (-10)")
    
    // Test associativity: (a + b) + c = a + (b + c)
    val sys3 = new UserSystem().addUser(User(4, "David", "david@example.com", "user"))
    val leftAssoc = UserSystemMonoid.combine(UserSystemMonoid.combine(sys1, sys2), sys3)
    val rightAssoc = UserSystemMonoid.combine(sys1, UserSystemMonoid.combine(sys2, sys3))
    if (leftAssoc.getAllUsers != rightAssoc.getAllUsers)
      println("UserSystemMonoid associativity test failed (-10)")
    
    // Test identity: empty + a = a + empty = a
    val identityLeft = UserSystemMonoid.combine(empty, sys1)
    val identityRight = UserSystemMonoid.combine(sys1, empty)
    if (identityLeft.getAllUsers != sys1.getAllUsers || identityRight.getAllUsers != sys1.getAllUsers)
      println("UserSystemMonoid identity test failed (-10)")
  }

  // Test Processor functionality
  def testProcessor(): Unit = {
    val user1 = User(1, "Alice", "alice@example.com", "admin")
    val user2 = User(2, "Bob", "bob@example.com", "user")
    val user3 = User(3, "Charlie", "charlie@example.com", "admin")
    val user4 = User(4, "David", "david@example.com", "user")
    
    val sys = new UserSystem()
      .addUser(user1)
      .addUser(user2)
      .addUser(user3)
      .addUser(user4)
    
    // Test fromIterable
    val processor = Processor.fromIterable(sys.getAllUsers.values)
    
    // Test toList
    val allUsersList = processor.toList()
    if (allUsersList.size != 4)
      println("Processor toList test failed (-5)")
    
    // Test filter
    val adminProcessor = processor.filter(_.role == "admin")
    val adminList = adminProcessor.toList()
    if (adminList.size != 2)
      println("Processor filter test failed (-10)")
    
    // Test map
    val nameProcessor = processor.map(_.name)
    val nameList = nameProcessor.toList()
    if (nameList.size != 4 || !nameList.contains("Alice"))
      println("Processor map test failed (-10)")
    
    // Test chaining: filter then map
    val adminNames = processor
      .filter(_.role == "admin")
      .map(_.name.toUpperCase)
      .toList()
    if (adminNames.size != 2 || !adminNames.contains("ALICE") || !adminNames.contains("CHARLIE"))
      println("Processor chaining test failed (-10)")
    
    // Test reduce
    val idSum = processor.map(_.id).reduce(_ + _)
    if (idSum != 10) // 1+2+3+4 = 10
      println("Processor reduce test failed (-10)")
    
    // Test immutability - original processor should be unchanged
    val originalList = processor.toList()
    if (originalList.size != 4)
      println("Processor immutability test failed (-5)")
    
    // Test complex chaining
    val result = processor
      .filter(_.role == "user")
      .map(u => u.copy(name = u.name.toUpperCase))
      .filter(_.name.length > 3)
      .toList()
    if (result.size != 1 || !result.head.name.equals("DAVID"))
      println("Processor complex chaining test failed (-10)")
    
    // Test utility methods
    if (processor.size != 4)
      println("Processor size test failed (-5)")
    if (processor.isEmpty != false)
      println("Processor isEmpty test failed (-5)")
    
    // Test reduce on empty collection
    val emptyProcessor = Processor.fromIterable(List.empty[Int])
    try {
      emptyProcessor.reduce(_ + _)
      println("Processor reduce empty test failed - should throw exception (-5)")
    } catch {
      case _: UnsupportedOperationException => // Expected
    }
  }

  def main(args: Array[String]): Unit = {
    testUser()
    testUserSystem()
    testUserSystemMonoid()
    testProcessor()
    
    println("done")
  }
}
