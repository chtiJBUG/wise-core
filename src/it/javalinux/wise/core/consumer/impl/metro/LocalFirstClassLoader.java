package it.javalinux.wise.core.consumer.impl.metro;

import java.net.URL;
import java.net.URLClassLoader;

public class LocalFirstClassLoader extends URLClassLoader {
    public LocalFirstClassLoader( URL[] urls,
                                  ClassLoader parent ) {
        super(urls, parent);
    }

    @Override
    public synchronized Class<?> loadClass( String name,
                                            boolean resolve ) throws ClassNotFoundException {
        Class claz = findLoadedClass(name);
        if (claz == null) {
            try {
                claz = findClass(name);
            } catch (ClassNotFoundException e) {
                if (getParent() != null) {
                    claz = getParent().loadClass(name);
                } else {
                    throw e;
                }

            }
        }
        if (resolve) {
            resolveClass(claz);
        }
        return claz;
    }

}
