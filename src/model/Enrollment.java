package model;

public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private int courseId;
    private double grade;

    public Enrollment(int enrollmentId, int studentId, int courseId, double grade) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    // Getters
    public int getEnrollmentId() { return enrollmentId; }

    public int getStudentId() { return studentId; }

    public int getCourseId() { return courseId; }

    public double getGrade() { return grade; }

    // Setters
    public void setGrade(double grade) { this.grade = grade; }

    // Utility method to calculate the letter grade
    public String getLetterGrade() {
        if (grade >= 90) {
            return "A";
        } else if (grade >= 80) {
            return "B";
        } else if (grade >= 70) {
            return "C";
        } else if (grade >= 60) {
            return "D";
        } else {
            return "F";
        }


    }
}
