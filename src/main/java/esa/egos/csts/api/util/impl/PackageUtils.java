package esa.egos.csts.api.util.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;


/**
 * Implements package utilities methods
 *
 */
public class PackageUtils
{
    private static final String UTF_8 = "UTF-8";

	private static final String JAVA = "java";

    private static final String CLASSES = "classes";

    private static final String CLASS_FILE_SUFFIX = ".class";

    private static final String JAVA_FILE_SUFFIX = "." + JAVA;

    private static final Logger LOG = Logger.getLogger(PackageUtils.class.getName());


    /**
     * Return the sub-packages of a package
     * 
     * @param packageName The package name
     * @param alsoNestedClasses If true includes also nested sub-packages
     * @return a list with sub-package names
     * @throws URISyntaxException 
     */
    public static List<String> getSubPackages(ClassLoader classLoader, String packageName, boolean alsoNestedClasses) throws Exception
    {
        List<String> ret = new ArrayList<String>();
        String resourcePath = packageName.replace('.', '/');
        URL url = ClassLoader.getSystemResource(resourcePath);
        if (url == null)
        {
            url = classLoader.getResource(resourcePath);
            if (url == null)
            {
                throw new RuntimeException("Couldn't find package " + packageName);
            }
        }

        File file = new File(url.getPath());
        if (file.isDirectory() && file.exists())
        {
            for (String directoryChild : file.list())
            {
                try
                {
                    if (new File(URLDecoder.decode(url.getPath(), UTF_8) + "/" + directoryChild).isDirectory())
                    {
                        StringBuilder sb = new StringBuilder(packageName);
                        sb.append(".");
                        sb.append(directoryChild);
                        String subPackageName = sb.toString();
                        ret.add(subPackageName);

                        if (alsoNestedClasses)
                        {
                            ret.addAll(getSubPackages(classLoader, subPackageName, alsoNestedClasses));
                        }
                    }
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, "Failed to provide sub-packages of the " + packageName + " package. ", e);
                }
            }
        }
        else
        {
            // assume it is running in Karaf(Felix) container
            // get sub-packages w/o OSGi dependency
            long dots = 0;
            if (alsoNestedClasses == false)
            {
                dots = getDotCount(packageName);
            }

            JarFile jarFile = null;
            try
            {
                String filePath = getPathFileNameFromResource(classLoader, resourcePath);
                jarFile = new JarFile(filePath);

                // Getting jar's entries
                Enumeration<? extends JarEntry> enumeration = jarFile.entries();
                // iterates over jar's entries
                while (enumeration.hasMoreElements())
                {
                    JarEntry jarEntry = enumeration.nextElement();
                    String name = jarEntry.getName();
                    if (name.startsWith(resourcePath) && name.endsWith("/"))
                    {
                        // remove the last slash
                        if (alsoNestedClasses == false)
                        {
                            long count = name.chars().filter(c -> c == '/').count();
                            if (count > dots + 2)
                            {
                                // ignore nested package
                                continue;
                            }
                        }

                        name = name.substring(0, name.length() - 1);
                        name = name.replace('/', '.');
                        if (name.equals(packageName))
                        {
                            // ignore the input package name
                            continue;
                        }
                        ret.add(name);
                        LOG.finest(name);
                    }
                }
            }
            finally
            {
                if (jarFile != null)
                {
                    jarFile.close();
                }
            }
        }

