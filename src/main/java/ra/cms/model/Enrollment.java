package ra.cms.model;

import java.time.LocalDateTime;

public class Enrollment {
    private Long id;
    private Student student; // Khóa ngoại liên kết tới Object Student
    private Course course;   // Khóa ngoại liên kết tới Object Course
    private LocalDateTime registeredAt;
    private EnrollmentStatus status;

    // Constructors
    public Enrollment() {
    }

    public Enrollment(Long id, Student student, Course course, LocalDateTime registeredAt, EnrollmentStatus status) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.registeredAt = registeredAt;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }

    // Custom toString tránh in toàn bộ object phức tạp gây chậm hoặc vòng lặp
    @Override
    public String toString() {
        return "Enrollment {" +
                " id = " + id +
                ", studentName = " + (student != null ? student.getName() : "null") +
                ", courseName = " + (course != null ? course.getName() : "null") +
                ", registeredAt = " + registeredAt +
                ", status = " + status +
                " }";
    }
}