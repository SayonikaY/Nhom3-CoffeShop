package nhom03.model.dao.impl;

import nhom03.model.dao.ChiTietPhieuNhapDao;
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.dao.PhieuNhapKhoDao;
import nhom03.model.entity.ChiTietPhieuNhap;
import nhom03.model.entity.NguyenLieu;
import nhom03.model.entity.PhieuNhapKho;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChiTietPhieuNhapJdbc implements ChiTietPhieuNhapDao {
    private final PhieuNhapKhoDao phieuNhapKhoDao;
    private final NguyenLieuDao nguyenLieuDao;

    public ChiTietPhieuNhapJdbc() {
        this.phieuNhapKhoDao = new PhieuNhapKhoJdbc();
        this.nguyenLieuDao = new NguyenLieuJdbc();
    }

    @Override
    public void insert(ChiTietPhieuNhap chiTietPhieuNhap) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO ChiTietPhieuNhap (MaPhieuNhap, MaNguyenLieu, SoLuong, DonGia) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, chiTietPhieuNhap.getPhieuNhapKho().getMaPhieuNhap());
            statement.setInt(2, chiTietPhieuNhap.getNguyenLieu().getMaNguyenLieu());
            statement.setBigDecimal(3, chiTietPhieuNhap.getSoLuong());
            statement.setBigDecimal(4, chiTietPhieuNhap.getDonGia());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                chiTietPhieuNhap.setMaChiTietNhap(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(ChiTietPhieuNhap chiTietPhieuNhap) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE ChiTietPhieuNhap SET MaPhieuNhap = ?, MaNguyenLieu = ?, SoLuong = ?, DonGia = ? WHERE MaChiTietNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, chiTietPhieuNhap.getPhieuNhapKho().getMaPhieuNhap());
            statement.setInt(2, chiTietPhieuNhap.getNguyenLieu().getMaNguyenLieu());
            statement.setBigDecimal(3, chiTietPhieuNhap.getSoLuong());
            statement.setBigDecimal(4, chiTietPhieuNhap.getDonGia());
            statement.setInt(5, chiTietPhieuNhap.getMaChiTietNhap());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(ChiTietPhieuNhap chiTietPhieuNhap) {
        deleteById(chiTietPhieuNhap.getMaChiTietNhap());
    }

    @Override
    public void deleteById(int maChiTietNhap) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM ChiTietPhieuNhap WHERE MaChiTietNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietNhap);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<ChiTietPhieuNhap> findAll() {
        List<ChiTietPhieuNhap> chiTietPhieuNhaps = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuNhap chiTietPhieuNhap = mapResultSetToChiTietPhieuNhap(resultSet);
                chiTietPhieuNhaps.add(chiTietPhieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuNhaps;
    }

    @Override
    public Optional<ChiTietPhieuNhap> findById(int maChiTietNhap) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaChiTietNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietNhap);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ChiTietPhieuNhap chiTietPhieuNhap = mapResultSetToChiTietPhieuNhap(resultSet);
                return Optional.of(chiTietPhieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    @Override
    public List<ChiTietPhieuNhap> findByMaPhieuNhap(int maPhieuNhap) {
        List<ChiTietPhieuNhap> chiTietPhieuNhaps = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaPhieuNhap = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuNhap);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuNhap chiTietPhieuNhap = mapResultSetToChiTietPhieuNhap(resultSet);
                chiTietPhieuNhaps.add(chiTietPhieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuNhaps;
    }

    @Override
    public List<ChiTietPhieuNhap> findByMaNguyenLieu(int maNguyenLieu) {
        List<ChiTietPhieuNhap> chiTietPhieuNhaps = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuNhap WHERE MaNguyenLieu = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maNguyenLieu);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuNhap chiTietPhieuNhap = mapResultSetToChiTietPhieuNhap(resultSet);
                chiTietPhieuNhaps.add(chiTietPhieuNhap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuNhaps;
    }

    private ChiTietPhieuNhap mapResultSetToChiTietPhieuNhap(ResultSet resultSet) throws SQLException {
        int maChiTietNhap = resultSet.getInt("MaChiTietNhap");
        int maPhieuNhap = resultSet.getInt("MaPhieuNhap");
        int maNguyenLieu = resultSet.getInt("MaNguyenLieu");
        BigDecimal soLuong = resultSet.getBigDecimal("SoLuong");
        BigDecimal donGia = resultSet.getBigDecimal("DonGia");

        Optional<PhieuNhapKho> phieuNhapKhoOptional = phieuNhapKhoDao.findById(maPhieuNhap);
        PhieuNhapKho phieuNhapKho = phieuNhapKhoOptional.orElseThrow(() -> new SQLException("PhieuNhapKho not found with ID: " + maPhieuNhap));

        Optional<NguyenLieu> nguyenLieuOptional = nguyenLieuDao.findById(maNguyenLieu);
        NguyenLieu nguyenLieu = nguyenLieuOptional.orElseThrow(() -> new SQLException("NguyenLieu not found with ID: " + maNguyenLieu));

        return new ChiTietPhieuNhap(maChiTietNhap, phieuNhapKho, nguyenLieu, soLuong, donGia);
    }
}
