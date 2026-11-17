public class MergeOptimized {

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) aux[k] = a[k];

        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)                   a[k] = aux[j++];
            else if (j > hi)                  a[k] = aux[i++];
            else if (less(aux[j], aux[i]))     a[k] = aux[j++];
            else                               a[k] = aux[i++];
        }
    }

    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
        if (hi <= lo) return; // Chống lỗi StackOverflow

        // Cải tiến 1: Ngắt Cutoff
        if (hi <= lo + cutoff - 1) {
            Insertion.sort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cutoff);
        sort(a, aux, mid + 1, hi, cutoff);

        // Cải tiến 2: Bỏ qua merge nếu đã sắp xếp
        if (!less(a[mid + 1], a[mid])) return;

        merge(a, aux, lo, mid, hi);
    }

    public static void sort(Comparable[] a, int cutoff) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1, cutoff);
    }

    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}