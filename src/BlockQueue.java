import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 {@link java.util.concurrent.locks.ReentrantLock 重入锁}
 * 默认是非公平锁
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
            if (value != null)
                return value.toString();
            else
                return "";
        }

        @Override
        public int hashCode() {
            if (value != null)
                return Math.toIntExact(Thread.currentThread().getId() + value.hashCode() + TAG.hashCode());
            else
                return Math.toIntExact(Thread.currentThread().getId() + TAG.hashCode());
        }
    }

    private final static boolean IS_FAIR = false;
    private final static int MAX_CAPACITY = 1000000;
    private volatile int size = 0;
    private Node<T> first = null;
    private Node<T> last = null;
    private Node<T> tmp = new Node<>();
    private ReentrantLock mLock = null;

    /**
     * 默认创建为非公平锁
     */
    public BlockQueue()
    {
        this(IS_FAIR);
    }

    /**
     * 可以创建非公平和公平锁
     * @param isFair
     */
    public BlockQueue(boolean isFair){
        mLock = new ReentrantLock(isFair);
    }

    /**
     *  压入
     *  压入到队列的末端
     * @param t 对应的泛型
     * @return 返回队列存储量size
     */
    public Integer put(T t) {
        mLock.lock();
        if (size < MAX_CAPACITY) {
            if (first == null) {
                first = new Node<>(t, null);
            } else {
                last = first;
                first = new Node<>(t, last);
            }
            size++;
        } else {
            poll();
            put(t);
        }
        mLock.unlock();
        return size;
    }


    /**
     * 弹出
     * @return 返回的是当前队列里的第一个Node
     */
    public Node<T> poll() {
        mLock.lock();
        tmp = first;
        if (size <= 1) {
            size = 0;
        } else {
            size--;
            first = first.next;
        }
        mLock.unlock();
        return tmp;
    }

    /**
     * 获得任意索引位置的Node
     * @param index 索引位置
     * @return 返回该索引位置的Node
     * @throws NullPointerException 如果该索引位置未有Node,将抛出该异常
     */
    public Node<T> get(int index){
        if (index >= size)
            return null;
        tmp = first;
        for (int i = 0;i < size;i++) {
            if (i == index) {
                break;
            }else {
                tmp = tmp.next;
            }
        }
        return tmp;
    }

    /**
     * 获得当前队列的长度
     * @return  当前队列的长度
     */
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
