import java.util.Arrays;
import java.util.Random;

public class ArrayGenerator {

    private static final long SEED = 42; // Seed cố định để kết quả thực nghiệm đồng nhất

    /**
     * Khởi tạo mảng dữ liệu dựa trên mã kịch bản và kích thước N
     * 0: Ngẫu nhiên | 1: Đảo ngược | 2: Giống nhau 100% | 3: Ít giá trị phân biệt
     * 4: Xếp 1 phần (Log) | 5: Xếp 1 phần (Tuyến tính) | 6: Hình ống | 7: Lệch vòng | 8: Chuỗi ngẫu nhiên
     */
    public static Comparable[] generateData(int type, int n) {
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

    // 1. Mảng ngẫu nhiên hoàn toàn (từ 0 đến N-1)
    public static Comparable[] genNgauNhien(int n) {
        Integer[] arr = new Integer[n];
        Random r = new Random(SEED);
        for(int i = 0; i < n; i++) arr[i] = r.nextInt(n);
        return arr;
    }

    // 2. Mảng đảo ngược hoàn toàn (giảm dần từ N xuống 1)
    public static Comparable[] genDaoNguoc(int n) {
        Integer[] arr = new Integer[n];
        for(int i = 0; i < n; i++) arr[i] = n - i;
        return arr;
    }

    // 3. Mảng trùng lặp 100%
    public static Comparable[] genGiongNhau(int n) {
        Integer[] arr = new Integer[n];
        for(int i = 0; i < n; i++) arr[i] = 1;
        return arr;
    }

    // 4. Mảng ít giá trị phân biệt (Ví dụ: chỉ có giá trị 0, 1, 2)
    public static Comparable[] genItGiaTri(int n) {
        Integer[] arr = new Integer[n];
        Random r = new Random(SEED);
        for(int i = 0; i < n; i++) arr[i] = r.nextInt(3);
        return arr;
    }

    // 5. Mảng sắp xếp gần đúng độ lệch Log N
    public static Comparable[] genXepMotPhanLog(int n) {
        Integer[] arr = new Integer[n];
        for(int i = 0; i < n; i++) arr[i] = i;
        Random r = new Random(SEED);
        int swaps = Math.max(1, (int)(Math.log(n) / Math.log(2)));
        for(int i = 0; i < swaps; i++) swapArr(arr, r.nextInt(n), r.nextInt(n));
        return arr;
    }

    // 6. Mảng sắp xếp gần đúng độ lệch tuyến tính (N/10)
    public static Comparable[] genXepMotPhanTuyenTinh(int n) {
        Integer[] arr = new Integer[n];
        for(int i = 0; i < n; i++) arr[i] = i;
        Random r = new Random(SEED);
        int swaps = n / 10;
        for(int i = 0; i < swaps; i++) swapArr(arr, r.nextInt(n), r.nextInt(n));
        return arr;
    }

    // 7. Mảng hình ống (tăng dần đến N/2 rồi giảm dần)
    public static Comparable[] genHinhOng(int n) {
        Integer[] arr = new Integer[n];
        int mid = n / 2;
        for(int i = 0; i <= mid; i++) arr[i] = i;
        for(int i = mid + 1; i < n; i++) arr[i] = n - i;
        return arr;
    }

    // 8. Mảng lệch vòng (phần tử lớn nhất nằm ở biên đầu tiên)
    public static Comparable[] genLechVong(int n) {
        Integer[] arr = new Integer[n];
        arr[0] = n;
        for(int i = 1; i < n; i++) arr[i] = i;
        return arr;
    }

    // 9. Mảng xâu ký tự ngẫu nhiên (dài 20 ký tự)
    public static Comparable[] genChuoiNgauNhien(int n) {
        String[] arr = new String[n];
        Random r = new Random(SEED);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder(20);
            for(int j = 0; j < 20; j++) sb.append(chars.charAt(r.nextInt(chars.length())));
            arr[i] = sb.toString();
        }
        return arr;
    }

    // Hàm bổ trợ hoán đổi 2 phần tử
    private static void swapArr(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }
}