package mypackage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HttpUtil {

    public static String WALLET_HEADER = "WALLET";
    public static String SIGNATURE_HEADER = "SIGNATURE";
    public static String AMOUNT_HEADER = "AMOUNT";
    public static String TOKEN_HEADER = "TOKEN";
    public static String BET_AMOUNT_HEADER = "BET_AMOUNT";
    public static String SELECTED_BET_HEADER = "SELECTED_BET";

    public static String ONLYONE_COOKIE = "onlyonecookie";

    public static void postResponse(HttpServletResponse resp, String data) throws IOException {
        PrintWriter writer = resp.getWriter();
        writer.print("{\"data\": \"" + data + "\"}");
    }

    public static String getCookie(HttpServletRequest req, String cookieValueToMatch) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        String res = null;
        for (Cookie cookie : cookies) {
            if (cookie == null) {
                continue;
            }
            if (ONLYONE_COOKIE.equals(cookie.getName())) {
                if (cookieValueToMatch != null && cookieValueToMatch.equals(cookie.getValue())) {
                    return cookie.getValue();
                }
                res = cookie.getValue();
            }
        }
        return res;
    }
}
