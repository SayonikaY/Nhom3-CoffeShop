package nhom03.model.entity;

public enum TrangThaiHoaDon {
    CHUA_THANH_TOAN, DA_THANH_TOAN, HUY;

    public static TrangThaiHoaDon fromString(String str) {
        return switch (str) {
            case "Chưa thanh toán" -> TrangThaiHoaDon.CHUA_THANH_TOAN;
            case "Đã thanh toán" -> TrangThaiHoaDon.DA_THANH_TOAN;
            case "Hủy" -> TrangThaiHoaDon.HUY;
            default -> throw new IllegalArgumentException("Invalid value: " + str);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case CHUA_THANH_TOAN -> "Chưa thanh toán";
            case DA_THANH_TOAN -> "Đã thanh toán";
            case HUY -> "Hủy";
        };
    }
}
