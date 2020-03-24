package esa.egos.csts.sim.impl.frm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Creates the FR types class file
 */
public class FrTypesClassBuilder
{
    private static final String INDENTATION = "    ";

    /** the class name */
    private String className;

    /** the lines of the class file */
    private List<String> fileLines;

    /** the flag indicating in a nested class */
    private int nestedClassDeepness;


    public FrTypesClassBuilder()
    {
        this.fileLines = new ArrayList<>();
        this.className = null;
        this.nestedClassDeepness = 0;
    }

    /**
     * Add the prologue of the class file
     * 
     * @param packageName The package name
     * @param className The class name
     */
    public void addPrologue(String packageName, String className)
    {
        this.fileLines.add("package " + packageName + ";\n");
        this.fileLines.add("import esa.egos.csts.api.oids.ObjectIdentifier;");
        this.fileLines.add("import esa.egos.csts.api.oids.OIDs;");
        this.fileLines.add("import esa.egos.csts.api.functionalresources.FunctionalResourceType;\n");
        this.fileLines.add("public class " + className + " \n{");
        this.className = className;
    }

    public void addNestedClass(String className)
    {
        this.nestedClassDeepness++;
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < this.nestedClassDeepness; i++)
        {
            sb.append(INDENTATION);
        }
        sb.append("public static class " + className + " \n");
        for (int i = 0; i < this.nestedClassDeepness; i++)
        {
            sb.append(INDENTATION);
        }
        sb.append("{");
        this.fileLines.add(sb.toString());
    }

    public void finalizeNestedClass()
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.nestedClassDeepness; i++)
        {
            sb.append(INDENTATION);
        }
        sb.append("}");
        this.fileLines.add(sb.toString());
        this.nestedClassDeepness--;
    }

    /**
     * Add a new FunctionalResourceType item(FR) to the class file
     * 
     * @param oidLabelName FR label name
     * @param oidBitNumber FR bit number
     */
    public void addItem(String oidLabelName, String parentOidReference, int oidBitNumber)
    {
        StringBuilder sb = new StringBuilder(INDENTATION);
        for (int i = 0; i < this.nestedClassDeepness; i++)
        {
            sb.append(INDENTATION);
        }
        sb.append("public static final FunctionalResourceType ");
        sb.append(oidLabelName);
        sb.append(" = FunctionalResourceType.of(ObjectIdentifier.of(");
        sb.append(parentOidReference);
        sb.append(", ");
        sb.append(oidBitNumber);
        sb.append("));");
        this.fileLines.add(sb.toString());
    }

    /**
     * Add a new FunctionalResourceType item(FR parameter or event) to the class
     * file
     * 
     * @param oidLabelName FR label name
     * @param oidBitNumber FR bit number
     */
    public void addItem(String oidLabelName, String parentOidReference, int start, int... oidBitNumbers)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= this.nestedClassDeepness; i++)
        {
            sb.append(INDENTATION);
        }
        sb.append("public static final ObjectIdentifier ");
        sb.append(oidLabelName);
        sb.append(" = ObjectIdentifier.of(");
        sb.append(parentOidReference);
        sb.append(", ");
        for (int i = start; i < oidBitNumbers.length; i++)
        {
            if (i != start)
            {
                sb.append(", ");
            }
            sb.append(oidBitNumbers[i]);
        }
        sb.append(");");
        this.fileLines.add(sb.toString());
    }

    /**
     * Add the epilogue of the class file
     */
    public void addEpilogue()
    {
        this.fileLines.add("}");
    }

    /**
     * Create the FR types class file
     * 
     * @param dir The directory where the class file name will be created
     * 
     * @throws IOException
     */
    public void createFrTypesClassFile(String dir) throws IOException 
    {
        StringBuilder sb = new StringBuilder(dir);
        sb.append("/");
        sb.append(className);
        sb.append(".java");
        Files.write(Paths.get(sb.toString()), this.fileLines, StandardCharsets.UTF_8);
    }
}
