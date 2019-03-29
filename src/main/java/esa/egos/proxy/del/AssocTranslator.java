package esa.egos.proxy.del;

import ccsds.csts.pdus.CstsFrameworkPdu;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IConfirmedProcessData;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.operations.impl.Bind;
import esa.egos.csts.api.operations.impl.ConfirmedProcessData;
import esa.egos.csts.api.operations.impl.ExecuteDirective;
import esa.egos.csts.api.operations.impl.Get;
import esa.egos.csts.api.operations.impl.Notify;
import esa.egos.csts.api.operations.impl.ProcessData;
import esa.egos.csts.api.operations.impl.Start;
import esa.egos.csts.api.operations.impl.Stop;
import esa.egos.csts.api.operations.impl.TransferData;
import esa.egos.csts.api.operations.impl.Unbind;

public class AssocTranslator {

	public static IOperation decodeBindInvocation(CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getBindInvocation() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");

		IBind bind = new Bind();
		bind.decodeBindInvocation(pdu.getBindInvocation());

		return bind;
	}

	public static IOperation decodeBindReturn(IOperation bindReturnOp, CstsFrameworkPdu pdu) throws ApiException{

		if (pdu.getBindReturn() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");
		
		IBind bind = (IBind) bindReturnOp;
		bind.decodeBindReturn(pdu.getBindReturn());
		
		return bind;
	}
	
	public static IOperation decodeUnbindInvocation(CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getUnbindInvocation() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");

		IUnbind unbind = new Unbind();
		unbind.decodeUnbindInvocation(pdu.getUnbindInvocation());

		return unbind;
	}
	
	public static IOperation decodeUnbindReturn(IOperation unbindReturnOp, CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getUnbindReturn() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");
		
		IUnbind unbind = (IUnbind) unbindReturnOp;
		unbind.decodeUnbindReturn(pdu.getUnbindReturn());
		
		return unbind;
	}

	public static IOperation decodeStartInvocation(CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getStartInvocation() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = new Start();
		start.decodeStartInvocation(pdu.getStartInvocation());

		return start;
	}

	public static IOperation decodeStartReturn(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStartReturn() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = (IStart) returnOp;
		start.decodeStartReturn(pdu.getStartReturn());

		return start;
	}

	public static IOperation decodeStopInvocation(CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getStopInvocation() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = new Stop();
		stop.decodeStopInvocation(pdu.getStopInvocation());

		return stop;
	}
	
	public static IOperation decodeStopReturn(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getStopReturn() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = (IStop) returnOp;
		stop.decodeStopReturn(pdu.getStopReturn());

		return stop;
	}

	public static IOperation decodeTransferDataInvocation(CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getTransferDataInvocation() == null) {
			throw new ApiException("No TRANSFER-DATA data for Assoc Translator transmitted.");
		}

		ITransferData transferData = new TransferData();
		transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());

		return transferData;
	}

	public static IOperation decodeProcessDataInvocation(CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getProcessDataInvocation() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}
		
		IOperation operation;
		if (pdu.getProcessDataInvocation().getStandardInvocationHeader().getInvokeId().longValue() == 0) {
			IProcessData processData = new ProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else {
			IConfirmedProcessData processData = new ConfirmedProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		}
		return operation;
		
	}
	
	public static IOperation decodeProcessDataReturn(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getProcessDataReturn() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}

		IConfirmedProcessData confirmedProcessData = (IConfirmedProcessData) returnOp;
		confirmedProcessData.decodeProcessDataReturn(pdu.getProcessDataReturn());
		
		return confirmedProcessData;
	}
	
	public static IOperation decodeNotifyInvocation(CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getNotifyInvocation() == null) {
			throw new ApiException("No NOTIFY data for Assoc Translator transmitted.");
		}

		INotify notify = new Notify();
		notify.decodeNotifyInvocation(pdu.getNotifyInvocation());

		return notify;
	}
	
	public static IOperation decodeGetInvocation(CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getGetInvocation() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = new Get();
		get.decodeGetInvocation(pdu.getGetInvocation());

		return get;
	}

	public static IOperation decodeGetReturn(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {
		
		if (pdu.getGetReturn() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = (IGet) returnOp;
		get.decodeGetReturn(pdu.getGetReturn());

		return get;
	}
	
	public static IOperation decodeExecuteDirectiveInvocation(CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getExecuteDirectiveInvocation() == null)
			throw new ApiException("No execute directive data for Assoc Translator transmitted.");

		IExecuteDirective executeDirective = new ExecuteDirective();
		executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());
		
		return executeDirective;
	}
	
	public static IOperation decodeExecuteDirectiveAcknowledgement(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveAcknowledge() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());

		return executeDirective;
	}
	
	public static IOperation decodeExecuteDirectiveReturn(IOperation returnOp, CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveReturn() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());

		return executeDirective;
	}

}
