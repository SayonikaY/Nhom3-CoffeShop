package nhom03.model.dao.impl;

import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.entity.NguyenLieu;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NguyenLieuJdbc implements NguyenLieuDao {
    @Override
    public void insert(NguyenLieu nguyenLieu) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO NguyenLieu (TenNguyenLieu, DonViTinh, SoLuongTon, DonGiaNhap) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, nguyenLieu.getTenNguyenLieu());
            statement.setString(2, nguyenLieu.getDonViTinh());
            statement.setBigDecimal(3, nguyenLieu.getSoLuongTon());
            statement.setBigDecimal(4, nguyenLieu.getDonGiaNhap());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                nguyenLieu.setMaNguyenLieu(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(NguyenLieu nguyenLieu) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE NguyenLieu SET TenNguyenLieu = ?, DonViTinh = ?, SoLuongTon = ?, DonGiaNhap = ? WHERE MaNguyenLieu = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, nguyenLieu.getTenNguyenLieu());
            statement.setString(2, nguyenLieu.getDonViTinh());
            statement.setBigDecimal(3, nguyenLieu.getSoLuongTon());
            statement.setBigDecimal(4, nguyenLieu.getDonGiaNhap());
            statement.setInt(5, nguyenLieu.getMaNguyenLieu());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(NguyenLieu nguyenLieu) {
        deleteById(nguyenLieu.getMaNguyenLieu());
    }

    @Override
    public void deleteById(int maNguyenLieu) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM NguyenLieu WHERE MaNguyenLieu = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maNguyenLieu);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<NguyenLieu> findAll() {
        List<NguyenLieu> nguyenLieus = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM NguyenLieu";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                NguyenLieu nguyenLieu = mapResultSetToNguyenLieu(resultSet);
                nguyenLieus.add(nguyenLieu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return nguyenLieus;
    }

    @Override
    public Optional<NguyenLieu> findById(int maNguyenLieu) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM NguyenLieu WHERE MaNguyenLieu = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maNguyenLieu);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                NguyenLieu nguyenLieu = mapResultSetToNguyenLieu(resultSet);
                return Optional.of(nguyenLieu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private NguyenLieu mapResultSetToNguyenLieu(ResultSet resultSet) throws SQLException {
        int maNguyenLieu = resultSet.getInt("MaNguyenLieu");
        String tenNguyenLieu = resultSet.getString("TenNguyenLieu");
        String donViTinh = resultSet.getString("DonViTinh");
        BigDecimal soLuongTon = resultSet.getBigDecimal("SoLuongTon");
        BigDecimal donGiaNhap = resultSet.getBigDecimal("DonGiaNhap");

        return new NguyenLieu(maNguyenLieu, tenNguyenLieu, donViTinh, soLuongTon, donGiaNhap);
    }
}
