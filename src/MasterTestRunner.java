import java.util.Random;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MasterTestRunner {
    private static final int TEST_RUNS = 10;
    private static final int MAX_CUTOFF = 30;
    private static final long SEED = 42;

    public static void main(String[] args) throws IOException {
        int[] sizes = {5000, 10000, 100000, 200000, 1000000, 2000000};
        String[] scenarioNames = {
                "1. NgauNhien", "2. DaoNguoc", "3. GiongNhauHoanToan",
                "4. ItGiaTriPhanBiet", "5. XepMotPhan_Log", "6. XepMotPhan_TuyenTinh",
                "7. HinhOng", "8. LechVong", "9. ChuoiNgauNhien"
        };

        PrintWriter csv = new PrintWriter(new FileWriter("KetQua_ThucNghiem_NhanhCham.csv"));
        csv.println("KichThuoc,KichBan,Cutoff,Merge_ms,Quick_ms");

        System.out.println("=========================================================================================");
        System.out.println("      THỰC NGHIỆM TÌM CUTOFF NHANH NHẤT VÀ CHẬM NHẤT TRÊN 9 KỊCH BẢN");
        System.out.println("=========================================================================================");
        System.out.println("--> Đang khởi động JVM (Warmup)...");
        warmup();
        System.out.println("--> Khởi động xong! Bắt đầu đo...\n");

        for (int N : sizes) {
            System.out.println("#########################################################################################");
            System.out.printf(" KÍCH THƯỚC DỮ LIỆU: N = %,d\n", N);
            System.out.println("#########################################################################################");

            for (int s = 0; s < 9; s++) {
                System.out.println("\n============================================================");
                System.out.println(" KỊCH BẢN: " + scenarioNames[s]);
                System.out.println("============================================================");

                Comparable[] data = generateData(s, N);

                System.out.printf("%-8s | %-20s | %-20s\n", "CUTOFF", "MergeOptimized(ms)", "QuickOptimized(ms)");
                System.out.println("------------------------------------------------------------");

                // Các biến để theo dõi Cutoff nhanh nhất và chậm nhất
                double minMergeMs = Double.MAX_VALUE, maxMergeMs = -1.0;
                int bestMergeCutoff = -1, worstMergeCutoff = -1;

                double minQuickMs = Double.MAX_VALUE, maxQuickMs = -1.0;
                int bestQuickCutoff = -1, worstQuickCutoff = -1;

                for (int cutoff = 0; cutoff <= MAX_CUTOFF; cutoff++) {
                    long totalMergeNano = 0;
                    long totalQuickNano = 0;

                    for (int i = 0; i < TEST_RUNS; i++) {
                        Comparable[] mergeCopy = data.clone();
                        Comparable[] quickCopy = data.clone();

                        long startM = System.nanoTime();
                        MergeOptimized.sort(mergeCopy, cutoff);
                        totalMergeNano += (System.nanoTime() - startM);

                        long startQ = System.nanoTime();
                        QuickOptimized.sort(quickCopy, cutoff);
                        totalQuickNano += (System.nanoTime() - startQ);
                    }

                    double avgMergeMs = (totalMergeNano / (double) TEST_RUNS) / 1_000_000.0;
                    double avgQuickMs = (totalQuickNano / (double) TEST_RUNS) / 1_000_000.0;

                    // Cập nhật đỉnh/đáy cho Mergesort
                    if (avgMergeMs < minMergeMs) { minMergeMs = avgMergeMs; bestMergeCutoff = cutoff; }
                    if (avgMergeMs > maxMergeMs) { maxMergeMs = avgMergeMs; worstMergeCutoff = cutoff; }

                    // Cập nhật đỉnh/đáy cho Quicksort
                    if (avgQuickMs < minQuickMs) { minQuickMs = avgQuickMs; bestQuickCutoff = cutoff; }
                    if (avgQuickMs > maxQuickMs) { maxQuickMs = avgQuickMs; worstQuickCutoff = cutoff; }

                    System.out.printf("%-8d | %-20.5f | %-20.5f\n", cutoff, avgMergeMs, avgQuickMs);
                    csv.printf("%d,%s,%d,%.5f,%.5f\n", N, scenarioNames[s], cutoff, avgMergeMs, avgQuickMs);
                }

                // IN RA TỔNG KẾT CHO KỊCH BẢN HIỆN TẠI
                System.out.println("------------------------------------------------------------");
                System.out.println(">>> KẾT LUẬN CHO KỊCH BẢN " + scenarioNames[s] + " (N = " + N + "):");
                System.out.println("[MERGESORT]");
                System.out.printf("  + NHANH nhất tại CUTOFF = %-2d (Thời gian: %.5f ms)\n", bestMergeCutoff, minMergeMs);
                System.out.printf("  + CHẬM  nhất tại CUTOFF = %-2d (Thời gian: %.5f ms)\n", worstMergeCutoff, maxMergeMs);

                System.out.println("[QUICKSORT]");
                System.out.printf("  + NHANH nhất tại CUTOFF = %-2d (Thời gian: %.5f ms)\n", bestQuickCutoff, minQuickMs);
                System.out.printf("  + CHẬM  nhất tại CUTOFF = %-2d (Thời gian: %.5f ms)\n", worstQuickCutoff, maxQuickMs);
            }
        }
        csv.close();
        System.out.println("\n=> Đã xuất toàn bộ kết quả chi tiết ra file 'KetQua_ThucNghiem_NhanhCham.csv'");
    }

    private static void warmup() {
        Comparable[] dummy = generateData(0, 5000);
        for (int i = 0; i < 200; i++) {
            MergeOptimized.sort(dummy.clone(), 10);
            QuickOptimized.sort(dummy.clone(), 10);
        }
    }

    // --- BỘ SINH DỮ LIỆU TƯƠNG ỨNG 9 KỊCH BẢN ---
    private static Comparable[] generateData(int type, int n) {
        switch (type) {
            case 0: return genNgauNhien(n);
            case 1: return genDaoNguoc(n);
            case 2: return genGiongNhau(n);
            case 3: return genItGiaTri(n);
            case 4: return genXepMotPhanLog(n);
            case 5: return genXepMotPhanTuyenTinh(n);
            case 6: return genHinhOng(n);
            case 7: return genLechVong(n);
            case 8: return genChuoiNgauNhien(n);
            default: return genNgauNhien(n);
        }
    }

    private static Comparable[] genNgauNhien(int n) {
        Integer[] arr = new Integer[n];
        Random r = new Random(SEED);
        for(int i=0; i<n; i++) arr[i] = r.nextInt(n);
        return arr;
    }

    private static Comparable[] genDaoNguoc(int n) {
        Integer[] arr = new Integer[n];
        for(int i=0; i<n; i++) arr[i] = n - i;
        return arr;
    }

    private static Comparable[] genGiongNhau(int n) {
        Integer[] arr = new Integer[n];
        for(int i=0; i<n; i++) arr[i] = 1;
        return arr;
    }

    private static Comparable[] genItGiaTri(int n) {
        Integer[] arr = new Integer[n];
        Random r = new Random(SEED);
        for(int i=0; i<n; i++) arr[i] = r.nextInt(3);
        return arr;
    }

    private static Comparable[] genXepMotPhanLog(int n) {
        Integer[] arr = new Integer[n];
        for(int i=0; i<n; i++) arr[i] = i;
        Random r = new Random(SEED);
        int swaps = Math.max(1, (int)(Math.log(n) / Math.log(2)));
        for(int i=0; i<swaps; i++) swapArr(arr, r.nextInt(n), r.nextInt(n));
        return arr;
    }

    private static Comparable[] genXepMotPhanTuyenTinh(int n) {
        Integer[] arr = new Integer[n];
        for(int i=0; i<n; i++) arr[i] = i;
        Random r = new Random(SEED);
        int swaps = n / 10;
        for(int i=0; i<swaps; i++) swapArr(arr, r.nextInt(n), r.nextInt(n));
        return arr;
    }

    private static Comparable[] genHinhOng(int n) {
        Integer[] arr = new Integer[n];
        int mid = n / 2;
        for(int i=0; i<=mid; i++) arr[i] = i;
        for(int i=mid+1; i<n; i++) arr[i] = n - i;
        return arr;
    }

    private static Comparable[] genLechVong(int n) {
        Integer[] arr = new Integer[n];
        arr[0] = n;
        for(int i=1; i<n; i++) arr[i] = i;
        return arr;
    }

    private static Comparable[] genChuoiNgauNhien(int n) {
        String[] arr = new String[n];
        Random r = new Random(SEED);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for(int i=0; i<n; i++) {
            StringBuilder sb = new StringBuilder(20);
            for(int j=0; j<20; j++) sb.append(chars.charAt(r.nextInt(chars.length())));
            arr[i] = sb.toString();
        }
        return arr;
    }

    private static void swapArr(Object[] arr, int i, int j) {
        Object t = arr[i]; arr[i] = arr[j]; arr[j] = t;
    }
}