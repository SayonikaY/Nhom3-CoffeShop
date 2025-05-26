package nhom03.model.entity;

import java.math.BigDecimal;

public class ChiTietPhieuXuat {
    private int maChiTietXuat;
    private PhieuXuatKho phieuXuatKho;
    private NguyenLieu nguyenLieu;
    private BigDecimal soLuong;

    public ChiTietPhieuXuat(int maChiTietXuat, PhieuXuatKho phieuXuatKho, NguyenLieu nguyenLieu,
                            BigDecimal soLuong) {
        this.maChiTietXuat = maChiTietXuat;
        this.phieuXuatKho = phieuXuatKho;
        this.nguyenLieu = nguyenLieu;
        this.soLuong = soLuong;
    }

    public int getMaChiTietXuat() {
        return maChiTietXuat;
    }

    public void setMaChiTietXuat(int maChiTietXuat) {
        this.maChiTietXuat = maChiTietXuat;
    }

    public PhieuXuatKho getPhieuXuatKho() {
        return phieuXuatKho;
    }

    public void setPhieuXuatKho(PhieuXuatKho phieuXuatKho) {
        this.phieuXuatKho = phieuXuatKho;
    }

    public NguyenLieu getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(NguyenLieu nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public BigDecimal getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(BigDecimal soLuong) {
        this.soLuong = soLuong;
    }
}
