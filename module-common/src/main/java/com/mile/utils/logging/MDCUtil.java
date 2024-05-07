package com.mile.utils.logging;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;
import org.slf4j.spi.MDCAdapter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MDCUtil {

    public static final String REQUEST_URI_MDC = "이용자 요청 URI 정보";
    public static final String USER_IP_MDC = "이용자 IP 정보";
    public static final String USER_REQUEST_COOKIES = "이용자 Cookie 정보";
    public static final String HEADER_MAP_MDC = "HTTP 헤더 정보";
    public static final String PARAMETER_MAP_MDC = "Parameter 정보";
    public static final String BODY_MDC = "HTTP Body 정보";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final MDCAdapter mdc = MDC.getMDCAdapter();

    public static void setJsonValue(String key, Object value) throws JsonProcessingException {
        try {
            if (value != null) {
                String json = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(value);
                mdc.put(key, json);
            } else {
                mdc.put(key, "내용 없음");
            }
        } catch (JsonProcessingException ex) {
            throw ex;
        }
    }

}
