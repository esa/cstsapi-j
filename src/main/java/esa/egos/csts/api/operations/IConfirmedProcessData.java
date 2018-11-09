package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.ProcessDataReturn;

public interface IConfirmedProcessData extends IProcessData {

	ProcessDataReturn encodeProcessDataReturn();

	void decodeProcessDataReturn(ProcessDataReturn processDataReturn);

}