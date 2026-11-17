import java.util.Arrays;
import java.util.Random;

public class CutoffTestRunner {

    private static final int WARMUP_RUNS = 2_000;
    private static final int TEST_RUNS = 5_000;
    private static final long SEED = 42;

    public static void main(String[] args) {
        // So sánh trên 2 kích thước mảng: vừa (1.000) và lớn (100.000)
        int[] sizes = {1_000, 100_000};

        System.out.println("=======================================================================");
        System.out.println("         SO SÁNH CUTOFF = 7 VÀ CUTOFF = 10 TRÊN 4 ĐIỀU KIỆN");
        System.out.println("=======================================================================");

        for (int N : sizes) {
            System.out.println("\n=======================================================================");
            System.out.printf("KÍCH THƯỚC MẢNG N = %,d\n", N);
            System.out.println("=======================================================================");

            System.out.println("--> Đang khởi động JVM (Warmup)...");
            warmup(N);

            runComparison("1. Mảng Ngẫu Nhiên", generateRandomArray(N, SEED));
            runComparison("2. Mảng Gần Sắp Xếp", generateNearlySortedArray(N, SEED));
            runComparison("3. Mảng Sắp Xếp Ngược", generateReverseArray(N));
            runComparison("4. Mảng Nhiều Trùng Lặp", generateDuplicateArray(N, 10, SEED));
        }
    }

    private static void runComparison(String testName, Integer[] originalData) {
        System.out.println("\n--- " + testName + " ---");
        System.out.printf("%-18s | %-15s | %-15s | %-12s\n",
                "Thuật toán", "Cutoff = 7 (ms)", "Cutoff = 10 (ms)", "Kết luận");
        System.out.println("-----------------------------------------------------------------------");

        double timeMerge7 = testMergeSort(originalData, 7);
        double timeMerge10 = testMergeSort(originalData, 10);
        printRow("MergeOptimized", timeMerge7, timeMerge10);

        double timeQuick7 = testQuickSort(originalData, 7);
        double timeQuick10 = testQuickSort(originalData, 10);
        printRow("QuickOptimized", timeQuick7, timeQuick10);
    }

    private static double testMergeSort(Integer[] originalData, int cutoff) {
        long totalTime = 0;
        for (int i = 0; i < TEST_RUNS; i++) {
            Integer[] data = originalData.clone();
            long start = System.nanoTime();
            MergeOptimized.sort(data, cutoff);
            totalTime += (System.nanoTime() - start);
        }
        return (totalTime / (double) TEST_RUNS) / 1_000_000.0;
    }

    private static double testQuickSort(Integer[] originalData, int cutoff) {
        long totalTime = 0;
        for (int i = 0; i < TEST_RUNS; i++) {
            Integer[] data = originalData.clone();
            long start = System.nanoTime();
            QuickOptimized.sort(data, cutoff);
            totalTime += (System.nanoTime() - start);
        }
        return (totalTime / (double) TEST_RUNS) / 1_000_000.0;
    }

    private static void printRow(String algoName, double time7, double time10) {
        double diff = ((time10 - time7) / time7) * 100;
        String winner = (Math.abs(diff) < 1.0) ? "Ngang nhau" : (time7 < time10 ? "Cutoff 7" : "Cutoff 10");
        System.out.printf("%-18s | %15.4f | %15.4f | %-12s\n", algoName, time7, time10, winner);
    }

    private static void warmup(int N) {
        Integer[] dummy = generateRandomArray(N, 123);
        for (int i = 0; i < WARMUP_RUNS; i++) {
            MergeOptimized.sort(dummy.clone(), 7);
            MergeOptimized.sort(dummy.clone(), 10);
            QuickOptimized.sort(dummy.clone(), 7);
            QuickOptimized.sort(dummy.clone(), 10);
        }
    }

    // --- CÁC HÀM TỰ SINH DỮ LIỆU ---
    private static Integer[] generateRandomArray(int n, long seed) {
        Random rand = new Random(seed); Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = rand.nextInt(1_000_000); return arr;
    }
    private static Integer[] generateNearlySortedArray(int n, long seed) {
        Integer[] arr = generateRandomArray(n, seed); Arrays.sort(arr);
        Random rand = new Random(seed); int swaps = Math.max(1, n / 20);
        for (int i = 0; i < swaps; i++) {
            int idx1 = rand.nextInt(n), idx2 = rand.nextInt(n);
            Integer temp = arr[idx1]; arr[idx1] = arr[idx2]; arr[idx2] = temp;
        } return arr;
    }
    private static Integer[] generateReverseArray(int n) {
        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = n - i; return arr;
    }
    private static Integer[] generateDuplicateArray(int n, int numUnique, long seed) {
        Random rand = new Random(seed); Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++) arr[i] = rand.nextInt(numUnique); return arr;
    }
}