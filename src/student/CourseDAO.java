import java.sql.*;
import java.util.*;

public class CourseDAO {

    /** Courses the given student is enrolled in (joins your existing enrollments table). */
    public List<Course> getEnrolledCourses(String studentId) throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.credits, c.completed_pct " +
                     "FROM courses c " +
                     "JOIN enrollments e ON c.course_id = e.course_id " +
                     "WHERE e.student_id = ? " +
                     "ORDER BY c.course_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_code"),
                            rs.getString("course_name"),
                            rs.getInt("credits"),
                            rs.getInt("completed_pct")));
                }
            }
        }
        return list;
    }

    /** All materials for a course, in section/display order - grouping into sections is done by the caller. */
    public List<Material> getMaterialsForCourse(int courseId) throws SQLException {
        List<Material> list = new ArrayList<>();
        String sql = "SELECT material_id, course_id, section_title, title, type, file_path, item_order, uploaded_at " +
                     "FROM materials WHERE course_id = ? ORDER BY material_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Material(
                            rs.getInt("material_id"),
                            rs.getInt("course_id"),
                            rs.getString("section_title"),
                            rs.getString("title"),
                            rs.getString("type"),
                            rs.getString("file_path"),
                            rs.getInt("item_order"),
                            rs.getTimestamp("uploaded_at")));
                }
            }
        }
        return list;
    }

    /** Groups a flat material list into sections, preserving first-seen section order. */
    public static LinkedHashMap<String, List<Material>> groupBySection(List<Material> materials) {
        LinkedHashMap<String, List<Material>> sections = new LinkedHashMap<>();
        for (Material m : materials) {
            sections.computeIfAbsent(m.sectionTitle, k -> new ArrayList<>()).add(m);
        }
        for (List<Material> items : sections.values()) {
            items.sort(Comparator.comparingInt(m -> m.itemOrder));
        }
        return sections;
    }
}
