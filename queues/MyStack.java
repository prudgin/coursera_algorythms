package queues;

public class MyStack<T> {

    private Node currentNode;

    public MyStack() {
        this.currentNode = new Node();
        currentNode.underNode = null;
    }

    private class Node {
        T value;
        Node underNode;
    }

    public void push(T pushedVal) {
        currentNode.value = pushedVal;
        Node newNode = new Node();
        newNode.underNode = currentNode;
        currentNode = newNode;
    }

    public T pop() {
        Node underNode = currentNode.underNode;
        if (underNode != null) {
            currentNode = currentNode.underNode;
            return currentNode.value;
        } else {
            System.out.println("End of stack!");
        }
        return null;
    }
}
