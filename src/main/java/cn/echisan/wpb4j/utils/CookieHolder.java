package cn.echisan.wpb4j.utils;

import cn.echisan.wpb4j.exception.Wbp4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Objects;

/**
 * Created by echisan on 2018/6/13
 */
public class CookieHolder {

    private static final Logger logger = LoggerFactory.getLogger(CookieHolder.class);

    private static final String DEFAULT_COOKIE_FILE_NAME = "wbcookie.txt";

    private static String COOKIE;

    private static String cookieFileName;

    private static String BASE_PATH = Objects.requireNonNull(CookieHolder.class.getClassLoader().getResource("")).getPath();

    public static void setCookies(String cookie, String fileName) throws IOException {
        cookieFileName = fileName;
        File file = new File(BASE_PATH + cookieFileName);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                throw new Wbp4jException("cookie" + file.getName() + "文件创建失败!");
            }
        }

        FileOutputStream fos = new FileOutputStream(file, false);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
        bw.write(cookie);
        bw.flush();
        bw.close();

        COOKIE = cookie;
    }

    public static void setCookies(String cookie) throws IOException {
        setCookies(cookie, DEFAULT_COOKIE_FILE_NAME);
    }

    public static String getCookies() throws IOException {
        if (COOKIE != null) {
            return COOKIE;
        }
        String cookie_file_name = cookieFileName == null ? DEFAULT_COOKIE_FILE_NAME : cookieFileName;
        File file = new File(BASE_PATH + cookie_file_name);
        if (file.exists()) {
            logger.info("find cookie file : " + file.getAbsolutePath()+file.getName());
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        }
        logger.info("位置:{},cookieFile不存在", file.getAbsolutePath());
        return null;
    }

    public static String getCookiesPath() {
        return BASE_PATH;
    }

    public static boolean exist(){
        String cookie_file_name = cookieFileName == null ? DEFAULT_COOKIE_FILE_NAME : cookieFileName;
        File file = new File(BASE_PATH + cookie_file_name);
        return file.isFile() && file.exists();
    }
}
