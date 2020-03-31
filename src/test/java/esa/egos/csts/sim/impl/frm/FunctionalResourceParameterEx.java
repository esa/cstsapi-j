package esa.egos.csts.sim.impl.frm;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerBoolean;
import com.beanit.jasn1.ber.types.BerInteger;
import com.beanit.jasn1.ber.types.BerObjectIdentifier;
import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerReal;
import com.beanit.jasn1.ber.types.BerType;
import com.beanit.jasn1.ber.types.string.BerVisibleString;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameter;
import esa.egos.csts.api.parameters.impl.ParameterValue;
import esa.egos.csts.api.parameters.impl.QualifiedParameter;
import esa.egos.csts.api.parameters.impl.QualifiedValues;
import esa.egos.csts.sim.impl.Utils;
import esa.egos.csts.sim.impl.frm.values.ICstsComplexValue;
import esa.egos.csts.sim.impl.frm.values.ICstsSimpleValue;
import esa.egos.csts.sim.impl.frm.values.ICstsValue;
import esa.egos.csts.sim.impl.frm.values.ICstsValueFactory;

public class FunctionalResourceParameterEx<T extends BerType> extends FunctionalResourceParameter
{

    private static final int INIT_STREAM_BUFF_SIZE = 12;

    private static final BigInteger INIT_INT_VALUE = BigInteger.valueOf(0);

    private static final boolean INIT_BOOL_VALUE = false;

    private static final byte[] INIT_STR_VALUE = "".getBytes();

    private static final byte[] INIT_OCT_STR_VALUE = new byte[0];

    private static final double INIT_REAL_VALUE = 0.0;

    private static final int[] INIT_OID_VALUE = new int[0];

    /** The generated BER class */
    private final Class<T> berClass;

    /** The instance of the BER class */
    private T berObject;

    /** The parameter qualifier */
    private ParameterQualifier qualifier;

    /** The BER name */
    private String berName;

    /** The CSTS value factory */
    private ICstsValueFactory cstsValueFactory;

    public FunctionalResourceParameterEx(ObjectIdentifier identifier,
                                         FunctionalResourceName functionalResourceName,
                                         Class<T> berClass,
                                         ICstsValueFactory cstsValueFactory) throws InstantiationException, IllegalAccessException
    {
        super(identifier, functionalResourceName);
        this.berClass = berClass;
        this.berObject = this.berClass.newInstance();
        this.qualifier = ParameterQualifier.UNDEFINED;
        this.berName = Utils.firstToLowerCase(this.berClass.getSimpleName());
        this.cstsValueFactory = cstsValueFactory;
        initValue();
    }

    /**
     * Set the value of a {@QualifiedParameter} instance to the FR parameter
     * 
     * @param qualifiedParameter The qualified parameter
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     */
    public synchronized void setValue(QualifiedParameter qualifiedParameter) throws InstantiationException,
                                                                             IllegalAccessException,
                                                                             IOException
    {
        EmbeddedData embeddedData = getEmbededData(qualifiedParameter);

        ByteArrayInputStream is = new ByteArrayInputStream(embeddedData.getData());
        this.berObject = this.berClass.newInstance();
        this.berObject.decode(is);
        this.qualifier = qualifiedParameter.getQualifiedValues().get(0).getQualifier();
    }

    @Override
    public synchronized QualifiedParameter toQualifiedParameter()
    {
        QualifiedParameter qualifiedParameter = new QualifiedParameter(getName());

        try
        {
            ReverseByteArrayOutputStream os = new ReverseByteArrayOutputStream(INIT_STREAM_BUFF_SIZE, true);
            this.berObject.encode(os);
            byte[] data = os.getArray();
            EmbeddedData embeddedData = EmbeddedData.of(getOid(), data);
            ParameterValue parameterValue = new ParameterValue(embeddedData);
            QualifiedValues qualifiedValues = new QualifiedValues(this.qualifier);
            qualifiedValues.getParameterValues().add(parameterValue);
            qualifiedParameter.getQualifiedValues().add(qualifiedValues);
        }
        catch (Exception e)
        {
            e.printStackTrace(); // TODO use logger w/ an error message
        }

        return qualifiedParameter;
    }

