import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            generateStatusPage(out);
        }
    }
    
    private void generateStatusPage(PrintWriter out) {
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("    <title>Database Status - C# Cheatsheet</title>");
        out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
        out.println("    <link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css' rel='stylesheet'>");
        out.println("    <link href='css/styles.css' rel='stylesheet'>");
        out.println("</head>");
        out.println("<body>");
        
        printHeader(out);
        printMainContent(out);
        printFooter(out);
        
        out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
        out.println("</body>");
        out.println("</html>");
    }
    
    private void printHeader(PrintWriter out) {
        out.println("    <header class='header'>");
        out.println("        <div class='container'>");
        out.println("            <h1><i class='fas fa-database'></i> Database Status</h1>");
        out.println("            <p>C# Cheatsheet Application - Database Connection Status</p>");
        out.println("        </div>");
        out.println("    </header>");
    }
    
    private void printMainContent(PrintWriter out) {
        out.println("    <main class='main-content'>");
        out.println("        <div class='container'>");
        
        if (!isDatabaseEnabled) {
            printDatabaseDisabledMessage(out);
        } else {
            testDatabaseConnection(out);
        }
        
        printNavigationLinks(out);
        
        out.println("        </div>");
        out.println("    </main>");
    }
    
    private void printDatabaseDisabledMessage(PrintWriter out) {
        out.println("            <div class='card mb-4'>");
        out.println("                <div class='card-header alert-warning'>");
        out.println("                    <h3><i class='fas fa-exclamation-triangle'></i> Database: Disabled</h3>");
        out.println("                </div>");
        out.println("                <div class='card-body'>");
        out.println("                    <p>The application is running without database connectivity.</p>");
        out.println("                    <p>To enable database features, configure MySQL and update web.xml parameters.</p>");
        out.println("                </div>");
        out.println("            </div>");
    }
    
    private void testDatabaseConnection(PrintWriter out) {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Test database connection
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                printSuccessfulConnection(out, conn);
                queryUserData(out, conn);
            }
        } catch (ClassNotFoundException | SQLException e) {
            printConnectionError(out, e);
        }
    }
    
    private void printSuccessfulConnection(PrintWriter out, @SuppressWarnings("unused") Connection conn) {
        out.println("            <div class='card mb-4'>");
        out.println("                <div class='card-header alert-success'>");
        out.println("                    <h3><i class='fas fa-check-circle'></i> Database: Connected Successfully!</h3>");
        out.println("                </div>");
        out.println("                <div class='card-body'>");
        out.println("                    <p><strong>Database URL:</strong> " + dbUrl + "</p>");
        out.println("                    <p><strong>Connection Status:</strong> Active</p>");
        out.println("                    <p><strong>Server Time:</strong> " + new java.util.Date() + "</p>");
        out.println("                </div>");
        out.println("            </div>");
    }
    
    private void queryUserData(PrintWriter out, Connection conn) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, email FROM users LIMIT 5")) {

            out.println("            <div class='card mb-4'>");
            out.println("                <div class='card-header'>");
            out.println("                    <h4><i class='fas fa-table'></i> Sample Data from 'users' Table</h4>");
            out.println("                </div>");
            out.println("                <div class='card-body'>");
            out.println("                    <div class='table-responsive'>");
            out.println("                        <table class='table table-striped'>");
            out.println("                            <thead>");
            out.println("                                <tr><th>ID</th><th>Name</th><th>Email</th></tr>");
            out.println("                            </thead>");
            out.println("                            <tbody>");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                out.printf("                                <tr><td>%d</td><td>%s</td><td>%s</td></tr>%n", id, name, email);
            }
            
            if (!hasData) {
                out.println("                                <tr><td colspan='3' class='text-center'>No data found</td></tr>");
            }
            
            out.println("                            </tbody>");
            out.println("                        </table>");
            out.println("                    </div>");
            out.println("                </div>");
            out.println("            </div>");
            
        } catch (Exception tableEx) {
            printTableNotFoundMessage(out);
        }
    }
    
    private void printTableNotFoundMessage(PrintWriter out) {
        out.println("            <div class='card mb-4'>");
        out.println("                <div class='card-header alert-warning'>");
        out.println("                    <h4><i class='fas fa-info-circle'></i> Table Setup Required</h4>");
        out.println("                </div>");
        out.println("                <div class='card-body'>");
        out.println("                    <p>Database connection is working, but the 'users' table needs to be created.</p>");
        out.println("                    <h6>Setup SQL Commands:</h6>");
        out.println("                    <div class='code-block'>");
        out.println("                        <pre>");
        out.println("CREATE DATABASE Csharpdb;");
        out.println("USE Csharpdb;");
        out.println("CREATE TABLE users (");
        out.println("    id INT AUTO_INCREMENT PRIMARY KEY,");
        out.println("    name VARCHAR(100) NOT NULL,");
        out.println("    email VARCHAR(100),");
        out.println("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        out.println(");");
        out.println("INSERT INTO users (name, email) VALUES");
        out.println("    ('John Doe', 'john@example.com'),");
        out.println("    ('Jane Smith', 'jane@example.com');");
        out.println("                        </pre>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
    }
    
    private void printConnectionError(PrintWriter out, Exception e) {
        out.println("            <div class='card mb-4'>");
        out.println("                <div class='card-header alert-danger'>");
        out.println("                    <h3><i class='fas fa-times-circle'></i> Database Connection: Failed</h3>");
        out.println("                </div>");
        out.println("                <div class='card-body'>");
        out.println("                    <p><strong>Error:</strong> " + e.getMessage() + "</p>");
        out.println("                    <h6>Common Solutions:</h6>");
        out.println("                    <ul>");
        out.println("                        <li>Start MySQL server</li>");
        out.println("                        <li>Create database 'Csharpdb' and 'users' table</li>");
        out.println("                        <li>Check connection parameters in web.xml</li>");
        out.println("                        <li>Verify MySQL is running on localhost:3306</li>");
        out.println("                    </ul>");
        out.println("                </div>");
        out.println("            </div>");
    }
    
    private void printNavigationLinks(PrintWriter out) {
        out.println("            <div class='card'>");
        out.println("                <div class='card-header'>");
        out.println("                    <h4><i class='fas fa-link'></i> Navigation</h4>");
        out.println("                </div>");
        out.println("                <div class='card-body'>");
        out.println("                    <div class='grid'>");
        out.println("                        <a href='index.html' class='btn btn-primary me-2'>");
        out.println("                            <i class='fas fa-home'></i> Home Page");
        out.println("                        </a>");
        out.println("                        <a href='CheatsheetServlet' class='btn btn-success me-2'>");
        out.println("                            <i class='fas fa-book'></i> C# Cheatsheet");
        out.println("                        </a>");
        out.println("                    </div>");
        out.println("                </div>");
        out.println("            </div>");
    }
    
    private void printFooter(PrintWriter out) {
        out.println("    <footer class='footer'>");
        out.println("        <div class='container text-center'>");
        out.println("            <p>&copy; 2024 C# Cheatsheet. Database: <strong>Csharpdb</strong></p>");
        out.println("        </div>");
        out.println("    </footer>");
    }
}