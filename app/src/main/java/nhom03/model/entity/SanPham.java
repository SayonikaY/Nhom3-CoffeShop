package nhom03.model.entity;

import java.math.BigDecimal;

public class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private LoaiSanPham loaiSanPham;
    private BigDecimal donGia;
    private String moTa;
    private String hinhAnh;
    private TrangThaiSanPham trangThai;

    public SanPham(int maSanPham, String tenSanPham, LoaiSanPham loaiSanPham, BigDecimal donGia,
                   String moTa, String hinhAnh, TrangThaiSanPham trangThai) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.loaiSanPham = loaiSanPham;
        this.donGia = donGia;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.trangThai = trangThai;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public TrangThaiSanPham getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiSanPham trangThai) {
        this.trangThai = trangThai;
    }
}
