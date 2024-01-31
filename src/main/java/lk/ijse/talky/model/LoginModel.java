package lk.ijse.talky.model;

import lk.ijse.talky.db.DbConnection;
import lk.ijse.talky.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    public static boolean validUser(String name, String pw) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT * FROM user WHERE name = ? AND pwd = ?");

        pstm.setString(1,name);
        pstm.setString(2,pw);

        ResultSet resultSet = pstm.executeQuery();

        return resultSet.next();
    }

}
