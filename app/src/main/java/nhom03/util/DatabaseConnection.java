package nhom03.util;

import java.sql.*;

public class DatabaseConnection {
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;"
        + "databaseName=CoffeeShop;"
        + "encrypt=true;"
        + "trustServerCertificate=true;";
    // + "integratedSecurity=true;";
    private static final String JDBC_USER = "sa";
    private static final String JDBC_PASSWORD = "P@ssw0rd";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server JDBC Driver not found", e);
        }
    }

    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(Connection connection, PreparedStatement statement) {
        close(connection, statement, null);
    }
}
