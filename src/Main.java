import java.util.Random;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        BlockQueue<String> mStringBlockQueue = new BlockQueue<>();

        System.out.println("单线程");
        System.out.println(String.valueOf(mStringBlockQueue.put("aaa")));
        System.out.println(String.valueOf(mStringBlockQueue.put("bbb")));
        System.out.println(String.valueOf(mStringBlockQueue.put("ccc")));
        System.out.println(String.valueOf(mStringBlockQueue.put("ddd")));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
        System.out.println(String.valueOf(mStringBlockQueue.poll().toString()));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
        System.out.println(String.valueOf(mStringBlockQueue.get(2).toString()));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
        Node<String> node = new Node<>("ggg");
        System.out.println(String.valueOf(mStringBlockQueue.contains(node)));
        node = new Node<>("ccc");
        System.out.println(String.valueOf(mStringBlockQueue.contains(node)));
//        System.out.println(String.valueOf(mStringBlockQueue.get(99).toString()));
//        System.out.println(String.valueOf(mStringBlockQueue.toString()));

        //多线程
        BlockQueue<String> mThreadStringBlockQueue = new BlockQueue<>(true);
//        BlockQueue<String> mThreadStringBlockQueue = new BlockQueue<>(false);
        System.out.println("\n多线程");
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                mThreadStringBlockQueue.put(String.valueOf(random.nextInt(1000)));
            }).start();
            new Thread(() -> {
                System.out.println(String.valueOf(mThreadStringBlockQueue.poll().toString()));
                System.out.println(String.valueOf(mThreadStringBlockQueue.toString()));
            }).start();
        }
    }
}
