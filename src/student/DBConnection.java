import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ---- EDIT THESE FOR YOUR SETUP ----
    private static final String HOST     = "localhost";
    private static final String PORT     = "3306";
    private static final String DB_NAME  = "faculty_management_system"; // your existing DB
    private static final String USER     = "appuser";
    private static final String PASSWORD = "app_password";
    // ------------------------------------

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME +
            "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e1) {
            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                throw new SQLException("No MySQL/MariaDB JDBC driver found on classpath. " +
                        "Add mysql-connector-j-x.x.x.jar to your project libraries.", e2);
            }
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
