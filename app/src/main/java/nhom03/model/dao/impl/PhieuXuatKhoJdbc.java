package nhom03.model.dao.impl;

import nhom03.model.dao.PhieuXuatKhoDao;
import nhom03.model.entity.PhieuXuatKho;
import nhom03.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhieuXuatKhoJdbc implements PhieuXuatKhoDao {
    @Override
    public void insert(PhieuXuatKho phieuXuatKho) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO PhieuXuatKho (NgayXuat, LyDo, GhiChu) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setTimestamp(1, Timestamp.valueOf(phieuXuatKho.getNgayXuat()));
            statement.setString(2, phieuXuatKho.getLyDo());
            statement.setString(3, phieuXuatKho.getGhiChu());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                phieuXuatKho.setMaPhieuXuat(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(PhieuXuatKho phieuXuatKho) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE PhieuXuatKho SET NgayXuat = ?, LyDo = ?, GhiChu = ? WHERE MaPhieuXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setTimestamp(1, Timestamp.valueOf(phieuXuatKho.getNgayXuat()));
            statement.setString(2, phieuXuatKho.getLyDo());
            statement.setString(3, phieuXuatKho.getGhiChu());
            statement.setInt(4, phieuXuatKho.getMaPhieuXuat());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(PhieuXuatKho phieuXuatKho) {
        deleteById(phieuXuatKho.getMaPhieuXuat());
    }

    @Override
    public void deleteById(int maPhieuXuat) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM PhieuXuatKho WHERE MaPhieuXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuXuat);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<PhieuXuatKho> findAll() {
        List<PhieuXuatKho> phieuXuatKhos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM PhieuXuatKho";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PhieuXuatKho phieuXuatKho = mapResultSetToPhieuXuatKho(resultSet);
                phieuXuatKhos.add(phieuXuatKho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return phieuXuatKhos;
    }

    @Override
    public Optional<PhieuXuatKho> findById(int maPhieuXuat) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM PhieuXuatKho WHERE MaPhieuXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuXuat);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                PhieuXuatKho phieuXuatKho = mapResultSetToPhieuXuatKho(resultSet);
                return Optional.of(phieuXuatKho);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private PhieuXuatKho mapResultSetToPhieuXuatKho(ResultSet resultSet) throws SQLException {
        int maPhieuXuat = resultSet.getInt("MaPhieuXuat");
        LocalDateTime ngayXuat = resultSet.getTimestamp("NgayXuat").toLocalDateTime();
        String lyDo = resultSet.getString("LyDo");
        String ghiChu = resultSet.getString("GhiChu");

        return new PhieuXuatKho(maPhieuXuat, ngayXuat, lyDo, ghiChu);
    }
}
