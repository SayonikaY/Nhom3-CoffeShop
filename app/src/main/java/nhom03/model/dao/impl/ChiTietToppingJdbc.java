package nhom03.model.dao.impl;

import nhom03.model.dao.ChiTietHoaDonDao;
import nhom03.model.dao.ChiTietToppingDao;
import nhom03.model.dao.ToppingDao;
import nhom03.model.entity.ChiTietHoaDon;
import nhom03.model.entity.ChiTietTopping;
import nhom03.model.entity.Topping;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChiTietToppingJdbc implements ChiTietToppingDao {
    private final ChiTietHoaDonDao chiTietHoaDonDao;
    private final ToppingDao toppingDao;

    public ChiTietToppingJdbc() {
        this.chiTietHoaDonDao = new ChiTietHoaDonJdbc();
        this.toppingDao = new ToppingJdbc();
    }

    @Override
    public void insert(ChiTietTopping chiTietTopping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO ChiTietTopping (MaChiTietHD, MaTopping, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, chiTietTopping.getChiTietHoaDon().getMaChiTiet());
            statement.setInt(2, chiTietTopping.getTopping().getMaTopping());
            statement.setInt(3, chiTietTopping.getSoLuong());
            statement.setBigDecimal(4, chiTietTopping.getDonGia());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                chiTietTopping.setMaChiTietTopping(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(ChiTietTopping chiTietTopping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE ChiTietTopping SET MaChiTietHD = ?, MaTopping = ?, SoLuong = ?, DonGia = ? WHERE MaChiTietTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, chiTietTopping.getChiTietHoaDon().getMaChiTiet());
            statement.setInt(2, chiTietTopping.getTopping().getMaTopping());
            statement.setInt(3, chiTietTopping.getSoLuong());
            statement.setBigDecimal(4, chiTietTopping.getDonGia());
            statement.setInt(5, chiTietTopping.getMaChiTietTopping());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(ChiTietTopping chiTietTopping) {
        deleteById(chiTietTopping.getMaChiTietTopping());
    }

    @Override
    public void deleteById(int maChiTietTopping) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM ChiTietTopping WHERE MaChiTietTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietTopping);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<ChiTietTopping> findAll() {
        List<ChiTietTopping> chiTietToppings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietTopping";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietTopping chiTietTopping = mapResultSetToChiTietTopping(resultSet);
                chiTietToppings.add(chiTietTopping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietToppings;
    }

    @Override
    public Optional<ChiTietTopping> findById(int maChiTietTopping) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietTopping WHERE MaChiTietTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietTopping);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ChiTietTopping chiTietTopping = mapResultSetToChiTietTopping(resultSet);
                return Optional.of(chiTietTopping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    @Override
    public List<ChiTietTopping> findByMaChiTietHoaDon(int maChiTietHD) {
        List<ChiTietTopping> chiTietToppings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietTopping WHERE MaChiTietHD = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietHD);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietTopping chiTietTopping = mapResultSetToChiTietTopping(resultSet);
                chiTietToppings.add(chiTietTopping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietToppings;
    }

    @Override
    public List<ChiTietTopping> findByMaTopping(int maTopping) {
        List<ChiTietTopping> chiTietToppings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietTopping WHERE MaTopping = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maTopping);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietTopping chiTietTopping = mapResultSetToChiTietTopping(resultSet);
                chiTietToppings.add(chiTietTopping);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietToppings;
    }

    private ChiTietTopping mapResultSetToChiTietTopping(ResultSet resultSet) throws SQLException {
        int maChiTietTopping = resultSet.getInt("MaChiTietTopping");
        int maChiTietHD = resultSet.getInt("MaChiTietHD");
        int maTopping = resultSet.getInt("MaTopping");
        int soLuong = resultSet.getInt("SoLuong");
        BigDecimal donGia = resultSet.getBigDecimal("DonGia");

        Optional<ChiTietHoaDon> chiTietHoaDonOptional = chiTietHoaDonDao.findById(maChiTietHD);
        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonOptional.orElseThrow(() -> new SQLException("ChiTietHoaDon not found with ID: " + maChiTietHD));

        Optional<Topping> toppingOptional = toppingDao.findById(maTopping);
        Topping topping = toppingOptional.orElseThrow(() -> new SQLException("Topping not found with ID: " + maTopping));

        return new ChiTietTopping(maChiTietTopping, chiTietHoaDon, topping, soLuong, donGia);
    }
}
