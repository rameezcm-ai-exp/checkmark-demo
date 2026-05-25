const express = require('express');
const mysql = require('mysql');
const { exec } = require('child_process');

const app = express();
app.use(express.json());

// CWE-798: Hardcoded Credentials
const dbPassword = "SuperSecretPassword123!"; 

const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: dbPassword,
  database: 'myapp'
});

// CWE-89: SQL Injection
app.post('/login', (req, res) => {
  const username = req.body.username;
  const password = req.body.password;
  
  // Vulnerable to SQLi
  const query = `SELECT * FROM users WHERE username = '${username}' AND password = '${password}'`;
  
  connection.query(query, (error, results) => {
    if (error) throw error;
    if (results.length > 0) {
      res.send("Logged in!");
    } else {
      res.send("Invalid credentials!");
    }
  });
});

// CWE-78: OS Command Injection
app.get('/ping', (req, res) => {
  const ip = req.query.ip;
  // Vulnerable to command injection (e.g. ?ip=127.0.0.1; cat /etc/passwd)
  exec(`ping -c 4 ${ip}`, (error, stdout, stderr) => {
    res.send(`<pre>${stdout}</pre>`);
  });
});

app.listen(3000, () => console.log('Server running on port 3000'));
