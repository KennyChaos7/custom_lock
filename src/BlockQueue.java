import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 {@link java.util.concurrent.locks.ReentrantLock 重入锁}
 * @param <T>
 */
public class BlockQueue<T> {

    public class Node<E> implements Serializable {
        private final String TAG = getClass().getSimpleName();
        private Node next = null;
        private E value = null;

        Node(){}
        Node(E e,Node<E> next){
            value = e;
            this.next = next;
        }
        E getValue() {
            return value;
        }
        void setValue(E e) {
            value = e;
        }

        @Override
        public String toString(){
            return value.toString();
        }

        @Override
        public int hashCode() {
            return value.hashCode() + TAG.hashCode();
        }
    }

    private int size = 0;
    private Node<T> first = null;
    private Node<T> last = null;
    private ReentrantLock mLock = null;
    private final static int MAX_CAPACITY = 1000000;

    public BlockQueue(){
        mLock = new ReentrantLock();
    }

    /**
     *
     * @param t
     * @return
     */
    public Integer put(T t) {
        if (size < MAX_CAPACITY) {
            if (first == null) {
                first = new Node<>(t, null);
            } else {
                last = first;
                first = new Node<>(t, last);
            }
            size++;
            return size;
        }else {
            poll();
            put(t);
            return MAX_CAPACITY;
        }
    }

    /**
     *
     * @return
     */
    public Node<T> poll(){
        if (size <= 1) {
            size = 0;
            return first;
        }
        else {
            size--;
            Node<T> tmp = first;
            first = first.next;
            return tmp;
        }
    }

    /**
     *
     * @param index
     * @return
     */
    public Node<T> get(int index){
        if (index >= size)
            return null;
        Node<T> tmp = first;
        for (int i = 0;i < size;i++) {
            if (i == index) {
                break;
            }else {
                tmp = tmp.next;
            }
        }
        return tmp;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        Node<T> tmp = first;
        stringBuilder.append("size = ").append(size).append("\n");
        for (int index = 0;index < size; index++){
            if (tmp != null) {
                stringBuilder.append("Index = ").append(index)
                        .append(" Value = ").append(tmp.toString())
                        .append("\n");
                tmp = tmp.next;
            }else
                break;
        }
        return stringBuilder.toString();
    }


}
