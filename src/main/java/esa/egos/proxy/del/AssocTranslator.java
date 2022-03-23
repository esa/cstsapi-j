package esa.egos.proxy.del;

import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.ObjectIdentifier;
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
import esa.egos.csts.api.operations.impl.OpsFactory;
import esa.egos.csts.api.operations.impl.b1.ConfirmedProcessData;
import esa.egos.csts.api.types.SfwVersion;
import esa.egos.proxy.xml.FrameworkConfig;

public class AssocTranslator {

	public static IOperation decodeBindInvocation(FrameworkConfig frameworkConfig, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getBindInvocation() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");

		IBind bind = OpsFactory.createBind(SfwVersion.B1);
		bind.decodeBindInvocation(pdu.getBindInvocation());
		ObjectIdentifier serviceIdentifier = bind.getServiceInstanceIdentifier().getCstsTypeIdentifier();
		if(frameworkConfig.getVersion(SfwVersion.B1).getServiceVersion(serviceIdentifier).contains(bind.getServiceVersion()) == false) {
			throw new ApiException("Configuration for standard B1 does not support version " 
					+ bind.getServiceVersion() + " for service " + serviceIdentifier);
		}
		

		return bind;
	}
	
	public static IOperation decodeBindInvocation(FrameworkConfig frameworkConfig, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getBindInvocation() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");

		IBind bind = OpsFactory.createBind(SfwVersion.B2);
		bind.decodeBindInvocation(pdu.getBindInvocation());
		
		ObjectIdentifier serviceIdentifier = bind.getServiceInstanceIdentifier().getCstsTypeIdentifier();
		if(frameworkConfig.getVersion(SfwVersion.B2).getServiceVersion(serviceIdentifier).contains(bind.getServiceVersion()) == false) {
			throw new ApiException("Configuration for standard BB does not support version " 
					+ bind.getServiceVersion() + " for service " + serviceIdentifier);
		}
		
		return bind;
	}

	public static IOperation decodeBindReturn(IOperation bindReturnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getBindReturn() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");

		IBind bind = (IBind) bindReturnOp;
		bind.decodeBindReturn(pdu.getBindReturn());

		return bind;
	}
	
	public static IOperation decodeBindReturn(IOperation bindReturnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getBindReturn() == null)
			throw new ApiException("No bind data for Assoc Translator transmitted.");

		IBind bind = (IBind) bindReturnOp;
		bind.decodeBindReturn(pdu.getBindReturn());

