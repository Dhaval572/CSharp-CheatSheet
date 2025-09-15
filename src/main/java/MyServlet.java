import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
    
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private boolean isDatabaseEnabled = false;

    @Override
    public void init() throws ServletException {
        // Get database configuration from web.xml
        dbUrl = getServletConfig().getInitParameter("dbUrl");
        dbUser = getServletConfig().getInitParameter("dbUser");
        dbPassword = getServletConfig().getInitParameter("dbPassword");
        
        // Check if database is configured
        isDatabaseEnabled = (dbUrl != null && !dbUrl.trim().isEmpty());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Prepare data for JSP
        request.setAttribute("dbUrl", dbUrl);
        request.setAttribute("dbUser", dbUser);
        request.setAttribute("dbPassword", dbPassword);
        request.setAttribute("isDatabaseEnabled", isDatabaseEnabled);
        
        if (!isDatabaseEnabled) {
            request.setAttribute("connectionStatus", "disabled");
        } else {
            testDatabaseConnectionForJSP(request);
        }
        
        // Forward to JSP
        request.getRequestDispatcher("jsp/database-status.jsp").forward(request, response);
    }
    
    private void testDatabaseConnectionForJSP(HttpServletRequest request) {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Test database connection
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                request.setAttribute("connectionStatus", "success");
                queryUserDataForJSP(request, conn);
            }
        } catch (ClassNotFoundException | SQLException e) {
            request.setAttribute("connectionStatus", "error");
            request.setAttribute("connectionError", e.getMessage());
            request.setAttribute("hasUserData", false);
            request.setAttribute("tableExists", false);
        }
    }
    
    private void queryUserDataForJSP(HttpServletRequest request, Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM users LIMIT 5")) {

            List<Map<String, Object>> userData = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("id", rs.getInt("id"));
                user.put("name", rs.getString("name"));
                user.put("email", rs.getString("email"));
                userData.add(user);
            }
            
            request.setAttribute("userData", userData);
            request.setAttribute("hasUserData", !userData.isEmpty());
            request.setAttribute("tableExists", true);
            
        } catch (Exception tableEx) {
            request.setAttribute("hasUserData", false);
            request.setAttribute("tableExists", false);
        }
    }
}