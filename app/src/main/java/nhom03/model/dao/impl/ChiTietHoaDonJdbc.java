package nhom03.model.dao.impl;

import nhom03.model.dao.ChiTietHoaDonDao;
import nhom03.model.dao.HoaDonDao;
import nhom03.model.dao.SanPhamDao;
import nhom03.model.entity.ChiTietHoaDon;
import nhom03.model.entity.HoaDon;
import nhom03.model.entity.SanPham;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChiTietHoaDonJdbc implements ChiTietHoaDonDao {
    private final HoaDonDao hoaDonDao;
    private final SanPhamDao sanPhamDao;

    public ChiTietHoaDonJdbc() {
        this.hoaDonDao = new HoaDonJdbc();
        this.sanPhamDao = new SanPhamJdbc();
    }

    @Override
    public void insert(ChiTietHoaDon chiTietHoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO ChiTietHoaDon (MaHoaDon, MaSanPham, SoLuong, DonGia, GhiChu) VALUES (?, ?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, chiTietHoaDon.getHoaDon().getMaHoaDon());
            statement.setInt(2, chiTietHoaDon.getSanPham().getMaSanPham());
            statement.setInt(3, chiTietHoaDon.getSoLuong());
            statement.setBigDecimal(4, chiTietHoaDon.getDonGia());
            statement.setString(5, chiTietHoaDon.getGhiChu());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                chiTietHoaDon.setMaChiTiet(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(ChiTietHoaDon chiTietHoaDon) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE ChiTietHoaDon SET MaHoaDon = ?, MaSanPham = ?, SoLuong = ?, DonGia = ?, GhiChu = ? WHERE MaChiTiet = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, chiTietHoaDon.getHoaDon().getMaHoaDon());
            statement.setInt(2, chiTietHoaDon.getSanPham().getMaSanPham());
            statement.setInt(3, chiTietHoaDon.getSoLuong());
            statement.setBigDecimal(4, chiTietHoaDon.getDonGia());
            statement.setString(5, chiTietHoaDon.getGhiChu());
            statement.setInt(6, chiTietHoaDon.getMaChiTiet());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(ChiTietHoaDon chiTietHoaDon) {
        deleteById(chiTietHoaDon.getMaChiTiet());
    }

    @Override
    public void deleteById(int maChiTiet) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM ChiTietHoaDon WHERE MaChiTiet = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTiet);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<ChiTietHoaDon> findAll() {
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietHoaDon chiTietHoaDon = mapResultSetToChiTietHoaDon(resultSet);
                chiTietHoaDons.add(chiTietHoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietHoaDons;
    }

    @Override
    public Optional<ChiTietHoaDon> findById(int maChiTiet) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon WHERE MaChiTiet = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTiet);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ChiTietHoaDon chiTietHoaDon = mapResultSetToChiTietHoaDon(resultSet);
                return Optional.of(chiTietHoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    @Override
    public List<ChiTietHoaDon> findByMaHoaDon(int maHoaDon) {
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon WHERE MaHoaDon = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maHoaDon);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietHoaDon chiTietHoaDon = mapResultSetToChiTietHoaDon(resultSet);
                chiTietHoaDons.add(chiTietHoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietHoaDons;
    }

    @Override
    public List<ChiTietHoaDon> findByMaSanPham(int maSanPham) {
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietHoaDon WHERE MaSanPham = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maSanPham);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietHoaDon chiTietHoaDon = mapResultSetToChiTietHoaDon(resultSet);
                chiTietHoaDons.add(chiTietHoaDon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietHoaDons;
    }

    private ChiTietHoaDon mapResultSetToChiTietHoaDon(ResultSet resultSet) throws SQLException {
        int maChiTiet = resultSet.getInt("MaChiTiet");
        int maHoaDon = resultSet.getInt("MaHoaDon");
        int maSanPham = resultSet.getInt("MaSanPham");
        int soLuong = resultSet.getInt("SoLuong");
        BigDecimal donGia = resultSet.getBigDecimal("DonGia");
        String ghiChu = resultSet.getString("GhiChu");

        Optional<HoaDon> hoaDonOptional = hoaDonDao.findById(maHoaDon);
        HoaDon hoaDon = hoaDonOptional.orElseThrow(() -> new SQLException("HoaDon not found with ID: " + maHoaDon));

        Optional<SanPham> sanPhamOptional = sanPhamDao.findById(maSanPham);
        SanPham sanPham = sanPhamOptional.orElseThrow(() -> new SQLException("SanPham not found with ID: " + maSanPham));

        return new ChiTietHoaDon(maChiTiet, hoaDon, sanPham, soLuong, donGia, ghiChu);
    }
}
