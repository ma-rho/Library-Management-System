package library.management.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;
    private String phone;

    // Constructor, Getters, and Setters
    public User(int userId, String username, String password, String role, String email, String phone) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phone = phone;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
