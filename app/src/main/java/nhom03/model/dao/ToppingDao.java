package nhom03.model.dao;

import nhom03.model.entity.Topping;

import java.util.List;
import java.util.Optional;

public interface ToppingDao {
    void insert(Topping topping);

    void update(Topping topping);

    void delete(Topping topping);

    void deleteById(int maTopping);

    List<Topping> findAll();

    Optional<Topping> findById(int maTopping);
}
