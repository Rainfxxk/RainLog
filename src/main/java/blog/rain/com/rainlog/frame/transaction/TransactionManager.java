package blog.rain.com.rainlog.frame.transaction;

import blog.rain.com.rainlog.frame.basedao.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    public static void beginTransaction() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        connection.setAutoCommit(false);
    }

    public static void commit() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        connection.commit();
        ConnectionUtil.closeConnection();
    }

    public static void rollback() throws SQLException {
        Connection connection = ConnectionUtil.getConnection();
        connection.rollback();
        ConnectionUtil.closeConnection();
    }
}
