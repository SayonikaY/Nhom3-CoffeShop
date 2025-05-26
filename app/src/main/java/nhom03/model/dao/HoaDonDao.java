package nhom03.model.dao;

import nhom03.model.entity.HoaDon;

import java.util.List;
import java.util.Optional;

public interface HoaDonDao {
    void insert(HoaDon hoaDon);

    void update(HoaDon hoaDon);

    void delete(HoaDon hoaDon);

    void deleteById(int maHoaDon);

    List<HoaDon> findAll();

    Optional<HoaDon> findById(int maHoaDon);

    List<HoaDon> findByMaBan(int maBan);
}
