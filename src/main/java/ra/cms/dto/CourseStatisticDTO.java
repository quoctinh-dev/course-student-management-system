package ra.cms.dto;

public class CourseStatisticDTO {
    private Long courseId;
    private String courseName;
    private int studentCount;

    public CourseStatisticDTO() {
    }

    public CourseStatisticDTO(Long courseId, String courseName, int studentCount) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.studentCount = studentCount;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(int studentCount) {
        this.studentCount = studentCount;
    }
}