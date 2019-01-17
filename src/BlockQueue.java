import java.io.Serializable;


public class BlockQueue<T> {

    private class Node<T> implements Serializable {
        private final String TAG = getClass().getSimpleName();
        private Node next = null;
        private int index = -1;
        private T value = null;

        Node(){};
        Node(T t){
            value = t;
        }
        T getValue() {
            return value;
        }
        void setValue(T t) {
            value = t;
        }

        @Override
        public String toString(){
            return "Node - " + index + " - " + value.toString();
        }

        @Override
        public int hashCode() {
            return value.hashCode() + index;
        }
    }

    private int size = 0;
    private Node<T> node = null;

    public Integer put(T t) {
        if (size == 0) {
            node = new Node<>(t);
            node.next = null;
            node.index = 0;
        } else {
            Node<T> next = new Node<>(t);
            next.next = null;
            next.index = size + 1;
            node.next = next;
            node = next;
        }
        size++;
        return size;
    }

    public Node<T> poll(){
        return node;
    }
}
