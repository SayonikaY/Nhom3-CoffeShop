package Models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
public class NguyenLieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaNguyenLieu", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenNguyenLieu", nullable = false, length = 100)
    private String tenNguyenLieu;

    @Nationalized
    @Column(name = "DonViTinh", nullable = false, length = 50)
    private String donViTinh;

    @ColumnDefault("0")
    @Column(name = "SoLuongTon", precision = 10, scale = 2)
    private BigDecimal soLuongTon;

    @Column(name = "DonGiaNhap", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGiaNhap;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTenNguyenLieu() {
        return tenNguyenLieu;
    }

    public void setTenNguyenLieu(String tenNguyenLieu) {
        this.tenNguyenLieu = tenNguyenLieu;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public BigDecimal getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(BigDecimal soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public BigDecimal getDonGiaNhap() {
        return donGiaNhap;
    }

    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }

}