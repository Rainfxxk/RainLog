package blog.rain.com.rainlog.frame.basedao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BaseDAO<T> {
    public Class entityClass;

    public BaseDAO() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        Type actualTypeArgument = actualTypeArguments[0];

        try {
            entityClass = Class.forName(actualTypeArgument.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw  new DAOException("BaseDAO Constructor Wrong!");
        }
    }

    private void setParameters(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
        }
    }

    protected int executeUpdate(String sql, Object... parameters) {
        boolean isInsert = sql.trim().toLowerCase().startsWith("insert");

        Connection connection = ConnectionUtil.getConnection();
        PreparedStatement preparedStatement;
        try {
            if (isInsert) {
                preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
            else {
                preparedStatement = connection.prepareStatement(sql);
            }

            setParameters(preparedStatement, parameters);

            int result = preparedStatement.executeUpdate();

            if (isInsert) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
            else {
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Error When Run BaseDAO.executeUpdate");
        }

        return 0;
    }

    private void setEntityValue(T entity, String propertyName, Object propertyValue) throws NoSuchFieldException, IllegalAccessException {
        Field field = entityClass.getDeclaredField(propertyName);

        if (field != null) {
            field.setAccessible(true);
            field.set(entity, propertyValue);
        }
    }

    protected List<T> executeQuery(String sql, Object... parameters) {
        ArrayList<T> result = new ArrayList<>();
        Connection connection = ConnectionUtil.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                T entity = (T) entityClass.newInstance();

                for (int i = 1; i < metaData.getColumnCount() + 1; i++) {
                    String columnName = metaData.getColumnName(i);
                    columnName = underLine2Hump(columnName);
                    Object columnValue = resultSet.getObject(i);
                    setEntityValue(entity, columnName, columnValue);
                }

                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DAOException("Error When Run BaseDAO.executeQuery");
        }

        return result;
    }

    private String underLine2Hump(String str) {
        char[] charArray = str.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '_') {
                charArray[i + 1] ^= 32;
                continue;
            }

            stringBuilder.append(charArray[i]);
        }

        return stringBuilder.toString();
    }
}
