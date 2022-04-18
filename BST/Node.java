package BST;

import org.jetbrains.annotations.NotNull;

class Node<K extends Comparable<K>, V> implements Comparable<Node<K, V>>{

    private final K key;
    private final V value;

    private Node<K, V> leftChild;
    private Node<K, V> rightChild;

    Node (K key, V value){
        if (key == null) {
            throw new IllegalArgumentException("Node constructor got null key.");
        }
        this.key = key;
        this.value = value;
    }

    public Node<K, V> getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node<K, V> leftChild) {
        System.out.println("setting left child to: " + leftChild);
        this.leftChild = leftChild;
    }

    public Node<K, V> getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node<K, V> rightChild) {
        System.out.println("setting right child to: " + rightChild);
        this.rightChild = rightChild;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "key=" + key +
                ", value=" + value +
                ", leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                '}';
    }

    @Override
    public int compareTo(@NotNull Node<K, V> other) {
        return key.compareTo(other.key);
    }

}
