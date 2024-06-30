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

@WebServlet("/searchRecipe")
public class RecipeServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

		String ingredientsInput = request.getParameter("ingredients");
		String[] ingredients = ingredientsInput.split(",");

		try (Connection conn = DBUtil.getConnection()) {
			StringBuilder sqlBuilder = new StringBuilder("SELECT id, name, image_path FROM recipes WHERE ");
			for (int i = 0; i < ingredients.length; i++) {
				sqlBuilder.append("ingredients LIKE ?");
				if (i < ingredients.length - 1) {
					sqlBuilder.append(" AND ");
				}
			}

			try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
				for (int i = 0; i < ingredients.length; i++) {
					stmt.setString(i + 1, "%" + ingredients[i].trim() + "%");
				}
				try (ResultSet rs = stmt.executeQuery()) {
					StringBuilder htmlContent = new StringBuilder();
					while (rs.next()) {
						int id = rs.getInt("id");
						String name = rs.getString("name");
						String imagePath = rs.getString("image_path");

						// HTML 구조를 문자열로 생성
						htmlContent.append("<div class='recipe-image-section'>").append("<a href='recipeDetail?id=")
								.append(id).append("'>").append("<img src='").append(imagePath)
								.append("' alt='' class='recipe_img'>").append("</a>")
								.append("<div class='recipe-information'>").append("<a href='recipeDetail?id=")
								.append(id).append("'>").append("<h4>").append(name).append("</h4>").append("</a>")
								.append("</div>").append("<div class='recipe-icons'>")
								.append("<img src='images/ico-rec.png' alt='' class='small-img hover-img'>")
								.append("<img src='images/ico-bookmark.png' alt='' class='small-img hover-img'>")
								.append("</div>").append("</div>");
					}
					// 기본 HTML 구조에 동적으로 생성된 부분 삽입
					String finalHtml = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
							+ "    <meta charset=\"UTF-8\">\n"
							+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
							+ "    <title>Document</title>\n" + "    <link rel=\"stylesheet\" href=\"index.css\">\n"
							+ "</head>\n" + "<body>\n" + "    <div class=\"menu-container\">\n"
							+ "        <div class=\"user-menu-container\">\n" + "            <div class=\"menu\">\n"
							+ "                <a href=\"\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <img src=\"images/ico-user.png\" alt=\"\" class=\"small-img\">\n"
							+ "                    </div>\n" + "                </a>\n"
							+ "                <a href=\"add_recipe.html\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <b>레시피 등록</b>\n" + "                    </div>\n"
							+ "                </a>\n"
							+ "                <a href=\"search_recipe.html\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <img src=\"images/ico-search.png\" alt=\"\" class=\"small-img\">\n"
							+ "                    </div>\n" + "                </a>\n"
							+ "                <a href=\"\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <b>고객문의</b>\n" + "                    </div>\n"
							+ "                </a>\n" + "            </div>\n" + "        </div>\n"
							+ "        <div class=\"recipe-menu-container\">\n"
							+ "            <div class=\"recipe-menu\">\n"
							+ "                <a href=\"index.html\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <i><b>Prefer</b></i>\n" + "                    </div>\n"
							+ "                </a>\n"
							+ "                <a href=\"index.html\" class=\"text-decoration-none \">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <i><b>Health Care</b></i>\n" + "                    </div>\n"
							+ "                </a>\n"
							+ "                <a href=\"index.html\" class=\"text-decoration-none \">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <i><b>New</b></i>\n" + "                    </div>\n"
							+ "                </a>\n"
							+ "                <a href=\"index.html\" class=\"text-decoration-none\">\n"
							+ "                    <div class=\"text-decoration-none hover-button padding\">\n"
							+ "                        <i><b>Best</b></i>\n" + "                    </div>\n"
							+ "                </a>\n" + "            </div>\n" + "        </div>\n" + "    </div>\n"
							+ "    <main>\n" + "        <div class=\"main-content-container\">\n"
							+ "            <div class=\"search-section-container\">\n"
							+ "                <form action=\"searchRecipe\" method=\"get\">\n"
							+ "                    <div class=\"search-section\">\n"
							+ "                        <input type=\"text\" name=\"ingredients\" class=\"search-text-section-index\" placeholder=\"식재료를 입력해주세요.\">\n"
							+ "                        <input type=\"image\" src=\"images/ico-search.png\" class=\"small-img absolute\" alt=\"제출\">\n"
							+ "                    </div>\n" + "                </form>\n" + "            </div>\n"
							+ "            <div class=\"recipe-section\">\n"
							+ "                <div class=\"recipe-section-header\">\n" + "                    <div>\n"
							+ "                        <a href=\"\">\n"
							+ "                            <h3>Prefer Recipe</h3>\n" + "                        </a>\n"
							+ "                    </div>\n" + "                    <div>\n"
							+ "                        <a href=\"\">\n" + "                            <b>더보기 </b>\n"
							+ "                            <img src=\"images/ico-arrow-down.png\" alt=\"\" class=\"small-img\">\n"
							+ "                        </a>\n" + "                    </div>\n"
							+ "                </div>\n" + htmlContent.toString() + "</div>\n" + "</div>\n"
							+ "</main>\n" + "</body>\n" + "</html>";
					response.getWriter().write(finalHtml);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
