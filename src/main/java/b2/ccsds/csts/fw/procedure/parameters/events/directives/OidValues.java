/**
 * This class file was automatically generated by jASN1 v1.11.3 (http://www.beanit.com)
 */

package b2.ccsds.csts.fw.procedure.parameters.events.directives;

import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.io.Serializable;
import com.beanit.jasn1.ber.*;
import com.beanit.jasn1.ber.types.*;
import com.beanit.jasn1.ber.types.string.*;

import b2.ccsds.csts.common.types.AuthorityIdentifier;
import b2.ccsds.csts.common.types.BufferSize;
import b2.ccsds.csts.common.types.DataTransferMode;
import b2.ccsds.csts.common.types.DataUnitId;
import b2.ccsds.csts.common.types.DeliveryLatencyLimit;
import b2.ccsds.csts.common.types.DeliveryMode;
import b2.ccsds.csts.common.types.IdentifierString;
import b2.ccsds.csts.common.types.IntPos;
import b2.ccsds.csts.common.types.IntUnsigned;
import b2.ccsds.csts.common.types.Label;
import b2.ccsds.csts.common.types.ProcessingLatencyLimit;
import b2.ccsds.csts.common.types.ServiceUserRespTimer;
import b2.ccsds.csts.generic.service.object.identifiers.SvcProductionStatusVersion1Type;
import b2.ccsds.csts.service.instance.id.ServiceInstanceIdentifier;

