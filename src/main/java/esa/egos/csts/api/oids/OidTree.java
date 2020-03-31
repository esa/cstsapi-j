package esa.egos.csts.api.oids;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * Represents an OID tree to translate numeric OIDs to OIDs with labels attached
 * to the numeric OID bits.
 */
public class OidTree {

	private static final Logger LOG = Logger.getLogger(OidTree.class.getName());

	/** The strict flag indicating that all parents must exist on addition of a child node */
	public static final boolean PARENT_NODES_MUST_EXIST = false;
	/** The position of the cross support functionalities bit in an integer array */
	public static final int CROSS_SUPP_FUNC_BIT_POS = 6;
	/** The position of the cross support functionalities bit in an integer array */
	public static final int CROSS_SUPP_FUNC_BIT_VALUE = 1;
	/** The position of the cross support functional resource bit in an integer array */
	public static final int CROSS_FUNC_RES_BIT_POS = 7;
	/** The position of the cross support functionality type bit (a parameter or an event or a directive) in an integer array */
	public static final int CROSS_SUPP_FUNC_KIND_BIT_POS = 8;
	/** The position of the parameter, event or directive bit in an integer array */
	public static final int PARAM_OR_EVENT_OR_DIRECT_BIT_POS = 9;
	/** The position of the parameter, event or a directive version bit in an integer array */
	public static final int PARAM_OR_EVENT_OR_DIRECT_VERSION_BIT_POS = 10;
	/** The position of the parameter, event or a directive qualifier bit in an integer array */
	public static final int PARAM_OR_EVENT_OR_DIRECT_QUALIFIER_BIT_POS = 11;
	/** The value of the parameter type bit in an integer array */
	public static final int PARAM_BIT_VALUE = 1;
	/** The value of the event type bit in an integer array */
	public static final int EVENT_BIT_VALUE = 2;
	/** The value of the directive type bit in an integer array */
	public static final int DIREC_BIT_VALUE = 3;
	/** The length of a cross support functionality resource integer array */
	public static final int CROSS_FUNC_RES_BIT_LEN = CROSS_FUNC_RES_BIT_POS + 1;
	/** The length of a cross support functionality integer array */
	public static final int CROSS_SUPP_FUNC_KIND_BIT_LEN = CROSS_SUPP_FUNC_KIND_BIT_POS + 1;
    /** The length of the parameter, event or directive bit integer array */
    public static final int PARAM_OR_EVENT_OR_DIRECT_BIT_LEN = PARAM_OR_EVENT_OR_DIRECT_QUALIFIER_BIT_POS + 1;

	/** The OID tree singlton */
	private static OidTree instance;
	/** The OID tree singlton lock */
	private static Object lock = new Object();

	/** The root node of the OID tree */
	private OidNode rootNode;

	/**
	 * Private constructor for initialisation
	 */
	private OidTree() {
		initialise();
	}

	/**
	 * Provides access to the OID tree.
	 * 
	 * @return the instance of the OID tree
	 */
	public static synchronized OidTree getInstance() {
		if (instance == null)
			synchronized (lock) {
				if (instance == null) {
					instance = new OidTree();
				}
			}
		return instance;
	}

	/**
	 * Initialisation of the Functional Resource OIDs from the OIDs class
	 */
	private void initialise() {
		this.rootNode = new OidNode(1, "iso");
		OidNode ccsdsNode = this.rootNode.addChildNode(3, "identifiedOrganisation").addChildNode(112, "standard")
				.addChildNode(4, "ccsds");
		ccsdsNode.addChildNode(6, "facility").addChildNode(0, "fid");
		ccsdsNode.addChildNode(7, "spacecraft").addChildNode(0, "scid");
		readOidsFromClass();
	}

