package esa.egos.csts.test.mdslite.impl.simulator.frm;

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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

	private static final String PARAM_EVENT_SUFFIX = "Type";

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
	 * Load FR metadata from the jASN compiler generated classes
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
		Map<String, int[]> paramEventOids = collectOids(this.oidClass, BerObjectIdentifier.class);
		System.out.println("OIDs size: " + paramEventOids.size());

		// FRs OIDs from hard coded map {FR-bit number, FR label}
		Map<String, int[]> frOids = findCrossSupportFunctionalities2(paramEventOids, BitLabels.FrMap);

		// OIDs of FRs + FR params + FR events + FR directives
		frOids.putAll(paramEventOids);

		// OIDs configuration
		createOidConfiguration(frOids, "src/test/resources/OidConfig.xml");

		// class -> ObjectIdentifier map
		buildClass2Oid(this.berClasses, frOids);
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
						System.out.println(
								fieldName + " " + Arrays.stream(oidArray).boxed().collect(Collectors.toList()));
					}
				}
			}
		}
		return ret;
	}

	private static String findCommonName(String firstName, String secondName) {
		StringBuilder commonName = new StringBuilder();
		int n = (firstName.length() < secondName.length()) ? firstName.length() : secondName.length();
		for (int i = 0; i < n; i++) {
			char ch = firstName.charAt(i);
			if (ch == secondName.charAt(i)) {
				commonName.append(ch);
			} else {
				break;
			}
		}

		return commonName.toString();
	}

	private static List<Integer> findCommonOid(int[] firstOid, int[] secondOid) {
		List<Integer> ret = new ArrayList<>();
		int n = (firstOid.length < secondOid.length) ? firstOid.length : secondOid.length;
		for (int i = 0; i < n; i++) {
			int oidBit = firstOid[i];
			if (oidBit == secondOid[i]) {
				ret.add(oidBit);
			} else {
				break;
			}
		}

		return ret;
	}

	/**
	 * Find CSTS FR OIDs and labels from OIDs
	 * @param oids The map all parameter and event OIDs
	 * @param frOidBit2FrLabel The map of FR OID bits and FR labels
	 * @return The map of FR labels mapped to FR OIDs
	 */
	public static Map<String, int[]> findCrossSupportFunctionalities2(Map<String, int[]> oids, Map<Integer, String> frOidBit2FrLabel) {
		LinkedHashMap<String, int[]> ret = new LinkedHashMap<String, int[]>();
		for (Entry<String, int[]> oidEntry : oids.entrySet()) {

			int[] frOidArray =  IntStream.of(oidEntry.getValue()).limit(OidTree.CROSS_SUPP_FUNC_TYPE_BIT_POS).toArray();
			if ( ret.entrySet().stream().filter(e -> e.getValue().equals(frOidArray)).findFirst().isPresent())
			{
				continue;
			}

			String frLabel = frOidBit2FrLabel.get(frOidArray[OidTree.CROSS_FUNC_RES_BIT_POS]);
			if (frLabel != null)
			{
				ret.put(frLabel, frOidArray);
			}
			else
			{
				System.out.println("Could not find a FR label for FR bit (" + frOidArray[OidTree.CROSS_FUNC_RES_BIT_POS] + "), ignore it");
			}
		}
		return ret;
	}

	/**
	 * Find CSTS FR from common parameter and event prefixes with respect their OIDs
	 * @param oids The parameter and event names mapped to their OIDs
	 * @return common parameter and event prefixes, ~FR names (e.g. ant ~ Antenna) mapped to FR OIDs
	 */
	public static Map<String, int[]> findCrossSupportFunctionalities(Map<String, int[]> oids) {
		Map<String, int[]> ret = new HashMap<String, int[]>();
		for (Entry<String, int[]> oidEntryA : oids.entrySet()) {
			for (Entry<String, int[]> oidEntryB : oids.entrySet()) {
				if (oidEntryA.equals(oidEntryB)) {
					continue;
				}
				String commonName = findCommonName(oidEntryA.getKey(), oidEntryB.getKey());
				if (!commonName.isEmpty()) {
					List<Integer> commonOid = findCommonOid(oidEntryA.getValue(), oidEntryB.getValue());
					if (commonOid.size() >= OidTree.CROSS_FUNC_RES_BIT_POS) {
						commonOid = commonOid.stream().limit(OidTree.CROSS_FUNC_RES_BIT_POS).collect(Collectors.toList());
						int[] oid = commonOid.stream().mapToInt(Integer::intValue).toArray();
						Optional<Entry<String, int[]>> op = ret.entrySet().stream()
								.filter(e -> Arrays.equals(e.getValue(), oid)).findFirst();
						if (op.isPresent()) {
							Entry<String, int[]> e = op.get();
							if (e.getKey().length() > commonName.length()) {
								// System.out.println("replacing " + e.getKey()
								// + " with " + commonName + " " +
								// Arrays.toString(oid));
								ret.remove(e.getKey());
								ret.put(commonName, oid);
							}
						} else {
							// System.out.println("adding " + commonName +
							// Arrays.toString(oid));
							ret.put(commonName, oid);
						}
					}
				}
			}
		}
		return ret;
	}

	private static String getOidNameFromClassName(Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		String className = clazz.getSimpleName();
		sb.append(className.substring(0, 1).toUpperCase());
		sb.append(className.substring(1));
		sb.append(PARAM_EVENT_SUFFIX);
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
		OidConfig oidConfig = new OidConfig();
		ArrayList<Oid> oidLabelList = new ArrayList<>();
		oids.entrySet().stream().forEach(e -> oidLabelList.add(new Oid(e.getValue(), removeSuffix(e.getKey(), PARAM_EVENT_SUFFIX))));
		oidConfig.setOidLabelList(oidLabelList);
		oidConfig.save(oidConfigFile);
	}
}
