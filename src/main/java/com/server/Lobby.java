package com.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.io.BufferedReader;
// import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;

import com.utli.Logger;

@CrossOrigin(origins = "*")
@RestController
public class Lobby {
    private static final Logger LOGGER = new Logger();

    @CrossOrigin(origins = "*")
    @org.springframework.web.bind.annotation.PostMapping(
        value = "/lobby/login",
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            // ignore
        }
        String request = sb.toString();
        
        List<String> parts = Arrays.asList(request.split("&"));
        String username = "";
        String password = "";

        for (int i = 0; i < parts.size(); i++) {
            if (parts.get(i) != null) {
                String[] kv = parts.get(i).split("=", 2);
                if (kv.length > 1) {
                    if (kv[0].equals("username")) {
                        username = kv[1];
                    } else if (kv[0].equals("password")) {
                        password = kv[1];
                    }
                }
            }
        }
        LOGGER.log("/lobby/login, request: " + request + ", username: " + username + ", password: " + password);

        Map<String, Object> response = new HashMap<>();

        if (password.equals("lfl1234")) {
            response.put("status", "1");
            response.put("message", "Login successful");
        } else {
            response.put("status", "0");
            response.put("message", "Password error");}

        return ResponseEntity.ok(response);
    }
    
    // private void MysqlHelperTest() {
    //     // example (任意位置呼叫)
    //     try (MysqlHelper db = new MysqlHelper("localhost", 3306, "testdb", "user", "pass")) {
    //         // 查詢多筆
    //         List<Map<String,Object>> rows = db.queryList("SELECT id,name FROM users WHERE status = ?", "active");
    //         // 單筆
    //         Map<String,Object> one = db.queryOne("SELECT * FROM users WHERE id = ?", 123);
    //         // 新增並取得 id
    //         long id = db.insertAndGetId("INSERT INTO users(name,status) VALUES(?,?)", "bob", "active");
    //         // 更新
    //         int updated = db.executeUpdate("UPDATE users SET status = ? WHERE id = ?", "inactive", id);
    //         // 切換資料庫
    //         db.changeDatabase("otherdb");
    //     } catch (SQLException e) {
    //         LOGGER.log("Database error: " + e.getMessage());
    //     }
    // }
}
