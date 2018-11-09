package esa.egos.csts.api.procedures;

import esa.egos.csts.api.diagnostics.ListOfParametersDiagnostics;
import esa.egos.csts.api.extensions.EmbeddedData;

public interface INotificationInternal extends INotification {

	boolean checkNotification();

	void stopNotification();

	void setListOfEventsDiagnostics(ListOfParametersDiagnostics listOfParametersDiagnostics);

	EmbeddedData encodeStartDiagnosticExt();

	void startNotification();

}