    /**
     * Set the value of a {@CstsValue} instance to the FR parameter
     * 
     * @param value The value
     * @throws NoSuchFieldException In case the BER class field does not exist
     * @throws IllegalArgumentException In case the provided value is wrong
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public synchronized void setCstsValue(ICstsValue value) throws NoSuchFieldException,
                                                           IllegalArgumentException,
                                                           IllegalAccessException,
                                                           InstantiationException
    {
        if (value instanceof ICstsSimpleValue<?>)
        {
            setSimpleValue((ICstsSimpleValue<?>) value, this.berObject, this.berClass);
        }
        else if (value instanceof ICstsComplexValue)
        {
            if (!this.berName.equals(value.getName()))
            {
                throw new IllegalArgumentException("value name " + value.getName() + ", but expected " + this.berName);
            }
            setComplexValue((ICstsComplexValue) value, this.berObject, this.berClass);
        }
        else
        {
            throw new UnsupportedOperationException("Value " + value + " is not supported by parameter " + getName());
        }
        this.qualifier = value.getQuality();
    }

    public synchronized ICstsValue getCstsValue() throws IllegalArgumentException, IllegalAccessException
    {
        ICstsValue ret;

        String name = Utils.firstToLowerCase(this.berObject.getClass().getSimpleName());
        Optional<Field> optionalField = getValueField(this.berObject.getClass());
        if (optionalField.isPresent())
        {
            // a parameter w/ simple value has the value field directly in its BER class
            ret = getSimpleValue(name, this.berObject, optionalField.get());
        }
        else
        {
            // a parameter w/ complex value
            ret = getComplexValue(name, this.berObject);
        }

        return ret;
    }

    private Optional<Field> getValueField(Class<?> berClass)
    {
        return Stream.of(berClass.getFields()).filter(f -> f.getName().equals("value")).findAny();
    }

    private ICstsValue getSimpleValue(String name, BerType berObject, Field valueField) throws IllegalArgumentException, IllegalAccessException
    {
        ICstsValue ret = this.cstsValueFactory.createEmptyValue();

        Object objValue = valueField.get(berObject);
        if (objValue != null && name == null)
        {
            name = valueField.getName();
        }
        if (berObject instanceof BerInteger)
        {
            if (objValue instanceof BigInteger)
            {
                ret = this.cstsValueFactory.createCstsIntValue(name, this.qualifier, (BigInteger) objValue);
            }
        }
        else if(berObject instanceof BerBoolean)
        {
            if (objValue instanceof Boolean)
            {
                ret = this.cstsValueFactory.createCstsBoolValue(name, this.qualifier, (boolean) objValue);
            }
        }
        else if(berObject instanceof BerVisibleString)
        {
            if (objValue instanceof byte[])
            {
                ret = this.cstsValueFactory.createCstsStringValue(name, this.qualifier, (byte[]) objValue);
            }
        }
        else if(berObject instanceof BerOctetString)
        {
            if (objValue instanceof byte[])
            {
                ret = this.cstsValueFactory.createCstsOctetStringValue(name, this.qualifier, (byte[]) objValue);
            }
        }
        else if(berObject instanceof BerReal)
        {
            if (objValue instanceof Double)
            {
                ret = this.cstsValueFactory.createCstsRealValue(name, this.qualifier, (double) objValue);
            }
        }
        else if(berObject instanceof BerObjectIdentifier)
        {
            if (objValue instanceof int[])
            {
                ret = this.cstsValueFactory.createCstsOidValue(name, this.qualifier, (int[]) objValue);
            }
        }

        return ret;
    }

    private ICstsValue getComplexValue(String name, BerType berObject) throws IllegalArgumentException,
                                                                       IllegalAccessException
    {
        ICstsValue ret = this.cstsValueFactory.createEmptyValue();

        boolean isChoice = isChoice(berObject.getClass());

        List<ICstsValue> values = new ArrayList<ICstsValue>();

        for (Field valueField : berObject.getClass().getDeclaredFields())
        {
            valueField.setAccessible(true);
            Object object = valueField.get(berObject);
            if (object instanceof BerType)
            {
                BerType subBerObject = (BerType) object;
                Optional<Field> optValueField = getValueField(subBerObject.getClass());
                if (optValueField.isPresent())
                {
                    // a simple value
                    ICstsValue value = getSimpleValue(valueField.getName(), subBerObject, optValueField.get());
                    values.add(value);
                }
                else
                {
                    // a complex value
                    ICstsValue value = getComplexValue(valueField.getName(), subBerObject);
                    values.add(value);
                }

                if (isChoice)
                {
                    // there is only one element in a CHOICE
                    break;
                }
            }
        }

        ret = this.cstsValueFactory.createCstsComplexValue(name, values.toArray(new ICstsValue[values.size()]));

        return ret;
    }

    /**
     * Get the field with the given name from the BER class NOTE: also makes the
     * field accessible
     * 
     * @param berClass The BER class
     * @param name The field name
     * @return The class Field
     * @throws NoSuchFieldException In case the field does not exist
     */
    private Field getValueField(Class<?> berClass, String name) throws NoSuchFieldException
    {
        Field ret = null;

        try
        {
//            System.out.println("Class: " + berClass.getSimpleName());
//            System.out.println("Fields:");
//            Stream.of(berClass.getFields()).forEach(f -> System.out.println(f.getName()));
//            System.out.println("Declared fields:");
//            Stream.of(berClass.getDeclaredFields()).forEach(f -> System.out.println(f.getName()));
            if (Stream.of(berClass.getFields()).filter(f -> f.getName().equals(name)).findAny().isPresent())
            {
                ret = berClass.getField(name);
            }
            else
            {
                ret = berClass.getDeclaredField(name);
            }
        }
        catch (NoSuchFieldException e)
        {
            throw new NoSuchFieldException("Parameter " + getName() + " has not value filed /w name " + name);
        }

        try
        {
            ret.setAccessible(true);
        }
        catch (SecurityException e)
        {
            throw new SecurityException("Parameter " + getName() + " value filed " + name
                                        + " cannot be set as accesible");
        }

        return ret;
    }

