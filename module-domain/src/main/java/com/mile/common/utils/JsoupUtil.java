package com.mile.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupUtil {

    public static String toPlainText(final String html) {

        Document document = Jsoup.parse(html);

        Elements pElements = document.select("p");
        for (Element p : pElements) {
            p.after(" ");
        }

        return document.text();
    }
}
