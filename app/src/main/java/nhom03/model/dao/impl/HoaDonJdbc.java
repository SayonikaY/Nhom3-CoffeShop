package nhom03.model.dao.impl;

import nhom03.model.dao.BanDao;
import nhom03.model.dao.HoaDonDao;
import nhom03.model.entity.Ban;
import nhom03.model.entity.HoaDon;
import nhom03.model.entity.TrangThaiHoaDon;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoaDonJdbc implements HoaDonDao {
    private final BanDao banDao;

    public HoaDonJdbc() {
        this.banDao = new BanJdbc();
    }

    @Override
    public void insert(HoaDon hoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO HoaDon (MaBan, NgayLap, TongTien, GiamGia, TrangThai) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, hoaDon.getBan().getMaBan());
            statement.setTimestamp(2, Timestamp.valueOf(hoaDon.getNgayLap()));
            statement.setBigDecimal(3, hoaDon.getTongTien());
            statement.setBigDecimal(4, hoaDon.getGiamGia());
            statement.setString(5, hoaDon.getTrangThai().toString());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                hoaDon.setMaHoaDon(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(HoaDon hoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE HoaDon SET MaBan = ?, NgayLap = ?, TongTien = ?, GiamGia = ?, TrangThai = ? WHERE MaHoaDon = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, hoaDon.getBan().getMaBan());
            statement.setTimestamp(2, Timestamp.valueOf(hoaDon.getNgayLap()));
            statement.setBigDecimal(3, hoaDon.getTongTien());
            statement.setBigDecimal(4, hoaDon.getGiamGia());
            statement.setString(5, hoaDon.getTrangThai().toString());
            statement.setInt(6, hoaDon.getMaHoaDon());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(HoaDon hoaDon) {
        deleteById(hoaDon.getMaHoaDon());
    }

    @Override
    public void deleteById(int maHoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM HoaDon WHERE MaHoaDon = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maHoaDon);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<HoaDon> findAll() {
        List<HoaDon> hoaDons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM HoaDon";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                HoaDon hoaDon = mapResultSetToHoaDon(resultSet);
                hoaDons.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return hoaDons;
    }

    @Override
    public Optional<HoaDon> findById(int maHoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM HoaDon WHERE MaHoaDon = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maHoaDon);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                HoaDon hoaDon = mapResultSetToHoaDon(resultSet);
                return Optional.of(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    @Override
    public List<HoaDon> findByMaBan(int maBan) {
        List<HoaDon> hoaDons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM HoaDon WHERE MaBan = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maBan);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                HoaDon hoaDon = mapResultSetToHoaDon(resultSet);
                hoaDons.add(hoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return hoaDons;
    }

    private HoaDon mapResultSetToHoaDon(ResultSet resultSet) throws SQLException {
        int maHoaDon = resultSet.getInt("MaHoaDon");
        int maBan = resultSet.getInt("MaBan");
        LocalDateTime ngayLap = resultSet.getTimestamp("NgayLap").toLocalDateTime();
        BigDecimal tongTien = resultSet.getBigDecimal("TongTien");
        BigDecimal giamGia = resultSet.getBigDecimal("GiamGia");
        TrangThaiHoaDon trangThai = TrangThaiHoaDon.fromString(resultSet.getString("TrangThai"));

        Optional<Ban> banOptional = banDao.findById(maBan);
        Ban ban = banOptional.orElseThrow(() -> new SQLException("Ban not found with ID: " + maBan));

        return new HoaDon(maHoaDon, ban, ngayLap, tongTien, giamGia, trangThai);
    }
}
