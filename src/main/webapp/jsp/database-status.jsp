<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>

<%
    // Get database configuration from servlet context
    String dbUrl = (String) request.getAttribute("dbUrl");
    String dbUser = (String) request.getAttribute("dbUser");
    String dbPassword = (String) request.getAttribute("dbPassword");
    boolean isDatabaseEnabled = (Boolean) request.getAttribute("isDatabaseEnabled");
    String connectionStatus = (String) request.getAttribute("connectionStatus");
    String connectionError = (String) request.getAttribute("connectionError");
    List<Map<String, Object>> userData = (List<Map<String, Object>>) request.getAttribute("userData");
    boolean hasUserData = (Boolean) request.getAttribute("hasUserData");
    boolean tableExists = (Boolean) request.getAttribute("tableExists");
%>

<!DOCTYPE html>
<html lang='en'>
<head>
    <meta charset='UTF-8'>
    <meta name='viewport' content='width=device-width, initial-scale=1.0'>
    <title>Database Status - C# Cheatsheet</title>
    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>
    <link href='https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css' rel='stylesheet'>
    <link href='css/styles.css' rel='stylesheet'>
</head>
<body>
    <!-- Header -->
    <header class='header'>
        <div class='container'>
            <h1><i class='fas fa-database'></i> Database Status</h1>
            <p>C# Cheatsheet Application - Database Connection Status</p>
        </div>
    </header>
    
    <!-- Main Content -->
    <main class='main-content'>
        <div class='container'>
            
            <% if (!isDatabaseEnabled) { %>
                <!-- Database Disabled Message -->
                <div class='card mb-4'>
                    <div class='card-header alert-warning'>
                        <h3><i class='fas fa-exclamation-triangle'></i> Database: Disabled</h3>
                    </div>
                    <div class='card-body'>
                        <p>The application is running without database connectivity.</p>
                        <p>To enable database features, configure MySQL and update web.xml parameters.</p>
                    </div>
                </div>
            <% } else if ("success".equals(connectionStatus)) { %>
                <!-- Successful Connection -->
                <div class='card mb-4'>
                    <div class='card-header alert-success'>
                        <h3><i class='fas fa-check-circle'></i> Database: Connected Successfully!</h3>
                    </div>
                    <div class='card-body'>
                        <p><strong>Database URL:</strong> <%= dbUrl %></p>
                        <p><strong>Connection Status:</strong> Active</p>
                        <p><strong>Server Time:</strong> <%= new java.util.Date() %></p>
                    </div>
                </div>
                
                <% if (tableExists && hasUserData) { %>
                    <!-- User Data Table -->
                    <div class='card mb-4'>
                        <div class='card-header'>
                            <h4><i class='fas fa-table'></i> Sample Data from 'users' Table</h4>
                        </div>
                        <div class='card-body'>
                            <div class='table-responsive'>
                                <table class='table table-striped'>
                                    <thead>
                                        <tr><th>ID</th><th>Name</th><th>Email</th></tr>
                                    </thead>
                                    <tbody>
                                        <% 
                                        if (userData != null && !userData.isEmpty()) {
                                            for (Map<String, Object> user : userData) {
                                        %>
                                            <tr>
                                                <td><%= user.get("id") %></td>
                                                <td><%= user.get("name") %></td>
                                                <td><%= user.get("email") %></td>
                                            </tr>
                                        <%
                                            }
                                        } else {
                                        %>
                                            <tr><td colspan='3' class='text-center'>No data found</td></tr>
                                        <% } %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <% } else if (tableExists && !hasUserData) { %>
                    <!-- Table exists but no data -->
                    <div class='card mb-4'>
                        <div class='card-header'>
                            <h4><i class='fas fa-table'></i> Sample Data from 'users' Table</h4>
                        </div>
                        <div class='card-body'>
                            <div class='table-responsive'>
                                <table class='table table-striped'>
                                    <thead>
                                        <tr><th>ID</th><th>Name</th><th>Email</th></tr>
                                    </thead>
                                    <tbody>
                                        <tr><td colspan='3' class='text-center'>No data found</td></tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                <% } else { %>
                    <!-- Table Setup Required -->
                    <div class='card mb-4'>
                        <div class='card-header alert-warning'>
                            <h4><i class='fas fa-info-circle'></i> Table Setup Required</h4>
                        </div>
                        <div class='card-body'>
                            <p>Database connection is working, but the 'users' table needs to be created.</p>
                            <h6>Setup SQL Commands:</h6>
                            <div class='code-block'>
                                <pre>CREATE DATABASE Csharpdb;
USE Csharpdb;
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
INSERT INTO users (name, email) VALUES
    ('John Doe', 'john@example.com'),
    ('Jane Smith', 'jane@example.com');</pre>
                            </div>
                        </div>
                    </div>
                <% } %>
            <% } else { %>
                <!-- Connection Error -->
                <div class='card mb-4'>
                    <div class='card-header alert-danger'>
                        <h3><i class='fas fa-times-circle'></i> Database Connection: Failed</h3>
                    </div>
                    <div class='card-body'>
                        <p><strong>Error:</strong> <%= connectionError %></p>
                        <h6>Common Solutions:</h6>
                        <ul>
                            <li>Start MySQL server</li>
                            <li>Create database 'Csharpdb' and 'users' table</li>
                            <li>Check connection parameters in web.xml</li>
                            <li>Verify MySQL is running on localhost:3306</li>
                        </ul>
                    </div>
                </div>
            <% } %>
            
            <!-- Navigation Links -->
            <div class='card'>
                <div class='card-header'>
                    <h4><i class='fas fa-link'></i> Navigation</h4>
                </div>
                <div class='card-body'>
                    <div class='grid'>
                        <a href='index.html' class='btn btn-primary me-2'>
                            <i class='fas fa-home'></i> Home Page
                        </a>
                        <a href='CheatsheetServlet' class='btn btn-success me-2'>
                            <i class='fas fa-book'></i> C# Cheatsheet
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>
    
    <!-- Footer -->
    <footer class='footer'>
        <div class='container text-center'>
            <p>&copy; 2024 C# Cheatsheet. Database: <strong>Csharpdb</strong></p>
        </div>
    </footer>
    
    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>
</body>
</html>