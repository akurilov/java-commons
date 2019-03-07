# Usage

## Gradle

```groovy
compile group: 'com.github.akurilov', name: 'java-commons', version: '2.3.5'
```

# Library Content

## Collections

### Circular Buffer

Allows to reuse the buffer for the elements inserting and removing w/o memory copy.

```java
import com.github.akurilov.commons.collection.CircularBuffer;
import com.github.akurilov.commons.collection.CircularArrayBuffer;
...
    final var buff = (CircularBuffer<String>) new CircularArrayBuffer<>(capacity);
    ...
```

### Trees

* Deep copy

```java
import com.github.akurilov.commons.collection.TreeUtil;
...
    final var srcTree = (Map<String, Object>) ...
    final var dstTree = (Map<String, Object>) TreeUtil.copyTree(srcTree);
```

* Add branch

```java
    final var branch = (Map<String, Object>) ...
    TreeUtil.addBranches(dstTree, branch);
```

* Deep merge

TODO

* Reduce a forest into a tree

```java
    final var forest = (Map<String, Object>) ...
    final var tree = (Map<String, Object>) TreeUtil.reduceForest(forest);
```

### Range

The range described with at least one bound (begin or end position) and optional size.

## Concurrency

### AsyncRunnable

The entity with the following defined states:
* `INITIAL`
* `STARTED`
* `SHUTDOWN`
* `STOPPED`
* `CLOSED`

The corresponding methods (start/shutdown/stop/close) are available guaranteeing the thread-safe transition. Each method
provides the extension points for user action upon transitions. Also, there are also a pair of `await` methods intended
to block until the instance leaves the `STARTED` state.

A user should extend the `AsyncRunnableBase` class.

### Throttles

A throttles provide semaphore-like non-blocking functionality. All throttles also support batch permits acquiring.

Rate throttle example:
```java
    final var rateLimit = 0.1;
    final var throttle = (Throttle) new RateThrottle(rateLimit);
    ...
    n = throttle.tryAcquire(10);
```

Complex weighted throttle example:
```java
    final var weights = new int[] { 80, 20 }; // permits distribution 80% in the 1st direction vs 20% in the 2nd one
    final var weightedThrottle = (IndexThrottle) new SequentialWeightsThrottle(weights);
    ...
    if(weightedThrottle.tryAcquire(0)) {
        // a permit in the 1st direction is acquired
        ...
    }
    ...
    n = weightedThrottle.tryAcquire(1, 10); // try to acquire up to 10 permits in the 2nd direction
```

## Functional Programming

### Partial Functions

TODO

## Object I/O

### Binary

TODO

### Collections

TODO

### Expressions

A values input evaluating an expression and returning the result. Utilizing the
[Java Unified Expression Language](http://juel.sourceforge.net/index.html) functionality.

Current time millis supplying example:
```java
    final var in = ExpressionInput.<Long>builder()
        .expr("${time:millisSinceEpoch()}")
        .func("time", "millisSinceEpoch", System.class.getMethod("currentTimeMillis"))
        .build();
    ...
    System.out.println(in.get());
```

Custom random dynamic string example:
```java
    final IntFunction<String> idSupplier = (radix) -> Long.toString(
        abs(Long.reverse(currentTimeMillis()) ^ Long.reverseBytes(nanoTime())),
        radix
    );
    final var in = ExpressionInput.<String>builder()
        .value("idSupplier", idSupplier, IntFunction.class)
        .value("radix", 36, int.class)
        .expr("${idSupplier.apply(radix)}")
        .build();
    System.out.println(in.get());
```

The most interesting feature is the ability to evaluate the expression on the previous expression result:
```java
    final var in = ExpressionInput.builder()
			.expression("${this.last() + 1}%{-1}")
			.type(int.class)
			.build();
		in.get(); // will return 0
		in.get(); // 1
		in.get(); // 2
		...
```
In other words, it's possible to calculate each next value using the previous one.

### Text

TODO

## Math

Includes the XorShift and greatest common divisor methods.

## Networking

TODO

## Lang

Contains the method `void throwUnchecked(final Throwable t)` allowing to throw checked exceptions as unchecked ones.

## Reflection

### Types Comparison

TODO

## System

### Direct Memory

Thread local direct memory byte buffers cache. Useful for the zero-copy I/O. Instead of allocating the buffer of exact
size find the most suitable sized byte buffer in the thread local cache. The cache contains the sequence of buffers
with a sizes 1, 2, 4, ..., 16MB. The total count of the buffers in the cache is 25 and their summary size is less than
32MB.

Examples:
```java
    MappedByteBuffer bbuff;
    bbuff = DirectMemUtil.getThreadLocalReusableBuff(0); // will return the byte buffer with the size of 1 bytes (min)
    bbuff = DirectMemUtil.getThreadLocalReusableBuff(1); // will return the byte buffer with the size of 1 bytes
    bbuff = DirectMemUtil.getThreadLocalReusableBuff(12345); // will return the byte buffer with the size of 16 KB
    bbuff = DirectMemUtil.getThreadLocalReusableBuff(1_000_000_000_000L); // will return the byte buffer with the size of 16 MB (max)
```

### Size In Bytes

The class to represent some size data. A size may be formatted into the form like "1.234MB". Also the size may be not
 fixed but describe some size range with the given bias.
