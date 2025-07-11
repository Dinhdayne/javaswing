package model;

public class Course {
    private String courseId;
    private String courseName;
    private int credits;
    private String departmentId;
    private String teacherId;
    private String semester;

    public Course(String courseId, String courseName, int credits, String departmentId, String teacherId, String semester) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.departmentId = departmentId;
        this.teacherId = teacherId;
        this.semester = semester;
    }

    // Getters and setters
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}
