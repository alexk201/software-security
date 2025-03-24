import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.*;
import java.io.*;

public class VulnerableJava extends HttpServlet {

    // Vulnerable to SQL Injection: Directly concatenating user input into SQL queries.
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Construct SQL query using unsanitized inputs.
        String query = "SELECT * FROM users WHERE username = '" + username +
                       "' AND password = '" + password + "'";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        PrintWriter out = response.getWriter();
        try {
            // Load MySQL driver and connect to database.
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "testuser", "testpass");
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                out.println("Login successful for user: " + username);
            } else {
                out.println("Login failed");
            }
        } catch(Exception e){
            // Vulnerable error handling: Reveals detailed error messages.
            out.println("Database error: " + e.getMessage());
        } finally {
            try { if(rs != null) rs.close(); } catch(Exception e) {}
            try { if(stmt != null) stmt.close(); } catch(Exception e) {}
            try { if(conn != null) conn.close(); } catch(Exception e) {}
        }
    }

    // Vulnerable to command injection: Using Runtime.exec() with unsanitized input.
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String command = request.getParameter("cmd");
        PrintWriter out = response.getWriter();
        try {
            // Directly executes user-supplied command.
            Process p = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                out.println(line);
            }
        } catch(Exception e) {
            out.println("Error executing command: " + e.getMessage());
        }
    }
}
