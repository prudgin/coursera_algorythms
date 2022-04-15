package queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    private boolean isEmpty;
    private Node firstNode;
    private Node lastNode;

    private class Node {
        Item value;
        Node leftNode;
        Node rightNode;

        private Node(Item value) {
            if (value == null) {
                throw new IllegalArgumentException("Null value not accepted");
            }
            this.value = value;
        }
    }

    private class DequeIterator implements Iterator<Item> {

        private Node currentNode;
        private boolean hasNext;

        private DequeIterator() {
            if (Deque.this.isEmpty()) {
                hasNext = false;
            } else {
                currentNode = Deque.this.firstNode;
                hasNext = true;
            }
        }

        @Override
        public boolean hasNext() {
            return !Deque.this.isEmpty && hasNext;
        }

        @Override
        public Item next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException("Deck empty.");
            }
            Item value = currentNode.value;
            currentNode = currentNode.rightNode;
            if (currentNode == null) {
                hasNext = false;
            }
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Method remove is not supported.");
        }
    }

    // construct an empty deque
    public Deque() {
        this.size = 0;
        this.isEmpty = true;
    }

    private void addNode(Item value, boolean first) {
        Node newNode = new Node(value);
        if (isEmpty) {
            firstNode = newNode;
            lastNode = newNode;
        } else {
            if (first) {
                newNode.rightNode = firstNode;
                firstNode.leftNode = newNode;
                firstNode = newNode;
            } else {
                newNode.leftNode = lastNode;
                lastNode.rightNode = newNode;
                lastNode = newNode;
            }

        }
        isEmpty = false;
        size++;
    }

    // add the item to the front
    public void addFirst(Item value) {
        addNode(value, true);
    }

    // add the item to the back
    public void addLast(Item value) {
        addNode(value, false);
    }

    private Item removeNode(boolean first) {
        if (isEmpty) {
            throw new NoSuchElementException("No popping from empty queue!");
        }
        Item value;
        if (size == 1) {
            value = firstNode.value;
            firstNode = null;
            lastNode = null;
            isEmpty = true;
        } else {
            if (first) {
                value = firstNode.value;
                firstNode = firstNode.rightNode;
                firstNode.leftNode = null;
            } else {
                value = lastNode.value;
                lastNode = lastNode.leftNode;
                lastNode.rightNode = null;
            }
        }
        size--;
        return value;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        return removeNode(true);
    }

    // remove and return the item from the back
    public Item removeLast() {
        return removeNode(false);
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // is the deque empty?
    public boolean isEmpty() {
        return isEmpty;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(1);
        System.out.println(deque.removeLast());
        deque.addFirst(2);
        System.out.println(deque.removeFirst());
        deque.addFirst(3);
        deque.addFirst(4);
        System.out.println("empty: " + deque.isEmpty());
        System.out.println("size: " + deque.size());
        Iterator<Integer> iterator = deque.iterator();
        System.out.println("has next: " + iterator.hasNext());
        for (Integer integer : deque) {
            System.out.println(integer);
        }
    }

}
