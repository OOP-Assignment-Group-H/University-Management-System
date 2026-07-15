import java.sql.Timestamp;

public class Submission {
    public int id;
    public int courseId;
    public String studentId;   // matches students.student_id e.g. "CS2022001"
    public String fileName;
    public String filePath;
    public Timestamp submittedAt;

    public Submission(int id, int courseId, String studentId, String fileName, String filePath, Timestamp submittedAt) {
        this.id = id;
        this.courseId = courseId;
        this.studentId = studentId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.submittedAt = submittedAt;
    }
}
