package nhom03.model.entity;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private int maChiTietNhap;
    private PhieuNhapKho phieuNhapKho;
    private NguyenLieu nguyenLieu;
    private BigDecimal soLuong;
    private BigDecimal donGia;

    public ChiTietPhieuNhap(int maChiTietNhap, PhieuNhapKho phieuNhapKho, NguyenLieu nguyenLieu,
                            BigDecimal soLuong, BigDecimal donGia) {
        this.maChiTietNhap = maChiTietNhap;
        this.phieuNhapKho = phieuNhapKho;
        this.nguyenLieu = nguyenLieu;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public int getMaChiTietNhap() {
        return maChiTietNhap;
    }

    public void setMaChiTietNhap(int maChiTietNhap) {
        this.maChiTietNhap = maChiTietNhap;
    }

    public PhieuNhapKho getPhieuNhapKho() {
        return phieuNhapKho;
    }

    public void setPhieuNhapKho(PhieuNhapKho phieuNhapKho) {
        this.phieuNhapKho = phieuNhapKho;
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

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
}
