package nhom03.model.entity;

import java.math.BigDecimal;

public class Topping {
    private int maTopping;
    private String tenTopping;
    private BigDecimal donGia;
    private String moTa;
    private TrangThaiSanPham trangThai;

    public Topping(int maTopping, String tenTopping, BigDecimal donGia, String moTa, TrangThaiSanPham trangThai) {
        this.maTopping = maTopping;
        this.tenTopping = tenTopping;
        this.donGia = donGia;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public int getMaTopping() {
        return maTopping;
    }

    public void setMaTopping(int maTopping) {
        this.maTopping = maTopping;
    }

    public String getTenTopping() {
        return tenTopping;
    }

    public void setTenTopping(String tenTopping) {
        this.tenTopping = tenTopping;
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

    public TrangThaiSanPham getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(TrangThaiSanPham trangThai) {
        this.trangThai = trangThai;
    }
}
