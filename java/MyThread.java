public class MyThread extends Thread {
    private float[] arr;

    MyThread(String name, float[] arr) {
        super(name);
        this.arr = arr;

    }

    float[] getArr() {
        return arr;
    }

    @Override
    public void run() {
//        System.out.println(Thread.currentThread().getName() + " start");
        calculate();
    }

    private void calculate() {
        int len = arr.length;

        for (int i = 0; i < len; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }
}