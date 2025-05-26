package nhom03.model.entity;

public class Ban {
    private int maBan;
    private String tenBan;
    private String ghiChu;
    private TrangThaiBan trangThai;

    public Ban(int maBan, String tenBan, String ghiChu, TrangThaiBan trangThai) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.ghiChu = ghiChu;
        this.trangThai = trangThai;
    }

    public int getMaBan() {
        return maBan;
    }

    public void setMaBan(int maBan) {
        this.maBan = maBan;
    }

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public TrangThaiBan getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiBan trangThai) {
        this.trangThai = trangThai;
    }
}
