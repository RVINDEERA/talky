package lk.ijse.talky.dto;

public class UserDto {
    String name;
    String pw;

    public UserDto() {
    }

    public UserDto(String name, String pw) {
        this.name = name;
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + name + '\'' +
                ", pw='" + pw + '\'' +
                '}';
    }
}
