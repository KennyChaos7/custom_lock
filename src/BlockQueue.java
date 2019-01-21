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

    private int size = 0;
    private Node<T> first = null;
    private Node<T> last = null;
    private Node<T> tmp = new Node<>();
    private ReentrantLock mLock = null;
    private final static int MAX_CAPACITY = 1000000;

    public BlockQueue(){
        mLock = new ReentrantLock(false);
    }

    /**
     *
     * @param t
     * @return
     */
    public Integer put(T t) {
        try {
            mLock.lock();
            if (!mLock.isLocked())
                mLock.lockInterruptibly();
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
        }catch (InterruptedException ex){
//            ex.printStackTrace();
        }finally {
            mLock.unlock();
            return size;
        }
    }

    /**
     *
     * @return
     */
    public Node<T> poll() {
        mLock.lock();
        try {
            if (!mLock.isLocked())
                mLock.lockInterruptibly();
            tmp = first;
            if (size <= 1) {
                size = 0;
            } else {
                size--;
                first = first.next;
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
        }finally {
            mLock.unlock();
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
