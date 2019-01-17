public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        BlockQueue<String> mStringBlockQueue = new BlockQueue<>();
        System.out.println(String.valueOf(mStringBlockQueue.put("sss")));
        System.out.println(String.valueOf(mStringBlockQueue.put("bbb")));

    }
}
