package esa.egos.csts.api.types;

import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerBoolean;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.fw.procedure.parameters.events.directives.LabelList.Labels;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class represents the CCSDS Label List type.
 * 
 * The name is not be confused with {@link Name}. The name of a Label List is a
 * String representation.
 * 
 * Except for modifying the underlying list of Labels, this class is considered immutable.
 */
public class LabelList {

	private final String name;
	private final boolean defaultList;
	private final List<Label> labels;

	/**
	 * Instantiates a new Label List.
	 */
	public LabelList(String name, boolean defaultList) {
		this.name = name;
		this.defaultList = defaultList;
		labels = new ArrayList<>();
	}

	/**
	 * Returns the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Indicates whether this Label List is a default list.
	 * 
	 * @return true if this Label List is a default list, false otherwise
	 */
	public boolean isDefaultList() {
		return defaultList;
	}

	/**
	 * Returns the list of Labels.
	 * 
	 * @return the list of Labels
	 */
	public List<Label> getLabels() {
		return labels;
	}

	/**
	 * Returns this Label List as Qualified Values for usage in a Qualified
	 * Parameter.
	 * 
	 * @return this Label List as Qualified Values
	 */
	public QualifiedValues toQualifiedValues() {

		if (labels.isEmpty()) {
			return new QualifiedValues(ParameterQualifier.UNDEFINED);
		}

		QualifiedValues qualifiedValue = new QualifiedValues(ParameterQualifier.VALID);

		ParameterValue name = new ParameterValue(ParameterType.CHARACTER_STRING);
		name.getStringParameterValues().add(this.name);
		qualifiedValue.getParameterValues().add(name);

		ParameterValue defaultList = new ParameterValue(ParameterType.BOOLEAN);
		defaultList.getBoolParameterValues().add(this.defaultList);
		qualifiedValue.getParameterValues().add(defaultList);

		ParameterValue labelList = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		for (Label label : labels) {
			labelList.getOIDparameterValues().addAll(label.toParameterValue().getOIDparameterValues());
		}
		qualifiedValue.getParameterValues().add(labelList);

		return qualifiedValue;

	}

	/**
	 * Encodes this Label List into a CCSDS Label List type.
	 * 
	 * @return the CCSDS Label List type representing this object
	 */
	public ccsds.csts.fw.procedure.parameters.events.directives.LabelList encode() {

		ccsds.csts.fw.procedure.parameters.events.directives.LabelList labelList = new ccsds.csts.fw.procedure.parameters.events.directives.LabelList();

		labelList.setName(new BerVisibleString(CSTSUtils.encodeString(name)));
		labelList.setDefaultList(new BerBoolean(defaultList));
		Labels labels = new Labels();
		for (Label label : this.labels) {
			labels.getLabel().add(label.encode());
		}
		labelList.setLabels(labels);
		return labelList;
	}

	/**
	 * Decodes a specified CCSDS Label List type.
	 * 
	 * @param labelList
	 *            the specified CCSDS Label List type
	 * @return a new Label List decoded from the specified CCSDS Label List type
	 */
	public static LabelList decode(ccsds.csts.fw.procedure.parameters.events.directives.LabelList labelList) {
		LabelList newLabelList = new LabelList(CSTSUtils.decodeString(labelList.getName().value), labelList.getDefaultList().value);
		for (ccsds.csts.common.types.Label label : labelList.getLabels().getLabel()) {
			newLabelList.getLabels().add(Label.decode(label));
		}
		return newLabelList;
	}

}
