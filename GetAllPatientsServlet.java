import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getAllPatients")
public class GetAllPatientsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        
        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/patient_management";
            String dbUser = "root";
            String dbPassword = "teena123";

            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 CallableStatement stmt = conn.prepareCall("{call fetch_patients()}")) {

                ResultSet rs = stmt.executeQuery();
                
                StringBuilder htmlResponse = new StringBuilder();
                htmlResponse.append("<table class='table table-bordered'><thead><tr><th>ID</th><th>Name</th><th>Age</th><th>Gender</th><th>Address</th><th>Contact Number</th></tr></thead><tbody>");
                
                while (rs.next()) {
                    htmlResponse.append("<tr>");
                    htmlResponse.append("<td>").append(rs.getInt("id")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("name")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getInt("age")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("gender")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("address")).append("</td>");
                    htmlResponse.append("<td>").append(rs.getString("contact_number")).append("</td>");
                    htmlResponse.append("</tr>");
                }
                
                htmlResponse.append("</tbody></table>");
                response.getWriter().println(htmlResponse.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error retrieving patient details: " + e.getMessage());
        }
    }
}
