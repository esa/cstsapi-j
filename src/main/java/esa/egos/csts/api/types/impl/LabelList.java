package esa.egos.csts.api.types.impl;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.openmuc.jasn1.ber.types.BerBoolean;
import org.openmuc.jasn1.ber.types.string.BerVisibleString;

import ccsds.csts.fw.procedure.parameters.events.directives.LabelList.Labels;
import esa.egos.csts.api.enums.ParameterQualifier;
import esa.egos.csts.api.enums.ParameterType;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedValue;
import esa.egos.csts.api.types.ILabel;

public class LabelList {

	private String name;
	private boolean defaultList;
	private List<ILabel> labels;

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

	public List<ILabel> getLabels() {
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
		for (ILabel label : labels) {
			labelList.getOIDparameterValues().addAll(label.toParameterValue().getOIDparameterValues());
		}
		qualifiedValue.getQualifiedParameterValues().add(labelList);

		return qualifiedValue;

	}

	public ccsds.csts.fw.procedure.parameters.events.directives.LabelList encode() {

		ccsds.csts.fw.procedure.parameters.events.directives.LabelList labelList = new ccsds.csts.fw.procedure.parameters.events.directives.LabelList();

		labelList.setName(new BerVisibleString(name.getBytes(StandardCharsets.UTF_16BE)));
		labelList.setDefaultList(new BerBoolean(defaultList));
		Labels labels = new Labels();
		for (ILabel label : this.labels) {
			labels.getLabel().add(label.encode());
		}
		labelList.setLabels(labels);
		return labelList;
	}

	public static LabelList decode(ccsds.csts.fw.procedure.parameters.events.directives.LabelList list) {
		LabelList labelList = new LabelList();
		labelList.setName(new String(list.getName().value, StandardCharsets.UTF_16BE));
		labelList.setDefaultList(list.getDefaultList().value);
		for (ccsds.csts.common.types.Label label : list.getLabels().getLabel()) {
			labelList.getLabels().add(Label.decode(label));
		}
		return labelList;
	}

}
