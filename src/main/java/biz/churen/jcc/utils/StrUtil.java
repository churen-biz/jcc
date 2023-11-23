package biz.churen.jcc.utils;

/**
 * @author lihai03
 * Created on 2023-11-23
 */
public class StrUtil {

    public static String firstNumber(String s, int start) {
        int end = start;
        for (int i = start; i < s.length(); i++) {
            char ci = s.charAt(i);
            if (ci >= '0' && ci <= '9') {
                end++;
            } else {
                break;
            }
        }
        return s.substring(start, end);
    }

    public static boolean isSpace(char ci) {
        return (' ' == ci);
    }

    public static boolean isDigit(char ci) {
        return (ci >= '0' && ci <= '9');
    }
}
