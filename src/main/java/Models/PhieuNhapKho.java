package Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class PhieuNhapKho {
    @Id
    @Column(name = "MaPhieuNhap", nullable = false)
    private Integer id;

    @ColumnDefault("getdate()")
    @Column(name = "NgayNhap")
    private Instant ngayNhap;

    @ColumnDefault("0")
    @Column(name = "TongTien", precision = 10, scale = 2)
    private BigDecimal tongTien;

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

    public Instant getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(Instant ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}