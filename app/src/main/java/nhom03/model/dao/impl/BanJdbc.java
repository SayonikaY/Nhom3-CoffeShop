package nhom03.model.dao.impl;

import nhom03.model.dao.BanDao;
import nhom03.model.entity.Ban;
import nhom03.model.entity.TrangThaiBan;
import nhom03.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BanJdbc implements BanDao {
    @Override
    public void insert(Ban ban) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Ban (TenBan, GhiChu, TrangThai) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, ban.getTenBan());
            statement.setString(2, ban.getGhiChu());
            statement.setString(3, ban.getTrangThai().toString());

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                ban.setMaBan(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void update(Ban ban) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE Ban SET TenBan = ?, GhiChu = ?, TrangThai = ? WHERE MaBan = ?";
            statement = connection.prepareStatement(sql);

            statement.setString(1, ban.getTenBan());
            statement.setString(2, ban.getGhiChu());
            statement.setString(3, ban.getTrangThai().toString());
            statement.setInt(4, ban.getMaBan());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public void delete(Ban ban) {
        deleteById(ban.getMaBan());
    }

    @Override
    public void deleteById(int maBan) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Ban WHERE MaBan = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maBan);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement);
        }
    }

    @Override
    public List<Ban> findAll() {
        List<Ban> bans = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Ban";
            statement = connection.prepareStatement(sql);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Ban ban = mapResultSetToBan(resultSet);
                bans.add(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return bans;
    }

    @Override
    public Optional<Ban> findById(int maBan) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Ban WHERE MaBan = ?";
            statement = connection.prepareStatement(sql);

            statement.setInt(1, maBan);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Ban ban = mapResultSetToBan(resultSet);
                return Optional.of(ban);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.close(connection, statement, resultSet);
        }

        return Optional.empty();
    }

    private Ban mapResultSetToBan(ResultSet resultSet) throws SQLException {
        int maBan = resultSet.getInt("MaBan");
        String tenBan = resultSet.getString("TenBan");
        String ghiChu = resultSet.getString("GhiChu");
        TrangThaiBan trangThai = TrangThaiBan.fromString(resultSet.getString("TrangThai"));

        return new Ban(maBan, tenBan, ghiChu, trangThai);
    }
}
