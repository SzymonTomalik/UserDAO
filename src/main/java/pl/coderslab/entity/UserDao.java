package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username,user_surname, email, password) VALUES (?, ?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id=?;";
    private static final String READ_USER_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email=?;";
    private static final String READ_ALL_USER_QUERY = "SELECT * FROM users;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username=?, user_surname=?, email=?, password=? WHERE id=?;";
    private static final String UPDATE_USER_BY_EMAIL_QUERY = "UPDATE users SET username=?, user_surname=?, password=? WHERE email=?;";
    private static final String UPDATE_USER_NAME_QUERY = "UPDATE users SET username=? WHERE id=?;";
    private static final String UPDATE_USER_NAME_BY_EMAIL_QUERY = "UPDATE users SET username=? WHERE email=?;";
    private static final String UPDATE_USER_SURNAME_QUERY = "UPDATE users SET user_surname=? WHERE id=?;";
    private static final String UPDATE_USER_SURNAME_BY_EMAIL_QUERY = "UPDATE users SET user_surname=? WHERE email=?;";
    private static final String UPDATE_USER_EMAIL_QUERY = "UPDATE users SET email=? WHERE id=?;";
    private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE users SET password=? WHERE id=?;";
    private static final String UPDATE_USER_PASSWORD_BY_EMAIL_QUERY = "UPDATE users SET password=? WHERE email=?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";
    private static final String DELETE_USER_BY_EMAIL_QUERY = "DELETE FROM users WHERE email=?";

    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getUser_Surname());
            preStmt.setString(3, user.getEmail());
            preStmt.setString(4, hashPassword(user.getPassword()));
            preStmt.executeUpdate();
            ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                System.out.println("Inserted Id: " + user.getId());
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addUser(String name, String surname, String email, String password) {
        User user = new User();
        user.setUserName(name);
        user.setUser_Surname(surname);
        user.setEmail(email);
        user.setPassword(password);

        UserDao userDao = new UserDao();
        userDao.create(user);
    }

    public User read(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(READ_USER_QUERY);
            preStmt.setInt(1, userId);


            ResultSet resultSet = preStmt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setUser_Surname(resultSet.getString("user_Surname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User readByEmail(String email) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(READ_USER_BY_EMAIL_QUERY);
            preStmt.setString(1, email);


            ResultSet resultSet = preStmt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setUser_Surname(resultSet.getString("user_Surname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(int id, User user, String userName, String user_Surname, String email, String password) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_QUERY);
            preStmt.setString(1, userName);
            preStmt.setString(2, user_Surname);
            preStmt.setString(3, email);
            preStmt.setString(4, hashPassword(password));
            preStmt.setInt(5, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateByEmail(String email, User user, String userName, String user_Surname, String password) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_BY_EMAIL_QUERY);
            preStmt.setString(1, userName);
            preStmt.setString(2, user_Surname);
            preStmt.setString(3, hashPassword(password));
            preStmt.setString(4, email);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateName(int id, User user, String userName) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_NAME_QUERY);
            preStmt.setString(1, userName);
            preStmt.setInt(2, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNameByEmail(String email, User user, String userName) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_NAME_BY_EMAIL_QUERY);
            preStmt.setString(1, userName);
            preStmt.setString(2, email);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSurname(int id, User user, String user_Surname) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_SURNAME_QUERY);
            preStmt.setString(1, user_Surname);
            preStmt.setInt(2, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSurameByEmail(String email, User user, String user_Surname) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_SURNAME_BY_EMAIL_QUERY);
            preStmt.setString(1, user_Surname);
            preStmt.setString(2, email);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmail(int id, User user, String email) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_EMAIL_QUERY);
            preStmt.setString(1, email);
            preStmt.setInt(2, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePassword(int id, User user, String password) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_PASSWORD_QUERY);
            preStmt.setString(1, hashPassword(password));
            preStmt.setInt(2, id);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePasswordByEmail(String email, User user, String password) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_PASSWORD_BY_EMAIL_QUERY);
            preStmt.setString(1, password);
            preStmt.setString(2, email);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(DELETE_USER_QUERY);
            preStmt.setInt(1, userId);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteByEmail(String email) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(DELETE_USER_BY_EMAIL_QUERY);
            preStmt.setString(1, email);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User[] findAll() {
        try (Connection conn = DbUtil.getConnection()) {
            User[] usersTab = new User[0], usersNewTab;
            PreparedStatement preStmt = conn.prepareStatement(READ_ALL_USER_QUERY);
            ResultSet resultSet = preStmt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUserName(resultSet.getString("username"));
                user.setUser_Surname(resultSet.getString("user_Surname"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                usersNewTab = Arrays.copyOf(usersTab, usersTab.length + 1);
                usersNewTab[usersNewTab.length - 1] = user;
                usersTab = Arrays.copyOf(usersNewTab, usersNewTab.length);
            }
            return usersTab;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printUserById(User user) {
        UserDao userDao = new UserDao();
        try {
            userDao.read(user.getId());
            System.out.println((user.getId() + " | " + user.getUserName() + " | " + user.getUser_Surname() + " | " + user.getEmail() + " | " + user.getPassword()));
        } catch (NullPointerException e) {
            System.out.println("Użytkownika o podanym ID nie ma w bazie");
        }
    }

    public static void printUserByEmail(User user) {
        UserDao userDao = new UserDao();
        try {
            userDao.readByEmail(user.getEmail());
            System.out.println((user.getId() + " | " + user.getUserName() + " | " + user.getUser_Surname() + " | " + user.getEmail() + " | " + user.getPassword()));
        } catch (NullPointerException e) {
            System.out.println("Użytkownika o podanym adresie email nie ma w bazie");
        }
    }

    public static void printAllUsers(User[] users) {
        UserDao userDao = new UserDao();
        User[] allData = userDao.findAll();
        for (User user : allData) {
            System.out.println(user);

        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
