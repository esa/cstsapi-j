package esa.egos.csts.api.parameters.impl;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Observable;
import java.util.Set;

import org.junit.Test;

import esa.egos.csts.api.enumerations.CstsResult;
import esa.egos.csts.api.enumerations.OperationType;
import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.events.IEvent;
import esa.egos.csts.api.exceptions.ApiException;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.operations.IAcknowledgedOperation;
import esa.egos.csts.api.operations.IConfirmedOperation;
import esa.egos.csts.api.operations.IOperation;
import esa.egos.csts.api.parameters.IConfigurationParameter;
import esa.egos.csts.api.procedures.IProcedure;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.serviceinstance.IServiceInstance;
import esa.egos.csts.api.types.SfwVersion;

public class LabelListsTest
{

    @Test
    public void test()
    {
        // reference value
        IProcedure procedure = new IProcedure() {
            
            private final ObjectIdentifier oi = ObjectIdentifier.of(1, 2, 3);
            private final ProcedureType pt = ProcedureType.of(oi);

            @Override
            public void update(Observable arg0, Object arg1)
            {
            }

            @Override
            public ProcedureType getType()
            {
                return pt;
            }

            @Override
            public <T extends IOperation> T createOperation(Class<T> cls) throws ApiException
            {
                return null;
            }

            @Override
            public Set<OperationType> getSupportedOperationTypes()
            {
                return null;
            }

            @Override
            public List<IConfigurationParameter> getConfigurationParameters()
            {
                return null;
            }

            @Override
            public IConfigurationParameter getConfigurationParameter(ObjectIdentifier identifier)
            {
                return null;
            }

            @Override
            public List<IEvent> getEvents()
            {
                return null;
            }

            @Override
            public IEvent getEvent(ObjectIdentifier event)
            {
                return null;
            }

            @Override
            public ProcedureInstanceIdentifier getProcedureInstanceIdentifier()
            {
                return ProcedureInstanceIdentifier.of(pt, ProcedureRole.PRIME, 0);
            }

            @Override
            public boolean isStateful()
            {
                return false;
            }

            @Override
            public boolean isPrime()
            {
                return true;
            }


            @Override
            public boolean isConfigured()
            {
                return false;
            }

            @Override
            public ProcedureRole getRole()
            {
                return ProcedureRole.PRIME;
            }

            @Override
            public void setRole(ProcedureRole procedureRole, int instanceNumber)
            {
            }

            @Override
            public IServiceInstance getServiceInstance()
            {
                return null;
            }

            @Override
            public void setServiceInstance(IServiceInstance serviceInstance)
            {
            }

            @Override
            public CstsResult initiateOperationInvoke(IOperation operation)
            {
                return null;
            }

            @Override
            public CstsResult initiateOperationReturn(IConfirmedOperation confOperation)
            {
                return null;
            }

            @Override
            public CstsResult initiateOperationAck(IAcknowledgedOperation ackOperation)
            {
                return null;
            }

            @Override
            public CstsResult informOperationInvoke(IOperation operation)
            {
                return null;
            }

            @Override
            public CstsResult informOperationReturn(IConfirmedOperation confOperation)
            {
                return null;
            }

            @Override
            public CstsResult informOperationAck(IAcknowledgedOperation ackOperation)
            {
                return null;
            }
            
        };
        LabelLists labelLists = new LabelLists(ObjectIdentifier.of(1,3,112,4,4,1,2), true, true, procedure);
        int hash = labelLists.hashCode();

        // equal value
        LabelLists labelLists1 = new LabelLists(ObjectIdentifier.of(1,3,112,4,4,1,2), true, true, procedure);
        int hash1 = labelLists1.hashCode();
        assertTrue(labelLists.equals(labelLists1));
        assertTrue(hash == hash1);

        // not equal values
        LabelLists labelLists2 = new LabelLists(ObjectIdentifier.of(1,3,112,4,4,1,2), false, true, procedure);
        int hash2 = labelLists2.hashCode();
        assertFalse(labelLists.equals(labelLists2));
        assertFalse(hash == hash2);
        LabelLists labelLists3 = new LabelLists(ObjectIdentifier.of(1,3,112,4,4,1,3), true, true, procedure);
        int hash3 = labelLists3.hashCode();
        assertFalse(labelLists.equals(labelLists3));
        assertFalse(hash == hash3);
        LabelLists labelLists4 = new LabelLists(ObjectIdentifier.of(1,3,112,4,4,1,2), true, false, procedure);
        int hash4 = labelLists4.hashCode();
        assertFalse(labelLists.equals(labelLists4));
        assertFalse(hash == hash4);
    }

}
