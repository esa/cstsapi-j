<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet href="frm.xsl" type="text/xsl" ?>
<fr:FunctionalResourceModel xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:fr="http://iso.org.dod.ccsds" xmlns:frtypes="http://ccsds.fr/types">
  <rootOid>
    <oidBit>1</oidBit>
    <oidBit>3</oidBit>
    <oidBit>112</oidBit>
    <oidBit>4</oidBit>
    <oidBit>4</oidBit>
    <oidBit>2</oidBit>
    <oidBit>1</oidBit>
  </rootOid>
  <asnTypeModule>
    <typeDefinition name="ResourceStat">
      <type xsi:type="frtypes:Enumerated">
        <values name="configured"/>
        <values name="operational" value="1"/>
        <values name="interrupted" value="2"/>
        <values name="halted" value="3"/>
      </type>
    </typeDefinition>
    <typeDefinition name="SleRtnDeliveryMode">
      <type xsi:type="frtypes:Enumerated">
        <values name="onlineTimely"/>
        <values name="onlineComplete" value="1"/>
        <values name="offline" value="2"/>
      </type>
    </typeDefinition>
    <typeDefinition name="SleServiceInstanceId">
      <type xsi:type="frtypes:OctetString">
        <sizeConstraint min="30" max="256"/>
      </type>
    </typeDefinition>
    <typeDefinition name="PcmFormat">
      <type xsi:type="frtypes:Enumerated">
        <values name="nrzL"/>
        <values name="nrzM" value="1"/>
        <values name="biPhaseL" value="2"/>
      </type>
    </typeDefinition>
    <typeDefinition name="LockStat">
      <type xsi:type="frtypes:Sequence">
        <elements xsi:type="frtypes:Element" name="carrierLock" optional="false">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values name="notLocked" value="1"/>
          </type>
        </elements>
        <elements xsi:type="frtypes:Element" name="subcarrierLock" optional="false">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values name="notLocked" value="1"/>
            <values name="notApplicable" value="2"/>
          </type>
        </elements>
        <elements xsi:type="frtypes:Element" name="symbolStreamLock" optional="false">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values value="1"/>
          </type>
        </elements>
      </type>
    </typeDefinition>
    <typeDefinition name="SleSiState">
      <type xsi:type="frtypes:Enumerated">
        <values name="unbound"/>
        <values name="ready" value="1"/>
        <values name="active" value="2"/>
      </type>
    </typeDefinition>
    <typeDefinition name="SleReturnTimeout" comment="The engineering unit of this parameter is second.">
      <type xsi:type="frtypes:IntegerType">
        <rangeConstraint min="1" max="600"/>
      </type>
    </typeDefinition>
    <typeDefinition name="ProductionStat">
      <type xsi:type="frtypes:Enumerated">
        <values name="configured"/>
        <values name="operational" value="1"/>
        <values name="interrupted" value="2"/>
        <values name="halted" value="3"/>
      </type>
    </typeDefinition>
    <typeDefinition name="SleRtnProductionStat">
      <type xsi:type="frtypes:Enumerated">
        <values name="running"/>
        <values name="interrupted" value="1"/>
        <values name="halted" value="2"/>
      </type>
    </typeDefinition>
    <typeDefinition name="DirectiveQualifier">
      <type xsi:type="frtypes:Sequence">
        <elements xsi:type="frtypes:Element" name="functResourceInstanceNumber" optional="false">
          <type xsi:type="frtypes:IntegerType"/>
        </elements>
        <elements xsi:type="frtypes:Element" name="directiveQualifierValues" optional="false">
          <type xsi:type="frtypes:SequenceOf">
            <elements xsi:type="frtypes:Sequence">
              <elements xsi:type="frtypes:Element" name="paramId" optional="false">
                <type xsi:type="frtypes:ObjectIdentifier"/>
              </elements>
              <elements xsi:type="frtypes:Element" name="parameterValue" optional="false">
                <type xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="paramTypeOid" optional="false">
                    <type xsi:type="frtypes:ObjectIdentifier"/>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="paramValue" optional="false">
                    <type xsi:type="frtypes:OctetString"/>
                  </elements>
                </type>
              </elements>
            </elements>
          </type>
        </elements>
      </type>
    </typeDefinition>
    <typeDefinition name="FwdLinkStat">
      <type xsi:type="frtypes:Enumerated">
        <values name="fwdLinkStatNotAvailable"/>
        <values name="noBitLock" value="1"/>
        <values name="noRfAvailable" value="2"/>
        <values name="nominal" value="3"/>
      </type>
    </typeDefinition>
    <typeDefinition name="Randomization">
      <type xsi:type="frtypes:Enumerated">
        <values name="off"/>
        <values name="on" value="1"/>
      </type>
    </typeDefinition>
    <typeDefinition name="ServiceInstanceId">
      <type xsi:type="frtypes:Choice">
        <elements xsi:type="frtypes:Element" name="sleServiceInstanceId" optional="false">
          <type xsi:type="frtypes:OctetString">
            <sizeConstraint min="1" max="64"/>
          </type>
        </elements>
        <elements xsi:type="frtypes:Element" name="cstsServiceInstanceId" optional="false">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="spacecraftId" optional="false">
              <type xsi:type="frtypes:ObjectIdentifier"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="facilityId" optional="false">
              <type xsi:type="frtypes:ObjectIdentifier"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="serviceType" optional="false">
              <type xsi:type="frtypes:ObjectIdentifier"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="serviceInstanceNumber" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="4294967295"/>
              </type>
            </elements>
          </type>
        </elements>
      </type>
    </typeDefinition>
    <typeDefinition name="SleReportingCycle">
      <type xsi:type="frtypes:Choice">
        <elements xsi:type="frtypes:Element" name="reportingOff" tag="0" optional="false">
          <type xsi:type="frtypes:Null"/>
        </elements>
        <elements xsi:type="frtypes:Element" name="reportingOn" tag="1" optional="false" comment="The engineeringUnit of this parameter is second">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2" max="600"/>
          </type>
        </elements>
      </type>
    </typeDefinition>
    <typeDefinition name="ServiceUserRespTimer" comment="The engineering unit of this type is second">
      <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.16"/>
    </typeDefinition>
    <typeDefinition name="LongIntUnsigned">
      <type xsi:type="frtypes:IntegerType">
        <rangeConstraint min="0" max="4294967295"/>
      </type>
    </typeDefinition>
    <typeDefinition name="LongIntPos">
      <type xsi:type="frtypes:IntegerType">
        <rangeConstraint min="1" max="4294967295"/>
      </type>
    </typeDefinition>
    <typeDefinition name="ShortIntUnsigned">
      <type xsi:type="frtypes:IntegerType">
        <rangeConstraint min="0" max="65536"/>
      </type>
    </typeDefinition>
    <typeDefinition name="ShortIntPos">
      <type xsi:type="frtypes:IntegerType">
        <rangeConstraint min="1" max="65536"/>
      </type>
    </typeDefinition>
    <typeDefinition name="LabelListSet">
      <type xsi:type="frtypes:SetOf">
        <elements xsi:type="frtypes:Element" name="labelList" optional="false">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="name" optional="false">
              <type xsi:type="frtypes:CharacterString" type="VisibleString"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="defaultList" optional="false" comment="Only one list in the set can be the default list.">
              <type xsi:type="frtypes:Boolean"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="labels" optional="false">
              <type xsi:type="frtypes:SequenceOf"/>
            </elements>
          </type>
        </elements>
      </type>
    </typeDefinition>
  </asnTypeModule>
  <functionalResourceSet name="RF Aperture" oidOffset="1000">
    <functionalResource SemanticDefinition="The Antenna FR accepts as input the carrier signal that shall either be radiated into space or into a water load, provided the given FR instance has the transmit capability. &#xD;&#xA;It provides as output the carrier signal received from space to the Rtn401SpaceLinkCarrierRcpt FR or the Rtn415SpaceLinkCarrierRcpt FR and the azimuth and elevation pointing angles to the TdmSegmentGeneration FR and to the RawRadiometricDataCollection FR, provided the given FR instance has the receive capability. The pointing angles are provided only while the antenna is in some form of auto-track mode.&#xD;&#xA;Note: An Antenna FR may be limited to 'transmit-only' or 'receive-only'." classifier="Antenna" stringIdentifier="antenna" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>1000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the overall status of the antenna and can take on four values: &#xD;&#xA;- 'configured': the antenna system has been configured, but is not yet tracking because it is still moving to the initial pointing or the spacecraft is not yet or no longer in view; &#xD;&#xA; - 'operational': the antenna is tracking in the reported pointing mode (cf. antennaPointingMode); &#xD;&#xA; - 'interrupted': a failure has been detected that prevents the antenna from tracking nominally; &#xD;&#xA; - 'halted': the antenna has been taken out of service, e.g. due to wind speed requiring the antenna to be put into stow position." classifier="antResourceStat" stringIdentifier="antenna-resource-status" version="1" creationDate="2019-07-29T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="AntResourceStat     &#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="If at all, this parameter can only be set by the local EM using a local implementation of a directive. In any case, setting of the antResourceStat to 'operational' or 'interrupted' by means of such directive is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter identifies the antenna that is involved in providing a given support. The antenna may either be identified by its name where typically this name is defined by the operating agency so that no guarantee can be given that the identifier is globally unique. Alternatively the antenna may be officially registered in SANA in which case it has a globally unique Object Identifier. Knowledge of which antenna is being used is needed for a number of aspects, e.g. to assess the observed signal levels with respect to the antenna performance or to perform time correlation that requires knowledge of the exact location of the given antenna.&#xD;&#xA;Note: In case the antennas used for uplink and downlink are not identical, the Functional Resource (FR) instance number shall be used to differentiate them. As concerns antenna arraying, it is assumed that each array is associated with a name constituting a single functional resource." classifier="antId" stringIdentifier="antenna-identifier" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="AntId               &#x9; ::= CHOICE&#xA;{&#xA;&#x9; antennaName         &#x9; [0]&#x9; &#x9; VisibleString (SIZE( 3 .. 16)) &#xA;,&#x9; antennaOid          &#x9; [1]&#x9; &#x9; OBJECT IDENTIFIER&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntId">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="antennaName" tag="0" optional="false">
              <type xsi:type="frtypes:CharacterString" type="VisibleString">
                <sizeConstraint min="3" max="16"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="antennaOid" tag="1" optional="false">
              <type xsi:type="frtypes:ObjectIdentifier"/>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current antenna azimuth pointing in 1/1000 degrees, where 0 degrees corresponds to pointing north and 90 degrees corresponds to pointing east. Depending on the way the antenna is built, values above 360 degrees may be reported in cases where the antenna enables such azimuth range as to mitigate the cable unwrap issue. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity.&#xD;&#xA;Note: Time-tagged antenna pointing is regarded a radiometric product and therefore not part of monitoring. " classifier="antActualAzimuth" stringIdentifier="antenna-actual-azimuth" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntActualAzimuth    &#x9; ::= INTEGER  (0 .. 540000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntActualAzimuth" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="540000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current antenna elevation pointing in 1/1000 degrees, where 0 degrees corresponds to pointing to the horizon and 90 degrees corresponds to zenith pointing. Depending on the way the antenna is built, values above 90 degrees may be reported in cases where the antenna enables such elevation range as to mitigate the azimuth singularity at zenith. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity." classifier="antActualElevation" stringIdentifier="antenna-actual-elevation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntActualElevation  &#x9; ::= INTEGER  (0 .. 180000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntActualElevation" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="180000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the azimuth pointing in 1/1000 degrees commanded to the servo system while the antPointingMode is either 'programTrack' or 'autoTrack', where 0 degrees corresponds to pointing north and 90 degrees corresponds to pointing east. Depending on the way the antenna is built, values above 360 degrees may be reported in cases where the antenna enables such azimuth range as to mitigate the cable unwrap issue. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity." classifier="antCommandedAzimuth" stringIdentifier="antenna-commanded-azimuth" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntCommandedAzimuth &#x9; ::= INTEGER  (0 .. 540000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntCommandedAzimuth" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="540000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the antenna elevation pointing in 1/1000 degrees commanded to the servo system while the antPointingMode is either 'programTrack' or 'autoTrack', where 0 degrees corresponds to pointing to the horizon and 90 degrees corresponds to zenith pointing. Depending on the way the antenna is built, values above 90 degrees may be reported in cases where the antenna enables such elevation range as to mitigate the azimuth singularity at zenith. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity." classifier="antCommandedElevation" stringIdentifier="antenna-commanded-elevation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="-- The engineering unit of the parameter is 1/1000 degree.&#xA;AntCommandedElevation&#x9; ::= INTEGER  (0 .. 180000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntCommandedElevation" comment="The engineering unit of the parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="180000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the azimuth in 1/1000 degrees the antenna shall point to when the pointing-mode is set to 'point',  where 0 degrees corresponds to pointing north and 90 degrees corresponds to pointing east. Depending on the way the antenna is built, values above 360 degrees may be supported in cases where the antenna enables such azimuth range as to mitigate the cable unwrap issue. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity." classifier="antContrAzimuth" stringIdentifier="antenna-controlled-azimuth" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntContrAzimuth     &#x9; ::= INTEGER  (0 .. 540000)" engineeringUnit="1/1000 degrees" configured="true" guardCondition="In most cases, the service agreement will state that this parameter can only be set by local EM. The parameter value will normally be derived from scheduling and trajectory information.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntContrAzimuth" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="540000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the elevation in 1/1000 degrees the antenna shall point to when the pointing-mode is set to 'point',  where 0 degrees corresponds to pointing to the local horizon and 90 degrees corresponds to zenith pointing. Depending on the way the antenna is built, values above 90 degrees may be reported in cases where the antenna enables such elevation range as to mitigate the azimuth singularity at zenith. For antennas with a mount different from elevation over azimuth (e.g. x/y mount), the antenna pointing shall be converted to the azimuth/elevation presentation. This also applies to antennas having a tilt mode as to overcome the zenith track singularity. " classifier="antContrElevation" stringIdentifier="antenna-controlled-elevation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntContrElevation   &#x9; ::= INTEGER  (0 .. 180000)" engineeringUnit="1/1000 degrees" configured="true" guardCondition="In most cases, the service agreement will state that this parameter can only be set by local EM. The parameter value will normally be derived from scheduling and trajectory information. ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntContrElevation" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="180000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="The parameter configures and reports the azimuth angular rate in 1/1000 degrees per second at which the antenna shall move when  antPointingMode is set to 'slew'. " classifier="antContrAzimuthRate" stringIdentifier="antenna-controlled-azimuth-rate" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree per second.&#xA;AntContrAzimuthRate &#x9; ::= INTEGER  (-5000 .. 5000)" engineeringUnit="1/1000 degrees/s" configured="true" guardCondition="In most cases, the service agreement will state that this parameter can only be set by local EM. The parameter value will normally be derived from scheduling and trajectory information. ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntContrAzimuthRate" comment="The engineering unit of this parameter is 1/1000 degree per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-5000" max="5000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="The parameter configures and reports the elevation angular rate in 1/1000 degrees per second at which the antenna shall move when the antPointingMode is set to 'slew'. " classifier="antContrElevationRate" stringIdentifier="antenna-controlled-elevation-rate" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="-- The engineering unit of this parameter is 1/000 degree per second.&#xA;AntContrElevationRate&#x9; ::= INTEGER  (-5000 .. 5000)" engineeringUnit="1/1000 degrees/s" configured="true" guardCondition="In most cases, the service agreement will state that this parameter can only be set by local EM. The parameter value will normally be derived from scheduling and trajectory information. ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntContrElevationRate" comment="The engineering unit of this parameter is 1/000 degree per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-5000" max="5000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the difference in 1/1000 degrees between the actual and the azimuth calculated for antPointingMode = 'programTrack'. Consequently this parameter will only be valid if antPointingMode = 'auto-track'. Antennas not having or not being operated in any closed-loop tracking mode cannot provide this parameter and in this case the parameter shall be flagged as unavailable. &#xD;&#xA;Note: Time-tagged antenna pointing is regarded a radiometric product and therfore not part of monitoring.  " classifier="antAzimuthError" stringIdentifier="antenna-azimuth-error" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree.&#xA;AntAzimuthErrror    &#x9; ::= INTEGER  (-540000 .. 540000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntAzimuthErrror" comment="The engineering unit of this parameter is 1/1000 degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-540000" max="540000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the difference in 1/1000 degrees between the actual and the elevation  calculated for antPointingMode = 'programTrack'. Consequently this parameter will only be valid if antPointingMode = 'auto-track'. Antennas not having or not being operated in any closed-loop tracking mode cannot provide this parameter and in this case the parameter shall be flagged as unavailable." classifier="antElevationError" stringIdentifier="antenna-elevation-error" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="-- The engineering unit of this parameter is 1/1000 degree per second.&#xA;AntElevationError   &#x9; ::= INTEGER  (-180000 .. 180000)" engineeringUnit="1/1000 degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntElevationError" comment="The engineering unit of this parameter is 1/1000 degree per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-180000" max="180000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the pointing mode of the antenna servo system. The values this parameter can take on are:&#xD;&#xA;- 'stow': the antenna is in or is moving to its stow position;&#xD;&#xA;- 'halt': the antenna has been stopped in its current position;&#xD;&#xA;- 'point': the antenna is moving or has moved to the specified azimuth and elevation;&#xD;&#xA;- 'slew': the antenna is moving at commanded angular rates;&#xD;&#xA;- 'program-track': the antenna is pointed in accordance with spacecraft trajectory predicts;&#xD;&#xA;- 'auto-track': the antenna is pointing in closed-loop mode.&#xD;&#xA;Antenna implementations will typically support only a subset of the above listed pointing modes." classifier="antPointingMode" stringIdentifier="antenna-pointing-mode" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="AntPointingMode     &#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; stow                &#x9; &#x9; (0)&#xA;,&#x9; halt                &#x9; &#x9; (1)&#xA;,&#x9; point               &#x9; &#x9; (2)&#xA;,&#x9; slew                &#x9; &#x9; (3)&#xA;,&#x9; programTrack        &#x9; &#x9; (4)&#xA;,&#x9; autoTrack           &#x9; &#x9; (5)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="In most cases, the service agreement will state that this parameter can only be set by local EM. If the antenna servo system does not support the pointing mode commanded by means of the antennaSetContrParams directive, the Functional Resource will reject the setting of the antPointingMode parameter with the diagnostic 'parameter out of range'.&#xD;&#xA;antResourceStatus ≠ 'halted'&#xD;&#xA;Setting antPointingMode to 'point' shall be rejected except if the parameters antContrAzimuth and antContrElevation have valid values.&#xD;&#xA;Setting antPointingMode to 'slew' shall be rejected except if the parameters antContrAzimuthRate and antContrElevationRate have valid values.  ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntPointingMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="stow"/>
            <values name="halt" value="1"/>
            <values name="point" value="2"/>
            <values name="slew" value="3"/>
            <values name="programTrack" value="4"/>
            <values name="autoTrack" value="5"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the nominal return carrier frequency disregarding any Doppler shift.&#xD;&#xA;" classifier="antTrackingRxNominalFreq" stringIdentifier="antenna-tracking-receiver-nominal-frequency" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="14" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;AntTrackingRxNominalFreq&#x9; ::= INTEGER  (2200000000 .. 32300000000)" engineeringUnit="Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxNominalFreq" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2200000000" max="32300000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the polarization of the channel that shall be used for tracking purposes. If automatic selection is chosen, the parameter specifies how much larger in 1/10 dB the power observed for the unselected polarization must be before a switch-over to the polarization with the stronger signal is performed. " classifier="antTrackingSignalPolarization" stringIdentifier="antenna-tracking-signal-polarization" version="1" creationDate="2019-09-24T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="15" typeDefinition="AntTrackingSignalPolarization&#x9; ::= CHOICE&#xA;{&#xA;&#x9; lhc                 &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; rhc                 &#x9; [1]&#x9; &#x9; NULL&#xA;,&#x9; -- The engineering unit of this element is 1/10 dB.&#xA;&#x9; autoHysteresis      &#x9; [2]&#x9; &#x9; INTEGER  (0 .. 100)&#xA;}&#xA;" engineeringUnit="none / 1/10 dB" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingSignalPolarization">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="lhc" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="rhc" tag="1" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="autoHysteresis" tag="2" optional="false" comment="The engineering unit of this element is 1/10 dB.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="100"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports for which condition the Doppler predicts have been calculated. It can take on three values:&#xD;&#xA;- 1-way: this mode will be used when the spacecraft is not locked to a forward link signal or while the spacecraft transponder is commanded to non-coherent mode or when the spacecraft receiver is in 'coherency enabled' mode and the forward link carrier frequency is ramped such that the Doppler on the forward link is compensated, i.e., the spacecraft always 'sees' the nominal forward link frequency; in this case it does not matter if the forward link is radiated by the same station as the one that is receiving the return link or a different station; &#xD;&#xA;- 2-way: this mode is applied when the spacecraft receiver is commanded to 'coherency enabled' mode and the station that is receiving the return link also radiates the forward link, the latter at a constant frequency;&#xD;&#xA;- 3-way: this mode is applied when the spacecraft receiver is in 'coherency enabled' mode and a station different from the one receiving the return link is radiating the forward link signal at a known constant frequency.&#xD;&#xA;&#xD;&#xA;" classifier="antTrackingRxPredictMode" stringIdentifier="antenna-tracking-receiver-predict-mode" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="16" typeDefinition="AntTrackingRxPredictMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; oneWay              &#x9; &#x9; (0)&#xA;,&#x9; twoWay              &#x9; &#x9; (1)&#xA;,&#x9; threeWay            &#x9; &#x9; (2)&#xA;}&#xA;" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxPredictMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="oneWay"/>
            <values name="twoWay" value="1"/>
            <values name="threeWay" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the band in Hz centered around the ntRcptNominalFreq, possibly corrected for the expected Doppler offset, in which the receiver shall search for the carrier signal. This parameter is also valid in case of a suppressed carrier modulation scheme." classifier="antTrackingRxFreqUncertainty" stringIdentifier="antenna-tracking-receiver-frequency-uncertainty" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="17" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;AntTrackingRxFreqUncertainty&#x9; ::= INTEGER  (0 .. 1500000)" engineeringUnit="Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxFreqUncertainty" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="1500000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter can only be set by local EM, not by the x-support user.&#xD;&#xA;This enumerated parameter reports the configuration of the tracking receiver and as such is only supported by antennas that are configured to operate in antPointingMode = 'auto-track' mode and use a tracking receiver in closed-loop pointing mode. The values the parameter may have are:&#xD;&#xA;- 'carrier tracking loop': the tracking receiver tracks the remnant carrier by means of a carrier tracking PLL;&#xD;&#xA;- 'cross-correlation mode': the tracking receiver tracks the return link signal by means of checking for the spectral symmetry (e.g. in case of suppressed carrier modulation schemes).&#xD;&#xA;If a tracking receiver is not available or not used, this parameter shall be flagged as undefined." classifier="antTrackingRxMode" stringIdentifier="antenna-tracking-receiver-mode" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="18" typeDefinition="AntTrackingRxMode   &#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; carrierTrackingMode &#x9; &#x9; (0)&#xA;,&#x9; crossCorrelationMode&#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false" guardCondition="If at all, this parameter can only be set by local EM by means of a local implemntation of an associated directice. &#xD;&#xA;">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="carrierTrackingMode"/>
            <values name="crossCorrelationMode" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the tracking receiver's dual-sided tracking loop bandwidth in tenth Hz. Depending on the mode the tracking receiver operates in, this is either the bandwidth of the PLL tracking the remnant carrier or the frequency range observed for checking spectral symmetry.&#xD;&#xA;If a tracking receiver is not available or not used, this parameter shall be flagged as undefined.&#xD;&#xA;In some implementations one receiver may be used both for antenna tracking and reception of telemetry and ranging. If so, the parameters of that receiver will be represented in both the Antenna FR type and the Rtn401SpaceLinkCarrierRcpt FR type." classifier="antTrackingRxLoopBwdth" stringIdentifier="antenna-tracking-receiver-loop-bandwidth" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="19" typeDefinition="-- The engineering unit of this parameter is 1/10 Hertz.&#xA;AntTrackingRxLoopBwdth&#x9; ::= INTEGER  (1 .. 1000000)" engineeringUnit="1/10 Hz" configured="false" guardCondition="This parameter can only be set by local EM by means of a local implemntation of an associated directice. &#xD;&#xA;">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxLoopBwdth" comment="The engineering unit of this parameter is 1/10 Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the order of the carrier tracking loop. It can take on the following values:&#xD;&#xA;- 'first order': such loop is hardly ever used because it has a static phase error even in case of a constant return link carrier frequency;&#xD;&#xA;- 'second order': this is the most commonly used loop as it has no static phase error for a constant return link carrier frequency;&#xD;&#xA;- 'third order': such configuration may have to be used in case of high Doppler rates, as such loop has no static phase error even when the return link carrier frequency is sweeping, but initial acquisition is more difficult with such loop. " classifier="antTrackingRxOrderOfLoop" stringIdentifier="antenna-tracking-receiver-order-of-loop" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="20" typeDefinition="AntTrackingRxOrderOfLoop&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; firstOrder          &#x9; &#x9; (0)&#xA;,&#x9; secondOrder         &#x9; &#x9; (1)&#xA;,&#x9; thirdOrder          &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxOrderOfLoop">
          <type xsi:type="frtypes:Enumerated">
            <values name="firstOrder"/>
            <values name="secondOrder" value="1"/>
            <values name="thirdOrder" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the return link signal level in tenth of dBm as observed by the tracking receiver, i.e., the signal level derived from the tracking receiver AGC reading. As to have comparable, i.e., station level plan independent values, the level reading should be calibrated to the LNA input. Due to the levels to be expected, the numbers will always be negative.&#xD;&#xA;If a tracking receiver is not available or not used, this parameter shall be flagged as undefined.&#xD;&#xA;In some implementations one receiver may be used both for antenna tracking and reception of telemetry and ranging. If so, the parameters of that receiver will be represented in both the Antenna FR type and the Rtn401SpaceLinkCarrierRcpt FR type." classifier="antTrackingRxInpLevel" stringIdentifier="antenna-tracking-receiver-input-level" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="21" typeDefinition="-- The engineering unit of this 1/10 dBm&#xA;AntTrackingRxInpLevel&#x9; ::= INTEGER  (-2000 .. -30)" engineeringUnit="1/10 dBm" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>21</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>21</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxInpLevel" comment="The engineering unit of this 1/10 dBm">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-2000" max="-30"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the lock status of the tracking receiver and as such is only supported by antennas that are configured to operate in antPointingMode = 'auto-track' mode and use a tracking receiver for closed-loop pointing. The values the parameter may have are:&#xD;&#xA;- 'locked': the tracking receiver has locked on the return link signal and consequently is driving the antenna pointing;&#xD;&#xA;- 'not locked': the tracking receiver is not locked on the return link signal and therefore cannot drive the antenna pointing. &#xD;&#xA;As a consequence, the antPointingMode will have changed to 'program-track' and won't return to 'auto-track' until tracking receiver lock is (re-)acquired. If a tracking receiver is not available or not used, this parameter shall be flagged as undefined.&#xD;&#xA;In some implementations one receiver may be used both for antenna tracking and reception of telemetry and ranging. If so, the parameters of that receiver will be represented in both the Antenna FR type and the Rtn401SpaceLinkCarrierRcpt FR type." classifier="antTrackingRxLockStat" stringIdentifier="antenna-tracking-receiver-lock-status" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="22" typeDefinition="AntTrackingRxLockStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; locked              &#x9; &#x9; (0)&#xA;,&#x9; notLocked           &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>22</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>22</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntTrackingRxLockStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values name="notLocked" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the length in minutes of the period during which the wind speed and peak wind speed are observed for the calculation of the antMeanWindSpeed and antPeakWindSpeed parameters." classifier="antWindIntegrationTime" stringIdentifier="antenna-wind-integration-time" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="23" typeDefinition="-- The engineering unit of this parameter is minutes.&#xA;AntWindIntegrationTime&#x9; ::= INTEGER  (1 .. 60)" engineeringUnit="minute" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>23</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>23</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntWindIntegrationTime" comment="The engineering unit of this parameter is minutes.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="60"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the wind speed as observed close to the antenna. This parameter is measured in tenth m/s and averaged over the most recent period where the length of the period is specified by the antWindIntegrationTime parameter." classifier="antMeanWindSpeed" stringIdentifier="antenna-mean-wind-speed " version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="24" typeDefinition="-- The engineering unit of this parameter is 1/10 meter per second.&#xA;AntMeanWindSpeed    &#x9; ::= INTEGER  (0 .. 1000)" engineeringUnit="1/10 m/s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>24</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>24</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntMeanWindSpeed" comment="The engineering unit of this parameter is 1/10 meter per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the highest wind speed observed close to the antenna during the most recent period  period where the length of the period is specified by the antWindIntegrationTime parameter. This parameter is measured in tenth m/s." classifier="antPeakWindSpeed" stringIdentifier="antenna-peak-wind-speed" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="25" typeDefinition="-- The engineering unit of this parameter is 1/10 meter per second.&#xA;AntPeakWindSpeed    &#x9; ::= INTEGER  (0 .. 1000)" engineeringUnit="1/10 m/s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>25</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>25</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntPeakWindSpeed" comment="The engineering unit of this parameter is 1/10 meter per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current wind direction in degrees, where 0 degrees corresponds to north and 90 degrees to east." classifier="antWindDirection" stringIdentifier="antenna-wind-direction" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="26" typeDefinition="-- The engineering unit of this parameter is degree.&#xA;AntWindDirection    &#x9; ::= INTEGER  (0 .. 359)" engineeringUnit="degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>26</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>26</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntWindDirection" comment="The engineering unit of this parameter is degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="359"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the amount of precipitation in mm that accumulated since 0:00 UTC of the current day." classifier="antAccumulatedPrecipitation" stringIdentifier="antenna-accumulated-precipitation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="27" typeDefinition="-- The engineering unit of this parameter is millimeter.&#xA;AntAccumulatedPrecipitation&#x9; ::= INTEGER  (0 .. 100)" engineeringUnit="mm" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>27</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>27</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntAccumulatedPrecipitation" comment="The engineering unit of this parameter is millimeter.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the precipitation rate in mm/h as observed during the most recent hour." classifier="antPrecipitationRate" stringIdentifier="antenna-precipitation-rate" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="28" typeDefinition="-- The engineering unit of this parameter is millimeter per hour.&#xA;AntPrecipitationRate&#x9; ::= INTEGER  (0 .. 100)" engineeringUnit="mm/h" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>28</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>28</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntPrecipitationRate" comment="The engineering unit of this parameter is millimeter per hour.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the relative humidity in % as observed by an outdoor meteorological sensor at the station. This parameter shall be flagged as invalid if the value from the meteo unit is invalid and it shall be flagged as unavailable if this parameter is not available at this time.&#xD;&#xA;Note: Time-tagged meteo data (relative humidity, atmospheric pressure, temperature) is regarded a radiometric product and therefore not part of the monitoring data." classifier="antRelativeHumidity" stringIdentifier="antenna-relative-humidity" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="29" typeDefinition="-- The engineering unit of this parameter is percent.&#xA;AntRelativeHumidity &#x9; ::= INTEGER  (0 .. 100)" engineeringUnit="%" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntRelativeHumidity" comment="The engineering unit of this parameter is percent.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the atmospheric pressure in hPa as observed by an outdoor meteorological sensor at the station. This parameter shall be flagged as invalid if the value from the meteo unit is invalid and it shall be flagged as unavailable if this parameter is not available at this time." classifier="antAtmosphericPressure" stringIdentifier="antenna-atmospheric-pressure " version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="30" typeDefinition="-- The engineering unit of this parameter is 100 Pascal (hPa).&#xA;AntAtmoshericPressure&#x9; ::= INTEGER  (800 .. 1100)" engineeringUnit="hPa" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntAtmoshericPressure" comment="The engineering unit of this parameter is 100 Pascal (hPa).">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="800" max="1100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the temperature in °C as observed by an outdoor meteorological sensor at the station. This parameter shall be flagged as invalid if the value from the meteo unit is invalid and it shall be flagged as unavailable if this parameter is not available at this instant in time." classifier="antAmbientTemperature" stringIdentifier="antenna-ambient-temperature" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="31" typeDefinition="-- The engineering unit of this parameter is degree Celsius.&#xA;AntAmbientTemperature&#x9; ::= INTEGER  (-100 .. 100)" engineeringUnit="°C" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>31</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>31</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="AntAmbientTemperature" comment="The engineering unit of this parameter is degree Celsius.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-100" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the antResourceStat parameter." classifier="antResourceStatChange" stringIdentifier="antenna-resource-status-change" version="1" creationDate="2018-12-10T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the antResourceStatValue that applies since the notified antResourceStatChange event occurred." classifier="antEventResourceStatValue" stringIdentifier="antenna-event-resource-status-value" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="AntEventResourceStatValue&#x9; ::= AntResourceStat" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="AntEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.0/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies any change of the antTrackingRxLockStat parameter." classifier="antTrackingRxLockStatChange" stringIdentifier="antenna-tracking-receiver-lock-status-change" version="1" creationDate="2019-07-29T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the antTrackingRxLockStat that applies since the notified antTrackingRxLockStatChange event occurred." classifier="antEventTrackingRxLockStat" stringIdentifier="antenna-event-tracking-receiver-lock-status" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="AntEventTrackingRxLockStat&#x9; ::= AntTrackingRxLockStat">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="AntEventTrackingRxLockStat">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.0/@functionalResource.0/@parameter.21/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies that gusts exceed the nominal wind speed the antenna can withstand in operation and therefore the antenna may have to or will be moved to its stow position." classifier="antWindSpeed" stringIdentifier="antenna-wind-speed" version="1" creationDate="2019-07-29T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports if the wind condition are critical in the sense that stowing of the antenna might become necessary ('warning') or that the wind speed necessitates the immediate stowing of the antenna ('stowing')." classifier="antWindSpeedCriticality" stringIdentifier="antenna-wind-speed-criticality" version="1" creationDate="2019-07-29T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="AntWindSpeedCriticality&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; warning             &#x9; &#x9; (0)&#xA;,&#x9; stowing             &#x9; &#x9; (1)&#xA;}&#xA;">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="AntWindSpeedCriticality">
            <type xsi:type="frtypes:Enumerated">
              <values name="warning"/>
              <values name="stowing" value="1"/>
            </type>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the Antenna FR type. " classifier="antSetContrParams" stringIdentifier="antenna-set-control-parameters" version="1" creationDate="2018-11-12T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the antenna FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="antContrParamIdsAndValues" stringIdentifier="antenna-controlled-parameter-identifiers-and-values" version="1" creationDate="2019-09-06T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="AntContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="AntContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="FwdModulatedWaveform" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.1/@functionalResource.0"/>
      <providedAncillaryInterface name="PointingAngles" requiringFunctionalResource="//@functionalResourceSet.17/@functionalResource.0"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="CCSDS 401 Forward Physical Channel Transmission" oidOffset="2000">
    <functionalResource SemanticDefinition="The Fwd401SpaceLinkCarrierXmit FR accepts as input for modulation of the carrier the optionally convolutionally encoded physical channel symbol stream from the FwdAosSyncAndChannelEncoding FR or from the TcSyncAndChannelEncoding FR. It also accepts from the ForwardLinkRanging FR the ranging signal for modulation of the forward carrier.&#xD;&#xA;The Fwd401SpaceLinkCarrierTransmission FR provides the to be radiated carrier signal to the Antenna FR and the actual carrier frequency and phase to the RawRadiometricDataCollection FR. &#x9;" classifier="Fwd401SpaceLinkCarrierXmit" stringIdentifier="Forward 401 Space Link Carrier Transmission" version="1" creationDate="2019-08-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>2000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the Fwd401SpaceLinkCarrierXmit FR resource status and can take on four values:&#xD;&#xA;- 'configured': the forward link equipment has been configured, but the carrier has not been brought up or has been stopped;&#xD;&#xA;- 'operational': the forward link is active, i.e., the carrier is up;&#xD;&#xA;- 'interrupted': a failure has been detected, e.g. carrier still on outside the transmission mask, that resulted in the carrier being shut down;&#xD;&#xA;- 'halted': the forward link has been taken out of service, e.g. due to failed HPA cooling." classifier="fwd401CarrierXmitResourceStat" stringIdentifier="forward-401-carrier-transmission-resource-status" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Fwd401CarrierXmitResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the fwd401CarrierXmitResourceStat to 'operational' or 'interrupted' by means of the directive fwd401CarrierXmitSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the name assigned to the forward physical channel. This name is a Visible String which has a length of 1 to 32 characters." classifier="fwd401CarrierXmitPhysChnlName" stringIdentifier="forward-401-carrier-transmission-physical-channel-name" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="Fwd401CarrierXmitPhysChnlName&#x9; ::= VisibleString (SIZE( 1 .. 32)) " engineeringUnit="None" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitPhysChnlName">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="1" max="32"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the current state of the carrier radiation and can take on three values:&#xD;&#xA;- 'radiating into space': the carrier is presently up and the signal is radiated via the antenna;&#xD;&#xA;- 'radiating into test load': the carrier is presently up and the signal is radiated into the water load;&#xD;&#xA;- 'non-radiating': the carrier is presently down, i.e., no signal is being radiated." classifier="fwd401CarrierXmitStat" stringIdentifier="forward-401-carrier-transmission-status" version="1" creationDate="2019-08-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="Fwd401CarrierXmitStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; radiatingIntoSpace  &#x9; &#x9; (0)&#xA;,&#x9; radiatingIntoTestLoad&#x9; &#x9; (1)&#xA;,&#x9; nonRadiating        &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="The fwd401CarrierXmitStat can be set to 'radiating into space' only if - in view of the given antenna pointing, the EIRP and the spectrum of the radiated signal - the ITU limits regarding the permitted spectral power density are respected.&#xD;&#xA;Furthermore, the following parameters must have a valid value:&#xD;&#xA;- fwd401CarrierXmitEirp;&#xD;&#xA;- fwd401CarrierXmitPolarization;&#xD;&#xA;- fwd401CarrierXmitContrNominalCarrierFrequ.  ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="radiatingIntoSpace"/>
            <values name="radiatingIntoTestLoad" value="1"/>
            <values name="nonRadiating" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the current forward link signal level expressed as Equivalent Isotropically Radiated Power (EIRP) in dBW." classifier="fwd401CarrierXmitEirp" stringIdentifier="forward-401-carrier-transmission-eirp" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is dBW.&#xA;Fwd401CarrierXmitEirp&#x9; ::= INTEGER  (0 .. 150)" engineeringUnit="dBW" configured="true" guardCondition="The commanded signal level must not result in a radiated signal that exceeds the spectral power density limits defined in the pertinent ITU regulations.&#xD;&#xA;Note: The applicable limit depends on the pointing of the antenna and the local horizon.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitEirp" comment="The engineering unit of this parameter is dBW.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="150"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the configured forward link polarization and can take on two values:&#xD;&#xA;- 'lcp': the carrier is radiated with left hand circular polarization;&#xD;&#xA;- 'rcp': the carrier is radiated in right hand circular polarization.&#xD;&#xA;Note: Polarization is defined from the point of view of the source, i.e., in the direction of the wave propagation." classifier="fwd401CarrierXmitPolarization" stringIdentifier="forward-401-carrier-transmission-polarization" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="Fwd401CarrierXmitPolarization&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; lhc                 &#x9; &#x9; (0)&#xA;,&#x9; rhc                 &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitPolarization">
          <type xsi:type="frtypes:Enumerated">
            <values name="lhc"/>
            <values name="rhc" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current forward link frequency in Hz. In general, the frequency will be constant, except during the forward link sweep and for Category B missions in case the forward link is being ramped as to compensate for the Doppler shift and rate on the forward link." classifier="fwd401CarrierXmitActualCarrierFreq" stringIdentifier="forward-401-carrier-transmission-actual-carrier-frequency" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Fwd401CarrierXmitActualCarrierFreq&#x9; ::= INTEGER  (2015000000 .. 40500000000)" engineeringUnit="Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitActualCarrierFreq" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2015000000" max="40500000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the controlled nominal forward link frequency in Hz and if the forward link frequency shall be ramped to compensate the 1-way Doppler offset." classifier="fwd401CarrierXmitContrNominalCarrierFreq" stringIdentifier="forward-401-carrier-transmission-controlled-nominal-carrier-frequency" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Fwd401CarrierXmitContrNominalCarrierFreq&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The engineering unit of this element is Hertz.&#xA;&#x9; nominalFwdLinkFreq  &#x9; INTEGER  (2025000000 .. 40500000000)&#xA;,&#x9; fwdLinkRamping      &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; rampingOff          &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; rampingOn           &#x9; &#x9; (1)&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitContrNominalCarrierFreq" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="nominalFwdLinkFreq" optional="false" comment="The engineering unit of this element is Hertz.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="2025000000" max="40500000000"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="fwdLinkRamping" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="rampingOff"/>
                <values name="rampingOn" value="1"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the frequency generation of the FR is locked to a reference frequency or free running. It can take on the values&#xD;&#xA;- 'lockedToRefFreq;&#xD;&#xA;- 'noRefFreqAvailable'." classifier="fwd401CarrierXmitReferenceFreqLock" stringIdentifier="forward-401-carrier-transmit-reference-frequency-lock" version="1" creationDate="2019-10-14T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="Fwd401CarrierXmitReferenceFreqLock&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; lockedToRefFreq     &#x9; &#x9; (0)&#xA;,&#x9; noRefFreqAvailable  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitReferenceFreqLock">
          <type xsi:type="frtypes:Enumerated">
            <values name="lockedToRefFreq"/>
            <values name="noRefFreqAvailable" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the current state of the carrier modulation and can take on two values:&#xD;&#xA;- 'on': the carrier is presently being modulated;&#xD;&#xA;- 'off': the carrier is presently not being modulated.&#xD;&#xA;Note: This parameter reports the modulation being off also when this is not explicitly commanded, e.g. when it is forced off automatically because the forward link sweep is active (fwd401CarrierXmitSweepProcedure = 'active')." classifier="fwd401CarrierXmitMod" stringIdentifier="forward-401-carrier-transmission-modulation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="Fwd401CarrierXmitMod&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; on                  &#x9; &#x9; (0)&#xA;,&#x9; off                 &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="Turning on of the modulation is not permitted while fwd401CarrierXmitSweepProcedure = 'active'.&#xD;&#xA;Turning off the modulation must not result in exceeding the ITU spectral power density limits.&#xD;&#xA;Furthermore, the modulation can be turned on only if at least one of the two sets of parameters listed below comprises only parameters of which all values are valid:&#xD;&#xA;Set 1:&#xD;&#xA;- fwd401CarrierXmitDataModType;&#xD;&#xA;- fwd401CarrierXmitDataModIndex;&#xD;&#xA;- fwd401CarrierXmitSubcarrierFfrequ;&#xD;&#xA;- fwd401CarrierXmitDataClock;&#xD;&#xA;- fwd401CarrierXmitBasebandWaveform.&#xD;&#xA;Set 2:&#xD;&#xA;- fwd401CarrierXmitRngModIindex.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitMod">
          <type xsi:type="frtypes:Enumerated">
            <values name="on"/>
            <values name="off" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports whether the symbol stream directly bpsk modulates the forward carrier such that the carrier is suppressed or directly bi-phase-L modulates the carrier such that there is a remnant carrier or modulates a subcarrier that in turn modulates the forward carrier. It can take on the following values:&#xD;&#xA;- 'bpsk': the carrier is bpsk modulated by the input data symbol stream; &#xD;&#xA;- 'direct': the carrier is directly bi-phase-L modulated by the input data symbol stream;&#xD;&#xA;- 'subcarrier': the carrier modulating signal is the subcarrier which in turn is PSK modulated by the to be radiated symbol stream.&#xD;&#xA;For all three modulation options the parameter specifies if the Doppler shift of the symbol stream shall be compensated and if so, if a fixed offset (in 1/10000 Hz) shall be applied or if the symbol rate shall be continuously updated (ramped) such that the spacecraft does not &quot;see&quot; any Doppler shift of the symbol rate. Given that CCSDS 401.0 prescribes that subcarrier and symbol rate have to be coherent, the subcarrier if applicable is Doppler shift compensated as the symbol rate.&#xD;&#xA;As applicable, the nominal symbol rate (in 1/10000 Hz) and nominal subcarrier frequency (in 1/10000 Hz) and the associated modulation index (in 1/100 rad) are specified." classifier="fwd401CarrierXmitSymbolStreamModType" stringIdentifier="forward-401-carrier-transmission-symbol-stream-modulation-type" version="1" creationDate="2019-08-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="Fwd401CarrierXmitSymbolStreamModType&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; symbolRateDopplerCompensation&#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; noCompensation      &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; -- The engineering unit of this parameter is 1/1000 Hertz.&#xA;&#x9; &#x9; staticOffset        &#x9; [1]&#x9; &#x9; INTEGER  (-30000000 .. 30000000)&#xA;,&#x9; &#x9; -- Given that CCSDS requires coherency of subcarrier and symbol clock, ramping of the symbol rate implies that also the subcarrier frequency is ramped, in case a subcarrier is used.&#xA;&#x9; &#x9; ramping             &#x9; [2]&#x9; &#x9; NULL&#xA;&#x9; }&#xA;&#xA;,&#x9; modType             &#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; bpsk                &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; bpskSymbolRate      &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; ccsdsBpskSymcolRate &#x9; [0]&#x9; &#x9; INTEGER  (100000000 | 200000000 | 400000000 | 800000000 | 1600000000 | 3200000000 | 6400000000 | 12800000000 | 25600000000 | 51200000000 | 102400000000 | 204800000000)&#xA;,&#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; nonCcsdsBpskSymbolRate&#x9; [1]&#x9; &#x9; INTEGER  (100000000 .. 204800000000)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; tcBpskPcmFormat     &#x9; PcmFormat (biPhaseL)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; direct              &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100 radian&#xA;&#x9; &#x9; &#x9; symbolStreamDirectModulationIndex&#x9; INTEGER  (20 .. 140)&#xA;,&#x9; &#x9; &#x9; directModSymbolRate &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; ccsdsDirectModSymbolRate&#x9; [0]&#x9; &#x9; INTEGER  (400000000 | 800000000 | 1600000000 | 3200000000 | 6400000000 | 12800000000 | 25600000000)&#xA;,&#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; nonCcsdsDirectModSymbolRate&#x9; [1]&#x9; &#x9; INTEGER  (400000000 .. 25600000000)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; tcDirectPcmFormat   &#x9; PcmFormat (biPhaseL)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; subcarrier          &#x9; [2]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; subcarrierNominalFrequency&#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; ccsdsNominalSubcarrierFrequencyAndSymbolRate&#x9; [0]&#x9; &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; subcarrier8Khz      &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; subcarrierNominalFrequency&#x9; INTEGER  (800000000)&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; nominalSymbolRate   &#x9; INTEGER  (781250 | 1562500 | 3125000 | 6250000 | 12500000 | 25000000 | 50000000 | 100000000 | 200000000)&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; subcarrier16Khz     &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; subcarrierNominalFrequency&#x9; INTEGER  (1600000000)&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; &#x9; nominalSymbolRate   &#x9; INTEGER  (781250 | 1562500 | 3125000 | 6250000 | 12500000 | 25000000 | 50000000 | 100000000 | 200000000 | 400000000)&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; &#x9; nonCcsdsSubcarrierFrequencyAndSymbolRate&#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; nominalSubcarrierFrequency&#x9; INTEGER  (800000000 .. 1600000000)&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100000 Hertz.&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; nominalSymbolRate   &#x9; INTEGER  (781250 .. 400000000)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; -- The engineering unit of this parameter is 1/100 radian.&#xA;&#x9; &#x9; &#x9; subcarrierModIndex  &#x9; INTEGER  (20 .. 140)&#xA;,&#x9; &#x9; &#x9; tcSubcarrierPcmFormat&#x9; PcmFormat (nrzL | nrzM)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="none" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSymbolStreamModType">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="symbolRateDopplerCompensation" optional="false">
              <type xsi:type="frtypes:Choice">
                <elements xsi:type="frtypes:Element" name="noCompensation" tag="0" optional="false">
                  <type xsi:type="frtypes:Null"/>
                </elements>
                <elements xsi:type="frtypes:Element" name="staticOffset" tag="1" optional="false" comment="The engineering unit of this parameter is 1/1000 Hertz.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="-30000000" max="30000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="ramping" tag="2" optional="false" comment="Given that CCSDS requires coherency of subcarrier and symbol clock, ramping of the symbol rate implies that also the subcarrier frequency is ramped, in case a subcarrier is used.">
                  <type xsi:type="frtypes:Null"/>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="modType" optional="false">
              <type xsi:type="frtypes:Choice">
                <elements xsi:type="frtypes:Element" name="bpsk" tag="0" optional="false">
                  <type xsi:type="frtypes:Sequence">
                    <elements xsi:type="frtypes:Element" name="bpskSymbolRate" optional="false">
                      <type xsi:type="frtypes:Choice">
                        <elements xsi:type="frtypes:Element" name="ccsdsBpskSymcolRate" tag="0" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>100000000</values>
                              <values>200000000</values>
                              <values>400000000</values>
                              <values>800000000</values>
                              <values>1600000000</values>
                              <values>3200000000</values>
                              <values>6400000000</values>
                              <values>12800000000</values>
                              <values>25600000000</values>
                              <values>51200000000</values>
                              <values>102400000000</values>
                              <values>204800000000</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="nonCcsdsBpskSymbolRate" tag="1" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                          <type xsi:type="frtypes:IntegerType">
                            <rangeConstraint min="100000000" max="204800000000"/>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="tcBpskPcmFormat" optional="false">
                      <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                        <singleValueConstraint>
                          <values>2</values>
                        </singleValueConstraint>
                      </type>
                    </elements>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="direct" tag="1" optional="false">
                  <type xsi:type="frtypes:Sequence">
                    <elements xsi:type="frtypes:Element" name="symbolStreamDirectModulationIndex" optional="false" comment="The engineering unit of this parameter is 1/100 radian">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="20" max="140"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="directModSymbolRate" optional="false">
                      <type xsi:type="frtypes:Choice">
                        <elements xsi:type="frtypes:Element" name="ccsdsDirectModSymbolRate" tag="0" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>400000000</values>
                              <values>800000000</values>
                              <values>1600000000</values>
                              <values>3200000000</values>
                              <values>6400000000</values>
                              <values>12800000000</values>
                              <values>25600000000</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="nonCcsdsDirectModSymbolRate" tag="1" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                          <type xsi:type="frtypes:IntegerType">
                            <rangeConstraint min="400000000" max="25600000000"/>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="tcDirectPcmFormat" optional="false">
                      <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                        <singleValueConstraint>
                          <values>2</values>
                        </singleValueConstraint>
                      </type>
                    </elements>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="subcarrier" tag="2" optional="false">
                  <type xsi:type="frtypes:Sequence">
                    <elements xsi:type="frtypes:Element" name="subcarrierNominalFrequency" optional="false">
                      <type xsi:type="frtypes:Choice">
                        <elements xsi:type="frtypes:Element" name="ccsdsNominalSubcarrierFrequencyAndSymbolRate" tag="0" optional="false">
                          <type xsi:type="frtypes:Choice">
                            <elements xsi:type="frtypes:Element" name="subcarrier8Khz" tag="0" optional="false">
                              <type xsi:type="frtypes:Sequence">
                                <elements xsi:type="frtypes:Element" name="subcarrierNominalFrequency" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                                  <type xsi:type="frtypes:IntegerType">
                                    <singleValueConstraint>
                                      <values>800000000</values>
                                    </singleValueConstraint>
                                  </type>
                                </elements>
                                <elements xsi:type="frtypes:Element" name="nominalSymbolRate" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                                  <type xsi:type="frtypes:IntegerType">
                                    <singleValueConstraint>
                                      <values>781250</values>
                                      <values>1562500</values>
                                      <values>3125000</values>
                                      <values>6250000</values>
                                      <values>12500000</values>
                                      <values>25000000</values>
                                      <values>50000000</values>
                                      <values>100000000</values>
                                      <values>200000000</values>
                                    </singleValueConstraint>
                                  </type>
                                </elements>
                              </type>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="subcarrier16Khz" tag="1" optional="false">
                              <type xsi:type="frtypes:Sequence">
                                <elements xsi:type="frtypes:Element" name="subcarrierNominalFrequency" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                                  <type xsi:type="frtypes:IntegerType">
                                    <singleValueConstraint>
                                      <values>1600000000</values>
                                    </singleValueConstraint>
                                  </type>
                                </elements>
                                <elements xsi:type="frtypes:Element" name="nominalSymbolRate" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz">
                                  <type xsi:type="frtypes:IntegerType">
                                    <singleValueConstraint>
                                      <values>781250</values>
                                      <values>1562500</values>
                                      <values>3125000</values>
                                      <values>6250000</values>
                                      <values>12500000</values>
                                      <values>25000000</values>
                                      <values>50000000</values>
                                      <values>100000000</values>
                                      <values>200000000</values>
                                      <values>400000000</values>
                                    </singleValueConstraint>
                                  </type>
                                </elements>
                              </type>
                            </elements>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="nonCcsdsSubcarrierFrequencyAndSymbolRate" tag="1" optional="false">
                          <type xsi:type="frtypes:Sequence">
                            <elements xsi:type="frtypes:Element" name="nominalSubcarrierFrequency" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="800000000" max="1600000000"/>
                              </type>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="nominalSymbolRate" optional="false" comment="The engineering unit of this parameter is 1/100000 Hertz.">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="781250" max="400000000"/>
                              </type>
                            </elements>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="subcarrierModIndex" optional="false" comment="The engineering unit of this parameter is 1/100 radian.">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="20" max="140"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="tcSubcarrierPcmFormat" optional="false">
                      <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                        <singleValueConstraint>
                          <values>0</values>
                          <values>1</values>
                        </singleValueConstraint>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the ranging signal shall be suppressed in the presence of a to be radiated telecommand signal. It can take on the values&#xD;&#xA;'yes'&#xD;&#xA;'no'" classifier="fwd401CarrierXmitTcPriority" stringIdentifier="forward-401-carrier-transmit-telecommand-priority" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="Fwd401CarrierXmitTcPriority&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; yes                 &#x9; &#x9; (0)&#xA;,&#x9; no                  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitTcPriority">
          <type xsi:type="frtypes:Enumerated">
            <values name="yes"/>
            <values name="no" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the configured peak modulation index for the ranging signal in 1/100 radians. If the fwd401CarrierXmitSymbolStreamModType is 'bpsk', concurrent uplink of telecommands and ranging is not possible." classifier="fwd401CarrierXmitRngModIndex" stringIdentifier="forward-401-carrier-transmission-ranging-modulation-index" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="-- The engineering unit of this parameter is 1/100 radian.&#xA;Fwd401CarrierXmitRngModIndex&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The engineering unit of this element is 1/100 radian.&#xA;&#x9; duringAmbiguityResolution&#x9; INTEGER  (0 .. 140)&#xA;,&#x9; -- The engineering unit of this element is 1/100 radian&#xA;&#x9; afterAmbiguityResolution&#x9; INTEGER  (0 .. 140)&#xA;}&#xA;" engineeringUnit="1/100 rad" configured="true" guardCondition="If modType in the fwd401CarrierXmitSymbolStreamModType parameter is 'bpsk', the value of the fwd401CarrierXmitRngModIndex parameter must be set to 0.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitRngModIndex" comment="The engineering unit of this parameter is 1/100 radian.">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="duringAmbiguityResolution" optional="false" comment="The engineering unit of this element is 1/100 radian.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="140"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="afterAmbiguityResolution" optional="false" comment="The engineering unit of this element is 1/100 radian">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="140"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the current state of the carrier sweep and can take on two values:&#xD;&#xA;- 'active': the carrier is presently being swept;&#xD;&#xA;- 'not active': the carrier is presently not being swept, i.e., the nominal frequency possibly compensated for Doppler is being radiated. &#xD;&#xA;Modulation shall be forced off, whenever the sweep is active. In case the forward link frequency is being ramped as to compensate for Doppler shift and rate on the forward link (Category B missions only), this is not regarded to be a sweep.&#xD;&#xA;The sweep procedure is strated by setting the fwd401CarrierXmitSweepProcStat parameter to 'active' by means of the fwd401CarrierXmitSetContrParams directive. Once the sweep procedure as specified by the  parameter fwd401CarrierXmitSweepProfile is completed, the fwd401CarrierXmitSweepProcStat parameter is set automatically to 'notActive'. and modulation by TC and ranging is re-enabled. &#xD;&#xA;If necessary, the sweep procedure while executing can be stopped by setting the fwd401CarrierXmitSweepProcStat parameter to 'notActive' using the fwd401CarrierXmitSetContrParams directive. In that case the forward link carrier frequency jumps back to the frequency set by the fwd401CarrierXmitContrNominalCarrierFreq parameter, compensated for 1-way Doppler if so configured." classifier="fwd401CarrierXmitSweepProcStat" stringIdentifier="forward-401-carrier-transmission-sweep-procedure-status" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="Fwd401CarrierXmitSweepProcStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; active              &#x9; &#x9; (0)&#xA;,&#x9; notActive           &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="fwd401CarrierXmitStat ≠ 'down'&#xD;&#xA;Furthermore, the parameter fwd401CarrierXmitSweepProfile must have a valid value.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSweepProcStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="active"/>
            <values name="notActive" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This complex parameter configures and reports the start frequency in Hz of the first leg of the sweep and for each leg it then specifies the stop frequency in Hz, the sweep rate in Hz/s and the duration of the dwell period in seconds (i.e., the time during which the carrier frequency is not changed) before the next leg is started. The assumption is that there are no frequency discontinuities, i.e., the start frequency of a sweep leg is always equal to the stop frequency of the previous leg.&#xD;&#xA;&#xD;&#xA;If parameter fwd401CarrierXmitContrNominalCarrierFreq is configured such that by means of ramping of the forward link frequency the 1-way Doppler is compensated, then the specified sweep profile frequencies will be modified automatically in accordance with the Doppler offeset given at the given time." classifier="fwd401CarrierXmitSweepProfile" stringIdentifier="forward-401-carrier-transmission-sweep-profile" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="14" typeDefinition="Fwd401CarrierXmitSweepProfile&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The engineering unit of this parameter is Hertz.&#xA;&#x9; startfreq           &#x9; INTEGER  (2025000000 .. 40500000000)&#xA;,&#x9; sweepLegs           &#x9; SEQUENCE  (SIZE( 1 .. 3))  OF&#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The engineering unit of this parameter is second&#xA;&#x9; &#x9; &#x9; dwellTime           &#x9; INTEGER  (0 .. 20)&#xA;,&#x9; &#x9; &#x9; -- The engineering unit of this parameter is hertz per second.&#xA;&#x9; &#x9; &#x9; sweepRate           &#x9; INTEGER  (1 .. 32000)&#xA;,&#x9; &#x9; &#x9; -- The engineering unit of this parameter is Hertz.&#xA;&#x9; &#x9; &#x9; endFreq             &#x9; INTEGER  (2025000000 .. 40500000000)&#xA;&#x9; &#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="Hz, Hz/s, s (see Semantic Definition for details)" configured="false" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSweepProfile">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="startfreq" optional="false" comment="The engineering unit of this parameter is Hertz.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="2025000000" max="40500000000"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="sweepLegs" optional="false">
              <type xsi:type="frtypes:SequenceOf">
                <sizeConstraint min="1" max="3"/>
                <elements xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="dwellTime" optional="false" comment="The engineering unit of this parameter is second">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="0" max="20"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="sweepRate" optional="false" comment="The engineering unit of this parameter is hertz per second.">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="1" max="32000"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="endFreq" optional="false" comment="The engineering unit of this parameter is Hertz.">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="2025000000" max="40500000000"/>
                    </type>
                  </elements>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the actual subcarrier frequency in 1/1000 Hz.  The fine resolution is specified here as to enable reporting of the fine tuning of the subcarrier frequency in cases where Doppler shift compensation is applied. In case that no subcarrier is used (fwd401CarrierXmitSymbolStreamModType ≠ 'subcarrier'), this parameter shall report '0'.  &#xD;&#xA;Note: The specified range is intended to also cover the case of non-CCSDS missions. " classifier="fwd401CarrierXmitSubcarrierFreq" stringIdentifier="forward-401-carrier-transmission-subcarrier-frequency" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="15" typeDefinition="-- The engineering unit of this parameter is 1/10000 Hertz&#xA;Fwd401CarrierXmitSubcarrierFreq&#x9; ::= INTEGER  (0 .. 320000000)" engineeringUnit="1/10000 Hz" configured="false" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSubcarrierFreq" comment="The engineering unit of this parameter is 1/10000 Hertz">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="320000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the subcarrier is currently being modulated and can take on two values:&#xD;&#xA;- 'on': the subcarrier is currently being modulated;&#xD;&#xA;- 'off': the subcarrier is currently not being modulated.&#xD;&#xA;In case no subcarrier is used (fwd401CarrierXmitSymbolStreamModType ≠ subcarrier'), this parameter shall be flagged as undefined." classifier="fwd401CarrierXmitSubcarrierMod" stringIdentifier="forward-401-carrier-transmission-subcarrier-modulation" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="16" typeDefinition="Fwd401CarrierXmitSubcarrierMod&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; on                  &#x9; &#x9; (0)&#xA;,&#x9; off                 &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="fwd401CarrierXmitSymbolStreamModType = 'subcarrier'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSubcarrierMod">
          <type xsi:type="frtypes:Enumerated">
            <values name="on"/>
            <values name="off" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the actual forward link symbol rate in 1/10000 Hz, " classifier="fwd401CarrierXmitSymbolRate" stringIdentifier="forward-401-carrier-transmission-symbol-rate" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="17" typeDefinition="-- The engineering unit of this parameter is 1/10000 Hertz.&#xA;Fwd401CarrierXmitSymbolRate&#x9; ::= INTEGER  (78125 .. 205100000000)" engineeringUnit="1/10000 Hz" configured="false" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Fwd401CarrierXmitSymbolRate" comment="The engineering unit of this parameter is 1/10000 Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="78125" max="205100000000"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwd401CarrierXmitResourceStat parameter." classifier="fwd401CarrierXmitResourceStatChange" stringIdentifier="forward-401-carrier-transmission-resource-status-change" version="1" creationDate="2018-11-13T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwd401CarrierXmitResourceStat value that applies since the notified fwd401CarrierXmitStatChange event occurred." classifier="fwd401CarrierXmitEventResourceStatValue" stringIdentifier="forward-401-carrier-transmission-event-resource-status-value" version="1" creationDate="2019-09-05T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Fwd401CarrierXmitEventResourceStatValue&#x9; ::= Fwd401CarrierXmitResourceStat" engineeringUnit="none">
          <oid/>
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>2000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="Fwd401CarrierXmitEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.1/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the Fwd401SpaceLinkCarrierXmit FR type. " classifier="fwd401CarrierXmitSetContrParams" stringIdentifier="forward-401-carrier-transmission-set-control-parameters" version="1" creationDate="2018-11-20T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the Fwd401SpaceLinkCarrierXmit FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="fwd401CarrierXmitContrParamIdsAndValues" stringIdentifier="forward-401-carrier-transmission-controlled-parameter-identifiers-and-values" version="1" creationDate="2019-09-09T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Fwd401CarrierXmitContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>2000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="Fwd401CarrierXmitContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="FwdPhysicalChnlSymbols" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.5/@functionalResource.0"/>
      <serviceAccesspoint name="FwdPhusicalChnlSymbols" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.6/@functionalResource.0"/>
      <serviceAccesspoint name="RngSignal" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.1/@functionalResource.1"/>
      <providedAncillaryInterface name="TransmitFrequency" requiringFunctionalResource="//@functionalResourceSet.3/@functionalResource.1"/>
    </functionalResource>
    <functionalResource SemanticDefinition="This FR does not take any input. &#xD;&#xA;It provides the ranging signal to be radiated to the spacecraft to the Forward 401 Space Link Carrier Transmission FR for modulation onto the forward carrier.&#xD;&#xA;It provides the timing information needed by the Range and Doppler Extraction FR.&#xD;&#xA;" classifier="FwdLinkRng" stringIdentifier="Forward Link Ranging" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>2001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the FwdLinkRng FR resource status and can take on four values:&#xD;&#xA;- 'configured';&#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="fwdLinkRngResourceStat" stringIdentifier="forward-link-ranging-resource-status" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdLinkRngResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the fwdLinkRngResourceStat to 'operational' or 'interrupted' by means of the directive fwdLinkRngSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdLinkRngResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the ranging system is active and can take on two values:&#xD;&#xA;- 'on': the ranging system is presently active, i.e., the ranging system is generating the ranging signal and modulating it on the forward link carrier;&#xD;&#xA;- 'off': the ranging system is presently not active as far as the forward link is concerned, i.e., no ranging signal is modulated on the forward link carrier." classifier="fwdLinkRngMod" stringIdentifier="forward-link-ranging-modulation" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="FwdLinkRngMod       &#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; on                  &#x9; &#x9; (0)&#xA;,&#x9; off                 &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="fwdLinkRngType must have a valid value. modType in the fwd401CarrierXmitSymbolStreamModType parameter must not be 'bpsk'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdLinkRngMod">
          <type xsi:type="frtypes:Enumerated">
            <values name="on"/>
            <values name="off" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports reports the type of ranging that is being used. It can take on the following values:&#xD;&#xA;- 'toneCode': the system performs range measurements in accordance with ECSS-E-50-02A or similar;&#xD;&#xA;- 'pseudoNoise': the system performs range measurements in accordance with CCSDS 414.1-B-2.&#xD;&#xA;In addition it permits to choose the Doppler compensation applied to the forward link ranging signal. It may be&#xD;&#xA;- 'noCompensation';&#xD;&#xA;- '1-way': the spacecraft &quot;sees&quot; the nominal ranging signal;&#xD;&#xA;- '2-way': the ESLT &quot;sees&quot; the nominal ranging signal." classifier="fwdLinkRngType" stringIdentifier="forward-link-ranging-type" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="FwdLinkRngType      &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; dopplerCompensation &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; noCompensation      &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; oneWay              &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; twoWay              &#x9; &#x9; (2)&#xA;&#x9; }&#xA;&#xA;,&#x9; rngType             &#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; toneCode            &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The engineering unit of this parameter is Hertz.&#xA;&#x9; &#x9; &#x9; toneFreq            &#x9; INTEGER  (100000 .. 1500000)&#xA;,&#x9; &#x9; &#x9; codeLength          &#x9; INTEGER  (1 .. 24)&#xA;,&#x9; &#x9; &#x9; toneWaveForm        &#x9; ENUMERATED&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; sine                &#x9; &#x9; (0)&#xA;&#x9; &#x9; &#x9; ,&#x9; square              &#x9; &#x9; (1)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; -- If dopplerCompensation is oneWay, the spacecraft will &quot;see&quot; the nominal tone frequency. If dopplerCompensation is twoWay, the ESLT will &quot;see&quot; the nominal tone frequency.&#xA;&#x9; &#x9; &#x9; dopplerCompensation &#x9; ENUMERATED&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; none                &#x9; &#x9; (0)&#xA;&#x9; &#x9; &#x9; ,&#x9; oneWay              &#x9; &#x9; (1)&#xA;&#x9; &#x9; &#x9; ,&#x9; twoWay              &#x9; &#x9; (2)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; -- The engineering unit of this element is seconds.&#xA;&#x9; &#x9; &#x9; codeComponentXmitDuration&#x9; INTEGER  (1 .. 1000000)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; pseudoNoise         &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; chipRate            &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; iis2                &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; i                   &#x9; INTEGER  (2)&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; k                   &#x9; INTEGER  (8 .. 10)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; &#x9; kis6                &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; i                   &#x9; INTEGER  (1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10 | 11 | 12 | 16 | 32 | 64)&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; k                   &#x9; INTEGER  (6)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; codeType            &#x9; ENUMERATED&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; t2bSine             &#x9; &#x9; (0)&#xA;&#x9; &#x9; &#x9; ,&#x9; t2bSquare           &#x9; &#x9; (1)&#xA;&#x9; &#x9; &#x9; ,&#x9; t4bSine             &#x9; &#x9; (2)&#xA;&#x9; &#x9; &#x9; ,&#x9; t4bSquare           &#x9; &#x9; (3)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="fwdLinkRngMod = 'off' and:&#xD;&#xA;if fwdLinkRngtype = 'toneCode', then the values of fwdLinkRngToneCodeToneFrequ and fwdLinkRngToneCodeCodeLength must be valid;&#xD;&#xA;if fwdLinkRngtype = 'pseudoNoise', then the values of fwdLinkRngPnChipRate and fwdLinkRngPnCodeType must be valid.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdLinkRngType">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="dopplerCompensation" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="noCompensation"/>
                <values name="oneWay" value="1"/>
                <values name="twoWay" value="2"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="rngType" optional="false">
              <type xsi:type="frtypes:Choice">
                <elements xsi:type="frtypes:Element" name="toneCode" tag="0" optional="false">
                  <type xsi:type="frtypes:Sequence">
                    <elements xsi:type="frtypes:Element" name="toneFreq" optional="false" comment="The engineering unit of this parameter is Hertz.">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="100000" max="1500000"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="codeLength" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="1" max="24"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="toneWaveForm" optional="false">
                      <type xsi:type="frtypes:Enumerated">
                        <values name="sine"/>
                        <values name="square" value="1"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="dopplerCompensation" optional="false" comment="If dopplerCompensation is oneWay, the spacecraft will &quot;see&quot; the nominal tone frequency. If dopplerCompensation is twoWay, the ESLT will &quot;see&quot; the nominal tone frequency.">
                      <type xsi:type="frtypes:Enumerated">
                        <values name="none"/>
                        <values name="oneWay" value="1"/>
                        <values name="twoWay" value="2"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="codeComponentXmitDuration" optional="false" comment="The engineering unit of this element is seconds.">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="1" max="1000000"/>
                      </type>
                    </elements>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="pseudoNoise" tag="1" optional="false">
                  <type xsi:type="frtypes:Sequence">
                    <elements xsi:type="frtypes:Element" name="chipRate" optional="false">
                      <type xsi:type="frtypes:Choice">
                        <elements xsi:type="frtypes:Element" name="i_is_2" tag="0" optional="false">
                          <type xsi:type="frtypes:Sequence">
                            <elements xsi:type="frtypes:Element" name="i" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <singleValueConstraint>
                                  <values>2</values>
                                </singleValueConstraint>
                              </type>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="k" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="8" max="10"/>
                              </type>
                            </elements>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="k_is_6" tag="1" optional="false">
                          <type xsi:type="frtypes:Sequence">
                            <elements xsi:type="frtypes:Element" name="i" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <singleValueConstraint>
                                  <values>1</values>
                                  <values>2</values>
                                  <values>3</values>
                                  <values>4</values>
                                  <values>5</values>
                                  <values>6</values>
                                  <values>7</values>
                                  <values>8</values>
                                  <values>9</values>
                                  <values>10</values>
                                  <values>11</values>
                                  <values>12</values>
                                  <values>16</values>
                                  <values>32</values>
                                  <values>64</values>
                                </singleValueConstraint>
                              </type>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="k" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <singleValueConstraint>
                                  <values>6</values>
                                </singleValueConstraint>
                              </type>
                            </elements>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="codeType" optional="false">
                      <type xsi:type="frtypes:Enumerated">
                        <values name="t2bSine"/>
                        <values name="t2bSquare" value="1"/>
                        <values name="t4bSine" value="2"/>
                        <values name="t4bSquare" value="3"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the code component currently being transmitted. If the FwdLinkRngType is 'pseudoNoise', or once the ambiguity has been resolved, this parameter is flagged as undefined." classifier="fwdLinkRngCodeComponent" stringIdentifier="forward-link-ranging-code-component" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="FwdLinkRngCodeComponent&#x9; ::= INTEGER  (1 .. 24)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdLinkRngCodeComponent">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="24"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdLinkRngResourceStat parameter." classifier="fwdLinkRngResourceStatChange" stringIdentifier="forwrd-link-ranging-resource-status-change" version="1" creationDate="2018-12-10T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdLinkRngResourceStat value that applies since the notified fwdLinkRngStatChange event occurred." classifier="fwdLinkRngEventResourceStatValue" stringIdentifier="forward-link-ranging-event-resource-status-value" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdLinkRngEventResourceStatValue&#x9; ::= FwdLinkRngResourceStat" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>2001</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdLinkRngEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.1/@functionalResource.1/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the Forward Link Ranging FR type. " classifier="fwdLinkRngSetContrParams" stringIdentifier="forward-link-ranging-set-control-parameters" version="1" creationDate="2018-11-13T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="The value of fwdLinkRngType must be valid.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2001</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the FwdLinkRng FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="fwdLinkRngContrParamIdsAndValues" stringIdentifier="forward-link-ranging-controlled-parameter-ids-and-values" version="1" creationDate="2019-09-06T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdLinkRngContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>2001</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdLinkRngContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <providedAncillaryInterface name="RngTimingSignal" requiringFunctionalResource="//@functionalResourceSet.3/@functionalResource.1"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="CCSDS 415 Forward Physical Channel Transmission" oidOffset="3000"/>
  <functionalResourceSet name="CCSDS 401 Return Physical Channel Reception" oidOffset="4000">
    <functionalResource SemanticDefinition="The Rtn401SpaceLinkCarrierRcpt FR accepts as input the carrier signal from the Antenna FR.&#xD;&#xA;It provides the symbol stream demodulated from the physical channel to the RtnTmSyncAndChannelDecode FR. It provides observables needed for the creation of radiometric data to the RangeAndDopplerExtraction FR.&#xD;&#xA;It provides the carrier waveform to the DdorRawDataCollection FR and to the OpenLoopRxFormatter FR. " classifier="Rtn401SpaceLinkCarrierRcpt" stringIdentifier="Return 401 Space Link Carrier Reception" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>4000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports on the return link carrier resource status and can take on four values:&#xD;&#xA;- 'configured': the return link equipment has been configured and the antenna moved to point, but due to geometry or spacecraft timeline, no carrier signal is expected to be seen at this time or the expected LOS time has passed where again LOS may be due to geometry or due to the spacecraft timeline;&#xD;&#xA;- 'operational': the return link is active, i.e., all receiving equipment is in nominal condition, the expected AOS time has passed and the expected LOS has not yet been reached;&#xD;&#xA;- 'interrupted': a failure has been detected, e.g. a receiver malfunction, that prevents the reception of the carrier signal;&#xD;&#xA;- 'halted': the return link has been taken out of service, e.g. due to a power failure affecting the return link string." classifier="rtn401CarrierRcptResourceStat" stringIdentifier="return-401-carrier-reception-resource-status" version="1" creationDate="2019-08-08T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Rtn401CarrierRcptResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can be set partially by the local EM, but not at all by cross support. Setting of the rtn401CarrierRcptResourceStat to 'operational' or 'interrupted' by means of the directive rtn401CarrierRcptSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the name assigned to the return physical channel. This name is a Visible String which has a length of 1 to 32 characters." classifier="rtn401CarrierRcptPhysChnlName" stringIdentifier="return-401-carrier-reception-physical-cannel-name" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="Rtn401CarrierRcptPhysChnlName&#x9; ::= VisibleString (SIZE( 1 .. 32)) " engineeringUnit="None" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptPhysChnlName">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="1" max="32"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the carrier modulation type and the associated parameters. The following options are selectable:&#xD;&#xA;- 'subcarrier': the symbol stream BPSK modulates a subcarrier which in turn phase modulates the carrier;&#xD;&#xA;- 'direct': the symbol stream directly bi-phase-L modulates the carrier with a modulation index resulting in a remnant carrier.&#xD;&#xA;- 'bpsk': the symbol stream bpsk modulates the carrier which means that the carrier is suppressed.&#xD;&#xA;- 'gmsk':&#xD;&#xA;- 'qpsk':&#xD;&#xA;&#xD;&#xA;The Symbol rate is specified in 1/1000 Hz." classifier="rtn401CarrierRcptModulationType" stringIdentifier="return-401-carrier-reception-modulation-type" version="1" creationDate="2019-08-08T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="Rtn401CarrierRcptModulationType&#x9; ::= CHOICE&#xA;{&#xA;&#x9; subcarrier          &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; symbolRate          &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The engineering unit of this element is 1/1000 symbols per second.&#xA;&#x9; &#x9; &#x9; absoluteSymbolRate  &#x9; [0]&#x9; &#x9; INTEGER  (1000 .. 32000000)&#xA;,&#x9; &#x9; &#x9; subcarrierFrequencySymbolRateRatio&#x9; [1]&#x9; &#x9; INTEGER  (2 .. 1024)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; subcarrierWaveform  &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; sine                &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; square              &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; -- The engineering unit of this element is 1/100 radian.&#xA;&#x9; &#x9; modulationIndexTelemetry&#x9; INTEGER  (1 .. 150)&#xA;,&#x9; &#x9; pcmFormat           &#x9; PcmFormat (nrzL)&#xA;&#x9; }&#xA;&#xA;,&#x9; direct              &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The engineering unit of this parameter is 1/1000 symbols per second.&#xA;&#x9; &#x9; symbolRate          &#x9; INTEGER  (8000000 .. 256000000)&#xA;,&#x9; &#x9; -- The engineering unit of this parameter is 1/100 radian.&#xA;&#x9; &#x9; modulationIndexTelemetry&#x9; INTEGER  (1 .. 150)&#xA;,&#x9; &#x9; pcmFormat           &#x9; PcmFormat (biPhaseL)&#xA;&#x9; }&#xA;&#xA;,&#x9; bpsk                &#x9; [2]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The engineering unit of this parameter is 1/1000 symbols per second.&#xA;&#x9; &#x9; symbolRate          &#x9; INTEGER  (64000000 .. 20000000000)&#xA;,&#x9; &#x9; pcmFormat           &#x9; PcmFormat (biPhaseL)&#xA;,&#x9; &#x9; -- Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.&#xA;&#x9; &#x9; matchedFilter       &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; matchedFilterOn     &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; matchedFilterOff    &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; -- This element specifies the symbol rate in 1/1000 per second.&#xA;&#x9; qpsk                &#x9; [3]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- This element specifies the symbol rate in 1/1000 symbols per second&#xA;&#x9; &#x9; symbolRate          &#x9; INTEGER  (1000 .. 20000000000)&#xA;,&#x9; &#x9; -- Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.&#xA;&#x9; &#x9; matchedFilter       &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; matchedFilterOn     &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; matchedFilterOff    &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; -- This element specifies the symbol rate in 1/1000 symbol per second.&#xA;&#x9; oqpsk               &#x9; [4]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- This element specifies the symbol rate in 1/1000 symbols per second&#xA;&#x9; &#x9; symbolRate          &#x9; INTEGER  (1000 .. 20000000000)&#xA;,&#x9; &#x9; -- Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.&#xA;&#x9; &#x9; matchedFilter       &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; matchedFilterOn     &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; matchedFilterOff    &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; gmsk                &#x9; [5]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- This element specifies the symbol rate in 1/1000 symbol per second.&#xA;&#x9; &#x9; symbolRate          &#x9; INTEGER  (1000 .. 20000000000)&#xA;,&#x9; &#x9; -- The scaling factor of this parameter is 1/100&#xA;&#x9; &#x9; bandwidthSymbolPeriodProduct&#x9; INTEGER  (0 .. 1000)&#xA;,&#x9; &#x9; concurrentGmskAndPnRng&#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; no                  &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; yes                 &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="depends on the element of the complex type" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptModulationType">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="subcarrier" tag="0" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="absoluteSymbolRate" tag="0" optional="false" comment="The engineering unit of this element is 1/1000 symbols per second.">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="1000" max="32000000"/>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="subcarrierFrequencySymbolRateRatio" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="2" max="1024"/>
                      </type>
                    </elements>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="subcarrierWaveform" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="sine"/>
                    <values name="square" value="1"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="modulationIndexTelemetry" optional="false" comment="The engineering unit of this element is 1/100 radian.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1" max="150"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="pcmFormat" optional="false">
                  <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                    <singleValueConstraint>
                      <values>0</values>
                    </singleValueConstraint>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="direct" tag="1" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false" comment="The engineering unit of this parameter is 1/1000 symbols per second.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="8000000" max="256000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="modulationIndexTelemetry" optional="false" comment="The engineering unit of this parameter is 1/100 radian.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1" max="150"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="pcmFormat" optional="false">
                  <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                    <singleValueConstraint>
                      <values>2</values>
                    </singleValueConstraint>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="bpsk" tag="2" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false" comment="The engineering unit of this parameter is 1/1000 symbols per second.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="64000000" max="20000000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="pcmFormat" optional="false">
                  <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.3">
                    <singleValueConstraint>
                      <values>2</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="matchedFilter" optional="false" comment="Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="matchedFilterOn"/>
                    <values name="matchedFilterOff" value="1"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="qpsk" tag="3" optional="false" comment="This element specifies the symbol rate in 1/1000 per second.">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false" comment="This element specifies the symbol rate in 1/1000 symbols per second">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1000" max="20000000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="matchedFilter" optional="false" comment="Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="matchedFilterOn"/>
                    <values name="matchedFilterOff" value="1"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="oqpsk" tag="4" optional="false" comment="This element specifies the symbol rate in 1/1000 symbol per second.">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false" comment="This element specifies the symbol rate in 1/1000 symbols per second">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1000" max="20000000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="matchedFilter" optional="false" comment="Enable or disable the square-root raised cosine matched filter depending on the filtering applied on the spacecraft side.">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="matchedFilterOn"/>
                    <values name="matchedFilterOff" value="1"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="gmsk" tag="5" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="symbolRate" optional="false" comment="This element specifies the symbol rate in 1/1000 symbol per second.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1000" max="20000000000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="bandwidthSymbolPeriodProduct" optional="false" comment="The scaling factor of this parameter is 1/100">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="1000"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="concurrentGmskAndPnRng" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="no"/>
                    <values name="yes" value="1"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the channel (polarization) that shall be used as input for reception and demodulation. It can take on the following values:&#xD;&#xA;- 'lhc';&#xD;&#xA;- 'rhc';&#xD;&#xA;- 'auto';&#xD;&#xA;- 'combining'.&#xD;&#xA;If 'auto' is chosen, the parameter specifies how much larger in 1/10 dB the power observed for the unselected polarization must be before a switch-over to the polarization with the stronger signal is performed.&#xD;&#xA;'combining' means that diversity combining of the lhc and rhc channels is performed. This is only permissible for modulation schemes with remnant carrier. This element of the choice specifies the bandwidth in Hertz within which the the two signals shall be combined" classifier="rtn401CarrierRcptPolarization" stringIdentifier="return-401-carrier-reception-polarization" version="1" creationDate="2019-10-02T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="Rtn401CarrierRcptPolarization&#x9; ::= CHOICE&#xA;{&#xA;&#x9; lhc                 &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; rhc                 &#x9; [1]&#x9; &#x9; NULL&#xA;,&#x9; -- The engineering unit of this element is 1/10 dB.&#xA;&#x9; autoHysteresis      &#x9; [2]&#x9; &#x9; INTEGER  (0 .. 100)&#xA;,&#x9; -- The enginnering unit of this element is Hertz.&#xA;&#x9; combiningBwdth      &#x9; [3]&#x9; &#x9; INTEGER  (10 .. 100000)&#xA;}&#xA;" engineeringUnit="none / 1/10 dB / Hz" configured="true" guardCondition="'combining'is only permissible if the rtn401CarrierRcptModulationType is either 'subcarrier' or 'direct'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptPolarization">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="lhc" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="rhc" tag="1" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="autoHysteresis" tag="2" optional="false" comment="The engineering unit of this element is 1/10 dB.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="100"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="combiningBwdth" tag="3" optional="false" comment="The enginnering unit of this element is Hertz.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="10" max="100000"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the level of the received signal in dBm as observed at the LNA input. When the Automatic Gain Control (AGC) is in coherent mode and the modulation scheme uses a remnant carrier, then the reported level refers to the carrier power. In all other cases, the reported level refers to the total signal power. " classifier="rtn401CarrierRcptSignalLevel" stringIdentifier="return-401-carrier-reception-signal-level" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="-- The engineering unit of this parameter is dBm&#xA;Rtn401CarrierRcptSignalLevel&#x9; ::= INTEGER  (-250 .. -30)" engineeringUnit="dBm" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptSignalLevel" comment="The engineering unit of this parameter is dBm">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-250" max="-30"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the system noise temperature in K derived from the noise density observed at the receiver input. As such, it takes into account all contributions to the noise temperature such as antenna microwave components, atmospheric noise and cosmic microwave background noise. The noise temperature varies with weather conditions and antenna elevation due to variation of the path length through the atmosphere and ground noise received by the antenna side lobes." classifier="rtn401CarrierRcptSystemNoiseTemperature" stringIdentifier="return-401-carrier-reception-system-noise-temperature" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="-- The engineering unit of this parameter is Kelvin.&#xA;Rtn401CarrierRcptSystemNoiseTemperature&#x9; ::= INTEGER  (1 .. 1000)" engineeringUnit="K" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptSystemNoiseTemperature" comment="The engineering unit of this parameter is Kelvin.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports (after acquisition of signal) on the power ratio of the signal received with left hand circular (LHC) polarization and the signal received with the orthogonal, i.e., right hand circular (RHC) polarization. If the angle reported is 0 degrees, then the full power is received via the LHC channel. At 45 degrees, the power in the LHC and RHC channels is equal, as if the input signal were linearly polarized. At 90 degrees, the full power is received with RHC polarization.&#xD;&#xA;Only stations supporting concurrent reception of LHC and RHC polarization provide this information. When this is not possible or the station is configured to use a single channel only, this parameter shall be flagged as unavailable. " classifier="rtn401CarrierRcptPolarizationAngle" stringIdentifier="return-401-carrier-reception-polarization-angle" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="-- The engineering unit of this parameter is degree.&#xA;Rtn401CarrierRcptPolarizationAngle&#x9; ::= INTEGER  (0 .. 90)" engineeringUnit="degree" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptPolarizationAngle" comment="The engineering unit of this parameter is degree.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="90"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the lock status for the carrier, if applicable for the subcarrier, and for the symbol stream." classifier="rtn401CarrierRcptLockStat" stringIdentifier="return-401-carrier-reception-lock-status" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="Rtn401CarrierRcptLockStat&#x9; ::= LockStat" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptLockStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.4"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the expected tone signal power to noise power in the tone tracking loop bandwidth in 1/10 dB." classifier="rtn401CarrierRcptPredictedToneLoopSnr" stringIdentifier="return-401-carrier-reception-predicted-tone-loop-signal-to-noise-ratio" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="-- The engineering unit of this parameter is 1/10 dB.&#xA;Rtn401CarrierRcptPredictedToneLoopSnr&#x9; ::= INTEGER  (0 .. 400)" engineeringUnit="dBHz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptPredictedToneLoopSnr" comment="The engineering unit of this parameter is 1/10 dB.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="400"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the observed tone signal power to noise power in the tone tracking loop bandwidth in 1/10 dB." classifier="rtn401CarrierRcptObservedToneLoopSnr" stringIdentifier="return-401-carrier-reception-observed-tone-loop-signal-to-noise-ratio" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="-- The engineering unit of this parameter is 1/10 dB.&#xA;Rtn401CarrierRcptObservedToneLoopSnr&#x9; ::= INTEGER  (0 .. 400)" engineeringUnit="1/10 dB" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptObservedToneLoopSnr" comment="The engineering unit of this parameter is 1/10 dB.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="400"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the nominal return carrier frequency disregarding any Doppler shift.&#xD;&#xA;" classifier="rtn401CarrierRcptNominalFreq" stringIdentifier="return-401-carrier-reception-nominal-frequency" version="1" creationDate="2019-09-30T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptNominalFreq&#x9; ::= INTEGER  (2200000000 .. 32300000000)" engineeringUnit="Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptNominalFreq" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2200000000" max="32300000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the band in Hz centered around the rtn401CarrierRcptNominalFrequ, possibly corrected for the expected Doppler offset, in which the receiver shall search for the carrier signal. This parameter is also valid in case of a suppressed carrier modulation scheme.&#xD;&#xA;" classifier="rtn401CarrierRcptFreqUncertainty" stringIdentifier="return-401-carrier-reception-frequency-uncertainty" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptFreqUncertainty&#x9; ::= INTEGER  (0 .. 1500000)" engineeringUnit="Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptFreqUncertainty" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="1500000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports for which condition the Doppler predicts have been calculated. It can take on four values:&#xD;&#xA;- none: no information regarding the expected Doppler shift is available and therefore the nominal frequencies are used;&#xD;&#xA;- 1-way: this mode will be used when the spacecraft is not locked to a forward link signal or while the spacecraft transponder is commanded to non-coherent mode or when the spacecraft receiver is in 'coherency enabled' mode and the forward link carrier frequency is ramped such that the Doppler on the forward link is compensated, i.e., the spacecraft always 'sees' the nominal forward link frequency; in this case it does not matter if the forward link is radiated by the same station as the one that is receiving the return link or a different station; &#xD;&#xA;- 2-way: this mode is applied when the spacecraft receiver is commanded to 'coherency enabled' mode and the station that is receiving the return link also radiates the forward link, the latter at a constant frequency;&#xD;&#xA;- 3-way: this mode is applied when the spacecraft receiver is in 'coherency enabled' mode and a station different from the one receiving the return link is radiating the forward link signal at a known constant frequency." classifier="rtn401CarrierRcptPredictMode" stringIdentifier="return-401-carrier-reception-predict-mode" version="1" creationDate="2019-09-10T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="Rtn401CarrierRcptPredictMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; none                &#x9; &#x9; (1)&#xA;,&#x9; oneWay              &#x9; &#x9; (2)&#xA;,&#x9; twoWay              &#x9; &#x9; (3)&#xA;,&#x9; threeWay            &#x9; &#x9; (4)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptPredictMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="none" value="1"/>
            <values name="oneWay" value="2"/>
            <values name="twoWay" value="3"/>
            <values name="threeWay" value="4"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the dual-sided tracking loop bandwidth in tenth Hz of the receiver and the duration in seconds within which the bandwidth reduction to the newly commanded loop bandwidth shall reached. This gradual change of the loop bandwidth is intended to avoid loss of lock. The duration for the gradual bandwidth change can be set to 'auto' or set to a specific duration. If the duration is set to 0, the newly commanded bandwidth is applied immediately. " classifier="rtn401CarrierRcptTrackingLoopBwdth" stringIdentifier="return-401-carrier-reception-tracking-loop-bandwidth" version="1" creationDate="2019-09-30T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="14" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptTrackingLoopBwdth&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The engineering unit of this element is Hertz.&#xA;&#x9; trackingLoopBwdth   &#x9; INTEGER  (1 .. 30000)&#xA;,&#x9; -- The engineering unit of this element is second.&#xA;&#x9; loopBwdthChangeDuration&#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The duration of the bandwidth reduction is chosen automatically&#xA;&#x9; &#x9; auto                &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; -- The engineering unit of this element is second. If it is set to 0, the newly commanded bandwidth is applied immediately.&#xA;&#x9; &#x9; bwdthChangeDuration &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 100)&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="1/10 Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptTrackingLoopBwdth" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="trackingLoopBwdth" optional="false" comment="The engineering unit of this element is Hertz.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="1" max="30000"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="loopBwdthChangeDuration" optional="false" comment="The engineering unit of this element is second.">
              <type xsi:type="frtypes:Choice">
                <elements xsi:type="frtypes:Element" name="auto" tag="0" optional="false" comment="The duration of the bandwidth reduction is chosen automatically">
                  <type xsi:type="frtypes:Null"/>
                </elements>
                <elements xsi:type="frtypes:Element" name="bwdthChangeDuration" tag="1" optional="false" comment="The engineering unit of this element is second. If it is set to 0, the newly commanded bandwidth is applied immediately.">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="100"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the order of the carrier tracking loop. It can take on the following values:&#xD;&#xA;- 'first order': such loop is hardly ever used because it has a static phase error even in case of a constant return link carrier frequency;&#xD;&#xA;- 'second order': this is the most commonly used loop as it has no static phase error for a constant return link carrier frequency;&#xD;&#xA;- 'third order': such configuration may have to be used in case of high Doppler rates, as such loop has no static phase error even when the return link carrier frequency is sweeping, but initial acquisition is more difficult with such loop. " classifier="rtn401CarrierRcptOrderOfLoop" stringIdentifier="return-401-carrier-reception-order-of-loop" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="15" typeDefinition="Rtn401CarrierRcptOrderOfLoop&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; firstOrder          &#x9; &#x9; (0)&#xA;,&#x9; secondOrder         &#x9; &#x9; (1)&#xA;,&#x9; thirdOrder          &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptOrderOfLoop">
          <type xsi:type="frtypes:Enumerated">
            <values name="firstOrder"/>
            <values name="secondOrder" value="1"/>
            <values name="thirdOrder" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="9This parameter reports the lock status of the the return in terms of carrier lock, subcarrier lock (if applicable) and symbol lock." classifier="rtn401CarrierRcptCarrierLockStat" stringIdentifier="return-401-carrier-reception-carrier-lock-status" version="1" creationDate="2018-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="16" typeDefinition="Rtn401CarrierRcptCarrierLockStat&#x9; ::= LockStat" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptCarrierLockStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.4"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the mean value of the phase error in 1/100 radians in the carrier tracking loop by accumulating the loop errors Ei over a period of n samples and then dividing by n. The mean M should be zero unless the loop is subject to a static phase error.&#xA;A given implementation shall specify the number of samples used to calculate this paramter. This shall be recorded in the Service Agreement." classifier="rtn401CarrierRcptLoopMeanPhaseError" stringIdentifier="return-401-carrier-reception-loop-mean-phase-error" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="17" typeDefinition="-- The engineering unit of this parameter is 1/100 radian.&#xA;Rtn401CarrierRcptLoopMeanPhaseError&#x9; ::= INTEGER  (-629 .. 629)" engineeringUnit="1/100 rad" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptLoopMeanPhaseError" comment="The engineering unit of this parameter is 1/100 radian.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-629" max="629"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the peak absolute value of the phase error |Ei - M| in 1/100 radians in the carrier tracking loop observed in the most recent n samples.&#xD;&#xA;A given implementation shall specify the number of samples used to calculate this parameter. This shall be recorded in the Service Agreement." classifier="rtn401CarrierRcptLoopPeakPhaseError" stringIdentifier="return-401-carrier-reception-loop-peak-phase-error" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="18" typeDefinition="-- The engineering unit of this parameter is 1/100 radian.&#xA;Rtn401CarrierRcptLoopPeakPhaseError&#x9; ::= INTEGER  (0 .. 629)" engineeringUnit="1/100 rad" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptLoopPeakPhaseError" comment="The engineering unit of this parameter is 1/100 radian.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="629"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the signal-to-noise ratio in the carrier tracking loop. It is derived from the carrier-loop-phase-error-std-deviation (std) as follows: SNR in 1/100 dB is given by 1000 log (2/(2PI std)^2) where log is to the base of 10. The carrier loop phase error standard deviation (or rms value) in rad over n samples, i.e. the sum of (Ei - M)^2 for i = 1 .. n is calculated, then divided by n and then the square root taken.&#xD;&#xA;A given implementation shall specify the number of samples used to calculate this parameter. This shall be recorded in the Service Agreement." classifier="rtn401CarrierRcptLoopSnr" stringIdentifier="return-401-carrier-reception-loop-signal-to-noise-ratio" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="19" typeDefinition="-- The engineering unit of this parameter is dB.&#xA;Rtn401CarrierRcptLoopSnr&#x9; ::= INTEGER  (-2800 .. 10000)" engineeringUnit="1/100 dB" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptLoopSnr" comment="The engineering unit of this parameter is dB.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-2800" max="10000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the observed carrier return link frequency in Hz. This parameter therefore varies with the Doppler shift induced by the radial velocity of the spacecraft relative to the ground based antenna. In 1-way mode, the Doppler shift applies only once, but also the onboard oscillator drift affects the observed return link carrier frequency. In 2-way mode in combination with a constant forward link frequency, the Doppler shift approximately doubles with respect to the 1-way case, but the contribution of the onboard oscillator drift is eliminated." classifier="rtn401CarrierRcptActualFreq" stringIdentifier="return-401-carrier-reception-actual-frequency" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="20" typeDefinition="-- The engineering unit of the parameter is Hertz.&#xA;Rtn401CarrierRcptActualFreq&#x9; ::= INTEGER  (2199700000 .. 38500000000)" engineeringUnit="Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptActualFreq" comment="The engineering unit of the parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2199700000" max="38500000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the observed carrier return link offset in Hz with respect to the nominal return link carrier frequency. As such it reports on the onboard oscillator drift (in case of 1-way operation) plus the Doppler shift induced by the radial velocity between spacecraft and the ground-based antenna. In 2-way mode, the effect of the onboard oscillator drift is eliminated. In combination with a constant forward link frequency, the Doppler shift approximately doubles compared to the 1-way case." classifier="rtn401CarrierRcptFreqOffset" stringIdentifier="return-401-carrier-reception-frequency-offset" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="21" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptFreqOffset&#x9; ::= INTEGER  (-5000000 .. 5000000)" engineeringUnit="Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>21</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>21</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptFreqOffset" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-5000000" max="5000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the forward link frequency in Hz that corresponds to the non-coherent return link frequency divided by the transponder turnaround ratio in the Doppler free case. The spacecraft is expected to lock on the forward link, when it 'sees' this frequency. " classifier="rtn401CarrierRcptBestLockFreq" stringIdentifier="return-401-carrier-reception-best-lock-frequency" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="22" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptBestLockFreq&#x9; ::= INTEGER  (2024956000 .. 40501863000)" engineeringUnit="Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>22</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>22</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptBestLockFreq" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2024956000" max="40501863000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the Doppler offset standard deviation in Hz over n samples. Each sample Si is the difference between the actual return link frequency and the predicted return link frequency where the predict takes into account the expected Doppler shift. Based on these samples, the mean Doppler offset M is calculated by forming the sum of n samples Si and dividing it by n. Then the sum of (Si - M)^2 for i = 1 .. n is calculated, then divided by n and then the square root taken." classifier="rtn401CarrierRcptDopplerStdDeviation" stringIdentifier="return-401-carrier-reception-doppler-standard-deviation" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="23" typeDefinition="-- The engineering unit of this parameter is Hertz.&#xA;Rtn401CarrierRcptDopplerStdDeviation&#x9; ::= INTEGER  (0 .. 5000000)" engineeringUnit="Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>23</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>23</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptDopplerStdDeviation" comment="The engineering unit of this parameter is Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="5000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the nominal subcarrier frequency in 1/1000 Hz. If the applicable modulation scheme does not use a subcarrier, this parameter shall be flagged as 'undefined'. Except if rtn401CarrierRcptPredictMode is set to 'none', the expected subcarrier frequency is determined by applying the 1-way offset because it is assumed that the subcarrier is generated by the spacecraft." classifier="rtn401CarrierRcptNominallSubcarrierFreq" stringIdentifier="return-401-carrier-reception-nominal-subcarrier-frequency" version="1" creationDate="2019-09-30T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="24" typeDefinition="-- Theengineering unit of this parameter is 1/1000 Hertz.&#xA;Rtn401CarrierRcptNominalSubcarrierFreq&#x9; ::= INTEGER  (2000000 .. 4000000000)" engineeringUnit="1/1000 Hz" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>24</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>24</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptNominalSubcarrierFreq" comment="Theengineering unit of this parameter is 1/1000 Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2000000" max="4000000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the subcarrier demodulator loop bandwidth expressed as symbol rate to subcarrier frequency ratio. If the applicable modulation scheme does not use a subcarrier, this parameter shall be flagged as undefined." classifier="rtn401CarrierRcptSubcarrierDemodLoopBwdth" stringIdentifier="return-401-carrier-reception-subcarrier-demodulator-loop-bandwidth" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="25" typeDefinition="Rtn401CarrierRcptSubcarrierDemodLoopBwdth&#x9; ::= REAL (1E-5 .. 1E-1)" engineeringUnit="none" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>25</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>25</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptSubcarrierDemodLoopBwdth">
          <type xsi:type="frtypes:Real">
            <rangeConstraint min="1E-5" max="1E-1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the subcarrier to carrier power ratio expressed in 1/100 dBc. If the applicable modulation scheme does not use a subcarrier, this parameter shall be flagged as 'undefined'." classifier="rtn401CarrierRcptSubcarrierLevelEstimate" stringIdentifier="return-401-carrier-reception-subcarrier-level-estimate" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="26" typeDefinition="-- The engineering unit of this parameter is 1/100 dBc.&#xA;Rtn401CarrierRcptSubcarrierLevelEstimate&#x9; ::= INTEGER  (-20000 .. 0)" engineeringUnit="1/100 dBc" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>26</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>26</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptSubcarrierLevelEstimate" comment="The engineering unit of this parameter is 1/100 dBc.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-20000" max="0"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the actually received subcarrier frequency in 1/1000 Hz, i.e., this parameter reflects the Doppler shift of the subcarrier frequency. If the applicable modulation scheme does not use a subcarrier, this parameter shall be flagged as 'undefined'." classifier="rtn401CarrierRcptActualSubcarrierFreq" stringIdentifier="return-401-carrier-reception-actual-subcarrier-frequency" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="27" typeDefinition="-- Theengineering unit of this parameter is 1/1000 Hertz.&#xA;Rtn401CarrierRcptActualSubcarrierFreq&#x9; ::= INTEGER  (2000000 .. 4000000000)" engineeringUnit="1/1000 Hz" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>27</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>27</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptActualSubcarrierFreq" comment="Theengineering unit of this parameter is 1/1000 Hertz.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="2000000" max="4000000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the symbol synchronizer loop bandwidth expressed as dual-sided symbol synchronizer loop bandwidth to symbol rate ratio. " classifier="rtn401CarrierRcptSymbolSynchronizerLoopBwdth" stringIdentifier="return-401-carrier-reception-symbol-synchronizer-loop-bandwidth" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="28" typeDefinition="Rtn401CarrierRcptSymbolSynchronizerLoopBwdth&#x9; ::= REAL (1E-5 .. 1E-2)" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>28</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>28</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptSymbolSynchronizerLoopBwdth">
          <type xsi:type="frtypes:Real">
            <rangeConstraint min="1E-5" max="1E-2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the actually received symbol stream rate in 1/1000 symbols/second, i.e., this parameter reflects the Doppler shift of the symbol rate." classifier="rtn401CarrierRcptActualSymbolRate" stringIdentifier="return-401-carrier-reception-actual-symbol-rate" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="29" typeDefinition="-- The engineering unit of this parameter is 1/1000 symbols per second.&#xA;Rtn401CarrierRcptActualSymbolRate&#x9; ::= INTEGER  (4000 .. 160000000000)" engineeringUnit="1/1000 symbols/s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptActualSymbolRate" comment="The engineering unit of this parameter is 1/1000 symbols per second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="4000" max="160000000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the estimated symbol energy over noise density ratio (Es/No) in 1/100 dB. The algorithms used to calculate this estimate tend to saturate at a certain Es/No level so that the reported estimate may be significantly too low. However, this saturation happens at levels that are so high that the telemetry is anyway virtually error free. It also reports the sistribution of the soft symbols in percent. The duration used for averaging the values reported by the rtn401CarrierRcptObservedEsOverNo parameter should be documented in the Service Agreement. " classifier="rtn401CarrierRcptObservedEsOverNoAndSoftSymbolDistribution" stringIdentifier="return-401-carrier-reception-observed-symbol-energy-over-noise-density-and-soft-symbol-distribution" version="1" creationDate="2019-10-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="30" typeDefinition="Rtn401CarrierRcptObservedEsOverNoAndSoftSymbolDistribution&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The engineering unit of this element is 1/100 dB&#xA;&#x9; esOverNo            &#x9; INTEGER  (-1000 .. 60000)&#xA;,&#x9; -- This element is reported in percent&#xA;&#x9; softBitDistribution &#x9; INTEGER  (-50 .. 50)&#xA;}&#xA;" engineeringUnit="1/100 dB" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptObservedEsOverNoAndSoftSymbolDistribution">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="esOverNo" optional="false" comment="The engineering unit of this element is 1/100 dB">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="-1000" max="60000"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="softBitDistribution" optional="false" comment="This element is reported in percent">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="-50" max="50"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the expected symbol energy over noise density ratio (Es/No) in 1/100 dB. Depending on the equipment, the value may be used to optimize carrier acquisition and lock detection in particular for very low values (e.g. - 2 dB) as they may be encountered for turbo coding with coding rates > 1/2. " classifier="rtn401CarrierRcptExpectedEsOverNo" stringIdentifier="return-401-carrier-reception-expected-symbol-energy-over-noise-density" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="31" typeDefinition="-- The engineering unit of this parameter is 1/100 dB.&#xA;Rtn401CarrierRcptExpectedEsOverNo&#x9; ::= INTEGER  (-1000 .. 60000)" engineeringUnit="1/100 dB" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>31</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>31</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptExpectedEsOverNo" comment="The engineering unit of this parameter is 1/100 dB.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-1000" max="60000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the difference between predicted and actual ranging signal power to noise density ratio expressed in 1/10 dB." classifier="rtn401CarrierRcptRngPowerOverNoResidual" stringIdentifier="return-401-carrier-reception-ranging-power-over-noise-density-ratio-residual" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="32" typeDefinition="-- The engineering unit of this parameter is 1/10 dB.&#xA;Rtn401CarrierRcptRngPowerOverNoResidual&#x9; ::= INTEGER  (-1000 .. 1000)" engineeringUnit="1/10 dB" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>32</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>32</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="Rtn401CarrierRcptRngPowerOverNoResidual" comment="The engineering unit of this parameter is 1/10 dB.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-1000" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the rtn401CarrierRcptResourceStat." classifier="rtn401CarrierRcptResourceStatChange" stringIdentifier="return-401-carrier-reception-resource-status-change" version="1" creationDate="2018-11-14T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the rtn401CarrierRcptResourceStat value that applies since the notified rtn401CarrierRcptResourceSatChange event has occurred." classifier="rtn401CarrierRcptEventResourceStatValue" stringIdentifier="return-401-carrier-reception-event-production-status-value" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Rtn401CarrierRcptEventResourceStatValue&#x9; ::= Rtn401CarrierRcptResourceStat" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="Rtn401CarrierRcptEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.3/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies any change of the lock status occurring when receiving or trying to acquire the return carrier and reports the lock status as given since the occurrence of the event." classifier="rtn401CarrierRcptLockStatChange" stringIdentifier="return-401-carrier-reception-lock-status-change" version="1" creationDate="2019-08-08T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the lock status as given since the occurrence of the rtn401CarrierRcptLockStatChange event. It should be noted that loss of carrier lock implies loss of subcarrier lock if a subcarrier is used and loss of symbol lock. Only the loss of carrier lock is reported in this case. Loss of subcarrier lock, if a subcarrier is used, implies loss of symbol lock. " classifier="rtn401CarrierRcptEventLockStat" stringIdentifier="return-401-carrier-reception-event-lock-status" version="1" creationDate="2019-09-10T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Rtn401CarrierRcptEventLockStat&#x9; ::= Rtn401CarrierRcptLockStat" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="Rtn401CarrierRcptEventLockStat">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.3/@functionalResource.0/@parameter.7/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the Rtn401SpaceLinkCarrierRcpt FR type. " classifier="rtn401CarrierRcptSetContrParams" stringIdentifier="rtn-401-carrier-reception-set-control-parameters" version="1" creationDate="2018-11-14T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the Rtn401SpaceLinkCarrierRcpt FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="rtn401SpaceLinkCarrierRcptContrParamIdsAndValues" stringIdentifier="rtn-401-carrier-reception-controlled-parameter-ids-and-values" version="1" creationDate="2018-12-10T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Rtn401SpaceLinkCarrierRcptContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="Rtn401SpaceLinkCarrierRcptContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="RtnPhysicalChnlSymbols" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.7/@functionalResource.0"/>
      <serviceAccesspoint name="ReceiveFrequAndRngSignal" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.3/@functionalResource.1"/>
      <providedAncillaryInterface name="AnalogWaveform" requiringFunctionalResource="//@functionalResourceSet.19/@functionalResource.0"/>
      <providedAncillaryInterface name="AnalogWaveform" requiringFunctionalResource="//@functionalResourceSet.18/@functionalResource.0"/>
    </functionalResource>
    <functionalResource SemanticDefinition="The Range and Doppler Extraction FR accepts as input the timing information from the Forward Link Ranging FR and the range and Doppler data from the Return 401 Space Link Carrier Reception FR.&#xD;&#xA;It provides range and Doppler observables to the TDM Segment Generation FR and to the Raw Radiometric Data Collection FR." classifier="RtnRngAndDopplerExtraction" stringIdentifier="Return Range and Doppler Extraction" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>4001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports on the return range and Doppler extraction resource status and can take on four values:&#xD;&#xA;- 'configured';&#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="rtnRngAndDopplerExtractionResourceStat" stringIdentifier="return-range-and-doppler-extraction-resource-status" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnRngAndDopplerExtractionResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can be set partially by the local EM, but not at all by x-support. Setting of the rtnRngAndDopplerExtractionResourceStat to 'operational' or 'interrupted' by means of the directive rtnRngAndDopplerExtractionSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports, in 1/1000 Hz, the dual sided bandwidth of the tone or PN chip tracking loop. " classifier="rtnRngAndDopplerExtractionToneOrPnChipLoop" stringIdentifier="return-range-and-Doppler-extraction-tone-or-pseudo-noise-chip-loop" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RtnRngAndDopplerExtractionToneOrPnChipLoop&#x9; ::= INTEGER  (1 .. 3000)" engineeringUnit="1/1000 Hz" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionToneOrPnChipLoop">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="3000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the duration of the period in milliseconds during which the tone tracking loop is operated in open-loop mode." classifier="rtnRngAndDopplerExtractionToneIntegrationTime" stringIdentifier="return-range-and-doppler-extraction-tone-integration-time" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="-- The engineering unit of this parameter is 1/1000 second.&#xA;RtnRngAndDopplerExtractionToneIntegrationTime&#x9; ::= INTEGER  (1 .. 10000)" engineeringUnit="1/1000 s" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionToneIntegrationTime" comment="The engineering unit of this parameter is 1/1000 second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="10000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the duration of the period in milliseconds after closure of the tone tracking loop until tone lock is reported and range measurements can start. This allows any residual loop transient to die away. " classifier="rtnRngAndDopplerExtractionToneSettlingTime" stringIdentifier="return-range-and-doppler-extraction-tone-settling-time" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is 1/1000 second.&#xA;RtnRngAndDopplerExtractionToneSettlingTime&#x9; ::= INTEGER  (1 .. 10000)" engineeringUnit="1/1000 s" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionToneSettlingTime" comment="The engineering unit of this parameter is 1/1000 second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="10000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the tone/ pn-chip to carrier power ratio expressed in 1/100 dBc." classifier="rtnRngAndDopplerExtractionToneOrPnChipLevel" stringIdentifier="return-range-and-Doppler-extraction-tone-or-pseudo-noise-chip-level" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="5" typeDefinition="RtnRngAndDopplerExtractionToneOrPnChipLevel&#x9; ::= INTEGER  (-20000 .. 0)" engineeringUnit="1/100 dB" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionToneOrPnChipLevel">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="-20000" max="0"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated value reports the tone respectively PN chip loop lock status. It can take on two values:&#xA;- 'locked';&#xA;- 'not locked'." classifier="rtnRngAndDopplerExtractionLoopLockStat" stringIdentifier="return-range-and-Doppler-extraction-loop-lock-status" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="RtnRngAndDopplerExtractionLoopLockStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; locked              &#x9; &#x9; (0)&#xA;,&#x9; notLocked           &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionLoopLockStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values name="notLocked" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports in 1/100 radians the expected ranging modulation index on the return link." classifier="rtnRngAndDopplerExtractionExpectedRngModIndex" stringIdentifier="return-range-and-Doppler-extraction-expected-ranging-modulation-index" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="-- The engineering unit of this parameter is 1/100 radian.&#xA;RtnRngAndDopplerExtractionExpectedRngModIndex&#x9; ::= INTEGER  (0 .. 140)" engineeringUnit="1/100 rad" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionExpectedRngModIndex" comment="The engineering unit of this parameter is 1/100 radian.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="140"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports if the ranging signal acquisition shall be presteered in accordance with the expected Doppler shift." classifier="rtnRngAndDopplerExtractionPresteering" stringIdentifier="return-range-and-doppler-extraction-presteering" version="1" creationDate="2019-10-17T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="RtnRngAndDopplerExtractionPresteering&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; dopplerPresteeringOn&#x9; &#x9; (0)&#xA;,&#x9; dopplerPresteeringOff&#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionPresteering">
          <type xsi:type="frtypes:Enumerated">
            <values name="dopplerPresteeringOn"/>
            <values name="dopplerPresteeringOff" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the code number up to which code number the replica has been correlated with the return link signal." classifier="rtnRngAndDopplerExtractionCodeNumberPnCodeOffset" stringIdentifier="return-range-and-Doppler-extraction-code-number-pseudo-noise-code-offset" version="1" creationDate="2019-10-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="RtnRngAndDopplerExtractionCodeNumberPnCodeOffset&#x9; ::= INTEGER  (0 .. 24)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionCodeNumberPnCodeOffset">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="24"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports in case of tone/code ranging if the ambiguity has been resolved and in case of PN ranging if lock on the PN sequence has been achieved. It can take on two values:&#xD;&#xA;- 'yes': the ranging system has reached the state where range measurements can be performed;&#xD;&#xA;- 'no': correlation of the forward link replica with the return link signal has not yet been achieved or has failed." classifier="rtnRngAndDopplerExtractionAmbiguityResolved" stringIdentifier="return-range-and-Doppler-extraction-ambiguity-resolved" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10" typeDefinition="RtnRngAndDopplerExtractionAmbiguityResolved&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; yes                 &#x9; &#x9; (0)&#xA;,&#x9; no                  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionAmbiguityResolved">
          <type xsi:type="frtypes:Enumerated">
            <values name="yes"/>
            <values name="no" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the expected duration of the PN code acquisition by the spacecraft." classifier="rtnRngAndDopplerExtractionExpectedSpacecraftPnAcqDuration" stringIdentifier="return-range-and-doppler-extraction-expected-spacecraft-pseudo-noise-acquisition-duration" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RtnRngAndDopplerExtractionExpectedSpacecraftPnAcqDuration&#x9; ::= INTEGER  (1 .. 1000)" engineeringUnit="s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionExpectedSpacecraftPnAcqDuration" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the integration time in seconds for the PN code correlator. This time has to be selected based on the known link budget and dynamics." classifier="rtnRngAndDopplerExtractionPnCodeIntegrationTime" stringIdentifier="return-range-and-doppler-extraction-pseudo-noise-code-integration-time" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RtnRngAndDopplerExtractionPnCodeIntegrationTime&#x9; ::= INTEGER  (1 .. 3600)" engineeringUnit="s" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionPnCodeIntegrationTime" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="3600"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports for PN ranging the mode in which the spacecraft transponder operates. It can take on two values:&#xD;&#xA;- 'passThrough';&#xD;&#xA;- 'regenerative'. " classifier="rtnRngAndDopplerExtractionSpacecraftTransponderMode" stringIdentifier="return-range-and-Doppler-extraction-spacecraft-transponder-mode" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="RtnRngAndDopplerExtractionSpacecraftTransponderMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; passThrough         &#x9; &#x9; (0)&#xA;,&#x9; regenerative        &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionSpacecraftTransponderMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="passThrough"/>
            <values name="regenerative" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the ranging measurement sampling rate in 1/1000 seconds." classifier="rtnRngAndDopplerExtractionRngMeasurementSamplingRate" stringIdentifier="return-range-and-doppler-extraction-range-measurement-sampling-rate" version="1" creationDate="2019-09-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="14" typeDefinition="-- The engineering unit of this parameter is 1/1000 second&#xA;RtnRngAndDopplerExtractionRngMeasurementSamplingRate&#x9; ::= INTEGER  (1 .. 3600000)" engineeringUnit="1/1000 s" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionRngMeasurementSamplingRate" comment="The engineering unit of this parameter is 1/1000 second">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="3600000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the Doppler measurement sampling rate in 1/1000 seconds." classifier="rtnRngAndDopplerExtractionDopplerMeasurementSamplingRate" stringIdentifier="return-range-and-doppler-extraction-doppler-measurement-sampling-rate" version="1" creationDate="2019-10-01T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="15" typeDefinition="-- The engineering unit of this parameter is 1/1000 second&#xA;RtnRngAndDopplerExtractionDopplerMeasurementSamplingRate&#x9; ::= INTEGER  (1 .. 3600000)" engineeringUnit="1/1000 s" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnRngAndDopplerExtractionDopplerMeasurementSamplingRate" comment="The engineering unit of this parameter is 1/1000 second">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="3600000"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the rtnRngAndDopplerExtractionResourceStat parameter value." classifier="rtnRngAndDopplerExtractionResourceStatChange" stringIdentifier="return-range-and-doppler-extraction-resource-status-change" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the rtnRngAndDopplerExtractionResourceStat value that applies since the notified rtnRngAndDopplerExtractionResourceStatChange event has occurred." classifier="rtnRngAndDopplerExtractionResourceStatValue" stringIdentifier="return-range-and-doppler-extraction-resource-status-value" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnRngAndDopplerExtractionResourceStatValue&#x9; ::= ResourceStat" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4001</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnRngAndDopplerExtractionResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event is notified if PN code acquisition has been unsuccessful after the following times have elapsed relative to the transmission of the signal:&#xD;&#xA;- 2-way light time,&#xD;&#xA;- expected on-board acquisition duration,&#xD;&#xA;- open-loop tone integration time (rtnRngAndDopplerExtractionToneIntegrationTime), &#xD;&#xA;- closed-loop tone settling time (rtnRngAndDopplerExtractionToneSettlingTime),&#xD;&#xA;- code integration time (rtnRngAndDopplerExtractionPnCodeIntegrationTime)." classifier="rtnRngAndDopplerExtractionPnCodeAcqFailure" stringIdentifier="return-range-and-doppler-extraction-pseudo-noise-code-acquisition-failure" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="This notification does not provide an event value." classifier="rtnRngAndDopplerExtractionPnCodeAcqFailureEventValue" stringIdentifier="return-range-and-doppler-extraction-pseudo-noise-code-acquisition-failure-event-value" version="1" creationDate="2019-09-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnRngAndDopplerExtractionPnCodeAcqFailureEventValue&#x9; ::= NULL" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4001</oidBit>
            <oidBit>2</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnRngAndDopplerExtractionPnCodeAcqFailureEventValue">
            <type xsi:type="frtypes:Null"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the RtnRngAndDopplerExtraction FR type. " classifier="rtnRngAndDopplerExtractionSetContrParams" stringIdentifier="return-range-and-doppler-extraction-set-control-parameters" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4001</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the RtnRngAndDopplerExtraction FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="rtnRngAndDopplerExtractionContrParamIdsAndValues" stringIdentifier="return-range-and-doppler-extraction controlled-parameter-ids-and-values" version="1" creationDate="2019-03-07T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnRngAndDopplerExtractionContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>4001</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnRngAndDopplerExtractionContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <providedAncillaryInterface name="RangeAndDoppler" requiringFunctionalResource="//@functionalResourceSet.16/@functionalResource.0"/>
      <providedAncillaryInterface name="RangeAndDoppler"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="CCSDS 415 Return Physical Channel Reception" oidOffset="5000"/>
  <functionalResourceSet name="Forward TC Synchronization and Channel Encoding" oidOffset="6000">
    <functionalResource SemanticDefinition="The fwdTcPlopSyncAndChannelEncoding FR accepts as input one of the following:&#xD;&#xA;- CLTUs;&#xD;&#xA;- TC frames to be radated via a specific physical channel.&#xD;&#xA;It also accepts the CLCWs extracted from the return link associated with the forward link used by this FR.&#xD;&#xA;This FR provides the symbol stream to be used for modulating the forward carrier of the physical channel associated with this FR. &#xD;&#xA;" classifier="FwdTcPlopSyncAndChnlEncode" stringIdentifier="Forward TC PLOP Sync and Channel Encode" version="1" creationDate="2019-03-12T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>6000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the fwdTcPlopSyncAndChnlEncoding FR resource status and can take on four values:&#xD;&#xA;- 'configured'; &#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="fwdTcPlopSyncResourceStat" stringIdentifier="forward-telecommand-plop-synchronization-resource-status" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncResourceStat&#x9; ::= ResourceStat" engineeringUnit="None" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the fwdTcPlopSyncResourceStat to 'operational' or 'interrupted' by means of the directive fwdTcPlopSyncSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the maximum length in octets of a CLTU the FR will forward to the fwd401SpaceLinkCarrierXmit FR. If the given FR instance is not configured to accept CLTUs as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="fwdTcPlopSyncMaxCltuLength" stringIdentifier="forward-telecommand-plop-synchronization-maximum-cltu-length " version="1" creationDate="2019-02-26T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="-- The engineering unit of this parameter is octet.&#xA;FwdTcPlopSyncMaxCltuLength&#x9; ::= INTEGER  (12 .. 4096)" engineeringUnit="octet" configured="true" guardCondition="The FR instance that the fwdTcPlopSyncSetContrParams directive setting this parameter is acting on must be configured to accept CLTUs as input.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncMaxCltuLength" comment="The engineering unit of this parameter is octet.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="12" max="4096"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports on the maximum number of TC MC frames that may be converted into a single CLTU. The value of this parameter must be '1' if  fwdTcPlopSyncMaxCltuRepetitions ≠ 1. If the given FR instance is not configured to accept TC MC frames as incoming service data unit, the value of this parameter shall be flagged as 'undefined'." classifier="fwdTcPlopSyncMaxNumberOfFramesPerCltu" stringIdentifier="forward-telecommand-plop-synchronization-maximum-number-of-frames-per-cltu" version="1" creationDate="2019-04-15T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="FwdTcPlopSyncMaxNumberOfFramesPerCltu&#x9; ::= INTEGER  (1 .. 15)" engineeringUnit="none" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncMaxNumberOfFramesPerCltu">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="15"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the encoding and randomization applied to the TC frames. " classifier="fwdTcPlopSyncEncodeType" stringIdentifier="forward-telecommand-plop-synchronization-encode-type" version="1" creationDate="2019-07-23T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="FwdTcPlopSyncEncodeType&#x9; ::= CHOICE&#xA;{&#xA;&#x9; noEncoding          &#x9; [0]&#x9; &#x9; Randomization (off)&#xA;,&#x9; bchEncoding         &#x9; [1]&#x9; &#x9; Randomization&#xA;,&#x9; ldpcEncoding        &#x9; [2]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; randomization       &#x9; Randomization (on)&#xA;,&#x9; &#x9; ldpcCoding          &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; code1               &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; n                   &#x9; INTEGER  (128)&#xA;,&#x9; &#x9; &#x9; &#x9; k                   &#x9; INTEGER  (64)&#xA;,&#x9; &#x9; &#x9; &#x9; tailSequence        &#x9; ENUMERATED&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; used                &#x9; &#x9; (0)&#xA;&#x9; &#x9; &#x9; &#x9; ,&#x9; notUsed             &#x9; &#x9; (1)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; code2               &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; n                   &#x9; INTEGER  (512)&#xA;,&#x9; &#x9; &#x9; &#x9; k                   &#x9; INTEGER  (256)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="If the 'Cltus' Service Access Point is used, the value of FwdTcPlopSyncEncodeType must be 'none'. Otherwise the value of FwdTcPlopSyncEncodeType is either 'bch' or 'ldpc'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncEncodeType">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="noEncoding" tag="0" optional="false">
              <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.11">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="bchEncoding" tag="1" optional="false">
              <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.11"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="ldpcEncoding" tag="2" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="randomization" optional="false">
                  <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.11">
                    <singleValueConstraint>
                      <values>1</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="ldpcCoding" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="code1" tag="0" optional="false">
                      <type xsi:type="frtypes:Sequence">
                        <elements xsi:type="frtypes:Element" name="n" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>128</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="k" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>64</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="tailSequence" optional="false">
                          <type xsi:type="frtypes:Enumerated">
                            <values name="used"/>
                            <values name="notUsed" value="1"/>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="code2" tag="1" optional="false">
                      <type xsi:type="frtypes:Sequence">
                        <elements xsi:type="frtypes:Element" name="n" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>512</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="k" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>256</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports how many times a given CLTU may at most be repeated on the forward link. The value of this parameter must be '1' if fwdTcPlopSyncMaxNumberOfFramesPerCltu ≠ 1. If the given instance of the FR is not configured to accept MC TC frames as incoming service data unit, the value of this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: If fwdTcPlopSyncAndChnlEncodingMaxCltuRepetitions = 1, the repeated transmissions option is either disabled or not supported." classifier="fwdTcPlopSyncMaxCltuRepetitions" stringIdentifier="forward-telecommand-plop-synchronization-maximum-cltu-repetitions" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="FwdTcPlopSyncMaxCltuRepetitions&#x9; ::= INTEGER  (1 .. 5)" engineeringUnit="none" configured="true" guardCondition="The FR instance the fwdTcPlopSyncSetContlParams directive is acting on must be configured to accept MC TC frames as input and fwdTcPlopSyncMaxNumberOfFramesPerCltu = 1. ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncMaxCltuRepetitions">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="5"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the Physical Layer Operation Procedure that the fwdTcPlopSyncAndChannelEncoding FR applies (PLOP1 or PLOP2). If PLOP1 applies, the parameter also specifies the length of the idle sequence (in octets).&#xD;&#xA;&#xD;&#xD;&#xA;The exact behavior of the FR as determined by the selected PLOP in effect is further described in the F-CLTU specification CCSDS 912.1-B-3.&#xD;&#xD;&#xA;" classifier="fwdTcPlopSyncPlop" stringIdentifier="forward-teleccommand-plop-synchronization-physical-layer-operations-procedure" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="FwdTcPlopSyncPlop   &#x9; ::= CHOICE&#xA;{&#xA;&#x9; -- The engineering unit of this element is cotet.&#xA;&#x9; plop1IdleSequenceLength&#x9; [0]&#x9; &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; plop2               &#x9; [1]&#x9; &#x9; NULL&#xA;}&#xA;" engineeringUnit="The engineering unit of the PLOP1 idle sequence length is octet." configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncPlop">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="plop1IdleSequenceLength" tag="0" optional="false" comment="The engineering unit of this element is cotet.">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="255"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="plop2" tag="1" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the size, in octets, of the bit pattern to be radiated to enable the spacecraft telecommand system to achieve bit lock. The radiation of the acquisition sequence will be performed in accordance with the applicable Physical Layer Operations Procedure (PLOP) - cf. fwdTcPlopSyncPlop." classifier="fwdTcPlopSyncAcqSeqLength" stringIdentifier="forward-telecommand-plop-synchronization-acquisition-sequence-length" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="-- The engineering unit of this parameter is octet.&#xA;FwdTcPlopSyncAcqSeqLength&#x9; ::= INTEGER  (0 .. 255)" engineeringUnit="octet" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncAcqSeqLength" comment="The engineering unit of this parameter is octet.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="255"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the minimum time, in microseconds, that the FR instance will ensure between the completion of the radiation of one CLTU and the beginning of the radiation of the following CLTU. The exact effect of the delay time depending on the PLOP is further described in the F-CLTU specification CCSDS 912.1-B-3.&#xD;&#xA;" classifier="fwdTcPlopSyncMinDelayTime" stringIdentifier="forward-telecommand-plop-synchronization-minimum-delay-time" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="-- The engineering unit of this parameter is 1/1000000 second, i.e. microsecond.&#xA;FwdTcPlopSyncMinDelayTime&#x9; ::= INTEGER  (1 .. 1000000)" engineeringUnit="1/1000000 s" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncMinDelayTime" comment="The engineering unit of this parameter is 1/1000000 second, i.e. microsecond.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1000000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the forward link as it can be derived from the Communication Link Control Word (CLCW) in the associated telemetry stream. It can take on the following values:&#xD;&#xA;- 'forward link status not available': no CLCWs from the spacecraft have been received by the service provider;&#xD;&#xA;- 'no rf available': the service provider has received at least one CLCW; in the last CLCW received by the service provider, the bit that flags ‘No RF Available’ was set to ‘1’;&#xD;&#xA;- 'no bit lock': the service provider has received at least one CLCW; in the last CLCW received by the service provider, the bit that flags ‘No RF Available’ was set to ‘0’, and the bit that flags ‘No Bit Lock’ was set to ‘1;&#xD;&#xA;- 'nominal': the provider has received at least one CLCW; in the last CLCW received by the provider, the bit that flags ‘No RF Available’ was set to ‘0’, and the bit that flags ‘No Bit Lock’ was set to ‘0’." classifier="fwdTcPlopSyncFwdLinkStat" stringIdentifier="forward-telecommand-plop-synchronization-forward-link-status" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="FwdTcPlopSyncFwdLinkStat&#x9; ::= FwdLinkStat" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncFwdLinkStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.10"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the Master or Virtual Channel that carries the CLCW to be used to determine the forward link RF and/or bit lock status, if applicable, and if and how the CLCW shall be evaluated for the Carrier Modulation Modes (CMM) transitions of the PLOP. The CLCW source is identified by the concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID). The range of the Spacecraft Identifier and the Virtual Channel Identifier depend on the TFVN as follows:&#xD;&#xA;- TFVN = 0 (version 1) - SCID = (0 .. 1023), VCID = (0 .. 7);&#xD;&#xA;- TFVN = 1 (version 2) - SCID = (0 .. 255), VCID = (0 .. 63)." classifier="fwdTcPlopSyncClcwEvaluation" stringIdentifier="forward-telecommand-plop-synchronization-clcw-evaluation" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" deprecated="true" typeDefinition="FwdTcPlopSyncClcwEvaluation&#x9; ::= CHOICE&#xA;{&#xA;&#x9; noEvaluation        &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; evaluation          &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; linkCondition       &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; noEvaluation        &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; rfAvailableVerified &#x9; &#x9; (1)&#xA;&#x9; &#x9; ,&#x9; bitLockVerified     &#x9; &#x9; (2)&#xA;&#x9; &#x9; ,&#x9; rfAvailableAndBitLockVerified&#x9; &#x9; (3)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; clcwSource          &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- This choice is the CLCWs to be extracted from telemetry frames.&#xA;&#x9; &#x9; &#x9; tfvn0               &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; &#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; &#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 7)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; -- This choice is for CLCW extraction from AOS frames.&#xA;&#x9; &#x9; &#x9; tfvn1               &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (1)&#xA;,&#x9; &#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; &#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; &#x9; -- This choice is for CLCW extraction from USLP frames.&#xA;&#x9; &#x9; &#x9; tfvn12              &#x9; [2]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (12)&#xA;,&#x9; &#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 65535)&#xA;,&#x9; &#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcPlopSyncClcwEvaluation">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="noEvaluation" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="evaluation" tag="1" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="linkCondition" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="noEvaluation"/>
                    <values name="rfAvailableVerified" value="1"/>
                    <values name="bitLockVerified" value="2"/>
                    <values name="rfAvailableAndBitLockVerified" value="3"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="clcwSource" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="tfvn0" tag="0" optional="false" comment="This choice is the CLCWs to be extracted from telemetry frames.">
                      <type xsi:type="frtypes:Sequence">
                        <elements xsi:type="frtypes:Element" name="tfvn" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>0</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="scid" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <rangeConstraint min="0" max="1023"/>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                          <type xsi:type="frtypes:Choice">
                            <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false">
                              <type xsi:type="frtypes:Null"/>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="0" max="7"/>
                              </type>
                            </elements>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="tfvn1" tag="1" optional="false" comment="This choice is for CLCW extraction from AOS frames.">
                      <type xsi:type="frtypes:Sequence">
                        <elements xsi:type="frtypes:Element" name="tfvn" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>1</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="scid" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <rangeConstraint min="0" max="255"/>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                          <type xsi:type="frtypes:Choice">
                            <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false">
                              <type xsi:type="frtypes:Null"/>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="0" max="63"/>
                              </type>
                            </elements>
                          </type>
                        </elements>
                      </type>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="tfvn12" tag="2" optional="false" comment="This choice is for CLCW extraction from USLP frames.">
                      <type xsi:type="frtypes:Sequence">
                        <elements xsi:type="frtypes:Element" name="tfvn" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <singleValueConstraint>
                              <values>12</values>
                            </singleValueConstraint>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="scid" optional="false">
                          <type xsi:type="frtypes:IntegerType">
                            <rangeConstraint min="0" max="65535"/>
                          </type>
                        </elements>
                        <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                          <type xsi:type="frtypes:Choice">
                            <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false">
                              <type xsi:type="frtypes:Null"/>
                            </elements>
                            <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                              <type xsi:type="frtypes:IntegerType">
                                <rangeConstraint min="0" max="63"/>
                              </type>
                            </elements>
                          </type>
                        </elements>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdTcPlopSyncResourceStat parameter." classifier="fwdTcPlopSyncResourceStatChange" stringIdentifier="forward-telecommand-plop-synchronization-resource-status-change" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdTcPlopSyncResourceStat value that applies since the notified fwdTcPlopSyncResourceStatChange event occurred." classifier="fwdTcPlopSyncEventResourceStatValue" stringIdentifier="forward-telecommand-plop-synchronization-event-resource-status-value" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncEventResourceStatValue&#x9; ::= FwdTcPlopSyncResourceStat" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>6000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcPlopSyncEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.5/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies any change of the fwdTcPlopSyncFwdLinkStat parameter." classifier="fwdTcPlopSyncFwdLinkStatChange" stringIdentifier="forward-telecommand-plop-sync-forward-link-status-change" version="1" creationDate="2018-12-15T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdTcPlopSyncFwdLinkStat value that applies since the notified fwdTcPlopSyncFwdLinkStatChange event occurred." classifier="fwdTcPlopSyncEventFwdLinkStatValue" stringIdentifier="forward-telecommand-plop-synchronization-event-forward-link-status-value" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncEventFwdLinkStatValue&#x9; ::= FwdTcPlopSyncFwdLinkStat" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>6000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcPlopSyncEventFwdLinkStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.5/@functionalResource.0/@parameter.8/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies when processing of the given data unit is completed. The data unit is identified by its data-unit-id and the service-instance-id of the service that submitted the data unit for processing." classifier="fwdTcPlopSyncDataUnitProcessingCompleted" stringIdentifier="forward-telecommand-plop-synchronization-data-unit-processing-completed" version="1" creationDate="2019-02-26T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="3">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value identifies the data unit that completed processing by reporting the data-unit-id of the data unit and the id of the service instance that submitted the data unit for processing. Note that the data unit may either be a frame or a CLTU." classifier="fwdTcPlopSyncDataUnitIdAndSourceValue" stringIdentifier="forward-telecommand-plop-synchronization-data-unit-id-and-source-value" version="1" creationDate="2019-04-15T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncDataUnitIdAndSourceValue&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; dataUnitId          &#x9; INTEGER  (0 .. 4294967295)&#xA;,&#x9; serviceInstanceIdentifier&#x9; ServiceInstanceId&#xA;}&#xA;" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>6000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcPlopSyncDataUnitIdAndSourceValue">
            <type xsi:type="frtypes:Sequence">
              <elements xsi:type="frtypes:Element" name="dataUnitId" optional="false">
                <type xsi:type="frtypes:IntegerType">
                  <rangeConstraint min="0" max="4294967295"/>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="serviceInstanceIdentifier" optional="false">
                <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.12"/>
              </elements>
            </type>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the FwdTcPlopSyncAndChnlEncode FR type. " classifier="fwdTcPlopSyncSetContrParams" stringIdentifier="forward-telecommand-plop-synchronization-set-control-parameters" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the fwdTcPlopSyncAndChnlEncode FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="fwdTcPlopSyncContrParamIdsAndValues" stringIdentifier="forward-telecommand-plop-synchronization-controlled-parameter-identifiers-and-values" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>6000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcPlopSyncContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <directives SemanticDefinition="When the FR receives this directive, it discards all data units that are associated with the service-instance-id specified in the directive qualifier." classifier="fwdTcPlopSyncDiscardDataUnits" stringIdentifier="forward-telecommand-plop-sync-discard-data-units" version="1" creationDate="2019-04-16T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The qualifier specifies the service-instance-id with which the data units to be discarded are associated." classifier="fwdTcPlopSyncServiceInstanceId" stringIdentifier="forward-telecommand-plop-synchronization-service-instance-id" version="1" creationDate="2019-04-15T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcPlopSyncServiceInstanceId&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; functResourceInstanceNumber&#x9; INTEGER  (1 .. 4294967295)&#xA;,&#x9; parameterValue      &#x9; ServiceInstanceId&#xA;}&#xA;" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>6000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcPlopSyncServiceInstanceId">
            <type xsi:type="frtypes:Sequence">
              <elements xsi:type="frtypes:Element" name="functResourceInstanceNumber" optional="false">
                <type xsi:type="frtypes:IntegerType">
                  <rangeConstraint min="1" max="4294967295"/>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="parameterValue" optional="false">
                <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.12"/>
              </elements>
            </type>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="Cltus" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.29/@functionalResource.0"/>
      <serviceAccesspoint name="FwdAllTcFrames" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.8/@functionalResource.0"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward Fixed Length Frame Synchronization, Channel Encodung, and OID Generation" oidOffset="7000">
    <functionalResource SemanticDefinition="This FR can be configured to accept one of the following input types:&#xD;&#xA;- CADUs to be radiated via a specific physical channel;&#xD;&#xA;- AOS frames to be radated via a specific physical channel. &#xD;&#xA;This FR provides the symbol stream to be used for modulating the forward carrier of the physical channel associated with this FR. " classifier="FwdAosSyncChnlEncodeAndOidGen" stringIdentifier="forward-aos-synchronization-channel-Encode-and-oid-generation" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="7000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>7000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the fwdAosSyncAndChnlEncoding FR resource status and can take on four values:&#xD;&#xA;- 'configured'; &#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="fwdAosSyncResourceStat" stringIdentifier="forward-aos-sychronization-resource-status" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdAosSyncResourceStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; value0              &#x9; &#x9; (0)&#xA;,&#x9; value1              &#x9; &#x9; (1)&#xA;}&#xA;" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the fwdAosSyncResourceStat to 'operational' or 'interrupted' by means of the directive fwdAosSyncSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncResourceStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="value0"/>
            <values name="value1" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the name assigned to the forward physical channel used to radiate &quot;AOS&quot; frames. This name is a Visible String which has a length of 1 to 32 characters." classifier="fwdAosSyncPhysicalChnlName" stringIdentifier="forward-aos-synchronization-physical-channel-name" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="FwdAosSyncPhysicalChnlName&#x9; ::= VisibleString (SIZE( 1 .. 32)) " engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncPhysicalChnlName">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="1" max="32"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the length in octets of a CADU the FR accepts for further processing. If the FR instance is not configured to accept AOS CADUs as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="fwdAosSyncCaduLength" stringIdentifier="forward-aos-synchronization-cadu-length " version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="3" typeDefinition="FwdAosSyncCaduLength&#x9; ::= NULL" engineeringUnit="octet" configured="true" guardCondition="incomingServiceDataUnit = AOS frame&#xD;&#xA;To be verified. Do we need this as configuration parameter? John's TN seems to cover only the case of frames as input service data units.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncCaduLength">
          <type xsi:type="frtypes:Null"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the FR randomizes the AOS frames. This parameter can take on two values:&#xD;&#xA;- 'randomization';&#xD;&#xA;- 'no randomization'.&#xD;&#xA;The details regarding the randomization are specified in CCSDS 131.0-B-2.  If the FR instance is not configued to accept AOS frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="fwdAosFrameRandomization" stringIdentifier="forward-aos-frame-randomization" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4" typeDefinition="FwdAosFrameRandomization&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; randomization       &#x9; &#x9; (0)&#xA;,&#x9; noRandomization     &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = MC Frame&#xD;&#xA;We need to clarify if and to which extent the permissible input service data units are to be configured.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosFrameRandomization">
          <type xsi:type="frtypes:Enumerated">
            <values name="randomization"/>
            <values name="noRandomization" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the encoding that is applied to the AOS frames. This parameter can take on the following values:&#xD;&#xA;- 'uncoded';&#xD;&#xA;- 'convolutional';&#xD;&#xA;- 'reedSolomon';&#xD;&#xA;- 'concatenated';&#xD;&#xA;- 'turbo';&#xD;&#xA;- 'ldpc'.&#xD;&#xA;If the FR instance is not configured to accept AOS Frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: The length and pattern of the ASM is determined by the applicable encoding." classifier="fwdAosSyncFrameEncoding" stringIdentifier="forward-aos-synchronization-frame-encoding" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="5" typeDefinition="FwdAosSyncFrameEncoding&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; uncoded             &#x9; &#x9; (0)&#xA;,&#x9; convolutional       &#x9; &#x9; (1)&#xA;,&#x9; reedSolomon         &#x9; &#x9; (2)&#xA;,&#x9; concatenated        &#x9; &#x9; (3)&#xA;,&#x9; turbo               &#x9; &#x9; (4)&#xA;,&#x9; ldpc                &#x9; &#x9; (5)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = AOS frame&#xD;&#xA;See comment for other parameters.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameEncoding">
          <type xsi:type="frtypes:Enumerated">
            <values name="uncoded"/>
            <values name="convolutional" value="1"/>
            <values name="reedSolomon" value="2"/>
            <values name="concatenated" value="3"/>
            <values name="turbo" value="4"/>
            <values name="ldpc" value="5"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the coding rate set for the convolutional encoding of the AOS frames. The convolutional encoding is applied to the ASM and the uncoded or Reed-Solomon encoded frame. This parameter can take on the following values:&#xD;&#xA;- 'rate1/2';&#xD;&#xA;- 'rate2/3';&#xD;&#xA;- 'rate3/4';&#xD;&#xA;- 'rate5/6';&#xD;&#xA;- 'rate7/8'.&#xD;&#xA;IF ((incoming service data unit NOT AOS frame) OR (fwdAosSyncFrameEncoding = 'reedSolomon') OR (fwdAosSyncFrameEncoding = 'turbo') OR (fwdAosSyncFrameEncoding = 'ldpc')), the value of this parameter shall be flagged as 'undefined'." classifier="fwdAosSyncFrameConvolCodeRate" stringIdentifier="forward-aos-synchronization-frame-convolutional-code-rate" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="FwdAosSyncFrameConvolCodeRate&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; rate12              &#x9; &#x9; (0)&#xA;,&#x9; rate23              &#x9; &#x9; (1)&#xA;,&#x9; rate34              &#x9; &#x9; (2)&#xA;,&#x9; rate56              &#x9; &#x9; (3)&#xA;,&#x9; rate78              &#x9; &#x9; (4)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="The FR instance is configured to accept AOS frames as input AND fwdAosSyncFrameEncoding = ('convolutional' OR 'concatenated').">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameConvolCodeRate">
          <type xsi:type="frtypes:Enumerated">
            <values name="rate1/2"/>
            <values name="rate2/3" value="1"/>
            <values name="rate3/4" value="2"/>
            <values name="rate5/6" value="3"/>
            <values name="rate7/8" value="4"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the correction capability of the applied Reed-Solomon code. It can take on the following values:&#xD;&#xA;- 'corrects8';&#xD;&#xA;- 'corrects16'.&#xD;&#xA;IF ((incoming service data unit NOT AOS Frame) OR NOT ((fwdAosSyncFrameEncoding = 'reedSolomon') OR (fwdAosSyncFrameEncoding = 'concatenated'))), the value of this parameter shall be flagged as 'undefined'." classifier="fwdAosSyncFrameRsErrorCorrectionCapability" stringIdentifier="forward-aos-synchronization-frame-rs-error-correction-capability" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="7" typeDefinition="FwdAosSyncFrameRsErrorCorrectionCapability&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; corrects8           &#x9; &#x9; (0)&#xA;,&#x9; corrects16          &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = 'AOS frame' AND fwdAosSyncFrameEncoding = ('reedSolomon' OR 'concatenated')">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameRsErrorCorrectionCapability">
          <type xsi:type="frtypes:Enumerated">
            <values name="corrects8"/>
            <values name="corrects16" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the interleaving depth of the applied Reed-Solomon encoding. It can take on the following values:&#xD;&#xA;- 'interleaving1';&#xD;&#xA;- 'interleaving2';&#xD;&#xA;- 'interleaving3';&#xD;&#xA;- 'interleaving4';&#xD;&#xA;- 'interleaving5';&#xD;&#xA;- 'interleaving8'.&#xD;&#xA;If ((incoming service data unit NOT MC Frame) OR NOT ((frame-encoding = 'reedSolomon') OR (frame-encoding = 'concatenated'))), the value of this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: If and how much virtual fill needs to be inserted is determined based on the values of transfer-frame-length and rs-interleaving-depth." classifier="fwdAosSyncFrameRsInterleavingDepth" stringIdentifier="forward-aos-synchronization-frame-rs-interleaving-depth" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="8" typeDefinition="FwdAosSyncFrameRsInterleavingDepth&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; interleaving1       &#x9; &#x9; (0)&#xA;,&#x9; interleaving2       &#x9; &#x9; (1)&#xA;,&#x9; interleaving3       &#x9; &#x9; (2)&#xA;,&#x9; interleaving4       &#x9; &#x9; (3)&#xA;,&#x9; interleaving5       &#x9; &#x9; (4)&#xA;,&#x9; interleaving8       &#x9; &#x9; (5)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = AOS frame AND fwdAosSyncFrameEncoding = ('reedSolomon' OR 'concatenated')">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameRsInterleavingDepth">
          <type xsi:type="frtypes:Enumerated">
            <values name="interleaving1"/>
            <values name="interleaving2" value="1"/>
            <values name="interleaving3" value="2"/>
            <values name="interleaving4" value="3"/>
            <values name="interleaving5" value="4"/>
            <values name="interleaving8" value="5"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the coding rate of the applied turbo encoding. This parameter can take on the following values:&#xD;&#xA;- 'rate1/2';&#xD;&#xA;- 'rate1/3';&#xD;&#xA;- 'rate1/4';&#xD;&#xA;- 'rate1/6'.&#xD;&#xA;If ((incoming service data unit NOT MC Frame) OR (frame-encoding ≠ 'turbo')), the value of this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: The information block length is determined by the value of aos-fwd-frame-length." classifier="fwdAosSyncFrameTurboCodeRate" stringIdentifier="forward-aos-synchronization-frame-turbo-code-rate" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9" typeDefinition="FwdAosSyncFrameTurboCodeRate&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; rate12              &#x9; &#x9; (0)&#xA;,&#x9; rate13              &#x9; &#x9; (1)&#xA;,&#x9; rate14              &#x9; &#x9; (2)&#xA;,&#x9; rate16              &#x9; &#x9; (3)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = AOS frame AND fwdAosSyncFrameEncoding = 'turbo'">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameTurboCodeRate">
          <type xsi:type="frtypes:Enumerated">
            <values name="rate1/2"/>
            <values name="rate1/3" value="1"/>
            <values name="rate1/4" value="2"/>
            <values name="rate1/6" value="3"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the coding rate of the applied ldpc encoding. This parameter can take on the following values:&#xD;&#xA;- 'rate1/2';&#xD;&#xA;- 'rate2/3';&#xD;&#xA;- 'rate3/4';&#xD;&#xA;- 'rate7/8'.&#xD;&#xA;If ((incoming service data unit NOT MC Frame) OR (fwdAosSyncFrameEncoding ≠ 'ldpc')), the value of this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: The information block length is determined by the value of transfer-frame-length." classifier="fwdAosSyncFrameLdpcCodeRate" stringIdentifier="forward-aos-synchronization-frame-ldpc-code-rate" version="1" creationDate="2019-02-27T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10" typeDefinition="FwdAosSyncFrameLdpcCodeRate&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; rate12              &#x9; &#x9; (0)&#xA;,&#x9; rate23              &#x9; &#x9; (1)&#xA;,&#x9; rate34              &#x9; &#x9; (2)&#xA;,&#x9; rate78              &#x9; &#x9; (3)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="incomingServiceDataUnit = AOS frame AND fwdAosSyncFrameEncoding = 'ldpc'">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdAosSyncFrameLdpcCodeRate">
          <type xsi:type="frtypes:Enumerated">
            <values name="rate1/2"/>
            <values name="rate2/3" value="1"/>
            <values name="rate3/4" value="2"/>
            <values name="rate7/8" value="3"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdAosSyncResourceStat parameter." classifier="fwdAosSyncResourceStatChange" stringIdentifier="forward-aos-synchronization-resource-status-change" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdAosSyncResourceStat value that applies since the notified fwdAosResourceStatChange event occurred." classifier="fwdAosSyncResourceStatValue" stringIdentifier="forward-aos-synchronization-resource-status-value" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdAosSyncResourceStatValue&#x9; ::= ResourceStat" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>7000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdAosSyncResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the fwdAosSyncAndChannelEncoding FR type.  " classifier="fwdAosSyncSetContrParams" stringIdentifier="forward-aos-synchronization-set-contr-params" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the fwdAosSyncAndChannelEncoding FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="fwdAosSyncParamIdsAndValues" stringIdentifier="forward-aos-synchronization-parameter-identifiers-and-values" version="1" creationDate="2018-12-16T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdAosSyncParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>7000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdAosSyncParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="Fwd All Frames" minAccessor="1" maxAccessor="1" minAccessed="1" maxAccessed="1"/>
      <serviceAccesspoint name="Fwd All Frames" minAccessor="1" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.9/@functionalResource.0"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Return TM Synchronization and Channel Decoding" oidOffset="8000">
    <functionalResource SemanticDefinition="The RtnTmSyncAndChnlDecode FR accepts as input the symbol stream from the Rtn401SpaceLinkCarrierRcpt FR. It provides the decoded and annotated telemetry frames to the RafTsProvider, the RcfTsProvider, the RocfTsProvider, to the McDemuxReception and to the TmFrameDataSink FRs.&#xD;&#xA;It provides the carrier waveform to the D-DOR Raw Data Collection FR and to the Open Loop Receiver/Formatter FR. " classifier="RtnTmSyncAndChnlDecode" stringIdentifier="Return Telemetry Synchronization and Channel Decoding" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8000" uses="//@functionalResourceSet.3/@functionalResource.0">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>8000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the return telemetry synchronization and decoding resource status and can take on four values:&#xD;&#xA;- 'configured': the return link synchronization and decoding equipment has been configured, but no incoming symbol stream is present;&#xD;&#xA;- 'operational': the return link is active, i.e., all syncronization and decoding is in nominal condition;&#xD;&#xA;- 'interrupted': a failure has been detected, e.g. the incoming data cannot be decoded;&#xD;&#xA;- 'halted': the return link has been taken out of service. " classifier="rtnTmSyncResourceStat" stringIdentifier="return-telemetry-synchronization-resource-status" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmSyncResourceStat&#x9; ::= ResourceStat" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the frame synchronizer behavior in terms of the correlation error threshold for declaring 'ASM lock', and for declaring 'ASM out of lock', the number of consecutive frames with 'ASM lock' required to transition from 'notLocked' to 'verify', the number of consecutive frames with 'ASM lock' required to transition from 'verify' to 'locked' and the number of consecutive frames with 'ASM out of lock' required to transition from 'locked' to 'notLocked' and the to be tolerated ASM position error in number of symbols (i.e. unexpected frame length) for not triggering the transition to 'notLocked'. " classifier="rtnTmSyncAsmConfig" stringIdentifier="return-telemetry-synchronization-asm-configuration" version="1" creationDate="2019-10-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RtnTmSyncAsmConfig  &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; asmCorrelationLockThreshold&#x9; INTEGER  (0 .. 191)&#xA;,&#x9; asmCorrelationOutOfLockThreshold&#x9; INTEGER  (1 .. 192)&#xA;,&#x9; verifyThreshold     &#x9; INTEGER  (1 .. 15)&#xA;,&#x9; lockedThreshold     &#x9; INTEGER  (1 .. 15)&#xA;,&#x9; notLockedThreshold  &#x9; INTEGER  (1 .. 15)&#xA;,&#x9; -- in number of symbols&#xA;&#x9; frameLengthErrorThreshold&#x9; INTEGER  (-10 .. 10)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncAsmConfig">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="asmCorrelationLockThreshold" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="191"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="asmCorrelationOutOfLockThreshold" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="1" max="192"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="verifyThreshold" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="1" max="15"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="lockedThreshold" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="1" max="15"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="notLockedThreshold" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="1" max="15"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="frameLengthErrorThreshold" optional="false" comment="in number of symbols">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="-10" max="10"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of symbols in the most recently received ASM that differ from the nominal ASM pattern. When comparing these numbers, one needs to take into consideration whether the synchronization is done on the (pre Viterbi decoder) symbol stream or on the (post Viterbi decoder) bit stream which may be the case if rtnTmSyncDecode is either 'convolutional' or 'concatenated'.&#xD;&#xA;The frame synchronization is also used to remove the phase ambiguity of some modulation schemes. To that end, the correlation process is performed both for the standard ASM pattern and its inversion.&#xD;&#xA;A given implementation will specify if the the synchronization is performed in the symbol or bit domain. This shall be recorded in the Service Agreement." classifier="rtnTmSyncAsmCorrelationError" stringIdentifier="return-telemetry-synchronization-asm-correlation-error" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RtnTmSyncAsmCorrelationError&#x9; ::= INTEGER  (0 .. 192)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncAsmCorrelationError">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="192"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the frame synchronizer lock status. It can take on the following values:&#xD;&#xA;- 'locked': The synchronizer has found at least two Attached Sync Markers with the expected number of symbols or bits between them and with each ASM having a correlation error that is lower than the configured error limit;&#xD;&#xA;- 'verify': the frame synchronizer has found one ASM in the symbol or bit stream with the correlation error lower than the configured threshold and is looking for a second ASM at the expected number of symbols or bits after the first ASM; if such ASM is found, the lock status changes to 'locked'; if no ASM is found where expected, the lock status changes to 'not locked'; if an ASM is found, but at a larger distance in terms of symbols or bits than expected, that newly found ASM will be the reference for searching the next verification ASM;&#xD;&#xA;- not locked: the frame synchronizer did not find any ASM with a correlation error below the correlation error limit. " classifier="rtnTmSyncFrameSyncLockStat" stringIdentifier="return-telemetry-synchronization-frame-synchronizer-lock-status" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="RtnTmSyncFrameSyncLockStat&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; locked              &#x9; &#x9; (0)&#xA;,&#x9; verify              &#x9; &#x9; (1)&#xA;,&#x9; notLocked           &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncFrameSyncLockStat">
          <type xsi:type="frtypes:Enumerated">
            <values name="locked"/>
            <values name="verify" value="1"/>
            <values name="notLocked" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if, based on the observed polarity of the ASM, the symbol stream had to be inverted or not. It can take two values:&#xD;&#xA;- 'yes': the frame synchronizer locked on the inverted ASM pattern and therefore inverts the polarity of the symbol stream;&#xD;&#xA;- 'no': the frame synchronizer locked on the ASM with the pattern in positive logic and therefore does not invert the symbol stream.&#xD;&#xA;As long as frame-synchronizer-lock-status is ≠ 'locked', this parameter shall be flagged as unavailable." classifier="rtnTmSyncSymbolInversion" stringIdentifier="return-telemtry-synchronization-symbol-inversion" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="RtnTmSyncSymbolInversion&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; yes                 &#x9; &#x9; (0)&#xA;,&#x9; no                  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncSymbolInversion">
          <type xsi:type="frtypes:Enumerated">
            <values name="yes"/>
            <values name="no" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="The parameter reports the ratio of erred frames to the total number of frames processed for the most recent n frames. The value of n shall be documented in the Service Agreeemnet. If the frames are RS or LDPC encoded, then frames for which the RS or LDPC correction is not possible are considered erred. Frames are also considered erred if the FECF is present and the check of the FECF is negative. In all other cases, i.e., if the frames are neither RS nor LDPC encoded nor do they contain a CCSDS compliant FECF, this parameter shall be flagged as 'undefined'." classifier="rtnTmSyncFrameErrorRate" stringIdentifier="return-telemetry-synchronization-frame-error-rate" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="RtnTmSyncFrameErrorRate&#x9; ::= REAL (0 .. 1)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncFrameErrorRate">
          <type xsi:type="frtypes:Real">
            <rangeConstraint min="0" max="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of bits in the frame corrected by means of the RS trailer. The number of bits that can be corrected depends on the coding and the interleaving depth. If rtnTmSyncDecode is neither 'reedSolomon' nor 'concatenated', this parameter shall be flagged as 'undefined'." classifier="rtnTmSyncNumberOfRsErrorsCorrected" stringIdentifier="return-telemetry-synchronization-number-of-rs-errors-corrected" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="RtnTmSyncNumberOfRsErrorsCorrected&#x9; ::= INTEGER  (0 .. 128)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncNumberOfRsErrorsCorrected">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="128"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the time tags used to annotate the telemetry frames with the Earth Receive Time (ERT) are generated by a time code generator slaved to a high precision reference or if this generator is free running. It can take on two values:&#xA;- 'yes' - The time tags are generated by a system that is locked to the station's frequency and timing system;&#xA;- 'no' - the system generating the time tags is free-running. " classifier="rtnTmSyncErtAnnotationLockedToReference" stringIdentifier="return-telemtry-synchronization-earth-receive-time-annotation-locked-to-reference" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="RtnTmSyncErtAnnotationLockedToReference&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; yes                 &#x9; &#x9; (0)&#xA;,&#x9; no                  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncErtAnnotationLockedToReference">
          <type xsi:type="frtypes:Enumerated">
            <values name="yes"/>
            <values name="no" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the length in octets of the CADU the frame synchronizer shall attempt to lock on. Depending on the implementation, the frame synchronizer may operate on the convolutionally encoded symbol stream or the already convolutionally decoded symbol stream, if convolutional encoding is applied at all." classifier="rtnTmSyncCaduLength" stringIdentifier="return-telemetry-synchronization-channel-access-data-unit-length" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="RtnTmSyncCaduLength &#x9; ::= INTEGER  (48 .. 32768)" engineeringUnit="number of symbols" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncCaduLength">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="48" max="32768"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the FR is configured to derandomize the incoming frames. This parameter can take on two values:&#xD;&#xA;- 'derandomization';&#xD;&#xA;- 'no derandomization'.&#xD;&#xA;The details regarding the randomization are specified in CCSDS 131.0-B-2.  " classifier="rtnTmSyncDerandomization" stringIdentifier="return-telemetry-synchronization-derandomization" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="RtnTmSyncDerandomization&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; derandomization     &#x9; &#x9; (0)&#xA;,&#x9; noDerandomization   &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncDerandomization">
          <type xsi:type="frtypes:Enumerated">
            <values name="derandomization"/>
            <values name="noDerandomization" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the decoding the RtnTmSyncAndDecoding FR is configured to apply to the incoming frames. This parameter can take on the following values:&#xD;&#xA;- 'no decoding';&#xD;&#xA;- 'convolutional';&#xD;&#xA;- 'reedSolomon';&#xD;&#xA;- 'concatenated';&#xD;&#xA;- 'turbo';&#xD;&#xA;- 'ldpc'.&#xD;&#xA;For the configured decoding option the parameter also specifies the associated configuration details.&#xD;&#xA;&#xD;&#xA;Note: The length and pattern of the ASM is implicitly specified by the decoding that is to be applied." classifier="rtnTmSyncDecode" stringIdentifier="return-telemetry-synchronization-decoding" version="1" creationDate="2019-07-24T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="RtnTmSyncDecode     &#x9; ::= CHOICE&#xA;{&#xA;&#x9; noDecoding          &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; convolutional       &#x9; [1]&#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; rate12              &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; rate23              &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; rate34              &#x9; &#x9; (2)&#xA;&#x9; ,&#x9; rate56              &#x9; &#x9; (3)&#xA;&#x9; ,&#x9; rate78              &#x9; &#x9; (4)&#xA;&#x9; }&#xA;&#xA;,&#x9; reedSolomon         &#x9; [2]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; rsCorrectionCapabilty&#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; corrects8           &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; corrects16          &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; rsInterleavingDepth &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; interleaving1       &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; interleaving2       &#x9; &#x9; (1)&#xA;&#x9; &#x9; ,&#x9; interleaving3       &#x9; &#x9; (2)&#xA;&#x9; &#x9; ,&#x9; interleaving4       &#x9; &#x9; (3)&#xA;&#x9; &#x9; ,&#x9; interleaving5       &#x9; &#x9; (4)&#xA;&#x9; &#x9; ,&#x9; interleaving8       &#x9; &#x9; (5)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; concatenated        &#x9; [3]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; convolutional       &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; rate12              &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; rate23              &#x9; &#x9; (1)&#xA;&#x9; &#x9; ,&#x9; rate34              &#x9; &#x9; (2)&#xA;&#x9; &#x9; ,&#x9; rate56              &#x9; &#x9; (3)&#xA;&#x9; &#x9; ,&#x9; rate78              &#x9; &#x9; (4)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; rsCorrectionCapabilty&#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; corrects8           &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; corrects16          &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; rsInterleavingDepth &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; interleaving1       &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; interleaving2       &#x9; &#x9; (1)&#xA;&#x9; &#x9; ,&#x9; interleaving3       &#x9; &#x9; (2)&#xA;&#x9; &#x9; ,&#x9; interleaving4       &#x9; &#x9; (3)&#xA;&#x9; &#x9; ,&#x9; interleaving5       &#x9; &#x9; (4)&#xA;&#x9; &#x9; ,&#x9; interleaving8       &#x9; &#x9; (5)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; turbo               &#x9; [4]&#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; rate12              &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; rate13              &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; rate14              &#x9; &#x9; (2)&#xA;&#x9; ,&#x9; rate16              &#x9; &#x9; (3)&#xA;&#x9; }&#xA;&#xA;,&#x9; ldpc                &#x9; [5]&#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; rate12              &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; rate23              &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; rate34              &#x9; &#x9; (2)&#xA;&#x9; ,&#x9; rate78              &#x9; &#x9; (3)&#xA;&#x9; }&#xA;&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncDecode">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="noDecoding" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="convolutional" tag="1" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="rate1/2"/>
                <values name="rate2/3" value="1"/>
                <values name="rate3/4" value="2"/>
                <values name="rate5/6" value="3"/>
                <values name="rate7/8" value="4"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="reedSolomon" tag="2" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="rsCorrectionCapabilty" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="corrects8"/>
                    <values name="corrects16" value="1"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="rsInterleavingDepth" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="interleaving1"/>
                    <values name="interleaving2" value="1"/>
                    <values name="interleaving3" value="2"/>
                    <values name="interleaving4" value="3"/>
                    <values name="interleaving5" value="4"/>
                    <values name="interleaving8" value="5"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="concatenated" tag="3" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="convolutional" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="rate1/2"/>
                    <values name="rate2/3" value="1"/>
                    <values name="rate3/4" value="2"/>
                    <values name="rate5/6" value="3"/>
                    <values name="rate7/8" value="4"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="rsCorrectionCapabilty" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="corrects8"/>
                    <values name="corrects16" value="1"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="rsInterleavingDepth" optional="false">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="interleaving1"/>
                    <values name="interleaving2" value="1"/>
                    <values name="interleaving3" value="2"/>
                    <values name="interleaving4" value="3"/>
                    <values name="interleaving5" value="4"/>
                    <values name="interleaving8" value="5"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="turbo" tag="4" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="rate1/2"/>
                <values name="rate1/3" value="1"/>
                <values name="rate1/4" value="2"/>
                <values name="rate1/6" value="3"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="ldpc" tag="5" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="rate1/2"/>
                <values name="rate2/3" value="1"/>
                <values name="rate3/4" value="2"/>
                <values name="rate7/8" value="3"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports if in the frame to be processed the FECF is present. It can take on two values:&#xD;&#xA;- 'yes';&#xD;&#xA;- 'no'." classifier="rtnTmSyncFecfPresent" stringIdentifier="return-telemetry-synchronization-fecf-present" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="RtnTmSyncFecfPresent&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; yes                 &#x9; &#x9; (0)&#xA;,&#x9; no                  &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmSyncFecfPresent">
          <type xsi:type="frtypes:Enumerated">
            <values name="yes"/>
            <values name="no" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the rtnTmSyncResourceStat parameter and the value of rtnTmSyncResourceStat that applies since the event has occurred." classifier="rtnTmSyncResourceStatChange" stringIdentifier="return-telemetry-synchronization-resource-status-change" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the rtnTmSyncResourceStat value that applies since the notified rtnTmSyncResourceStatChange occurred." classifier="rtnTmSyncEventResourceStatValue" stringIdentifier="return-telemetry-synchronization-resource-status-value" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmSyncEventResourceStatValue&#x9; ::= RtnTmSyncResourceStat">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>8000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnTmSyncEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.7/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event notifies any change of rtnTmSyncFrameSyncLockStat and the value of rtnTmSyncFrameSyncLockStat that applies since the event has occurred. " classifier="rtnTmSyncFrameSyncLockStatChange" stringIdentifier="return-telemetry-synchronization-frame-synchronizer-lock-status-change" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the tnTmSyncFrameSyncLockStatValue that applies since the notified rtnTmSyncFrameSyncLockStatChange event occurred." classifier="rtnTmSyncFrameSyncLockStatValue" stringIdentifier="return-telemetry-synchronization-frame-synchronizer-lock-status-value" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" typeDefinition="RtnTmSyncFrameSyncLockStatValue&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; locked              &#x9; &#x9; (0)&#xA;,&#x9; verify              &#x9; &#x9; (1)&#xA;,&#x9; notLocked           &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none">
          <externalTypeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>3</oidBit>
            <oidBit>23</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
          </externalTypeOid>
          <typeDef name="RtnTmSyncFrameSyncLockStatValue">
            <type xsi:type="frtypes:Enumerated">
              <values name="locked"/>
              <values name="verify" value="1"/>
              <values name="notLocked" value="2"/>
            </type>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the RtnTmSyncAndChnlDecode FR type.  " classifier="rtnTmSyncSetContrParams" stringIdentifier="return-telemetry-synchronization-set-control-parameters" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the RtnTmSyncAndChnlDecode FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="rtnTmSyncContrParamIdsAndValues" stringIdentifier="return-telemetry-synchronization-controlled-parameter-identifiers-and-values" version="1" creationDate="2019-08-12T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmSyncContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>8000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnTmSyncContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="allAnnotatedFrames" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.32/@functionalResource.0"/>
      <serviceAccesspoint name="allAnnotatedFrames" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.34/@functionalResource.0"/>
      <serviceAccesspoint name="allAannotatedFrames" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.33/@functionalResource.0"/>
      <serviceAccesspoint name="allAnnotatedFrames" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.13/@functionalResource.0"/>
      <serviceAccesspoint name="allAnnotatedFrames" minAccessor="0" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.11/@functionalResource.0"/>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward TC Space Link Protocol" oidOffset="9000">
    <functionalResource SemanticDefinition="This FR accepts TC frames without FECF belonging to one Master Channel.&#xD;&#xA;It provides all TC frames for one physical channel which optionally contain FECFs." classifier="FwdTcMcMux" stringIdentifier="Forward Telecommand Master Channel Multiplexer" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>9000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports on the return Forward TC MC Multiplexer resource status and can take on four values:&#xD;&#xA;- 'configured';&#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="fwdTcMcMuxResourceStat" stringIdentifier="forward-telecommand-master-channel-multiplexer-resource-status" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcMcMuxResourceStat&#x9; ::= ResourceStat" engineeringUnit="None" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the maximum number of TC MC frames that may be converted into a single CLTU. " classifier="fwdTcMcMuxMaxNumberOfFramesPerCltu" stringIdentifier="forward-telecommand-master-channel-multiplexer-maximum-number-of-frames-per-cltu" version="1" creationDate="2019-04-15T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="FwdTcMcMuxMaxNumberOfFramesPerCltu&#x9; ::= INTEGER  (1 .. 15)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxMaxNumberOfFramesPerCltu">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="15"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the maximum length in octets of a TC MC frame the FR accepts for further processing. The reported length refers to the frame length after insertion of the Frame Error Control Field if applicable. " classifier="fwdTcMcMuxMaxFrameLength" stringIdentifier="forward-telecommand-master-channel-maximum-frame-length" version="1" creationDate="2019-09-10T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="-- The engineering unit of this parameter is octet.&#xA;FwdTcMcMuxMaxFrameLength&#x9; ::= INTEGER  (1 .. 1024)" engineeringUnit="octet" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxMaxFrameLength" comment="The engineering unit of this parameter is octet.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1024"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configers and reports the Spacecraft IDs and consequently the Master Channels that are permitted on the given forward link.  " classifier="fspTcMcMuxValidScids" stringIdentifier="forward-telecommand-master-channel-multiplexer-valid-spacecraft-ids" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4" typeDefinition="FspTcMcMuxValidScids&#x9; ::= SEQUENCE  (SIZE( 1 .. 1024))  OF&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FspTcMcMuxValidScids">
          <type xsi:type="frtypes:SequenceOf">
            <sizeConstraint min="1" max="1024"/>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports how the TC Master Channels are multiplexed on the physical channel. In case fwdTcMcMuxScheme =  ‘fifo’, this parameter will be flagged as undefined. fwdTcMcMuxScheme = 'absolute priority', then this parameter contains a sequence of the SCIDs used on the forward link where the first SCID in the sequence has the highest priority, the second has the second-highest priority etc. Consequently the sequence has as many elements as Spacecraft Identifiers are permitted on the given physical channel. If fwdTcMcMuxScheme = 'polling vector', then the sequence consists of up to 3072 elements where each element is a SCID. Each SCID used in fwdTcMcMuxControl must be an element of fspTcMcMuxValidScids.&#xD;&#xA; If the given FR instance is not configured to accept TC MC frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'. " classifier="fwdTcMcMuxContr" stringIdentifier="forward-telecommand-master-channel-multiplexer-control" version="1" creationDate="2019-03-12T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="5" typeDefinition="FwdTcMcMuxContr     &#x9; ::= CHOICE&#xA;{&#xA;&#x9; fifo                &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; absolutePriority    &#x9; [1]&#x9; &#x9; SEQUENCE  (SIZE( 1 .. 1024))  OF&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; pollingVector       &#x9; [2]&#x9; &#x9; SEQUENCE  (SIZE( 1 .. 3072))  OF&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxContr">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="fifo" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="absolutePriority" tag="1" optional="false">
              <type xsi:type="frtypes:SequenceOf">
                <sizeConstraint min="1" max="1024"/>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="1023"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="pollingVector" tag="2" optional="false">
              <type xsi:type="frtypes:SequenceOf">
                <sizeConstraint min="1" max="3072"/>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="1023"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the FR inserts the Frame Error Control Field into each frame before passing it to the FwdTcPlopSyncAndChnlEncode FR. This parameter can take on two values:&#xD;&#xA;- 'present';&#xD;&#xA;- 'absent'.&#xD;&#xA;The details regarding the FEC are specified in CCSDS 232.0-B-2. " classifier="fwdTcMcMuxPresenceOfFec" stringIdentifier="forward-telecommand-master-channel-multiplexer-presence-of-frame-error-control" version="1" creationDate="2019-03-12T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="FwdTcMcMuxPresenceOfFec&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; present             &#x9; &#x9; (0)&#xA;,&#x9; absent              &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxPresenceOfFec">
          <type xsi:type="frtypes:Enumerated">
            <values name="present"/>
            <values name="absent" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdTcMcMuxResourceStat." classifier="fwdTcMcMuxResourceStatChange" stringIdentifier="forward-telecommand-master-channel-multiplexer-resource-status" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdTcMcMuxResourceStat value that applies since the notified fwdTcMcMuxResourceStatChange event has occurred." classifier="fwdTcMcMuxEventResourceStatValue" stringIdentifier="forward-telecommand-master-channel-multiplexer-event-resource-status-value" version="1" creationDate="2019-09-10T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcMcMuxEventResourceStatValue&#x9; ::= FwdTcMcMuxResourceStat" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcMcMuxEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.8/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the FwdTcMcMux FR type.  " classifier="FwdTcMcMuxSetContrParams" stringIdentifier="forward-telecommand-master-channel-multiplexer-set-control-parameters" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the FwdTcMcMux FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the TC MC Mux FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="fwdTcMcMuxContrParamIdsAndValues" stringIdentifier="forward-telecommand-master-channel-multiplexer-controlled-parameter-ids-and-values" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcMcMuxContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcMcMuxContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <directives SemanticDefinition="When receiving this directive, the FR discards all data units buffered at that time for the master channel multiplexing." classifier="fwdTcMcMuxDiscardDataUnits" stringIdentifier="forward-telecommand-master-channel-multiplexer-discard-data-units" version="1" creationDate="2019-03-12T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="This directive does not need a qualifier value.   " classifier="fwdTcMcMuxDiscardDataUnitsQualifier" stringIdentifier="forward-telecommand-master-channel-multiplexer-discard-data-units-qualifier" version="1" creationDate="2019-03-12T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcMcMuxDiscardDataUnitsQualifier&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; functResourceInstanceNumber&#x9; INTEGER  (1 .. 4294967295)&#xA;,&#x9; functResourceQualifiers&#x9; NULL&#xA;}&#xA;" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcMcMuxDiscardDataUnitsQualifier">
            <type xsi:type="frtypes:Sequence">
              <elements xsi:type="frtypes:Element" name="functResourceInstanceNumber" optional="false">
                <type xsi:type="frtypes:IntegerType">
                  <rangeConstraint min="1" max="4294967295"/>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="functResourceQualifiers" optional="false">
                <type xsi:type="frtypes:Null"/>
              </elements>
            </type>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="McFrames" minAccessor="1" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.8/@functionalResource.1"/>
    </functionalResource>
    <functionalResource SemanticDefinition="This FR accepts TC frames not containing FECFs for a specific TC VC as input.&#xD;&#xA;It provides the TC frames not containing FECFs for a specific TC Master Channel." classifier="FwdTcVcMux" stringIdentifier="forward-telecommand-virtual-channel-multiplexer" version="1" creationDate="2019-07-25T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>9001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports on the return Forward TC VC Multiplexer resource status and can take on four values:&#xD;&#xA;- 'configured';&#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="fwdTcVcMuxResourceStat" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-resource-status" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcVcMuxResourceStat&#x9; ::= ResourceStat" engineeringUnit="None" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMuxResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports on the maximum TC VC frame length in octets that the FR accepts as input for further processing where the specified length shall not take into account the Frame Error Control Field, if present on the physical channel.&#xD;&#xA;Note: The value of the maximum-tc-vc-frame-length parameter of an instance of this FR must be less than or equal to the parameter maximum-tc-mc-frame-length of the TC MC Mux FR consuming the TC MC frames generated by this TC VC Mux FR instance." classifier="fwdTcVcMaxFrameLength" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-maximum-frame-length" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="FwdTcVcMaxFrameLength&#x9; ::= INTEGER  (1 .. 1024)" engineeringUnit="octet" configured="true" guardCondition="fwdTcVcMaxFrameLength value ≤ fwdTcMcMuxMaxFrameLength value">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMaxFrameLength">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1024"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports how an AD frame on the given Virtual Channel shall be annotated in terms of number of times it shall be transmitted to the spacecraft. " classifier="fwdTcVcMuxAdFrameRepetitions" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-AD-frame-repetitions" version="1" creationDate="2019-04-15T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="FwdTcVcMuxAdFrameRepetitions&#x9; ::= SEQUENCE  (SIZE( 1 .. 64))  OF&#x9; &#x9; vcAdFrameRepetition &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; vcid                &#x9; INTEGER  (0 .. 63)&#xA;,&#x9; &#x9; repetitions         &#x9; INTEGER  (1 .. 5)&#xA;&#x9; }&#xA;" engineeringUnit="None" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMuxAdFrameRepetitions">
          <type xsi:type="frtypes:SequenceOf">
            <sizeConstraint min="1" max="64"/>
            <elements xsi:type="frtypes:Element" name="VcAdFrameRepetition" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="63"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="repetitions" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="1" max="5"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports how a BC frame on the given Virtual Channel shall be annotated in terms of number of times it shall be transmitted to the spacecraft. " classifier="fwdTcVcMuxBcFrameRepetitions" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-BC-frame-repetitions" version="1" creationDate="2019-04-15T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="FwdTcVcMuxBcFrameRepetitions   ::=   SEQUENCE (SIZE (1 .. 64) OF VcFrameRepetition   SEQUENCE&#xD;&#xA;   {   vcid                         INTEGER   (0 .. 63)&#xD;&#xA;   ,   frameRepetitions     INTEGER   (1 .. 5)&#xD;&#xA;   }" engineeringUnit="None" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the Master Channel that is provided by the given FR instance. The Master Channel ID is the concatenation of TFVN and SCID. Given that this FR type handles only TC frames, the TFVN is fixed and only the SCID is variable. " classifier="fwdTcVcMuxMc" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-master-channel" version="1" creationDate="2019-09-10T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="FwdTcVcMuxMc        &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="SCID value of fwdTcVcMuxMc ͼ {fwdTcMcMuxValidScids}">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMuxMc">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="tfvn" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports how the Virtual Channels are multiplexed into the Master Channel formed by this FR instance. In case fwdTcVcMuxScheme = ‘fifo’, this parameter will be flagged as undefined. If fwdTcVcMuxScheme = 'absolute priority', then this parameter contains a sequence of the VCIDs used on the given Master Channel where the first VCID in the sequence has the highest priority, the second has the second-highest priority etc. Consequently the sequence has as many elements as Virtual Channels exist on the Master Channel provided by the given FR instance. If fwdTcVcMuxScheme = 'polling vector', then the sequence consists of up to 192 elements where each element is a VCID. " classifier="fwdTcVcMuxContr" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-control" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="FwdTcVcMuxContr     &#x9; ::= CHOICE&#xA;{&#xA;&#x9; fifo                &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; absolutePriority    &#x9; [1]&#x9; &#x9; SEQUENCE  (SIZE( 1 .. 64))  OF&#x9; &#x9; &#x9; vcId                &#x9; INTEGER  (0 .. 63)&#xA;,&#x9; pollingVector       &#x9; [2]&#x9; &#x9; SEQUENCE  (SIZE( 1 .. 192))  OF&#x9; &#x9; &#x9; vcId                &#x9; INTEGER  (0 .. 63)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="Each vcId element of fwdTcVcMuxContr ͼ {fwdTcVcMuxValidTcVcIds} ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMuxContr">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="fifo" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="absolutePriority" tag="1" optional="false">
              <type xsi:type="frtypes:SequenceOf">
                <sizeConstraint min="1" max="64"/>
                <elements xsi:type="frtypes:Element" name="vcId" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="63"/>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="pollingVector" tag="2" optional="false">
              <type xsi:type="frtypes:SequenceOf">
                <sizeConstraint min="1" max="192"/>
                <elements xsi:type="frtypes:Element" name="vcId" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="63"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdTcVcMuxResourceStat." classifier="fwdTcVcMuxResourceStat" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-resource-status" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdTcVcMuxResourceStat value that applies since the notified fwdTcVcMuxResourceStatChange event has occurred." classifier="fwdTcVcMuxResourceStatValue" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-resource-status-value" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcVcMuxResourceStatValue&#x9; ::= ResourceStat" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9001</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcVcMuxResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the FwdTcVcMux FR type.  " classifier="fwdTcVcMuxSetContrParams" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-set-control-parameters" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the FwdTcVcMux FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="fwdTcVcMuxContrParamIdsAndValues" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-controlled-parameter-ids-and-values" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcVcMuxContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9001</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcVcMuxContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <directives SemanticDefinition="When receiving this directive, the FR discards all data units buffered at that time for the virtual channel multiplexing." classifier="fwdTcVcMuxDiscardDataUnits" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-discard-data-units" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9001</oidBit>
          <oidBit>3</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="This directive does not need a qualifier value.   " classifier="fwdTcVcMuxDiscardDataUnitsQualifier" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-discard-data-units-qualifier" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdTcVcMuxDiscardDataUnitsQualifier&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; functResourceInstanceNumber&#x9; INTEGER  (1 .. 4294967295)&#xA;,&#x9; functResourceQualifiers&#x9; NULL&#xA;}&#xA;" engineeringUnit="None">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9001</oidBit>
            <oidBit>3</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdTcVcMuxDiscardDataUnitsQualifier">
            <type xsi:type="frtypes:Sequence">
              <elements xsi:type="frtypes:Element" name="functResourceInstanceNumber" optional="false">
                <type xsi:type="frtypes:IntegerType">
                  <rangeConstraint min="1" max="4294967295"/>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="functResourceQualifiers" optional="false">
                <type xsi:type="frtypes:Null"/>
              </elements>
            </type>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="FwdTcFramesOfOneVcMinusFecf" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.8/@functionalResource.2"/>
      <serviceAccesspoint name="FwdTcFramesOfOneVcMinusFecf" minAccessor="0" maxAccessor="-1" minAccessed="1" maxAccessed="1"/>
    </functionalResource>
    <functionalResource SemanticDefinition="This FR instance accepts as input one of the following:&#xD;&#xA;- ENCAP.requests carrying IP packets; this type of input data gets converted to Encapsulation packets which are then passed to the VCP service. Such request will only be accepted if (the EPI in the request is 2 and 2 ͼ {valid-protocol-ids)) AND (PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}).&#xD;&#xA;- ENCAP.requests bearing CFDP data units;  this type of input data gets converted to Encapsulation packets which are then passed to the VCP service. Such request will only be accepted if (the EPI in the request is 3 AND 3 ͼ {valid-protocol-ids)) AND (the PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}).&#xD;&#xA;- VCP.requests carrying CFDP data units; such request will only be accepted if (the space packet header of the packet in the request has the APID 2045 AND 2045 ͼ {valid-apids}) AND (the PVN in the request is 1 AND 1 ͼ {valid-packet-version-numbers}).&#xD;&#xA;ENCAP.requests carrying forward file data units; this type of input data gets converted to Encapsulation packets which are then passed to the VCP service. Such request will only be accepted if (the EPI in the request is 7 AND 7 ͼ {valid-protocol-ids}) AND (the PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}).&#xD;&#xA;- VCP.requests carrying space packets; such requests will only be accepted if (the PVN in the request is 1 AND 1 ͼ {valid-packet-version-numbers}) and the APID contained in the header of the packet in the request ͼ valid-apids.&#xD;&#xA;- MAP Channel frame data units that are the result of MAP multiplexing; this input type is only accepted if presence-of-segment-header = 'present'. &#xD;&#xA;Regardless of the specific input type, incoming requests are only accepted if the VCID of the SDLP Channel / GVCID of the request is equal to tc-frame-vcid and the TVN of the GVCID in the request is 0.&#xD;&#xA;In deviation from CCSDS 232.0-B-2, the VCP.requests carrying space packets have an additional argument that flags if the packet of this request may be blocked, i.e. may be embedded in a TC frame together with other space packets or has to the single packet traveling in the given TC frame.&#xD;&#xA;In parallel it may accept as input&#xD;&#xA;- COP directives;&#xD;&#xA;- CLCWs extracted from the associated return link.&#xD;&#xA;This FR provides TC frames of a specific Virtual Channel. These frames do not (yet) contain the FECF." classifier="FwdTcEncapVcPktProcVcGen" stringIdentifier="TC Encap VC Pkt Processing &amp; VC Gen" version="1" creationDate="2019-03-26T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9002">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>9002</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports on the maximum TC VC frame length without FECF in octets that may result from processing the input to be accepted for further processing. &#xD;&#xA;Note: The value of the maximum-tc-vc-frame-length parameter of an instance of this FR must be less than or equal to the parameter TC VC Mux: maximum-tc-vc-frame-length of the TC VC Mux FR instance consuming the TC VC frames provided by this  TC Encap, VC Pkt Processing &amp; VC Gen FR instance." classifier="MaximumTcVcFrameLength" stringIdentifier="maximum-tc-vc-frame-length" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (5 .. 1024)" engineeringUnit="octet" configured="true" guardCondition="commanded-maximum-tc-vc-frame-length ≤ TC VC Mux: maximum-tc-vc-frame-length">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the VCID of the TC VC Frames the given FR instance provides. " classifier="TcFrameVcid" stringIdentifier="tc-frame-vcid" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="true" guardCondition="commanded-tc-frame-vcid ͼ {TC VC Mux: valid-tc-vcids}">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum length in octets that an incoming packet of a VCP.request or the data unit of an ENCAP.request may have to be accepted for further processing by this FR instance. " classifier="MaxPacketLength" stringIdentifier="max-packet-length" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (1 .. 4294967287)" engineeringUnit="octet" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of APIDs on of which an incoming VCP.request must have to be accepted by this FR instance." classifier="ValidApids" stringIdentifier="valid-apids" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4" typeDefinition="SEQUENCE (SIZE (1 .. 2048) OF IntUnsigned (0 .. 2047)" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if in the TC VC Frames generated by the given FR instance the segment header is present or absent. It can take on two values:&#xD;&#xA;- 'present';&#xD;&#xA;- 'absent'." classifier="PresenceOfSegmentHeader" stringIdentifier="presence-of-segment-header" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 1)&#xD;&#xA;{  present   (0)&#xD;&#xA;,   absent   (1)&#xD;&#xA;}" engineeringUnit="none" configured="true" guardCondition="The segment header must not be set to be absent if the active input data is of the type MAP Channel frame data units.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This  parameter reports on the Packet Version Numbers of packets that the FR may insert into the TC VC frames.&#xD;&#xA;Note: If '1' is not an element of the valid-packet-version-numbers parameter, then incoming VCP.requests are disregarded by the FR instance. If 8 is not an element of the valid-packet-version-numbers parameter, then incoming ENCAP.requests are disregarded by the FR instance. " classifier="ValidPacketVersionNumbers" stringIdentifier="valid-packet-version-numbers" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="SEQUENCE (SIZE (1 .. 2) OF IntPos ({1,8})" engineeringUnit="none" configured="true" guardCondition="If the FR instance shall accept ENCAP.requests as input, 8 must not be removed from valid-packet-version-numbers. If the FR instance shall accept VCP-requests as input, 1 must not be removed from valid-packet-version-numbers.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the minimum length in octets that the Encapsulation Data Unit embedded in the incoming ENCAP.request must have to be accepted as input by this FR instance. If an incoming ENCAP.request does not meet this requirement, it will be disregarded by this FR instance.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'." classifier="EncapMinDataUnitLength" stringIdentifier="encap-min-data-unit-length" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="7" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 4294967287)" engineeringUnit="octet" configured="true" guardCondition="8 ͼ {valid-packet-version-numbers} AND the resulting length of the Encapsulation Packet must not exceed the length specified in max-packet-length.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the maximum length in octets that the data unit of an incoming ENCAP.request may have. If an incoming request does not meet this requirement, that request will be disregarded by the FR.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'. " classifier="EncapMaxDataUnitLength" stringIdentifier="encap-max-data-unit-length" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="8" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 ..  4294967287)" engineeringUnit="octet" configured="true" guardCondition="8 ͼ {valid-packet-version-numbers} AND the resulting length of the Encapsulation Packet must not exceed the length specified in max-packet-length.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the set of protocol IDs one of which the EPI of an incoming  ENCAP.request must be to be accepted by this FR instance to be accepted as input.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'.&#xD;&#xA;NOTE: The only valid EPI values for the ENCAP.requests handled by this FR type are 2 and 3.  " classifier="ValidProtocolIds" stringIdentifier="valid-protocol-ids" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9" typeDefinition="SEQUENCE (SIZE (1 .. 8) OF IntUnsigned (0 .. 7)" engineeringUnit="none" configured="true" guardCondition="8 ͼ {valid-packet-version-numbers}">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the set of protocol ID extensions of which the EPI of an incoming ENCAP.request must contain one to be accepted by this FR instance, provided the protocol ID specified in the EPI is 6. The FR instance disregards incoming ENCAP.requests if the EPI specifies a protocol ID of 6, but the protocol id extension of the EPI does not specify a a value that is ͼ {valid-protocol-id-extensions}.  &#xD;&#xA;If ((8 NOT ELEMENT OF valid-packet-version-numbers) OR (data-field-content = 'virtual channel access service data unit') OR (6 NOT ELEMENT OF valid-protocol-ids)), this parameter shall be flagged as 'undefined'.&#xD;&#xA;NOTE: None of the ENCAP.request types handled by this FR instance is permitted to specify 6 as protocol ID. Thedrefore at least for now the parameter valid-protocol-id-extensions is specified only for the sake of completeness, but is not used.  " classifier="ValidProtocolIdExtensions" stringIdentifier="valid-protocol-id-extensions" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10" typeDefinition="SEQUENCE (SIZE (1 .. 16) OF IntUnsigned (0 .. 15)" engineeringUnit="none" configured="true" guardCondition="8 ͼ {valid-packet-version-numbers} AND 6 ͼ {valid-protocol-ids}">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies if on the given Virtual Channel the aggregation (blocking) of packets in a common TC VC frame is permitted. It can take on two values:&#xD;&#xA;- 'permitted';&#xD;&#xA;- 'prohibited'.&#xD;&#xA;Note: If blocking = 'prohibited', the permission of blocking in an incoming VCP.request is disregarded by this FR instance. " classifier="Blocking" stringIdentifier="blocking" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="11" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 1)&#xD;&#xA;&#xA;{   permitted   (0)&#xD;&#xA;&#xA;,   prohibited   (1)&#xD;&#xA;&#xA;}&#xD;&#xA;" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the period in 1/1000 seconds from inserting a first space packet into a Frame Data Field until this unit is passed to the FOP regardless of the number of packets contained; this timeout period is applicable to all blocking performed for the given VC. The parameter shall be flagged as undefined in case blocking = 'prohibited'." classifier="BlockingTimeoutPeriod" stringIdentifier="blocking-timeout-period" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="12" typeDefinition="SEQUENCE (SIZE (1)) OF Duration: milliseconds: IntUnsigned (100 .. 100000)" engineeringUnit="1/1000 s" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports how the FR instance shall act in case the transmission in AD mode of a TC frame is considered failed because the permitted number of TC frame retransmissions has been exhausted (cf. transmission-limit) and the permitted time from the moment when the last radiation has occurred until the frame should have been acknowledged by a CLCW (cf. t1-initial) has expired. The FOP offers two options which are reflected in the two values that this parameter can take on:&#xD;&#xA;-'alert': the AD mode is terminated and a FOP alert is generated;&#xD;&#xA;- 'suspend': the AD mode is suspended and may be resumed at a later point by invoking the dedicated directive.&#xD;&#xA;NOTE: This FR type does not offer an FR directive for setting this parameter. It can only be set by means of the related COP directive which may be sent by the F-SP-TS Provider FR." classifier="TimeoutType" stringIdentifier="timeout-type" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 1)   &#xD;&#xA;{   alert   (0)&#xD;&#xA;,   suspend   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports how many times a CLTU containing a TC Frame containing a Type-A service data unit of the given Virtual Channel shall be radiated.&#xD;&#xA;Note: This parameter must be equal or less than the parameter maximum-cltu-repetitions of the TC Sync and Channel Encoding FR instance generating the physical channel used to transfer the given Virtual Channel. " classifier="TypeARepetitions" stringIdentifier="type-a-repetitions" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="14" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (1 .. 5)" engineeringUnit="none" configured="true" guardCondition="type-a-repetions ≤ TC Sync and Channel Encoding: maximum-cltu-repetions">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports how many times a transfer frame containing a COP control service data unit shall be forwarded on the given Virtual Channel.&#xD;&#xA;NOTE: This parameter must be equal or less than the parameter maximum-cltu-repetitions of the TC Sync and Channel Encoding FR instance generating the physical channel used to transfer the given Virtual Channel. " classifier="CopControlRepetions" stringIdentifier="cop-control-repetions" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="15" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (1 .. 5)" engineeringUnit="none" configured="true" guardCondition="cop-control-repetions ≤ TC Sync and Channel Encoding: maximum-cltu-repetions">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the time, in microseconds, from the transmission of a TC transfer frame in AD mode, as long as not acknowledged by an incoming CLCW, until this frame will be retransmitted or, after the last permitted retransmission has happened (cf. transmission-limit), the FOP alert or AD mode suspension will occur.&#xD;&#xA;NOTE: This FR type does not offer an FR directive for setting this parameter. It can only be set by means of the related COP directive which may be sent by the F-SP-TS Provider FR." classifier="T1Initial" stringIdentifier="t1-initial" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="16" typeDefinition="SEQUENCE (SIZE (1)) OF Duration: microseconds: IntUnsigned" engineeringUnit="1/1000000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum number of times the first TC frame on the FOP Sent_Queue may be transmitted.&#xD;&#xA;NOTE: This FR type does not offer an FR directive for setting this parameter. It can only be set by means of the related COP directive which may be sent by the F-SP-TS Provider FR." classifier="TransmissionLimit" stringIdentifier="transmission-limit" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="17" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (1 .. 255)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of TC frames that may be transmitted on the given VC before an acknowledgement in the CLCW is required.&#xD;&#xA;NOTE: This FR type does not offer an FR directive for setting this parameter. It can only be set by means of the related COP directive which may be sent by the F-SP-TS Provider FR." classifier="FopSlidingWindowWidth" stringIdentifier="fop-sliding-window-width" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="18" typeDefinition="SEQUENCE (SIZE (1)) OF IntPos (1 .. 255)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the present state of the Frame Operation Procedure (FOP) as specified in CCSDS 232.1-B-2. The parameter can take on the following values:&#xD;&#xA;- 'active';&#xD;&#xA;- 'retransmit without wait';&#xD;&#xA;- 'retransmit with wait';&#xD;&#xA;- 'initializing without BC frame';&#xD;&#xA;- 'initializing with BC frame';&#xD;&#xA;- 'initial'.&#xD;&#xA;The FOP state table is explained in details in the CCSDS Communications Operation Procedure-1, CCSDS 232.1-B-2. " classifier="FopState" stringIdentifier="fop-state" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="19" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 5)&#xD;&#xA;{   active   (0)&#xD;&#xA;,   retransmit-without-wait   (1)&#xD;&#xA;,   retransmit-with-wait   (2)&#xD;&#xA;,   initializing-without-bc-frame   (3)&#xD;&#xA;,   initializing-with-bc-frame   (4)&#xD;&#xA;,   initial   (5)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the Frame Sequence Number, N(S), to be put in the Transfer Frame Primary Header of the next Type-AD frame to be transmitted after (re-)initialization of the AD service. This value will be equal to Transmitter_Frame_Sequence_Number, V(S).&#xD;&#xA;NOTE: This FR type does not offer an FR directive for setting this parameter. It can only be set by means of the related COP directive which may be sent by the F-SP-TS Provider FR." classifier="TransmitterFrameSequenceNumber" stringIdentifier="transmitter-frame-sequence-number" version="1" creationDate="2015-09-23T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="20" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 255)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the TC Encap VC Pkt Processing &amp; VC Gen FR type.  &#xD;&#xA;" classifier="SetTcEncapVcPktProcessingVcGenControlParameters" stringIdentifier="set-tc-encap-vc-pkt-processing-vc-gen-control-parameters" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9002</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the TC Encap VC Pkt Processing &amp;VC Gen FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="ControlledTcEncapVcPktProcessingVcGenParameterIdsAndValues" stringIdentifier="controlled-tc-encap-vc-pkt-processing-vc-gen-parameter-ids-and-values" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Will be entered when the format problem is solved" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>9002</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
        </qualifier>
      </directives>
    </functionalResource>
    <functionalResource classifier="FwdMapMux" stringIdentifier="MAP Mux" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9003">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>9003</oidBit>
      </oid>
    </functionalResource>
    <functionalResource SemanticDefinition="This FR instance accepts as input one of the following:&#xD;&#xA;- ENCAP.requests carrying IP packets; this type of input data gets converted to Encapsulation packets which are then passed to the MAPP service. Such request will only be accepted if (the EPI in the request is 2 and 2 ͼ {valid-protocol-ids)) AND (PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}) AND the GMAP ID specified in the SDLP Channel of the ENCAP.request identifies a MAP ID that is ͼ {valid-map-ids}&#xD;&#xA;- ENCAP.requests bearing CFDP data units;  this type of input data gets converted to Encapsulation packets which are then passed to the MAPP service. Such request will only be accepted if (the EPI in the request is 3 AND 3 ͼ {valid-protocol-ids)) AND (the PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}) AND the MAP ID contained in the GMAP ID conveyed in the SDLP_Channel parameter of the ENCAP.request is  ͼ {valid-map-ids}.&#xD;&#xA;- MAPP.requests carrying CFDP data units; such request will only be accepted if (the space packet header of the packet in the request has the APID 2045 AND 2045 ͼ {valid-apids}) AND (the PVN in the request is 1 AND 1 ͼ {valid-packet-version-numbers}) AND the MAP ID parameter of the MAPP.request is ͼ {valid-map-ids}.&#xD;&#xA;ENCAP.requests carrying forward file data units; this type of input data gets converted to Encapsulation packets which are then passed to the VCP service. Such request will only be accepted if (the EPI in the request is 7 AND 7 ͼ {valid-protocol-ids}) AND (the PVN in the request is 8 AND 8 ͼ {valid-packet-version-numbers}).&#xD;&#xA;- VCP.requests carrying space packets; such requests will only be accepted if (the PVN in the request is 1 AND 1 ͼ {valid-packet-version-numbers}) and the APID contained in the header of the packet in the request ͼ valid-apids.&#xD;&#xA;- MAP Channel frame data units that are the result of MAP multiplexing; this input type is only accepted if presence-of-segment-header = 'present'. &#xD;&#xA;Regardless of the specific input type, incoming requests are only accepted if the VCID of the SDLP Channel / GVCID of the request is equal to tc-frame-vcid and the TVN of the GVCID in the request is 0.&#xD;&#xA;In deviation from CCSDS 232.0-B-2, the VCP.requests carrying space packets have an additional argument that flags if the packet of this request may be blocked, i.e. may be embedded in a TC frame together with other space packets or has to the single packet traveling in the given TC frame.&#xD;&#xA;In parallel it may accept as input&#xD;&#xA;- COP directives;&#xD;&#xA;- CLCWs extracted from the associated return link.&#xD;&#xA;This FR provides TC frames of a specific Virtual Channel. These frames do not (yet) contain the FECF.Note: The present FR Model does not envisage the use of the &#xD;&#xA;MAPA.request primitive as an input to this FR type. Therefore, the &#xD;&#xA;parameters defined for this FR do not cover the case of incoming &#xD;&#xA;MAPA.request primitives. " classifier="FwdEncapMapPktProc" stringIdentifier="Encap &amp; MAP Pkt Processing" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="9004">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>9004</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports on the maximum transfer frame length in octets that is permitted on the given MAP Channel, where the specified length must take into account the Frame Error Control Field, if present on the given physical channel.&#xD;&#xA;Note: The value of the maximum-transfer-frame-length parameter of an instance of this FR must be less than or equal to the parameter maximum-transfer-frame-length of the TC Encapsulation, VC Packet Processing and VC Generation FR consuming the MAP channel generated by this Encapsulation, MAP Packet Processing and MAP Multiplexing FR instance." classifier="MaximumTransferFrameLength" stringIdentifier="maximum-transfer-frame-length" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (1 .. 1024)" engineeringUnit="octets" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This  parameter reports on the Packet Version Numbers of packets that the FR may insert into the MAP channel.&#xD;&#xA;Note 1: If 1 is not an  element of the set of supported Packet Version Numbers, then incoming MAPP requests are disregarded by the FR. If 8 is not an element of the set of supported Packet Version Numbers, then incoming ENCAPP requests are disregarded. If both versions are allowed, then the data insertion is performed in the same sequence as the requests are received by the FR.&#xD;&#xA;Note 2: This parameter must be a subset of the valid-packet-version-numbers parameter of the TC encapsulation, VC Pachet Processing and VC Generation FR instance that consumes the MAP channel generated by this FR instance. " classifier="ValidPacketVersionNumbers" stringIdentifier="valid-packet-version-numbers" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1 .. 2) OF IntUnsigned ({1,8})" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the minimum length in octets that the data unit argument of an incoming ENCAP request must have. If an incoming request does not meet this requirement, that request will be disregarded by the FR.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: The value of this parameter must be equal or greater than the encap-minimum-data-unit-length of the TC Encapsulation, VC Packet Processing and VC Generation FR instance that consumes the MAP channel generated by this FR instance. " classifier="EncapMinDataUnitLength" stringIdentifier="encap-min-data-unit-length" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 4294967287)" engineeringUnit="octets" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the maximum length in octets that the data unit argument of an incoming ENCAP request may have. If an incoming request does not meet this requirement, that request will be disregarded by the FR.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: The value of this parameter must equal or less than the encap-max-data-unit-length of the TC Encapsulation, VC Packet Processing and VC Generation FR instance that consumes the MAP channel generated by this FR instance. " classifier="EncapMaxDataUnitLength" stringIdentifier="encap-max-data-unit-length" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 4294967287)" engineeringUnit="octets" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the set of protocol IDs of which an encap request must contain one to be valid. The FR disregards incoming encap requests that are not valid.&#xD;&#xA;If (8 NOT ELEMENT OF valid-packet-version-numbers), this parameter shall be flagged as 'undefined'.&#xD;&#xA;Note: This parameter must be a subset of the valid-protocol-ids parameter of the TC Encapsulation, VC Packet Processing and VC Generation FR instance that consumes the MAP chnnel generated by this FR instance." classifier="ValidProtocolIds" stringIdentifier="valid-protocol-ids" version="1" creationDate="2016-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1 .. 8) OF IntUnsigned (0 .. 7)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the set of protocol ID extensions of which an encap request must contain one to be valid if the protocol ID of that request is '6'. The FR disregards incoming encap requests that are not valid.&#xD;&#xA;If ((8 NOT ELEMENT OF valid-packet-version-numbers) OR (6 NOT ELEMENT OF valid-protocol-ids)), this parameter shall be flagged as 'undefined'.  " classifier="ValidProtocolIdExtensions" stringIdentifier="valid-protocol-id-extensions" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1 .. 16) OF IntUnsigned (0 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the MAP IDs that are permitted on the Virtual Channel generated by the given FR instance and for each permitted MAP-ID the maximum packet length, if segmentation is permitted, if blocking is permitted and if so, which timeout period shall be applied." classifier="ValidMapIds" stringIdentifier="valid-map-ids" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1 .. 64) OF a sequence of IntUnsigned where  the sequence consists of up to 64 sequences where each of them has five elements. The first element specifies the MAP-ID, the second element specifies the maximum packet length applicable to the given MAP channel (6 .. 65537), the third element indicates whether segmentation of packets is permitted (0) or prohibited (1) on the given MAP channel, the fourth element specifies if more than one packet may be aggregated within one MAP channel frame data unit (blocking: permitted (0), prohibited (1)) and the fifth specifies the blocking timeout period in 1/1000 s and shall be set to zero in case blocking is prohibited." engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports on the MAP multiplexing scheme that the given FR instance applies to multiplex the MAP channels on the Virtual Channel. It can take on the following values:&#xD;&#xA;- 'fifo': the multiplexing is performed such that the MAPP requests queued for the different MAPs are inserted into the Virtual Channel in the same sequence as they get sent by the FRs generating these MAPP requests;&#xD;&#xA;- 'absolute priority': MAPP requests are multiplexed in the order of priority given to the MAP for which they have been queued where all priorities have to be different;&#xD;&#xA;- 'polling vector': the MAPP request queues of the MAP Channels are checked for MAPP requests ready to be inserted in the Virtual Channel according to the polling vector." classifier="MapMultiplexingScheme" stringIdentifier="map-multiplexing-scheme" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xD;&#xA;{   fifo   (0)&#xD;&#xA;,   absolutePriority   (1)&#xD;&#xA;,   pollingVector   (2)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on how the MAP Channels are multiplexed on the Virtual Channel. In case map-multiplexing-scheme = ‘fifo’, this parameter will be flagged as undefined. If map-multiplexing-scheme = 'absolute priority', then this parameter contains a sequence of the MAP IDs used on the given Virtual Channel where the first MAP ID in the sequence has the highest priority, the second has the second-highest priority etc. Consequently the sequence has as many elements as MAP Channels exist on the given Virtual Channel of the given mission. If map-multiplexing-scheme = 'polling vector', then the sequence consists of up to 192 elements where each element is a MAP ID. " classifier="MapMultiplexingControl" stringIdentifier="map-multiplexing-control" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1 .. 192) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9004</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward AOS Space Link Protocol" oidOffset="10000">
    <functionalResource SemanticDefinition="This FR accepts AOS frames without FECF belonging to one Master Channel.&#xD;&#xA;It provides all AOS frames for one physical channel.which optionally contain FECFs." classifier="FwdAosMcMux" stringIdentifier="AOS MC Mux" version="1" creationDate="2019-03-26T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>10000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the Spacecraft IDs and consequently the Master Channels that are permitted on the given physical channel.  If the given FR instance is not configured to accept AOS frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="ValidAosScids" stringIdentifier="valid-aos-scids" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="SEQUENCE (SIZE (1 .. 256) OF IntUnsigned (0 .. 255)" configured="true" guardCondition="incomingServiceDataUnit = AOS frame">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports on the scheme that the FR applies to multiplex if applicable the Master Channels on the physical channel. It can take on the following values:&#xD;&#xA;- 'fifo': the multiplexing is performed such that the MC Frames queued for the different Master Channels are multiplexed on the physical channel in the same sequence as they are received on the FR input;&#xD;&#xA;- 'absolute priority': the MC Frames are multiplexed in the order of priority given to the Master Channel for which they have been queued where all priorities have to be different;&#xD;&#xA;- 'polling vector': the MC Frame queues of the Master Channels are checked for MC Frames ready to be multiplexed on the physical channel according to a polling vector.&#xD;&#xA; If the given FR instance is not configured to accept MC frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="AosMcMultiplexingScheme" stringIdentifier="aos-mc-multiplexing-scheme" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 2)&#xD;&#xA;{   fifo   (0)&#xD;&#xA;,   absolute priority   (1)&#xD;&#xA;,   polling vector   (2)&#xD;&#xA;}" engineeringUnit="none" configured="false" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on how the Master Channels are multiplexed on the physical channel. In case aos-mc-multiplexing-scheme = ‘fifo’, this parameter will be flagged as undefined. If aos-mc-multiplexing-scheme = 'absolute priority', then this parameter contains a sequence of the SCIDs used on the physical channel where the first SCID in the sequence has the highest priority, the second has the second-highest priority etc. Consequently the sequence has as many elements as Spacecraft Identifiers are permitted on the physical channel. If aos-mc-multiplexing-scheme = 'polling vector', then the sequence consists of up to 768 elements where each element is a SCID. Each SCID used in aos-mc-multiplexing-control must be an element of valid-aos-scids.&#xD;&#xA; If the given FR instance is not configured to accept MC frames, the value of this parameter shall be flagged as 'undefined'. " classifier="AosMcMultiplexingControl" stringIdentifier="aos-mc-multiplexing-control" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="3" typeDefinition="SEQUENCE (SIZE (1 .. 768) OF IntUnsigned (0 .. 255)" engineeringUnit="none" configured="false" guardCondition="aos-mc-multiplexing-scheme ≠ 'fifo'">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the FR inserts the Header Error Control Field into each AOS frame. This parameter can take on two values:&#xD;&#xA;- 'present';&#xD;&#xA;- 'absent'." classifier="PresenceOfAosFrameHeaderErrorControl" stringIdentifier="presence-of-aos-frame-header-error-control" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 1)&#xD;&#xA;{   present   (0)&#xD;&#xA;,   absent   (1)&#xD;&#xA;}" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the length in octets of the Insert Zone which will be inserted into each frame before generating the corresponding CADU. If insert-zone-length = 0, then the Insert Zone is absent. If the FR instance is not configured to accept MC Frames as incoming service data units, the value of this parameter shall be flagged as 'undefined'." classifier="InsertZoneLength" stringIdentifier="insert-zone-length" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2040)" engineeringUnit="octet" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the FR inserts the Frame Error Control Field into each AOS frame before providing it to the Fwd AOS Sync and Channel Encoding FR. This parameter can take on two values:&#xD;&#xD;&#xA;- 'present';&#xD;&#xA;- 'absent'." classifier="PresenceOfAosFrameErrorControl" stringIdentifier="presence-of-aos-frame-error-control" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="6" typeDefinition="SEQUENCE (SIZE (1)) OF Enumerated (0 .. 1)&#xD;&#xA;{   present   (0)&#xD;&#xA;,   absent   (1)&#xD;&#xA;}" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the AOS MC Mux FR type.  &#xD;&#xA;" classifier="SetAosMcMuxControlParameters" stringIdentifier="set-aos-mc-mux-control-parameters" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the AOS MC Mux FR and the parameter value must be of the same type as the parameter value that shall be set. " classifier="ControlledAosMcMuxParametersIdsAndValues" stringIdentifier="controlled-aos-mc-mux-parameters-ids-and-values" version="1" creationDate="2015-11-05T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="Will be entered when the format problem is solved" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>10000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
        </qualifier>
      </directives>
    </functionalResource>
    <functionalResource SemanticDefinition="None" classifier="FwdAosVcMux" stringIdentifier="AOS VC Multiplexing" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10001" deprecated="true">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>10001</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports on the spacecraft ID of the Master Channel that is generated by the given FR instance. " classifier="MasterChannel" stringIdentifier="master-channel" version="1" creationDate="2014-06-02T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 255)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the VCIDs that are permitted on the Master Channel generated by the given FR instance." classifier="ValidVcIds" stringIdentifier="valid-vc-ids" version="1" creationDate="2014-06-02T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1 .. 64) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports on the VC multiplexing scheme that the given FR instance applies to multiplex the Virtual Channels on the Master Channel. It can take on the following values:&#xD;&#xA;- 'fifo': the multiplexing is performed such that the AOS VC frames queued for the different Virtual Channels are inserted into the Master Channel in the same sequence as they get queued by the FRs generating these frames;&#xD;&#xA;- 'absolute priority': AOS VC Frames are multiplexed in the order of priority given to the Virtual Channel for which they have been queued where all priorities have to be different;&#xD;&#xA;- 'polling vector': the AOS VC frame queues of the Virtual Channels are checked for AOS VC frames ready to be inserted in the Master Channel according to the polling vector." classifier="VcMultiplexingScheme" stringIdentifier="vc-multiplexing-scheme" version="1" creationDate="2014-06-02T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xD;&#xA;{   fifo   (0)&#xD;&#xA;,   absolutePriority   (1)&#xD;&#xA;,   pollingVector   (2)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on how the Virtual Channels are multiplexed on the Master Channel. In case aos-vc-multiplexing-scheme = ‘fifo’, this parameter will be flagged as undefined. If aos-vc-multiplexing-scheme = 'absolute priority', then this parameter contains a sequence of the VCIDs used on the given Master Channel where the first VCID in the sequence has the highest priority, the second has the second-highest priority etc. Consequently the sequence has as many elements as Virtual Channels exist on the given Master Channel of the given mission. If aos-vc-multiplexing-scheme = 'polling vector', then the sequence consists of up to 192 elements where each element is a VCID. " classifier="VcMultiplexingControl" stringIdentifier="vc-multiplexing-control" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1 .. 192) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
    <functionalResource SemanticDefinition="None" classifier="FwdAosEncapPktProcAndVcGen" stringIdentifier="AOS Encapsulation, Packet Processing and VC Generation" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="10002">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>10002</oidBit>
      </oid>
      <parameter SemanticDefinition="This  parameter reports on the Packet Version Numbers of packets that the FR may insert into the AOS VC Channel.&#xD;&#xA;Note 1: If 1 is not an element of the set of supported Packet Version Numbers, then incoming VCP requests are disregarded by the FR. If 8 is not an element of the set of supported Packet Version Numbers, then incoming ENCAP requests are disregarded. If both versions are allowed, then the data insertion is performed in the same sequence as the requests are received by the FR." classifier="ValidPacketVersionNumbers" stringIdentifier="valid-packet-version-numbers" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1 .. 2) OF IntUnsigned ({1,8})" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the maximum length in octets that a packet contained in an incoming PACKET or ENCAP request may have. The FR will disregard requests containing packets that exceed this limit. " classifier="MaxPacketLength" stringIdentifier="max-packet-length" version="1" creationDate="2014-06-16T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (6 .. 65537)" engineeringUnit="octets" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward Unified Space Data Link Protocol" oidOffset="11000">
    <functionalResource classifier="FwdUslpMcMux" version="1" oidBit="11000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>11000</oidBit>
      </oid>
    </functionalResource>
    <functionalResource classifier="FwdUslpVcMux" version="1" oidBit="11001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>11001</oidBit>
      </oid>
    </functionalResource>
    <functionalResource classifier="FwdUslpVcGen" version="1" oidBit="11002">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>11002</oidBit>
      </oid>
    </functionalResource>
    <functionalResource stringIdentifier="FwdUslpMapMux" version="1" oidBit="11003">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>11003</oidBit>
      </oid>
    </functionalResource>
    <functionalResource classifier="FwdUslpEncapAndMapPktProc" version="1" oidBit="11004">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>11004</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Return TM Space Link Protocol" oidOffset="12000">
    <functionalResource SemanticDefinition="The RtnTmMcDemux FR accepts suceessfully decoded frames and passes on all frames of the configured Master Channel. If so configured it also extracts and delivers the OCFs of that Master Channel. " classifier="RtnTmMcDemux" stringIdentifier="return-master-channel-demultiplexer" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>12000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the RtnTmMcDemux FR resource status and can take on four values:&#xD;&#xA;- 'configured'; &#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="rtnTmMcDemuxResourceStat" stringIdentifier="return-tm-master-channel-demultiplexer-resource-status" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmMcDemuxResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the rtnTmMcDemuxResourceStat to 'operational' or 'interrupted' by means of the directive rtnTmMcDemuxSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmMcDemuxResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the Master Channel ID the given FR instance is configured to process.  " classifier="rtnTmMcDemuxMcId" stringIdentifier="return-tm-master-channel-demultiplexer-master-channel-identifier" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RtnTmMcDemuxMcId    &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- For Tm frames the Transfer Frame Version Number is always 0.&#xA;&#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmMcDemuxMcId">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="For Tm frames the Transfer Frame Version Number is always 0.">
              <type xsi:type="frtypes:IntegerType">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the FR instance shall extract CLCWs from the MC frames and forward them to the FR types that consume them. It can take on two values:&#xD;&#xA;- clcw: the CLCW shall be extracted from frames of the configured Master Channel;&#xD;&#xA;- no clcw: no CLCW extraction shall take place, either because the configured MC does not carry CLCWs or no FR is set up to consume the MC CLCW stream. " classifier="rtnTmMcDemuxClcwExtraction" stringIdentifier="return-tm-master-channel-demultiplexer-clcw-extraction" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RtnTmMcDemuxClcwExtraction&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; clcw                &#x9; &#x9; (0)&#xA;,&#x9; noClcw              &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmMcDemuxClcwExtraction">
          <type xsi:type="frtypes:Enumerated">
            <values name="clcw"/>
            <values name="noClcw" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the rtnTmMcDemuxResourceStat parameter." classifier="rtnTmMcDemuxResourceStatChange" stringIdentifier="return-tm-master-channel-demultiplexer-resource-status-change" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the rtnMcDemuxResourceStat value that applies since the notified rtnMcDemuxResourceStatChange event occurred." classifier="rtnTmMcDemuxEventResourceStatValue" stringIdentifier="return-master-channel-demultiplexer-event-resource-status-value" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmMcDemuxEventResourceStatValue&#x9; ::= RtnTmMcDemuxResourceStat">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>12000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnTmMcDemuxEventResourceStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.11/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the RtnTmMcDemux FR type. " classifier="rtnTmMcDemuxSetContrParams" stringIdentifier="return-tm-master-channel-demultiplexxer-set-control-parameters" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the RtnMcDemux FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="rtnTmMcDemuxContrParamIdsAndValues" stringIdentifier="return-tm-master-channel-demultiplexer-conrol-parameter-identifiers-and-values" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnMcDemuxContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the parameters being set">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>12000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnMcDemuxContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="McFrames" minAccessor="1" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.11/@functionalResource.1"/>
      <providedAncillaryInterface name="Clcws" requiringFunctionalResource="//@functionalResourceSet.5/@functionalResource.0"/>
    </functionalResource>
    <functionalResource SemanticDefinition="The RtnTmVcDemux FR accepts frames from a given master channel and passes on all frames of the configured Virtual Channel. If so configured it also extracts and delivers the OCFs of that Virtual Channel. " classifier="RtnTmVcDemux" stringIdentifier="return-tm-virtual-channel-demultiplexer" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>12001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the RtnTmVcDemux FR resource status and can take on four values:&#xD;&#xA;- 'configured'; &#xD;&#xA;- 'operational';&#xD;&#xA;- 'interrupted';&#xD;&#xA;- 'halted'." classifier="rtnTmVcDemuxResourceStat" stringIdentifier="return-tm-virtual-channel-demultiplexer-resource-status" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmVcDemuxResourceStat&#x9; ::= ResourceStat" engineeringUnit="none" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by an x-support user. Setting of the rtnVcDemuxResourceStat to 'operational' or 'interrupted' by means of the directive rtnVcDemuxSetContrParams is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmVcDemuxResourceStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.0"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the Virtual Channel ID the given FR instance is configured to process. " classifier="rtnTmVcDemuxVcId" stringIdentifier="return-tm-master-channel-id" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RtnTmVcDemuxVcId    &#x9; ::= INTEGER  (0 .. 7)" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmVcDemuxVcId">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="7"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports if the FR instance shall extract CLCWs from the VC frames and forward them to the FR types that consume them. It can take on two values:&#xD;&#xA;- clcw: the CLCW shall be extracted from frame of the configured Master Channel;&#xD;&#xA;- no clcw: no CLCW extraction shall take place, either because the configured VC does not carry CLCWs or no FR is set up to consume the VC CLCW stream. " classifier="rtnTmVcDemuxClcwExtraction" stringIdentifier="return-tm-virtual-channel-demultiplexer-clcw-extraction" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RtnTmVcDemuxClcwExtraction&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; clcw                &#x9; &#x9; (0)&#xA;,&#x9; noClcw              &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmVcDemuxClcwExtraction">
          <type xsi:type="frtypes:Enumerated">
            <values name="clcw"/>
            <values name="noClcw" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the GVCID of the frames being delivered by this FR instance. The Master Channel Id being part of the GVCID is redundant in the sense that it is determined by the configuration of the RtnTmMcDemux FR delivering frames to the given RtmVcDemux FR instance. However, the GVCID is a frequently used parameter and is provided by the RtnTmVcDemus FR for convenience." classifier="rtnTmVcDemuxGvcid" stringIdentifier="return-tm-virtual-channel-demultiplexer-global-virtual-channel-identifier" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="RtnTmVcDemuxGvcid   &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The Transfer Frame Version Number of TM frames is always 0.&#xA;&#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; vcid                &#x9; INTEGER  (0 .. 7)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RtnTmVcDemuxGvcid">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The Transfer Frame Version Number of TM frames is always 0.">
              <type xsi:type="frtypes:IntegerType">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="vcid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="7"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the RtnTmVcDemux FR type. " classifier="rtnTmVcDemuxSetContrParams" stringIdentifier="return-tm-virtual-channel-demultiplexxer-set-control-parameters" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" guardCondition="none">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12001</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the RtnTmVcDemux FR and the parameter value must be of the same type as the parameter value that shall be set.&#xD;&#xA;" classifier="rtnTmVcDemuxContrParamIdsAndValues" stringIdentifier="return-telemetry-master-channel-demultiplexer-conrol-parameter-identifiers-and-values" version="1" creationDate="2019-09-11T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RtnTmVcDemuxContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the parameters being set">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>12001</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RtnTmVcDemuxContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
      <serviceAccesspoint name="VcFrames" minAccessor="1" maxAccessor="1" minAccessed="1" maxAccessed="1" accessingFunctionalResource="//@functionalResourceSet.11/@functionalResource.2"/>
      <providedAncillaryInterface name="Clcws" requiringFunctionalResource="//@functionalResourceSet.5/@functionalResource.0"/>
    </functionalResource>
    <functionalResource SemanticDefinition="none" classifier="RtnPktExtAndDeencap" stringIdentifier="return-packet-extraction-and-de-encapsulation" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="12002">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>12002</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the set of APIDs of the space packets that are to be forwarded by means of Packet.indication primitives to the Return File Service Production FR.  If the APID set contains an element set to the value -1, no space packets shall be forwarded to the Return File Service Production FR. " classifier="RfspApidSet" stringIdentifier="rfsp-apid-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="RfspApidSet         &#x9; ::= NULL" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RfspApidSet">
          <type xsi:type="frtypes:Null"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol IDs of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the Return File Service Production FR.  If the Protocol ID set contains an element set to the value -1, no encapsulation packets shall be forwarded to the  Return File Service Production FR.&#xD;&#xA;Note: If the Protocol ID is '6', the encapsulation packet is only forwarded if the Protocol ID Extension is an element of the parameter rfsp-protocol-id-extension-set. " classifier="RfspProtocolIdSet" stringIdentifier="rfsp-protocol-id-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1 .. 8) OF IntUnsigned (-1 .. 7)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol ID extensions of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the Return File Service Production FR.  If ((6 NOT element of rfsp-protocol-id-set) OR (-1 element of rfsp-protocol-id-set)), then this parameter shall be flagged as 'undefined'." classifier="RfspProtocolIdExtensionSet" stringIdentifier="rfsp-protocol-id-extension-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1 .. 16) OF IntUnsigned (-1 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol IDs of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the Rtn IP over CCSDS Router FR.  If the Protocol ID set contains an element set to the value -1, no encapsulation packets shall be forwarded to the  Rtn IP over CCSDS Router FR.&#xD;&#xA;Note: If the Protocol ID is '6', the encapsulation packet is only forwarded if the Protocol ID Extension is an element of the parameter riocr-protocol-id-extension-set. " classifier="RiocrProtocolIdSet" stringIdentifier="riocr-protocol-id-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1 .. 8) OF IntUnsigned (-1 .. 7)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol ID extensions of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the Rtn IP over CCSDS Router FR.  If ((6 NOT element of riocr-protocol-id-set) OR (-1 element of riocr-protocol-id-set)), then this parameter shall be flagged as 'undefined'." classifier="RiocrProtocolIdExtensionSet" stringIdentifier="riocr-protocol-id-extension-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1 .. 16) OF IntUnsigned (-1 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of APIDs of the space packets that are to be forwarded by means of Packet.indication primitives to the CFDP Receiving Entity FR.  If the APID set contains an element set to the value -1, no space packets shall be forwarded to the CFDP Receiving Entity FR. &#xD;&#xA;Note: If the statndard APID assignement is used for the transfer of CFDP PDUs over space packets, then the APID is 2045." classifier="CreApidSet" stringIdentifier="cre-apid-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1 .. 2048) OF IntUnsigned (-1 .. 2047)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol IDs of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the CFDP Receiving Entity FR.  If the Protocol ID set contains an element set to the value -1, no encapsulation packets shall be forwarded to the CFDP Receiving Entity FR.&#xD;&#xA;Note: If the standard protocol id assignment is used, then the protocol id '3' is used for the transfer of CFDP PDUs.&#xD;&#xA;Note: If the Protocol ID is '6', the encapsulation packet is only forwarded if the Protocol ID Extension is an element of the parameter cre-protocol-id-extension-set. " classifier="CreProtocolIdSet" stringIdentifier="cre-protocol-id-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1 .. 8) OF IntUnsigned (-1 .. 7)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Protocol ID extensions of the encapsulation packets that are to be forwarded by means of ENCAP.indication primitives to the CFDP Receiving Entity FR.  If ((6 NOT element of cre-protocol-id-set) OR (-1 element of cre-protocol-id-set)), then this parameter shall be flagged as 'undefined'." classifier="CreProtocolIdExtensionSet" stringIdentifier="cre-protocol-id-extension-set" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1 .. 16) OF IntUnsigned (-1 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter parameter reports if CFDP PDUs shall be extracted from space packets or encpsulation packets and forwarded to the CFDP sending entity. It can take on the following values:&#xD;&#xA;- space packet: the CFDP PDUs will be extrracted from spacepackets with the APID = 2045;&#xD;&#xA;- encapsulation packet: the CFDP PDUs are extracted from encapsulation packets with the protocol-id = 3;&#xD;&#xA;- no cfdp support: no extraction and forwarding of CFDP PDUs takes place. " classifier="CfdpPduExtraction" stringIdentifier="cfdp-pdu-extraction" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   spacePacket   (0)&#xD;&#xA;,   encapsulationPacket   (1)&#xD;&#xA;,   noCfdpSupport   (2)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12002</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Return Unified Space Data Link Protocol" oidOffset="13000"/>
  <functionalResourceSet name="Frame Data Sink" oidOffset="14000">
    <functionalResource SemanticDefinition="None" classifier="FrameDataSink" stringIdentifier="frame-data-sink" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="14000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>14000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the service package on behalf of which telemetry frames are passed to one or more offline frame buffers." classifier="ServicePackageId" stringIdentifier="service-package-id" version="1" creationDate="2014-06-27T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="ServicePackageId    &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; element1            &#x9; INTEGER  (0 .. 1000)&#xA;,&#x9; element2            &#x9; DirectiveQualifier&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="ServicePackageId">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="element1" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1000"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="element2" optional="false">
              <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the start and stop time of the period during which this FR instance forwards frames to the Offline Frame Buffer FR instances consuming the frame stream generated by this FR instance." classifier="ProductionPeriod" stringIdentifier="production-period" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (2) OF Time" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward File Data Delivery Production" oidOffset="15000">
    <functionalResource SemanticDefinition="none" classifier="CfdpSendEntity" stringIdentifier="CFDP Sending Entity" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="15000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>15000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the entity id length and the entity IDs required for the CFDP core procedure where the first element of the parameter specifies the length of the entity IDs in octets, the second element reports the ID of the local, i.e. the sending entity and the third element reports the ID of the receiving entity. " classifier="EntityIds" stringIdentifier="entity-ids" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (3)) OF IntUnsigned" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide an EOF-sent.indication to the Forward File Service Production FR when the initial transmission of the transaction's EOF PDU has been performed. It can take on two values:&#xD;&#xA;- EOF-sent indication enabled;&#xD;&#xA;- EOF-sent indication disabled." classifier="EofSentIndication" stringIdentifier="eof-sent-indication" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   eofSentIndicationEnabled   (0)&#xD;&#xA;,   eofSentIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the default fault handler override, if any. If the parameter has the value '-1', the default fault handlers as specified in CCSDS 727.0.B-4 apply. Other values specify the overrides as per CCSDS 727.0.B-4." classifier="FaultHandlerOverride" stringIdentifier="fault-handler-override" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF INTEGER (-1 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports on the output format that the CFDP Sending Entity is configured to generate. It may take on four values:&#xD;&#xA;- ENCAP.request;&#xD;&#xA;- VCP.request;&#xD;&#xA;- PACKET.request;&#xD;&#xA;- MAPP.request.&#xD;&#xA;If request-type = encapRequest, then the trailing end of the UT address shall be the concatenation of the Packet Version Number set to 7 and the Embedded Protocol Id set to 3. &#xD;&#xA;If request-type ≠ encapRequest, then the trailing end of the UT address shall be the concatenation of the Packet Version Number set to 0 and the APID set to 2045. " classifier="RequestType" stringIdentifier="request-type" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xD;&#xA;{   encapRequest   (0)&#xD;&#xA;,   vcpRequest   (1)&#xD;&#xA;,   packetRequest   (2)&#xD;&#xA;,   mappRequest   (3)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the Transfer Frame Version Number (TVN) applicable to the space link protocol used to transfer the CFDP PDUs. The frames may be either TC frames or AOS frames" classifier="TransferFrameVersionNumber" stringIdentifier="transfer-frame-version-number" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   tcFrame   (0)&#xD;&#xA;,   aosFrame   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the spacecraft ID in the transfer frame headers used to transfer the CFDP PDUs.&#xD;&#xA;If (transfer-frame-version-number = 0) then the range of spacecraft-id is 0 .. 1023.&#xD;&#xA;If (transfer-frame-version-number = 1) then the range of spacecraft-id is 0 .. 255.  &#xD;&#xA;" classifier="SpacecraftId" stringIdentifier="spacecraft-id" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned (0 .. 1023)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the virtual channel ID in the transfer frame headers used to transfer the CFDP PDUs.&#xD;&#xA;If (transfer-frame-version-number = 0) then the range of spacecraft-id is 0 .. 7.&#xD;&#xA;If (transfer-frame-version-number = 1) then the range of spacecraft-id is 0 .. 63.  " classifier="VirtualChannelId" stringIdentifier="virtual-channel-id" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the MAP ID in the segment header of the TC frames used to transfer the CFDP PDUs.&#xD;&#xA;If (request-type ≠ mappRequest) then this parameter shall be flagged as undefined." classifier="MapId" stringIdentifier="map-id" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned (0 .. 63)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports  the one-way light time in milliseconds to the remote CFDP entity. " classifier="OneWayLightTime" stringIdentifier="one-way-light-time" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the NAK timer period in milliseconds the remote CFDP entity will apply. " classifier="NakInterval" stringIdentifier="nak-interval" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="10" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the keep alive timer period in milliseconds the remote CFDP entity will apply. If the value of this parameter is zero, the remote entity does not to send keep alive PDUs periodically." classifier="KeepAliveInterval" stringIdentifier="keep-alive-interval" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="11" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the report timer period in milliseconds at which the remote CFDP entity will issue report.indication primitives. If the value of the parameter is zero, the remote entity does not to issue report.indication primitives periodically." classifier="ReportInterval" stringIdentifier="report-interval" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="12" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the remote CFDP entity will provide immediate or deferred NAKs. It can have the vales:&#xD;&#xA;- immediate;&#xD;&#xA;- deferred." classifier="NakMode" stringIdentifier="nak-mode" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="13" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   immediate   (0)&#xD;&#xA;,   deferred   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the default transmission mode of the remote CFDP entity. The parameter can take on two values:&#xD;&#xA;- acknowledged;&#xD;&#xA;- unacknowledged.&#xD;&#xA;" classifier="DefaultTransmissionMode" stringIdentifier="default-transmission-mode" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="14" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   acknowledged   (0)&#xD;&#xA;,   unacknowledged   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports how the remote CFDP entity will dispose of an incomplete received file upon transaction cancellation. The parameter can take on two values:&#xD;&#xA;- discard;&#xD;&#xA;- retain." classifier="IncompleteFileHandling" stringIdentifier="incomplete-file-handling" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="15" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{  discard   (0)&#xD;&#xA;,   retain   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if at the remote CFDP entity the CRC option is active. It can have two values:&#xD;&#xA;- CRC present in PDUs;&#xD;&#xA;- CRC absent in PDUs." classifier="Crc" stringIdentifier="crc" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="16" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{  crcPresent   (0)&#xD;&#xA;,   crcAbsent   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the maximum file segment length (in octets) the remote CFDP entity is able to handle." classifier="MaxFileSegmentLength" stringIdentifier="max-file-segment-length" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="17" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned" engineeringUnit="octets" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports how many times the NAK timer may expire at the remote CFDP entity without intervening reception of file data or metadata not previously received until that entity will declare a NAK Limit Reached fault." classifier="NakTimerExpirationLimit" stringIdentifier="nak-timer expiration-limit" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="18" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum period in milliseconds that may pass without reception of a PDU until the remote CFDP entity will declare an Inactivity fault condition. " classifier="TransactionInactivityLimit" stringIdentifier="transaction-inactivity-limit" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="19" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the start and end time of the reception opportunity at the remote CFDP entity." classifier="ReceptionOpportunity" stringIdentifier="reception-opportunity" version="1" creationDate="2014-06-29T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="20" typeDefinition="SEQUENCE (SIZE (2) OF Time" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
    <functionalResource SemanticDefinition="none" classifier="FwdFileSvcProd" stringIdentifier="Forward File Server" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="15001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>15001</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Return File Data Delivery Production" oidOffset="16000">
    <functionalResource SemanticDefinition="none" classifier="CfdpRcvEntity" stringIdentifier="ccsds-file-delivery-protocol-receiving-entity" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="16000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>16000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the entity id length and the entity IDs required for the CFDP core procedure where the first element of the parameter specifies the length of the entity IDs in octets, the second element reports the ID of the local, i.e. the receiving entity and the third element reports the ID of the sending entity. " classifier="EntityIds" stringIdentifier="entity-ids" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (3)) OF IntUnsigned" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide an EOF-Recv.indication to the Return File Service Production FR when the EOF PDU associated with a transaction has been received from the sending end.  It can take on two values:&#xD;&#xA;- EOF-recv indication enabled;&#xD;&#xA;- EOF-recv indication disabled." classifier="EofRecvIndication" stringIdentifier="eof-recv-indication" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   eofRecvIndicationEnabled   (0)&#xD;&#xA;,   eofRecvIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide a File-Segment-Recv.indication to the Return File Service Production FR when individual file data segments have been received from the sending end. It can take on two values:&#xD;&#xA;- File-Segment-Recv indication enabled;&#xD;&#xA;- File-Segment-Recv indication disabled." classifier="FileSegmentRecvIndication" stringIdentifier="file-segment-recv-indication" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   fileSegmentRecvIndicationEnabled   (0)&#xD;&#xA;,   fileSegmentRecvIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide a Transaction-Finished.indication to the Return File Service Production FR when the transaction is complete. It can take on two values:&#xD;&#xA;- Transaction-Finished indication enabled;&#xD;&#xA;- Transaction-Finished indication disabled." classifier="TransactionFinishedIndication" stringIdentifier="transaction-finished-indication" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   transactionFinishedIndicationEnabled   (0)&#xD;&#xA;,   transactionFinishedIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide a Suspended.indication to the Return File Service Production FR when the transaction has been suspended. It can take on two values:&#xD;&#xA;- Suspended indication enabled;&#xD;&#xA;- Suspended indication disabled." classifier="SuspendedIndication" stringIdentifier="suspended-indication" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   suspendedIndicationEnabled   (0)&#xD;&#xA;,   suspendedIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the local CFDP entity is configured to provide a Resumed.indication to the Return File Service Production FR when the transaction has been resumed. It can take on two values:&#xD;&#xA;- Resumed indication enabled;&#xD;&#xA;- Resumed indication disabled." classifier="ResumedIndication" stringIdentifier="resumed-indication" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   resumedIndicationEnabled   (0)&#xD;&#xA;,   resumedIndicationDisabled   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the default fault handler override, if any. If the parameter has the value '-1', the default fault handlers as specified in CCSDS 727.0.B-4 apply. Other values specify the overrides as per CCSDS 727.0.B-4." classifier="FaultHandlerOverride" stringIdentifier="fault-handler-override" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1)) OF INTEGER (-1 .. 15)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports  the one-way light time in milliseconds to the remote CFDP entity. " classifier="OneWayLightTime" stringIdentifier="one-way-light-time" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports on the prompt timer period in milliseconds the remote CFDP entity applies. If the value of the parameter is zero, the remote entity will not send periodic prompt PDUs." classifier="PromptInterval" stringIdentifier="prompt-interval" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the discrepancy limit (in octets) between the progress monitored by observing the actual transmission and the progress monitored by means of the Keep Alive PDUs at the remote CFDP entity. When the limit is reached, the remote entity will declare a Keep Alive Limit Reached fault. " classifier="KeepAliveDiscrepancyLimit" stringIdentifier="keep-alive-discrepancy-limit" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="10" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned" engineeringUnit="octet" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum number of retransmissions of a given PDU the remote CFDP entity will perform until it declares a Positive ACK Limit Reached fault. A retransmission is performed whenever the ACK timer expires." classifier="PositiveAckTimerExpirationLimit" stringIdentifier="positive-ack-timer-expiration-limit" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="11" typeDefinition="SEQUENCE (SIZE (1) OF IntUnsigned" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum period in milliseconds that may pass without reception of a PDU until the remote CFDP entity will declare an Inactivity fault condition. " classifier="TransactionInactivityLimit" stringIdentifier="transaction-inactivity-limit" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="12" typeDefinition="SEQUENCE (SIZE (1) OF Duration [1]" engineeringUnit="1/1000 s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the start and end time of the tranmission opportunity at the remote CFDP entity." classifier="TransmissionOpportunity" stringIdentifier="transmission-opportunity" version="1" creationDate="2014-06-30T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="13" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
    <functionalResource SemanticDefinition="none" classifier="RtnFileSvcProd" stringIdentifier="return-file-service-production" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="16001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>16001</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Real-Time Radiometric Data Collection" oidOffset="17000">
    <functionalResource SemanticDefinition="none" classifier="TdmSegmentGen" stringIdentifier="trajectory-data-message-segment-generation" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="17000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>17000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Non-Validated Radiometric Data Collection" oidOffset="18000">
    <functionalResource SemanticDefinition="none" classifier="NonValRmDataCollection" stringIdentifier="non-validated-radiometric-data-collection" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="18000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>18000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Delta-DOR Raw Data Collection" oidOffset="19000">
    <functionalResource SemanticDefinition="none" classifier="DdorRawDataCollection" stringIdentifier="delta-differential-one-way-ranging-raw-data-collection" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="19000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>19000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Open-Loop Receiver/Formatter" oidOffset="20000">
    <functionalResource SemanticDefinition="none" classifier="OpenLoopRxFormatter" stringIdentifier="open-loop-receiver-formatter" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="20000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>20000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter specifies in Hz the sampling rate used for the open-loop recording." classifier="OpenLoopSamplingRate" stringIdentifier="open-loop-sampling-rate" version="1" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (1000 .. 2000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter specifies the quantization of the samples, i.e. the number of bits used to represent one sample. The following values are supported:&#xD;&#xA;- '1 bit';&#xD;&#xA;- '2 bit';&#xD;&#xA;- '4 bit';&#xD;&#xA;- '8 bit';&#xD;&#xA;- '16 bit'.&#xD;&#xA;The samples are complex and each component (real and imaginary) will have this quantization." classifier="OpenLoopQuantization" stringIdentifier="open-loop-quantization" version="1" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 4)&#xA;{   1-bit   (0)&#xA;,   2-bit   (1)&#xA;,   4-bit   (2)&#xA;,   8-bit   (3)&#xA;,   16-bit   (4)&#xA;}" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter classifier="OpenLoopDopplerCompensationMode" version="1" oidBit="3" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording center frequency with respect to the nominal carrier return link conversion for right hand circular polarization." classifier="OpenLoopCenterFrequencyOffsetRhc" stringIdentifier="open-loop-center-frequency-offset-rhc" version="1" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-100000000 .. 100000000)" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording center frequency with respect to the nominal carrier return link conversion for left hand circular polarization." classifier="OpenLoopCenterFrequencyOffsetLhc" stringIdentifier="open-loop-center-frequency-offset-lhc" version="1" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-100000000 .. 100000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording subchannel 1 center frequency with respect to the open-loop recording center frequency specified for the polarization selected for this subchannel. If this subchannel is 'off', this parameter shall be flagged as undefined." classifier="Subchannel1Offset" stringIdentifier="subchannel-1-offset" version="1" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-80000000 .. 80000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if this subchannel is active and if so which polarization the input signal has. It can take on three values:&#xA;- 'off'  - the subchannel is not used for the current open-loop recording;&#xA;- 'rhc' - the right hand circular polarized signal is used as input to this sub-channel;&#xA;- 'lhc' - the left hand circular polarized signal is used as input to this sub-channel." classifier="Subchannel1Polarization" stringIdentifier="subchannel-1-polarization" version="1" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   off   (0)&#xA;,   rhc   (1)&#xA;,   lhc   (2)&#xA;}" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording subchannel 2 center frequency with respect to the open-loop recording center frequency specified for the polarization selected for this subchannel. If this subchannel is 'off', this parameter shall be flagged as undefined." classifier="Subchannel2Offset" stringIdentifier="subchannel-2-offset" version="1" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-80000000 .. 80000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if this subchannel is active and if so which polarization the input signal has. It can take on three values:&#xA;- 'off'  - the subchannel is not used for the current open-loop recording;&#xA;- 'rhc' - the right hand circular polarized signal is used as input to this sub-channel;&#xA;- 'lhc' - the left hand circular polarized signal is used as input to this sub-channel." classifier="Subchannel2Polarization" stringIdentifier="subchannel-2-polarization" version="1" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   off   (0)&#xA;,   rhc   (1)&#xA;,   lhc   (2)&#xA;}" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording subchannel 3 center frequency with respect to the open-loop recording center frequency specified for the polarization selected for this subchannel. If this subchannel is 'off', this parameter shall be flagged as undefined." classifier="Subchannel3Offset" stringIdentifier="subchannel-3-offset" version="1" authorizingEntity="CSTS WG" oidBit="10" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-80000000 .. 80000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if this subchannel is active and if so which polarization the input signal has. It can take on three values:&#xA;- 'off'  - the subchannel is not used for the current open-loop recording;&#xA;- 'rhc' - the right hand circular polarized signal is used as input to this sub-channel;&#xA;- 'lhc' - the left hand circular polarized signal is used as input to this sub-channel." classifier="Subchannel3Polarization" stringIdentifier="subchannel-3-polarization" version="1" authorizingEntity="CSTS WG" oidBit="11" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   off   (0)&#xA;,   rhc   (1)&#xA;,   lhc   (2)&#xA;}" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter specifies the offset in Hz of the open-loop recording subchannel 4 center frequency with respect to the open-loop recording center frequency specified for the polarization selected for this subchannel. If this subchannel is 'off', this parameter shall be flagged as undefined." classifier="Subchannel4Offset" stringIdentifier="subchannel-4-offset" version="1" authorizingEntity="CSTS WG" oidBit="12" typeDefinition="SEQUENCE (SIZE (1)) OF Integer (-80000000 .. 80000000)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if this subchannel is active and if so which polarization the input signal has. It can take on three values:&#xA;- 'off'  - the subchannel is not used for the current open-loop recording;&#xA;- 'rhc' - the right hand circular polarized signal is used as input to this sub-channel;&#xA;- 'lhc' - the left hand circular polarized signal is used as input to this sub-channel." classifier="Subchannel4Polarization" stringIdentifier="subchannel-4-polarization" version="1" authorizingEntity="CSTS WG" oidBit="13" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   off   (0)&#xA;,   rhc   (1)&#xA;,   lhc   (2)&#xA;}" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>20000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Offline Frame Buffer" oidOffset="21000">
    <functionalResource SemanticDefinition="none" classifier="OfflineFrameBuffer" stringIdentifier="offline-frame-buffer" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="21000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>21000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Tracking Data Message Recording Buffer" oidOffset="22000">
    <functionalResource SemanticDefinition="None" classifier="TdmRcrdBuffer" stringIdentifier="trajectory-data-message-recording-buffer" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="22000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>22000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Non-Validated Radiometric Data Store" oidOffset="23000">
    <functionalResource SemanticDefinition="none" classifier="NonValRmDataStore" stringIdentifier="non-validated-radiometric-data-store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="23000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>23000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Validated Radiometric Data Store" oidOffset="24000">
    <functionalResource SemanticDefinition="None" classifier="ValRmDataStore" stringIdentifier="validated-radiometric-data-store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="24000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>24000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Delta-DOR Raw Data Store" oidOffset="25000">
    <functionalResource SemanticDefinition="none" classifier="DdorRawDataStore" stringIdentifier="delta-differential-one-way-ranging-raw-data-store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="25000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>25000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Open-Loop Data Store" oidOffset="26000">
    <functionalResource SemanticDefinition="none" classifier="OpenLoopDataStore" stringIdentifier="open-loop-data-store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="26000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>26000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Return File Data Store" oidOffset="27000">
    <functionalResource SemanticDefinition="None" classifier="RtnFileDataStore" stringIdentifier="return-file-data-store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="27000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>27000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward File Data Store" oidOffset="28000">
    <functionalResource SemanticDefinition="None" classifier="FwdFileDataStore" stringIdentifier="Forward File Data Store" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="28000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>28000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="SLE Forward Space Packet" oidOffset="29000">
    <functionalResource SemanticDefinition="none" classifier="FspTsProvider" stringIdentifier="FSP TS Provider" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="29000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>29000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the identifier of the given service instance. " classifier="ServiceInstanceIdentifier" stringIdentifier="service-instance-identifier" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF VisibleString (SIZE (30 .. 256))" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the F-CLTU service. It can take on the following values:&#xD;&#xA;- 'unbound': all resources required to enable the provision of the Forward CLTU service have been allocated, and all objects required to provide the service have been instantiated; However, no association yet exists between the user and the provider, i.e., the F-CLTU transfer service provider port is not bound;&#xD;&#xA;- 'ready': an association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of CLTUs from the user to the provider (by means of the CLTU-TRANSFER-DATA operation) is not permitted; the user may enable the delivery of CLTUs by means of the appropriate service operation (CLTU-START), which, in turn, will cause the provider to transition to the state 'active';&#xD;&#xA;- 'active':  this state resembles state ‘ready’, except that now the user can send CLTUs and the provider is enabled to radiate CLTUs to the spacecraft; the service continues in this state until the user invokes the CLTU-STOP operation to cause the provider to suspend transmission of CLTUs and transition back to state 'ready' or the PEER-ABORT invocation to cause the service to transition back to the 'unbound' state." classifier="SiState" stringIdentifier="si-state" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="2" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   unbound   (0)&#xA;,   ready   (1)&#xA;,   active   (2)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the delivery mode of the given instance of the FSP service. For the present version of this service only the ‘forward online’ delivery mode is defined." classifier="DeliveryMode" stringIdentifier="delivery-mode" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="3" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0)&#xA;{   fwd-online   (0)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the setting of the maximum time period in seconds permitted from when a confirmed FSP operation is invoked until the return is received by the invoker." classifier="ReturnTimeoutPeriod" stringIdentifier="return-timeout-period" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="4" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (1 .. 600)" engineeringUnit="s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports on the status of the service production process used by the given instance of the FSP service. It can take on the following values:&#xD;&#xA;- 'configured': equipment has been assigned to support the service instance, but the production process is not yet capable of processing Space Packets;&#xD;&#xA;- 'operational BD': the production process has been configured for support, has completed the acquisition sequence, and is capable of processing Space Packets and radiating them in BD transmission mode; the AD transmission mode is currently not supported;&#xD;&#xA;- 'operational AD and BD': the production process has been configured for support, has completed the acquisition sequence, and is capable of processing Space Packets and radiating them in any transmission mode;&#xD;&#xA;- 'operational AD suspended': a TC frame has been transmitted the transmission-limit number of times but no acknowledgement by CLCW has been received; the AD mode has therefore been suspended;&#xD;&#xA;- 'interrupted': the production process is stopped due to a fault;&#xD;&#xA;- 'halted': the production process is stopped and production equipment is out of service due to management action." classifier="ProductionStatus" stringIdentifier="production-status" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="5" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 3)&#xA;{   configured   (0)&#xA;,   operational-bd   (1)&#xA;,   operational-ad-bd   (2)&#xA;,   operational-ad-suspended   (3)&#xA;,   interrupted   (4)&#xA;,   halted   (5)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the RF lock flag in the CLCW shall be reflected in the service provider’s production-status parameter. It can take on the following values:&#xD;&#xA;- 'yes': the ‘No RF available’ flag in the CLCW must be false in order for the provider to set production-status to ‘operational’;&#xD;&#xA;- 'no': the CLCW ‘No RF available’ flag shall be ignored." classifier="RfAvailableRequired" stringIdentifier="rf-available-required" version="1" creationDate="2014-06-17T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="6" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   'yes'   (0)&#xD;&#xA;,   'no'   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the bit lock flag in the CLCW shall be reflected in the service provider’s production-status parameter. It can take on the following values:&#xD;&#xA;- 'yes': the ‘No bit lock’ flag in the CLCW must be false in order for the provider to set production-status to ‘operational’;&#xD;&#xA;- 'no': the CLCW ‘No bit lock’ flag shall be ignored." classifier="BitLockRequired" stringIdentifier="bit-lock-required" version="1" creationDate="2014-06-17T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="7" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xD;&#xA;{   yes   (0)&#xD;&#xA;,   no   (1)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the given service instance is permitted to invoke directives. This parameter can take on two values:&#xD;&#xA;- 'enabled': this service instance is permitted to invoke directives for the given Virtual Channel;&#xD;&#xA;- 'disabled': directives cannot be invoked using this service instance." classifier="DirectiveInvocationEnabled" stringIdentifier="directive-invocation-enabled" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="8" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xA;{   enabled   (0)&#xA;,  disabled   (1)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports if the service instance that is permitted to invoke directives for the given VC, if any, is in the state 'active' (cf. si-state). At most one service instance for a given VC will be configured to have the capability to invoke directives. This parameter can take on two values:&#xD;&#xA;- 'yes': the service instance permitted to invoke directives for the given Virtual Channel is 'active';&#xD;&#xA;- 'no': it is presently not possible to invoke directives for the given VC, i.e., the service instance permitted to invoke directives for the given Virtual Channel is 'unbound' or 'ready'." classifier="DirectiveInvocationOnline" stringIdentifier="directive-invocation-online" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="9" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 1)&#xA;{   'yes'   (0)&#xA;,   no   (1)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the transmission mode that the given service instance may use for the Space Packets that it processes. This parameter can take on two values: &#xD;&#xA;- 'expedited': the Space Packets will be embedded in TC frames that will be radiated in BD mode;&#xD;&#xA;- 'sequence controlled': the Space Packets will be embedded in TC frames that will be radiated in AD mode; &#xD;&#xA;- 'any': the service supports either transmission mode and will therefore transmit a Space Packet in the mode requested in the FSP-DATA-TRANSFER invocation." classifier="PermittedTransmissionMode" stringIdentifier="permitted-transmission-mode" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="10" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 2)&#xA;{   expedited   (0)&#xA;,   sequence-controlled   (1)&#xA;,   any   (2)&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports all APIDs that the Space Packets processed by the given service instance may contain. If all APIDs are permitted, this parameter shall take the value -1." classifier="ApidList" stringIdentifier="apid-list" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="11" typeDefinition="SEQUENCE (SIZE (1 .. 2048)) OF Integer (0 .. 2047, -1)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current setting of the time in seconds between successive FSP-STATUS-REPORT invocations sent by the FSP service provider. The permissible values are in the range (2 .. 600).  If cyclic reporting is off, the value reported is undefined." classifier="ReportingCycle" stringIdentifier="reporting-cycle" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="12" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (2 .. 600)" engineeringUnit="s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the packet-identification the FSP service provider expects to receive in the next FSP-TRANSFER-DATA invocation for this invocation to be valid. As long as si-state ≠ 'active', the value reported will be zero." classifier="ExpectedPacketIdentification" stringIdentifier="expected-packet-identification" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="13" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 ..  4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the directive-invocation-identification the FSP service provider expects to receive in the next FSP-INVOKE-DIRECTIVE invocation for this invocation to be valid. The initial value of this parameter is zero." classifier="ExpectedDirectiveIdentification" stringIdentifier="expected-directive-identification" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="14" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 ..  4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the event-invocation-identification the FSP service provider expects to receive in the next FSP-THROW-EVENT invocation for this invocation to be valid. The initial value of this parameter is zero." classifier="ExpectedEventInvocationIdentification" stringIdentifier="expected-event-invocation-identification" version="1" creationDate="2014-06-18T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="15" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 ..  4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
      <parameter SemanticDefinition="This parameter configers and reports the Spacecraft IDs and consequently the Master Channels that are permitted on the given forward link.  " classifier="fwdTcMcMuxValidScids" stringIdentifier="forward-telecommand-master-channel-multiplexer-valid-spacecraft-ids" version="1" creationDate="2019-03-11T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="16" typeDefinition="FwdTcMcMuxValidScids&#x9; ::= SEQUENCE  (SIZE( 1 .. 1024))  OF&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcMcMuxValidScids">
          <type xsi:type="frtypes:SequenceOf">
            <sizeConstraint min="1" max="1024"/>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the VC-IDs of the TC VC frames the FR instance accepts as input." classifier="fwdTcVcMuxValidTcVcIds" stringIdentifier="forward-telecommand-virtual-channel-multiplexer-valid-tc-vcids" version="1" creationDate="2019-03-22T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="17" typeDefinition="FwdTcVcMuxValidTcVcIds&#x9; ::= SEQUENCE  (SIZE( 1 .. 64))  OF&#x9; &#x9; vcId                &#x9; INTEGER  (0 .. null)" engineeringUnit="none" configured="true" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>29000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdTcVcMuxValidTcVcIds">
          <type xsi:type="frtypes:SequenceOf">
            <sizeConstraint min="1" max="64"/>
            <elements xsi:type="frtypes:Element" name="vcId" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="8"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="SLE Forward CLTU" oidOffset="30000">
    <functionalResource SemanticDefinition="The FCltuTsProvider FR provides the to be radiated CLTUs to the FwdTcPlopSyncAndChnlEncode FR." classifier="FwdCltuTsProvider" stringIdentifier="Forward-CLTU-Transfer-Service-Provider" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="30000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>30000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the service production process used by the given instance of a F-CLTU service. It can take on the following values:&#xD;&#xA;- 'configured': equipment has been assigned to support the service instance, but the production process is not yet capable of radiating CLTUs;&#xD;&#xA;- 'operational': the production process has been configured for support, has completed the acquisition sequence, and is capable of radiating CLTUs;&#xD;&#xA;- 'interrupted': the production process is stopped due to a fault;&#xD;&#xA;- 'halted': the production process is stopped and production equipment is out of service due to management action." classifier="fwdCltuProductionStat" stringIdentifier="forward-cltu-production-status" version="1" creationDate="2019-09-09T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdCltuProductionStat&#x9; ::= ProductionStat" engineeringUnit="none" configured="false" guardCondition="This parameter can only partially be set by local EM and not at all by a cross support user. Setting of the fwdCltuProductionStatus to 'operational' or 'interrupted' by means of the directive fwdCltuSetContrParams is not permissible. ">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuProductionStat">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.7"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the identifier of the given service instance. " classifier="fwdCltuServiceInstanceIdentifier" stringIdentifier="forward-cltu-service-instance-identifier" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="FwdCltuServiceInstanceIdentifier&#x9; ::= SleServiceInstanceId" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuServiceInstanceIdentifier">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.2"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the delivery mode of the given instance of the F-CLTU service. For the present version of this service only the ‘forward online’ delivery mode is defined." classifier="fwdCltuDeliveryMode" stringIdentifier="forward-cltu-delivery-mode" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="FwdCltuDeliveryMode &#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; fwdOnline           &#x9; &#x9; (0)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuDeliveryMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="fwdOnline"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the setting of the maximum time period in seconds permitted from when a confirmed F-CLTU operation is invoked until the return has to be received by the invoker." classifier="fwdCltuReturnTimeoutPeriod" stringIdentifier="forward-cltu-return-timeout-period" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is second.&#xA;FwdCltuReturnTimeoutPeriod&#x9; ::= SleReturnTimeout" engineeringUnit="s" configured="true" guardCondition="Setting of this parameter by means of the fwdCltuSetContrParams directive is only permissible while fwdCltuSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuReturnTimeoutPeriod" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.6"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports how the F-CLTU provider notifies certain changes of fwdCltuProductionStat to the service user. It can take on two values:&#xD;&#xA;- 'immediate': the user is notified of a fwdCltuProductionStat change to 'interrupted' as soon as this transition is detected;&#xD;&#xA;- 'deferred': the user is notified about the fwdCltuProductionStat change only if and when the radiation of a CLTU is affected." classifier="fwdCltuNotificationMode" stringIdentifier="forward-cltu-notification-mode" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="FwdCltuNotificationMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; immediate           &#x9; &#x9; (0)&#xA;,&#x9; deferred            &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the fwdCltuSetContrParams directive is only permissible while fwdCltuSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuNotificationMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="immediate"/>
            <values name="deferred" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter configures and reports the way the F-CLTU service provider will act in the event of a protocol abort. This parameter can take on two values:&#xD;&#xA;- 'abort': service production will cease in the event of a protocol abort;&#xD;&#xA;- 'continue': service production will disregard the protocol abort event and continue radiating the CLTUs already buffered at the time of the event." classifier="fwdCltuProtocolAbortMode" stringIdentifier="forward-cltu-protocol-abort-mode" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="FwdCltuProtocolAbortMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; abort               &#x9; &#x9; (0)&#xA;,&#x9; continue            &#x9; &#x9; (1)&#xA;}&#xA;" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the fwdCltuSetContrParams directive is only permissible while fwdCltuSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuProtocolAbortMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="abort"/>
            <values name="continue" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the F-CLTU service. It can take on the following values:&#xD;&#xA;- 'unbound': all resources required to enable the provision of the Forward CLTU service have been allocated, and all objects required to provide the service have been instantiated; however, no association yet exists between the user and the provider, i.e., the F-CLTU transfer service provider port is not bound;&#xD;&#xA;- 'ready': an association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of CLTUs from the user to the provider (by means of the CLTU-TRANSFER-DATA operation) is not permitted; the user may enable the delivery of CLTUs by means of the appropriate service operation (CLTU-START), which, in turn, will cause the provider to transition to the state 'active';&#xD;&#xA;- 'active':  this state resembles state ‘ready’, except that now the user can send CLTUs and the provider is enabled to radiate CLTUs to the spacecraft; the service continues in this state until the user invokes either the CLTU-STOP operation to cause the provider to suspend transmission of CLTUs and transition back to state 'ready' or the user invokes the PEER-ABORT operation to cause the service to transition back to the 'unbound' state." classifier="fwdCltuSiState" stringIdentifier="forward-cltu-si-state" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="FwdCltuSiState      &#x9; ::= SleSiState" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuSiState">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.5"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current setting of the time in seconds between successive CLTU-STATUS-REPORT invocations sent by the F-CLTU service provider. The permissible values are in the range (2 .. 600). If cyclic reporting is off, this parameter shall be flagged as 'undefined'." classifier="fwdCltuReportingCycle" stringIdentifier="forward-cltu-reporting-cycle" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="-- The engineering unit of this parameter is second.&#xA;FwdCltuReportingCycle&#x9; ::= SleReportingCycle" engineeringUnit="s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuReportingCycle" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.13"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the cltu-identification the F-CLTU service provider expects to receive in the next CLTU-TRANSFER-DATA invocation for this invocation to be valid. As long as fwdCltuSiState ≠ 'active', the value reported will be zero." classifier="fwdCltuExpectedCltuIdentification" stringIdentifier="forward-cltu-expected-cltu-identification" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="FwdCltuExpectedCltuIdentification&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuExpectedCltuIdentification">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the value of the event-invocation-identification the F-CLTU service provider expects to receive in the next CLTU-THROW-EVENT invocation for this invocation to be valid. The initial value of this parameter is zero." classifier="fwdCltuExpectedEventInvocationId" stringIdentifier="forward-cltu-expected-event-invocation-identification" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="FwdCltuExpectedEventInvocationId&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuExpectedEventInvocationId">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the total number of CLTUs received while the given service instance has been accessible to the user. Only CLTUs that the service provider accepted and consequently buffered are counted in the total." classifier="fwdCltuNumberOfCltusReceived" stringIdentifier="forward-cltu-number-of-cltus-received" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="FwdCltuNumberOfCltusReceived&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuNumberOfCltusReceived">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of CLTUs that the provider attempted to radiate while the given service instance has been active including&#xA;- CLTUs that radiated successfully;&#xA;- CLTUs that expired;&#xA;- CLTUs that aborted;&#xA;- a CLTU in the process of being radiated." classifier="fwdCltuNumberOfCltusProcessed" stringIdentifier="forward-cltu-number-of-cltus-processed" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="FwdCltuNumberOfCltusProcessed&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuNumberOfCltusProcessed">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of CLTUs that the provider successfully radiated completely during the service provision period. A CLTU in the process of being radiated is not included in this count." classifier="fwdCltuNumberOfCltusRadiated" stringIdentifier="forward-cltu-number-of-cltus-radiated" version="1" creationDate="2017-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="FwdCltuNumberOfCltusRadiated&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FwdCltuNumberOfCltusRadiated">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the fwdCltuProductionStat parameter and the production status value applicable since the event occurred." classifier="fwdCltuProductionStatChange" stringIdentifier="forward-cltu-production-status-change" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the fwdCltuProductionStatus value that applies since the notified fwdCltuProductionStatusChange event occurred." classifier="fwdCltuEventProductionStatValue" stringIdentifier="forward-cltu-event-production-status-value" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" typeDefinition="FwdCltuEventProductionStatValue&#x9; ::= FwdCltuProductionStat" engineeringUnit="none">
          <externalTypeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>3</oidBit>
            <oidBit>14</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>8</oidBit>
            <oidBit>1</oidBit>
          </externalTypeOid>
          <typeDef name="FwdCltuEventProductionStatValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.29/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the FwdCltuTsProvider FR type. " classifier="fwdCltuSetContrlParams" stringIdentifier="forward-cltu-set-control-parameters" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>30000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the FwdCltuTsProvider FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="fwdCltuContrParamIdsAndValues" stringIdentifier="forward-cltu-controlled-parameter-identifiers-and-values" version="1" creationDate="2019-08-07T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FwdCltuContrParamIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>30000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FwdCltuContrParamIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Forward Frame CSTS" oidOffset="31000">
    <functionalResource SemanticDefinition="The Forward Frame CSTS Provider functional resource provides service-user-generated Space Link Protocol Data Units (SL-PDUs: transfer frames or Channel Access Data Units) to a functional resource in the Synchronization and Channel Coding FR Stratum. Each instance of the Forward Frame CSTS Provider FR type must be configured to transfer SL-PDUs that conform to the specific Synchronization and Channel Coding FR Set that it accesses. The functions of the Forward Frame CSTS Provider are specified in CCSDS 922.3." classifier="FwdFrameCstsProvider" stringIdentifier="forward-frame-csts-provider" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1000" deprecated="true">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>1000</oidBit>
      </oid>
      <parameter SemanticDefinition="This parameter reports the production status of the Forward Frame service instance. This parameter can take on one of four values:&#xD;&#xA; - 'configured': all production functions needed to support this service instance have been configured ;&#xD;&#xA; - ‘operational’: all production functions have been enabled to process data for this service instance;&#xD;&#xA; - ‘interrupted’: one or more production functions have been stopped because of an error condition that may be temporary;&#xD;&#xA; - ‘halted’: one or more production functions have been stopped by management action.&#xD;&#xA;" classifier="ffSvProductionStat" stringIdentifier="forward-frame-service-production-status" version="1" authorizingEntity="CSSA" oidBit="1" typeDefinition="FfSvcProductionStat &#x9; ::= ProductionStat" configured="false">
        <oid/>
        <typeDef name="FfSvcProductionStat">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.7"/>
          </type>
        </typeDef>
        <externalOid/>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the Forward Frame service. It can take on the following values:&#xD;&#xA; - 'unbound': all resources required to enable the provision of the service have been allocated, and all objects required to provide the service have been instantiated; However, no association yet exists between the user and the provider, i.e., the transfer service provider port is not bound;&#xD;&#xA; - 'ready': an association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of SLPDUs from the user to the provider (by means of the PROCESS-DATA operation) is not permitted; the user may enable the transfer of SLPDUs by means of the appropriate service operation (START), which, in turn, will cause the provider to transition to the state 'active';&#xD;&#xA; - 'active': this state resembles state ‘ready’, except that now the user can send SLPDUs and the provider is enabled  to radiate them to the target Space User Node; the service continues in this state until the user invokes (a) the STOP operation to cause the provider to suspend transmission of SLPDUs and transition back to state 'ready' or (b) the PEER-ABORT invocation to cause the service to transition back to the 'unbound' state.&#xD;&#xA;" classifier="fFrameServiceInstanceState" stringIdentifier="forward-frame-service-instance-state" version="1" authorizingEntity="CSS Area" oidBit="1" typeDefinition="FFrameServiceInstanceState&#x9; ::= SleSiState" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameServiceInstanceState">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.5"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the service instance identifier used in the PDUs exchanged between the user and provider of the Forward Frame service instance. " classifier="ffServiceInstanceId" stringIdentifier="forward-frame-service-instance-identifier" version="1" oidBit="2" typeDefinition="FfServiceInstanceId &#x9; ::= ServiceInstanceId" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfServiceInstanceId">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.12"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the identifier that is used to authenticate BIND invocations.&#xD;&#xA;&#xD;&#xA;The value of this parameter is coordinated between the the User Mission and the Provider Cross Support Service System (CSSS) by secure means. Access to this parameter within the functional resource instance is strictly limited to secure processes of the Provision Management function of the Provider CSSS.&#xD;&#xA;-     This parameter shall not be included in the list of monitorable parameters in  &#xD;&#xA;       cross support Service Agreements. &#xD;&#xA;-     This parameter shall not be included in the list of real-time resettable parameters in &#xD;&#xA;       cross support Service Agreements. &#xD;&#xA;-     This parameter shall not be included in Service Management Configuration Profiles.&#xD;&#xA;-     This parameter shall not be included in  Service Management Event Sequences.&#xD;&#xA;-     This parameter shall not be included in any configuration profile." classifier="ffInitiatorId" stringIdentifier="forward-frame-initiator-identifier" version="1" oidBit="3" typeDefinition="FfInitiatorId       &#x9; ::= VisibleString( (SIZE( 3 .. 16))  ^  (FROM (ALL EXCEPT “ “)))" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfInitiatorId">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="3" max="16"/>
            <permittedAlphabetConstraint type="RANGE">
              <values>ALL EXCEPT “ “</values>
            </permittedAlphabetConstraint>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the identifier that is to be used in BIND returns.&#xD;&#xA;&#xD;&#xA;The value of this parameter is coordinated between the the User Mission and the Provider Cross Support Service System (CSSS) by secure means. Access to this parameter within the functional resource instance is strictly limited to secure processes of the Provision Management function of the Provider CSSS.&#xD;&#xA;-     This parameter shall not be included in the list of monitorable parameters in  &#xD;&#xA;       cross support Service Agreements. &#xD;&#xA;-     This parameter shall not be included in the list of real-time resettable parameters in &#xD;&#xA;       cross support Service Agreements. &#xD;&#xA;-     This parameter shall not be included in Service Management Configuration Profiles.&#xD;&#xA;-     This parameter shall not be included in  Service Management Event Sequences.&#xD;&#xA;-     This parameter shall not be included in any configuration profile." classifier="ffResponderId" stringIdentifier="forward-frame-responder-identifier" version="1" oidBit="4" typeDefinition="FfResponderId       &#x9; ::= VisibleString (SIZE( 3 .. 16)) " configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfResponderId">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="3" max="16"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the logical name of the port used by the underlying communication service to respond to invocation on this service instance. The logical name is translated internally by the service provider to a port identifier that is appropriate to the underlying communication service.&#xD;&#xA;&#xD;&#xA;" classifier="ffResponderPortId" stringIdentifier="forward-frame-responder-port-identifier" version="1" oidBit="5" typeDefinition="FfResponderPortId   &#x9; ::= VisibleString (SIZE( 1 .. 128)) " configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfResponderPortId">
          <type xsi:type="frtypes:CharacterString" type="VisibleString">
            <sizeConstraint min="1" max="128"/>
          </type>
        </typeDef>
      </parameter>
      <parameter classifier="ffServiceUserRespondingTimer" version="1" oidBit="6" typeDefinition="FfServiceUserRespondingTimer&#x9; ::= ServiceUserRespTimer" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfServiceUserRespondingTimer">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.14"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports if the data processing mode of the service instance. This complex parameter can have one of two values:&#xD;&#xA; - 'sequenceControlled': the Sequence-Controlled Frame Data Processing procedure is the prime procedure type;&#xD;&#xA; - 'bufferedData’: the Buffered Frame Data Processing procedure is the prime procedure type.&#xD;&#xA;&#xD;&#xA;The ‘bufferedData’ value is further qualified to specify  the transfer mode as either:&#xD;&#xA;- 'complete'; or&#xD;&#xA; - timely.‘&#xD;&#xA;" classifier="fFrameDataProcessingMode" stringIdentifier="forward-frame-data-processing-mode" version="1" authorizingEntity="CSS Area" oidBit="7" typeDefinition="FFrameDataProcessingMode&#x9; ::= CHOICE&#xA;{&#xA;&#x9; sequenceControlled  &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; bufferedData        &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- This parameter configures and reports the buffered-data transfer mode of the service instance. &#xA;&#x9; &#x9; dataTransferMode    &#x9; ENUMERATED&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; complete            &#x9; &#x9; (0)&#xA;&#x9; &#x9; ,&#x9; timely              &#x9; &#x9; (1)&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; -- This parameter configures and reports the initial value of the maximum size allowed in for incoming Forward Buffers, in number of PROCESS-DATA invocations&#xA;&#x9; &#x9; maxFwdBufferSize    &#x9; LongIntPos&#xA;,&#x9; &#x9; -- The initial value of the processing latency limit in milliseconds, for the data processing procedure of the service instance&#xA;&#x9; &#x9; processingLatencyLimit&#x9; LongIntPos&#xA;&#x9; }&#xA;&#xA;}&#xA;" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameDataProcessingMode">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="sequenceControlled" tag="0" optional="false">
              <type xsi:type="frtypes:Null"/>
            </elements>
            <elements xsi:type="frtypes:Element" name="bufferedData" tag="1" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="dataTransferMode" optional="false" comment="This parameter configures and reports the buffered-data transfer mode of the service instance. ">
                  <type xsi:type="frtypes:Enumerated">
                    <values name="complete"/>
                    <values name="timely" value="1"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="maxFwdBufferSize" optional="false" comment="This parameter configures and reports the initial value of the maximum size allowed in for incoming Forward Buffers, in number of PROCESS-DATA invocations">
                  <type xsi:type="frtypes:TypeReferenceLocal">
                    <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.16"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="processingLatencyLimit" optional="false" comment="The initial value of the processing latency limit in milliseconds, for the data processing procedure of the service instance">
                  <type xsi:type="frtypes:TypeReferenceLocal">
                    <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.16"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the bit mask to applied to the first four octets of each incoming transfer fame) to mask out any bits that are not used to ascertain whether the Global VCID of that transfer frame is authorized to use the Forward Frame service instance.&#xD;&#xA;For the specific space data link protocol being supported by the Forward Frame service instance, the bits corresponding to the Transfer Frame Version Number (TFVN), Spacecraft Identifier (SCID), and Virtual Channel Identifier (VCID) are set to 1, and all other bits in the 4-octet filed are set to 0.&#xD;&#xA;The first 4 octets of each incoming transfer frame is ANDed with the bit mask, and if the result matches the authorized GVCID for the service instance (as specified by fFrameAuthorizedGvcid) the frame has a valid GVCID." classifier="fFrameGvcidBitMask" stringIdentifier="forward-frame-csts-provider-global-vcid-bit-mask" version="1" authorizingEntity="CSS Area" oidBit="8" typeDefinition="FFrameGvcidBitMask  &#x9; ::= OCTET STRING (SIZE( 4)) " configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameGvcidBitMask">
          <type xsi:type="frtypes:OctetString">
            <sizeConstraint min="4" max="4"/>
          </type>
        </typeDef>
      </parameter>
      <parameter classifier="fFrameAuthorizedGvcid" stringIdentifier="forward-frame-csts-provider-authorized-gvcid" version="1" oidBit="9" typeDefinition="FFrameAuthorizedGvcid&#x9; ::= OCTET STRING (SIZE( 4)) " configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameAuthorizedGvcid">
          <type xsi:type="frtypes:OctetString">
            <sizeConstraint min="4" max="4"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the minimum permitted length, in octets, for incoming space link protocol data units (e.g., transfer frames)." classifier="fFrameMinFrameLength" stringIdentifier="forward-frame-csts-provider-minimum-frame-length" version="1" oidBit="10" typeDefinition="FFrameMinFrameLength&#x9; ::= ShortIntPos" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameMinFrameLength">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.18"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the maximum permitted length, in octets, for incoming space link protocol data units (e.g., transfer frames). &#xD;&#xA;The maximum permitted length must be equal to or greater than the minimum permitted length (fFrameMinFrameLength).&#xD;&#xA;" classifier="fFrameMaxFrameLength" stringIdentifier="forward-frame-csts-provider-maximum-frame-length" version="1" oidBit="11" typeDefinition="FFrameMaxFrameLength&#x9; ::= ShortIntPos" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameMaxFrameLength">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.18"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the initial value of the Input Queue size (that is, the size of the queue upon the binding of the service instance), in number of PROCESS-DATA invocations, for the data processing procedure of the service instance." classifier="ffInputQueueSize" stringIdentifier="forward-frame-input-queue-size" version="1" oidBit="12" typeDefinition="FfInputQueueSize    &#x9; ::= LongIntPos" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfInputQueueSize">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.16"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the set of Parameter Label Lists used by the Cyclic Report and/or Information Query procedures of the service instance.&#xD;&#xA;&#xD;&#xA;NOTE - Both the Cyclic Report and Information Query procedures are optional for implementations of the Forward Frame service. If the service implementation supports neither of these procedures, or if either or both are supported but there are no label lists, this parameter shall be undefined." classifier="ffNamedParamLabelLists" stringIdentifier="forward-frame-named-parameter-label-list" version="1" oidBit="13" typeDefinition="-- the values of the paramOrEventId components are constrained to be parameter identifiers&#xA;FfNamedParamLabelLists&#x9; ::= LabelListSet" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfNamedParamLabelLists" comment="the values of the paramOrEventId components are constrained to be parameter identifiers">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.19"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the minimum allowed delivery cycle, in milliseconds, to which any instance of the Cyclic Report procedure can be set.&#xD;&#xA;&#xD;&#xA;If the Cyclic Report procedure is not present in the implementation of the service, this paramter shall be undefined." classifier="ffMinAllowedDeliveryCycle" stringIdentifier="forward-frame-minimum-allowed-delivery-cycle" version="1" oidBit="14" typeDefinition="FfMinAllowedDeliveryCycle&#x9; ::= LongIntPos" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfMinAllowedDeliveryCycle">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.16"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports the set of Event Label Lists used by the Notification procedures of the service instance.&#xD;&#xA;The Notification procedures is optional for implementations of the Forward Frame service. If the service implementation does not support this procedure, or if it is supported but there are no label lists, this parameter shall be undefined." classifier="ffNamedEventLabelLists" stringIdentifier="forward-frame-named-event-label-list " version="1" oidBit="15" typeDefinition="-- The values of the paramOrEventId components are constrained to be event identifiers&#xA;FfNamedEventLabelLists&#x9; ::= LabelListSet" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FfNamedEventLabelLists" comment="The values of the paramOrEventId components are constrained to be event identifiers">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.19"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter configures and reports whether the service instance is enabled to invoke directives on the production functional resources instances that directly support it. This parameter can take on two values:&#xD;&#xA; - 'enabled';&#xD;&#xA; - ‘disabled’.&#xD;&#xA;If the Master Throw Event procedure is not implemented for the service instance, this parameter is undefined and ignored in the configuration of the functional  resource." classifier="fFrameMasterThrowEventEnabled" stringIdentifier="forward-frame-master-throw-event-enabled" version="1" oidBit="16" typeDefinition="FFrameMasterThrowEventEnabled&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; enabled             &#x9; &#x9; (0)&#xA;,&#x9; disabled            &#x9; &#x9; (1)&#xA;}&#xA;" configured="true">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameMasterThrowEventEnabled">
          <type xsi:type="frtypes:Enumerated">
            <values name="enabled"/>
            <values name="disabled" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of space lin protocol data units (e.g., frames), in number of PROCESS-DATA invocations, received by the data processing procedure of the service instance since the beginning of the Service Instance Provision Period. " classifier="fFrameNumberDataUnitsRcvd" stringIdentifier="forward-frame-number-of-data-units-received" version="1" oidBit="17" typeDefinition="FFrameNumberDataUnitsRcvd&#x9; ::= LongIntUnsigned" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameNumberDataUnitsRcvd">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.15"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of spalce link protocol data units (e.g., frames) , in number of PROCESS-DATA invocations, submitted by the data processing procedure of the service instance to service production processing  since the beginning of the Service Instance Provision Period. " classifier="fFrameNumberDataUnitsToProcessing" stringIdentifier="forward-frame-number-of-data-units-submitted-to-processing" version="1" oidBit="18" typeDefinition="FFrameNumberDataUnitsToProcessing&#x9; ::= LongIntUnsigned" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>18</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameNumberDataUnitsToProcessing">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.15"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of space link protocol data units (e.g., frames) in number of PROCESS-DATA invocations, that have been reported by service production to have completed processing – i.e., to have been radiated - since the beginning of the Service Instance Provision Period" classifier="fFrameNumberDataUnitsProcessed" stringIdentifier="forward-frame-number-of-data-units-submitted- processed" version="1" oidBit="19" typeDefinition="FFrameNumberDataUnitsProcessed&#x9; ::= LongIntUnsigned" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>19</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="FFrameNumberDataUnitsProcessed">
          <type xsi:type="frtypes:TypeReferenceLocal">
            <typeDefinition href="../work-jp/FwdFrameCstsProvider.frm#//@asnTypeModule/@typeDefinition.15"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies that the production status of the Forward Frame service instance has changed.&#xD;&#xA;" classifier="ffSvcProductionStatChange" stringIdentifier="forward-frame-service-production-status-change" version="1" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="This event carries the new value of the production status following the change:&#xD;&#xA; - 'configured': all production functions needed to support this service instance are configured ;&#xD;&#xA; - ‘operational’: all production functions have been enabled to process data for this service instance;&#xD;&#xA; - ‘interrupted’: one or more production functions have been stopped because of an error condition that may be temporary;&#xD;&#xA; - ‘halted’: one or more production functions have been stopped by management action." classifier="ffSvcProductionStatChangeValue" stringIdentifier="forward-frame-service-production-status-change-value" version="1" oidBit="1" typeDefinition="FfSvcProductionStatChangeValue&#x9; ::= FfSvcProductionStat">
          <oid/>
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="FfSvcProductionStatChangeValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.30/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <event SemanticDefinition="This event triggers when a one or more parameters controlling the configuration of service production have been changed.&#xD;&#xA;&#xD;&#xA;This event carries no additional information." classifier="ffSvcProductionConfgurationChange" stringIdentifier="forward-frame-service-production-configuration-change" version="1" oidBit="2">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
      </event>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Terrestrial File Transfer" oidOffset="32000">
    <functionalResource SemanticDefinition="none" classifier="TgftHost" stringIdentifier="Cross Support File Transfer Service Provider" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="32000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>32000</oidBit>
      </oid>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="SLE Return All Frames" oidOffset="33000">
    <functionalResource SemanticDefinition="The RafTsProvider accepts as input the frame provided by the RtnTmSyncAndDecoding and the OfflineFrameBuffer FRs." classifier="RafTsProvider" stringIdentifier="return-all-frames-transfer-service-provider" version="1" creationDate="2019-03-26T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="33000" uses="//@functionalResourceSet.7/@functionalResource.0 //@functionalResourceSet.20/@functionalResource.0">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>33000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the service production process used by the given instance of an RAF service. It can take on the following values:&#xA;- 'running' - the RAF production process is capable of processing a return space link physical channel, if available;&#xA;- 'interrupted' - the RAF production process is stopped due to a fault;&#xA;- 'halted' - the RAF production process is stopped and production equipment is out of service due to management action." classifier="rafProductionStatus" stringIdentifier="return-all-frames-production-status" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RafProductionStatus &#x9; ::= SleRtnProductionStat" engineeringUnit="none" configured="true" guardCondition="Setting of the rafProductionStatus to 'interrupted' by means of the directive rafSetControlParameters is not permissible.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafProductionStatus">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.8"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the identifier of the given service instance. " classifier="rafServiceInstanceIdentifier" stringIdentifier="return-all-frames-service-instance-identifier" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RafServiceInstanceIdentifier&#x9; ::= SleServiceInstanceId" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafServiceInstanceIdentifier">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.2"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the delivery mode of the given RAF service instance. It can take on three values:&#xD;&#xA;- 'online timely'  - the timely online delivery mode limits the size of the backlog of not yet delivered data that is allowed to accumulate by discarding data that cannot be delivered within a certain time. Furthermore, when data is discarded, it is discarded ‘in chunks’, i.e., as a sufficiently large block of contiguous frames rather than as random frames here and there; in general, this approach maximizes the usefulness of the data that is delivered.&#xD;&#xA;- 'online complete' - this delivery mode attempts to deliver all acquired frames having the user selected characteristics, in order, with minimum delay consistent with the available ground communications bandwidth. To that end, the service provider has a buffer sufficiently large to deal with communications service delays, outages, and bandwidth limitations;&#xD;&#xA;- 'offline' - in this delivery mode, the provider side buffer enables data to be delivered hours or days after their acquisition. To that end, this buffer is sufficiently large to hold all data that might be accumulated during several space link sessions." classifier="rafDeliveryMode" stringIdentifier="return-all-frames-delivery-mode" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RafDeliveryMode     &#x9; ::= SleRtnDeliveryMode" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafDeliveryMode">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.1"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the setting of the maximum time period in seconds permitted from when a confirmed RAF operation is invoked until the return is received by the invoker." classifier="rafReturnTimeoutPeriod" stringIdentifier="return-all-frames-return-timeout-period" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RafReturnTimeoutPeriod&#x9; ::= SleReturnTimeout" engineeringUnit="s" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafReturnTimeoutPeriod" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.6"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum allowable delivery latency time, in seconds, for the online delivery modes, i.e., the maximum delay from when the frame is acquired by the provider until it is delivered to the user.  If rafDeliveryMode = 'offline', rafLatencyLimit has no effect." classifier="rafLatencyLimit" stringIdentifier="return-all-frames-latency-limit" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="-- The engineering unit of this parameter is second&#xA;RafLatencyLimit     &#x9; ::= INTEGER  (1 .. 100)" engineeringUnit="s" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafLatencyLimit" comment="The engineering unit of this parameter is second">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of frames the provider shall block in one RAF-PDU before passing it to the underlying communications layer except if expiry of the latency limit requires earlier release of the RAF-PDU." classifier="rafTransferBufferSize" stringIdentifier="return-all-frames-transfer-buffer-size" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="RafTransferBufferSize&#x9; ::= INTEGER  (1 .. 1000)" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafTransferBufferSize">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="1000"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the set of the quality of the frames that the user may select with the given RAF service instance. The permitted frame quality settings must be chosen from the following set of values: &#xD;&#xA;- 'good' - only frames that based on successful RS or LDPC decoding/correcting and/or based on the check of the FECF are assumed to be error free will be delivered to the user;&#xD;&#xA;- 'erred' - only frames that based on unsuccessful RS or LDPC decoding/correcting or  based on the failed check of the FECF were found to contain errors will be delivered to the user;&#xD;&#xA;- 'all'  - all frames regardless if error free or not are delivered to the user; this includes telemetry frames for which due to lack of compatibility with the pertinent CCSDS Recommendations the quality cannot be determined." classifier="rafPermittedFrameQuality" stringIdentifier="return-all-frames-permitted-frame-quality" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="RafPermittedFrameQuality&#x9; ::= SET  (SIZE( 1 .. 3))  OF&#x9; &#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; good                &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; erred               &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; all                 &#x9; &#x9; (2)&#xA;&#x9; }&#xA;" engineeringUnit="none" configured="true" guardCondition="Setting of this parameter by means of the rafSetControlParameters directive is only permissible while rafSiState = 'unbound'.">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafPermittedFrameQuality">
          <type xsi:type="frtypes:SetOf">
            <sizeConstraint min="1" max="3"/>
            <elements xsi:type="frtypes:Element" name="frameQuality" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="good"/>
                <values name="erred" value="1"/>
                <values name="all" value="2"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the quality of the frames that shall be delivered by the given RAF service instance. It can take on three values:&#xD;&#xA;- 'good' - only frames that based on successful RS or LDPC decoding/correcting and/or based on the check of the FECF are assumed to be error free will be delivered to the user;&#xD;&#xA;- 'erred' - only frames that based on unsuccessful RS or LDPC decoding/correcting and/or based on the failed check of the FECF were found to contain errors will be delivered to the user;&#xD;&#xA;- 'all'  - all frames regardless if error free or not are delivered to the user; this includes telemetry frames for which due to lack of compatibility with the pertinent CCSDS Recommendations the quality cannot be determined.&#xD;&#xA;If the given service instance does not constrain this parameter to a single value, it shall be flagged 'undefined' whenever si-state ≠ 'active'." classifier="rafRequestedFrameQuality" stringIdentifier="return-all-frames-requested-frame-quality" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="RafRequestedFrameQuality&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; good                &#x9; &#x9; (0)&#xA;,&#x9; erred               &#x9; &#x9; (1)&#xA;,&#x9; all                 &#x9; &#x9; (2)&#xA;}&#xA;" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafRequestedFrameQuality">
          <type xsi:type="frtypes:Enumerated">
            <values name="good"/>
            <values name="erred" value="1"/>
            <values name="all" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the RAF service. It can take on the following values:&#xD;&#xA;- 'unbound' - All resources required to enable the provision of the RAF service have been allocated, and all objects required to provide the service have been instantiated; however, no association yet exists between the user and the provider, i.e., the RAF transfer service provider port is not bound;&#xD;&#xA;- 'ready'- An association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of telemetry frames from the provider to the user (by means of the RAF-TRANSFER-DATA operation) is not permitted; the user may enable the delivery of telemetry frames by means of the appropriate service operation (RAF-START), which, in turn, will cause the provider to transition to the state 'active';&#xD;&#xA;- 'active' -  This state resembles state ‘ready’, except that now the provider will send telemetry frames provided frames of the selected characteristics are made available by the RAF production process; the service continues in this state until the user invokes either the RAF-STOP operation to cause the provider to suspend delivery of telemetry frames and transition back to state 'ready' or the PEER-ABORT invocation to cause the service to transition back to the 'unbound' state." classifier="rafSiState" stringIdentifier="return-all-frames-service-instance-state" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="RafSiState          &#x9; ::= SleSiState" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafSiState">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.5"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current setting of the time in seconds between successive RAF-STATUS-REPORT invocations sent by the RAF service provider. The permissible values are in the range (2 .. 600). If cyclic reporting is off, this parameter shall be flagged as 'undefined'." classifier="rafReportingCycle" stringIdentifier="return-all-frames-reporting-cycle" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RafReportingCycle   &#x9; ::= SleReportingCycle" engineeringUnit="s" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafReportingCycle" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.13"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the  total number of telemetry frames that were annotated with a frame-quality of ‘good’ and delivered to the user since the start of the service instance provision period." classifier="rafNumberOfErrorFreeFramesDelivered" stringIdentifier="return-all-frames-number-of-error-free-frames-delivered" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="RafNumberOfErrorFreeFramesDelivered&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafNumberOfErrorFreeFramesDelivered">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the  total number of telemetry frames that were delivered to the user since the start of the service instance provision period." classifier="rafNumberOfFramesDelivered" stringIdentifier="return-all-frames-number-of-frames-delivered" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="RafNumberOfFramesDelivered&#x9; ::= INTEGER  (0 .. 4294967295)" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RafNumberOfFramesDelivered">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
      <event SemanticDefinition="This event notifies any change of the rafProductionStatus parameter. " classifier="rafProductionStatusChange" stringIdentifier="return-all-frames-roduction-status-change" version="1" creationDate="2016-02-29T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <value SemanticDefinition="The event value reports the rafTsProductionStatus value that applies since the notified rafTsProductionStatusChange event occurred." classifier="rafEventProductionStatusValue" stringIdentifier="return-all-frames-production-status-value" version="1" creationDate="2019-09-11T01:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RafEventProductionStatusValue&#x9; ::= RafProductionStatus" engineeringUnit="none">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>33000</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RafEventProductionStatusValue">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@functionalResourceSet.32/@functionalResource.0/@parameter.0/@typeDef"/>
          </typeDef>
        </value>
      </event>
      <directives SemanticDefinition="This directive permits setting of the controllable parameters of the RafTsProvider FR type. " classifier="rafSetControlParameters" stringIdentifier="return-all-frames-set-control-parameters" version="1" creationDate="2016-02-29T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" guardCondition="None">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>33000</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <qualifier SemanticDefinition="The directive qualifier specifies the FR instance the directive shall act on and contains a set of parameter identifier and parameter value pairs. To be valid, the parameter identifier must reference a controllable parameter of the RafTsProvider FR and the parameter value must be of the same type as the parameter value that shall be set." classifier="rafControlledParameterIdsAndValues" stringIdentifier="return-all-frames-controlled-parameter-identifiers-and-values" version="1" creationDate="2016-02-29T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RafControlledParameterIdsAndValues&#x9; ::= DirectiveQualifier" engineeringUnit="depends on the specific paramter(s) being set ">
          <typeOid>
            <oidBit>1</oidBit>
            <oidBit>3</oidBit>
            <oidBit>112</oidBit>
            <oidBit>4</oidBit>
            <oidBit>4</oidBit>
            <oidBit>2</oidBit>
            <oidBit>1</oidBit>
            <oidBit>33000</oidBit>
            <oidBit>3</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
            <oidBit>1</oidBit>
          </typeOid>
          <typeDef name="RafControlledParameterIdsAndValues">
            <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.9"/>
          </typeDef>
        </qualifier>
      </directives>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="SLE Return Channel Frames" oidOffset="34000">
    <functionalResource SemanticDefinition="The RcfTsProvider accepts as input the frames provided by the RtnTmSyncAndDecoding and the OfflineFrameBuffer FRs. It delivers the frames of the selected Master or Virtual Channel." classifier="RcfTsProvider" stringIdentifier="return-channel-frames-transfer-service-provider" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="34000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>34000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the service production process used by the given instance of an RCF service. It can take on the following values:&#xA;- 'running' - the RCF production process is capable of processing a return link Master or Virtual Channel, if available;&#xA;- 'interrupted' - the RCF production process is stopped due to a fault;&#xA;- 'halted' - the RCF production process is stopped and production equipment is out of service due to management action." classifier="rcfProductionStatus" stringIdentifier="return-channel-frames-production-status" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RcfProductionStatus &#x9; ::= SleRtnProductionStat" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfProductionStatus">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.8"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the identifier of the given service instance. " classifier="rcfServiceInstanceIdentifier" stringIdentifier="return-channel-frames-service-instance-identifier" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RcfServiceInstanceIdentifier&#x9; ::= SleServiceInstanceId" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfServiceInstanceIdentifier">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.2"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the delivery mode of the given RCF service instance. It can take on three values:&#xD;&#xA;- 'online timely'  - the timely online delivery mode limits the size of the backlog of not yet delivered data that is allowed to accumulate by discarding data that cannot be delivered within a certain time. Furthermore, when data is discarded, it is discarded ‘in&#xD;&#xA;chunks’, i.e., as a sufficiently large block of contiguous frames rather than as random frames here and there; in general, this approach maximizes the usefulness of the data that is delivered.&#xD;&#xA;- 'online complete' - this delivery mode attempts to deliver all acquired frames having the user selected characteristics, in order, with minimum delay consistent with the available ground communications bandwidth. To that end, the service provider has a buffer sufficiently large to deal with communications service delays, outages, and bandwidth limitations;&#xD;&#xA;- 'offline' - in this delivery mode, the provider side buffer enables data to be delivered hours or days after their acquisition. To that end, this buffer is sufficiently large to hold all data that might be accumulated during several space link sessions." classifier="rcfDeliveryMode" stringIdentifier="return-channel-frames-delivery-mode" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RcfDeliveryMode     &#x9; ::= SleRtnDeliveryMode" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfDeliveryMode">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.1"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the setting of the maximum time period in seconds permitted from when a confirmed RCF operation is invoked until the return is received by the invoker." classifier="rcfReturnTimeoutPeriod" stringIdentifier="return-channel-frames-return-timeout-period" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RcfReturnTimeoutPeriod&#x9; ::= SleReturnTimeout" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfReturnTimeoutPeriod" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.6"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum allowable delivery latency time, in seconds, for the online delivery modes, i.e., the maximum delay from when the frame is acquired by the provider until it is delivered to the user. This parameter shall be flagged as undefined if delivery-mode = 'offline'." classifier="rcfLatencyLimit" stringIdentifier="return-channel-frames-latency-limit" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RcfLatencyLimit     &#x9; ::= INTEGER  (1 .. 100)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfLatencyLimit" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of frames the provider shall block in one RCF-PDU before passing it to the underlying communications layer except if expiry of latency-limit requires earlier release of the RCF-PDU. If delivery-mode = 'offline', latency-limit has no effect." classifier="rcfTransferBufferSize" stringIdentifier="return-channel-frames-transfer-buffer-size" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="RcfTransferBufferSize&#x9; ::= INTEGER  (1 .. 100)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfTransferBufferSize">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Master and/or Virtual Channel that the given RCF service instance permits the user to select. The parameter is a set of the concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID). The range of the Spacecraft Identifier and the Virtual Channel Identifier depend on the TFVN as follows:&#xA;- TFVN = 0 (version 1) - SCID = (0 .. 1023)10, VCID = (0 .. 7)10;&#xA;- TFVN = 1 (version 2) - SCID = (0 .. 255)10, VCID = (0 .. 63)10." classifier="rcfPermittedGlobalVcidSet" stringIdentifier="return-channel-frames-permitted-global-vcid-set " version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="RcfPermittedGlobalVcidSet&#x9; ::= SET  OF&#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; tm                  &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The TFVN of Tm frames is always 0.&#xA;&#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- For the Master Channel the vcid remains unspecified.&#xA;&#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 7)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; aos                 &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (1)&#xA;,&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- For the Master Channel the vcid remains unspecified.&#xA;&#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfPermittedGlobalVcidSet">
          <type xsi:type="frtypes:SetOf">
            <elements xsi:type="frtypes:Choice">
              <elements xsi:type="frtypes:Element" name="tm" tag="0" optional="false">
                <type xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of Tm frames is always 0.">
                    <type xsi:type="frtypes:IntegerType">
                      <singleValueConstraint>
                        <values>0</values>
                      </singleValueConstraint>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="scid" optional="false">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="0" max="1023"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                    <type xsi:type="frtypes:Choice">
                      <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="For the Master Channel the vcid remains unspecified.">
                        <type xsi:type="frtypes:Null"/>
                      </elements>
                      <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                        <type xsi:type="frtypes:IntegerType">
                          <rangeConstraint min="0" max="7"/>
                        </type>
                      </elements>
                    </type>
                  </elements>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="aos" tag="1" optional="false">
                <type xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of AOS frames is always 1.">
                    <type xsi:type="frtypes:IntegerType">
                      <singleValueConstraint>
                        <values>1</values>
                      </singleValueConstraint>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="scid" optional="false">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="0" max="255"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                    <type xsi:type="frtypes:Choice">
                      <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="For the Master Channel the vcid remains unspecified.">
                        <type xsi:type="frtypes:Null"/>
                      </elements>
                      <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                        <type xsi:type="frtypes:IntegerType">
                          <rangeConstraint min="0" max="63"/>
                        </type>
                      </elements>
                    </type>
                  </elements>
                </type>
              </elements>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the Master or Virtual Channel that the given RCF service instance shall deliver to the user. The parameter is a concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID). The range of the Spacecraft Identifier and the Virtual Channel Identifier depend on the TFVN as follows:&#xA;- TFVN = 0 (version 1) - SCID = (0 .. 1023)10, VCID = (0 .. 7)10;&#xA;- TFVN = 1 (version 2) - SCID = (0 .. 255)10, VCID = (0 .. 63)10.&#xA;If the global VCID is not constrained to a single value by the given RCF service instance (see permitted-global-vcid-set), then this parameter shall be flagged undefined as long as si-state ≠ 'active." classifier="rcfRequestedGlobalVcid" stringIdentifier="return-channel-frames-requested-global-vcid" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="RcfRequestedGlobalVcid&#x9; ::= CHOICE&#xA;{&#xA;&#x9; tm                  &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The TFVN of TM frames is always 0.&#xA;&#x9; &#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- In case the Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 7)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; aos                 &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; &#x9; tfvn                &#x9; INTEGER  (1)&#xA;,&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- When a Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfRequestedGlobalVcid">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="tm" tag="0" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of TM frames is always 0.">
                  <type xsi:type="frtypes:IntegerType">
                    <singleValueConstraint>
                      <values>0</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="1023"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="In case the Master Channel is selected, the VCID remains unspecified.">
                      <type xsi:type="frtypes:Null"/>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="vcid" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="0" max="7"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="aos" tag="1" optional="false" comment="The TFVN of AOS frames is always 1.">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of AOS frames is always 1.">
                  <type xsi:type="frtypes:IntegerType">
                    <singleValueConstraint>
                      <values>1</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="255"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="When a Master Channel is selected, the VCID remains unspecified.">
                      <type xsi:type="frtypes:Null"/>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="0" max="63"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the RCF service. It can take on the following values:&#xA;- 'unbound' - All resources required to enable the provision of the RCF service have been allocated, and all objects required to provide the service have been instantiated; however, no association yet exists between the user and the provider, i.e., the RCF transfer service provider port is not bound;&#xA;- 'ready'- An association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of telemetry frames from the provider to the user (by means of the RCF-TRANSFER-DATA operation) is not permitted; the user may enable the delivery of telemetry frames by means of the appropriate service operation (RCF-START), which, in turn, will cause the provider to transition to the state 'active';&#xA;- 'active' -  This state resembles state ‘ready’, except that now the provider will send telemetry frames provided frames of the selected characteristics are made available by the RCF production process; the service continues in this state until the user invokes the RCF-STOP operation to cause the provider to suspend delivery of telemetry frames and transition back to state 'ready' or the PEER-ABORT invocation to cause the service to transition back to the 'unbound' state." classifier="rcfSiState" stringIdentifier="return-channel-frames-service-instance-state" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="RcfSiState          &#x9; ::= SleSiState" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfSiState">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.5"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current setting of the time in seconds between successive RCF-STATUS-REPORT invocations sent by the RCF service provider. The permissible values are in the range (2 .. 600). If cyclic reporting is off, the value reported is zero." classifier="rcfReportingCycle" stringIdentifier="return-channel-frames-reporting-cycle" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="RcfReportingCycle   &#x9; ::= SleReportingCycle" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfReportingCycle">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.13"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the total number of telemetry frames that were delivered to the user since the start of the service instance provision period." classifier="rcfNumberOfFramesDelivered" stringIdentifier="return-channel-frames-number-of-frames-delivered" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="RcfNumberOfFramesDelivered&#x9; ::= INTEGER  (0 .. 4294967295)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>34000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RcfNumberOfFramesDelivered">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="SLE Return Operational Control Fields" oidOffset="35000">
    <functionalResource SemanticDefinition="The RocfTsProvider accepts as input the frames provided by the RtnTmSyncAndDecoding and the OfflineFrameBuffer FRs. It delivers the Operational Control Fields (OCF) extracted from the frames of the selected Master or Virtual Channel provided the OCFs meet the other selection criteria set by the ROCF service user." classifier="RocfTsProvider" stringIdentifier="return-operational-control-fields-transfer-service-provider" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="35000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>35000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the service production process used by the given instance of an ROCF service. It can take on the following values:&#xA;- 'running' - the ROCF production process is capable of processing a return link Master or Virtual Channel, if available;&#xA;- 'interrupted' - the ROCF production process is stopped due to a fault;&#xA;- 'halted' - the ROCF production process is stopped and production equipment is out of service due to management action." classifier="rocfProductionStatus" stringIdentifier="return-operational-control-fields-production-status" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="1" typeDefinition="RocfProductionStatus&#x9; ::= SleRtnProductionStat" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfProductionStatus">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.8"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the identifier of the given service instance. " classifier="rocfServiceInstanceIdentifier" stringIdentifier="return-operational-control-fields-service-instance-identifier" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="2" typeDefinition="RocfServiceInstanceIdentifier&#x9; ::= SleServiceInstanceId" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfServiceInstanceIdentifier">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.2"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the delivery mode of the given ROCF service instance. It can take on three values:&#xD;&#xA;- 'online timely'  - the timely online delivery mode limits the size of the backlog of not yet delivered data that is allowed to accumulate by discarding data that cannot be delivered within a certain time. Furthermore, when data is discarded, it is discarded ‘in&#xD;&#xA;chunks’, i.e., as a OCFs extracted from a sufficiently large block of contiguous frames rather than as OCFs from random frames here and there; in general, this approach maximizes the usefulness of the data that is delivered.&#xD;&#xA;- 'online complete' - this delivery mode attempts to deliver the OCFs having the user selected characteristics from all acquired frames, in order, with minimum delay consistent with the available ground communications bandwidth. To that end, the service provider has a buffer be sufficiently large to deal with communications service delays, outages, and bandwidth limitations;&#xD;&#xA;- 'offline' - in this delivery mode, the provider side buffer enables data to be delivered hours or days after their acquisition. To that end, this buffer is sufficiently large to hold all data that might be accumulated during several space link sessions." classifier="rocfDeliveryMode" stringIdentifier="return-operational-control-fields-delivery-mode" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="3" typeDefinition="RocfDeliveryMode    &#x9; ::= SleRtnDeliveryMode" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfDeliveryMode">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.1"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the setting of the maximum time period in seconds permitted from when a confirmed ROCF operation is invoked until the return is received by the invoker." classifier="rocfReturnTimeoutPeriod" stringIdentifier="return-operational-control-fields-return-timeout-period" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="4" typeDefinition="-- The engineering unit of this parameter is second&#xA;RocfReturnTimeoutPeriod&#x9; ::= SleReturnTimeout" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>4</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfReturnTimeoutPeriod" comment="The engineering unit of this parameter is second">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.6"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the maximum allowable delivery latency time, in seconds, for the online delivery modes, i.e., the maximum delay from when the provider extracts an OCF from a newly acquired frame until it is delivered to the user: this parameter shall be flagged as undefined if delivery-mode = 'offline'." classifier="rocfLatencyLimit" stringIdentifier="return-operational-control-field-latency-limit" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="5" typeDefinition="-- The engineering unit of this parameter is second.&#xA;RocfLatencyLimit    &#x9; ::= INTEGER  (1 .. 100)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>5</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfLatencyLimit" comment="The engineering unit of this parameter is second.">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the number of OCFs the provider shall block in one ROCF-PDU before passing it to the underlying communications layer except if expiry of latency-limit requires earlier release of the ROCF-PDU. If delivery-mode = 'offline', latency-limit has no effect." classifier="rocfTransferBufferSize" stringIdentifier="return-operational-control-fields-transfer-buffer-size" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="6" typeDefinition="RocfTransferBufferSize&#x9; ::= INTEGER  (1 .. 100)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>6</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfTransferBufferSize">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="1" max="100"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the telemetry Master and/or Virtual Channel out of which the given ROCF service instance extracts the OCFs. The parameter is a set of the concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID). The range of the Spacecraft Identifier and the Virtual Channel Identifier depend on the TFVN as follows:&#xA;- TFVN = 0 (version 1) - SCID = (0 .. 1023)10, VCID = (0 .. 7)10;&#xA;- TFVN = 1 (version 2) - SCID = (0 .. 255)10, VCID = (0 .. 63)10." classifier="rocfPermittedGlobalVcidSet" stringIdentifier="return-operational-control-field-permitted-global-vcid-set " version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="7" typeDefinition="RocfPermittedGlobalVcidSet&#x9; ::= SET  OF&#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; tm                  &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The TFVN of Tm frames is always 0.&#xA;&#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- For the Master Channel the vcid remains unspecified.&#xA;&#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 7)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;,&#x9; &#x9; aos                 &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; &#x9; &#x9; tfvn                &#x9; INTEGER  (1)&#xA;,&#x9; &#x9; &#x9; scid                &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; &#x9; -- For the Master Channel the vcid remains unspecified.&#xA;&#x9; &#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; &#x9; }&#xA;&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>7</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfPermittedGlobalVcidSet">
          <type xsi:type="frtypes:SetOf">
            <elements xsi:type="frtypes:Choice">
              <elements xsi:type="frtypes:Element" name="tm" tag="0" optional="false">
                <type xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of Tm frames is always 0.">
                    <type xsi:type="frtypes:IntegerType">
                      <singleValueConstraint>
                        <values>0</values>
                      </singleValueConstraint>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="scid" optional="false">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="0" max="1023"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                    <type xsi:type="frtypes:Choice">
                      <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="For the Master Channel the vcid remains unspecified.">
                        <type xsi:type="frtypes:Null"/>
                      </elements>
                      <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                        <type xsi:type="frtypes:IntegerType">
                          <rangeConstraint min="0" max="7"/>
                        </type>
                      </elements>
                    </type>
                  </elements>
                </type>
              </elements>
              <elements xsi:type="frtypes:Element" name="aos" tag="1" optional="false">
                <type xsi:type="frtypes:Sequence">
                  <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of AOS frames is always 1.">
                    <type xsi:type="frtypes:IntegerType">
                      <singleValueConstraint>
                        <values>1</values>
                      </singleValueConstraint>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="scid" optional="false">
                    <type xsi:type="frtypes:IntegerType">
                      <rangeConstraint min="0" max="255"/>
                    </type>
                  </elements>
                  <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                    <type xsi:type="frtypes:Choice">
                      <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="For the Master Channel the vcid remains unspecified.">
                        <type xsi:type="frtypes:Null"/>
                      </elements>
                      <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                        <type xsi:type="frtypes:IntegerType">
                          <rangeConstraint min="0" max="63"/>
                        </type>
                      </elements>
                    </type>
                  </elements>
                </type>
              </elements>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of Master and/or Virtual Channel that the given ROCF service instance permits the user to select. The parameter is a set of the concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID). The range of the Spacecraft Identifier and the Virtual Channel Identifier depend on the TFVN as follows:&#xA;- TFVN = 0 (version 1) - SCID = (0 .. 1023)10, VCID = (0 .. 7)10;&#xA;- TFVN = 1 (version 2) - SCID = (0 .. 255)10, VCID = (0 .. 63)10.&#xA;If the global VCID is not constrained to a single value by the given ROCF service instance, then this parameter shall be flagged as undefined as long as si-state ≠ 'active." classifier="rocfRequestedGlobalVcid" stringIdentifier="return-operational-control-fields-requested-global-vcid" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="8" typeDefinition="RocfRequestedGlobalVcid&#x9; ::= CHOICE&#xA;{&#xA;&#x9; tm                  &#x9; [0]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The TFVN of TM frames is always 0.&#xA;&#x9; &#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- In case the Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 7)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;,&#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; aos                 &#x9; [1]&#x9; &#x9; SEQUENCE&#xA;&#x9; {&#xA;&#x9; &#x9; -- The TFVN of AOS frames is always 1.&#xA;&#x9; &#x9; tfvn                &#x9; INTEGER  (1)&#xA;,&#x9; &#x9; scid                &#x9; INTEGER  (0 .. 255)&#xA;,&#x9; &#x9; vcid                &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- When a Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; virtualChannel      &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; }&#xA;&#xA;&#x9; }&#xA;&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>8</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfRequestedGlobalVcid">
          <type xsi:type="frtypes:Choice">
            <elements xsi:type="frtypes:Element" name="tm" tag="0" optional="false">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of TM frames is always 0.">
                  <type xsi:type="frtypes:IntegerType">
                    <singleValueConstraint>
                      <values>0</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="1023"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="In case the Master Channel is selected, the VCID remains unspecified.">
                      <type xsi:type="frtypes:Null"/>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="vcid" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="0" max="7"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="aos" tag="1" optional="false" comment="The TFVN of AOS frames is always 1.">
              <type xsi:type="frtypes:Sequence">
                <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of AOS frames is always 1.">
                  <type xsi:type="frtypes:IntegerType">
                    <singleValueConstraint>
                      <values>1</values>
                    </singleValueConstraint>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="scid" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="255"/>
                  </type>
                </elements>
                <elements xsi:type="frtypes:Element" name="vcid" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="When a Master Channel is selected, the VCID remains unspecified.">
                      <type xsi:type="frtypes:Null"/>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="virtualChannel" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="0" max="63"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the set of OCF types that the given ROCF service instance permits the user to select. Three types are defined:&#xA;- 'all control word types' - all OCFs shall be delivered regardless of their type;&#xA;- 'clcw' - only OCFs containing the CCSDS defined Communication Link Control Word (CLCW) data structure shall be delivered;&#xA;- 'not-clcw' - only privately defined OCFs shall be delivered.   " classifier="rocfPermittedControlWordTypeSet" stringIdentifier="return-operational-control-fields-permitted-control-word-type-set " version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="9" typeDefinition="RocfPermittedControlWordTypeSet&#x9; ::= SET  (SIZE( 1 .. 3))  OF&#x9; &#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; allControlWordTypes &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; clcwsOnly           &#x9; &#x9; (1)&#xA;&#x9; ,&#x9; nonClcwsOnly        &#x9; &#x9; (2)&#xA;&#x9; }&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>9</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfPermittedControlWordTypeSet">
          <type xsi:type="frtypes:SetOf">
            <sizeConstraint min="1" max="3"/>
            <elements xsi:type="frtypes:Element" name="controlWordType" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="allControlWordTypes"/>
                <values name="clcwsOnly" value="1"/>
                <values name="nonClcwsOnly" value="2"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the type of the OCFs that shall be delivered. It can take on three values:&#xD;&#xA;- all control word types' - the service provider delivers all OCFs extracted from the selected telemetry channel, regardless of the control word type they contain;&#xD;&#xA;- 'clcws only' - the service provider delivers the OCFs extracted from the selected telemetry channel that contain CLCW reports;&#xD;&#xA;- 'non-clcws only' - the service provider delivers the OCFs extracted from the selected telemetry channel that contain reports different from CLCWs." classifier="rocfRequestedControlWordType" stringIdentifier="return-operational-control-fields-requested-control-word-type" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="10" typeDefinition="RocfRequestedControlWordType&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; allControlWordTypes &#x9; &#x9; (0)&#xA;,&#x9; clcwsOnly           &#x9; &#x9; (1)&#xA;,&#x9; nonClcwsOnly        &#x9; &#x9; (2)&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>10</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfRequestedControlWordType">
          <type xsi:type="frtypes:Enumerated">
            <values name="allControlWordTypes"/>
            <values name="clcwsOnly" value="1"/>
            <values name="nonClcwsOnly" value="2"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the TC Master Channel and/or the Virtual Channels that the CLCWs that shall be extracted are associated with. If requested-control-word-type ≠ 'clcw', this parameter shall be flagged as undefined. " classifier="rocfPermittedTcVcidSet" stringIdentifier="return-operational-control-fields-permitted-tc-vcid-set" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="11" typeDefinition="RocfPermittedTcVcidSet&#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The TFVN of TC frames is always 0.&#xA;&#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; vcid                &#x9; SET  (SIZE( 1 .. 65))  OF&#x9; &#x9; &#x9; &#x9; CHOICE&#xA;&#x9; &#x9; {&#xA;&#x9; &#x9; &#x9; -- When the Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; &#x9; vcid                &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; &#x9; }&#xA;&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>11</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfPermittedTcVcidSet">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of TC frames is always 0.">
              <type xsi:type="frtypes:IntegerType">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="vcid" optional="false">
              <type xsi:type="frtypes:SetOf">
                <sizeConstraint min="1" max="65"/>
                <elements xsi:type="frtypes:Element" name="vcOrMc" optional="false">
                  <type xsi:type="frtypes:Choice">
                    <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="When the Master Channel is selected, the VCID remains unspecified.">
                      <type xsi:type="frtypes:Null"/>
                    </elements>
                    <elements xsi:type="frtypes:Element" name="vcid" tag="1" optional="false">
                      <type xsi:type="frtypes:IntegerType">
                        <rangeConstraint min="0" max="63"/>
                      </type>
                    </elements>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports which TC Master Channel or Virtual Channel the OCFs to be delivered shall be associated with. The parameter is a concatenation of the CCSDS assigned Spacecraft Identifier (SCID), the Transfer Frame Version Number (TFVN) and, if applicable, the Virtual Channel Identifier (VCID).&#xA;If requested-control-word-type ≠ clcw, this parameter shall be flagged as undefined." classifier="rocfRequestedTcVcid" stringIdentifier="return-operational-control-field-requested-tc-vcid" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="12" typeDefinition="RocfRequestedTcVcid &#x9; ::= SEQUENCE&#xA;{&#xA;&#x9; -- The TFVN of TC frames is always 0.&#xA;&#x9; tfvn                &#x9; INTEGER  (0)&#xA;,&#x9; scid                &#x9; INTEGER  (0 .. 1023)&#xA;,&#x9; vcid                &#x9; CHOICE&#xA;&#x9; {&#xA;&#x9; &#x9; -- When the Master Channel is selected, the VCID remains unspecified.&#xA;&#x9; &#x9; masterChannel       &#x9; [0]&#x9; &#x9; NULL&#xA;,&#x9; &#x9; vcid                &#x9; [1]&#x9; &#x9; INTEGER  (0 .. 63)&#xA;&#x9; }&#xA;&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>12</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfRequestedTcVcid">
          <type xsi:type="frtypes:Sequence">
            <elements xsi:type="frtypes:Element" name="tfvn" optional="false" comment="The TFVN of TC frames is always 0.">
              <type xsi:type="frtypes:IntegerType">
                <singleValueConstraint>
                  <values>0</values>
                </singleValueConstraint>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="scid" optional="false">
              <type xsi:type="frtypes:IntegerType">
                <rangeConstraint min="0" max="1023"/>
              </type>
            </elements>
            <elements xsi:type="frtypes:Element" name="vcid" optional="false">
              <type xsi:type="frtypes:Choice">
                <elements xsi:type="frtypes:Element" name="masterChannel" tag="0" optional="false" comment="When the Master Channel is selected, the VCID remains unspecified.">
                  <type xsi:type="frtypes:Null"/>
                </elements>
                <elements xsi:type="frtypes:Element" name="vcid" tag="1" optional="false">
                  <type xsi:type="frtypes:IntegerType">
                    <rangeConstraint min="0" max="63"/>
                  </type>
                </elements>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the update modes that the given ROCF service instance permits. The update modes are:&#xA;- 'continuous' - the given ROCF service permits the 'continuous' mode, i.e. the OCF service provider delivers each OCF that fulfills the delivery criteria specified by the ROCF-START invocation parameters start-time, stop-time, requested-global-VCID, control-word-type, and tc-vcid;&#xA;- 'change-based' - the ROCF service provider delivers those OCFs that fulfill the delivery criteria specified by the ROCF-START invocation parameters start-time, stop-time, requested-global-VCID, control-word-type, and tc-vcid provided the content of the OCF is different than the one of the OCF with the same tc-vcid value previously delivered. " classifier="rocfPermittedUpdateMode" stringIdentifier="return-operational-control-fields-permitted-update-mode" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="13" typeDefinition="RocfPermittedUpdateMode&#x9; ::= SET  (SIZE( 1 .. 2))  OF&#x9; &#x9; &#x9; ENUMERATED&#xA;&#x9; {&#xA;&#x9; &#x9; continuous          &#x9; &#x9; (0)&#xA;&#x9; ,&#x9; onChange            &#x9; &#x9; (1)&#xA;&#x9; }&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>13</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfPermittedUpdateMode">
          <type xsi:type="frtypes:SetOf">
            <sizeConstraint min="1" max="2"/>
            <elements xsi:type="frtypes:Element" name="updateMode" optional="false">
              <type xsi:type="frtypes:Enumerated">
                <values name="continuous"/>
                <values name="onChange" value="1"/>
              </type>
            </elements>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the update mode applied by the ROCF service provider for the delivery of OCFs. It can take on two values:&#xA;- 'continuous' - the ROCF service provider delivers each OCF that fulfills the delivery criteria specified by the ROCF-START invocation parameters start-time, stop-time, requested-global-VCID, control-word-type, and tc-vcid;&#xA;- 'change-based' - the ROCF service provider delivers those OCFs that fulfill the delivery criteria specified by the ROCF-START invocation parameters start-time, stop-time, requested-global-VCID, control-word-type, and tc-vcid provided the content of the OCF is different than the one of the OCF with the same tc-vcid value previously delivered. " classifier="rocfRequestedUpdateMode" stringIdentifier="return-operational-control-fields-requested-update-mode" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="14" typeDefinition="RocfRequestedUpdateMode&#x9; ::= ENUMERATED&#xA;{&#xA;&#x9; continuous          &#x9; &#x9; (0)&#xA;,&#x9; onChange            &#x9; &#x9; (1)&#xA;}&#xA;" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>14</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfRequestedUpdateMode">
          <type xsi:type="frtypes:Enumerated">
            <values name="continuous"/>
            <values name="onChange" value="1"/>
          </type>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the given instance of the ROCF service. It can take on the following values:&#xA;- 'unbound' - All resources required to enable the provision of the ROCF service have been allocated, and all objects required to provide the service have been instantiated; however, no association yet exists between the user and the provider, i.e., the ROCF transfer service provider port is not bound;&#xA;- 'ready'- An association has been established between the user and the provider, and they may interact by means of the service operations. However, sending of Operational Control Fields (OCFs) from the provider to the user (by means of the ROCF-TRANSFER-DATA operation) is not permitted; the user may enable the delivery of OCFs by means of the appropriate service operation (ROCF-START), which, in turn, will cause the provider to transition to the state 'active';&#xA;- 'active' -  This state resembles state ‘ready’, except that now the provider will send OCFs provided frames of the selected characteristics are made available by the ROCF production process; the service continues in this state until the user invokes the ROCF-STOP operation to cause the provider to suspend delivery of OCFs and transition back to state 'ready or the PEER-ABORT invocation to cause the service to transition back to the 'unbound' state." classifier="rocfSiState" stringIdentifier="return-operational-control-fields-service-instance-state" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="15" typeDefinition="RocfSiState         &#x9; ::= SleSiState" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>15</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfSiState">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.5"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the current setting of the time in seconds between successive ROCF-STATUS-REPORT invocations sent by the ROCF service provider. The permissible values are in the range (2 .. 600). If cyclic reporting is off, the value reported is undefined." classifier="rocfReportingCycle" stringIdentifier="return-operational-control-fields-reporting-cycle" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="16" typeDefinition="RocfReportingCycle  &#x9; ::= SleReportingCycle" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>16</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfReportingCycle">
          <type xsi:type="frtypes:TypeReferenceLocal" typeDefinition="//@asnTypeModule/@typeDefinition.13"/>
        </typeDef>
      </parameter>
      <parameter SemanticDefinition="This parameter reports the  total number of OCFs that were delivered to the user since the start of the service instance provision period." classifier="rocfNumberOfOcfsDelivered" stringIdentifier="return-operational-control-field-number-of-ocfs-delivered" version="1" creationDate="2019-09-12T00:00:00.000+0200" authorizingEntity="CSS Area" oidBit="17" typeDefinition="RocfNumberOfOcfsDelivered&#x9; ::= INTEGER  (0 .. 4294967295)" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>35000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>17</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
        <typeDef name="RocfNumberOfOcfsDelivered">
          <type xsi:type="frtypes:IntegerType">
            <rangeConstraint min="0" max="4294967295"/>
          </type>
        </typeDef>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Tracking Data CSTS" oidOffset="36000">
    <functionalResource SemanticDefinition="none" classifier="TdCstsProvider" stringIdentifier="tracking-data-csts-provider" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="36000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>36000</oidBit>
      </oid>
      <parameter SemanticDefinition="mm since 00:00 UTC" classifier="ProductionStatus" stringIdentifier="Production status" version="1" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="Unsigned Integer" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>36000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>36000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Monitored Data CSTS" oidOffset="37000">
    <functionalResource SemanticDefinition="The MonitoredDataCSTSProvider FR accepts as input the monitored data provided by the MonitoredDataCollection FR." classifier="MdCstsProvider" stringIdentifier="monitored-data-csts-provider" version="1" creationDate="2019-03-26T00:00:00.000+0100" authorizingEntity="CSS Area" oidBit="37000" uses="//@functionalResourceSet.36/@functionalResource.1">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>37000</oidBit>
      </oid>
    </functionalResource>
    <functionalResource SemanticDefinition="none" classifier="MdCollection" stringIdentifier="monitored-data-collection" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="37001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>37001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the monitored data collection. It can take on the following values:&#xD;&#xA;- 'configured': equipment has been assigned to support the data collection, but data collection process is not active;&#xD;&#xA;- 'operational': the data collection process is active, i.e., it collects all relevant monitored data;&#xD;&#xA;- 'interrupted': the data collection process is stopped due to a fault;&#xD;&#xA;- 'halted': the data collection process is stopped and production equipment is out of service due to management action." classifier="ProductionStatus" stringIdentifier="production-status" version="1" creationDate="2015-05-26T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 3)&#xD;&#xA;{   configured   (0)&#xD;&#xA;,   operational   (1)&#xD;&#xA;,   interrupted   (2)&#xD;&#xA;,   halted   (3)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>37001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>37001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Service Control CSTS" oidOffset="38000">
    <functionalResource SemanticDefinition="none" classifier="SvcContrCstsProvider" stringIdentifier="service-control-csts-provider" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="38000">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>38000</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the production status of the service production process associated with the given Service Control service instance.  It can take on the following values:&#xD;&#xA;- 'configured': equipment has been assigned to support the service instance, but the production process is not yet capable of acting on configuration data;&#xD;&#xA;- 'operational': the production process has been configured for support and is now capable to act on configuration data;&#xD;&#xA;- 'interrupted': the service production process is stopped due to a fault;&#xD;&#xA;- 'halted': the service production process is stopped and production equipment is out of service due to management action." classifier="ProductionStatus" stringIdentifier="production-status" version="1" creationDate="2015-05-26T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 3)&#xD;&#xA;{   configured   (0)&#xD;&#xA;,   operational   (1)&#xD;&#xA;,   interrupted   (2)&#xD;&#xA;,   halted   (3)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>38000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>38000</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
    <functionalResource SemanticDefinition="none" classifier="SvcContrProd" stringIdentifier="service-control-prod" version="1" creationDate="2019-03-25T23:00:00.000+0100" authorizingEntity="CSS Area" oidBit="38001">
      <oid>
        <oidBit>1</oidBit>
        <oidBit>3</oidBit>
        <oidBit>112</oidBit>
        <oidBit>4</oidBit>
        <oidBit>4</oidBit>
        <oidBit>2</oidBit>
        <oidBit>1</oidBit>
        <oidBit>38001</oidBit>
      </oid>
      <parameter SemanticDefinition="This enumerated parameter reports the status of the service production necessary for acting on configuration parameters. It can take on the following values:&#xD;&#xA;- 'configured': equipment has been assigned to support acting on configuration data, but the process implementing this is not active;&#xD;&#xA;- 'operational': the process interacting with configuration parameters is active;&#xD;&#xA;- 'interrupted': the process for interaction with configuration parameters is stopped due to a fault;&#xD;&#xA;- 'halted': the process for interaction with configuration parameters is stopped and production equipment is out of service due to management action." classifier="ProductionStatus" stringIdentifier="production-status" version="1" creationDate="2015-05-26T00:00:00.000+0200" authorizingEntity="CSTS WG" oidBit="1" typeDefinition="SEQUENCE (SIZE (1)) OF IntUnsigned (0 .. 3)&#xD;&#xA;{   configured   (0)&#xD;&#xA;,   operational   (1)&#xD;&#xA;,   interrupted   (2)&#xD;&#xA;,   halted   (3)&#xD;&#xA;}" engineeringUnit="none" configured="false">
        <oid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>38001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </oid>
        <typeOid>
          <oidBit>1</oidBit>
          <oidBit>3</oidBit>
          <oidBit>112</oidBit>
          <oidBit>4</oidBit>
          <oidBit>4</oidBit>
          <oidBit>2</oidBit>
          <oidBit>1</oidBit>
          <oidBit>38001</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
          <oidBit>1</oidBit>
        </typeOid>
      </parameter>
    </functionalResource>
  </functionalResourceSet>
  <functionalResourceSet name="Delay Tolerant Networking" oidOffset="39000"/>
</fr:FunctionalResourceModel>
