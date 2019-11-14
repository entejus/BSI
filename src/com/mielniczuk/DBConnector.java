package com.mielniczuk;

import javax.sql.DataSource;
import java.sql.*;

public class DBConnector {
    private Connection connect;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public DBConnector(DataSource dataSource) {
        try {
            connect =dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    byte[] getData() {
        byte[] data = null;
        try {
            statement = connect.createStatement();
            resultSet = statement
                    .executeQuery("select data from bsi.crypto order by id desc limit 1");
            if (!resultSet.first()) {
                return null;
            }
            data =  resultSet.getBytes("data");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data ;
    }

    void setData(byte[] data) {
        try {
            preparedStatement = connect.prepareStatement("insert into bsi.crypto values (default , ?)");
            preparedStatement.setBytes(1,data);
//            preparedStatement.setBlob(1, data);
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
