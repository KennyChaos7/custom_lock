public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");
        BlockQueue<String> mStringBlockQueue = new BlockQueue<>();
        System.out.println(String.valueOf(mStringBlockQueue.put("aaa")));
        System.out.println(String.valueOf(mStringBlockQueue.put("bbb")));
        System.out.println(String.valueOf(mStringBlockQueue.put("ccc")));
        System.out.println(String.valueOf(mStringBlockQueue.put("ddd")));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
        System.out.println(String.valueOf(mStringBlockQueue.poll().toString()));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
        System.out.println(String.valueOf(mStringBlockQueue.get(2).toString()));
        System.out.println(String.valueOf(mStringBlockQueue.toString()));
//        System.out.println(String.valueOf(mStringBlockQueue.get(99).toString()));
//        System.out.println(String.valueOf(mStringBlockQueue.toString()));

    }
}
