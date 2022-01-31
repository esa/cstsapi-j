/**
 * @(#) EE_APIPX_Registry.java
 */

package esa.egos.proxy.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.logging.Level;
import java.util.logging.Logger;
import esa.egos.csts.api.enumerations.Result;
import esa.egos.csts.api.procedures.AbstractProcedure;
import esa.egos.csts.api.serviceinstance.IServiceInstanceIdentifier;
import esa.egos.proxy.util.impl.Reference;

/**
 * The class holds all information per registered service instance that is
 * needed to route PDU's to the service instance they belong.
 */
public class EE_APIPX_Registry {
     
     protected static final Logger LOGGER = Logger.getLogger(AbstractProcedure.class.getName());
     
     private final List<EE_APIPX_RegCard> eeAPIPXRegCard;

     private final AtomicInteger sequencer;

     public EE_APIPX_Registry() {
          this.eeAPIPXRegCard = new ArrayList<EE_APIPX_RegCard>();
          this.sequencer = new AtomicInteger(0);
     }

     /**
      * Registers the port. S_OK The port has been registered. SLE_E_DUPLICATE The
      * port is already registered. E_FAIL The registration fails due to a further
      * unspecified error.
      */
     public synchronized Result registerPort(IServiceInstanceIdentifier psiid, String portId,
               Reference<Integer> regId) {
          // check if the siid is registered
          if (this.eeAPIPXRegCard.contains(psiid)) {
               return Result.SLE_E_DUPLICATE;
          }

          // insert a new regcard
          int regCardId = this.sequencer.incrementAndGet();
          EE_APIPX_RegCard regCard = new EE_APIPX_RegCard(regCardId, psiid, portId, null);
          this.eeAPIPXRegCard.add(regCard);

          regId.setReference(regCardId);

          return Result.S_OK;
     }

     /**
      * De-registers the port. S_OK The port has been de-registered. SLE_E_UNKNOWN
      * The port is not registered. E_FAIL The de-registration fails due to a further
      * unspecified error.
      */
     public synchronized Result deregisterPort(int regId, Reference<String> portId) {
          // check if the reg card is registered
          boolean isRegistered = false;

          // iterate on the list and check if it contains the regId
          for (Iterator<EE_APIPX_RegCard> it = this.eeAPIPXRegCard.iterator(); it.hasNext();) {
               EE_APIPX_RegCard theRegCard = it.next();
               if (theRegCard.getRegCardId() == regId) {
                    isRegistered = true;
                    portId.setReference(theRegCard.getPortId());
                    it.remove();
                    break;
               }
          }

          if (!isRegistered) {
               return Result.SLE_E_UNKNOWN;
          }

          return Result.S_OK;
     }

     /**
      * Set the reference of the link used for the service instance.
      */
     public synchronized Result setLink(EE_APIPX_Link plink, IServiceInstanceIdentifier psiid) {
          // check if the reg card is registered
          EE_APIPX_RegCard theRegCard = null;

          // iterate on the list and check if it contains the regId with the
          // required siid
          for (Iterator<EE_APIPX_RegCard> it = this.eeAPIPXRegCard.iterator(); it.hasNext();) {
               theRegCard = it.next();
               if (theRegCard.getSiid().equals(psiid)) {
                    break;
               }
          }

          if (theRegCard == null) {
               return Result.E_FAIL;
          }

          theRegCard.setLink(plink);

          return Result.S_OK;
     }

     /**
      * Return the reference of the link used for the service instance.
      */
     public synchronized EE_APIPX_Link getLink(IServiceInstanceIdentifier psiid) {
          // check if the reg card is registered
          EE_APIPX_RegCard theRegCard = null;

          // iterate on the list and check if it contains the regId with the
          // required siid
          for (Iterator<EE_APIPX_RegCard> it = this.eeAPIPXRegCard.iterator(); it.hasNext();) {
               theRegCard = it.next();
               if (theRegCard.getSiid().equals(psiid)) {
                    break;
               }
          }

          if (theRegCard != null) {
               return theRegCard.getLink();
          }

          return null;
     }

     /**
      * De registers the port. S_OK The port has been de registered. SLE_E_UNKNOWN
      * The port is not registered. E_FAIL The de registration fails due to a further
      * unspecified error.
      */
     public synchronized Result deregisterPort(EE_APIPX_Link pLink, Reference<String> portId) {
          // check if the reg card is registered
          boolean isRegistered = false;

          // iterate on the list and check if it contains the regId
          for (Iterator<EE_APIPX_RegCard> it = this.eeAPIPXRegCard.iterator(); it.hasNext();) {

               EE_APIPX_RegCard theRegCard = it.next();
               EE_APIPX_Link currentLink = theRegCard.getLink();
               if (currentLink != null) {
                    if (currentLink.equals(pLink)) {
                         isRegistered = true;
                         portId.setReference(theRegCard.getPortId());
                         it.remove();
                         break;
                    }
               } else {
                    LOGGER.warning("There was an unclean EE_APIPX_RegCard with null-link, it is removed");
                    it.remove();
               }

          }

          if (!isRegistered) {
               return Result.SLE_E_UNKNOWN;
          }

          return Result.S_OK;
     }

     /**
      * Return the service instance identifier used for the link.
      */
     public synchronized IServiceInstanceIdentifier getSii(EE_APIPX_Link pLink) {
          // check if the reg card is registered
          EE_APIPX_RegCard theRegCard = null;
          IServiceInstanceIdentifier sii = null;

          // iterate on the list and check if it contains the regId
          for (Iterator<EE_APIPX_RegCard> it = this.eeAPIPXRegCard.iterator(); it.hasNext();) {
               theRegCard = it.next();
               if (theRegCard.getLink().equals(pLink)) {
                    sii = theRegCard.getSiid();
                    return sii;
               }
          }

          return sii;
     }

     public synchronized int getPortRegistrationCount(String portId) {
          int result = 0;

          for (EE_APIPX_RegCard li : this.eeAPIPXRegCard) {
               if (li.getPortId().equals(portId)) {
                    result++;
               }
          }

          return result;
     }
}
