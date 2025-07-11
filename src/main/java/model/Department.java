package model;
    
public class Department {
    private String departmentId;
    private String departmentName;
    private String headTeacherId;
    private String phone;
    private String email;

    public Department(String departmentId, String departmentName, String headTeacherId, String phone, String email) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.headTeacherId = headTeacherId;
        this.phone = phone;
        this.email = email;
    }

    // Getters and setters
    public String getDepartmentId() { return departmentId; }
    public void setDepartmentId(String departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getHeadTeacherId() { return headTeacherId; }
    public void setHeadTeacherId(String headTeacherId) { this.headTeacherId = headTeacherId; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
}
