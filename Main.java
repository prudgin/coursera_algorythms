public class Main {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        rq.enqueue("A");
        rq.enqueue("B");
        rq.enqueue("C");
        int aCount = 0;
        int bCount = 0;
        int cCount = 0;
        for (int i = 0; i < 1000; i++){
            String val = rq.sample();
            switch (val){
                case ("A"):
                    aCount++;
                    break;
                case ("B"):
                    bCount++;
                    break;
                case ("C"):
                    cCount++;
                    break;
            }
        }
        System.out.println("A: " + aCount);
        System.out.println("B: " + bCount);
        System.out.println("C: " + cCount);

    }
}
