package com.cronos.onlinereview.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class Prinner {
    @Autowired
    @Qualifier("tcsJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public void printValue() {
        try {
            List<Map<String, Object>> result = executeSql("select contest_type_id, contest_type_desc from contest_type_lu");
            for (Map<String, Object> map : result) {
                System.out.println("result item: " + map.toString());
                for (String key : map.keySet()) {
                    System.out.println("key is: " + key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Map<String, Object>> executeSql(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
