package model;

import jakarta.persistence.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
public class ChiTietHoaDon {
    @Id
    @Column(name = "MaChiTiet", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaHoaDon")
    private model.HoaDon maHoaDon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaSanPham")
    private model.SanPham maSanPham;

    @Column(name = "SoLuong", nullable = false)
    private Integer soLuong;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia;

    @Nationalized
    @Lob
    @Column(name = "GhiChu")
    private String ghiChu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public model.HoaDon getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(model.HoaDon maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public model.SanPham getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(model.SanPham maSanPham) {
        this.maSanPham = maSanPham;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}