    /**
     * Set the value of a {@CstsSimpleValue} instance to the provided BER class
     * instance
     * 
     * @param value The value
     * @param berObject The BER object
     * @param berClass The BER class of the BER object
     * @throws NoSuchFieldException In case the BER class field does not exist
     * @throws IllegalArgumentException In case the provided value is wrong
     * @throws IllegalAccessException
     */
    private void setSimpleValue(ICstsSimpleValue<?> value,
                                BerType berObject,
                                Class<?> berClass) throws NoSuchFieldException,
                                                                   IllegalArgumentException,
                                                                   IllegalAccessException
    {
        Field valueField = getValueField(berClass, "value");

        try
        {
            // pure simple value
            System.out.println("Setting " + value + " to field " + valueField.getName() + " w/ type " + valueField.getType());
            valueField.set(berObject, value.getValue());
        }
        catch (IllegalArgumentException e)
        {
            throw new IllegalArgumentException("Cannot set " + value.getValue().getClass().getName()
                                               + " to value filed " + valueField.getType().getName() + "in parameter "
                                               + getName(), e);
        }
    }

    /**
     * Set the value of a {@CstsComplexValue} instance to the provided BER class
     * 
     * @param value The value
     * @param berObject The BER object
     * @param berClass The BER class of the BER object
     * @throws NoSuchFieldException In case the BER class field does not exist
     * @throws IllegalAccessException
     * @throws InstantiationException 
     */
    private void setComplexValue(ICstsComplexValue value,
                                 BerType berObject,
                                 Class<?> berClass) throws NoSuchFieldException,
                                                                    IllegalArgumentException,
                                                                    IllegalAccessException, InstantiationException
    {
        List<String> sequence = null;
        if (isChoice(berClass))
        {
            if (value.getValues().size() > 1)
            {
                sequence = Stream.of(this.berClass.getFields()).map(f -> f.getName()).collect(Collectors.toList());
                throw new IllegalArgumentException("Parameter " + getName() + " value is a CHOICE, but " + value
                                                   + " contains more than one element. Provide only one of "
                                                   + String.join(", ", sequence));
            }

//            System.out.println("CHOICE in " + berClass.getSimpleName());
        }
        else
        {
            // exclude common BER fields serialVersionUID, tag and code
            sequence = Stream.of(berClass.getDeclaredFields())
                    .filter(f -> !f.getName().equals("tag") && !f.getName().equals("serialVersionUID")
                                 && !f.getName().equals("code"))
                    .map(f -> f.getName()).collect(Collectors.toList());

            if (value.getValues().size() != sequence.size())
            {
                throw new IllegalArgumentException("Parameter " + getName() + " value is a SEQUENCE, but " + value
                                                   + " does not contain all elements of the SEQUENCE: "
                                                   + String.join(", ", sequence));
            }
        }

        for (ICstsValue subValue : value.getValues())
        {
            Field valueField = getValueField(berClass, subValue.getName());

            Class<?> clazz = valueField.getType();
            if (!(BerType.class.isAssignableFrom(clazz)))
            {
                throw new IllegalArgumentException("Parameter " + getName() + " value filed " + value
                                                   + " is not an instance of the BerType class but it is an instance of "
                                                   + clazz.getTypeName());
            }

            BerType subBerObject = BerType.class.cast(clazz.newInstance());

            if (subValue instanceof ICstsSimpleValue<?>)
            {
                setSimpleValue((ICstsSimpleValue<?>) subValue, subBerObject, clazz);
            }
            else if (subValue instanceof ICstsComplexValue)
            {
                setComplexValue((ICstsComplexValue) subValue, subBerObject, clazz);
            }
            else
            {
                throw new UnsupportedOperationException("Value " + value + " is not supported by parameter "
                                                        + getName());
            }

            // inject an intermediate BER object
            valueField.set(berObject, subBerObject);
        }
    }

