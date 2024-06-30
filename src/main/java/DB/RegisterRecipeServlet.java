package DB;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/registerRecipe")
@MultipartConfig
public class RegisterRecipeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String name = request.getParameter("name");
        String ingredients = request.getParameter("ingredients");
        String method = request.getParameter("method");
        Part filePart = request.getPart("image");

        // 이미지 파일 이름 가져오기
        String fileName = filePart.getSubmittedFileName();

        // 이미지 저장 경로 설정
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();

        // 이미지 파일 저장
        String filePath = uploadPath + File.separator + fileName;
        try (InputStream fileContent = filePart.getInputStream()) {
            Files.copy(fileContent, new File(filePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }

        // 데이터베이스에 이미지 경로 저장
        String imageUrl = "uploads/" + fileName; // 상대 경로로 저장
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO recipes (name, ingredients, method, image_path) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, ingredients);
                stmt.setString(3, method);
                stmt.setString(4, imageUrl);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // '레시피가 성공적으로 등록되었습니다.' 메시지 대신 홈 화면으로 리디렉션
        response.sendRedirect("index.html");
    }
}
