package nhom03.model.dao;

import nhom03.model.entity.ChiTietPhieuXuat;

import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuXuatDao {
    void insert(ChiTietPhieuXuat chiTietPhieuXuat);

    void update(ChiTietPhieuXuat chiTietPhieuXuat);

    void delete(ChiTietPhieuXuat chiTietPhieuXuat);

    void deleteById(int maChiTietXuat);

    List<ChiTietPhieuXuat> findAll();

    Optional<ChiTietPhieuXuat> findById(int maChiTietXuat);

    List<ChiTietPhieuXuat> findByMaPhieuXuat(int maPhieuXuat);

    List<ChiTietPhieuXuat> findByMaNguyenLieu(int maNguyenLieu);
}
