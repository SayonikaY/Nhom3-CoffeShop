package nhom03.model.dao;

import nhom03.model.entity.SanPham;

import java.util.List;
import java.util.Optional;

public interface SanPhamDao {
    void insert(SanPham sanPham);

    void update(SanPham sanPham);

    void delete(SanPham sanPham);

    void deleteById(int maSanPham);

    List<SanPham> findAll();

    Optional<SanPham> findById(int maSanPham);
}
