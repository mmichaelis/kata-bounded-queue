package mmichaelis.dojo.bqueue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @since 2013-07-25
 */
@RunWith(Parameterized.class)
public class BoundedQueueImplParametrizedTest {

  private final String description;
  private final Integer size;
  private final Integer[] queueBefore;
  private final Integer[] queueAfter;
  private final Integer[] enqueuing;
  private final Integer[] reading;
  private final Integer[] enqueued;
  private final boolean   blocksEnqueuing;
  private final boolean   blocksReading;
  private final boolean   blockedReading;

  public BoundedQueueImplParametrizedTest(final String description,
                                          final Integer size,
                                          final Integer[] queueBefore,
                                          final Integer[] queueAfter,
                                          final Integer[] enqueuing,
                                          final Integer[] reading,
                                          final Integer[] enqueued,
                                          final boolean blocksEnqueuing,
                                          final boolean blocksReading,
                                          final boolean blockedReading) {
    this.description = description;
    this.size = size;
    this.queueBefore = queueBefore;
    this.queueAfter = queueAfter;
    this.enqueuing = enqueuing;
    this.reading = reading;
    this.enqueued = enqueued;
    this.blocksEnqueuing = blocksEnqueuing;
    this.blocksReading = blocksReading;
    this.blockedReading = blockedReading;
  }

  @Test
  public void testScenario() throws Exception {
  }

  @Parameterized.Parameters(name = "{index}: {0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(
        QueueExample.description("Enqueuing 1, results in queue {1}.").withQueueSize(2).queueBefore().enqueuing(1).queueAfter(1).toArray(),
        QueueExample.description("Reading 1 from queue {1}").withQueueSize(2).queueBefore(1).reading(1).queueAfter().toArray(),
        QueueExample.description("Enqueuing 2, results in queue {2}.").withQueueSize(2).queueBefore().enqueuing(2).queueAfter(2).toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore(2).enqueuing(3).queueAfter(2, 3).toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore(2, 3).blocksEnqueuing(4).queueAfter(2, 3).toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore(2, 3).enqueued(4).reading(2).queueAfter(3, 4).toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore(3, 4).reading(3).queueAfter(4).toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore(4).reading(4).queueAfter().toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore().blocksReading().queueAfter().toArray(),
        QueueExample.description("").withQueueSize(2).queueBefore().blockedReading(5).enqueuing(5).queueAfter().toArray()
    );
  }

  private static final class QueueExample {
    private final String description;
    private Integer size;
    private Integer[] enqueuing = new Integer[0];
    private Integer[] queueAfter = new Integer[0];
    private Integer[] queueBefore = new Integer[0];
    private Integer[] reading = new Integer[0];
    private boolean   blocksEnqueuing;
    private boolean   blocksReading;
    private Integer[]   enqueued = new Integer[0];
    private boolean   blockedReading;

    public Object[] toArray() {
      return new Object[]{
          description,
          size,
          queueBefore,
          queueAfter,
          enqueuing,
          reading,
          enqueued,
          blocksEnqueuing,
          blocksReading,
          blockedReading
      };
    }

    public QueueExample(final String description) {
      this.description = description;
    }

    public QueueExample withQueueSize(final Integer size) {
      this.size = size;
      return this;
    }

    public QueueExample enqueuing(final Integer... enqueuing) {
      this.enqueuing = enqueuing;
      this.blocksEnqueuing = false;
      return this;
    }

    public QueueExample queueAfter(final Integer... queueAfter) {
      this.queueAfter = queueAfter;
      return this;
    }

    public QueueExample queueBefore(final Integer... queueBefore) {
      this.queueBefore = queueBefore;
      return this;
    }

    public QueueExample reading(final Integer... reading) {
      this.reading = reading;
      this.blocksReading = false;
      return this;
    }

    public QueueExample blocksEnqueuing(final Integer... enqueuing) {
      enqueuing(enqueuing);
      this.blocksEnqueuing = true;
      return this;
    }

    public QueueExample blocksReading() {
      this.blocksReading = true;
      return this;
    }

    public QueueExample enqueued(final Integer... enqueued) {
      this.enqueued = enqueued;
      return this;
    }

    public QueueExample blockedReading(final Integer... reading) {
      reading(reading);
      this.blockedReading = true;
      return this;
    }

    public static QueueExample description(final String description) {
      return new QueueExample(description);
    }
  }
}
