package esa.egos.csts.api.parameters.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.beanit.jasn1.ber.ReverseByteArrayOutputStream;
import com.beanit.jasn1.ber.types.BerBoolean;
import com.beanit.jasn1.ber.types.BerInteger;
import com.beanit.jasn1.ber.types.BerNull;
import com.beanit.jasn1.ber.types.BerObjectIdentifier;
import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerReal;
import com.beanit.jasn1.ber.types.BerType;
import com.beanit.jasn1.ber.types.string.BerVisibleString;

import esa.egos.csts.api.enumerations.ParameterQualifier;
import esa.egos.csts.api.enumerations.ParameterType;
import esa.egos.csts.api.extensions.EmbeddedData;
import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.values.ICstsComplexValue;
import esa.egos.csts.api.functionalresources.values.ICstsSimpleValue;
import esa.egos.csts.api.functionalresources.values.ICstsValue;
import esa.egos.csts.api.functionalresources.values.ICstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.CstsNullValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.util.impl.CSTSUtils;

/**
 * This class implements FR parameter with a value type based on jASN.1 generated class.
 * It allows to set, encode, decode and get its value in the generic way via Java reflection.
 */
public class FunctionalResourceParameterEx<T extends BerType> extends FunctionalResourceParameter
{
    /** The logger */
    private final static Logger LOG = Logger.getLogger(FunctionalResourceParameterEx.class.getName());
 
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

