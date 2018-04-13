package esa.egos.csts.api.procedures;

import esa.egos.csts.api.operations.ITransferData;

public interface IUnbufferedDataDelivery extends IProcedure {

	boolean addData(ITransferData transferData);
	
}
