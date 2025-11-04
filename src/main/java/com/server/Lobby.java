package com.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.io.BufferedReader;
import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;

import com.utli.MysqlHelper;
import com.utli.Utli;

@CrossOrigin(origins = "*")
@RestController
public class Lobby {
    private static final Utli UTLI = new Utli();

    private static final String HOST = "192.168.1.177";
    private static final int PORT = 3306;
    private static final String DB_NAME = "lobby";
    private static final String DB_USER = "root";
    private static final String DB_PW = "rootpassword";
    private static MysqlHelper db = null;
    static {
        try {
            db = new MysqlHelper(HOST, PORT, DB_NAME, DB_USER, DB_PW);
            UTLI.log("Lobby DB 初始化成功");
        } catch (java.sql.SQLException | RuntimeException e) {
            System.err.println("DB 初始化失敗: " + e.getMessage());
        }
    }

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
                        password = UTLI.md5(kv[1]);
                    }
                }
            }
        }
        UTLI.log("/lobby/login, request: " + request + ", username: " + username + ", password: " + password);

        Map<String, Object> response = new HashMap<>();

        try {
            Map<String, Object> selectData = db.queryOne("SELECT * FROM account WHERE account = ?", username);
            UTLI.log("selectData: " + String.valueOf(selectData));
            if (selectData == null) {
                response.put("code", 2);
                response.put("status", false);
                response.put("msg", "Account not found");
                response.put("data", null);
            } else {
                if (password.equals(selectData.get("password"))) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", selectData.get("id"));
                    data.put("nickname", selectData.get("nickname"));
                    data.put("headImage", selectData.get("headImage"));
                    data.put("sex", selectData.get("sex"));
                    data.put("coin", selectData.get("coin"));
                    data.put("token", UTLI.getToken());
                    response.put("code", 0);
                    response.put("status", true);
                    response.put("msg", "Success");
                    response.put("data", data);
                } else {
                    response.put("code", 1);
                    response.put("status", false);
                    response.put("msg", "Password error");
                    response.put("data", null);
                }
            }
        } catch (SQLException e) {
            UTLI.log("Database error: " + e.getMessage());
            response.put("code", 2);
            response.put("status", false);
            response.put("msg", "Database error");
            response.put("data", null);
        }

        return ResponseEntity.ok(response);
    }
}
