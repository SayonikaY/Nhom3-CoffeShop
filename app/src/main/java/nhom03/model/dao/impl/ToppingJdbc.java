package nhom03.model.dao.impl;

import nhom03.model.dao.ToppingDao;
import nhom03.model.entity.Topping;
import nhom03.model.entity.TrangThaiSanPham;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToppingJdbc implements ToppingDao {
    @Override
    public void insert(Topping topping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Topping (TenTopping, DonGia, MoTa, TrangThai) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, topping.getTenTopping());
            statement.setBigDecimal(2, topping.getDonGia());
            statement.setString(3, topping.getMoTa());
            statement.setString(4, topping.getTrangThai().toString());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                topping.setMaTopping(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(Topping topping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE Topping SET TenTopping = ?, DonGia = ?, MoTa = ?, TrangThai = ? WHERE MaTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, topping.getTenTopping());
            statement.setBigDecimal(2, topping.getDonGia());
            statement.setString(3, topping.getMoTa());
            statement.setString(4, topping.getTrangThai().toString());
            statement.setInt(5, topping.getMaTopping());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(Topping topping) {
        deleteById(topping.getMaTopping());
    }

    @Override
    public void deleteById(int maTopping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Topping WHERE MaTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maTopping);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<Topping> findAll() {
        List<Topping> toppings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Topping";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Topping topping = mapResultSetToTopping(resultSet);
                toppings.add(topping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return toppings;
    }

    @Override
    public Optional<Topping> findById(int maTopping) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Topping WHERE MaTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maTopping);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Topping topping = mapResultSetToTopping(resultSet);
                return Optional.of(topping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private Topping mapResultSetToTopping(ResultSet resultSet) throws SQLException {
        int maTopping = resultSet.getInt("MaTopping");
        String tenTopping = resultSet.getString("TenTopping");
        BigDecimal donGia = resultSet.getBigDecimal("DonGia");
        String moTa = resultSet.getString("MoTa");
        TrangThaiSanPham trangThai = TrangThaiSanPham.fromString(resultSet.getString("TrangThai"));

        return new Topping(maTopping, tenTopping, donGia, moTa, trangThai);
    }
}
