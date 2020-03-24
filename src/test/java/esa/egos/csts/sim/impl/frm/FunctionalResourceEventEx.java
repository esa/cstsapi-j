package esa.egos.csts.sim.impl.frm;

import java.io.ByteArrayInputStream;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.enumerations.EventValueType;
import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.events.Event;
import esa.egos.csts.api.events.EventValue;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;

public class FunctionalResourceEventEx<T extends BerType> extends Event
{

    private static final int INIT_STREAM_BUFF_SIZE = 12;

    private final Class<T> berValueType;

    private T berValue;


    public FunctionalResourceEventEx(ObjectIdentifier identifier,
                                     FunctionalResourceName functionalResourceName,
                                     Class<T> clazz) throws InstantiationException, IllegalAccessException
    {
        super(identifier, functionalResourceName);
        this.berValueType = clazz;
        this.berValue = this.berValueType.newInstance();
    }

    public Class<T> getBerValueType()
    {
        return this.berValueType;
    }

    public T getBerValue()
    {
        return this.berValue;
    }

    public QualifiedParameter toQualifiedParameter()
    {
        QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());

        try
        {
            ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(INIT_STREAM_BUFF_SIZE, true);
            this.berValue.encode(os);
            byte[] data = os.getArray();
            EmbeddedData embeddedData = EmbeddedData.of(getOid(), data);
            ParameterValue parameterValue = new ParameterValue(embeddedData);

            QualifiedValues qualifiedValues = new QualifiedValues(ParameterQualifier.VALID);
            qualifiedValues.getParameterValues().add(parameterValue);

            qualifiedParameter.getQualifiedValues().add(qualifiedValues);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return qualifiedParameter;
    }

    public void toBer(EventValue eventValue) throws Exception
    {
        ObjectIdentifier oid = getName().getOid();
        if (eventValue.getType() != EventValueType.EXTENDED)
        {
            throw new Exception("Event " + oid + " value type is not " + EventValueType.EXTENDED.name());
        }
        EmbeddedData embeddedData = eventValue.getExtension();
        if (embeddedData.getOid().equals(oid) == true)
        {
            ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData());
            this.berValue = this.berValueType.newInstance();
            this.berValue.decode(is);
        }
        else
        {
            StringBuilder sb = new StringBuilder("Unexpected event: ");
            sb.append(embeddedData.getOid());
            sb.append(" expected event ");
            sb.append(oid);
            sb.append(", /w type ");
            sb.append(this.berValueType.getName());
            System.out.println(sb.toString());
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FunctionalResourceParameterEx [");
        sb.append(this.getName());
        sb.append(", berValueType=");
        sb.append(this.berValueType.getName());
        sb.append("]");
        return sb.toString();
    }
}
