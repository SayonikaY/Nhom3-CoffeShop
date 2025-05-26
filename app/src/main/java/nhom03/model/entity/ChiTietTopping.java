package nhom03.model.entity;

import java.math.BigDecimal;

public class ChiTietTopping {
    private int maChiTietTopping;
    private ChiTietHoaDon chiTietHoaDon;
    private Topping topping;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietTopping(int maChiTietTopping, ChiTietHoaDon chiTietHoaDon, Topping topping,
                          int soLuong, BigDecimal donGia) {
        this.maChiTietTopping = maChiTietTopping;
        this.chiTietHoaDon = chiTietHoaDon;
        this.topping = topping;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public int getMaChiTietTopping() {
        return maChiTietTopping;
    }

    public void setMaChiTietTopping(int maChiTietTopping) {
        this.maChiTietTopping = maChiTietTopping;
    }

    public ChiTietHoaDon getChiTietHoaDon() {
        return chiTietHoaDon;
    }

    public void setChiTietHoaDon(ChiTietHoaDon chiTietHoaDon) {
        this.chiTietHoaDon = chiTietHoaDon;
    }

    public Topping getTopping() {
        return topping;
    }

    public void setTopping(Topping topping) {
        this.topping = topping;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }
}
