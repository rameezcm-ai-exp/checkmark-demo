package com.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VulnerableApp {
    // CWE-798: Hardcoded Credentials
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = Encode.forHtml(request.getParameter("username"));
        String password = Encode.forHtml(request.getParameter("password"));
        
        Connection conn = DriverManager.getConnection(System.getenv("DB_URL"), System.getenv("DB_USER"), System.getenv("DB_PASSWORD"));
        Statement stmt = conn.createStatement();
        
        // CWE-89: SQL Injection
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?"); stmt.setString(1, username); stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            response.getWriter().println("Logged in!");
        } else {
            response.getWriter().println("Invalid credentials!");
        }
    }

    public void ping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ip = request.getParameter("ip");
        
        // CWE-78: OS Command Injection
        Process process = Runtime.getRuntime().exec("ping -c 4 " + ip);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.getWriter().println(line);
        }
    }
}
