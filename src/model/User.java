package model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String email;

    public User(int userId, String username, String password, String role, String email) {
        this.userId = userId;
        this.username = username;
        this.password = password; // In practice, hash this!
        this.role = role;
        this.email = email;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
}