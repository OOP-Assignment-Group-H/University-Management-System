package student.course;
import java.sql.*;
import java.util.*;

public class SubmissionDAO {

    public int addSubmission(int courseId, String studentId, String fileName, String storedPath) throws SQLException {
        String sql = "INSERT INTO submissions (course_id, student_id, file_name, file_path) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, courseId);
            ps.setString(2, studentId);
            ps.setString(3, fileName);
            ps.setString(4, storedPath);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    public List<Submission> getSubmissions(int courseId, String studentId) throws SQLException {
        List<Submission> list = new ArrayList<>();
        String sql = "SELECT submission_id, course_id, student_id, file_name, file_path, submitted_at " +
                "FROM submissions WHERE course_id = ? AND student_id = ? ORDER BY submitted_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            ps.setString(2, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Submission(
                            rs.getInt("submission_id"),
                            rs.getInt("course_id"),
                            rs.getString("student_id"),
                            rs.getString("file_name"),
                            rs.getString("file_path"),
                            rs.getTimestamp("submitted_at")));
                }
            }
        }
        return list;
    }
}

