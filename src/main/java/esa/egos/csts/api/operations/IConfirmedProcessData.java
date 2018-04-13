package esa.egos.csts.api.operations;

import ccsds.csts.common.operations.pdus.ProcessDataReturn;
import ccsds.csts.common.types.Extended;

public interface IConfirmedProcessData extends IProcessData {

	void decodeProcessDataReturn(ProcessDataReturn processDataReturn);

	ProcessDataReturn encodeProcessDataReturn();

	ProcessDataReturn encodeProcessDataReturn(Extended resultExtension);

}