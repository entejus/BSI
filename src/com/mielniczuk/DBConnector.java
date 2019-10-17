package com.mielniczuk;

import java.io.InputStream;
import java.sql.*;

public class DBConnector {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DBConnector() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/bsi?"
                            + "user=root&password=password");

            statement = connect.createStatement();
        } catch (Exception e) {
            throw e;
        }
    }

    public ResultSet getData(int id) {
        try {
            resultSet = statement
                    .executeQuery("select data from bsi.crypto where ID=" + id + ";");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public void setData(InputStream data) {
        try {
            preparedStatement = connect.prepareStatement("insert into bsi.crypto values (default , ?)");
            preparedStatement.setBlob(1, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
