package nhom03.model.dao;

import nhom03.model.entity.ChiTietPhieuNhap;

import java.util.List;
import java.util.Optional;

public interface ChiTietPhieuNhapDao {
    void insert(ChiTietPhieuNhap chiTietPhieuNhap);

    void update(ChiTietPhieuNhap chiTietPhieuNhap);

    void delete(ChiTietPhieuNhap chiTietPhieuNhap);

    void deleteById(int maChiTietNhap);

    List<ChiTietPhieuNhap> findAll();

    Optional<ChiTietPhieuNhap> findById(int maChiTietNhap);

    List<ChiTietPhieuNhap> findByMaPhieuNhap(int maPhieuNhap);

    List<ChiTietPhieuNhap> findByMaNguyenLieu(int maNguyenLieu);
}
