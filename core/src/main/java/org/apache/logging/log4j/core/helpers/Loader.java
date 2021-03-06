/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

import java.io.InputStream;
import java.io.InterruptedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

/**
 * Load resources (or images) from various sources.
 */

public final class Loader {

    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";

    private static boolean ignoreTCL = false;

    private static final Logger LOGGER = StatusLogger.getLogger();

    static {
        String ignoreTCLProp = OptionConverter.getSystemProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }

    private Loader() {
    }

    /**
     * This method will search for <code>resource</code> in different
     * places. The search order is as follows:
     * <p/>
     * <ol>
     * <p/>
     * <p><li>Search for <code>resource</code> using the thread context
     * class loader under Java2. If that fails, search for
     * <code>resource</code> using the class loader that loaded this
     * class (<code>Loader</code>). Under JDK 1.1, only the the class
     * loader that loaded this class (<code>Loader</code>) is used.
     * <p/>
     * <p><li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is
     * using the system class loader in JDK 1.2 and virtual machine's
     * built-in class loader in JDK 1.1.
     * <p/>
     * </ol>
     * @param resource The resource to load.
     * @param defaultLoader The default ClassLoader.
     * @return A URL to the resource.
     */
    public static URL getResource(String resource, ClassLoader defaultLoader) {
        ClassLoader classLoader;
        URL url;

        try {
            classLoader = getTCL();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using context classloader "
                        + classLoader + ".");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }

            // We could not find resource. Let us now try with the classloader that loaded this class.
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            // We could not find resource. Finally try with the default ClassLoader.
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                url = defaultLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        } catch (IllegalAccessException t) {
            LOGGER.warn(TSTR, t);
        } catch (InvocationTargetException t) {
            if (t.getTargetException() instanceof InterruptedException
                || t.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LOGGER.warn(TSTR, t);
        } catch (Throwable t) {
            //
            //  can't be InterruptedException or InterruptedIOException
            //    since not declared, must be error or RuntimeError.
            LOGGER.warn(TSTR, t);
        }

        // Last ditch attempt: get the resource from the class path. It
        // may be the case that clazz was loaded by the Extentsion class
        // loader which the parent of the system class loader. Hence the
        // code below.
        LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }

    /**
     * This method will search for <code>resource</code> in different
     * places. The search order is as follows:
     * <p/>
     * <ol>
     * <p/>
     * <p><li>Search for <code>resource</code> using the thread context
     * class loader under Java2. If that fails, search for
     * <code>resource</code> using the class loader that loaded this
     * class (<code>Loader</code>). Under JDK 1.1, only the the class
     * loader that loaded this class (<code>Loader</code>) is used.
     * <p/>
     * <p><li>Try one last time with
     * <code>ClassLoader.getSystemResource(resource)</code>, that is is
     * using the system class loader in JDK 1.2 and virtual machine's
     * built-in class loader in JDK 1.1.
     * <p/>
     * </ol>
     * @param resource The resource to load.
     * @param defaultLoader The default ClassLoader.
     * @return An InputStream to read the resouce.
     */
    public static InputStream getResourceAsStream(String resource, ClassLoader defaultLoader) {
        ClassLoader classLoader;
        InputStream is;

        try {
            classLoader = getTCL();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using context classloader " + classLoader + ".");
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }

            // We could not find resource. Let us now try with the classloader that loaded this class.
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }

            // We could not find resource. Finally try with the default ClassLoader.
            if (defaultLoader != null) {
                LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                is = defaultLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
        } catch (IllegalAccessException t) {
            LOGGER.warn(TSTR, t);
        } catch (InvocationTargetException t) {
            if (t.getTargetException() instanceof InterruptedException
                || t.getTargetException() instanceof InterruptedIOException) {
                Thread.currentThread().interrupt();
            }
            LOGGER.warn(TSTR, t);
        } catch (Throwable t) {
            //
            //  can't be InterruptedException or InterruptedIOException
            //    since not declared, must be error or RuntimeError.
            LOGGER.warn(TSTR, t);
        }

        // Last ditch attempt: get the resource from the class path. It
        // may be the case that clazz was loaded by the Extentsion class
        // loader which the parent of the system class loader. Hence the
        // code below.
        LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResourceAsStream(resource);
    }

    /**
     * Load a Class by name.
     * @param clazz The class name.
     * @return The Class.
     * @throws ClassNotFoundException if the Class could not be found.
     */
    public static Class loadClass(String clazz) throws ClassNotFoundException {
        // Just call Class.forName(clazz) if we are instructed to ignore the TCL.
        if (ignoreTCL) {
            return Class.forName(clazz);
        } else {
            try {
                return getTCL().loadClass(clazz);
            } catch (Throwable e) {
                return Class.forName(clazz);
            }
        }
    }

    public static ClassLoader getClassLoader(Class class1, Class class2) {

        ClassLoader loader1 = null;
        try {
            loader1 = getTCL();
        } catch (Exception ex) {
            LOGGER.warn("Caught exception locating thread ClassLoader {}", ex.getMessage());
        }
        ClassLoader loader2 = class1 == null ? null : class1.getClassLoader();
        ClassLoader loader3 = class2 == null ? null : class2.getClass().getClassLoader();

        if (isChild(loader1, loader2)) {
            return isChild(loader1, loader3) ? loader1 : loader3;
        } else {
            return isChild(loader2, loader3) ? loader2 : loader3;
        }
    }

    private static boolean isChild(ClassLoader loader1, ClassLoader loader2) {
        if (loader1 != null && loader2 != null) {
            ClassLoader parent = loader1.getParent();
            while (parent != null && parent != loader2) {
                parent = parent.getParent();
            }
            return parent != null;
        }
        return loader1 != null;
    }

    /**
     * Return the ClassLoader to use.
     * @return the ClassLoader.
     */
    public static ClassLoader getClassLoader() {

        return getClassLoader(Loader.class, null);
    }

    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        ClassLoader cl;
        if (System.getSecurityManager() == null) {
            cl = Thread.currentThread().getContextClassLoader();
        } else {
            cl = (ClassLoader) java.security.AccessController.doPrivileged(
                new java.security.PrivilegedAction() {
                    public Object run() {
                        return Thread.currentThread().getContextClassLoader();
                    }
                }
            );
        }

        return cl;
    }
}
