import java.util.Random;

public class QuickOptimized {

    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        Comparable v = a[lo];
        while (true) {
            while (less(a[++i], v)) if (i == hi) break;
            while (less(v, a[--j])) if (j == lo) break;
            if (i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }

    private static int medianOf3(Comparable[] a, int i, int j, int k) {
        return (less(a[i], a[j]) ?
                (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i) :
                (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
    }

    private static void sort(Comparable[] a, int lo, int hi, int cutoff) {
        if (hi <= lo) return; // Chống lỗi StackOverflow

        // Cải tiến 1: Ngắt Cutoff
        if (hi <= lo + cutoff - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }

        // Cải tiến 2: Median-of-3
        int mid = lo + (hi - lo) / 2;
        int m = medianOf3(a, lo, mid, hi);
        exch(a, lo, m);

        int j = partition(a, lo, hi);
        sort(a, lo, j - 1, cutoff);
        sort(a, j + 1, hi, cutoff);
    }

    public static void sort(Comparable[] a, int cutoff) {
        shuffle(a); // Cải tiến 3: Shuffle
        sort(a, 0, a.length - 1, cutoff);
    }

    private static void shuffle(Comparable[] a) {
        Random rand = new Random();
        for (int i = 0; i < a.length; i++) {
            int r = i + rand.nextInt(a.length - i);
            exch(a, i, r);
        }
    }

    private static boolean less(Comparable v, Comparable w) { return v.compareTo(w) < 0; }
    private static void exch(Object[] a, int i, int j) { Object swap = a[i]; a[i] = a[j]; a[j] = swap; }
}