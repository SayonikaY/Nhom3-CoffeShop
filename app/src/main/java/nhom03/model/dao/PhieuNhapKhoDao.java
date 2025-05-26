package nhom03.model.dao;

import nhom03.model.entity.PhieuNhapKho;

import java.util.List;
import java.util.Optional;

public interface PhieuNhapKhoDao {
    void insert(PhieuNhapKho phieuNhapKho);

    void update(PhieuNhapKho phieuNhapKho);

    void delete(PhieuNhapKho phieuNhapKho);

    void deleteById(int maPhieuNhap);

    List<PhieuNhapKho> findAll();

    Optional<PhieuNhapKho> findById(int maPhieuNhap);
}
