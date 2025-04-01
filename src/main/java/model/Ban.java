package model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity
public class Ban {
    @Id
    @Column(name = "MaBan", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "TenBan", nullable = false, length = 50)
    private String tenBan;

    @Nationalized
    @ColumnDefault("N'Trống'")
    @Column(name = "TrangThai", length = 20)
    private String trangThai;

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

    public String getTenBan() {
        return tenBan;
    }

    public void setTenBan(String tenBan) {
        this.tenBan = tenBan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}