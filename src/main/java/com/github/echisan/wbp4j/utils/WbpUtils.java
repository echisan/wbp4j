package com.github.echisan.wbp4j.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class WbpUtils {

    /**
     * Gets image url.
     *
     * @param pid     the pid
     * @param imageSize the imageSize
     * @param https   the https
     * @return the image url
     */
    public static String getImageUrl(String pid, ImageSize imageSize, boolean https) {
        pid = pid.trim();
        String size = imageSize.name();
        // 传递 pid
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{32}$");
        Matcher m = p.matcher(pid);

        if (m.matches()) {
            CRC32 crc32 = new CRC32();
            crc32.update(pid.getBytes());
            return (https ? "https" : "http") + "://" + (https ? "ws" : "ww")
                    + ((crc32.getValue() & 3) + 1) + ".sinaimg.cn/" + size
                    + "/" + pid + "." + (pid.charAt(21) == 'g' ? "gif" : "jpg");
        }
        // 传递 url
        String url = pid;
        Pattern p1 = Pattern.compile("^(https?://[a-z]{2}d.sinaimg.cn/)(large|bmiddle|mw1024|mw690|small|square|thumb180|thumbnail)(/[a-z0-9]{32}.(jpg|gif))$");
        Matcher m1 = p1.matcher(url);
        if (m1.find()) {
            return m.group(1) + size + m.group(3);
        }
        return null;
    }
}
