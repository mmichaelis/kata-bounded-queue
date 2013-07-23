# Kata-Bounded-Queue

## Original Description

The following description is a direct translation from [Kata-Bounded-Queue][]:

Develop a class for a Waiting Queue with limited length for communication between several threads.

**Reading Threads** take elements from the queue. If the queue is empty, the reading threads block and
wait for the next element.

**Writing Threads** attach elements. If the queue is full, the writing threads block and wait for another
thread to take an element from the queue.

The interface of the class should look like this:

```java
class BoundedQueue<T> {
  BoundedQueue(int size) {...}
  void Enqueue(T element) {...}
  T Dequeue() {...}
  int Count() {...} // number of elements in queue
  int Size() {...} // maximum number of elements in queue
}
```

**Example:**

<table>
  <tr>
    <th>Writing Thread</th><th>Queue</th><th>Reading Thread</th>
  </tr>
  <tr>
    <td>new BoundedQueue<int>(2)</td><td>&nbsp;</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>Enqueue(1)</td><td>1</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>&nbsp;</td><td>Dequeue() -> 1</td>
  </tr>
  <tr>
    <td>Enqueue(2)</td><td>2</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>Enqueue(3)</td><td>2, 3</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>Enqueue(4) <em>// blocks</em></td><td>&nbsp;</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>3</td><td>Dequeue() -> 2</td>
  </tr>
  <tr>
    <td><em>// free again</em></td><td>3, 4</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>4</td><td>Dequeue() -> 3</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>&nbsp;</td><td>Dequeue() -> 4</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>&nbsp;</td><td>Dequeue() <em>// blocks</em></td>
  </tr>
  <tr>
    <td>Enqueue(5)</td><td>5</td><td>&nbsp;</td>
  </tr>
  <tr>
    <td>&nbsp;</td><td>&nbsp;</td><td>-> 5 <em>// kommt frei</em></td>
  </tr>
</table>

Ignore performance issues.

## Links

* [Kata-Bounded-Queue][]

[Kata-Bounded-Queue]: <http://de.scribd.com/doc/140927737/Class-Kata-Bounded-Queue> "Class Kata „Bounded Queue“"
