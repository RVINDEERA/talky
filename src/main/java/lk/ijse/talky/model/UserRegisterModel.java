package lk.ijse.talky.model;

import lk.ijse.talky.db.DbConnection;
import lk.ijse.talky.dto.UserDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class    UserRegisterModel {
    public static boolean saveUser(UserDto userDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("INSERT INTO user VALUES (?,?)");
        pstm.setString(1, userDto.getName());
        pstm.setString(2, userDto.getPw());

        return pstm.executeUpdate() > 0;
    }

    public static boolean checkUserName(String name) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement("SELECT COUNT(*) FROM user WHERE name = ?");
        pstm.setString(1, name);

        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        } else {
            return false;
        }
    }
}
