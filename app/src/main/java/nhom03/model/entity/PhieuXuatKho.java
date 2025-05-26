package nhom03.model.entity;

import java.time.LocalDateTime;

public class PhieuXuatKho {
    private int maPhieuXuat;
    private LocalDateTime ngayXuat;
    private String lyDo;
    private String ghiChu;

    public PhieuXuatKho(int maPhieuXuat, LocalDateTime ngayXuat, String lyDo, String ghiChu) {
        this.maPhieuXuat = maPhieuXuat;
        this.ngayXuat = ngayXuat;
        this.lyDo = lyDo;
        this.ghiChu = ghiChu;
    }

    public int getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(int maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
    }

    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
