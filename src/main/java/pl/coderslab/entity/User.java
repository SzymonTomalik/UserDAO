package pl.coderslab.entity;

public class User {
    private int id;
    private String userName;
    private String user_Surname;
    private String email;
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUser_Surname() {
        return user_Surname;
    }

    public void setUser_Surname(String user_Surname) {
        this.user_Surname = user_Surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return id+" | "+userName+" | "+user_Surname+" | "+email+" | "+password;
    }
}

