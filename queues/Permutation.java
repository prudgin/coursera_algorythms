package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import queues.RandomizedQueue;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k <= 0) {
            return;
        }
        int n = 1;
        boolean inputEmpty = false;
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();

        while (n <= k) {
            inputEmpty = StdIn.isEmpty();
            if (inputEmpty) {
                break;
            }
            randomizedQueue.enqueue(StdIn.readString());
            n++;
        }

        n = 1;
        while (!inputEmpty) {
            inputEmpty = StdIn.isEmpty();
            if (inputEmpty) {
                break;
            }
            String inputString = StdIn.readString();
            if (StdRandom.bernoulli((double) k / (double) (k + n))) {
                randomizedQueue.dequeue();
                randomizedQueue.enqueue(inputString);
            }
            n++;
        }


        for (int i = 0; i < k; i++) {
            System.out.println(randomizedQueue.dequeue());
        }
    }
}
