# Usage

## Gradle

```groovy
compile group: 'com.github.akurilov', name: 'java-commons', version: '1.1.0'
```

# Library Content

## Collections

### Optionally Locking Buffer

Allows to avoid the thread blocking due to necessary synchronization.
Use Java's ```Lock.tryLock()``` method to perform an effort to obtain the exclusive lock immediately.
Skip all the subsequent actions if this particular required lock is held by another thread.
The class ```OptLockArrayBuffer``` is designed to support the optional locking.

Publishing the ```ArrayList.removeRange(from, to)``` method allows to recycle the buffer after it
partially consumed w/o redundant instantiation. The interface ```OptLockBuffer``` is designed to support the range removing method.

### Range

TODO

## Concurrency

### Throttle

TODO

### Stoppable Task

TODO

## Object I/O

### Binary

TODO

### Collections

TODO

### Text

TODO

## Math

### XorShift

TODO

## Reflection

### Types Comparison

TODO

## System

### Direct Memory

TODO

### Size In Bytes

TODO
