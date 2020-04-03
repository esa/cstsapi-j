package esa.egos.csts.api.util.impl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class PackageUtils
{
    private static final String CLASS_FILE_SUFFIX = ".class";

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
                    LOG.severe("Failed to provide sub-packages of the " + packageName + " package. " + e.getMessage());
                }
            }
        }

        return ret;
    }

    /**
     * Get all classes from a package
     * 
     * @param packageName The package name
     * @param alsoNestedClasses The nested classes are included
     * @return the list of found classes in the package
     * @throws ClassNotFoundException
     */
    public static List<Class<?>> getPackageClasses(String packageName,
                                                   boolean alsoNestedClasses)
    {
        List<Class<?>> ret = new ArrayList<Class<?>>();
        URL url = ClassLoader.getSystemResource(packageName.replace('.', '/'));
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
                    LOG.severe("Failed to provide classes of the " + packageName + " package. " + e.getMessage());
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
}
