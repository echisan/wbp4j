package cn.echisan.wbp4j.io;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by echisan on 2018/11/5
 */
public class ClassLoaderWrapper {
    ClassLoader systemClassLoader;
    ClassLoader defaultClassLoader;

    ClassLoaderWrapper() {
        try {
            systemClassLoader = ClassLoader.getSystemClassLoader();
            defaultClassLoader = getClass().getClassLoader();
        } catch (SecurityException ignored) {

        }
    }

    URL getResourceAsURL(String resource){
        return getResourceAsURL(resource,getClassLoaders(null));
    }

    URL getResourceAsURL(String resource, ClassLoader classLoader){
        return getResourceAsURL(resource,getClassLoaders(classLoader));
    }

    URL getResourceAsURL(String resource, ClassLoader[] classLoader){
        URL url;
        for (ClassLoader cl : classLoader) {

            if (null != cl) {
                // look for the resource as passed in...
                url = cl.getResource(resource);
                // ...but some class loaders want this leading "/", so we'll add it
                // and try again if we didn't find the resource
                if (null == url) {
                    url = cl.getResource("/" + resource);
                }
                // "It's always in the last place I look for it!"
                // ... because only an idiot would keep looking for it after finding it, so stop looking already.
                if (null != url) {
                    return url;
                }
            }
        }
        return null;
    }

    InputStream getResourceAsStream(String resource){
        return getResourceAsStream(resource,getClassLoaders(null));
    }

    InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                InputStream returnValue = cl.getResourceAsStream(resource);
                if (null == returnValue) {
                    returnValue = cl.getResourceAsStream("/" + resource);
                }
                if (null != returnValue) {
                    return returnValue;
                }
            }
        }
        return null;
    }

    ClassLoader[] getClassLoaders(ClassLoader classLoader) {
        return new ClassLoader[]{
                classLoader,
                defaultClassLoader,
                Thread.currentThread().getContextClassLoader(),
                getClass().getClassLoader(),
                systemClassLoader
        };
    }
}
