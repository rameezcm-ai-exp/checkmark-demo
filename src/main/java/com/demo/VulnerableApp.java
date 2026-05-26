package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

public class VulnerableApp {
    // Use environment variables or a secure storage for credentials
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // Validate and sanitize input
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", username) || !Pattern.matches("^[a-zA-Z0-9_]+$", password)) {
            response.getWriter().println("Invalid input!");
            return;
        }
        
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db", "root", DB_PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        pstmt.setString(1, username);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            response.getWriter().println("Logged in!");
        } else {
            response.getWriter().println("Invalid credentials!");
        }
    }

    public void ping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ip = request.getParameter("ip");
        
        // Validate IP address
        if (!Pattern.matches("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", ip)) {
            response.getWriter().println("Invalid IP address!");
            return;
        }
        
        // Use a library or a more secure way to execute system commands
        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", "4", ip);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.getWriter().println(line);
        }
    }
}