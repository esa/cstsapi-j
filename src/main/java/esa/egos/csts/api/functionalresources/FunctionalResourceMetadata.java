package esa.egos.csts.api.functionalresources;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Optional;

import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.events.FunctionalResourceEventEx;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.CstsValueFactory;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.oids.OidTree;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.types.Name;
import esa.egos.csts.api.util.impl.CSTSUtils;
import esa.egos.csts.api.util.impl.PackageUtils;
import esa.egos.proxy.xml.OidConfig;
import esa.egos.proxy.xml.Oid;

/**
 * Collects FR data from JASN.1 generated package
 */
public class FunctionalResourceMetadata
{
    /** The logger */
    private static final Logger LOG = Logger.getLogger(FunctionalResourceMetadata.class.getName());

    /** The instance singleton */
    private static FunctionalResourceMetadata instance = null;

    /** The parameter cache */
    private static Map<Name, FunctionalResourceParameterEx<?>> parameterCache = new HashMap<Name, FunctionalResourceParameterEx<?>>();

    /** The instance singleton lock */
    private static Object lock = new Object();

    private static final String  JAVA = "java";

    private static final String TYPE_SUFFIX = "Type";

    private static final String FR_OID_SUFFIX = "FrOid";

    private static final String FR_PAR_OID_SUFFIX = "ParamOid";

    private static final String FR_EV_OID_SUFFIX = "EventOid";

    private static final String FR_TYPES_CLASS_NAME = "Fr";

    /** The BER classes map */
    private Map<String, Class<?>> berClasses;

    /** The non-BER classes map */
    private List<Class<?>> nonBerClasses;

    /** The OID name to OID array map */
    private Map<String, int[]> oidName2oidArray;

    /** The OID to OID name map */
    private Map<ObjectIdentifier, String> oid2oidName;

    /** The FR OID name to OID array map */
    private Map<String, int[]> frOidName2oidArray;

    /** The FR parameter OID name to OID array map */
    private Map<String, int[]> frParameterOidName2oidArray;

    /** The FR event OID name to OID array map */
    private Map<String, int[]> frEventOidName2oidArray;

    /** The FR directive OID name to OID array map */
    private Map<String, int[]> frDirectiveOidName2oidArray;

    /** The FR OID to map of FR parameter OID to parameter BER class */
    private Map<ObjectIdentifier, Map<ObjectIdentifier, Class<?>>> frOid2frParameterOidAndClass;

    /** The FR OID to map of FR event OID to event BER class */
    private Map<ObjectIdentifier, Map<ObjectIdentifier, Class<?>>> frOid2frEventOidAndClass;

    /** CSTS value factory */
    private ICstsValueFactory cstsValueFactory;

    private enum Type
    {
        PARAMETER,
        EVENT,
        DIRECTIVE
    }

    /**
     * C-tor
     */
    private FunctionalResourceMetadata()
    {
        this.berClasses = new LinkedHashMap<String, Class<?>>();
        this.nonBerClasses = new ArrayList<Class<?>>();
        this.oidName2oidArray = new LinkedHashMap<String, int[]>();
        this.oid2oidName = new LinkedHashMap<ObjectIdentifier, String>();
        this.frOidName2oidArray = new LinkedHashMap<String, int[]>();
        this.frParameterOidName2oidArray = new LinkedHashMap<String, int[]>();
        this.frEventOidName2oidArray = new LinkedHashMap<String, int[]>();
        this.frDirectiveOidName2oidArray = new LinkedHashMap<String, int[]>();
        this.frOid2frParameterOidAndClass = new LinkedHashMap<>();
        this.frOid2frEventOidAndClass = new LinkedHashMap<>();
        this.cstsValueFactory = CstsValueFactory.getInstance();
    }

    /**
     * Get the singleton instance
     * @return The instance
     */
    public static FunctionalResourceMetadata getInstance()
    {
        if (instance == null)
        {
            synchronized (lock)
            {
                if (instance == null)
                {
                    instance = new FunctionalResourceMetadata();
                }
            }
        }

        return instance;
    }

