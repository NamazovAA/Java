public class Main {
    private static final int size = 10000000;
    private static final int half = size / 2;
    private static float[] arr = new float[size];

    public static void main(String[] args) {

        for (int i = 0; i < size; i++) {
            arr[i] = 1;
        }
        long singleTime = firstThread(arr);
        long multiTime = secondThread(arr);
    }
    private static long firstThread(float[] arr) {
        long start = System.currentTimeMillis();

        for (int i = 0; i < size; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }

        long singleTime = System.currentTimeMillis() - start;

        System.out.println("first thread time: " + singleTime);
        return singleTime;
    }
    private static long secondThread(float[] arr){
        float[] a = new float[half];
        float[] b = new float[half];

        long start = System.currentTimeMillis();

        System.arraycopy(arr, 0, a, 0, half);
        System.arraycopy(arr, half, b, 0, half);

        MyThread t1 = new MyThread("a", a);
        MyThread t2 = new MyThread("b", b);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        a = t1.getArr();
        b = t2.getArr();

        System.arraycopy(a, 0, arr, 0, half);
        System.arraycopy(b, 0, arr, a.length, b.length);

        long multiTime = System.currentTimeMillis() - start;

        System.out.println("multi thread time: " + multiTime);

        return multiTime;
    }
}
