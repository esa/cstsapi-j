package esa.egos.csts.api.procedures.throwevent;

import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.procedures.IStatefulProcedureInternal;

public interface IThrowEventInternal extends IThrowEvent, IStatefulProcedureInternal {

	void queueDirective(IExecuteDirective executeDirective);

	boolean hasNoFurtherOperationsWaiting();

	void removeDirective(IExecuteDirective executeDirective);

}
