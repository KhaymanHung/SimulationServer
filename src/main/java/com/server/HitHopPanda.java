package com.server;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.io.BufferedReader;
// import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;

import com.utli.BigDecimalUtil;
import com.utli.Logger;

@CrossOrigin(origins = "*")
@RestController
@SuppressWarnings("unchecked")
public class HitHopPanda {
    double userMoney = 0;
    long sid = 1;
    private static final Logger LOGGER = new Logger();
    private final int[] multiple = new int[] {
        0, 0, 0,    // 0 百搭，1 免費遊戲，2 炸彈
        2, 3, 4, 5, // 3-6 一般圖標
        6, 7, 8, 9, // 7-10 一般圖標
        10,11,12    // 11-12 一般圖標
    };

    @CrossOrigin(origins = "*")
    @org.springframework.web.bind.annotation.PostMapping(
        value = "/api/SLOTHHPanda/EnterGame",
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, Object>> enterGame(HttpServletRequest req) {
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
        LOGGER.log("==================================== enter game start ====================================");
        LOGGER.log("enterGame, request: " + request);
        String token = "MTAwMDA0ODY0fDE3NTkzODgxNTZ8MHw4ODY2MDIwfDEwMTAwMDMz";
        this.userMoney = 500000.00;
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("tableid", 10100033);
        data.put("token", token);
        Map<String, Object> innerData = new LinkedHashMap<>();
        innerData.put("userid", 100004864);
        innerData.put("NickName", "2948739");
        innerData.put("gold", this.userMoney);
        innerData.put("betScores", Arrays.asList(1,10,40,200));
        innerData.put("lJackPotScores", new ArrayList<>());
        innerData.put("GlodMultiple", 100);
        innerData.put("wicon", "");
        data.put("data", innerData);
        data.put("gameid", 8866020);
        data.put("levelid", 88660200);
        data.put("BetLv", Arrays.asList(1,2,3,4,5,6,7,8,9,10));
        data.put("Currency", "BRL");
        data.put("totalLineCount", 30);
        data.put("minBetScore", 10);
        
        // 將原本這行
        // data.put("Context", "{\"iTotalFree\":0,\"currTotalFree\":0,\"betScore\":0,\"bSelectFree\":false,\"WinTimes\":[2,2,2,2,2],\"WinItems\":[4,7,8,9,6]}");
        
        // 改為分拆設定
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("iTotalFree", 0);
        context.put("currTotalFree", 0);
        context.put("betScore", 0);
        context.put("bSelectFree", false);
        context.put("WinTimes", Arrays.asList(2, 2, 2, 2, 2));
        List<Integer> WinItems = new ArrayList<>();
        List<Integer> allItems = new ArrayList<>(Arrays.asList(3,4,5,6,7,8,9,10,11,12));
        for (int i = 0; i < 5; i++) {
            if (WinItems.size() >= 5) {
                break;
            }
            int index = (int)Math.floor(Math.random() * allItems.size());
            WinItems.add(allItems.get(index));
            allItems.remove(index);
        }
        context.put("WinItems", WinItems);
        // 將 context 轉為 JSON 字串再放入 data
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String contextJson = objectMapper.writeValueAsString(context);
            data.put("Context", contextJson);
        } catch (JsonProcessingException e) {
            data.put("Context", "{}");
        }
        
        data.put("otherGameID", 0);
        data.put("result", 1);
        data.put("msg", null);

        // 回傳結果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", 0);
        result.put("status", true);
        result.put("msg", "Success");
        result.put("data", data);

        LOGGER.log("==================================== enter game end ====================================");

        return ResponseEntity.ok(result);
    }

