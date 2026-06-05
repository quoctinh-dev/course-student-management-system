package ra.cms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Student {
    private Long id;
    private String name;
    private LocalDate dob; // Thay thế cho DATE
    private String email;
    private boolean sex;   // true: Nam, false: Nữ (hoặc ngược lại)
    private String phone;
    private String password;
    private LocalDateTime createdAt; // Thay thế cho TIMESTAMP

    // Constructors
    public Student() {
    }

    public Student(Long id, String name, LocalDate dob, String email, boolean sex, String phone, String password, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.email = email;
        this.sex = sex;
        this.phone = phone;
        this.password = password;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Custom toString
    @Override
    public String toString() {
        return "Student {" +
                " id = " + id +
                ", name = '" + name + '\'' +
                ", dob = " + dob +
                ", email = '" + email + '\'' +
                ", sex = " + (sex ? "Nam" : "Nữ") +
                ", phone = '" + phone + '\'' +
                ", createdAt = " + createdAt +
                " }";
    }
}