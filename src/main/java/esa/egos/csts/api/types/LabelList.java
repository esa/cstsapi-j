package esa.egos.csts.api.types;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerBoolean;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.fw.procedure.parameters.events.directives.LabelList.Labels;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedValue;

public class LabelList {

	private String name;
	private boolean defaultList;
	private List<Label> labels;

	public LabelList() {
		labels = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDefaultList() {
		return defaultList;
	}

	public void setDefaultList(boolean defaultList) {
		this.defaultList = defaultList;
	}

	public List<Label> getLabels() {
		return labels;
	}

	public QualifiedValue toQualifiedValue() {

		if (labels.isEmpty()) {
			return new QualifiedValue(ParameterQualifier.UNDEFINED);
		}
		
		QualifiedValue qualifiedValue = new QualifiedValue(ParameterQualifier.VALID);
		
		ParameterValue name = new ParameterValue(ParameterType.CHARACTER_STRING);
		name.getStringParameterValues().add(this.name);
		qualifiedValue.getQualifiedParameterValues().add(name);

		ParameterValue defaultList = new ParameterValue(ParameterType.BOOLEAN);
		defaultList.getBoolParameterValues().add(this.defaultList);
		qualifiedValue.getQualifiedParameterValues().add(defaultList);
		
		ParameterValue labelList = new ParameterValue(ParameterType.OBJECT_IDENTIFIER);
		for (Label label : labels) {
			labelList.getOIDparameterValues().addAll(label.toParameterValue().getOIDparameterValues());
		}
		qualifiedValue.getQualifiedParameterValues().add(labelList);

		return qualifiedValue;

	}

	public ccsds.csts.fw.procedure.parameters.events.directives.LabelList encode() {

		ccsds.csts.fw.procedure.parameters.events.directives.LabelList labelList = new ccsds.csts.fw.procedure.parameters.events.directives.LabelList();

		labelList.setName(new BerVisibleString(name.getBytes(StandardCharsets.UTF_8)));
		labelList.setDefaultList(new BerBoolean(defaultList));
		Labels labels = new Labels();
		for (Label label : this.labels) {
			labels.getLabel().add(label.encode());
		}
		labelList.setLabels(labels);
		return labelList;
	}

	public static LabelList decode(ccsds.csts.fw.procedure.parameters.events.directives.LabelList list) {
		LabelList labelList = new LabelList();
		labelList.setName(new String(list.getName().value, StandardCharsets.UTF_8));
		labelList.setDefaultList(list.getDefaultList().value);
		for (ccsds.csts.common.types.Label label : list.getLabels().getLabel()) {
			labelList.getLabels().add(Label.decode(label));
		}
		return labelList;
	}

}
