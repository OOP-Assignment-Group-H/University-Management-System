public class Course {
    public int id;
    public String code;   // courses.course_code
    public String name;   // courses.course_name
    public int credits;
    public int completedPct;

    public Course(int id, String code, String name, int credits, int completedPct) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.completedPct = completedPct;
    }
}
