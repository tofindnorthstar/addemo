package model;
/**
 * @ClassName User
 * @Description: TODO
 * @Author jack
 * @Date 2019/8/31
 * @Version V1.0
 */
public class User {
    private String userName;
    private String cName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", cName='" + cName + '\'' +
                '}';
    }
}