    @org.springframework.web.bind.annotation.PostMapping(
        value = "/api/SLOTHHPanda/Gamble",
        produces = "application/json"
    )
    @ResponseBody
    public ResponseEntity<Map<String, String>> gamble(@org.springframework.web.bind.annotation.RequestBody(required = false) String request) {
        LOGGER.log("==================================== spine start ====================================");
        LOGGER.log("Gamble, request:" + request);
        double gambleValue = 10;
        int gambleLv = 1;
        double lineCountValue = 20;
        if (request != null && !request.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> reqMap = objectMapper.readValue(request, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {});
                Object gambleObj = reqMap.get("gamble");
                if (gambleObj != null) {
                    gambleValue = Double.parseDouble(gambleObj.toString());
                }
                Object gambleLvObj = reqMap.get("lv");
                if (gambleLvObj != null) {
                    gambleLv = Integer.parseInt(gambleLvObj.toString());
                }
                Object lineCountObj = reqMap.get("lineCount");
                if (lineCountObj != null) {
                    lineCountValue = Double.parseDouble(lineCountObj.toString());
                }
            } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
                LOGGER.log("Parse request error (JsonProcessingException): " + e.getMessage());
            }
        }
        // double gamble = (gambleValue * lineCountValue);
        // double gamble = BigDecimalUtil.multiply(gambleValue, lineCountValue);

        List<Object> gambleResult = new ArrayList<>();
        boolean roundEnd;
        do {
            roundEnd = true;
            // 取得前一輪結果
            Map<String, Object> lastResult = null;
            if (!gambleResult.isEmpty()) {
                lastResult = (Map<String, Object>)gambleResult.get(gambleResult.size() - 1);
            }

            // 產生新一輪結果
            Map<String, Object> gambleItem = createSpinResult(gambleResult.size(), lastResult, gambleValue, gambleLv, lineCountValue);
            if (gambleItem != null) {
                Map<String, Object> dt = (Map<String, Object>)gambleItem.get("dt");
                if (dt != null) {
                    Map<String, Object> si = (Map<String, Object>)dt.get("si");
                    if (si != null) {
                        Map<String, Object> rs = (Map<String, Object>)si.get("rs");
                        if (rs != null) {
                            // 如果有消除圖標或炸彈，則繼續下一輪
                            if (rs.get("ewp") != null || rs.get("bf") != null) {
                                roundEnd = false;
                            }
                            LOGGER.log("roundEnd:" + roundEnd
                                        + ", ewp: " + (rs.get("ewp") != null ? rs.get("ewp").toString() : "null")
                                        + ", bf: " + (rs.get("bf") != null ? rs.get("bf").toString() : "null")
                                        + ", fs: " + (si.get("fs") != null ? si.get("fs").toString() : "null"));
                        } else {
                            // 沒有消除圖標或炸彈時，判斷是否進入免費遊戲，如果有免費遊戲且不是最後一輪則繼續下一輪
                            Map<String, Object> fs = (Map<String, Object>)si.get("fs");
                            if (fs != null && (Integer)fs.get("s") > 0) {
                                roundEnd = false;
                                LOGGER.log("roundEnd:" + roundEnd + ", rs is null, fs: " + fs.toString());
                            } else {
                                if (fs == null) {
                                    LOGGER.log("roundEnd:" + roundEnd + ", rs is null && fs is null");
                                } else {
                                    LOGGER.log("roundEnd:" + roundEnd + ", rs is null && free game ended");
                                }
                            }
                        }
                    }
                }
                gambleResult.add(gambleItem);
            }   
        } while (!roundEnd);

        // this.userMoney = BigDecimalUtil.subtract(this.userMoney, gamble);
        // LOGGER.log("userMoney after gamble: " + this.userMoney + ", gamble: " + gamble + ", gambleValue: " + gambleValue + ", lineCountValue: " + lineCountValue);

        
        // 回傳結果
        Map<String, String> result = new LinkedHashMap<>();
        result.put("code", "0");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String gambleResultJson = objectMapper.writeValueAsString(gambleResult);
            result.put("data", gambleResultJson);
        } catch (JsonProcessingException e) {
            result.put("data", "[]");
        }
        result.put("msg", "success");
        result.put("status", "true");

        LOGGER.log("==================================== spine end ====================================");

        return ResponseEntity.ok(result);
    }

    private Map<String, Object> createSpinResult(int index, Map<String, Object> previousResult, double gambleValue, int gambleLv, double lineCountValue) {
        if (index < 0 || (index > 0 && previousResult == null)) {
            LOGGER.log("index:" + index + ", previousResult:" + previousResult);
            return null;
        }

        List<Integer> rl = new LinkedList<>();      // 3x3盤面
        List<Integer> sym = new LinkedList<>();     // 可以乘倍的圖標列表
        
        // 前一輪參數資料
        Map<String, Object> previousDt;             // 前一輪dt
        Map<String, Object> previousSi = null;      // 前一輪si
        Map<String, Object> previousBm = null;      // 前一輪bm
        List<Integer> previousRl;                   // 前一輪盤面
        Map<String, Object> previousRs = null;      // 前一輪rs
        List<Integer> previousEwp = null;           // 前一輪一般消除圖標位置
        Map<String, Object> previousBf = null;      // 前一輪bf
        List<Integer> previousBp = null;            // 前一輪炸彈圖標位置
        List<Integer> previousEp = null;            // 前一輪炸彈消除圖標位置
        Map<String, Object> previousFs = null;      // 前一輪fs

        // 第一輪產生隨機盤面，後續輪盤面由前一輪掉落後產生
        if (index == 0) {
            // 產生隨機盤面，範圍為0-12，0 百搭，1 免費遊戲，2 炸彈，3-12 一般圖標
            for (int i = 0; i < 9; i++) {
                rl.add((int)Math.floor((Math.random() * 10) + 3));
            }

            for (int i = 0; i < 3; i++) {
                // 百搭、免費遊戲、炸彈各有1%機率出現，如果隨機到的位置是其它特殊圖標，則不放
                // 測試暫時提高機率為10%
                if ((int)Math.floor(Math.random() * 100) < 10) {
                    int wildPos = (int)Math.floor(Math.random() * 9); // 隨機產生0~8之間的位置
                    if (rl.get(wildPos) != 0 && rl.get(wildPos) != 1 && rl.get(wildPos) != 2) {
                        rl.set(wildPos, i);
                    }
                }
            }

            // 隨機產生得獎後加倍圖標，限定在3~12 之間
            List<Integer> allSymList = new ArrayList<>(Arrays.asList(3,4,5,6,7,8,9,10,11,12));
            for (int i = 0; i < 5; i++) {
                if (sym.size() >= 5) {
                    break;
                }
                int symIndex = (int)Math.floor(Math.random() * allSymList.size()); // 隨機產生3~12之間的圖標
                sym.add(allSymList.get(symIndex));
                allSymList.remove(symIndex); // 移除已選圖標，避免重複
            }
            // LOGGER.log("First round, rl: " + rl.toString() + ", sym: " + sym.toString());
        } else {
            // 非第一輪，帶入前一輪結果
            previousDt = (Map<String, Object>)previousResult.get("dt");
            if (previousDt == null) {
                LOGGER.log("previousDt:" + previousDt);
                return null;
            }

            previousSi = (Map<String, Object>)previousDt.get("si");
            if (previousSi == null) {
                LOGGER.log("previousSi:" + previousSi);
                return null;
            }

            previousRl = (List<Integer>)previousSi.get("rl");
            if (previousRl == null || previousRl.size() != 9) {
                LOGGER.log("previousRl: " + previousRl + ", size: " + ((previousRl != null) ? previousRl.size() : "null"));
                return null;
            }

            // 帶入前一輪結果的消除圖標位置
            previousRs = (Map<String, Object>)previousSi.get("rs");
            if (previousRs != null) {
                previousEwp = (List<Integer>)previousRs.get("ewp");
                previousBf = (Map<String, Object>)previousRs.get("bf");
            }
            if (previousBf != null) {
                previousBp = (List<Integer>)previousBf.get("bp");
                previousEp = (List<Integer>)previousBf.get("ep");
            }

            previousFs = (Map<String, Object>)previousSi.get("fs");
            if (previousRs == null && previousFs != null) {
                // 前一輪沒有消除圖標及炸彈，但進入免費遊戲，重新產生盤面
                rl = new LinkedList<>();

                // 產生隨機盤面，範圍為3-12，免費遊戲中不會有0 百搭，1 免費遊戲，2 炸彈
                for (int i = 0; i < 9; i++) {
                    rl.add((int)Math.floor((Math.random() * 10) + 3));
                }
            } else {
                // 帶入前一輪結果的盤面
                rl = new LinkedList<>(previousRl);

                // 根據前一輪消除圖標位置，將消除圖標上方的圖標掉落下來，並在最上方補上新的隨機圖標
                for (int i = 0; i < 9; i++) {
                    if ((previousEwp != null && previousEwp.contains(i))
                        || (previousBp != null && previousBp.contains(i))
                        || (previousEp != null && previousEp.contains(i))) {
                        // 位置i有消除圖標，將上方圖標掉落下來
                        if (i % 3 > 0) {
                            // 不是最上方，將上方圖標掉落下來
                            for (int j = i; j > (i - (i % 3)); j--) {
                                rl.set(j, rl.get(j - 1));
                            }
                        }
                        rl.set((i - (i % 3)), -1); // 標記最上方位置需要補上新圖標
                    }
                }

                // 可以補上新圖標的位置，才能換成特殊圖標
                for (int i = 0; i < 9; i++) {
                    if (rl.get(i) == -1) {
                        for (int j = 0; j < 3; j++) {
                            if (previousFs != null) {
                                // 在免費遊戲中不會出現特殊圖標
                                continue;
                            }

                            // 每種特殊圖標同一輪只會出現一次
                            if (rl.contains(j)) {
                                continue;
                            }

                            // 百搭、免費遊戲、炸彈各有1%機率出現
                            // 測試暫時提高機率為10%
                            if ((int)Math.floor(Math.random() * 100) < 10) {
                                rl.set(i, j);
                            }
                        }

                        // 如果還是-1，表示沒有換成特殊圖標，則補上隨機一般圖標
                        if (rl.get(i) == -1) {
                            rl.set(i, (int)Math.floor((Math.random() * 10) + 3));
                        }
                    }
                }
            }

            // 帶入前一輪的可以乘倍圖標列表
            previousBm = (Map<String, Object>)previousSi.get("bm");
            if (previousBm != null) {
                List<Integer> previousSym = (List<Integer>)previousBm.get("sym");
                if (previousSym != null) {
                    sym = new LinkedList<>(previousSym);
                }
            }
        }

        String spinSid = this.getSid();
        String psid = spinSid;
        if (index > 0) {
            String previousPsid = null;
            if (previousSi != null) {
                previousPsid = (String)previousSi.get("psid");
            }
            if (previousPsid == null) {
                LOGGER.log("previousPsid:" + previousPsid);
                return null;
            }
            psid = previousPsid;
        }
        
        double cs = BigDecimalUtil.divide(gambleValue, 100, 2); // 每線押注金額
        double gamble = BigDecimalUtil.multiply(gambleValue, gambleLv);
        gamble = BigDecimalUtil.multiply(gamble, lineCountValue);
        double tb = BigDecimalUtil.divide(gamble, 100, 2);      // 此輪押注金額(僅第一輪有)
        double tbb = tb;
        if (index > 0) {
            tb = 0;
        }

        List<Integer> ws = null;        // 此輪消除圖標
        Map<String, Object> wp = null;  // 此輪消除圖標位置

        Map<Integer, Object> checkWinList = checkWinList(rl);
        if (checkWinList != null && !checkWinList.isEmpty()) {
            ws = new ArrayList<>(checkWinList.keySet());
            wp = new LinkedHashMap<>();
            int i = 1;
            for (Map.Entry<Integer, Object> entry : checkWinList.entrySet()) {
                wp.put(i + "", entry.getValue());
                i++;
            }
        } else if (index == 0 && rl.contains(2)) {
            // 第一輪沒有消除圖標，盤面有炸彈圖標時，將炸彈圖標替換成其他圖標
            rl.set(rl.indexOf(2), (int)Math.floor((Math.random() * 10) + 3));

            // 重新檢查是否有消除圖標
            checkWinList = checkWinList(rl);
            if (checkWinList != null && !checkWinList.isEmpty()) {
                ws = new ArrayList<>(checkWinList.keySet());
                wp = new LinkedHashMap<>();
                int i = 1;
                for (Map.Entry<Integer, Object> entry : checkWinList.entrySet()) {
                    wp.put(i + "", entry.getValue());
                    i++;
                }
            }
        }
        // LOGGER.log("[debug] checkWinList, index: " + index + ", rl: " + rl.toString() + ", ws: " + ((ws != null) ? ws.toString() : "null") + ", wp: " + ((wp != null) ? wp.toString() : "null"));
        
        Map<String, Object> gambleItem = new LinkedHashMap<>();
        Map<String, Object> dt = new LinkedHashMap<>();

        Map<String, Object> bm = new LinkedHashMap<>();
        List<Integer> rbm = null;           // 得獎圖標有中乘倍時的位置，沒有中乘倍時為null
        List<Integer> obmd = new ArrayList<>(Arrays.asList(2, 2, 2, 2, 2)); // 得獎圖標中乘倍前的倍數，預設為全部2倍
        List<Integer> nbmd = null;          // 得獎圖標中乘倍後的倍數，沒有消除圖標時為null
        Map<String, Object> lm = null;      // 乘倍圖標中獎位置，得獎圖標沒有中乘倍時為null
        Map<String, Object> bmw = null;     // 乘倍圖標中獎時乘倍前得獎金額，得獎圖標沒有中乘倍時為null
        Map<String, Object> lw = null;      // 乘倍後每線消除得獎金額

        boolean enterFree = false; // 是否進入免費遊戲
        if (previousFs != null && ((Integer)previousFs.get("s")).equals((Integer)previousFs.get("ts"))) {
            enterFree = true;
        }

        // 如果不是第一輪，則帶入前一輪的乘倍前倍數
        if (index > 0) {
            if (enterFree) {
                // 前一輪進入免費遊戲，則乘倍前倍數預設值改為5,5,5,5,5
                obmd = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5));
            } else {
                List<Integer> previousNbmd = null;
                if (previousBm != null) {
                    previousNbmd = (List<Integer>)previousBm.get("nbmd");
                }
                if (previousNbmd != null && previousNbmd.size() == 5) {
                    obmd = new ArrayList<>(previousNbmd);
                } else {
                    if (previousBm != null) {
                        List<Integer> previousObmd = (List<Integer>)previousBm.get("obmd");
                        obmd = (new ArrayList<>(previousObmd));
                    }
                }
            }
        }

        if (previousFs != null && previousRs == null) {
            // 免費遊戲中如果前一輪未得獎，則重新隨機產生得獎後加倍圖標，限定在3~12 之間
            sym.clear();
            List<Integer> allSymList = new ArrayList<>(Arrays.asList(3,4,5,6,7,8,9,10,11,12));
            for (int i = 0; i < 5; i++) {
                if (sym.size() >= 5) {
                    break;
                }
                int symIndex = (int)Math.floor(Math.random() * allSymList.size()); // 隨機產生3~12之間的圖標
                sym.add(allSymList.get(symIndex));
                allSymList.remove(symIndex); // 移除已選圖標，避免重複
            }
        }

        // 找出得獎圖標中有沒有中乘倍
        if (ws != null && !ws.isEmpty()) {
            for (int i = 0; i < ws.size(); i++) {
                // 有中乘倍
                for (int j = 0; j < sym.size(); j++) {
                    if (ws.get(i).equals(sym.get(j))) {
                        if (rbm == null) {
                            rbm = new ArrayList<>();
                        }
                        rbm.add(j);
                    }
                }
            }
        }

        if (rbm != null && !rbm.isEmpty()) {
            // 有中乘倍，找出乘倍圖標中獎位置及乘倍前得獎金額
            lm = new LinkedHashMap<>();
            for (int i = 0; i < rbm.size(); i++) {
                lm.put((i + 1) + "", (double)rbm.get(i));
            }
            
            bmw = new LinkedHashMap<>();
            for (int i = 0; i < rbm.size(); i++) {
                int symId = sym.get(rbm.get(i));
                int mult = this.multiple[symId];
                int wpIndex = (ws != null) ? ws.indexOf(symId) + 1 : -1;
                if (wpIndex > 0 && wp != null && wp.get(wpIndex + "") != null) {
                    // 計算有中乘倍的圖標乘倍前得獎金額，加入bmw
                    double winAmount = BigDecimalUtil.multiply(BigDecimalUtil.multiply(mult, cs), ((List<?>)wp.get(wpIndex + "")).size());
                    bmw.put(wpIndex + "", winAmount);

                    // 計算乘倍後得獎金額，加入lw
                    int obmdMulti = obmd.get(rbm.get(i));
                    if (lw == null) {
                        lw = new LinkedHashMap<>();
                    }
                    lw.put(wpIndex + "", BigDecimalUtil.multiply(winAmount, obmdMulti));
                }
            }
        }

        // 如果不是所有得獎圖標都有中乘倍，則另外在lw中加入沒有中乘倍的得獎金額
        if ((ws != null && lw != null && ws.size() != lw.size()) || (lw == null && ws != null && !ws.isEmpty())) {
            for (int i = 0; i < ws.size(); i++) {
                int wpIndex = i + 1;
                if (lw == null) {
                    lw = new LinkedHashMap<>();
                }
                if (wp != null && lw.get(wpIndex + "") == null && wp.get(wpIndex + "") != null) {
                    int symId = ws.get(i);
                    int mult = this.multiple[symId];
                    double winAmount = BigDecimalUtil.multiply(BigDecimalUtil.multiply(mult, cs), ((List<?>)wp.get(wpIndex + "")).size());
                    lw.put(wpIndex + "", winAmount);
                }
            }
        }

        // 將lw依key排序
        if (lw != null && !lw.isEmpty()) {
            // Sort lw by key and create a new LinkedHashMap to preserve order
            Map<String, Object> sortedLw = new LinkedHashMap<>();
            lw.entrySet().stream()
                .sorted(Map.Entry.comparingByKey((a, b) -> Integer.compare(Integer.parseInt(a), Integer.parseInt(b))))
                .forEachOrdered(entry -> sortedLw.put(entry.getKey(), entry.getValue()));
            lw = sortedLw;
        }

        // 如果得獎圖標有中倍數，則決定倍數消除後的新增倍數
        if (ws != null && !ws.isEmpty()) {
            nbmd = new ArrayList<>();
            // 先存入沒有中乘倍的倍數
            for (int i = 0; i < obmd.size(); i++) {
                if (nbmd.size() >= 5) {
                    break;
                }
                if (rbm != null && rbm.contains(i)) {
                    continue;
                }
                nbmd.add(obmd.get(i));
            }
            // 再重新隨機產生倍數補足到5個
            for (int i = nbmd.size(); i < 5; i++) {
                if (previousFs != null) {
                    // 免費遊戲中，倍數限定為5~10之間
                    nbmd.add((int)Math.floor((Math.random() * 6) + 5));
                } else {
                    // 正常遊戲中，倍數限定為2~6之間
                    nbmd.add((int)Math.floor((Math.random() * 5) + 2));
                }
            }
        }
        
        bm.put("rbm", rbm);         // 得獎圖標有中乘倍時的位置
        bm.put("sym", sym);         // 可以乘倍的圖標列表
        bm.put("obmd", obmd);       // 得到乘倍前的倍數
        bm.put("nbmd", nbmd);       // 得到乘倍後的倍數
        bm.put("lm", lm);           // 乘倍圖標中獎位置
        bm.put("bmw", bmw);         // 乘倍圖標中獎時乘倍前得獎金額
        
        // Map<String, Object> bf = new LinkedHashMap<>();
        // bf.put("bp", new ArrayList<>());                                             // 此輪炸彈圖標位置
        // bf.put("ep", new ArrayList<>());                                             // 此輪炸彈消除圖標(炸彈本身及特殊圖標不消除)
        Map<String, Object> rs = null;
        if (wp != null || rl.contains(2)) {
            rs = new LinkedHashMap<>();
            List<Integer> ewp = null;               // 此輪一般消除圖標，有炸彈時為null
            Map<String,Object> bf = null;           // 此輪炸彈圖標位置，沒有炸彈時為null
            // 有消除圖標時，就不處理炸彈圖標邏輯
            if (wp != null) {
                ewp = new ArrayList<>();
                for (int i = 0; i < wp.size(); i++) {
                    List<Integer> posList = (List<Integer>)wp.get((i + 1) + "");
                    if (posList != null && !posList.isEmpty()) {
                        for (int j = 0; j < posList.size(); j++) {
                            if (!ewp.contains(posList.get(j))) {
                                ewp.add(posList.get(j));
                            }
                        }
                    }
                }
                
                // 排序ewp值，方便後續處理
                Collections.sort(ewp);
            } else {
                // 盤面有炸彈圖標時的處理邏輯
                bf = new LinkedHashMap<>();
                List<Integer> bp = new ArrayList<>();       // 此輪炸彈圖標位置
                List<Integer> ep = new ArrayList<>();       // 此輪炸彈消除圖標位置(炸彈本身及特殊圖標不消除)
                for (int i = 0; i < rl.size(); i++) {
                    if (rl.get(i) == 2) {
                        bp.add(i);
                        for (int j = 0; j < linkList[i].length; j++) {
                            if (rl.get(linkList[i][j]) != 0 && rl.get(linkList[i][j]) != 1 && rl.get(linkList[i][j]) != 2) {
                                // 消除圖標(非特殊圖標)
                                if (!ep.contains(linkList[i][j])) {
                                    ep.add(linkList[i][j]);
                                }
                            }
                        }
                    }
                }
                bf.put("bp", bp);
                bf.put("ep", ep);
            }
            rs.put("ewp", ewp);
            rs.put("bf", bf);
        }
        
        Map<String, Object> gaw = null;                             // 消除的單個icon得獎金額，乘倍後獎金/消除個數
        if (lw != null && !lw.isEmpty()) {
            gaw = new LinkedHashMap<>();
            for (Map.Entry<String, Object> entry : lw.entrySet()) {
                String key = entry.getKey();
                double value = (double)entry.getValue();
                if (wp != null && wp.get(key) != null) {
                    int count = ((List<?>)wp.get(key)).size();
                    if (count > 0) {
                        gaw.put(key, (BigDecimalUtil.divide(value, count, 8)));
                    }
                }
            }
        }

        List<List<Integer>> rns = null;                             // 這一輪的掉落圖標
        if (index > 0 && (previousEwp != null || previousBp != null || previousEp != null)) {
            rns = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                List<Integer> col = new ArrayList<>();
                rns.add(col);
            }
            for (int i = 0; i < 3; i++) {
                int addCount = 0;
                for (int j = 0; j < 3; j++) {
                    int pos = (i * 3) + j;
                    if ((previousEwp != null && previousEwp.contains(pos))
                        || (previousBp != null && previousBp.contains(pos))
                        || (previousEp != null && previousEp.contains(pos))) {
                        addCount++;
                    }
                }
                if (addCount > 0) {
                    List<Integer> col = rns.get(i);
                    for (int k = 0; k < addCount; k++) {
                        col.add(rl.get((i * 3) + k));
                    }
                }
            }
        }

        Map<String, Object> fstc = null;                           // 未消除時為0，連消時4為連消數量，有免費回合時會出現21和22
        if (index > 0) {
            fstc = new LinkedHashMap<>();
            fstc.put("4", index);
        }
                 
        double tw = 0;
        if (lw != null && !lw.isEmpty()) {
            for (Map.Entry<String, Object> entry : lw.entrySet()) {
                double value = (double)entry.getValue();
                tw = BigDecimalUtil.add(tw, value);
            }
        }

        double np = BigDecimalUtil.subtract(tw, tb);                    // 此輪淨利潤(得獎-押注)，可能為負數
        double blb = BigDecimalUtil.divide(this.userMoney, 100, 2);   // 押注前餘額
        double blab = BigDecimalUtil.subtract(blb, tb);                 // 押注後餘額
        double bl = BigDecimalUtil.add(blb, np);                        // 得獎後餘額
        
        double ctw = tw;    // 此回合得獎金額
        double ptw = 0.0;   // 前一輪時總得獎金額
        double aw = tw;     // 累計得獎金額
        if (index > 0) {
            double previousAw = 0;
            if (previousSi != null) {
                Object previousAwObj = previousSi.get("aw");
                if (previousAwObj != null) {
                    previousAw = Double.parseDouble(previousAwObj.toString());
                }
            }
            ptw = previousAw;
            aw = BigDecimalUtil.add(previousAw, tw);
        }
        
        int cwc = (index + 1); // 已連消回合數
        if (index > 0 && (ws == null || ws.isEmpty())) {
            cwc = 0;
        }
        int pcwc = 0;   // 第一回合有消除為1，沒有消除為0，後續回合皆為0
        if (index == 0 && ws != null && !ws.isEmpty()) {
            pcwc = 1;
        }

        Map<String, Object> fs = null;
        if (previousFs == null) {
            if (rs == null && rl.contains(1)) {
                // 前一輪沒有免費遊戲，此輪沒有消除及炸彈，盤面有免費遊戲圖標，表示進入免費遊戲
                fs = new LinkedHashMap<>();
                int fsCount = 0;
                for (int i = 0; i < rl.size(); i++) {
                    if (rl.get(i) == 1) {
                        fsCount++;
                    }
                }
                fsCount = fsCount * 3;          // 免費遊戲圖標數量乘以3倍
                fs.put("s", fsCount);           // 免費遊戲未執行回合量
                fs.put("ts", fsCount);          // 免費遊戲總回合數
                fs.put("as", null);             // 不明
                fs.put("aw", 0.0);              // 免費遊戲總得獎金額
                
                nbmd = new ArrayList<>(Arrays.asList(5, 5, 5, 5, 5));
                bm.replace("nbmd", nbmd);
            }
        } else {
            Map<String, Object> tempFs = new LinkedHashMap<>(previousFs);

            if (fstc == null) {
                fstc = new LinkedHashMap<>();
            }

            if (previousRs == null) {
                // 免費遊戲中，前輪未得獎也沒有炸彈

                // 取得前輪免費遊戲剩餘回合數
                int previousS = 0;
                Object previousSObj = previousFs.get("s");
                if (previousSObj != null) {
                    previousS = Integer.parseInt(previousSObj.toString());
                }
                
                tempFs.replace("s", (previousS - 1));     // 免費遊戲剩餘回合數-1

                // fstc的21加1
                Map<String, Object> previousFstc = null;
                if (previousSi != null) {
                    previousFstc = (Map<String, Object>)previousSi.get("fstc");
                }
                if (previousFstc != null) {
                    Object previousFstc21Obj = previousFstc.get("21");
                    int previousFstc21 = 0;
                    if (previousFstc21Obj != null) {
                        previousFstc21 = Integer.parseInt(previousFstc21Obj.toString());
                    }
                    if (fstc.get("21") != null) {
                        fstc.replace("21", (previousFstc21 + 1));
                    } else {
                        fstc.put("21", (previousFstc21 + 1));
                    }

                    // 帶入前輪fstc22值
                    Object previousFstc22Obj = previousFstc.get("22");
                    if (previousFstc22Obj != null) {
                        int previousFstc22 = Integer.parseInt(previousFstc22Obj.toString());
                        fstc.put("22", previousFstc22); 
                    }
                }
            } else {
                // 免費遊戲中，前輪得獎或有炸彈
                
                Map<String, Object> previousFstc = null;
                if (previousSi != null) {
                    previousFstc = (Map<String, Object>)previousSi.get("fstc");
                }
                if (previousFstc != null) {
                    // fstc的22加1
                    Object previousFstc22Obj = previousFstc.get("22");
                    int previousFstc22 = 0;
                    if (previousFstc22Obj != null) {
                        previousFstc22 = Integer.parseInt(previousFstc22Obj.toString());
                    }
                    if (fstc.get("22") != null) {
                        fstc.replace("22", (previousFstc22 + 1));
                    } else {
                        fstc.put("22", (previousFstc22 + 1));
                    }

                    // 帶入前輪fstc21值
                    Object previousFstc21Obj = previousFstc.get("21");
                    if (previousFstc21Obj != null) {
                        int previousFstc21 = Integer.parseInt(previousFstc21Obj.toString());
                        fstc.put("21", previousFstc21);
                    }
                }
            }

            if (rs != null && rs.get("ewp") != null) {
                // 免費遊戲中有消除圖標，則累計得獎金額
                double previousAw = 0;
                Object previousAwObj = previousFs.get("aw");
                if (previousAwObj != null) {
                    previousAw = Double.parseDouble(previousAwObj.toString());
                }
                tempFs.replace("aw", BigDecimalUtil.add(previousAw, ctw));
            }

            fs = tempFs;
        }
        LOGGER.log("fs: " + ((fs != null) ? fs.toString() : "null"));


        int st = 1;      // 前一回合的狀態，第一回合為1
        if (index > 0) {
            if (previousSi != null) {
                Object previousNstObj = previousSi.get("nst");
                st = previousNstObj != null ? Integer.parseInt(previousNstObj.toString()) : 1;
            }
        }

        int nst;    // 本輪狀態，未得獎為1，得獎或有炸彈為4，免費遊戲未得獎或第一回合為21，得獎或有炸彈為22
        if ((lw != null && !lw.isEmpty()) || (rs != null && rs.get("bf") != null)) {
            if (fs != null) {
                nst = 22;   // 免費遊戲得獎或有炸彈
            } else {
                nst = 4;    // 一般遊戲得獎或有炸彈
            }
        } else {
            if (fs != null) {
                int s = fs.get("s") != null ? Integer.parseInt(fs.get("s").toString()) : 0;
                if (s == 0) {
                    nst = 1;    // 免費遊戲未得獎且免費遊戲結束，回到一般遊戲
                } else {
                    nst = 21;   // 免費遊戲未得獎且還有回合
                }
            } else {
                nst = 1;    // 一般遊戲未得獎
            }
        }

        this.userMoney = BigDecimalUtil.multiply(bl, 100);
        LOGGER.log("userMoney after gamble, start money:" + blb + ", gamble: " + tb
                + ", tw:" + tw + ", ptw: " + ptw + ", aw: " + aw + ", profit: " + np + ", end money: " + bl);
        LOGGER.log("round:" + (index + 1)
                            + ", rl: " + rl.toString() + ", ws: " + ws
                            + ", lw: " + (lw != null ? lw.toString() : "null")
                            + ", gaw: " + (gaw != null ? gaw.toString() : "null")
                            + ", wp: " + (wp != null ? wp.toString() : "null")
                            + ", rs: " + (rs != null ? rs.toString() : "null")
                            + ", bm: " + bm.toString()
                            + ", rns: " + (rns != null ? rns.toString() : "null")
                            + ", fstc: " + (fstc != null ? fstc.toString() : "null"));
        // ws = null; // 先將消除圖標清空，避免影響後續邏輯判斷
        // wp = null; // 先將消除圖標位置清空，避免影響後續邏輯判斷
        // lw = null; // 先將乘倍後每線消除得獎金額清空，避免影響後續邏輯判斷
        // gaw = null; // 先將消除的單個icon得獎金額清空，避免影響後續邏輯判斷
        // bm.replace("rbm", null); // 先將得獎圖標有中乘倍時的位置清空，避免影響後續邏輯判斷
        // bm.replace("nbmd", null); // 先將乘倍後倍數清空，避免影響後續邏輯判斷
        // bm.replace("lm", null); // 先將乘倍圖標中獎位置清空，避免影響後續邏輯判斷
        // bm.replace("bmw", null); // 先將得獎圖標中獎時乘倍前得獎金額清空，避免影響後續邏輯判斷
        // rs = null; // 先將消除及炸彈相關資料清空，避免影響後續邏輯判斷
        // rns = null; // 先將這一輪的掉落圖標清空，避免影響後續邏輯判斷

        Map<String, Object> si = new LinkedHashMap<>();
        si.put("wp", wp);                                       // 此輪消除圖標位置
        si.put("lw", lw);                                       // 乘倍後每線消除得獎金額
        si.put("ws", ws);                                       // 此輪消除圖標id
        si.put("gaw", gaw);                                     // 消除的單個icon得獎金額，乘倍後獎金/消除個數
        si.put("bm", bm);                                       // 得獎倍數資料
        si.put("rns", rns);                                     // 這一輪的掉落圖標
        si.put("fs", fs);                                       // 免費遊戲相關參數
        si.put("rs", rs);                                       // 一般消除及炸彈相關資料，沒有消除也沒有炸彈時為null
        si.put("ptw", ptw);                                     // 前一輪時總得獎金額
        si.put("gwt", -1);
        si.put("pmt", null);
        si.put("ab", null);
        si.put("ml", gambleLv);                                 // 押注倍數
        si.put("cs", cs);                                       // 每線押注金額
        si.put("rl", rl);                                       // 消除前盤面
        si.put("ctw", ctw);                                     // 此回合得獎金額
        si.put("cwc", cwc);                                     // 已連消回合數
        si.put("fstc", fstc);                                   // 連消時4為連消數量，有免費回合時會出現21和22
        si.put("pcwc", pcwc);                                   // 第一回合有消除為1，沒有消除為0，後續回合皆為0
        si.put("rwsp", null);                                   // 不明，目前看到結果皆為null
        si.put("hashr", null);
        si.put("fb", null);
        si.put("sid", spinSid);                                 // 此輪局號
        si.put("psid", psid);                                   // 連消及免費遊戲時的總局號(及第一輪局號)
        si.put("st", st);                                       // 前一回合的nst，第一回合為1
        si.put("nst", nst);                                     // 得獎後為4，未得獎為1，免費遊戲得獎為21，未得獎為22，免費遊戲第一回合為22
        si.put("pf", 1);
        si.put("aw", aw);
        si.put("wid", 0);
        si.put("wt", "C");
        si.put("wk", "0_C");
        si.put("wbn", null);
        si.put("wfg", null);
        si.put("blb", blb);                                     // 押注前玩家餘額
        si.put("blab", blab);                                   // 押注後玩家餘額
        si.put("bl", bl);                                       // 得獎後玩家餘額
        si.put("tb", tb);                                       // 此輪押注金額(僅第一輪有)
        si.put("tbb", tbb);                                     // 總押注金額
        si.put("tw", tw);                                       // 總得獎金額
        si.put("np", np);                                       // 扣除押注金額後的贏分
        si.put("ocr", null);
        si.put("mr", null);
        si.put("ge", Arrays.asList(1, 11));
        si.put("sc", 0);
        dt.put("si", si);
        gambleItem.put("dt", dt);
        gambleItem.put("err", null);

        return gambleItem;
    }

    private String getSid() {
        long base = new Date().getTime() * 1000000;
        String sidStr = String.valueOf(this.sid);
        while (sidStr.length() < 7) {
            sidStr = "0" + sidStr;
        }
        this.sid++;
        return base + sidStr;
    }

    private final int[][] linkList = new int[][] {
        {1,3,4}, {0,2,3,4,5}, {1,4,5},
        {0,1,4,6,7}, {0,1,2,3,5,6,7,8}, {1,2,4,7,8},
        {3,4,7}, {3,4,5,6,8}, {4,5,7}  // 包含斜線相連
    };

    private Map<Integer, Object> checkWinList(List<Integer> rl) {
        Map<Integer, Object> winList = new LinkedHashMap<>();

        // 使用類別層級的私有遞迴方法
        for (int i = 0; i < rl.size(); i++) {
            int checkSym = rl.get(i);
            if (checkSym < 3) {
                continue;
            }
            List<Integer> linkPos = new ArrayList<>();
            linkPos.add(i);
            checkLink(rl, i, linkPos); // 呼叫私有方法

            // 把linkPos排序
            Collections.sort(linkPos);

            // 把 linkPos 加入 winList
            if (linkPos.size() >= 3) {
                winList.put(checkSym, linkPos);
            }
        }

        return winList;
    }

    private void checkLink(List<Integer> rl, int pos, List<Integer> linkPos) {
        int[] neighbors = linkList[pos];
        for (int nb : neighbors) {
            if ((rl.get(nb).equals(rl.get(pos)) || rl.get(nb).equals(0)) && !linkPos.contains(nb)) {
                linkPos.add(nb);
                if (rl.get(nb).equals(0)) {
                    // 如果是百搭，繼續往下找
                    checkWildLink(rl, nb, rl.get(pos), linkPos);
                } else {
                    checkLink(rl, nb, linkPos);
                }
            }
        }
    }

    private void checkWildLink(List<Integer> rl, int pos, int sym, List<Integer> linkPos) {
        int[] neighbors = linkList[pos];
        for (int nb : neighbors) {
            if ((rl.get(nb).equals(sym) || rl.get(nb).equals(0)) && !linkPos.contains(nb)) {
                linkPos.add(nb);
                if (rl.get(nb).equals(0)) {
                    // 如果是百搭，繼續往下找
                    checkWildLink(rl, nb, sym, linkPos);
                } else {
                    checkLink(rl, nb, linkPos);
                }
            }
        }
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
