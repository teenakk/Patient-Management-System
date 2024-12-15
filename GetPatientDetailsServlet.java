import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/getPatientDetails")
public class GetPatientDetailsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patientIdStr = request.getParameter("patient_id");

        try {
            int patientId = Integer.parseInt(patientIdStr);

            String jdbcURL = "jdbc:mysql://localhost:3306/patient_management";
            String dbUser = "root";
            String dbPassword = "teena123";

            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
                String sql = "{CALL getpatient_details(?)}";
                try (CallableStatement stmt = conn.prepareCall(sql)) {
                    stmt.setInt(1, patientId);
                    ResultSet rs = stmt.executeQuery();
                    
                    response.setContentType("text/html");
                    if (rs.next()) {
                        response.getWriter().println("<h1>Patient Details</h1>");
                        response.getWriter().println("<p>Name: " + rs.getString("name") + "</p>");
                        response.getWriter().println("<p>Age: " + rs.getInt("age") + "</p>");
                        response.getWriter().println("<p>Gender: " + rs.getString("gender") + "</p>");
                        response.getWriter().println("<p>Address: " + rs.getString("address") + "</p>");
                        response.getWriter().println("<p>Contact Number: " + rs.getString("contact_number") + "</p>");
                    } else {
                        response.getWriter().println("<h1>No patient found with ID: " + patientId + "</h1>");
                    }
                }
            }
        } catch (NumberFormatException e) {
            response.getWriter().println("Invalid patient ID format.");
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error retrieving patient details: " + e.getMessage());
        }
    }
}
