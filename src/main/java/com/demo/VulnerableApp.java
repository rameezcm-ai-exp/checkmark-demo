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
    // CWE-798: Hardcoded Credentials
    private static final String DB_PASSWORD = "SuperSecretPassword123!";

    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
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
        if (!Pattern.matches("^([0-9]{1,3}\.){3}[0-9]{1,3}$", ip)) {
            response.getWriter().println("Invalid IP address!");
            return;
        }
        
        // Use ProcessBuilder to execute the command with arguments
        ProcessBuilder processBuilder = new ProcessBuilder("ping", "-c", "4", ip);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.getWriter().println(line);
        }
    }
}