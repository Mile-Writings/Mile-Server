package util;

import com.mile.utils.JsoupUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JsoupUtilTest {
    JsoupUtil jsoupUtil;

    @BeforeEach
    void setUp() {
        jsoupUtil = new JsoupUtil();
    }

    @Test
    void testParse() {
        String request = "<p>My<p/><p>name<p/>is";

        String expected = "My name is";
        String result = jsoupUtil.toPlainText(request);

        assertThat(result).isEqualTo(expected);
    }

}
