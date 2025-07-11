package model;

public class Grade {
    private int gradeId;
    private String studentId;
    private String courseId;
    private String semester;
    private double midtermGrade;
    private double finalGrade;
    private double overallGrade;
    private String status;
    private String notes;

    public Grade(int gradeId, String studentId, String courseId, String semester, double midtermGrade,
                 double finalGrade, double overallGrade, String status, String notes) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.semester = semester;
        this.midtermGrade = midtermGrade;
        this.finalGrade = finalGrade;
        this.overallGrade = overallGrade;
        this.status = status;
        this.notes = notes;
    }

    // Getters and setters
    public int getGradeId() { return gradeId; }
    public void setGradeId(int gradeId) { this.gradeId = gradeId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getCourseId() { return courseId; }
    public void setCourseId(String courseId) { this.courseId = courseId; }
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    public double getMidtermGrade() { return midtermGrade; }
    public void setMidtermGrade(double midtermGrade) { this.midtermGrade = midtermGrade; }
    public double getFinalGrade() { return finalGrade; }
    public void setFinalGrade(double finalGrade) { this.finalGrade = finalGrade; }
    public double getOverallGrade() { return overallGrade; }
    public void setOverallGrade(double overallGrade) { this.overallGrade = overallGrade; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
}
