package esa.egos.csts.api.operations.impl;

import esa.egos.csts.api.operations.IBind;
import esa.egos.csts.api.operations.IExecuteDirective;
import esa.egos.csts.api.operations.IGet;
import esa.egos.csts.api.operations.INotify;
import esa.egos.csts.api.operations.IPeerAbort;
import esa.egos.csts.api.operations.IProcessData;
import esa.egos.csts.api.operations.IReturnBuffer;
import esa.egos.csts.api.operations.IStart;
import esa.egos.csts.api.operations.IStop;
import esa.egos.csts.api.operations.ITransferData;
import esa.egos.csts.api.operations.IUnbind;
import esa.egos.csts.api.serviceinstance.impl.ServiceType;
import esa.egos.csts.api.types.SfwVersion;

public class OpsFactory {
	
	public static IBind createBind(SfwVersion sfwVersion) {
		switch(sfwVersion) {
		case B1: return new  esa.egos.csts.api.operations.impl.b1.Bind();
		case B2: return new  esa.egos.csts.api.operations.impl.b2.Bind();
		default: return null;
		}
	}
	
	public static IBind createBind(SfwVersion sfwVersion, ServiceType sfType, int serviceVersion) {
		switch(sfwVersion) {
		case B1: return new  esa.egos.csts.api.operations.impl.b1.Bind(sfType,serviceVersion);
		case B2: return new  esa.egos.csts.api.operations.impl.b2.Bind(sfType,serviceVersion);
		default: return null;
		}
	}
	
	public static IUnbind createUnbind(SfwVersion sfwVersion) {
		switch(sfwVersion) {
		case B1: return new  esa.egos.csts.api.operations.impl.b1.Unbind();
		case B2: return new  esa.egos.csts.api.operations.impl.b2.Unbind();
		default: return null;
		}
	}
	
	public static IReturnBuffer createReturnBuffer(SfwVersion sfwVersion) {
		switch(sfwVersion) {
		case B1: return new  esa.egos.csts.api.operations.impl.b1.ReturnBuffer();
		case B2: return new  esa.egos.csts.api.operations.impl.b2.ReturnBuffer();
		default: return new esa.egos.csts.api.operations.impl.b1.ReturnBuffer();
		}
	}
	
	public static IPeerAbort createPeerAbort(SfwVersion sfwVersion) {
		switch(sfwVersion) {
		case B1: return new  esa.egos.csts.api.operations.impl.b1.PeerAbort();
		case B2: return new  esa.egos.csts.api.operations.impl.b2.PeerAbort();
		default: return null;
		}
	}
	
	public static IStart createStart(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.Start();
		case B2: return new esa.egos.csts.api.operations.impl.b2.Start();
		default: return null;
		}
	}
	
	public static IStop createStop(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.Stop();
		case B2: return new esa.egos.csts.api.operations.impl.b2.Stop();
		default: return null;
		}
	}
	
	public static IProcessData createProcessData(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.ProcessData();
		case B2: return new esa.egos.csts.api.operations.impl.b2.ProcessData();
		default: return null;
		}
	}
	
	public static IGet createGet(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.Get();
		case B2: return new esa.egos.csts.api.operations.impl.b2.Get();
		default: return null;
		}
	}
	
	public static INotify createNotify(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.Notify();
		case B2: return new esa.egos.csts.api.operations.impl.b2.Notify();
		default: return null;
		}
	}
	
	public static ITransferData createTransferData(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.TransferData();
		case B2: return new esa.egos.csts.api.operations.impl.b2.TransferData();
		default: return null;
		}
	}
	
	public static IExecuteDirective createExecuteDifrective(SfwVersion sfwVersion) {
		switch (sfwVersion) {
		case B1: return new esa.egos.csts.api.operations.impl.b1.ExecuteDirective();
		case B2: return new esa.egos.csts.api.operations.impl.b2.ExecuteDirective();
		default: return null;
		}
	}

}
