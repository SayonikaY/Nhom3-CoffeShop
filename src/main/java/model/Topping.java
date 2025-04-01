package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
public class Topping {
    @Id
    @Column(name = "MaTopping", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenTopping", nullable = false, length = 100)
    private String tenTopping;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia;

    @Nationalized
    @Lob
    @Column(name = "MoTa")
    private String moTa;

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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}