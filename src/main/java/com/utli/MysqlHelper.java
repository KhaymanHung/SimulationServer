package com.utli;

import java.sql.*;
import java.util.*;

/**
 * 簡單的 MySQL helper（JDBC），建構時傳入連線參數，可切換資料庫。
 * 支援新增/修改/刪除及查詢，查詢結果以 List<Map<String,Object>> 或 Map<String,Object> 回傳。
 *
 * 注意：專案部署時請加上 MySQL JDBC driver（例如 mysql-connector-java）於 classpath 或 pom.xml 依賴。
 */
public class MysqlHelper implements AutoCloseable {
    private String host;
    private int port;
    private String db;
    private String user;
    private String password;
    private Connection conn;

    public MysqlHelper(String host, int port, String db, String user, String password) throws SQLException {
        this.host = host;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
        this.conn = createConnection();
    }

    private Connection createConnection() throws SQLException {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", host, port, db);
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 更換目前使用的資料庫（會重新建立連線）
     */
    public synchronized void changeDatabase(String newDb) throws SQLException {
        if (newDb == null || newDb.isEmpty()) return;
        if (Objects.equals(this.db, newDb)) return;
        this.db = newDb;
        closeConnectionSilently();
        this.conn = createConnection();
    }

    /**
     * 執行 INSERT/UPDATE/DELETE，回傳受影響列數
     */
    public int executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            return ps.executeUpdate();
        }
    }

    /**
     * 執行 INSERT 並回傳 auto-generated key（若有）
     * 若無回傳 key，回傳 -1
     */
    public long insertAndGetId(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParams(ps, params);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    /**
     * 查詢多列結果，回傳 List<Map<String,Object>>
     */
    public List<Map<String, Object>> queryList(String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return resultSetToList(rs);
            }
        }
    }

    /**
     * 查詢單列（第一列），若無資料回傳 null
     */
    public Map<String, Object> queryOne(String sql, Object... params) throws SQLException {
        List<Map<String, Object>> list = queryList(sql, params);
        return list.isEmpty() ? null : list.get(0);
    }

    private void setParams(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p instanceof java.util.Date date) {
                ps.setTimestamp(i + 1, new Timestamp(date.getTime()));
            } else {
                ps.setObject(i + 1, p);
            }
        }
    }

    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        ResultSetMetaData md = rs.getMetaData();
        int cols = md.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= cols; i++) {
                String col = md.getColumnLabel(i);
                Object val = rs.getObject(i);
                row.put(col, val);
            }
            rows.add(row);
        }
        return rows;
    }

    private void closeConnectionSilently() {
        if (this.conn != null) {
            try { this.conn.close(); } catch (SQLException ignored) {}
            this.conn = null;
        }
    }

    @Override
    public void close() {
        closeConnectionSilently();
    }
}