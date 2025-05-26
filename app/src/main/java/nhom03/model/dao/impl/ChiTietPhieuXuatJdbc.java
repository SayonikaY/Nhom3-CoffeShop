package nhom03.model.dao.impl;

import nhom03.model.dao.ChiTietPhieuXuatDao;
import nhom03.model.dao.NguyenLieuDao;
import nhom03.model.dao.PhieuXuatKhoDao;
import nhom03.model.entity.ChiTietPhieuXuat;
import nhom03.model.entity.NguyenLieu;
import nhom03.model.entity.PhieuXuatKho;
import nhom03.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChiTietPhieuXuatJdbc implements ChiTietPhieuXuatDao {
    private final PhieuXuatKhoDao phieuXuatKhoDao;
    private final NguyenLieuDao nguyenLieuDao;

    public ChiTietPhieuXuatJdbc() {
        this.phieuXuatKhoDao = new PhieuXuatKhoJdbc();
        this.nguyenLieuDao = new NguyenLieuJdbc();
    }

    @Override
    public void insert(ChiTietPhieuXuat chiTietPhieuXuat) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO ChiTietPhieuXuat (MaPhieuXuat, MaNguyenLieu, SoLuong) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, chiTietPhieuXuat.getPhieuXuatKho().getMaPhieuXuat());
            statement.setInt(2, chiTietPhieuXuat.getNguyenLieu().getMaNguyenLieu());
            statement.setBigDecimal(3, chiTietPhieuXuat.getSoLuong());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                chiTietPhieuXuat.setMaChiTietXuat(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(ChiTietPhieuXuat chiTietPhieuXuat) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE ChiTietPhieuXuat SET MaPhieuXuat = ?, MaNguyenLieu = ?, SoLuong = ? WHERE MaChiTietXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, chiTietPhieuXuat.getPhieuXuatKho().getMaPhieuXuat());
            statement.setInt(2, chiTietPhieuXuat.getNguyenLieu().getMaNguyenLieu());
            statement.setBigDecimal(3, chiTietPhieuXuat.getSoLuong());
            statement.setInt(4, chiTietPhieuXuat.getMaChiTietXuat());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(ChiTietPhieuXuat chiTietPhieuXuat) {
        deleteById(chiTietPhieuXuat.getMaChiTietXuat());
    }

    @Override
    public void deleteById(int maChiTietXuat) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM ChiTietPhieuXuat WHERE MaChiTietXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietXuat);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<ChiTietPhieuXuat> findAll() {
        List<ChiTietPhieuXuat> chiTietPhieuXuats = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuXuat";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuXuat chiTietPhieuXuat = mapResultSetToChiTietPhieuXuat(resultSet);
                chiTietPhieuXuats.add(chiTietPhieuXuat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuXuats;
    }

    @Override
    public Optional<ChiTietPhieuXuat> findById(int maChiTietXuat) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuXuat WHERE MaChiTietXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maChiTietXuat);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                ChiTietPhieuXuat chiTietPhieuXuat = mapResultSetToChiTietPhieuXuat(resultSet);
                return Optional.of(chiTietPhieuXuat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    @Override
    public List<ChiTietPhieuXuat> findByMaPhieuXuat(int maPhieuXuat) {
        List<ChiTietPhieuXuat> chiTietPhieuXuats = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuXuat WHERE MaPhieuXuat = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maPhieuXuat);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuXuat chiTietPhieuXuat = mapResultSetToChiTietPhieuXuat(resultSet);
                chiTietPhieuXuats.add(chiTietPhieuXuat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuXuats;
    }

    @Override
    public List<ChiTietPhieuXuat> findByMaNguyenLieu(int maNguyenLieu) {
        List<ChiTietPhieuXuat> chiTietPhieuXuats = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM ChiTietPhieuXuat WHERE MaNguyenLieu = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maNguyenLieu);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ChiTietPhieuXuat chiTietPhieuXuat = mapResultSetToChiTietPhieuXuat(resultSet);
                chiTietPhieuXuats.add(chiTietPhieuXuat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return chiTietPhieuXuats;
    }

    private ChiTietPhieuXuat mapResultSetToChiTietPhieuXuat(ResultSet resultSet) throws SQLException {
        int maChiTietXuat = resultSet.getInt("MaChiTietXuat");
        int maPhieuXuat = resultSet.getInt("MaPhieuXuat");
        int maNguyenLieu = resultSet.getInt("MaNguyenLieu");
        BigDecimal soLuong = resultSet.getBigDecimal("SoLuong");

        Optional<PhieuXuatKho> phieuXuatKhoOptional = phieuXuatKhoDao.findById(maPhieuXuat);
        PhieuXuatKho phieuXuatKho = phieuXuatKhoOptional.orElseThrow(() -> new SQLException("PhieuXuatKho not found with ID: " + maPhieuXuat));

        Optional<NguyenLieu> nguyenLieuOptional = nguyenLieuDao.findById(maNguyenLieu);
        NguyenLieu nguyenLieu = nguyenLieuOptional.orElseThrow(() -> new SQLException("NguyenLieu not found with ID: " + maNguyenLieu));

        return new ChiTietPhieuXuat(maChiTietXuat, phieuXuatKho, nguyenLieu, soLuong);
    }
}
