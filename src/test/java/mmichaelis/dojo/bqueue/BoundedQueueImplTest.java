package mmichaelis.dojo.bqueue;

import com.google.common.collect.Queues;
import net.joala.condition.ConditionFactory;
import net.joala.condition.DefaultConditionFactory;
import net.joala.expression.AbstractExpression;
import net.joala.time.TimeoutImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

/**
 * Test with original test case from Kata description for {@link BoundedQueueImpl}.
 *
 * @since 2013-07-24
 */
public class BoundedQueueImplTest {
  private final WritingThreadDequeExpression writingThreadDequeExpression = new WritingThreadDequeExpression();
  private BoundedQueueImpl<Integer> queue;
  private ReadingThread             readingThread;
  private WritingThread             writingThread;
  private final ConditionFactory conditionFactory = new DefaultConditionFactory(new TimeoutImpl(1L, TimeUnit.SECONDS));

  @Before
  public void setUp() throws Exception {
    queue = new BoundedQueueImpl<Integer>(2);
    readingThread = new ReadingThread(queue);
    writingThread = new WritingThread(queue);
    readingThread.start();
    writingThread.start();
  }

  @After
  public void tearDown() throws Exception {
    readingThread.close();
    writingThread.close();
  }

  @Test
  public void testKataScenario() throws Exception {
    writingThread.write(1);
    conditionFactory.condition(writingThreadDequeExpression).assertThat(is(empty()));
    assertEquals("Should be able to read first value.", Integer.valueOf(1), readingThread.read());
    writingThread.write(2);
    conditionFactory.condition(writingThreadDequeExpression).assertThat(is(empty()));
    writingThread.write(3);
    conditionFactory.condition(writingThreadDequeExpression).assertThat(is(empty()));
    writingThread.write(4); // blocks
    conditionFactory.condition(writingThreadDequeExpression).assertThat(not(is(empty())));
    readingThread.read();
    conditionFactory.condition(writingThreadDequeExpression).assertThat(is(empty()));
    readingThread.read();
    readingThread.read();
    readingThread.read(); // blocks
    writingThread.write(5);
  }

  private static class ReadingThread extends Thread implements AutoCloseable {
    private final BoundedQueueImpl<Integer> queue;

    // TODO: Continue here:
    // Perhaps only provide a counter of open read requests
    // hold values read in a queue which can be read (and cleared then)

    private final ArrayDeque<Integer> deque   = Queues.newArrayDeque();
    private       boolean             running = true;

    private ReadingThread(final BoundedQueueImpl<Integer> queue) {
      super("Reading Thread");
      this.queue = queue;
    }

    public synchronized void triggerRead() {
      // deque.addLast(true);
    }

    public Integer read() {
      return queue.Dequeue();
    }

    @Override
    public void run() {
      boolean repeat;
      synchronized (this) {
        repeat = running;
      }
      while (repeat) {
        final Integer element;
        synchronized (deque) {
          element = deque.peekFirst();
        }
        if (element != null) {
          queue.Enqueue(element);
          synchronized (deque) {
            deque.removeFirst();
          }
        }
        Thread.yield();
        synchronized (this) {
          repeat = running;
        }
      }
    }

    @Override
    public synchronized void close() throws Exception {
      running = false;
    }
  }

  private static class WritingThread extends Thread implements AutoCloseable {
    private final BoundedQueueImpl<Integer> queue;
    private final ArrayDeque<Integer> deque   = Queues.newArrayDeque();
    private       boolean             running = true;

    private WritingThread(final BoundedQueueImpl<Integer> queue) {
      super("Writing Thread");
      this.queue = queue;
    }

    public void write(final Integer value) {
      synchronized (deque) {
        deque.addLast(value);
      }
    }

    @Override
    public void run() {
      boolean repeat;
      synchronized (this) {
        repeat = running;
      }
      while (repeat) {
        final Integer element;
        synchronized (deque) {
          element = deque.peekFirst();
        }
        if (element != null) {
          queue.Enqueue(element);
          synchronized (deque) {
            deque.removeFirst();
          }
        }
        Thread.yield();
        synchronized (this) {
          repeat = running;
        }
      }
    }

    private ArrayDeque<Integer> getDeque() {
      return deque;
    }

    @Override
    public synchronized void close() {
      running = false;
    }
  }

  private class WritingThreadDequeExpression extends AbstractExpression<Collection<Integer>> {
    @Override
    public Collection<Integer> get() {
      return writingThread.getDeque();
    }
  }
}
