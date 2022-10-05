import java.util.*;
import java.util.function.Consumer;

public class BinaryTree<T extends Comparable<T>> implements Tree<T> {

    private class Node {
        private T value;
        private Node left;
        private Node right;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node root;

    @Override
    public void add(T item) {
        Objects.requireNonNull(item);
        root = appendNode(root, item);
    }

    private Node appendNode(Node current, T value) {
        if (current == null) {
            return new Node(value);
        }

        int compare = value.compareTo(current.value);
        if (compare < 0) {
            current.left = appendNode(current.left, value);
        } else if (compare > 0) {
            current.right = appendNode(current.right, value);
        }

        return current;
    }

    @Override
    public boolean contains(T item) {
        return Objects.nonNull(findNode(root, item));
    }

    private Node findNode(Node current, T value) {
        if (Objects.isNull(current)) {
            return null;
        }

        int compare = value.compareTo(current.value);
        if (compare < 0) {
            return findNode(current.left, value);
        } else if (compare > 0) {
            return findNode(current.right, value);
        }

        return current;
    }

    @Override
    public void remove(T item) {
        root = remove(root, item);
    }

    private Node remove(Node current, T value) {
        if (Objects.isNull(current)) {
            return null;
        }

        // current = 6
        // current.left = 4
        int compare = value.compareTo(current.value);
        if (compare < 0) {
            current.left = remove(current.left, value);
            return current;
        } else if (compare > 0) {
            current.right = remove(current.right, value);
            return current;
        }

        // У текущего узла нет дочерних узлов
        if (current.left == null && current.right == null) {
            return null;
        }

        // У текущего узла как минимум 1 ненулевой дочерний узел
        if (current.left == null) {
            return current.right;
        }
        if (current.right == null) {
            return current.left;
        }

        // У текущего узла оба ненулевых дочерних узла
        Node minOnTheRight = findMin(current.right);
        current.value = minOnTheRight.value;
        current.right = remove(current.right, minOnTheRight.value);
        return current;
    }

    private Node findMin(Node current) {
        Node left = current.left;
        return Objects.isNull(left) ? current : findMin(left);
    }

    public void dfs(Consumer<T> visitor) {
        // depth-first search
        dfs(root, visitor);
    }

    private void dfs(Node current, Consumer<T> visitor) {
        // depth-first search
        if (current != null) {
            dfs(current.left, visitor);
            visitor.accept(current.value);
            dfs(current.right, visitor);
        }
    }

    @Override
    public void bfs(Consumer<T> visitor) {
        // breadth-first search
        if (root == null) {
            return;
        }

        Queue<Node> nodes = new LinkedList<>();
        nodes.add(root);
        while (!nodes.isEmpty()) {
            Node node = nodes.poll();
            visitor.accept(node.value);

            if (node.left != null) {
                nodes.add(node.left);
            }

            if (node.right != null) {
                nodes.add(node.right);
            }
        }

    }

    private boolean isBalanced (Node current){
        if (current == null){
            return true;
        }
        return isBalanced(current.right) && isBalanced(current.left);
    }
    private int high(Node current){
        if (current == null){
            return 0;
        }
        return Math.max((high(current.left)+1), (high(current.right)+1));
    }

    @Override
    public boolean isBalanced() {
        // TODO: 29.09.2022 Проверить, является ли дерево сбалансированным.
        // Дерево является сбалансированным, если глубины от любого узла до любых двух листьев
        // отличаются не более, чем на 1
        if (root == null){
            return false;
        }
        return isBalanced(root.left) && isBalanced(root.right) && Math.abs(high(root.left)-high(root.right)) <= 1;

    }
}
