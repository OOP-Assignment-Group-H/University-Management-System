package student.course;
import java.sql.*;

public class StudentDAO {

    public static class StudentInfo {
        public String studentId;
        public String fullName;
        public String degreeName;
        public StudentInfo(String studentId, String fullName, String degreeName) {
            this.studentId = studentId;
            this.fullName = fullName;
            this.degreeName = degreeName;
        }
    }

    public StudentInfo getStudent(String studentId) throws SQLException {
        String sql = "SELECT s.student_id, s.full_name, d.degree_name " +
                "FROM students s LEFT JOIN degrees d ON s.degree_id = d.degree_id " +
                "WHERE s.student_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new StudentInfo(rs.getString("student_id"), rs.getString("full_name"), rs.getString("degree_name"));
                }
            }
        }
        return new StudentInfo(studentId, studentId, "");
    }
}

