package model;

public class Class {
    private String classId;
    private String className;
    private String departmentId;
    private String academicYear;
    private String teacherId;

    public Class(String classId, String className, String departmentId, String academicYear, String teacherId) {
        this.classId = classId;
        this.className = className;
        this.departmentId = departmentId;
        this.academicYear = academicYear;
        this.teacherId = teacherId;
    }

    // Getters and setters
    public String getClassId() { return classId; }
    public void setClassId(String classId) { this.classId = classId; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }
    public String getTeacherId() { return teacherId; }
    public void setTeacherId(String teacherId) { this.teacherId = teacherId; }
    
}
