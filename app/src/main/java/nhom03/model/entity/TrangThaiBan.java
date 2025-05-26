package nhom03.model.entity;

public enum TrangThaiBan {
    TRONG, DANG_SU_DUNG, DAT_TRUOC;

    public static TrangThaiBan fromString(String str) {
        return switch (str) {
            case "Trống" -> TrangThaiBan.TRONG;
            case "Đang sử dụng" -> TrangThaiBan.DANG_SU_DUNG;
            case "Đặt trước" -> TrangThaiBan.DAT_TRUOC;
            default -> throw new IllegalArgumentException("Invalid value: " + str);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case TRONG -> "Trống";
            case DANG_SU_DUNG -> "Đang sử dụng";
            case DAT_TRUOC -> "Đặt trước";
        };
    }
}
