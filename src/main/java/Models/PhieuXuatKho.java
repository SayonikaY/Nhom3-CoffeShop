package Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Entity
public class PhieuXuatKho {
    @Id
    @Column(name = "MaPhieuXuat", nullable = false)
    private Integer id;

    @ColumnDefault("getdate()")
    @Column(name = "NgayXuat")
    private Instant ngayXuat;

    @Nationalized
    @Column(name = "LyDo")
    private String lyDo;

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

    public Instant getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(Instant ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}