    /**
     * Constructor
     * 
     * @param identifier The parameter object identifier
     * @param functionalResourceName The functional resource name
     * @param berClass The jASN.1 generate class for the parameter value
     * @param cstsValueFactory The external value factory
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public FunctionalResourceParameterEx(ObjectIdentifier identifier,
                                         FunctionalResourceName functionalResourceName,
                                         Class<T> berClass,
                                         ICstsValueFactory cstsValueFactory) throws InstantiationException, IllegalAccessException
    {
        super(identifier, functionalResourceName);
        this.berClass = berClass;
        this.berObject = this.berClass.newInstance();
        this.qualifier = ParameterQualifier.UNDEFINED;
        this.berName = CSTSUtils.firstToLowerCase(this.berClass.getSimpleName());
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
            LOG.log(Level.SEVERE, "Failed to encode " + getName() + " to QualifiedParameter.", e);
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

        setChanged();
        notifyObservers();
        clearChanged();
    }

    public synchronized ICstsValue getCstsValue() throws IllegalArgumentException, IllegalAccessException
    {
        ICstsValue ret;

        String name = CSTSUtils.firstToLowerCase(this.berObject.getClass().getSimpleName());
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
        boolean isChoice = isChoice(berObject.getClass());

        List<ICstsValue> values = new ArrayList<ICstsValue>();

        boolean choiceItemFound = false;
        // iterate through all superclasses
        Class<?> cls = berObject.getClass();
        while (cls != null && !choiceItemFound)
        {
            for (Field valueField : cls.getDeclaredFields())
            {
                valueField.setAccessible(true);
                Object object = valueField.get(berObject);

                // field is List<? extends BerType> seqOf
                if (object instanceof List && valueField.getName().equals("seqOf"))
                {
                    List<?> list = (List<?>) object;
                    // find generic type (must be BerType)
                    ParameterizedType listType = (ParameterizedType)valueField.getGenericType();
                    Class<?> clazz = (Class<?>)listType.getActualTypeArguments()[0];
                    if (BerType.class.isAssignableFrom(clazz))
                    {
                        for (Object listObject : list)
                        {
                            Optional<Field> optValueField = getValueField(listObject.getClass());
                            if (optValueField.isPresent())
                            {
                                // a simple value
                                ICstsValue value = getSimpleValue("seqOf", (BerType)listObject, optValueField.get());
                                values.add(value);
                            }
                            else
                            {
                                // a complex value
                                String listClass = clazz.getName();
                                String className = listClass.substring(listClass.lastIndexOf('$')+1);

                                ICstsValue value = getComplexValue(className, (BerType)listObject);
                                
                                ICstsValue listItem = this.cstsValueFactory.createCstsComplexValue("seqOf", value);
                                
                                values.add(listItem);
                            }
                        }
                    }
                }
                else if (object instanceof BerType)
                {
                    BerType subBerObject = (BerType) object;
                    Optional<Field> optValueField = getValueField(subBerObject.getClass());
                    if (subBerObject instanceof BerNull)
                    {
                        CstsNullValue value = CstsNullValue.of(valueField.getName());
                        values.add(value);
                    }
                    else if (optValueField.isPresent())
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
                        choiceItemFound = true;
                        break;
                    }
                }
            }
            cls = cls.getSuperclass();
        }

        ICstsValue ret = this.cstsValueFactory.createCstsComplexValue(name, values.toArray(new ICstsValue[values.size()]));

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

        Class<?> cls = berClass;
        while (cls != null)
        {
            Class<?> fcls = cls;
            LOG.finest(() -> {
                StringBuilder sb = new StringBuilder(fcls.getName());
                sb.append("\nAvailable fields:\n");
                Stream.of(fcls.getFields()).forEach(f -> 
                {
                    sb.append(f.getName());
                    sb.append('\n');
                });
                sb.append("Available declared fields:\n");
                Stream.of(fcls.getDeclaredFields()).forEach(f -> 
                {
                    sb.append(f.getName());
                    sb.append('\n');
                });
                return sb.toString();
            });

            if (Stream.of(fcls.getFields()).filter(f -> f.getName().equals(name)).findAny().isPresent())
            {
                ret = fcls.getField(name);
            }
            else
            {
                try
                {
                    ret = fcls.getDeclaredField(name);
                    break;
                }
                catch (NoSuchFieldException e)
                {
                }
            }
            cls = fcls.getSuperclass();
        }
        if (ret == null)
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
            LOG.fine(() -> "Setting " + value + " to field " + valueField.getName() + " w/ type "
                           + valueField.getType() + " for parameter " + getName());
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
        boolean isChoice = isChoice(berClass);
        if (isChoice)
        {
            if (value.getValues().size() > 1)
            {
                List<String> sequence = Stream.of(this.berClass.getFields()).map(f -> f.getName()).collect(Collectors.toList());
                throw new IllegalArgumentException("Parameter " + getName() + " value is a CHOICE, but " + value
                                                   + " contains more than one element. Provide only one of "
                                                   + String.join(", ", sequence));
            }

            LOG.fine(() -> "Setting " + value + " to the " + berName + " choice");
        }
        else
        {
            // exclude common BER fields serialVersionUID, tag and code
            Class<?> cls = berClass;
            List<String> sequence = new ArrayList<String>();
            while (cls != null)
            {
                sequence.addAll(Stream.of(cls.getDeclaredFields())
                        .filter(f -> !f.getName().equals("tag")
                                  && !f.getName().equals("serialVersionUID")
                                  && !f.getName().equals("code")
                                  && !f.getType().equals(BerNull.class))
                        .map(f -> f.getName()).collect(Collectors.toList()));
                cls = cls.getSuperclass();
            }
            
            long seqOfValsCnt = value.getValues().stream().filter(v -> v.getName().equals("seqOf")).count();
            int seqOfFrCnt = sequence.contains("seqOf") ? 1 : 0;

            if ((value.getValues().size() - seqOfValsCnt) != (sequence.size() - seqOfFrCnt))
            {
                throw new IllegalArgumentException("Parameter " + getName() + " value is a SEQUENCE, but " + value
                                                   + " does not contain all elements of the SEQUENCE: "
                                                   + String.join(", ", sequence));
            }

            LOG.fine(() -> "Setting " + value + " to the " + berName + " sequence");
        }
        
        // for choice - first set all BerType fields to null to unselect previous value
        if (isChoice)
        {
            for (Field valueField : berClass.getDeclaredFields())
            {
                Class<?> clazz = valueField.getType();
                if (!(BerType.class.isAssignableFrom(clazz)))
                {
                    continue;
                }
                valueField.setAccessible(true);
                valueField.set(berObject, null);
            }
        }
        
        boolean firstToList = true;
        for (ICstsValue subValue : value.getValues())
        {
            Field valueField = getValueField(berClass, subValue.getName());

            Class<?> clazz = valueField.getType();
            
            // process seqOf field
            boolean toList = false;
            Method listGetterMethod = null;
            if (subValue.getName().equals("seqOf"))
            {
                // clazz should be List<BerType>
                if (clazz != List.class)
                {
                    throw new IllegalArgumentException("Parameter " + getName() + ": field 'seqOf' is not List");
                }
                toList = true;
                // get generic type of list
                ParameterizedType listType = (ParameterizedType)valueField.getGenericType();
                clazz = (Class<?>)listType.getActualTypeArguments()[0];
                
                String listGetterName = clazz.getName();
                String methodName = "get";
                if (subValue instanceof ICstsComplexValue)
                {
                    Optional<ICstsValue> res = ((ICstsComplexValue) subValue).getValues().stream()
                                                .filter(v -> (v instanceof ICstsComplexValue)).findFirst();
                    if (!res.isPresent())
                    {
                        throw new IllegalArgumentException("Parameter " + getName() + " doesn't contain ICstsComplexValue");
                    }
                    subValue = res.get();
                    methodName += listGetterName.substring(listGetterName.lastIndexOf('$')+1);
                }
                else
                {
                    methodName += listGetterName.substring(listGetterName.lastIndexOf('.')+1);
                }
                try
                {
                    listGetterMethod = berClass.getMethod(methodName);
                }
                catch (NoSuchMethodException e)
                {
                    throw new IllegalArgumentException("Parameter " + getName() + ": cannot find list getter: " + listGetterName);
                }
            }

            if (!(BerType.class.isAssignableFrom(clazz)))
            {
                throw new IllegalArgumentException("Parameter " + getName() + " value filed " + value
                                                   + " is not an instance of the BerType class but it is an instance of "
                                                   + clazz.getTypeName());
            }

            BerType subBerObject = BerType.class.cast(clazz.newInstance());

            // treat CstsNullValue individually
            if (subValue instanceof CstsNullValue)
            {
                // subBerObject is now instance of BerNull class
            }
            else if (subValue instanceof ICstsSimpleValue<?>)
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

            LOG.finest(() -> "Injecting intermediate " + subBerObject.getClass().getSimpleName() + " to "
                             + berObject.getClass().getSimpleName() + " in parameter " + getName()
                             + " on setComplexValue()");
            // inject an intermediate BER object
            if (toList)
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<BerType> list = (List<BerType>)listGetterMethod.invoke(berObject);
                    if (firstToList)
                    {
                        list.clear();
                        firstToList = false;
                    }
                    list.add(subBerObject);
                }
                catch (Exception e)
                {
                    throw new UnsupportedOperationException("Cannot get list for " + value + " in parameter "
                                                            + getName());
                }
            }
            else
            {
                valueField.set(berObject, subBerObject);
            }
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
        else
        {
            if (valueField != null)
            {
                LOG.warning(() -> "Unsupported BER type " + valueField.getType() + " on " + getName() + " initialization");
            }
            else
            {
                LOG.severe(() -> "The value field is null on " + getName() + " initialization");
            }
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
    private void initComplexValue(BerType berObject) throws IllegalArgumentException,
                                                     IllegalAccessException,
                                                     InstantiationException
    {
        boolean isChoice = isChoice(berObject.getClass());

        for (Field valueField : berObject.getClass().getDeclaredFields())
        {
            valueField.setAccessible(true);
            if (BerType.class.isAssignableFrom(valueField.getType()))
            {
                LOG.finest(() -> "Initialize value filed " + valueField.getType() + " in parameter " + getName());
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
        sb.append(", berClass=");
        sb.append(this.berClass);
        sb.append(", berObject=");
        sb.append(this.berObject);
        sb.append(", qualifier=");
        sb.append(this.qualifier);
        sb.append(", berName=");
        sb.append(this.berName);
        sb.append("]");
        return sb.toString();
    }

    /**
     * Convert FR parameter value to string
     * @return String
     */
    public String valueToString()
    {
        StringBuilder sb = new StringBuilder(this.berName).append("(")
                .append(getName().getFunctionalResourceName().getInstanceNumber()).append(")");
        return sb.append(':').append(this.berObject.toString()).append('\n').toString();
    }
}
