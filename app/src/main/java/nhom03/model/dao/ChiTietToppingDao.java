package nhom03.model.dao;

import nhom03.model.entity.ChiTietTopping;

import java.util.List;
import java.util.Optional;

public interface ChiTietToppingDao {
    void insert(ChiTietTopping chiTietTopping);

    void update(ChiTietTopping chiTietTopping);

    void delete(ChiTietTopping chiTietTopping);

    void deleteById(int maChiTietTopping);

    List<ChiTietTopping> findAll();

    Optional<ChiTietTopping> findById(int maChiTietTopping);

    List<ChiTietTopping> findByMaChiTietHoaDon(int maChiTietHD);

    List<ChiTietTopping> findByMaTopping(int maTopping);
}
