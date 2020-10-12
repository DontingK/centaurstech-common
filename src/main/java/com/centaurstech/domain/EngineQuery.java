package com.centaurstech.domain;

import com.centaurstech.utils.time.TimeCalculator;

import java.util.Map;

/**
 * Engine query form super class
 * Created by Feliciano on 7/5/2017.
 * @author Feliciano.Long
 */
public abstract class EngineQuery {

    public static String CHAT_KEY_SPLITTER = "@@@";
    public static String EXTRA_DATA_SPLITTER = "###";

    /**
     * Session chat key
     */
    String chatKey;

    /**
     * Appended extra data in chat key
     */
    String extra;

    /**
     * Query start time
     */
    long beginTime;

    /**
     * Query process duration
     */
    long processTime;

    /**
     * Engine query request params
     */
    Map<String,String> requestParams;

    public EngineQuery() {
        beginTime = TimeCalculator.nowInMillis();
    }

    public EngineQuery(String chat_key) {
        this();
        chatKey = chat_key;
        if (chat_key.contains(CHAT_KEY_SPLITTER)) {
            chatKey = chat_key.substring(0, chat_key.indexOf(CHAT_KEY_SPLITTER));
            if (chat_key.contains(EXTRA_DATA_SPLITTER)) {
                String sub = chat_key.substring(chat_key.indexOf(CHAT_KEY_SPLITTER) + 3);
                if (sub.contains(EXTRA_DATA_SPLITTER)) {
                    extra = sub.substring(0, sub.indexOf(EXTRA_DATA_SPLITTER));
                }
            }
        }
    }

    public EngineQuery(Map<String,String> requestParams) {
        this(requestParams.get("chat_key"));
    }

    public EngineQuery(Map<String,String> requestParams, boolean keepRequestParams) {
        this(requestParams.get("chat_key"));
        if (keepRequestParams) {
            this.requestParams = requestParams;
        }
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public long getQueryTime() {
        return processTime = TimeCalculator.nowInMillis() - beginTime;
    }

    public String getQueryTimeString() {
        return "Query " + chatKey + " takes " + getQueryTime() + "ms";
    }

    /**
     * Check if engine form has a valuable key
     * @param requestParams
     * @param key
     * @return
     */
    public static Boolean hasValue(Map<String,String> requestParams, String key) {
        String result = requestParams.getOrDefault(key, null);
        if (result != null && !result.isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean hasValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return hasValue(requestParams, key);
    }

    /**
     * Get a value using a series of keys from engine form
     * @param requestParams
     * @param keys
     * @return
     */
    public static String getStringValue(Map<String, String> requestParams, String... keys) {
        for (String key : keys) {
            if (hasValue(requestParams, key)) {
                return requestParams.get(key);
            }
        }
        return "";
    }

    public String getStringValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return getStringValue(requestParams, key);
    }

    /**
     * Get a integer value using a key from engine form
     * @param requestParams
     * @param key
     * @return
     */
    public static Integer getIntegerValue(Map<String, String> requestParams, String key) {
        return getIntegerValue(requestParams, key, -1);
    }

    public Integer getIntegerValue(String key) {
        if (requestParams == null) {
            throw new NullPointerException("Request params not initialized!");
        }
        return getIntegerValue(requestParams, key);
    }

    public static Integer getIntegerValue(Map<String, String> requestParams, String key, Integer defaultValue) {
        if (!requestParams.containsKey(key)) {
            return defaultValue;
        }
        String stringValue = requestParams.get(key);
        try {
            return Integer.valueOf(stringValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public Integer getIntegerValue(String key, Integer defaultValue) {
        if (requestParams == null) {
            throw new NullPointerException("Request parameters not initialized!");
        }
        return getIntegerValue(requestParams, key, defaultValue);
    }

}
