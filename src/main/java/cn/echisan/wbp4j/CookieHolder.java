package cn.echisan.wbp4j;

import org.apache.log4j.Logger;

import java.io.*;
import java.util.Objects;

/**
 * 用于存放cookie，登陆成功后就会把cookie存放到这里
 * 下次上传图片只需要在这里获取就可以了
 * <p>
 * Created by echisan on 2018/6/13
 */
public class CookieHolder {
    private static final Logger logger = Logger.getLogger(CookieHolder.class);

    /**
     * 存储在内存中的cookie
     */
    private static String COOKIE;

    /**
     * 是否启用缓存功能，默认开启
     */
    private static boolean enableCache = true;

    /**
     * 缓存的cookie文件名
     */
    private static final String DEFAULT_COOKIE_FILE_NAME = "wb-cookie";

    /**
     * 自定义的cookie文件名
     */
    private static String cookieFileName = DEFAULT_COOKIE_FILE_NAME;

    /**
     * cookie缓存所在的路径，默认存放到编译后的文件夹中
     */
    private static String cookiePath = Objects.requireNonNull(CookieHolder.class.getClassLoader().getResource("")).getPath();

    /**
     * 往CookieHolder中存放cookie
     *
     * @param cookie cookie
     * @throws IOException IOException
     */
    public static void setCOOKIE(String cookie) throws IOException {
        if (enableCache) {
            writeCookieToFile(cookie, cookiePath, cookieFileName);
        }
        COOKIE = cookie;
    }

    /**
     * 从Holder中获取cookie
     *
     * @return cookie
     * @throws IOException IOException
     */
    public static String COOKIE() throws IOException {
        // 如果内存中存在则直接返回
        if (COOKIE != null) {
            return COOKIE;
        }
        if (enableCache) {
            return readCookieFromCache();
        }
        return null;
    }

    /**
     * 将cookie写入缓存文件
     *
     * @param cookie   cookie
     * @param filePath 缓存文件存放的路径
     * @param fileName 缓存文件名
     * @throws IOException IOException
     */
    private static void writeCookieToFile(String cookie, String filePath, String fileName) throws IOException {
        File file = new File(filePath + File.separator + fileName);
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
    }


    /**
     * 从缓存中读取cookie
     *
     * @return cookie
     */
    private static String readCookieFromCache() throws IOException {
        File file = new File(cookiePath + File.separator + cookieFileName);
        if (file.exists()) {
            logger.info("找到cookie缓存[" + file.getAbsolutePath() + "]!");
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            StringBuilder sb = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return sb.toString();
        }
        logger.info("位置:[" + file.getAbsolutePath() + "]cookie缓存未找到!");
        return null;
    }

    /**
     * 设置是否开启缓存功能
     *
     * @param enableCache boolean
     */
    public static void setEnableCache(boolean enableCache) {
        CookieHolder.enableCache = enableCache;
    }

    /**
     * 自定义缓存文件名称
     *
     * @param cookieFileName cookieFileName
     */
    public static void setCookieFileName(String cookieFileName) {
        CookieHolder.cookieFileName = cookieFileName;
    }

    /**
     * 自定义缓存文件路径
     *
     * @param cookiePath 文件路径
     */
    public static void setCookiePath(String cookiePath) {
        CookieHolder.cookiePath = cookiePath;
    }

    public static String getCookieFilePathAndName() {
        if (enableCache) {
            return cookiePath + File.separator + cookieFileName;
        }
        return "未开启缓存功能";
    }

    /**
     * 判断是否已存在缓存
     *
     * @return boolean
     */
    public static boolean hasCookieCache() {
        File file = new File(cookiePath + File.separator + cookieFileName);
        return file.exists() && file.isFile();
    }
}
