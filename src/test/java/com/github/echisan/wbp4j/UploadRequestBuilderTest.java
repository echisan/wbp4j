package com.github.echisan.wbp4j;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

public class UploadRequestBuilderTest {

    @Test
    public void buildTest(){

    }

    /**
     * Gets image url.
     *
     * @param pid     the pid
     * @param sizeidx the sizeidx
     * @param https   the https
     * @return the image url
     */
    public static String getImageUrl(String pid, int sizeidx, boolean https) {
        String[] sizeArr = new String[]{"large", "mw1024", "mw690", "bmiddle", "small", "thumb180", "thumbnail", "square"};
        pid = pid.trim();
        String size = sizeArr[sizeidx];
        // 传递 pid
        Pattern p = Pattern.compile("^[a-zA-Z0-9]{32}$");
        Matcher m = p.matcher(pid);

        if (m.matches()) {
            System.out.println("匹配了");
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
