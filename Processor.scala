
class Processor[A] private (private val data: List[A]) {
  
  def filter(p: A => Boolean): Processor[A] = {
    new Processor(data.filter(p))
  }
  
  def map[B](f: A => B): Processor[B] = {
    new Processor(data.map(f))
  }
  
  def reduce(f: (A, A) => A): A = {
    if (data.isEmpty) {
      throw new UnsupportedOperationException("Cannot reduce empty collection")
    }
    data.reduce(f)
  }
  
  def toList(): List[A] = {
    data
  }
  
  // Utility methods for convenience
  def size: Int = data.size
  
  def isEmpty: Boolean = data.isEmpty
}

object Processor {
  def fromIterable[A](it: Iterable[A]): Processor[A] = {
    new Processor(it.toList)
  }
}
