package ra.cms.model;

import java.time.LocalDateTime;

public class Course {
    private Long id;
    private String name;
    private Integer duration;
    private String instructor;
    private LocalDateTime createdAt;

    // Constructors
    public Course() {
    }

    public Course(Long id, String name, Integer  duration, String instructor, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.instructor = instructor;
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

    public Integer  getDuration() {
        return duration;
    }

    public void setDuration(Integer  duration) {
        this.duration = duration;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
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
        return "Course {" +
                " id = " + id +
                ", name = '" + name + '\'' +
                ", duration = " + duration + " tháng/giờ" +
                ", instructor = '" + instructor + '\'' +
                ", createdAt = " + createdAt +
                " }";
    }
}