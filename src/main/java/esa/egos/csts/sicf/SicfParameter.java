package esa.egos.csts.sicf;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;

import com.beanit.jasn1.ber.types.BerBoolean;
import com.beanit.jasn1.ber.types.BerEnum;
import com.beanit.jasn1.ber.types.BerInteger;
import com.beanit.jasn1.ber.types.BerOctetString;
import com.beanit.jasn1.ber.types.BerReal;
import com.beanit.jasn1.ber.types.BerType;

import esa.egos.csts.api.functionalresources.FunctionalResourceName;
import esa.egos.csts.api.functionalresources.FunctionalResourceType;
import esa.egos.csts.api.functionalresources.values.impl.CstsValueFactory;
import esa.egos.csts.api.functionalresources.values.impl.FunctionalResourceValue;
import esa.egos.csts.api.oids.ObjectIdentifier;
import esa.egos.csts.api.parameters.impl.FunctionalResourceParameterEx;
import esa.egos.csts.api.types.Label;
import esa.egos.csts.api.types.Name;

public class SicfParameter {
	private static final String OID_PREFIX = "1.3.112.4.4.2.1.";

	private final String mcsName;
	private final String oidString;
	private final int funcResInstanceNumber;
	private final String description;
	private final String type;

	private String enumReference;

	private final ObjectIdentifier paramOid;
	private final FunctionalResourceName funcResName;
	private final Name name;
	private final Label label;
	private final FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> frParamEx;

	public SicfParameter(String mcsName, String oidString,
			int funcResInstanceNumber, String description, String type) {
		this.mcsName = mcsName;
		this.oidString = oidString;
		this.funcResInstanceNumber = funcResInstanceNumber;
		this.description = description;
		this.type = type;

		this.paramOid = ObjectIdentifier.of(oidString);
		this.funcResName = createFuncResName();
		this.name = Name.of(paramOid, funcResName);
		this.label = Label.of(paramOid, getFuncResType());
		this.frParamEx = createfrParamEx();
	}

	private FunctionalResourceName createFuncResName() {
		FunctionalResourceType funcResType = getFuncResType();
		return FunctionalResourceName.of(funcResType, funcResInstanceNumber);
	}

	private FunctionalResourceType getFuncResType() {
		String funcResTypeNumber = StringUtils.substringBetween(oidString,
				OID_PREFIX, ".");
		FunctionalResourceType funcResType = FunctionalResourceType
				.of(ObjectIdentifier.of(OID_PREFIX + funcResTypeNumber));
		return funcResType;
	}

	@SuppressWarnings("unchecked")
	private FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> createfrParamEx() {
		BerType berType;
		if (type.equalsIgnoreCase("INTEGER"))
			berType = new BerInteger();
		else if (type.equalsIgnoreCase("BOOLEAN"))
			berType = new BerBoolean();
		else if (type.equalsIgnoreCase("DOUBLE"))
			berType = new BerReal();
		else if (type.equalsIgnoreCase("STRING"))
			berType = new BerOctetString();
		else if (type.equalsIgnoreCase("ENUMERATIVE"))
			berType = new BerEnum();
		else
			throw new RuntimeException("Invalid Parameter Type: " + type);

		try {
			return new FunctionalResourceParameterEx(paramOid, funcResName,
					berType.getClass(), FunctionalResourceValue.class,
					CstsValueFactory.getInstance());
		} catch (InstantiationException | IllegalAccessException
				| NoSuchMethodException | SecurityException
				| IllegalArgumentException | InvocationTargetException e) {
		}
		throw new RuntimeException(
				"Couldn't create Functional Resource Parameter.");
	}

	public static String getOidPrefix() {
		return OID_PREFIX;
	}

	public int getFuncResInstanceNumber() {
		return funcResInstanceNumber;
	}

	public String getMcsName() {
		return mcsName;
	}

	public String getOidString() {
		return oidString;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public String getEnumReference() {
		return enumReference;
	}

	public void setEnumReference(String enumReference) {
		this.enumReference = enumReference;
	}

	public ObjectIdentifier getParamOid() {
		return paramOid;
	}

	public FunctionalResourceName getFuncResName() {
		return funcResName;
	}

	public Name getName() {
		return name;
	}

	public Label getLabel() {
		return label;
	}

	public FunctionalResourceParameterEx<?, FunctionalResourceValue<?>> getFrParamEx() {
		return frParamEx;
	}
}
