package nhom03.model.dao.impl;

import nhom03.model.dao.PhieuNhapKhoDao;
import nhom03.model.entity.PhieuNhapKho;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuNhapKhoJdbc implements PhieuNhapKhoDao {
    @Override
    public void insert(PhieuNhapKho phieuNhapKho) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO PhieuNhapKho (NgayNhap, TongTien, GhiChu) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setTimestamp(1, Timestamp.valueOf(phieuNhapKho.getNgayNhap()));
            statement.setBigDecimal(2, phieuNhapKho.getTongTien());
            statement.setString(3, phieuNhapKho.getGhiChu());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                phieuNhapKho.setMaPhieuNhap(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(PhieuNhapKho phieuNhapKho) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE PhieuNhapKho SET NgayNhap = ?, TongTien = ?, GhiChu = ? WHERE MaPhieuNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setTimestamp(1, Timestamp.valueOf(phieuNhapKho.getNgayNhap()));
            statement.setBigDecimal(2, phieuNhapKho.getTongTien());
            statement.setString(3, phieuNhapKho.getGhiChu());
            statement.setInt(4, phieuNhapKho.getMaPhieuNhap());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(PhieuNhapKho phieuNhapKho) {
        deleteById(phieuNhapKho.getMaPhieuNhap());
    }

    @Override
    public void deleteById(int maPhieuNhap) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM PhieuNhapKho WHERE MaPhieuNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuNhap);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<PhieuNhapKho> findAll() {
        List<PhieuNhapKho> phieuNhapKhos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM PhieuNhapKho";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PhieuNhapKho phieuNhapKho = mapResultSetToPhieuNhapKho(resultSet);
                phieuNhapKhos.add(phieuNhapKho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return phieuNhapKhos;
    }

    @Override
    public Optional<PhieuNhapKho> findById(int maPhieuNhap) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM PhieuNhapKho WHERE MaPhieuNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuNhap);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PhieuNhapKho phieuNhapKho = mapResultSetToPhieuNhapKho(resultSet);
                return Optional.of(phieuNhapKho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private PhieuNhapKho mapResultSetToPhieuNhapKho(ResultSet resultSet) throws SQLException {
        int maPhieuNhap = resultSet.getInt("MaPhieuNhap");
        LocalDateTime ngayNhap = resultSet.getTimestamp("NgayNhap").toLocalDateTime();
        BigDecimal tongTien = resultSet.getBigDecimal("TongTien");
        String ghiChu = resultSet.getString("GhiChu");

        return new PhieuNhapKho(maPhieuNhap, ngayNhap, tongTien, ghiChu);
    }
}