    /**
     * ASN.1 CHOICE does not contain the BER tag
     * 
     * @param berClass The BER class
     * 
     * @return The flag indicating whether the BER class is ASN.1 CHOICE and not ASN.1 SEQUENCE
     */
    private static boolean isChoice(Class<?> berClass)
    {
        // ASN.1 CHOICE has not BER Tag
        return !Stream.of(berClass.getFields()).filter(f -> f.getName().equals("tag")).findAny().isPresent();
    }

    /**
     * Get {@EmbeddedData} from {@QualifiedParameter}
     * 
     * @param qualifiedParameter The qualified parameter
     * @return EmbeddedData
     */
    private EmbeddedData getEmbededData(QualifiedParameter qualifiedParameter)
    {
        if (!qualifiedParameter.getName().equals(getName()))
        {
            throw new IllegalArgumentException("a different parameter name in " + qualifiedParameter + ", expected is "
                                               + getName());
        }

        if (qualifiedParameter.getQualifiedValues() == null)
        {
            throw new IllegalArgumentException("missing qualified values in " + qualifiedParameter);
        }

        if (qualifiedParameter.getQualifiedValues().size() != 1)
        {
            throw new IllegalArgumentException("qualified values size must be 1, but size is "
                                               + qualifiedParameter.getQualifiedValues().size() + " in "
                                               + qualifiedParameter);
        }

        if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues() == null)
        {
            throw new IllegalArgumentException("missing parameter values in " + qualifiedParameter);
        }

