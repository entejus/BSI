package com.mielniczuk;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class DBConnector {
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public DBConnector() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://localhost/bsi?"
                            + "user=root&password=password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ByteArrayInputStream getData() {
        ByteArrayInputStream data = null;
        try {
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select data from bsi.crypto order by id desc limit 1");
            resultSet.next();
            data =  (ByteArrayInputStream)resultSet.getBinaryStream("data");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data ;
    }

    void setData(ByteArrayInputStream data) {
        try {
            preparedStatement = connect.prepareStatement("insert into bsi.crypto values (default , ?)");
            preparedStatement.setBlob(1, data);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

     void close() {
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
