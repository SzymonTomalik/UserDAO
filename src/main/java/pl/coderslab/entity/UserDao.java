package pl.coderslab.entity;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;


public class UserDao {
    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String READ_USER_QUERY = "SELECT * FROM users WHERE id=?;";
    private static final String READ_ALL_USER_QUERY = "SELECT * FROM users;";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET username=?, email=?, password=? WHERE id=?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id=?";


    public User create(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, hashPassword(user.getPassword()));
            preStmt.executeUpdate();
            ResultSet rs = preStmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
                System.out.println("Inserted Id: " + user.getId());
            }
            return user;
        } catch (SQLException e) {
            System.out.println("The entered data is incorrect or the email address provided already exists");
            e.printStackTrace();
            return null;
        }
    }

    public static void addUser(String name, String email, String password) {
        User user = new User();
        user.setUserName(name);
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
                if (resultSet.getInt("id") == userId) {

                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setUserName(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    return user;
                }
                if (resultSet.getInt("id") > userId) {
                    System.out.println("User is not exist in database.");
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void printUserById(User user) {
        UserDao userDao = new UserDao();
        try {
            userDao.read(user.getId());
            System.out.println((user.getId() + " | " + user.getUserName() + " | " + user.getEmail() + " | " + user.getPassword()));
        } catch (NullPointerException e) {
            System.out.println("The user with this ID is not exist in the database ");
        }
    }

    public void update(User user) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(UPDATE_USER_QUERY);
            PreparedStatement statement1 = conn.prepareStatement("SELECT * FROM users");
            ResultSet resultSet = statement1.executeQuery();
            preStmt.setString(1, user.getUserName());
            preStmt.setString(2, user.getEmail());
            preStmt.setString(3, this.hashPassword(user.getPassword()));
            preStmt.setInt(4, user.getId());
            preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Your changes have incorrect data or the email address provided already exists");
            e.printStackTrace();
        }
    }


    public void delete(int userId) {
        try (Connection conn = DbUtil.getConnection()) {
            PreparedStatement preStmt = conn.prepareStatement(DELETE_USER_QUERY);
            preStmt.setInt(1, userId);
            preStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Incorrect data");
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
