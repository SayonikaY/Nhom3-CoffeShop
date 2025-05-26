package nhom03.model.dao;

import nhom03.model.entity.ChiTietHoaDon;

import java.util.List;
import java.util.Optional;

public interface ChiTietHoaDonDao {
    void insert(ChiTietHoaDon chiTietHoaDon);

    void update(ChiTietHoaDon chiTietHoaDon);

    void delete(ChiTietHoaDon chiTietHoaDon);

    void deleteById(int maChiTiet);

    List<ChiTietHoaDon> findAll();

    Optional<ChiTietHoaDon> findById(int maChiTiet);

    List<ChiTietHoaDon> findByMaHoaDon(int maHoaDon);

    List<ChiTietHoaDon> findByMaSanPham(int maSanPham);
}
