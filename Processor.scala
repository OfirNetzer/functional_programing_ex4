// A generic data processing pipeline with filter, map, and reduce operations
// All operations return a NEW Processor instance (no mutation).
final case class Processor[A](private val data: List[A]) {

  // returns a NEW Processor[A], keeps type safety across the chain
  def filter(p: A => Boolean): Processor[A] =
    Processor(data.filter(p))

  // returns a NEW Processor[B], keeps type safety across the chain
  def map[B](f: A => B): Processor[B] =
    Processor(data.map(f))

  // reduces the data to a single value of type A
  // throws UnsupportedOperationException if data is empty
  def reduce(f: (A, A) => A): A = {
    if (data.isEmpty) throw new UnsupportedOperationException("reduce of empty collection")
    data.reduce(f)
  }

  // returns the underlying data as an immutable List
  def toList(): List[A] = data

  // Utility methods
  def isEmpty: Boolean = data.isEmpty
  def size: Int = data.size
}
// Companion object for Processor to create instances from Iterable
object Processor {
  def fromIterable[A](it: Iterable[A]): Processor[A] =
    Processor(it.toList)
}
