package DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/recipeDetail")
public class RecipeDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT name, ingredients, method, image_path FROM recipes WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String ingredients = rs.getString("ingredients");
                        String method = rs.getString("method");
                        String imagePath = rs.getString("image_path");

                        String imageUrl = request.getContextPath() + "/" + imagePath;

                        response.getWriter().println("<!DOCTYPE html><html lang=\"en\"><head><meta charset=\"UTF-8\">" +
                                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>Document</title>" +
                                "<link rel=\"stylesheet\" href=\"index.css\"></head><body><div class=\"menu-container\">" +
                                // 메뉴 컨테이너 내용 생략
                                "</div><main><div class=\"recipeSection\"><div><img src=\"" + imageUrl + 
                                "\" alt=\"\" class=\"recipe_img_big\"></div><h1>" + name + "</h1><h3>" + method + 
                                "</h3><h3>" + ingredients + 
                                "</h3><div><img src=\"images/ico-rec.png\" alt=\"\" class=\"small-img hover-img\">" +
                                "<img src=\"images/ico-bookmark.png\" alt=\"\" class=\"small-img hover-img\">" +
                                "</div></div></main></body></html>");
                    } else {
                        response.getWriter().println("<html><body><h1>Recipe not found.</h1></body></html>");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<html><body><h1>Error occurred while fetching recipe details.</h1></body></html>");
        }
    }
}