    /**
     * Load FR meta data from the jASN compiler generated binary classes
     * 
     * @param packageName The package name containing the generated binary classes
     * @throws Exception
     */
    public synchronized void loadFromBinaryClasses(String packageName) throws Exception
    {
        try
        {
            // collect all classes from JASN.1 generated package
            LOG.info("looking for jASN.1 classes in package " + packageName);
            List<Class<?>> classes = PackageUtils.getPackageClasses(packageName, false);
            LOG.info("num of jASN.1 classes: " + classes.size());
            LOG.finest(() -> {
                StringBuilder sb = new StringBuilder();
                classes.stream().forEach(c -> {
                    sb.append(c.getCanonicalName());
                    sb.append('\n');
                });
                return sb.toString();
            });

            processClasses(classes);
        }
        catch (Exception e)
        {
            throw new Exception("Could not load FR metadata from " + packageName, e);
        }
    }

    /**
     * Load FR meta data from the jASN compiler generated source classes
     * 
     * @param url The url where the source classes were generated
     * @throws Exception
     */
    public synchronized void loadFromBinaryClasses(URL url) throws Exception
    {
        try
        {
            // collect all classes from JASN.1 generated package
            LOG.info("looking for jASN.1 classes at " + url.getPath());
            List<Class<?>> classes = PackageUtils.getClasses(url, false);
            LOG.info("num of jASN.1 classes: " + classes.size());
            LOG.finest(() -> {
                StringBuilder sb = new StringBuilder();
                classes.stream().forEach(c -> {
                    sb.append(c.getCanonicalName());
                    sb.append('\n');
                });
                return sb.toString();
            });
            processClasses(classes);
        }
        catch (Exception e)
        {
            throw new Exception("Could not load FR metadata from " + url, e);
        }
    }

    /**
     * Load FR meta data from the jASN compiler generated source classes
     * 
     * @param url The url where the source classes were generated
     * @throws Exception
     */
    public synchronized void loadFromSourceClasses(URL url) throws Exception
    {
        try
        {
            // collect all classes from JASN.1 generated package
            LOG.info("looking for jASN.1 classes at " + url.getPath());
            List<Class<?>> classes = PackageUtils.getSourceClasses(url);
            LOG.info("num of jASN.1 classes: " + classes.size());
            LOG.finest(() -> {
                StringBuilder sb = new StringBuilder();
                classes.stream().forEach(c -> {
                    sb.append(c.getCanonicalName());
                    sb.append('\n');
                });
                return sb.toString();
            });
            processClasses(classes);
        }
        catch (Exception e)
        {
            throw new Exception("Could not load FR metadata from " + url, e);
        }
    }

    /**
     * Remove a string suffix
     * 
     * @param str The string w/ a suffix
     * @param suffix The suffix
     * 
     * @return the string w/o the suffix
     */
    private static String removeSuffix(String str, String suffix)
    {
        int idx = str.lastIndexOf(suffix);
        String ret = str;
        if (idx != -1)
        {
            ret = str.substring(0, idx);
        }
        return ret;
    }

    /**
     * Process a single OID found in the OID definition class.
     * The method collects FR, FR parameter, FR event and FR directives
     * OID names and their integer arrays
     * 
     * @param oidName The OID (JASN.1 generated class) name
     * @param oidArray The OID as an int array
     */
    private boolean processOid(String oidName, int[] oidArray)
    {
        boolean ret = false;

        this.oidName2oidArray.put(oidName, oidArray);
        this.oid2oidName.put(ObjectIdentifier.of(oidArray), oidName);
        String oidLabelName = removeSuffix(oidName, TYPE_SUFFIX);
        if (oidArray.length == OidTree.CROSS_FUNC_RES_BIT_LEN)
        {
            this.frOidName2oidArray.put(oidLabelName, oidArray);
            ret = true;
        }
        else if (oidArray.length == OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_LEN)
        {
            switch (oidArray[OidTree.CROSS_SUPP_FUNC_KIND_BIT_POS])
            {
            case OidTree.PARAM_BIT_VALUE:
                this.frParameterOidName2oidArray.put(oidLabelName, oidArray);
                ret = true;
                break;
            case OidTree.EVENT_BIT_VALUE:
                this.frEventOidName2oidArray.put(oidLabelName, oidArray);
                ret = true;
                break;
            case OidTree.DIREC_BIT_VALUE:
                this.frDirectiveOidName2oidArray.put(oidLabelName, oidArray);
                ret = true;
                break;
            default:
                break;
            }
        }

        return ret;
    }

