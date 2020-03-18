package esa.egos.csts.sim.impl.frm;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.beanit.jasn1.ber.types.BerObjectIdentifier;
import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.oids.OidTree;
import esa.egos.proxy.xml.OidConfig;
import esa.egos.proxy.xml.Oid;

public class FunctionalResourceMetadata {

	private static FunctionalResourceMetadata instance = null;

	private static Object lock = new Object();

	private static final String CLASS_FILE_SUFFIX = ".class";

	private static final String TYPE_SUFFIX = "Type";

	private List<Class<?>> berClasses = new ArrayList<Class<?>>();

	private Class<?> oidClass = null;

	private FunctionalResourceMetadata() {
		this.berClasses = new ArrayList<Class<?>>();
	}

	public static FunctionalResourceMetadata getInstance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new FunctionalResourceMetadata();
				}
			}
		}

		return instance;
	}

	/**
	 * Load FR meta data from the jASN compiler generated classes
	 *  
	 * @param packageName The package name containing the generated classes
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public synchronized void load(String packageName) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {

		// collect all classes from the generated package
		List<Class<?>> classes = getLocalPackageClasses(packageName, false);

		// check whether a class implements the BerType IF
		this.berClasses.clear();
		List<Class<?>> notBerClasses = new ArrayList<Class<?>>();

		for (Class<?> clazz : classes) {
			if (BerType.class.isAssignableFrom(clazz)) {
				this.berClasses.add(clazz);
			} else {
				notBerClasses.add(clazz);
			}
		}

		// find the OidVals class w/ OIDs definitions 
		this.oidClass = findOidDefinitionClass(notBerClasses);

		this.berClasses.stream().forEach(clazz -> System.out.println(clazz.getCanonicalName()));

		System.out.println("ber classes size: " + this.berClasses.size());
		System.out.println(notBerClasses);
		System.out.println(notBerClasses.size());
		System.out.println(this.oidClass);

		// OIDs from the generated OidVals class
		Map<String, int[]> oids = collectOids(this.oidClass, BerObjectIdentifier.class);
		System.out.println("OIDs size: " + oids.size());

		// OIDs configuration
		createOidConfiguration(oids, "src/test/resources/OidConfig.xml");

		// class -> ObjectIdentifier map
		buildClass2Oid(this.berClasses, oids);
	}

	/**
	 * Assess whether the class contains the OID definitions
	 * 
	 * @param clazz
	 * @return
	 */
	private static boolean isOidDefinitionClass(Class<?> clazz) {
		return (clazz.getSimpleName().toLowerCase().contains("oid"));
	}

	private static Class<?> findOidDefinitionClass(List<Class<?>> classes) {
		return classes.stream().filter(clazz -> isOidDefinitionClass(clazz)).findFirst().get();
	}

	private static int getDotCount(final String str) {
		return str.length() - str.replaceAll("[.]", "").length();
	}

	public List<Class<?>> getLocalPackageClasses(String packageName, boolean alsoNestedClasses)
			throws ClassNotFoundException {
		List<Class<?>> ret = new ArrayList<Class<?>>();
		File packageDirectory = new File(
				getClass().getClassLoader().getResource(packageName.replace('.', '/')).getFile());
		if (packageDirectory.exists()) {
			int packageNameSegmentCount = getDotCount(packageName);
			for (String fileName : packageDirectory.list()) {
				if (fileName.endsWith(CLASS_FILE_SUFFIX)) {
					Class<?> clazz = Class.forName(
							packageName + "." + fileName.substring(0, fileName.length() - CLASS_FILE_SUFFIX.length()));
					if (!alsoNestedClasses) {
						if ((packageNameSegmentCount + 1) != getDotCount(clazz.getCanonicalName())) {
							// ignore nested classes
							continue;
						}
					}
					ret.add(clazz);
				}
			}
		}

		return ret;
	}

	public static Map<String, int[]> collectOids(Class<?> oidClass, Class<?> oidContainerClass) throws IllegalArgumentException, IllegalAccessException
			 {
		Map<String, int[]> ret = new LinkedHashMap<String, int[]>();
		for (Field field : oidClass.getFields()) {
			Class<?> clazz = field.getType();
			if (Modifier.isStatic(field.getModifiers()) && oidContainerClass.isAssignableFrom(clazz)) {
				String fieldName = field.getName();
				Object obj = field.get(null);
				for (Field nestedField : obj.getClass().getFields()) {
					if (int[].class.isAssignableFrom(nestedField.getType())) {
						int[] oidArray = int[].class.cast(nestedField.get(obj));
						ret.put(fieldName, oidArray);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Find CSTS FR OIDs and labels from OIDs
	 * @param oids The map all parameter and event OIDs
	 * @return The map of FR labels mapped to FR OIDs
	 */
	public static Map<String, int[]> findCrossSupportFunctionalities(Map<String, int[]> oids) {
		LinkedHashMap<String, int[]> ret = new LinkedHashMap<String, int[]>();
		for (Entry<String, int[]> oidEntry : oids.entrySet()) {
			if (oidEntry.getValue().length == OidTree.CROSS_FUNC_RES_BIT_POS + 1) {
				ret.put(removeSuffix(oidEntry.getKey(), TYPE_SUFFIX), oidEntry.getValue());
			}
		}
		return ret;
	}

	/**
	 * Find CSTS FR parameters, events or directives
	 * @param oids The map all parameter and event OIDs
	 * @param kind The functional resource parameter(1), event(2), directive(3)
	 * @return The map of FR parameters, events or directives labels mapped to FR OIDs
	 */
	public static Map<String, int[]> findFunctionalResource(Map<String, int[]> oids, int type) {
		LinkedHashMap<String, int[]> ret = new LinkedHashMap<String, int[]>();
		for (Entry<String, int[]> oidEntry : oids.entrySet()) {
			if (oidEntry.getValue().length >= OidTree.CROSS_SUPP_FUNC_KIND_BIT_POS + 1
					&& oidEntry.getValue()[OidTree.CROSS_SUPP_FUNC_KIND_BIT_POS] == type) {
				ret.put(removeSuffix(oidEntry.getKey(), TYPE_SUFFIX), oidEntry.getValue());
			}
		}
		return ret;
	}


	private static String getOidNameFromClassName(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		String className = clazz.getSimpleName();
		sb.append(className.substring(0, 1).toUpperCase());
		sb.append(className.substring(1));
		sb.append(TYPE_SUFFIX);
		return sb.toString();
	}

	private static Entry<Map<Class<?>, ObjectIdentifier>, Map<Class<?>, ObjectIdentifier>> buildClass2Oid(
			List<Class<?>> berClasses, Map<String, int[]> oids) {
		Map<Class<?>, ObjectIdentifier> parameters = new HashMap<>();
		Map<Class<?>, ObjectIdentifier> events = new HashMap<>();
		Entry<Map<Class<?>, ObjectIdentifier>, Map<Class<?>, ObjectIdentifier>> ret =
				new AbstractMap.SimpleEntry<Map<Class<?>, ObjectIdentifier>, Map<Class<?>, ObjectIdentifier>>(
				parameters, events);
		for (Class<?> clazz : berClasses) {
			String oidName = getOidNameFromClassName(clazz);
			int[] oidArray = oids.get(oidName);
			if (oidArray != null)
			{
				switch(oidArray[OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_POS])
				{
				case OidTree.PARAM_BIT_VALUE:
					parameters.put(clazz, ObjectIdentifier.of(oidArray));
					break;
				case OidTree.EVENT_BIT_VALUE:
					events.put(clazz, ObjectIdentifier.of(oidArray));
					break;
				case OidTree.DIREC_BIT_VALUE:
					System.out.println("directives are not supported (" + oidArray[OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_POS] 
							+ ") at pos (" + OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_POS + "), " + Arrays.toString(oidArray));
					break;
				default:
					System.out.println("unknown bit value (" + oidArray[OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_POS] 
							+ ") at pos (" + OidTree.PARAM_OR_EVENT_OR_DIRECT_BIT_POS + "), " + Arrays.toString(oidArray));
					break;
				}
			}
		}

		return ret;
	}

	public static String removeSuffix(String str, String suffix) {
		int idx = str.lastIndexOf(suffix);
		String ret = str;
		if (idx != -1)
		{
			ret = str.substring(0, idx);
		}
		return ret;
	}

	/**
	 * Create the OidConfig.xml configuration file w/ application OIDs (e.g. FR OIDs)
	 * 
	 * @param oids The map containing e.g an FR or an FR parameter label 
	 * @param oidConfigFile The output configuration file for the CSTS API
	 */
	public static void createOidConfiguration(Map<String, int[]> oids, String oidConfigFile) {

		Map<String, int[]> frOids = FunctionalResourceMetadata.findCrossSupportFunctionalities(oids);
		Map<String, int[]> frParameterOids = FunctionalResourceMetadata.findFunctionalResource(oids, OidTree.PARAM_BIT_VALUE);
		Map<String, int[]> frEventOids = FunctionalResourceMetadata.findFunctionalResource(oids, OidTree.EVENT_BIT_VALUE);
		Map<String, int[]> frDirectiveOids = FunctionalResourceMetadata.findFunctionalResource(oids, OidTree.DIREC_BIT_VALUE);

		OidConfig oidConfig = new OidConfig();
		ArrayList<Oid> oidLabelList = new ArrayList<>();

		// add FR OIDs
		frOids.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
		// add FR parameter OIDs
		frParameterOids.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
		// add FR event OIDs
		frEventOids.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));
		// add FR directive OIDs
		frDirectiveOids.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), e.getKey())));

		// set sizes
		oidConfig.setNumFrOids(frOids.size());
		oidConfig.setNumFrParameterOids(frParameterOids.size());
		oidConfig.setNumFrEventOids(frEventOids.size());
		oidConfig.setNumFrDirectiveOids(frDirectiveOids.size());

		oidConfig.setOidLabelList(oidLabelList);
		oidConfig.save(oidConfigFile);
	}
}
