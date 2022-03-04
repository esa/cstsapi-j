package esa.egos.csts.app.si.md;

import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.types.LabelList;

public class CyclicReportProcedureConfig {
	
	private final long minimumAllowedDeliveryCycle;
	
	private final LabelList labelList;
	
	private final ListOfParameters listOfParameters;
	
	public CyclicReportProcedureConfig(long minimumAllowedDeliveryCycle,LabelList labelList, ListOfParameters listOfParameters) {
		this.minimumAllowedDeliveryCycle = minimumAllowedDeliveryCycle;
		this.labelList = labelList;
		this.listOfParameters = listOfParameters;
	}
	
	public void configureCyclicReportProcedure(CyclicReportProvider cyclicReport) {
		cyclicReport.getLabelLists().initializeValue(labelList.getLabels());
		cyclicReport.getMinimumAllowedDeliveryCycle().initializeValue(minimumAllowedDeliveryCycle);
	}
	
	public void configureCyclicReportProcedure(CyclicReportUser cyclicReport) {
		cyclicReport.setListOfParameters(listOfParameters);
	}
	

}
