package esa.egos.csts.api.procedures;

import esa.egos.csts.api.extensions.EmbeddedData;

public interface IUnbufferedDataDelivery extends IStatefulProcedure {

	void transferData(byte[] data);
	
	void transferData(EmbeddedData embeddedData);

}
