package Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ChiTietPhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaChiTietNhap", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuNhap")
    private Models.PhieuNhapKho maPhieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguyenLieu")
    private Models.NguyenLieu maNguyenLieu;

    @Column(name = "SoLuong", nullable = false, precision = 10, scale = 2)
    private BigDecimal soLuong;

    @Column(name = "DonGia", nullable = false, precision = 10, scale = 2)
    private BigDecimal donGia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Models.PhieuNhapKho getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(Models.PhieuNhapKho maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public Models.NguyenLieu getMaNguyenLieu() {
        return maNguyenLieu;
    }

    public void setMaNguyenLieu(Models.NguyenLieu maNguyenLieu) {
        this.maNguyenLieu = maNguyenLieu;
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