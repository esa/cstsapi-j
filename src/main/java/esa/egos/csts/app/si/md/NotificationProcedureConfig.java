package esa.egos.csts.app.si.md;

import esa.egos.csts.api.parameters.impl.ListOfParameters;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportProvider;
import esa.egos.csts.api.procedures.cyclicreport.CyclicReportUser;
import esa.egos.csts.api.procedures.notification.NotificationProvider;
import esa.egos.csts.api.procedures.notification.NotificationUser;
import esa.egos.csts.api.types.LabelList;

public class NotificationProcedureConfig {
	
	private final LabelList labelList;
	
	private final ListOfParameters listOfEvents;
	
	public NotificationProcedureConfig(LabelList labelList, ListOfParameters listOfEvents) {
		this.labelList = labelList;
		this.listOfEvents = listOfEvents;
	}
	
	public void configureNotificationProcedure(NotificationProvider notification) {
		notification.getLabelLists().initializeValue(labelList.getLabels());
	}
	
	public void configureNotificationProcedure(NotificationUser notification) {
		notification.setListOfEvents(listOfEvents);
	}
	

}
