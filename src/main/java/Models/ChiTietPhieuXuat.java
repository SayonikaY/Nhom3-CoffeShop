package Models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class ChiTietPhieuXuat {
    @Id
    @Column(name = "MaChiTietXuat", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaPhieuXuat")
    private Models.PhieuXuatKho maPhieuXuat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MaNguyenLieu")
    private Models.NguyenLieu maNguyenLieu;

    @Column(name = "SoLuong", nullable = false, precision = 10, scale = 2)
    private BigDecimal soLuong;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Models.PhieuXuatKho getMaPhieuXuat() {
        return maPhieuXuat;
    }

    public void setMaPhieuXuat(Models.PhieuXuatKho maPhieuXuat) {
        this.maPhieuXuat = maPhieuXuat;
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

}