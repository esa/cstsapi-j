package esa.egos.csts.api.util.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides static methods to read ASN.1 files and extract all
 * defined Object Identifiers. These Object Identifiers are used to generate a
 * new Java class, fitting into the structure of the CSTS API.
 */
public class OIDReader {

	private static final Pattern PATTERN = Pattern.compile("\\w+\\s+OBJECT\\s+IDENTIFIER\\s+::=\\s+\\{[\\w\\s]+\\}");
	
	/**
	 * Generates a new Java class for the CSTS API and defines all Object Identifier
	 * of the specified ASN.1 file in a hierarchical representation.
	 * 
	 * The generated Java class will specify the Object Identifiers in the following
	 * notation:
	 * 
	 * public static final ObjectIdentifier css = ObjectIdentifier.of(1, 3, 112, 4, 4);
	 * public static final ObjectIdentifier csts = ObjectIdentifier.of(css, 1);
	 * public static final ObjectIdentifier crossSupportResources = ObjectIdentifier.of(css, 2);
	 * public static final ObjectIdentifier framework = ObjectIdentifier.of(csts, 1);
	 * public static final ObjectIdentifier services = ObjectIdentifier.of(csts, 2);
	 * ...
	 * 
	 * @param inputFilePath
	 *            the path of the ASN.1 file specifying all Object Identifiers to be
	 *            read
	 * @param outputFilePath
	 *            the path of the Java class containing all Object Identifiers which
	 *            will be written
	 * @throws IOException
	 *             if an I/O error occurs reading from the stream or writing to or
	 *             creating the file
	 */
	public static void generateHierarchical(String inputFilePath, String outputFilePath) throws IOException {

		List<List<String>> tokens = new ArrayList<>();

		String fileString = new String(Files.readAllBytes(Paths.get(inputFilePath)));
		Matcher matcher = PATTERN.matcher(fileString);

		while (matcher.find()) {
			tokens.add(
					Arrays.asList(matcher.group()
							.replaceAll("OBJECT", "")
							.replaceAll("IDENTIFIER", "")
							.replaceAll("::=", "")
							.replaceAll("\\{", "")
							.replaceAll("\\}", "")
							.split("\\s+")));
		}

		StringBuilder sb = new StringBuilder();
		sb.append("package esa.egos.csts.api.oids;");
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append("/**");
		sb.append(System.getProperty("line.separator"));
		sb.append(" * This class represents all predefined Object Identifiers of the CSTS.");
		sb.append(System.getProperty("line.separator"));
		sb.append(" */");
		sb.append(System.getProperty("line.separator"));
		sb.append("public class OIDs {");
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		for (List<String> list : tokens) {
			sb.append("\tpublic static final ObjectIdentifier ");
			sb.append(list.get(0));
			sb.append(" = ObjectIdentifier.of(");
			list.subList(1, list.size()).forEach(s -> sb.append(s + ", "));
			sb.delete(sb.length() - 2, sb.length());
			sb.append(");");
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("}");

		Files.write(Paths.get(outputFilePath), sb.toString().getBytes());
	}

	/**
	 * Generates a new Java class for the CSTS API and defines all Object Identifier
	 * of the specified ASN.1 file in a flat representation.
	 * 
	 * The generated Java class will specify the Object Identifiers in the following
	 * notation:
	 * 
	 * public static final ObjectIdentifier css = ObjectIdentifier.of(1, 3, 112, 4, 4);
	 * public static final ObjectIdentifier csts = ObjectIdentifier.of(1, 3, 112, 4, 4, 1);
	 * public static final ObjectIdentifier crossSupportResources = ObjectIdentifier.of(1, 3, 112, 4, 4, 2);
	 * public static final ObjectIdentifier framework = ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 1);
	 * public static final ObjectIdentifier services = ObjectIdentifier.of(1, 3, 112, 4, 4, 1, 2);
	 * ...
	 * 
	 * @param inputFilePath
	 *            the path of the ASN.1 file specifying all Object Identifiers to be
	 *            read
	 * @param outputFilePath
	 *            the path of the Java class containing all Object Identifiers which
	 *            will be written
	 * @throws IOException
	 *             if an I/O error occurs reading from the stream or writing to or
	 *             creating the file
	 */
	public static void generateFlat(String inputFilePath, String outputFilePath) throws IOException {

		List<List<String>> tokens = new ArrayList<>();

		String fileString = new String(Files.readAllBytes(Paths.get(inputFilePath)));
		Matcher matcher = PATTERN.matcher(fileString);

		while (matcher.find()) {
			tokens.add(
					Arrays.asList(matcher.group()
							.replaceAll("OBJECT", "")
							.replaceAll("IDENTIFIER", "")
							.replaceAll("::=", "")
							.replaceAll("\\{", "")
							.replaceAll("\\}", "")
							.split("\\s+")));
		}

		Map<String, List<String>> OIDs = new LinkedHashMap<>();
		tokens.stream()
		.forEachOrdered(l -> OIDs.put(l.get(0),
				Stream.concat(
						l.subList(1, l.size()).stream()
						.filter(s -> !s.matches("\\d+"))
						.map(OIDs::get)
						.flatMap(List::stream),
						l.subList(1, l.size()).stream()
						.filter(s -> s.matches("\\d+")))
				.collect(Collectors.toCollection(ArrayList::new))));

		StringBuilder sb = new StringBuilder();
		sb.append("package esa.egos.csts.api.oids;");
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		sb.append("/**");
		sb.append(System.getProperty("line.separator"));
		sb.append(" * This class represents all predefined Object Identifiers of the CSTS.");
		sb.append(System.getProperty("line.separator"));
		sb.append(" */");
		sb.append(System.getProperty("line.separator"));
		sb.append("public class OIDs {");
		sb.append(System.getProperty("line.separator"));
		sb.append(System.getProperty("line.separator"));
		for (Entry<String, List<String>> e : OIDs.entrySet()) {
			sb.append("\tpublic static final ObjectIdentifier ");
			sb.append(e.getKey());
			sb.append(" = ObjectIdentifier.of(");
			e.getValue().forEach(s -> sb.append(s + ", "));
			sb.delete(sb.length() - 2, sb.length());
			sb.append(");");
			sb.append(System.getProperty("line.separator"));
		}
		sb.append(System.getProperty("line.separator"));
		sb.append("}");

		Files.write(Paths.get(outputFilePath), sb.toString().getBytes());
	}

}