package esa.egos.csts.app.si.md;

import esa.egos.csts.api.procedures.informationquery.InformationQueryProvider;
import esa.egos.csts.api.procedures.informationquery.InformationQueryUser;
import esa.egos.csts.api.types.LabelList;

public class InformationQueryProcedureConfig {
	
	private final LabelList labelList;
	
	public InformationQueryProcedureConfig(LabelList labelList) {
		this.labelList = labelList;
	}
	
	public void configureInformationQueryProcedure(InformationQueryProvider informationQuery) {
		informationQuery.getLabelLists().initializeValue(labelList.getLabels());
	}
	
	
	public void configureInformationQueryProcedure(InformationQueryUser informationQuery) {

	}

}
