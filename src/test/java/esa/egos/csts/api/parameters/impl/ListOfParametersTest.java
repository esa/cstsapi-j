package esa.egos.csts.api.parameters.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import esa.egos.csts.api.enumerations.ProcedureRole;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.oids.OIDs;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureInstanceIdentifier;
import esa.egos.csts.api.procedures.impl.ProcedureType;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

/**
 * test methods of ListOfParameters class
 */
public class ListOfParametersTest
{

    /**
     * test equals() and hashCode() methods
     */
    @Test
    public void testEqualsObject()
    {
        ObjectIdentifier oid = ObjectIdentifier.of(1, 3, 112, 4, 4, 2);

        // EMPTY
        ListOfParameters emptyList = ListOfParameters.empty();
        ListOfParameters anotherEmptyList = ListOfParameters.empty();
        assertTrue(emptyList.equals(anotherEmptyList));
        assertTrue(emptyList.hashCode() == anotherEmptyList.hashCode());
        
        // LIST_NAME 
        ListOfParameters listName = ListOfParameters.of("listName");
        ListOfParameters eqListName = ListOfParameters.of("listName");
        ListOfParameters neqListName = ListOfParameters.of("listname");
        assertTrue(listName.equals(eqListName));
        assertFalse(listName.equals(neqListName));
        assertTrue(listName.hashCode() == eqListName.hashCode());
        assertFalse(listName.hashCode() == neqListName.hashCode());
        
        // FUNCTIONAL_RESOURCE_TYPE
        ListOfParameters frtList = ListOfParameters.of(FunctionalResourceType.of(ObjectIdentifier.of(1, 3, 112, 4, 4, 2, 1)));
        ListOfParameters eqFrtList = ListOfParameters.of(FunctionalResourceType.of(ObjectIdentifier.of(oid, 1)));
        ListOfParameters neqFrtList = ListOfParameters.of(FunctionalResourceType.of(ObjectIdentifier.of(oid, 2)));
        assertTrue(frtList.equals(eqFrtList));
        assertFalse(frtList.equals(neqFrtList));
        assertTrue(frtList.hashCode() == eqFrtList.hashCode());
        assertFalse(frtList.hashCode() == neqFrtList.hashCode());
        
        // FUNCTIONAL_RESOURCE_NAME
        ListOfParameters frnList = ListOfParameters.of(FunctionalResourceName.of(FunctionalResourceType.of(oid), 1));
        ListOfParameters eqFrnList = ListOfParameters.of(FunctionalResourceName.of(FunctionalResourceType.of(oid), 1));
        ListOfParameters neqFrnList = ListOfParameters.of(FunctionalResourceName.of(FunctionalResourceType.of(oid), 2));
        assertTrue(frnList.equals(eqFrnList));
        assertFalse(frtList.equals(eqFrnList)); // compare with functional resource type
        assertFalse(frnList.equals(neqFrnList));
        assertTrue(frnList.hashCode() == eqFrnList.hashCode());
        assertFalse(frnList.hashCode() == neqFrnList.hashCode());
        
        // PROCEDURE_TYPE
        ListOfParameters ptList = ListOfParameters.of(ProcedureType.of(OIDs.cyclicReport));
        ListOfParameters eqPtList = ListOfParameters.of(ProcedureType.of(ObjectIdentifier.of(1,3,112,4,4,1,1,3,2,1,1)));
        ListOfParameters neqPtList = ListOfParameters.of(ProcedureType.of(OIDs.notification));
        assertTrue(ptList.equals(eqPtList));
        assertFalse(ptList.equals(eqFrnList));
        assertFalse(ptList.equals(neqPtList));
        assertTrue(ptList.hashCode() == eqPtList.hashCode());
        assertFalse(ptList.hashCode() == neqPtList.hashCode());
        
        // PROCEDURE_INSTANCE_IDENTIFIER
        ListOfParameters piiList = ListOfParameters.of(ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                       ProcedureRole.PRIME, 0));
        ListOfParameters eqPiiList = ListOfParameters.of(ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                       ProcedureRole.PRIME, 0));
        ListOfParameters neqPiiList = ListOfParameters.of(ProcedureInstanceIdentifier.of(ProcedureType.of(OIDs.cyclicReport),
                                                       ProcedureRole.PRIME, 1));
        assertTrue(piiList.equals(eqPiiList));
        assertFalse(piiList.equals(eqFrnList));
        assertFalse(piiList.equals(neqPiiList));
        assertTrue(piiList.hashCode() == eqPiiList.hashCode());
        assertFalse(piiList.hashCode() == neqPiiList.hashCode());

        // LABELS_SET
        FunctionalResourceType frt1 = FunctionalResourceType.of(ObjectIdentifier.of(oid, 1));
        FunctionalResourceType frt2 = FunctionalResourceType.of(ObjectIdentifier.of(oid, 2));
        ListOfParameters lsList = ListOfParameters.of(Label.of(ObjectIdentifier.of(oid, 1), frt1), Label.of(ObjectIdentifier.of(oid, 2), frt2));
        ListOfParameters eqLsList = ListOfParameters.of(Label.of(ObjectIdentifier.of(oid, 1), frt1), Label.of(ObjectIdentifier.of(oid, 2), frt2));
        ListOfParameters neqLsList = ListOfParameters.of(Label.of(ObjectIdentifier.of(oid, 1), frt1), Label.of(ObjectIdentifier.of(oid, 3), frt2));
        assertTrue(lsList.equals(eqLsList));
        assertFalse(lsList.equals(eqFrnList));
        assertFalse(lsList.equals(neqLsList));
        assertTrue(lsList.hashCode() == eqLsList.hashCode());
        assertFalse(lsList.hashCode() == neqLsList.hashCode());
        
        // NAMES_SET
        FunctionalResourceName frn1 = FunctionalResourceName.of(frt1, 1);
        FunctionalResourceName frn2 = FunctionalResourceName.of(frt2, 2);
        ListOfParameters nsList = ListOfParameters.of(Name.of(ObjectIdentifier.of(oid, 1), frn1), Name.of(ObjectIdentifier.of(oid, 2), frn2));
        ListOfParameters eqNsList = ListOfParameters.of(Name.of(ObjectIdentifier.of(oid, 1), frn1), Name.of(ObjectIdentifier.of(oid, 2), frn2));
        ListOfParameters neqNsList = ListOfParameters.of(Name.of(ObjectIdentifier.of(oid, 1), frn1), Name.of(ObjectIdentifier.of(oid, 3), frn2));
        assertTrue(nsList.equals(eqNsList));
        assertFalse(nsList.equals(eqFrtList));
        assertFalse(nsList.equals(neqNsList));
        assertTrue(nsList.hashCode() == eqNsList.hashCode());
        assertFalse(nsList.hashCode() == neqNsList.hashCode());

    }

}
