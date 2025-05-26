package nhom03.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PhieuNhapKho {
    private int maPhieuNhap;
    private LocalDateTime ngayNhap;
    private BigDecimal tongTien;
    private String ghiChu;

    public PhieuNhapKho(int maPhieuNhap, LocalDateTime ngayNhap, BigDecimal tongTien, String ghiChu) {
        this.maPhieuNhap = maPhieuNhap;
        this.ngayNhap = ngayNhap;
        this.tongTien = tongTien;
        this.ghiChu = ghiChu;
    }

    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