        return ret;
    }

    /**
     * Get JAR path file name installed in Felix(Karaf) container
     * from a resource path
     * 
     * https://jar-download.com/artifacts/org.apache.felix/org.apache.felix.framework/6.0.2/source-code/org/apache/felix/framework/util/WeakZipFileFactory.java
     * 
     * @param classLoader The class loader
     * @param resourcePath The resource path
     * @return JAR path file name
     * @throws RuntimeException
     */
    public static String getPathFileNameFromResource(ClassLoader classLoader, String resourcePath) throws Exception
    {
        try (InputStream inputStream = classLoader.getResourceAsStream(resourcePath))
        {
            // try to get ZIP(jar) file from WeakZipInputStream
            try
            {
                Field field = inputStream.getClass().getDeclaredField("m_zipFileSnapshot");
                field.setAccessible(true);
                ZipFile zipFile = (ZipFile) field.get(inputStream);
                field.setAccessible(false);
                return zipFile.getName();
            }
            catch (Exception e)
            {
                throw new RuntimeException("Couldn't get a JAR path file installed in Felix(Karaf) container from resource "
                                           + resourcePath, e);
            }
        }
    }

    /**
     * Get all binary classes from a package
     * 
     * @param classLoader The class loader
     * @param packageName The package name
     * @param alsoNestedClasses The nested classes are included
     * @return the list of found classes in the package
     * @throws Exception 
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getPackageClasses(ClassLoader classLoader, String packageName, boolean alsoNestedClasses) throws Exception
    {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        String internalPath = packageName.replace('.', '/');
        URL url = ClassLoader.getSystemResource(internalPath);
        if (url == null)
        {
            url = classLoader.getResource(internalPath);
            if (url == null)
            {
                throw new Exception("Couldn't find package " + packageName);
            }
        }
        try
        {
            final String fileName = URLDecoder.decode(url.getFile(), UTF_8);
            File packageDirectory = new File(fileName);
            if (packageDirectory.exists())
            {
                ret = getPackageClassesFromDirectory(packageDirectory, packageName, alsoNestedClasses);
            }
            else if (url.getProtocol().equals("jar"))
            {
                ret = getPackageClassesFromJar(url, classLoader, packageName, alsoNestedClasses);
            }
            else if (url.getProtocol().equals("bundle"))
            {
                if (url.getAuthority().equals("felix"))
                {
                    Bundle bundle = FrameworkUtil.getBundle(PackageUtils.class);
                    if (bundle == null)
                    {
                        throw new Exception(String.format("Failed to get classes from package %s. Unable to retrieve bundle", packageName));
                    }
                    Enumeration entries = bundle.findEntries(internalPath, "*", true);
                    while(entries != null && entries.hasMoreElements())
                    {
                        ret.add(entries.nextElement().getClass());
                    }
                }
                else
                {
                    String pathFileName = getPathFileNameFromResource(classLoader, internalPath);
                    ret = getPackageClassesFromJar(new URL("jar:file:"+ pathFileName + "!/"), classLoader, packageName, alsoNestedClasses);
                }
            }
        }
        catch (Exception e)
        {
            throw new Exception("Failed to get classes from package " + packageName + ". ", e);
        }

        return ret;
    }

    /**
     * Get all binary classes from a package
     * 
     * @param packageName The package name
     * @param alsoNestedClasses The nested classes are included
     * @return the list of found classes in the package
     * @throws Exception 
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getPackageClasses(String packageName, boolean alsoNestedClasses) throws Exception
    {
        return getPackageClasses(PackageUtils.class.getClassLoader(), packageName, alsoNestedClasses);
    }

    /**
     * Collect classes from apackage directory
     * 
     * @param packageDirectory
     * @param packageName
     * @param alsoNestedClasses
     * @return
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> getPackageClassesFromDirectory(File packageDirectory, String packageName, boolean alsoNestedClasses) throws ClassNotFoundException
    {
        LOG.log(Level.INFO, "getting classes from a package director");
        List<Class<?>> ret = new ArrayList<Class<?>>();
        long packageNameSegmentCount = getDotCount(packageName);
        for (String fileName : packageDirectory.list())
        {
            if (fileName.endsWith(CLASS_FILE_SUFFIX))
            {
                // build the class name
                Class<?> clazz = Class
                        .forName(packageName + "."
                                 + fileName.substring(0, fileName.length() - CLASS_FILE_SUFFIX.length()));
                if (!alsoNestedClasses)
                {
                    if ((packageNameSegmentCount + 1) != getDotCount(clazz.getCanonicalName()))
                    {
                        // ignore nested classes
                        continue;
                    }
                }
                ret.add(clazz);
            }
        }
        return ret;
    }

    /**
     * Collect classes from a package in a jar file
     * 
     * @param url The URL to a jar file
     * @param packageName The package name
     * @param alsoNestedClasses The flag indication whether nested packages should be included (true) or not (false)
     * @return A list of found classes
     * @throws IOException
     */
    public static List<Class<?>> getPackageClassesFromJar(URL url, ClassLoader classLoader, String packageName, boolean alsoNestedClasses) throws IOException
    {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        JarFile jarFile = null;

        try
        {
            final String urlDecoded = URLDecoder.decode(url.getFile(), UTF_8);
            LOG.info(() -> { return "getting " + packageName + ((alsoNestedClasses)?".*":"")
                    + " classes from JAR file " + urlDecoded;});
            // extract the path file name from URL: file:<path file name>!/package
            String[] split1 = urlDecoded.split("file:");
            String[] split2 = split1[1].split("!");
            String filePath = split2[0];
            String internalPath = packageName.replace('.', '/');

            LOG.log(Level.INFO, "filePath: " + filePath);

            if (internalPath.charAt(0) == '/')
            {
                // cut off the very the first slash if present
                internalPath = internalPath.substring(1);
            }
            long packageNameSegmentCount = getDotCount(packageName);
            jarFile = new JarFile(filePath);

            // Getting jar's entries
            Enumeration<? extends JarEntry> enumeration = jarFile.entries();
            // iterates over jar's entries
            while (enumeration.hasMoreElements())
            {
                JarEntry jarEntry = enumeration.nextElement();

                // take the classes stored at the certain path in the jar only
                // the relative path of file in the jar.
                String relativePathFileName = jarEntry.getName(); 
                if (relativePathFileName.contains(internalPath) && relativePathFileName.endsWith(CLASS_FILE_SUFFIX))
                {
                    // build the class name
                    String className = relativePathFileName.replace("/", ".").replace(CLASS_FILE_SUFFIX, "");
                    Class<?> clazz = Class.forName(className, true, classLoader);
                    if (!alsoNestedClasses)
                    {
                        if ((packageNameSegmentCount + 1) != getDotCount(clazz.getCanonicalName()))
                        {
                            // ignore nested classes
                            continue;
                        }
                    }
                    ret.add(clazz);
                    LOG.finest(() -> {
                        return "class: " + className;
                    });
                }
            }
        }
        catch (Exception e)
        {
            StringBuilder sbMsg = new StringBuilder("Could not get package ");
            sbMsg.append(packageName).append(" classes from file ").append(url);
            throw new RuntimeException(sbMsg.toString(), e);
        }
        finally
        {
            if (jarFile != null)
            {
                jarFile.close();
            }
        }
        return ret;
    }

    /**
     * Get all binary classes from a URL
     * 
     * @param url The URL
     * @param alsoNestedClasses The nested classes are included
     * @return the list of found classes in the package
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException 
     */
    public static List<Class<?>> getClasses(URL url,
                                            boolean alsoNestedClasses) throws MalformedURLException, UnsupportedEncodingException
    {
        if (url == null)
        {
            throw new NullPointerException("Could not find classes from URL is null");
        }

        String packageName = getPackageName(URLDecoder.decode(url.getPath(), UTF_8));

        List<Class<?>> ret = new ArrayList<Class<?>>();
        File packageDirectory = new File(url.getFile());
        if (packageDirectory.exists())
        {
            long packageNameSegmentCount = getDotCount(packageName);
            for (String fileName : packageDirectory.list())
            {
                try
                {
                    if (fileName.endsWith(CLASS_FILE_SUFFIX))
                    {
                        Class<?> clazz = Class
                                .forName(packageName + "."
                                         + fileName.substring(0, fileName.length() - CLASS_FILE_SUFFIX.length()));
                        if (!alsoNestedClasses)
                        {
                            if ((packageNameSegmentCount + 1) != getDotCount(clazz.getCanonicalName()))
                            {
                                // ignore nested classes
                                continue;
                            }
                        }
                        ret.add(clazz);
                    }
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, "Failed to provide classes from " + url + ". ", e);
                }
            }
            LOG.info("packageDirectory " + url.getPath() + " exist");
        }
        else
        {
            LOG.info("packageDirectory " + url.getPath() + " does not exist");
        }

        return ret;
    }

    /**
     * Get all source classes from a URL
     * 
     * @param url The URL
     * @return the list of found classes in the package
     * @throws ClassNotFoundException
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException 
     */
    public static List<Class<?>> getSourceClasses(URL url) throws MalformedURLException, UnsupportedEncodingException
    {
        if (url == null)
        {
            throw new IllegalArgumentException("Could not find classes from URL " + url);
        }

        String packageName = getPackageName(URLDecoder.decode(url.getPath(), UTF_8));

        List<Class<?>> ret = new ArrayList<Class<?>>();
        File packageDirectory = new File(url.getFile());
        if (packageDirectory.exists())
        {
            for (String fileName : packageDirectory.list())
            {
                try
                {
                    if (fileName.endsWith(JAVA_FILE_SUFFIX))
                    {
                        Class<?> clazz = Class
                                .forName(packageName + "."
                                         + fileName.substring(0, fileName.length() - JAVA_FILE_SUFFIX.length()));
                        ret.add(clazz);
                    }
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, "Failed to provide classes from " + url + ". ", e);
                }
            }
        }

        return ret;
    }

    /**
     * Return number of dots in the string
     * @param str The string
     * @return The number of dots in the string
     */
    private static long getDotCount(final String str)
    {
        return str.chars().filter(c -> c == '.').count();
    }

    /**
     * Return a package name from the path
     * 
     * @param path The path
     * @return
     */
    private static String getPackageName(String path)
    {
        int index = path.indexOf(CLASSES);
        int offset = CLASSES.length() + 1;
        if (index == -1)
        {
            index = path.indexOf(JAVA);
            offset = JAVA.length() + 1 ;
        }
        
        String packageName = path.substring(index + offset);
        return packageName.replace("/", ".");
    }

}
