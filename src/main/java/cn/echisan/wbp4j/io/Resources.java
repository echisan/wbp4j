package cn.echisan.wbp4j.io;

import java.io.IOException;
import java.net.URL;

/**
 * Created by echisan on 2018/11/5
 */
public class Resources {
    private static ClassLoaderWrapper classLoaderWrapper = new ClassLoaderWrapper();

    Resources() {
    }

    public static ClassLoader getDefaultClassLoader() {
        return classLoaderWrapper.defaultClassLoader;
    }

    public static void setDefaultClassLoader(ClassLoader classLoader) {
        classLoaderWrapper.defaultClassLoader = classLoader;
    }

    public static URL getResourceURL(ClassLoader classLoader, String resource) throws IOException {
        URL url = classLoaderWrapper.getResourceAsURL(resource, classLoader);
        if (url == null) {
            throw new IOException("Could not find resource" + resource);
        }
        return url;
    }
}
