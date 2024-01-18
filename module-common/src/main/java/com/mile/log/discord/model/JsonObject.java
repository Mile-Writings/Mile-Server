package com.mile.log.discord.model;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class JsonObject {

    private final HashMap<String, Object> discordMessageMap = new HashMap<>();

    public void put(String key, Object value) {
        if (value != null) {
            discordMessageMap.put(key, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        Set<Entry<String, Object>> entrySet = discordMessageMap.entrySet();
        builder.append("{");

        int iter = 0;
        for (Map.Entry<String, Object> entry : entrySet) {
            Object val = entry.getValue();
            builder.append(quote(entry.getKey())).append(":");

            if (val instanceof String) {
                builder.append(quote(String.valueOf(val)));
            } else if (val instanceof Integer) {
                builder.append(Integer.valueOf(String.valueOf(val)));
            } else if (val instanceof Boolean) {
                builder.append(val);
            } else if (val instanceof JsonObject) {
                builder.append(val);
            } else if (val.getClass().isArray()) {
                builder.append("[");
                int len = Array.getLength(val);
                for (int j = 0; j < len; j++) {
                    builder.append(Array.get(val, j).toString()).append(j != len - 1 ? "," : "");
                }
                builder.append("]");
            }

            builder.append(++iter == entrySet.size() ? "}" : ",");
        }

        return builder.toString();
    }

    private String quote(String string) {
        return "\"" + string + "\"";
    }
}