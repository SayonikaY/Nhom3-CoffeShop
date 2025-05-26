package nhom03.model.dao;

import nhom03.model.entity.Ban;

import java.util.List;
import java.util.Optional;

public interface BanDao {
    void insert(Ban ban);

    void update(Ban ban);

    void delete(Ban ban);

    void deleteById(int maBan);

    List<Ban> findAll();

    Optional<Ban> findById(int maBan);
}