public final class OidValues {
	public static final BerObjectIdentifier pACdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 3});
	public static final BerObjectIdentifier pACeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 2});
	public static final BerObjectIdentifier pACinitiatorId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1, 2});
	public static final BerObjectIdentifier pACparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1});
	public static final BerObjectIdentifier pACresponderId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1, 3});
	public static final BerObjectIdentifier pACresponderPortId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1, 4});
	public static final BerObjectIdentifier pACserviceInstanceId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1, 5});
	public static final BerObjectIdentifier pACserviceUserRespTimer = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1, 1, 1});
	public static final BerObjectIdentifier pBDDconfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 2, 4});
	public static final BerObjectIdentifier pBDDconfigurationChangeEvtValue1 = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1, 1});
	public static final BerObjectIdentifier pBDDconfigurationChangeEvtValue2 = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1, 3});
	public static final BerObjectIdentifier pBDDdataDiscardedExcessBacklog = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 2, 1});
	public static final BerObjectIdentifier pBDDdeliveryLatencyLimit = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1, 3});
	public static final BerObjectIdentifier pBDDdeliveryMode = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1, 2});
	public static final BerObjectIdentifier pBDDdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 3});
	public static final BerObjectIdentifier pBDDendOfData = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 2, 3});
	public static final BerObjectIdentifier pBDDeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 2});
	public static final BerObjectIdentifier pBDDparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1});
	public static final BerObjectIdentifier pBDDproductionConfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 2, 1});
	public static final BerObjectIdentifier pBDDproductionStatusChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 1, 1});
	public static final BerObjectIdentifier pBDDproductionStatusChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 1, 1, 1});
	public static final BerObjectIdentifier pBDDrecordingBufferOverflow = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 2, 2});
	public static final BerObjectIdentifier pBDDreturnBufferSize = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3, 1, 1});
	public static final BerObjectIdentifier pBDPconfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 2});
	public static final BerObjectIdentifier pBDPconfigurationChangeEvtValue1 = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1, 2});
	public static final BerObjectIdentifier pBDPconfigurationChangeEvtValue2 = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 1, 1});
	public static final BerObjectIdentifier pBDPconfigurationChangeEvtValue3 = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1, 3});
	public static final BerObjectIdentifier pBDPdataProcessingCompleted = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 1});
	public static final BerObjectIdentifier pBDPdataTransferMode = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1, 1});
	public static final BerObjectIdentifier pBDPdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 3});
	public static final BerObjectIdentifier pBDPeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 2});
	public static final BerObjectIdentifier pBDPmaxForwardBufferSize = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1, 2});
	public static final BerObjectIdentifier pBDPparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1});
	public static final BerObjectIdentifier pBDPprocessingLatencyLimit = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5, 1, 3});
	public static final BerObjectIdentifier pBDPproductionConfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 2, 1});
	public static final BerObjectIdentifier pBDPproductionStatusChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 1, 1});
	public static final BerObjectIdentifier pBDPproductionStatusChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 1, 1});
	public static final BerObjectIdentifier pCRdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 3});
	public static final BerObjectIdentifier pCReventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 2});
	public static final BerObjectIdentifier pCRminimumAllowedDeliveryCycle = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 1, 2});
	public static final BerObjectIdentifier pCRnamedLabelLists = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 1, 1});
	public static final BerObjectIdentifier pCRnamedLabelListsTypeExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 1, 1, 1});
	public static final BerObjectIdentifier pCRparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8, 1});
	public static final BerObjectIdentifier pDPconfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 2});
	public static final BerObjectIdentifier pDPconfigurationChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 1, 1});
	public static final BerObjectIdentifier pDPdataProcessingCompleted = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 1});
	public static final BerObjectIdentifier pDPdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 3});
	public static final BerObjectIdentifier pDPeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2});
	public static final BerObjectIdentifier pDPinputQueueSize = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 1, 1});
	public static final BerObjectIdentifier pDPparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 1});
	public static final BerObjectIdentifier pDPproductionConfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 2, 1});
	public static final BerObjectIdentifier pDPproductionStatusChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 1, 1});
	public static final BerObjectIdentifier pDPproductionStatusChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 1, 1, 1});
	public static final BerObjectIdentifier pIQdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7, 3});
	public static final BerObjectIdentifier pIQeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7, 2});
	public static final BerObjectIdentifier pIQnamedLabelLists = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7, 1, 1});
	public static final BerObjectIdentifier pIQnamedLabelListsTypeExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7, 1, 1, 1});
	public static final BerObjectIdentifier pIQparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7, 1});
	public static final BerObjectIdentifier pNdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9, 3});
	public static final BerObjectIdentifier pNeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9, 2});
	public static final BerObjectIdentifier pNnamedLabelLists = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9, 1, 1});
	public static final BerObjectIdentifier pNnamedLabelListsTypeExt = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9, 1, 1, 1});
	public static final BerObjectIdentifier pNparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9, 1});
	public static final BerObjectIdentifier pSCDPconfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 2});
	public static final BerObjectIdentifier pSCDPconfigurationChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 1, 1});
	public static final BerObjectIdentifier pSCDPdataProcessingCompleted = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4, 2, 1});
	public static final BerObjectIdentifier pSCDPdataUnitId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 1, 1});
	public static final BerObjectIdentifier pSCDPdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 3});
	public static final BerObjectIdentifier pSCDPeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 2});
	public static final BerObjectIdentifier pSCDPexpired = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 2, 1});
	public static final BerObjectIdentifier pSCDPlocked = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 2, 2});
	public static final BerObjectIdentifier pSCDPparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 1});
	public static final BerObjectIdentifier pSCDPproductionConfigurationChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 2, 1});
	public static final BerObjectIdentifier pSCDPproductionStatusChange = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 2, 1, 1});
	public static final BerObjectIdentifier pSCDPproductionStatusChangeEvtValue = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 5, 1, 1, 1});
	public static final BerObjectIdentifier pSCDPresetDirective = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 3, 1});
	public static final BerObjectIdentifier pSCDPresetDirectiveDirQual = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6, 3, 1, 1});
	public static final BerObjectIdentifier pTEdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 10, 3});
	public static final BerObjectIdentifier pTEeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 10, 2});
	public static final BerObjectIdentifier pTEparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 10, 1});
	public static final BerObjectIdentifier pUBDDeventsId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 2, 2});
	public static final BerObjectIdentifier pUBDDparametersId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 2, 1});
	public static final BerObjectIdentifier pUDDdirectivesId = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 2, 3});
	public static final BerObjectIdentifier procAssociationControl = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 1});
	public static final BerObjectIdentifier procBuffDataDelivery = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 3});
	public static final BerObjectIdentifier procBufferDataProcessing = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 5});
	public static final BerObjectIdentifier procCyclicReport = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 8});
	public static final BerObjectIdentifier procDataProcessing = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 4});
	public static final BerObjectIdentifier procInformationQuery = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 7});
	public static final BerObjectIdentifier procNotification = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 9});
	public static final BerObjectIdentifier procSeqControlledDataProcess = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 6});
	public static final BerObjectIdentifier procThrowEvent = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 10});
	public static final BerObjectIdentifier procUnbuffDataDelivery = new BerObjectIdentifier(new int[]{1, 3, 112, 4, 4, 1, 1, 4, 2});
}
