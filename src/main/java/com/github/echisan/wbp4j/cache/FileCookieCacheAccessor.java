package com.github.echisan.wbp4j.cache;

import com.github.echisan.wbp4j.WbpConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 该类用于访问Cookie的缓存文件
 */
public class FileCookieCacheAccessor implements CookieCacheAccessor {
    private static final Logger logger = LoggerFactory.getLogger(FileCookieCacheAccessor.class);
    /**
     * 默认的缓存文件名称，其实也可以是路径，但要符合对应操作系统的路径格式
     */
    private static final String DEFAULT_CACHE_FILE_NAME = "wbpcookie";

    /**
     * 缓存文件名称或路径
     */
    private final String cacheFileName;

    /**
     * custom cookie file name
     *
     * @param cookieCacheFileName the cookieCacheFileName
     */
    public FileCookieCacheAccessor(String cookieCacheFileName) {
        this.cacheFileName = cookieCacheFileName;
    }

    /**
     * use default cookie cache file name
     */
    public FileCookieCacheAccessor() {
        this(DEFAULT_CACHE_FILE_NAME);
    }

    /**
     * save cookie to file
     *
     * @param cookie the cookie
     * @throws IOException the IOException
     */
    @Override
    public void saveCookie(String cookie) throws IOException {
        File file = new File(cacheFileName);
        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                throw new IOException("create cookie cache failed!! filePath:[ " + file.getPath() + " ].");
            }
        }
        FileOutputStream fos = new FileOutputStream(file);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        bw.write(cookie);
        bw.flush();
        bw.close();
        if (logger.isDebugEnabled()) {
            logger.debug("write cookie to file success!! filePath: [" + file.getPath() + " ].");
        }
    }

    /**
     * get cookie from cache file
     *
     * @return cookie
     * @throws IOException the IOException
     */
    @Override
    public String getCookie() throws IOException {
        File file = new File(cacheFileName);
        if (!file.exists()) {
            if (logger.isDebugEnabled()) {
                logger.debug("can not find cookie cache file. filePath: [ " + file.getPath() + "]. ");
            }
            return null;
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String cookie = br.readLine();
        br.close();

        if (!isCookieCacheLegal(cookie)) {
            logger.info("because the cookie length less 50,assert this cookie is not correct!");
            boolean delete = file.delete();
            if (!delete) {
                throw new IOException("could not delete the incorrect cookie file!! filePath:[ " + file.getPath() + " ].");
            }
            return null;
        }

        return cookie;
    }

    /**
     * 判断缓存文件中的的cookie是否合法
     *
     * @param cookie the cookie
     * @return is cookie cache legal
     */
    private boolean isCookieCacheLegal(String cookie) {
        if (cookie == null){
            return false;
        }
        return cookie.length() > WbpConstants.COOKIE_LENGTH_THRESHOLD;
    }
}
