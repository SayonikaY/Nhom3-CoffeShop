package nhom03.model.dao.impl;

import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.LoaiSanPham;
import nhom03.model.entity.SanPham;
import nhom03.model.entity.TrangThaiSanPham;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SanPhamJdbc implements SanPhamDao {
    @Override
    public void insert(SanPham sanPham) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO SanPham (TenSanPham, LoaiSanPham, DonGia, MoTa, HinhAnh, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, sanPham.getTenSanPham());
            statement.setString(2, sanPham.getLoaiSanPham().toString());
            statement.setBigDecimal(3, sanPham.getDonGia());
            statement.setString(4, sanPham.getMoTa());
            statement.setString(5, sanPham.getHinhAnh());
            statement.setString(6, sanPham.getTrangThai().toString());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                sanPham.setMaSanPham(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(SanPham sanPham) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE SanPham SET TenSanPham = ?, LoaiSanPham = ?, DonGia = ?, MoTa = ?, HinhAnh = ?, TrangThai = ? WHERE MaSanPham = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, sanPham.getTenSanPham());
            statement.setString(2, sanPham.getLoaiSanPham().toString());
            statement.setBigDecimal(3, sanPham.getDonGia());
            statement.setString(4, sanPham.getMoTa());
            statement.setString(5, sanPham.getHinhAnh());
            statement.setString(6, sanPham.getTrangThai().toString());
            statement.setInt(7, sanPham.getMaSanPham());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(SanPham sanPham) {
        deleteById(sanPham.getMaSanPham());
    }

    @Override
    public void deleteById(int maSanPham) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM SanPham WHERE MaSanPham = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maSanPham);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<SanPham> findAll() {
        List<SanPham> sanPhams = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM SanPham";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                SanPham sanPham = mapResultSetToSanPham(resultSet);
                sanPhams.add(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return sanPhams;
    }

    @Override
    public Optional<SanPham> findById(int maSanPham) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM SanPham WHERE MaSanPham = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maSanPham);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                SanPham sanPham = mapResultSetToSanPham(resultSet);
                return Optional.of(sanPham);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private SanPham mapResultSetToSanPham(ResultSet resultSet) throws SQLException {
        int maSanPham = resultSet.getInt("MaSanPham");
        String tenSanPham = resultSet.getString("TenSanPham");
        LoaiSanPham loaiSanPham = LoaiSanPham.fromString(resultSet.getString("LoaiSanPham"));
        BigDecimal donGia = resultSet.getBigDecimal("DonGia");
        String moTa = resultSet.getString("MoTa");
        String hinhAnh = resultSet.getString("HinhAnh");
        TrangThaiSanPham trangThai = TrangThaiSanPham.fromString(resultSet.getString("TrangThai"));

        return new SanPham(maSanPham, tenSanPham, loaiSanPham, donGia, moTa, hinhAnh, trangThai);
    }
}