    /**
     * Split classes to BER and non-BER class
     * 
     * @param classes The classes to be processed
     */
    private void collectBerClasses(List<Class<?>> classes)
    {
        // check whether a class implements the BerType IF
        this.berClasses.clear();

        for (Class<?> clazz : classes)
        {
            if (BerType.class.isAssignableFrom(clazz))
            {
                this.berClasses.put(clazz.getSimpleName(), clazz);
            }
            else
            {
                this.nonBerClasses.add(clazz);
            }
        }
    }

    private static String getParamOidNameFromBerClassName(String name)
    {
        return CSTSUtils.firstToLowerCase(name) + FR_PAR_OID_SUFFIX;
    }

    private static String getEventOidNameFromBerClassName(String name)
    {
        return CSTSUtils.firstToLowerCase(name) + FR_EV_OID_SUFFIX;
    }

    /**
     * Identifies whether the BER class is FR parameter or FR event
     * 
     * @param berClassEntry The BER class name (key) and BE class (value)
     * @param name 
     * @param oidName2oidArray
     * @param oid2oid2class
     * @return
     */
    private static boolean processBerClass(Entry<String, Class<?>> berClassEntry,
                                           Map<String, int[]> oidName2oidArray,
                                           Map<ObjectIdentifier, Map<ObjectIdentifier, Class<?>>> oid2oid2class,
                                           Type type)
    {
        boolean ret = false;

        String oidName;
        switch (type)
        {
        case PARAMETER:
            oidName = getParamOidNameFromBerClassName(berClassEntry.getKey());
            break;
        case EVENT:
            oidName = getEventOidNameFromBerClassName(berClassEntry.getKey());
            break;
        case DIRECTIVE:
        default:
            oidName = "";
            break;
        }

        int[] oidArray = oidName2oidArray.get(oidName);
        if (oidArray != null)
        {
            // find the FR to which the FR parameter or FR event belongs to
            ObjectIdentifier frOid = ObjectIdentifier.of(Arrays.copyOf(oidArray, OidTree.CROSS_FUNC_RES_BIT_LEN));
            Map<ObjectIdentifier, Class<?>> oid2Class = oid2oid2class.get(frOid);
            if (oid2Class == null)
            {
                // the first FR parameter or FR event of a FR
                // create map for the FR
                oid2Class = new HashMap<>();
                oid2oid2class.put(frOid, oid2Class);
            }

            // bind the FR parameter or FR event class with its OID
            ObjectIdentifier oid = ObjectIdentifier.of(oidName2oidArray.get(oidName));
            oid2Class.put(oid, berClassEntry.getValue());
            ret = true;
        }

        return ret;
    }

    /**
     * Find OIDs and bind BER classes with their OIDs and keep them in dedicated maps
     * 
     * @param classes The BER classes
     * @throws Exception
     */
    private void processClasses(List<Class<?>> classes) throws Exception
    {
        collectBerClasses(classes);

        // find the OidVals class w/ OIDs definitions
        Optional<Class<?>> opt = OidTree.findOidDefinitionClass(this.nonBerClasses);
        if (!opt.isPresent())
        {
            throw new Exception("missing an OID class (e.g. OidValues) in the generated FR types package");
        }

        OidTree.readOidValues(opt.get(), this::processOid);

        for (Entry<String, Class<?>> berClassEntry : this.berClasses.entrySet())
        {
            // try to process the BER class as an FR parameter
            if (!processBerClass(berClassEntry, this.frParameterOidName2oidArray, this.frOid2frParameterOidAndClass, Type.PARAMETER))
            {
                // try to process the BER class as an FR event
                processBerClass(berClassEntry, this.frEventOidName2oidArray, this.frOid2frEventOidAndClass, Type.EVENT);
            }
        }
    }

