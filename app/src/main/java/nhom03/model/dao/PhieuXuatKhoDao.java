package nhom03.model.dao;

import nhom03.model.entity.PhieuXuatKho;

import java.util.List;
import java.util.Optional;

public interface PhieuXuatKhoDao {
    void insert(PhieuXuatKho phieuXuatKho);

    void update(PhieuXuatKho phieuXuatKho);

    void delete(PhieuXuatKho phieuXuatKho);

    void deleteById(int maPhieuXuat);

    List<PhieuXuatKho> findAll();

    Optional<PhieuXuatKho> findById(int maPhieuXuat);
}
