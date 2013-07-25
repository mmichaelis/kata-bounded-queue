package mmichaelis.dojo.bqueue;

/**
 * @param <T> content type in queue
 * @since 2013-07-24
 */
public interface BoundedQueue<T> {
  void Enqueue(T element);

  T Dequeue();

  /**
   * Number of elements in queue.
   *
   * @return elements in queue
   */
  int Count();

  /**
   * Maximum number of elements in queue.
   *
   * @return maximum number of elements
   */
  int Size();
}
