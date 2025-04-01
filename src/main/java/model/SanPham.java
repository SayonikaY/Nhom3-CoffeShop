package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
public class SanPham {
    @Id
    @Column(name = "MaSanPham", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenSanPham", nullable = false, length = 100)
    private String tenSanPham;

    @Nationalized
    @Column(name = "LoaiSanPham", nullable = false, length = 20)
    private String loaiSanPham;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia;

    @Nationalized
    @Lob
    @Column(name = "MoTa")
    private String moTa;

    @Nationalized
    @Column(name = "HinhAnh")
    private String hinhAnh;

    @Nationalized
    @ColumnDefault("N'Còn'")
    @Column(name = "TrangThai", length = 10)
    private String trangThai;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}