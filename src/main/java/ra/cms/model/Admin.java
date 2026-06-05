package ra.cms.model;

public class Admin {
    private Long id;
    private String username;
    private String password;

    // Constructors
    public Admin() {
    }

    public Admin(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Custom toString
    @Override
    public String toString() {
        return "Admin {" +
                " id = " + id +
                ", username = '" + username + '\'' +
                ", password = '[PROTECTED]' " +
                " }";
    }
}