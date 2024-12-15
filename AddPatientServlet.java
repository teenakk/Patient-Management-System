import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/addPatient")
public class AddPatientServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contact_number");
        
        try {
            int age = Integer.parseInt(ageStr);
            
            // Database connection
            String jdbcURL = "jdbc:mysql://localhost:3306/patient_management";
            String dbUser = "root";
            String dbPassword = "teena123";
            
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Insert patient details via stored procedure
            try (Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
                String sql = "{CALL add_patient(?, ?, ?, ?, ?)}";
                try (CallableStatement stmt = conn.prepareCall(sql)) {
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, gender);
                    stmt.setString(4, address);
                    stmt.setString(5, contactNumber);
                    stmt.executeUpdate();
                }
            }

            // Redirect to success page
            response.sendRedirect("success.html");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Invalid age format. Please enter a valid number.");
        } catch (SQLException e) {
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("An error occurred while adding patient details. Please try again.");
        } catch (Exception e) {
            
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("An unexpected error occurred. Please contact support.");
        }

       
    }
}
