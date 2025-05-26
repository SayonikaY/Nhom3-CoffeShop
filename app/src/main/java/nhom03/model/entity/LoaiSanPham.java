package nhom03.model.entity;

public enum LoaiSanPham {
    DO_UONG, DO_AN, KHAC;

    public static LoaiSanPham fromString(String str) {
        return switch (str) {
            case "Đồ uống" -> LoaiSanPham.DO_UONG;
            case "Đồ ăn" -> LoaiSanPham.DO_AN;
            case "Khác" -> LoaiSanPham.KHAC;
            default -> throw new IllegalArgumentException("Invalid value: " + str);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case DO_UONG -> "Đồ uống";
            case DO_AN -> "Đồ ăn";
            case KHAC -> "Khác";
        };
    }
}
