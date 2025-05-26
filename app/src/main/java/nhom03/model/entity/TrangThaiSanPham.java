package nhom03.model.entity;

public enum TrangThaiSanPham {
    CON, HET, NGUNG;

    public static TrangThaiSanPham fromString(String str) {
        return switch (str) {
            case "Còn" -> TrangThaiSanPham.CON;
            case "Hết" -> TrangThaiSanPham.HET;
            case "Ngưng" -> TrangThaiSanPham.NGUNG;
            default -> throw new IllegalArgumentException("Invalid value: " + str);
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case CON -> "Còn";
            case HET -> "Hết";
            case NGUNG -> "Ngưng";
        };
    }
}