	/**
	 * Read the OIDs class and create the OID tree
	 */
	private void readOidsFromClass() {
		try {
			for (Field field : OIDs.class.getFields()) {
				int[] oidArray = ObjectIdentifier.class.cast(field.get(null)).toArray();
				addChildNode(oidArray, true, oidArray.length - 1, field.getName());
				LOG.finest(() -> {
					String ret = "";
					try {
						ret = field.getName() + ", " + Arrays.toString(
								ObjectIdentifier.class.cast(field.get(null)).toArray());
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
					return ret;
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add an OID node
	 * 
	 * @param oidArray
	 *            The object identifier
	 * @param bitLabel
	 *            The OID bit label
	 */
	public void addNode(int[] oidArray, String bitLabel) {
		int lastPos = oidArray.length-1;
		if (oidArray.length > CROSS_SUPP_FUNC_BIT_POS 
				&& oidArray[CROSS_SUPP_FUNC_BIT_POS] == CROSS_SUPP_FUNC_BIT_VALUE) {
			if (CROSS_FUNC_RES_BIT_POS == lastPos) {
				addChildNode(oidArray, PARENT_NODES_MUST_EXIST, CROSS_FUNC_RES_BIT_POS, bitLabel);

				// add the parameter(1) node
				int[] parameterOidArray = Arrays.copyOf(oidArray, oidArray.length+1);
				parameterOidArray[parameterOidArray.length-1] = PARAM_BIT_VALUE;
				addChildNode(parameterOidArray, PARENT_NODES_MUST_EXIST, CROSS_SUPP_FUNC_KIND_BIT_POS, "parameter");

				// add the event(2) node
				int[] eventOidArray = Arrays.copyOf(oidArray, oidArray.length+1);
				parameterOidArray[eventOidArray.length-1] = EVENT_BIT_VALUE;
				addChildNode(eventOidArray, PARENT_NODES_MUST_EXIST, CROSS_SUPP_FUNC_KIND_BIT_POS, "event");

				// add the directive(3) node
				int[] directiveOidArray = Arrays.copyOf(oidArray, oidArray.length+1);
				directiveOidArray[directiveOidArray.length-1] = DIREC_BIT_VALUE;
				addChildNode(directiveOidArray, PARENT_NODES_MUST_EXIST, CROSS_SUPP_FUNC_KIND_BIT_POS, "directive");
			}
			else if (PARAM_OR_EVENT_OR_DIRECT_BIT_POS == lastPos) {
				addChildNode(oidArray, PARENT_NODES_MUST_EXIST, PARAM_OR_EVENT_OR_DIRECT_BIT_POS, bitLabel);
			}
			else if (PARAM_OR_EVENT_OR_DIRECT_VERSION_BIT_POS == lastPos) {
				addChildNodeEx(oidArray, PARENT_NODES_MUST_EXIST, PARAM_OR_EVENT_OR_DIRECT_BIT_POS, bitLabel, "version");
			}
			else if (PARAM_OR_EVENT_OR_DIRECT_QUALIFIER_BIT_POS == lastPos) {
				addChildNodeEx(oidArray, PARENT_NODES_MUST_EXIST, PARAM_OR_EVENT_OR_DIRECT_BIT_POS, bitLabel, "version", "qualifier");
			}
		}
		else {
			addChildNode(oidArray, PARENT_NODES_MUST_EXIST, lastPos, bitLabel);
		}
	}

	/**
	 * Add a new child node with label to the selected parent
	 * 
	 * @param oidArray
	 *            The object identifier
	 * @param bitPos
	 *            The index in the oidArray parameter
	 * @param bitLabel
	 *            The parameter, event or directive label
	 * 
	 * @throws NoSuchElementException
	 *             in case the parent does not exist
	 */
	public OidNode addChildNode(int[] oidArray, boolean parentMustExist, int bitPos, String bitLabel) {
		if (this.rootNode.getOidBit() != oidArray[0]) {
			throw new NoSuchElementException("cannot add new label (" + bitLabel + "), wrong root node bit ("
					+ oidArray[0] + ") expected (" + this.rootNode.getOidBit() + ")");
		}
		OidNode parent = this.rootNode;
		for (int i = 1; i < bitPos; i++) {
			OidNode node = parent.getChildNode(oidArray[i]);
			if (node == null) {
				if (!parentMustExist) {
					node = parent.addChildNode(oidArray[i], "unk"); 
				}
				else {
					throw new NoSuchElementException("cannot add new label (" + bitLabel + "), wrong node bit ("
							+ oidArray[i] + ") at position (" + i + "), in " + Arrays.toString(oidArray));
					
				}
			}
			parent = node;
		}
		OidNode ret = parent.getChildNode(oidArray[bitPos]);
		if (ret != null) {
			// already exist, so rename it only
			ret.setOidBitLable(bitLabel);
		}
		else {
			// not exist add, a new one
			ret = parent.addChildNode(oidArray[bitPos], bitLabel);
		}
		return ret;
	}

	/**
	 * Add new child node(s) with label(s) at the selected position.
	 * 
	 * @param oidArray
	 *            The object identifier
	 * @param bitPos
	 *            The index in the oidArray of the first bit label to be added
	 * @param bitLabels
	 *            The node label(s)
	 * 
	 * @throws NoSuchElementException
	 *             in case the parent node does not exist
	 */
	public OidNode addChildNodeEx(int[] oidArray, boolean parentMustExist, int bitPos, String... bitLabels) {
		if (this.rootNode.getOidBit() != oidArray[0]) {
			throw new NoSuchElementException("cannot add new label (" + bitLabels + "), wrong root node bit ("
					+ oidArray[0] + ") expected (" + this.rootNode.getOidBit() + ")");
		}
		// find(create) the parent
		OidNode parent = this.rootNode;
		for (int i = 1; i < bitPos; i++) {
			OidNode node = parent.getChildNode(oidArray[i]);
			if (node == null) {
				if (!parentMustExist) {
					node = parent.addChildNode(oidArray[i], "unk"); 
				}
				else {
					throw new NoSuchElementException("cannot add new label (" + bitLabels + "), wrong node bit ("
							+ oidArray[i] + ") at position (" + i + "), in " + Arrays.toString(oidArray));
					
				}
			}
			parent = node;
		}

		// create children or set their labels
		OidNode ret = parent;
		for (int j = 0; j < bitLabels.length; j++, bitPos++) {
			ret = parent.getChildNode(oidArray[bitPos]);
			if (ret != null) {
				// already exist, so rename it only
				ret.setOidBitLable(bitLabels[j]);
			}
			else {
				// not exist add, a new one
				ret = parent.addChildNode(oidArray[bitPos], bitLabels[j]);
			}
			parent = ret;
		}
		return ret;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		if (this.rootNode != null) {
			printNode(s, rootNode);
		} else {
			s.append("no root node!");
		}

		return s.toString();
	}

	/**
	 * Helper function to print the OID tree.
	 * 
	 * @param s
	 *            the target the print is done to,
	 * @param node
	 *            the node to print
	 */
	private void printNode(StringBuffer s, OidNode node) {
		s.append(node);
		s.append("\n");
		for (OidNode c : node.getChildNodes()) {
			printNode(s, c);
		}
	}

	/**
	 * Return OID as string with from the OID array
	 * 
	 * @param oidArray
	 *            The OID array
	 * @return the OID as string
	 */
	public String print(int[] oidArray) {
		StringBuilder sb = new StringBuilder();
		this.rootNode.print(sb, oidArray, 0);
		return sb.toString();
	}

}
