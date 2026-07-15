import java.sql.Timestamp;

public class Material {
    public int id;
    public int courseId;
    public String sectionTitle;
    public String title;
    public String type;      // PDF, Video, Folder, Link
    public String filePath;
    public int itemOrder;
    public Timestamp uploadedAt;

    public Material(int id, int courseId, String sectionTitle, String title, String type,
                     String filePath, int itemOrder, Timestamp uploadedAt) {
        this.id = id;
        this.courseId = courseId;
        this.sectionTitle = sectionTitle;
        this.title = title;
        this.type = type;
        this.filePath = filePath;
        this.itemOrder = itemOrder;
        this.uploadedAt = uploadedAt;
    }
}
