package com.mile.utils.logging;


import com.mile.filter.CustomServletWrappingFilter;
import com.mile.filter.wrapper.CachedBodyRequestWrapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRequestUtil {

    public static String getRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();
        request.getHeaderNames().asIterator()
                .forEachRemaining(name -> {
                    if (!name.equals("user-agent")) {
                        headerMap.put(name, request.getHeader(name));
                    }
                });
        return headerMap;
    }

    public static Map<String, String> getParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(name -> paramMap.put(name, request.getParameter(name)));

        return paramMap;
    }

    public static String getBody(HttpServletRequest httpReq) {
        CachedBodyRequestWrapper nativeRequest = WebUtils.getNativeRequest(httpReq, CachedBodyRequestWrapper.class);

        if (nativeRequest != null) {
            return nativeRequest.getBody();
        }
        return "requestBody 정보 없음";
    }

    public static String getUserIP(HttpServletRequest httpReq) {
        String ip = httpReq.getHeader("X-Forwarded-For");
        if (ip == null)
            ip = httpReq.getRemoteAddr();

        return ip;
    }

    public static Map<String, String> getUserLocation(HttpServletRequest request) {
        Map<String, String> locationMap = new HashMap<>();
        String userIP = getUserIP(request);

        String locationFindAPIUrl = "https://ipapi.co/" + userIP + "/json/";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(locationFindAPIUrl, String.class);

        String[] locationEntity = Objects.requireNonNull(response).split(",");

        for (String entity : locationEntity) {
            String[] element = entity.split(":");
            if (element.length == 2) {
                locationMap.put(
                        element[0].replace(" ", "").replace("\n", "").replace("{", "").replace("}", "").replace("\"", ""),
                        element[1].replace(" ", "").replace("\"", ""));
            } else {
                locationMap.put("languages", entity);
            }
        }

        return locationMap;
    }

    public static Cookie[] getUserCookies(HttpServletRequest httpReq) {
        return httpReq.getCookies();
    }
}
