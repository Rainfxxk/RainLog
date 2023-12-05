package blog.rain.com.rainlog.frame.basedao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    private static Connection createConnection() {
        try {
            InputStream resource = ConnectionUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(resource);
            String DRIVER = properties.getProperty("jdbc.driver");
            String URL = properties.getProperty("jdbc.url");
            String NAME = properties.getProperty("jdbc.name");
            String PASSWORD = properties.getProperty("jdbc.password");

            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, NAME, PASSWORD);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        Connection connection = threadLocal.get();

        if (connection == null) {
            connection = createConnection();
            threadLocal.set(connection);
        }

        return connection;
    }

    public static void closeConnection() throws SQLException {
        Connection connection = threadLocal.get();

        if (connection == null) {
            return;
        }

        if (!connection.isClosed()) {
            connection.close();
            threadLocal.set(null);
        }
    }
}