    /**
     * Create instances of {@IParameter} for provided
     * {@FunctionalResourceType} and its instance number
     * 
     * @param frType The {@FunctionalResourceType}
     * @param instanceNumber The instance number
     * @return the list of all FR parameters
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public FunctionalResourceParameterEx<?> createParameter(Name name) throws InstantiationException,
                                                                                       IllegalAccessException
    {
        FunctionalResourceType frType = name.getFunctionalResourceName().getType();
        Map<ObjectIdentifier, Class<?>> oid2Class = this.frOid2frParameterOidAndClass.get(frType.getOid());
        if (oid2Class == null)
        {
            throw new IllegalArgumentException(frType + " is not supported");
        }

        Class<?> berClass = oid2Class.get(name.getOid());
        if (berClass == null)
        {
            throw new IllegalArgumentException(name + " is not supported");
        }

        return new FunctionalResourceParameterEx(name.getOid(),
                                                 name.getFunctionalResourceName(),
                                                 berClass,
                                                 this.cstsValueFactory);
    }

    /**
     * Create instances of {@IParameter} for provided
     * {@FunctionalResourceType} and its instance number
     * 
     * @param frType The {@FunctionalResourceType}
     * @param instanceNumber The instance number
     * @return the list of all FR parameters
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<FunctionalResourceParameterEx<?>> createParameters(FunctionalResourceType frType,
                                                                   int instanceNumber) throws InstantiationException,
                                                                                       IllegalAccessException
    {
        List<FunctionalResourceParameterEx<?>> ret = new ArrayList<>();
        Map<ObjectIdentifier, Class<?>> oid2Class = this.frOid2frParameterOidAndClass.get(frType.getOid());
        if (oid2Class != null)
        {
            for (Entry<ObjectIdentifier, Class<?>> e : oid2Class.entrySet())
            {
                FunctionalResourceName frName = FunctionalResourceName.of(frType, instanceNumber);
                FunctionalResourceParameterEx<?> parameter = new FunctionalResourceParameterEx(e.getKey(),
                                                                                               frName,
                                                                                               e.getValue(),
                                                                                               this.cstsValueFactory);
                ret.add(parameter);
            }
        }
        return ret;
    }

    /**
     * Create instances of {@IEvent}} for provided {@FunctionalResourceType} and
     * its instance number
     * 
     * @param frType The {@FunctionalResourceType}
     * @param instanceNumber The instance number
     * @return the list of all FR events
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<FunctionalResourceEventEx<?>> createEvents(FunctionalResourceType frType,
                                                           int instanceNumber) throws InstantiationException,
                                                                               IllegalAccessException
    {
        List<FunctionalResourceEventEx<?>> ret = new ArrayList<>();
        Map<ObjectIdentifier, Class<?>> oid2Class = this.frOid2frEventOidAndClass.get(frType.getOid());
        if (oid2Class != null)
        {
            for (Entry<ObjectIdentifier, Class<?>> e : oid2Class.entrySet())
            {
                FunctionalResourceName frName = FunctionalResourceName.of(frType, instanceNumber);
                FunctionalResourceEventEx<?> parameter = new FunctionalResourceEventEx(e.getKey(),
                                                                                       frName,
                                                                                       e.getValue());
                ret.add(parameter);
            }
        }
        return ret;
    }

    /**
     * Create the OidConfig.xml configuration file w/ application OIDs (e.g. FR
     * OIDs)
     * 
     * @param oids The map containing e.g an FR or an FR parameter label
     * @param oidConfigFile The output configuration file for the CSTS API
     * @throws Exception 
     */
    public void createOidConfiguration(String oidConfigFile) throws Exception
    {
        String oidConfigPath = oidConfigFile.substring(0, oidConfigFile.lastIndexOf("/"));
        File directory = new File(oidConfigPath);
        directory.mkdirs();

        OidConfig oidConfig = new OidConfig();
        ArrayList<Oid> oidLabelList = new ArrayList<>();

        // add FR OIDs
        this.frOidName2oidArray.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
        // add FR parameter OIDs
        this.frParameterOidName2oidArray.entrySet().stream()
                .forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
        // add FR event OIDs
        this.frEventOidName2oidArray.entrySet().stream()
                .forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
        // add FR directive OIDs
        this.frDirectiveOidName2oidArray.entrySet().stream()
                .forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));

        // set sizes
        oidConfig.setNumFrOids(this.frOidName2oidArray.size());
        oidConfig.setNumFrParameterOids(this.frParameterOidName2oidArray.size());
        oidConfig.setNumFrEventOids(this.frEventOidName2oidArray.size());
        oidConfig.setNumFrDirectiveOids(this.frDirectiveOidName2oidArray.size());

        oidConfig.setOidLabelList(oidLabelList);
        oidConfig.save(oidConfigFile);
    }

    private String makeFrTypeName(String frOid, boolean firstUpperCase)
    {
        return (firstUpperCase)? CSTSUtils.firstToUpperCase(removeSuffix(frOid, FR_OID_SUFFIX)):
            CSTSUtils.firstToLowerCase(removeSuffix(frOid, FR_OID_SUFFIX));
    }

    /**
     * Create the FR types class files from collected OIDs
     * 
     * @param dir The directory where the class file name will be created
     * @throws IOException
     */
    public void createFrTypesClassFile(String dir) throws IOException
    {
        FrWriter frTypesBuilder = new FrWriter();

        new File(dir).mkdirs();
        String packageName = dir.substring(dir.indexOf(JAVA) + JAVA.length() + 1);
        packageName = packageName.replace("/", ".");
        frTypesBuilder.addPrologue(packageName, FR_TYPES_CLASS_NAME);

        // FRs
        for (Entry<String, int[]> frEntry : this.frOidName2oidArray.entrySet())
        {
            frTypesBuilder.addItem(makeFrTypeName(frEntry.getKey(), false),
                                   "OIDs.crossSupportFunctionalities",
                                   frEntry.getValue()[OidTree.CROSS_FUNC_RES_BIT_POS]);
        }

        // FR parameters and FR events
        for (Entry<String, int[]> frEntry : this.frOidName2oidArray.entrySet())
        {
            ObjectIdentifier frOid = ObjectIdentifier.of(frEntry.getValue());
            Map<ObjectIdentifier, Class<?>> oid2parClass = this.frOid2frParameterOidAndClass.get(frOid);
            Map<ObjectIdentifier, Class<?>> oid2evClass = this.frOid2frEventOidAndClass.get(frOid);
            if ((oid2parClass != null && !oid2parClass.isEmpty()) || (oid2evClass != null && !oid2evClass.isEmpty()))
            {
                frTypesBuilder.addNestedClass(makeFrTypeName(frEntry.getKey(), true));

                if (oid2parClass != null)
                {
                    frTypesBuilder.addNestedClass("parameter");

                    for (Entry<ObjectIdentifier, Class<?>> frParEntry : oid2parClass.entrySet())
                    {
                        String oidName = removeSuffix(this.oid2oidName.get(frParEntry.getKey()), TYPE_SUFFIX);
                        int[] oidArray = frParEntry.getKey().toArray();
                        frTypesBuilder.addItem(oidName,
                                               "OIDs.crossSupportFunctionalities",
                                               OidTree.CROSS_FUNC_RES_BIT_POS,
                                               oidArray);
                    }

                    frTypesBuilder.finalizeNestedClass();
                }

                if (oid2evClass != null)
                {
                    frTypesBuilder.addNestedClass("event");

                    for (Entry<ObjectIdentifier, Class<?>> frEvEntry : oid2evClass.entrySet())
                    {
                        String oidName = removeSuffix(this.oid2oidName.get(frEvEntry.getKey()), TYPE_SUFFIX);
                        int[] oidArray = frEvEntry.getKey().toArray();
                        frTypesBuilder.addItem(oidName,
                                               "OIDs.crossSupportFunctionalities",
                                               OidTree.CROSS_FUNC_RES_BIT_POS,
                                               oidArray);
                    }

                    frTypesBuilder.finalizeNestedClass();
                }

                frTypesBuilder.finalizeNestedClass();
            }
        }

        frTypesBuilder.addEpilogue();
        frTypesBuilder.createFrTypesClassFile(dir);
    }

    /**
     * Change CSTS value factory 
     * NOTE: It must be invoked prior to creation of FR parameters or event
     * @param cstsValueFactory
     */
    public void setCstsValueFactory(ICstsValueFactory cstsValueFactory)
    {
        this.cstsValueFactory = cstsValueFactory;
    }

    /**
     * Creates the Fr class and/or the OidConfig.xml file 
     * @param args The arguments:
     *  -g <source package name with jASN.1 generated classes> mandatory argument 
     *  -d <output directory for OidConfig.xml> optional argument
     *  -f <output package for Fr class> optional argument
     */
    public static void main(String[] args) throws Exception 
    {
        try
        {
            boolean expectedSourcePath = false;
            boolean expectedOutputOidConfigFile = false;
            boolean expectedOutputFrDirectory = false;

            URL classesUrl = null;
            String outputOidConfigFileName = null;
            String outputFrDirectoryName = null;

            for (String arg : args)
            {
                LOG.fine("arg: " + arg);
                if (arg == null)
                {
                    continue;
                }

                switch (arg)
                {
                case "-p":
                    expectedSourcePath = true;
                    break;
                case "-c":
                    expectedOutputOidConfigFile = true;
                    break;
                case "-f":
                    expectedOutputFrDirectory = true;
                    break;
                default:
                    if (expectedSourcePath)
                    {
                        classesUrl = new URL("file", null, arg);
                    }
                    else if (expectedOutputOidConfigFile)
                    {
                        outputOidConfigFileName = arg;
                    }
                    else if (expectedOutputFrDirectory)
                    {
                        outputFrDirectoryName = arg;
                    }
                    // reset previous option
                    expectedSourcePath = false;
                    expectedOutputOidConfigFile = false;
                    expectedOutputFrDirectory = false;
                    break;
                }
            }

            LOG.fine("classesUrl: " + classesUrl);
            LOG.fine("outputOidConfigFileName: " + outputOidConfigFileName);
            LOG.fine("outputPackageName: " + outputFrDirectoryName);

            if (classesUrl != null && (outputOidConfigFileName != null || outputFrDirectoryName != null))
            {
                // load FR meta data from jASN.1 compiler generated classes
                FunctionalResourceMetadata.getInstance().loadFromBinaryClasses(classesUrl);

                if (outputOidConfigFileName != null)
                {
                    // create the OidConfig.xml file in the output directory
                    FunctionalResourceMetadata.getInstance().createOidConfiguration(outputOidConfigFileName);
                }

                if (outputFrDirectoryName != null)
                {
                    // create the Fr class in the output package
                    FunctionalResourceMetadata.getInstance().createFrTypesClassFile(outputFrDirectoryName);
                }
            }
            else
            {
                printOptions();
            }
        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "Failed to create the OidConfig and/or FR class", e);
            System.exit(1);
        }
    }

    private static void printOptions()
    {
        System.out.println("FR metadata arguments:");
        System.out.println("======================");
        System.out.println("-p <path to directory with jASN.1 generated classes>, mandatory argument");
        System.out.println("-c <path file name where to generate the OID XML config file>, optional argument");
        System.out.println("-f <output directory for the Fr class>, optional argument");
    }

    public static ObjectIdentifier getFrOid(ObjectIdentifier oid)
    {
        return ObjectIdentifier.of(Arrays.copyOf(oid.toArray(), OidTree.CROSS_FUNC_RES_BIT_LEN));
    }

    /**
     * Convert FR parameter stored in a qualified parameter value to string
     * @param qualifiedParameter The qualified parameter w/ extended type
     * @return String
     */
    public String toString(QualifiedParameter qualifiedParameter)
    {
        String ret = null;

        Name name = qualifiedParameter.getName();
        try
        {
            FunctionalResourceParameterEx<?> frPar = parameterCache.get(name);
            if (frPar == null)
            {
                frPar = createParameter(qualifiedParameter.getName());
                parameterCache.put(name, frPar);
            }
            frPar.setValue(qualifiedParameter);
            ret = frPar.valueToString();
        }
        catch (Exception e)
        {
            // parameter is not available so it cannot be converted, ignore it
        }

        return ret;
    }
}
