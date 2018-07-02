package cn.echisan.wbp4j.utils;

import cn.echisan.wbp4j.exception.Wbp4jException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Objects;

/**
 * Created by echisan on 2018/6/13
 */
public class CookieHolder {

    private static final Logger logger = Logger.getLogger(CookieHolder.class);

    private static final String DEFAULT_COOKIE_FILE_NAME = "wb-cookie";

    private static String COOKIE;

    private static String cookieFileName;

    private static String BASE_PATH = Objects.requireNonNull(CookieHolder.class.getClassLoader().getResource("")).getPath();

    private static boolean enableCache = false;

    public static void setCookies(String cookie, String fileName) throws IOException {
        // 如果是开发模式才进行缓存
        if (enableCache){
            cookieFileName = fileName;
            File file = new File(BASE_PATH + cookieFileName);
            if (!file.exists()) {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    throw new IOException("cookie [" + file.getName() + "] 文件创建失败!");
                }
            }

            FileOutputStream fos = new FileOutputStream(file, false);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
            bw.write(cookie);
            bw.flush();
            bw.close();

            COOKIE = cookie;
        }
    }

    public static void setCookies(String cookie) throws IOException {
        setCookies(cookie, DEFAULT_COOKIE_FILE_NAME);
    }

    public static String getCookies() throws IOException {
        if (COOKIE != null) {
            logger.debug("内存中已存在cookie,直接使用");
            return COOKIE;
        }
        // 如果是开发模式就从缓存里找
        if (enableCache){
            String cookie_file_name = cookieFileName == null ? DEFAULT_COOKIE_FILE_NAME : cookieFileName;
            File file = new File(BASE_PATH + cookie_file_name);
            if (file.exists()) {
                logger.info("找到cookie缓存[" + file.getAbsolutePath()+"]!");
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                StringBuilder sb = new StringBuilder();
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                return sb.toString();
            }
            logger.info("位置:["+file.getAbsolutePath()+"]cookie缓存未找到!");
            return null;
        }
        return null;
    }

    public static String getCookiesPath() {
        return BASE_PATH;
    }

    public static boolean exist(){
        if (enableCache){
            String cookie_file_name = cookieFileName == null ? DEFAULT_COOKIE_FILE_NAME : cookieFileName;
            File file = new File(BASE_PATH + cookie_file_name);
            return file.isFile() && file.exists();
        }
        throw new Wbp4jException("非开发模式不对cookie进行缓存，操作失败");
    }

    public static String getCookiesFile(){
        if (enableCache){
            String cookie_file_name = cookieFileName == null ? DEFAULT_COOKIE_FILE_NAME : cookieFileName;
            return BASE_PATH + cookie_file_name;
        }
        throw new Wbp4jException("非开发模式不对cookie进行缓存，操作失败");
    }

    public static boolean deleteCookieCache(){
        if (enableCache){
            File file = new File(getCookiesFile());
            if (file.exists() && file.isFile()){
                boolean delete = file.delete();
                if (delete){
                    logger.info("cookie缓存删除成功");
                    return true;
                }
                logger.info("cookie缓存失败");
                return false;
            }
            logger.info("缓存文件压根不存在,没法删除,所以算是成功了");
            return true;
        }
        throw new Wbp4jException("非开发模式不对cookie进行缓存，操作失败");
    }

    public static void enableCache(boolean enableCache) {
        CookieHolder.enableCache = enableCache;
    }

    public static boolean isEnableCache(){
        return enableCache;
    }
}
