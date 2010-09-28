package com.predic8.plugin.membrane_client.creator;

import com.predic8.plugin.membrane_client.creator.typecreators.AnyURICreator;
import com.predic8.plugin.membrane_client.creator.typecreators.Base64BinaryCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.BooleanCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.ByteCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.DateCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.DateTimeCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.DecimalCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.DoubleCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.DurationCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.ENTITIESCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.ENTITYCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.FloatCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.GDayCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.GMonthCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.GMonthDayCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.GYearCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.GYearMonthCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.HexBinaryCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.IDCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.IDREFCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.IDREFSCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.IntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.LanguageCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.LongCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NCNameCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NMTOKENCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NMTOKENSCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NOTATIONCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NameCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NegativeIntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NonNegativeIntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NonPositiveIntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.NormalizedStringCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.PositiveIntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.QNameCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.ShortCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.StringCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.StringEnumerationCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.TimeCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.TokenCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.TypeCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.UnsignedByteCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.UnsignedIntegerCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.UnsignedLongCreator;
import com.predic8.plugin.membrane_client.creator.typecreators.UnsignedShortCreator;

public class SimpleTypeCreatorFactory {

public static final String SIMPLE_TYPE_STRING = "string";
	
	public static final String SIMPLE_TYPE_NORMALIZED_STRING = "normalizedString";
	
	public static final String SIMPLE_TYPE_TOKEN = "token";
	
	public static final String SIMPLE_TYPE_BASE_64_BINARY = "base64Binary";
	
	public static final String SIMPLE_TYPE_HEX_BINARY = "hexBinary";
	
	public static final String SIMPLE_TYPE_INTEGER = "integer";
	
	public static final String SIMPLE_TYPE_POSITIVE_INTEGER = "positiveInteger";
	
	public static final String SIMPLE_TYPE_NEGATIVE_INTEGER = "negativeInteger";
	
	public static final String SIMPLE_TYPE_NON_NEGATIVE_INTEGER = "nonNegativeInteger";
	
	public static final String SIMPLE_TYPE_NON_POSITIVE_INTEGER = "nonPositiveInteger";
	
	public static final String SIMPLE_TYPE_LONG = "long";
	
	public static final String SIMPLE_TYPE_UNSIGNED_LONG = "unsignedLong";
	
	public static final String SIMPLE_TYPE_INT = "int";
	
	public static final String SIMPLE_TYPE_UNSIGNED_INTEGER = "unsignedInteger";
	
	public static final String SIMPLE_TYPE_UNSIGNED_INT = "unsignedInt";
	
	public static final String SIMPLE_TYPE_SHORT = "short";
	
	public static final String SIMPLE_TYPE_UNSIGNED_SHORT = "unsignedShort";
	
	public static final String SIMPLE_TYPE_BYTE = "byte";
	
	public static final String SIMPLE_TYPE_UNSIGNED_BYTE = "unsignedByte";
	
	public static final String SIMPLE_TYPE_DECIMAL = "decimal";
	
	public static final String SIMPLE_TYPE_FLOAT = "float";
	
	public static final String SIMPLE_TYPE_DOUBLE = "double";
	
	public static final String SIMPLE_TYPE_BOOLEAN = "boolean";
	
	public static final String SIMPLE_TYPE_DURATION = "duration";
	
	public static final String SIMPLE_TYPE_DATE_TIME = "dateTime";
	
	public static final String SIMPLE_TYPE_DATE = "date";
	
	public static final String SIMPLE_TYPE_TIME = "time";
	
	public static final String SIMPLE_TYPE_G_YEAR = "gYear";
	
	public static final String SIMPLE_TYPE_G_YEAR_MONTH = "gYearMonth";
	
	public static final String SIMPLE_TYPE_G_MONTH = "gMonth";
	
	public static final String SIMPLE_TYPE_G_MONTH_DAY = "gMonthDay";
	
	public static final String SIMPLE_TYPE_G_DAY = "gDay";
	
	public static final String SIMPLE_TYPE_NAME = "Name";
	
	public static final String SIMPLE_TYPE_QNAME = "QName";
	
	public static final String SIMPLE_TYPE_NCNAME = "NCName";
	
	public static final String SIMPLE_TYPE_ANY_URI = "anyURI";
	
	public static final String SIMPLE_TYPE_LANGUAGE = "language";
	
	public static final String SIMPLE_TYPE_ID = "ID";
	
	public static final String SIMPLE_TYPE_IDREF = "IDREF";
	
	public static final String SIMPLE_TYPE_IDREFS = "IDREFS";
	
	public static final String SIMPLE_TYPE_ENTITY = "ENTITY";
	
	public static final String SIMPLE_TYPE_ENTITIES = "ENTITIES";
	
	public static final String SIMPLE_TYPE_NOTATION = "NOTATION";
	
	public static final String SIMPLE_TYPE_NMTOKEN = "NMTOKEN";
	
	public static final String SIMPLE_TYPE_NMTOKENS = "NMTOKENS";
	
