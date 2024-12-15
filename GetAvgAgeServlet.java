import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.CallableStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getAvgAge")
public class GetAvgAgeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String jdbcURL = "jdbc:mysql://localhost:3306/patient_management";
            String dbUser = "root";
            String dbPassword = "teena123";
            
            Class.forName("com.mysql.cj.jdbc.Driver");

    
            try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
                String sql = "{? = CALL get_avg_age()}";
                try (CallableStatement stmt = conn.prepareCall(sql)) {
                    stmt.registerOutParameter(1, java.sql.Types.DECIMAL);
                    stmt.execute();
                    BigDecimal avgAge = stmt.getBigDecimal(1);
                    
                    response.setContentType("text/html");
                    response.getWriter().println("<h1>Average Age: " + avgAge + "</h1>");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error retrieving average age: " + e.getMessage());
        }
    }
}