        if (qualifiedParameter.getQualifiedValues().get(0).getParameterValues().size() != 1)
        {
            throw new IllegalArgumentException("parameter values size must be 1, but size is " + qualifiedParameter
                    .getQualifiedValues().get(0).getParameterValues().size() + " in " + qualifiedParameter);
        }

        ParameterValue parameterValue = qualifiedParameter.getQualifiedValues().get(0).getParameterValues().get(0);
        if (parameterValue == null)
        {
            throw new IllegalArgumentException("missing parameter value in qualified parameter "
                                               + qualifiedParameter.getName());
        }

        if (parameterValue.getType() != ParameterType.EXTENDED)
        {
            throw new IllegalArgumentException("parameter value type is not " + ParameterType.EXTENDED.name() + " in "
                                               + qualifiedParameter);
        }

        EmbeddedData ret = parameterValue.getExtended();
        if (!ret.getOid().equals(getName().getOid()))
        {
            throw new IllegalArgumentException(ret + " has " + ret.getOid() + ", but expected is "
                                               + getName().getOid());
        }

        return ret;
    }

    /**
     * Initialize the encapsulated BER object so parameter can be successfully
     * encoded even if no value has been set yet
     * 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public synchronized void initValue() throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        Optional<Field> optionalField = getValueField(this.berObject.getClass());
        if (optionalField.isPresent())
        {
            initSimpleValue(this.berObject, optionalField.get());
        }
        else
        {
            // a parameter w/ complex value
            initComplexValue(this.berObject);
        }
    }

    /**
     * Initialize a simple value field
     * 
     * @param berObject The BER object whose field shall be initialized
     * @param valueField The BER object field 
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void initSimpleValue(BerType berObject, Field valueField) throws IllegalArgumentException, IllegalAccessException
    {
        //System.out.println("init " + valueField.getName());
        if (berObject instanceof BerInteger)
        {
            valueField.set(berObject, INIT_INT_VALUE);
        }
        else if (berObject instanceof BerBoolean)
        {
            valueField.set(berObject, INIT_BOOL_VALUE);
        }
        else if (berObject instanceof BerVisibleString)
        {
            valueField.set(berObject, INIT_STR_VALUE);
        }
        else if (berObject instanceof BerOctetString)
        {
            valueField.set(berObject, INIT_OCT_STR_VALUE);
        }
        else if (berObject instanceof BerReal)
        {
            valueField.set(berObject, INIT_REAL_VALUE);
        }
        else if (berObject instanceof BerObjectIdentifier)
        {
            valueField.set(berObject, INIT_OID_VALUE);
        }
    }

    /**
     * Initialize a complex value field
     * 
     * @param berObject The BER object whose fields shall be initialized
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void initComplexValue(BerType berObject) throws IllegalArgumentException, IllegalAccessException, InstantiationException
    {
        boolean isChoice = isChoice(berObject.getClass());

        for (Field valueField : berObject.getClass().getDeclaredFields())
        {
            valueField.setAccessible(true);
            if (BerType.class.isAssignableFrom(valueField.getType()))
            {
//                System.out.println("init " + valueField.getName());
                BerType subBerObject = (BerType) valueField.getType().newInstance();
                Optional<Field> optValueField = getValueField(subBerObject.getClass());
                if (optValueField.isPresent())
                {
                    // a simple value
                    initSimpleValue(subBerObject, optValueField.get());
                }
                else
                {
                    // a complex value
                    initComplexValue(subBerObject);
                }

                // inject an intermediate BER object
                valueField.set(berObject, subBerObject);

                if (isChoice)
                {
                    // there is only one element in a CHOICE
                    break;
                }
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FunctionalResourceParameterEx [");
        sb.append(super.toString());
        sb.append(",\nberObject=");
        sb.append(this.berObject);
        sb.append("]");
        return sb.toString();
    }
}