	public static final String COMPLEX_TYPE_ENUMERATION = "enumeration";
	
	
	public static TypeCreator getCreator(CompositeCreatorContext ctx) {
		
		if (SIMPLE_TYPE_STRING.equals(ctx.getTypeName()))
			return new StringCreator();

		if (SIMPLE_TYPE_NORMALIZED_STRING.equals(ctx.getTypeName()))
			return new NormalizedStringCreator();
		
		if (SIMPLE_TYPE_TOKEN.equals(ctx.getTypeName()))
			return new TokenCreator();
		
		if (SIMPLE_TYPE_BASE_64_BINARY.equals(ctx.getTypeName()))
			return new Base64BinaryCreator();
		
		if (SIMPLE_TYPE_HEX_BINARY.equals(ctx.getTypeName()))
			return new HexBinaryCreator();

		if (SIMPLE_TYPE_INTEGER.equals(ctx.getTypeName()))
			return new IntegerCreator();
		
		if (SIMPLE_TYPE_POSITIVE_INTEGER.equals(ctx.getTypeName()))
			return new PositiveIntegerCreator();
		
		if (SIMPLE_TYPE_NEGATIVE_INTEGER.equals(ctx.getTypeName()))
			return new NegativeIntegerCreator();
		
		if (SIMPLE_TYPE_NON_NEGATIVE_INTEGER.equals(ctx.getTypeName()))
			return new NonNegativeIntegerCreator();
		
		if (SIMPLE_TYPE_NON_POSITIVE_INTEGER.equals(ctx.getTypeName()))
			return new NonPositiveIntegerCreator();

		if (SIMPLE_TYPE_LONG.equals(ctx.getTypeName()))
			return new LongCreator();
		
		if (SIMPLE_TYPE_UNSIGNED_LONG.equals(ctx.getTypeName()))
			return new UnsignedLongCreator();
		
		if (SIMPLE_TYPE_INT.equals(ctx.getTypeName()))
			return new IntegerCreator();
		
		if (SIMPLE_TYPE_UNSIGNED_INT.equals(ctx.getTypeName()))
			return new UnsignedIntegerCreator();
		
		if (SIMPLE_TYPE_UNSIGNED_INTEGER.equals(ctx.getTypeName()))
			return new UnsignedIntegerCreator();
		
		if (SIMPLE_TYPE_SHORT.equals(ctx.getTypeName()))
			return new ShortCreator();
		
		if (SIMPLE_TYPE_UNSIGNED_SHORT.equals(ctx.getTypeName()))
			return new UnsignedShortCreator();
		
		if (SIMPLE_TYPE_BYTE.equals(ctx.getTypeName()))
			return new ByteCreator();
		
		if (SIMPLE_TYPE_UNSIGNED_BYTE.equals(ctx.getTypeName()))
			return new UnsignedByteCreator();
		
		if (SIMPLE_TYPE_DECIMAL.equals(ctx.getTypeName()))
			return new DecimalCreator();
		
		if (SIMPLE_TYPE_FLOAT.equals(ctx.getTypeName()))
			return new FloatCreator();

		if (SIMPLE_TYPE_DOUBLE.equals(ctx.getTypeName()))
			return new DoubleCreator();
		
		if (SIMPLE_TYPE_BOOLEAN.equals(ctx.getTypeName()))
			return new BooleanCreator();
		
		if (SIMPLE_TYPE_DURATION.equals(ctx.getTypeName()))
			return new DurationCreator();
		
		if (SIMPLE_TYPE_DATE_TIME.equals(ctx.getTypeName()))
			return new DateTimeCreator();

		if (SIMPLE_TYPE_DATE.equals(ctx.getTypeName()))
			return new DateCreator();

		if (SIMPLE_TYPE_TIME.equals(ctx.getTypeName()))
			return new TimeCreator();

		if (SIMPLE_TYPE_G_YEAR.equals(ctx.getTypeName()))
			return new GYearCreator();
		
		if (SIMPLE_TYPE_G_YEAR_MONTH.equals(ctx.getTypeName()))
			return new GYearMonthCreator();

		if (SIMPLE_TYPE_G_MONTH.equals(ctx.getTypeName()))
			return new GMonthCreator();
		
		if (SIMPLE_TYPE_G_MONTH_DAY.equals(ctx.getTypeName()))
			return new GMonthDayCreator();

		if (SIMPLE_TYPE_G_DAY.equals(ctx.getTypeName()))
			return new GDayCreator();

		if (SIMPLE_TYPE_NAME.equals(ctx.getTypeName()))
			return new NameCreator();
		
		if (SIMPLE_TYPE_QNAME.equals(ctx.getTypeName()))
			return new QNameCreator();
		
		if (SIMPLE_TYPE_NCNAME.equals(ctx.getTypeName()))
			return new NCNameCreator();
		
		if (SIMPLE_TYPE_ANY_URI.equals(ctx.getTypeName()))
			return new AnyURICreator();
		
		if (SIMPLE_TYPE_LANGUAGE.equals(ctx.getTypeName()))
			return new LanguageCreator();
		
		if (SIMPLE_TYPE_ID.equals(ctx.getTypeName()))
			return new IDCreator();
		
		if (SIMPLE_TYPE_IDREF.equals(ctx.getTypeName()))
			return new IDREFCreator();
		
		if (SIMPLE_TYPE_IDREFS.equals(ctx.getTypeName()))
			return new IDREFSCreator();
		
		if (SIMPLE_TYPE_ENTITY.equals(ctx.getTypeName()))
			return new ENTITYCreator();
		
		if (SIMPLE_TYPE_ENTITIES.equals(ctx.getTypeName()))
			return new ENTITIESCreator();
		
		if (SIMPLE_TYPE_NOTATION.equals(ctx.getTypeName()))
			return new NOTATIONCreator();
		
		if (SIMPLE_TYPE_NMTOKEN.equals(ctx.getTypeName()))
			return new NMTOKENCreator();
		
		if (SIMPLE_TYPE_NMTOKENS.equals(ctx.getTypeName()))
			return new NMTOKENSCreator();
		
		if (COMPLEX_TYPE_ENUMERATION.equals(ctx.getTypeName()))
			return new StringEnumerationCreator();

		throw new RuntimeException("type is not supported yet: " + ctx.getTypeName());
	}

}
