package mmichaelis.dojo.bqueue;

/**
 * @param <T> content type in queue
 * @since 2013-07-24
 */
public class BoundedQueueImpl<T> implements BoundedQueue<T> {
  private final int size;

  public BoundedQueueImpl(int size) {
    this.size = size;
  }

  @Override
  public void Enqueue(T element) {
  }

  @Override
  public T Dequeue() {
    return null;
  }

  @Override
  public int Count() {
    return 0;
  }

  @Override
  public int Size() {
    return size;
  }
}
