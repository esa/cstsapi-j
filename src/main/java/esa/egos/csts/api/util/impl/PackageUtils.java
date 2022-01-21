package esa.egos.csts.api.util.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 * Implements package utilities methods
 *
 */
public class PackageUtils
{
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
     * @return The lis with sub-package names
     */
    public static List<String> getSubPackages(String packageName, boolean alsoNestedClasses)
    {
        List<String> ret = new ArrayList<String>();
        URL url = ClassLoader.getSystemResource(packageName.replace('.', '/'));
        File packageDirectory = new File(url.getFile());
        if (packageDirectory.exists())
        {
            for (String directoryChild : packageDirectory.list())
            {
                try
                {
                    if (new File(url.getPath() + "/" + directoryChild).isDirectory())
                    {
                        StringBuilder sb = new StringBuilder(packageName);
                        sb.append(".");
                        sb.append(directoryChild);
                        String subPackageName = sb.toString();
                        ret.add(subPackageName);

                        if (alsoNestedClasses)
                        {
                            ret.addAll(getSubPackages(subPackageName, alsoNestedClasses));
                        }
                    }
                }
                catch (Exception e)
                {
                    LOG.log(Level.SEVERE, "Failed to provide sub-packages of the " + packageName + " package. ", e);
                }
            }
        }

        return ret;
    }

    /**
     * Get all binary classes from a package
     * 
     * @param packageName The package name
     * @param alsoNestedClasses The nested classes are included
     * @return the list of found classes in the package
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getPackageClasses(String packageName, boolean alsoNestedClasses)
    {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        String internalPath = packageName.replace('.', '/');
        URL url = ClassLoader.getSystemResource(internalPath);
        LOG.log(Level.INFO, "package URL: " + url);
        if (url == null)
        {
            throw new IllegalArgumentException("Could not find package " + packageName);
        }
        LOG.log(Level.INFO, "URL: " + url.getFile());
        try
        {
            File packageDirectory = new File(url.getFile());
            if (packageDirectory.exists())
            {
                ret = getPackageClassesFromDirectory(packageDirectory, packageName, alsoNestedClasses);
            }
            else
            {
                ret = getPackageClassesFromJar(url, packageName, alsoNestedClasses);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOG.log(Level.SEVERE, "Failed to provide classes of the " + packageName + " package. ", e);
        }

        return ret;
    }

    private static List<Class<?>> getPackageClassesFromDirectory(File packageDirectory, String packageName, boolean alsoNestedClasses) throws ClassNotFoundException
    {
        LOG.log(Level.INFO, "getting classes from a package director");
        List<Class<?>> ret = new ArrayList<Class<?>>();
        int packageNameSegmentCount = getDotCount(packageName);
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

    public static List<Class<?>> getPackageClassesFromJar(URL url, String packageName, boolean alsoNestedClasses) throws IOException
    {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        JarFile jar = null;

        try
        {
            LOG.log(Level.INFO, "getting classes from JAR file " + url);
            // extract the path file name from URL: file:<path file name>!/package
            String[] split1 = url.getFile().split("file:");
            String[] split2 = split1[1].split("!");
            String filePath = split2[0];
            String internalPath = split2[1];

            LOG.log(Level.INFO, "filePath: " + filePath);
            LOG.log(Level.INFO, "internalPath: " + internalPath);

            if (internalPath.charAt(0) == '/')
            {
                // cut off the very first slash
                internalPath = internalPath.substring(1);
            }
            int packageNameSegmentCount = getDotCount(packageName);
            jar = new JarFile(filePath);

            // Getting jar's entries
            Enumeration<? extends JarEntry> enumeration = jar.entries();
            // iterates over jar's entries
            while (enumeration.hasMoreElements())
            {
                ZipEntry zipEntry = enumeration.nextElement();
                LOG.log(Level.INFO, "zipEntry: " + zipEntry.getName());

                // take the classes stored at the certain path in the jar only
                if (zipEntry.getName().contains(internalPath) && zipEntry.getName().endsWith(CLASS_FILE_SUFFIX))
                {
                    // the relative path of file in the jar.
                    String fileName = zipEntry.getName();

                    // build the class name
                    Class<?> clazz = Class.forName(fileName.replace("/", ".").replace(CLASS_FILE_SUFFIX, ""));
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
        }
        catch (Exception e)
        {
            StringBuilder sbMsg = new StringBuilder("Could not get package ");
            sbMsg.append(packageName).append(" classes from file ").append(url);
            throw new RuntimeException(sbMsg.toString(), e);
        }
        finally
        {
            if (jar != null)
            {
                jar.close();
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
     */
    public static List<Class<?>> getClasses(URL url,
                                            boolean alsoNestedClasses) throws MalformedURLException
    {
        if (url == null)
        {
            throw new NullPointerException("Could not find classes from URL is null");
        }

        String packageName = getPackageName(url.getPath());

        List<Class<?>> ret = new ArrayList<Class<?>>();
        File packageDirectory = new File(url.getFile());
        if (packageDirectory.exists())
        {
            int packageNameSegmentCount = getDotCount(packageName);
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
            System.out.println("packageDirectory " + url.getPath() + " exist");
        }
        else
        {
            System.out.println("packageDirectory " + url.getPath() + " does not exist");
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
     */
    public static List<Class<?>> getSourceClasses(URL url) throws MalformedURLException
    {
        if (url == null)
        {
            throw new IllegalArgumentException("Could not find classes from URL " + url);
        }

        String packageName = getPackageName(url.getPath());

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
    private static int getDotCount(final String str)
    {
        return str.length() - str.replaceAll("[.]", "").length();
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
