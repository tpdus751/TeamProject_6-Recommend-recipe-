package DB;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe"; // 데이터베이스 URL
    static final String USER = "c##tpdus751"; // 사용자 이름
    static final String PASS = "0605"; // 비밀번호

    public static Connection getConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
