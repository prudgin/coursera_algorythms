package queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int INITIAL_ARR_CAPACITY = 1;
    private Item[] arr;
    private int elementsCount;

    private class RQIterator implements Iterator<Item> {

        boolean hasNext = false;
        RandomizedQueue<Item> iterationQueue;

        private RQIterator() {
            if (!RandomizedQueue.this.isEmpty()) {
                this.hasNext = true;
                int iterLength = RandomizedQueue.this.size();
                // + 1 so it won't resize
                iterationQueue = new RandomizedQueue<>(iterLength + 1);
                for (int i = 0; i < iterLength; i++) {
                    iterationQueue.enqueue(RandomizedQueue.this.arr[i]);
                }
            }
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Queue empty.");
            }
            Item nexItem = iterationQueue.dequeue();
            if (iterationQueue.isEmpty()) {
                hasNext = false;
            }
            return nexItem;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Method remove is not supported.");
        }
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        arr = (Item[]) new Object[INITIAL_ARR_CAPACITY];
        elementsCount = 0;
    }

    private RandomizedQueue(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial queue capacity must be positive");
        }
        arr = (Item[]) new Object[initialCapacity];
        elementsCount = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return (elementsCount == 0);
    }

    // return the number of items on the randomized queue
    public int size() {
        return elementsCount;
    }

    private Item[] resizeArr(Item[] oldArr, int newSize, int oldSize) {
        Item[] newArr = (Item[]) new Object[newSize];
        int iterLength = Math.min(newSize, oldSize);
        if (iterLength >= 0) System.arraycopy(oldArr, 0, newArr, 0, iterLength);
        return newArr;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null values not accepted");
        }
        int arrLength = arr.length;
        if (elementsCount == arrLength) {
            arr = resizeArr(arr, arrLength * 2, arrLength);
        }
        if (isEmpty()) {
            arr[0] = item;
        } else {
            int whosGonnaBeLast = StdRandom.uniform(0, elementsCount + 1);
            arr[elementsCount] = arr[whosGonnaBeLast];
            arr[whosGonnaBeLast] = item;
        }
        elementsCount++;

    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Dequeing from empty queue not allowed.");
        }
        Item item = arr[--elementsCount];
        arr[elementsCount] = null;
        int arrLength = arr.length;
        if (arrLength >= 8 && (elementsCount <= arrLength / 4)) {
            arr = resizeArr(arr, arrLength / 2, arrLength);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Sampling from empty queue not allowed.");
        }
        if (size() == 1) {
            return arr[0];
        }
        int randIndex = StdRandom.uniform(0, elementsCount);
        return arr[randIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RQIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        System.out.println("empty: " + randomizedQueue.isEmpty());
        int n = 10;
        for (int i = 0; i < n; i++) {
            randomizedQueue.enqueue(i);
        }
        System.out.println("enqueued " + n + " elements");
        System.out.println("size: " + randomizedQueue.size());
        System.out.println("sample: " + randomizedQueue.sample());
        System.out.println("dequeue: " + randomizedQueue.dequeue());
        System.out.println("size: " + randomizedQueue.size());
        System.out.println("empty: " + randomizedQueue.isEmpty());
        System.out.println("run iterator");
        for (Integer element : randomizedQueue) {
            System.out.println(element);
        }
    }

}
