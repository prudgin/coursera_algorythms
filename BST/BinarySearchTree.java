package BST;

public class BinarySearchTree<K extends Comparable<K>, V> {

    private Node<K, V> root;

    public BinarySearchTree(K rootKey, V rootValue) {
        this.root = new Node<>(rootKey, rootValue);
    }

    public void add(K key, V value) {
        root = add(key, value, root);
    }

    private Node<K, V> add(K key, V value, Node<K, V> potentialParentNode) {

        Node<K, V> toAddNode = new Node<>(key, value);

        if (potentialParentNode == null) {
            return toAddNode;
        }

        if (toAddNode.compareTo(potentialParentNode) <= 0) {
            potentialParentNode.setLeftChild(
                    add(key, value, potentialParentNode.getLeftChild())
            );

        } else {
            potentialParentNode.setRightChild(
                    add(key, value, potentialParentNode.getRightChild())
            );
        }

        return potentialParentNode;
    }

    @Override
    public String toString() {
        return "BinarySearchTree{" +
                "root=" + root +
                '}';
    }

    public static void main(String[] args) {
        BinarySearchTree<Integer, Integer> tree = new BinarySearchTree<Integer, Integer>(5, 5);
        System.out.println("adding 6, 6 ==================================");
        tree.add(6, 6);
        System.out.println(tree);
        System.out.println("adding 2, 2 ==================================");
        tree.add(2, 2);
        System.out.println(tree);
        System.out.println("adding 3, 3 ==================================");
        tree.add(3, 3);
        System.out.println(tree);
    }
}
