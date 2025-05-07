package Models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
public class ChiTietTopping {
    @Id
    @Column(name = "MaChiTietTopping", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaChiTiet")
    private ChiTietHoaDon maChiTiet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaTopping")
    private Models.Topping maTopping;

    @ColumnDefault("1")
    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ChiTietHoaDon getMaChiTiet() {
        return maChiTiet;
    }

    public void setMaChiTiet(ChiTietHoaDon maChiTiet) {
        this.maChiTiet = maChiTiet;
    }

    public Models.Topping getMaTopping() {
        return maTopping;
    }

    public void setMaTopping(Models.Topping maTopping) {
        this.maTopping = maTopping;
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

}