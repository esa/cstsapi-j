package esa.egos.csts.api.procedures.notification;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public interface INotificationInternal extends INotification, IStatefulProcedureInternal {

	boolean checkListOfEvents();

	void stopNotification();

	void setListOfEventsDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics);

	EmbeddedData encodeStartDiagnosticExt();

	void startNotification();

}