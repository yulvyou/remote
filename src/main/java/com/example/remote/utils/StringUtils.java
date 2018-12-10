package com.example.remote.utils;

/**
 *
 */

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {

    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern
            .compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$");

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");

    private static final Pattern YEAR_PATTERN = Pattern.compile("\\d{4}-\\d{4}学年|\\d{4}年");

    private static final Pattern HTML_TAG = Pattern.compile("<([^>]*)>");

    private static final Pattern VALIDATE_PATTERN = Pattern.compile("[a-zA-Z0-9\u4e00-\u9fa5]+");

    private static final Pattern LETTER_PATTERN = Pattern.compile("[a-zA-Z]");

    public static boolean isEmpty(String res) {
        return res == null || res.trim().isEmpty();
    }

    public static boolean isNotEmpty(String res) {
        return !isEmpty(res);
    }

    public static String escape(String res, int count, String append) {
        if (!isEmpty(res)) {
            String tmp = res.trim();
            if (tmp.length() > count && count > 0) {
                tmp = tmp.substring(0, count);
                tmp = tmp.trim();
                if (!isEmpty(append)) {
                    tmp += append;
                }
            }
            return tmp;
        }
        return res;
    }

    public static void main(String[] args) {
        System.out.println(isValidateName("广西壮族族123", true));
        System.out.println(isValidateName("广西壮族123", true));
        System.out.println(isValidateName("广西壮族A", true));
        System.out.println(isValidateName("广西壮族ABC", true));
        System.out.println(isValidateName("123", true));
        System.out.println(isValidateName("a", true));
        System.out.println(isValidateName("QWERTYUIOPASDFGHJKLZX", true));


        System.out.println(containsLetterIndex(0,"A,B,C"));

    }

    public static String filter(String src) {
        if (!isEmpty(src)) {
            return src.replaceAll("contenteditable=\\\"true\\\"", "").replaceAll("contenteditable='true'", "")
                    .replaceAll("http://img.jyeoo.net", "/redirect");
        }
        return src;
    }

    public static String filterHtml(String html) {
        if (!isEmpty(html)) {
            Matcher matcher = HTML_TAG.matcher(html);
            boolean isMatch = matcher.find();
            StringBuffer sb = new StringBuffer();
            while (isMatch) {
                matcher.appendReplacement(sb, "");
                isMatch = matcher.find();
            }
            matcher.appendTail(sb);
            return sb.toString().replace("&nbsp;", "").trim();
        }
        return html;
    }

    /**
     * 编号
     *
     * @param code
     * @return
     */
    public static String getLetterCode(String code) {
        try {
            if (code.length() == 1) {
                int result = Integer.valueOf(code);
                return (char) (result + 'A') + "";
            } else {
                String res = "";
                for (int i = 0; i < code.length(); i++) {
                    String charAt = code.charAt(i) + "";
                    int result = Integer.valueOf(charAt);
                    res += (char) (result + 'A') + "";
                }
                return res;
            }
        } catch (Exception e) {
        }

        return code;
    }


    public static boolean containsLetterIndex(int index,String text){
        if(isNotEmpty(text)){
            return text.toUpperCase().contains(getLetterCode(index));
        }
        return false;
    }

    /**
     * 编号
     *
     * @param code
     * @return
     */
    public static String getLetterCode(int code) {
        try {
            return (char) (code + 'A') + "";
        } catch (Exception e) {
        }
        return "";
    }

    public static boolean containsLetterCode(String code, String answer) {
        if (!isEmpty(code) && !isEmpty(answer)) {
            code = code.toUpperCase();
            answer = answer.toUpperCase();
            return answer.contains(code);
        }
        return false;
    }

    public static String formatUserNickName(String src) {
        if (!isEmpty(src)) {
            if (isMobile(src)) {
                return src.substring(0, 3) + "****" + src.substring(7);
            } else {
                if (src.length() > 4) {
                    return src.substring(0, 4) + "...";
                }
            }
        }
        return src;
    }

    /**
     * 是否是手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (!isEmpty(str)) {
            return PHONE_NUMBER_PATTERN.matcher(str).find();
        }
        return false;
    }

    /**
     * 是否是邮箱
     *
     * @param str
     * @return
     */
    public static boolean isEmail(String str) {
        if (!isEmpty(str)) {
            return EMAIL_PATTERN.matcher(str).find();
        }
        return false;
    }

    public static String formatJavaScript(String str) {
        if (!isEmpty(str)) {
            str = str.replaceAll("\\\\", "\\\\\\\\").replaceAll("'", "\\\\\'");
            str = str.replace("\r\n", " ");
            str = str.replace("\n", " ");
            str = str.replace("<", "&lt;");
            str = str.replace(">", "&gt;");
        }
        return str;
    }

    public static HashMap<String, String> splitTitle(String title) {
        if (!isEmpty(title)) {
            Matcher m = YEAR_PATTERN.matcher(title);
            if (m.find()) {
                HashMap<String, String> map = new HashMap<String, String>();
                int start = m.start();
                int end = m.end();
                if (start == 0) {
                    map.put("year", title.substring(start, end));
                    map.put("title", title.substring(end));
                } else {
                    map.put("year", title.substring(start, end));
                    map.put("title", title);
                }
                return map;
            }
        }
        return null;
    }

    public static int editDistance(String first, String second) {
        if (first == null) {
            first = "";
        }
        if (second == null) {
            second = "";
        }
        if (first.length() == 0) {
            return second.length();
        }
        if (second.length() == 0) {
            return first.length();
        }

        int firstLen = first.length() + 1;
        int secondLen = second.length() + 1;

        int[][] distanceMatrix = new int[firstLen][secondLen];
        for (int i = 0; i < firstLen; i++) {
            distanceMatrix[i][0] = i;
        }
        for (int j = 0; j < secondLen; j++) {
            distanceMatrix[0][j] = j;
        }

        for (int i = 1; i < firstLen; i++) {
            for (int j = 1; j < secondLen; j++) {
                int deletion = distanceMatrix[i - 1][j] + 1;
                int insertion = distanceMatrix[i][j - 1] + 1;
                int substitution = distanceMatrix[i - 1][j - 1];
                if (first.charAt(i - 1) != second.charAt(j - 1)) {
                    substitution++;
                }
                distanceMatrix[i][j] = Math.min(Math.min(insertion, deletion), substitution);
            }
        }
        return distanceMatrix[firstLen - 1][secondLen - 1];
    }

    public static String encodeUrl(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception e) {
        }
        return url;
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(throwable.getClass().getName() + ":" + throwable.getMessage() + "\n");
            StackTraceElement[] elements = throwable.getStackTrace();
            int count = 0;
            for (StackTraceElement element : elements) {
                count++;
                sb.append("\tat " + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName()
                        + ":" + element.getLineNumber() + ")" + "\n");
                if (count > 10) {
                    break;
                }
            }
            return sb.toString();
        }
        return null;
    }

    public static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 截断字符串
     *
     * @param src
     * @param length
     * @return
     */
    public static String cutStr(String src, int length) {
        if (!isEmpty(src) && length > 0) {
            if (src.length() > length) {
                return src.substring(0, length);
            }
        }
        return src;
    }

    public static String getTimeStr(Date time) {
        long timeSpan = System.currentTimeMillis() - time.getTime();
        log.debug("time span is:" + timeSpan);
        if (timeSpan < 60000) {
            // less than one minutes
            log.debug("less than one minutes");
            return "刚刚";
        } else if (timeSpan < 3600000l) {
            // less than one hour
            log.debug("less than one hour");
            return String.format("%d分钟前", timeSpan / 60000);
        } else if (timeSpan < 24 * 3600000l) {
            // less than one day
            log.debug("less than one day");
            return String.format("%d小时前", timeSpan / 3600000);
        } else if (timeSpan < 7 * 24 * 3600000l) {
            // less than one week
            log.debug("less than one week");
            return String.format("%d天前", timeSpan / 24 / 3600000);
        } else if (timeSpan < 30 * 24 * 3600000l) {
            // less than one month
            log.debug("less than one month");
            return String.format("%d星期前", timeSpan / 7 / 24 / 3600000);
        } else if (timeSpan < 365 * 24 * 3600000l) {
            // less than one year
            log.debug("less than one year");
            return String.format("%d个月前", timeSpan / 30 / 24 / 3600000);
        } else {
            log.debug("less than one century");
            return String.format("%d年前", timeSpan / 365 / 24 / 3600000);
        }
    }

    /**
     * 用时格式化
     *
     * @param second
     * @return
     */
    public static String formatDuration(Integer second) {
        if (second == null || second < 0) {
            second = 0;
        }
        int minute = second / 60;
        int s = second % 60;
        StringBuilder builder = new StringBuilder();
        if (minute != 0 || second != 0) {
            if (minute > 0) {
                builder.append(minute).append("分");
            }
            if (s > 0) {
                builder.append(s).append("秒");
            }
        } else {
            builder.append("0秒");
        }
        return builder.toString();
    }

    /**
     * 分数格式化
     *
     * @param score
     * @return
     */
    public static String formatScore(float score, int newValue) {
        if (newValue <= 0) {
            newValue = 0;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(newValue);
        return nf.format(score);
    }

    public static String formatPercent(double p, int newValue) {
        if (newValue <= 0) {
            newValue = 0;
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMaximumFractionDigits(newValue);
        return nf.format(p);
    }

    public static String formatScore(float score) {
        return formatScore(score, 1);
    }

    public static String encode(String src) {
        try {
            return URLEncoder.encode(src, "utf-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return src;
    }

    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }

    /**
     * 集合 拼接
     *
     * @param collection
     * @param split
     * @return
     */
    public static String join(Collection<?> collection, String split) {
        if (collection == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = collection.iterator();
        boolean hasNext = it.hasNext();
        while (hasNext) {
            sb.append(String.valueOf(it.next()));
            hasNext = it.hasNext();
            if (hasNext) {
                sb.append(split);
            }
        }
        return sb.toString();
    }

    public static String join(Collection<?> collection) {
        return join(collection, ",");
    }

    // 截取数字
    public static String getNumbers(String content) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    /**
     * 获取省份的短名字
     *
     * @param name
     * @return
     */
    public static String shortProvinceName(String name) {
        if (!isEmpty(name)) {
            if (name.indexOf("市") > -1 || name.indexOf("特别行政区") > -1) {
                return name.substring(0, 2);
            } else if (name.indexOf("自治区") > -1) {
                if (name.startsWith("内蒙古")) {
                    return name.substring(0, 3);
                } else {
                    return name.substring(0, 2);
                }
            } else if (name.indexOf("省") > -1) {
                return name.substring(0, name.length() - 1);
            }
        }
        return name;
    }

    public static boolean isValidateName(String name) {
        return isValidateName(name,false);
    }

    public static boolean isValidateName(String name,boolean isUserName) {
        if (!isEmpty(name)) {
            Matcher m = VALIDATE_PATTERN.matcher(name);
            boolean isValid = m.matches();
            if(isValid && isUserName){
                int count = getChineseNums(name);
                if(count>0){ //中文+数字
                    if(count>4){
                        isValid = false;
                    }else{
                        m = LETTER_PATTERN.matcher(name);
                        isValid = !m.find();
                    }
                }else{ //英文+数字
                    m = LETTER_PATTERN.matcher(name);
                    isValid = m.find() && name.length()<=16;
                }
            }
            return isValid;
        }
        return false;
    }

    /**
     * 只对URL中中文进行编码
     *
     * @param url
     * @return
     */
    public static String encodeChinese(String url) {
        try {

            // Matcher matcher =
            // Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
            /*
             * while (matcher.find()) { String tmp=matcher.group();
             * url=url.replaceAll(tmp,java.net.URLEncoder.encode(tmp,"utf-8"));
             * }
             */
            Matcher matcher = Pattern.compile("[^\\x00-\\xff]").matcher(url);
            while (matcher.find()) {
                String tmp = matcher.group();
                url = url.replaceAll(tmp, java.net.URLEncoder.encode(tmp, "utf-8"));
            }

        } catch (Exception e) {
        }
        return url;
    }

    /**
     * 获取字符串中中文的个数
     *
     * @param src
     * java中文在UTF8占3个字节
     * @return
     */
    public static int getChineseNums(String src) {
        try {
            if (isNotEmpty(src)) {
                return (src.getBytes("utf-8").length-src.length() ) / 2;
            }
        } catch (Exception e) {
        }
        return 0;
    }
}

