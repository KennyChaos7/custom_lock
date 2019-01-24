import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 {@link java.util.concurrent.locks.ReentrantLock 重入锁}
 * 默认是非公平锁
 * @param <T>
 */
public class BlockQueue<T> {
    private final static int DEFAULT_CAPACITY = 1000000;
    private final static boolean DEFAULT_ISFAIR = false;
    

    private ReentrantLock mLock = null;
    private int capacity = 0;
    private volatile int size = 0;
    private Node<T> first = null;
    private Node<T> last = null;
    private Node<T> tmp = new Node<>();

    /**
     * 默认创建为非公平锁
     * 可以创建非公平和公平锁
     */
    public BlockQueue(){
        this(DEFAULT_CAPACITY,DEFAULT_ISFAIR);
    }
    public BlockQueue (boolean isFair) {
        this(DEFAULT_CAPACITY,isFair);
    }
    public BlockQueue (int capacity, boolean isFair) {
        this.capacity = capacity;
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
        if (size < capacity) {
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

    /**
     * 对比是否在队列中
     * @param target  要对比的节点
     * @return
     */
    public boolean contains(Node<T> target)
    {
        if (target == null)
            return false;
        mLock.lock();
        for (Node point = first; point != null; point = point.next){
            if (point.hashCode() == target.hashCode()) {
                mLock.unlock();
                return true;
            }
        }
        mLock.unlock();
        return false;
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
class Node<E> implements Serializable {
    private final String TAG = getClass().getSimpleName();
    public Node next = null;
    public E value = null;

    Node(){}
    Node(E e){
        value = e;
        this.next = null;
    }
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
