package nhom03.model.dao;

import nhom03.model.entity.NguyenLieu;

import java.util.List;
import java.util.Optional;

public interface NguyenLieuDao {
    void insert(NguyenLieu nguyenLieu);

    void update(NguyenLieu nguyenLieu);

    void delete(NguyenLieu nguyenLieu);

    void deleteById(int maNguyenLieu);

    List<NguyenLieu> findAll();

    Optional<NguyenLieu> findById(int maNguyenLieu);
}