		return bind;
	}

	public static IOperation decodeUnbindInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getUnbindInvocation() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");

		IUnbind unbind = OpsFactory.createUnbind(SfwVersion.B1);
		unbind.decodeUnbindInvocation(pdu.getUnbindInvocation());

		return unbind;
	}
	
	public static IOperation decodeUnbindInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getUnbindInvocation() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");

		IUnbind unbind = OpsFactory.createUnbind(SfwVersion.B2);
		unbind.decodeUnbindInvocation(pdu.getUnbindInvocation());

		return unbind;
	}

	public static IOperation decodeUnbindReturn(IOperation unbindReturnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getUnbindReturn() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");

		IUnbind unbind = (IUnbind) unbindReturnOp;
		unbind.decodeUnbindReturn(pdu.getUnbindReturn());

		return unbind;
	}
	
	public static IOperation decodeUnbindReturn(IOperation unbindReturnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getUnbindReturn() == null)
			throw new ApiException("No unbind data for Assoc Translator transmitted.");

		IUnbind unbind = (IUnbind) unbindReturnOp;
		unbind.decodeUnbindReturn(pdu.getUnbindReturn());

		return unbind;
	}

	public static IOperation decodeStartInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStartInvocation() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = OpsFactory.createStart(SfwVersion.B1);;
		start.decodeStartInvocation(pdu.getStartInvocation());

		return start;
	}
	
	public static IOperation decodeStartInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStartInvocation() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = OpsFactory.createStart(SfwVersion.B2);;
		start.decodeStartInvocation(pdu.getStartInvocation());

		return start;
	}

	public static IOperation decodeStartReturn(IOperation returnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStartReturn() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = (IStart) returnOp;
		start.decodeStartReturn(pdu.getStartReturn());

		return start;
	}
	
	public static IOperation decodeStartReturn(IOperation returnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStartReturn() == null) {
			throw new ApiException("No START data for Assoc Translator transmitted.");
		}

		IStart start = (IStart) returnOp;
		start.decodeStartReturn(pdu.getStartReturn());

		return start;
	}

	public static IOperation decodeStopInvocation( b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStopInvocation() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = OpsFactory.createStop(SfwVersion.B1);
		stop.decodeStopInvocation(pdu.getStopInvocation());

		return stop;
	}
	
	public static IOperation decodeStopInvocation( b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStopInvocation() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = OpsFactory.createStop(SfwVersion.B2);
		stop.decodeStopInvocation(pdu.getStopInvocation());

		return stop;
	}

	public static IOperation decodeStopReturn(IOperation returnOp,  b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStopReturn() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = (IStop) returnOp;
		stop.decodeStopReturn(pdu.getStopReturn());

		return stop;
	}
	
	public static IOperation decodeStopReturn(IOperation returnOp,  b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getStopReturn() == null) {
			throw new ApiException("No STOP data for Assoc Translator transmitted.");
		}

		IStop stop = (IStop) returnOp;
		stop.decodeStopReturn(pdu.getStopReturn());

		return stop;
	}

	public static IOperation decodeTransferDataInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getTransferDataInvocation() == null) {
			throw new ApiException("No TRANSFER-DATA data for Assoc Translator transmitted.");
		}

		ITransferData transferData = OpsFactory.createTransferData(SfwVersion.B1);
		transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());

		return transferData;
	}
	
	public static IOperation decodeTransferDataInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getTransferDataInvocation() == null) {
			throw new ApiException("No TRANSFER-DATA data for Assoc Translator transmitted.");
		}

		ITransferData transferData = OpsFactory.createTransferData(SfwVersion.B2);
		transferData.decodeTransferDataInvocation(pdu.getTransferDataInvocation());

		return transferData;
	}

	public static IOperation decodeProcessDataInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getProcessDataInvocation() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}

		IOperation operation;
		if (pdu.getProcessDataInvocation().getStandardInvocationHeader().getInvokeId().longValue() == 0) {
			IProcessData processData = OpsFactory.createProcessData(SfwVersion.B1);
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else {
			IConfirmedProcessData processData = new ConfirmedProcessData();
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		}
		return operation;

	}
	
	public static IOperation decodeProcessDataInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getProcessDataInvocation() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}

		IOperation operation;
		if (pdu.getProcessDataInvocation().getStandardInvocationHeader().getInvokeId().longValue() == 0) {
			IProcessData processData = OpsFactory.createProcessData(SfwVersion.B2);
			processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		} else {
			IConfirmedProcessData processData = new ConfirmedProcessData();
			 processData.decodeProcessDataInvocation(pdu.getProcessDataInvocation());
			operation = processData;
		}
		return operation;

	}

	public static IOperation decodeProcessDataReturn(IOperation returnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getProcessDataReturn() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}

		IConfirmedProcessData confirmedProcessData = (IConfirmedProcessData) returnOp;
		confirmedProcessData.decodeProcessDataReturn(pdu.getProcessDataReturn());

		return confirmedProcessData;
	}
	
	public static IOperation decodeProcessDataReturn(IOperation returnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getProcessDataReturn() == null) {
			throw new ApiException("No PROCESS-DATA data for Assoc Translator transmitted.");
		}

		IConfirmedProcessData confirmedProcessData = (IConfirmedProcessData) returnOp;
		confirmedProcessData.decodeProcessDataReturn(pdu.getProcessDataReturn());

		return confirmedProcessData;
	}

	public static IOperation decodeNotifyInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getNotifyInvocation() == null) {
			throw new ApiException("No NOTIFY data for Assoc Translator transmitted.");
		}

		INotify notify = OpsFactory.createNotify(SfwVersion.B1);
		notify.decodeNotifyInvocation(pdu.getNotifyInvocation());

		return notify;
	}
	
	public static IOperation decodeNotifyInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getNotifyInvocation() == null) {
			throw new ApiException("No NOTIFY data for Assoc Translator transmitted.");
		}

		INotify notify = OpsFactory.createNotify(SfwVersion.B2);
		notify.decodeNotifyInvocation(pdu.getNotifyInvocation());

		return notify;
	}

	public static IOperation decodeGetInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getGetInvocation() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = OpsFactory.createGet(SfwVersion.B1);
		get.decodeGetInvocation(pdu.getGetInvocation());

		return get;
	}
	
	public static IOperation decodeGetInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getGetInvocation() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = OpsFactory.createGet(SfwVersion.B2);
		get.decodeGetInvocation(pdu.getGetInvocation());

		return get;
	}

	public static IOperation decodeGetReturn(IOperation returnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getGetReturn() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = (IGet) returnOp;
		get.decodeGetReturn(pdu.getGetReturn());

		return get;
	}
	
	public static IOperation decodeGetReturn(IOperation returnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getGetReturn() == null) {
			throw new ApiException("No GET data for Assoc Translator transmitted.");
		}

		IGet get = (IGet) returnOp;
		get.decodeGetReturn(pdu.getGetReturn());

		return get;
	}

	public static IOperation decodeExecuteDirectiveInvocation(b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getExecuteDirectiveInvocation() == null)
			throw new ApiException("No execute directive data for Assoc Translator transmitted.");

		IExecuteDirective executeDirective = OpsFactory.createExecuteDifrective(SfwVersion.B1);
		executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());

		return executeDirective;
	}
	
	public static IOperation decodeExecuteDirectiveInvocation(b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {

		if (pdu.getExecuteDirectiveInvocation() == null)
			throw new ApiException("No execute directive data for Assoc Translator transmitted.");

		IExecuteDirective executeDirective = OpsFactory.createExecuteDifrective(SfwVersion.B2);
		executeDirective.decodeExecuteDirectiveInvocation(pdu.getExecuteDirectiveInvocation());

		return executeDirective;
	}

	public static IOperation decodeExecuteDirectiveAcknowledgement(IOperation returnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveAcknowledge() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());

		return executeDirective;
	}
	
	public static IOperation decodeExecuteDirectiveAcknowledgement(IOperation returnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveAcknowledge() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveAcknowledge(pdu.getExecuteDirectiveAcknowledge());

		return executeDirective;
	}

	public static IOperation decodeExecuteDirectiveReturn(IOperation returnOp, b1.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveReturn() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());

		return executeDirective;
	}
	
	public static IOperation decodeExecuteDirectiveReturn(IOperation returnOp, b2.ccsds.csts.pdus.CstsFrameworkPdu pdu) throws ApiException {
		if (pdu.getExecuteDirectiveReturn() == null) {
			throw new ApiException("No EXECUTE-DIRECTIVE data for Assoc Translator transmitted.");
		}

		IExecuteDirective executeDirective = (IExecuteDirective) returnOp;
		executeDirective.decodeExecuteDirectiveReturn(pdu.getExecuteDirectiveReturn());

		return executeDirective;
	}

}
