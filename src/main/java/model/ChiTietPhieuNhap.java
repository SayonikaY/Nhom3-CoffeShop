package model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ChiTietPhieuNhap {
    @Id
    @Column(name = "MaChiTietNhap", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuNhap")
    private model.PhieuNhapKho maPhieuNhap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguyenLieu")
    private model.NguyenLieu maNguyenLieu;

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

    public model.PhieuNhapKho getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(model.PhieuNhapKho maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public model.NguyenLieu getMaNguyenLieu() {
        return maNguyenLieu;
    }

    public void setMaNguyenLieu(model.NguyenLieu maNguyenLieu) {
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