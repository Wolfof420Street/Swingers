package model;

public class Course {
    private int courseId;
    private String courseName;
    private int instructorId;
    private int credits;

    public Course(int courseId, String courseName, int instructorId, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.instructorId = instructorId;
        this.credits = credits;
    }

    // Getters
    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getInstructorId() { return instructorId; }
    public int getCredits() { return credits; }
}