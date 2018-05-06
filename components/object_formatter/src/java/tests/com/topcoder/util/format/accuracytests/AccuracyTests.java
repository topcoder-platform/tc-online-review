/*
 * AccuracyTests.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.accuracytests;

import com.topcoder.util.format.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import java.util.Stack;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * A suite of tests to verify the accuracy of the object formatter component.
 *
 * @author RachaelLCook
 * @version 1.0
 */

public class AccuracyTests extends TestCase
{
	private static class TestFormatter implements ObjectFormatMethod
	{
		TestFormatter( final Class klass )
		{
			class_ = klass;
		}

		public String format( final Object obj )
		{
			if ( ! class_.isInstance( obj ) )
			{
				throw new IllegalArgumentException();
			}

			return class_.getName() + ": " + obj;
		}

		private final Class class_;
	}

	private static class FunnyObject
	{
		public String toString()
		{
			return "funny";
		}
	}

	private final FunnyObject FUNNY = new FunnyObject();

	private final byte BYTE = 12;

	private final char CHAR = 2;

	private final short SHORT = 3;

	private final int INT = 4;

	private final long LONG = 5;

	private final float FLOAT = 6.5f;

	private final double DOUBLE = 7.125;

	private final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat();

	/**
	 * Exercises the {@link FormatMethodFactory FormatMethodFactory} interface.
	 */
	public void testFormatMethodFactory()
	{
		final Date now = new Date();
		final String format_str = "yyyy.MM.dd G 'at' HH:mm:ss z";
		final SimpleDateFormat sdf = new SimpleDateFormat( format_str );
		final String now_str = sdf.format( now );
		assertEquals( "date format w/ format string must equal equivalent " +
					  "SimpleDateFormat", now_str, 
					  FormatMethodFactory.getDefaultDateFormatMethod
					  ( format_str ).format( now ) );
		assertEquals( "date format w/ null format string should use current " +
					  "format string from supplied SimpleDateFormat",
					  now_str,
					  FormatMethodFactory.getDefaultDateFormatMethod
					  ( null, sdf ).format( now ) );
		assertEquals( "date format w/ format string and SimpleDateFormat " +
					  "should apply format string to SDF", now_str,
					  FormatMethodFactory.getDefaultDateFormatMethod
					  ( format_str, new SimpleDateFormat() ).format( now ) );
		assertEquals( "default object format method should equal obj.toString",
					  FUNNY.toString(),
					  FormatMethodFactory.getDefaultObjectFormatMethod().format
					  ( FUNNY ) );
	}

	/**
	 * Exercises the {@link ObjectFormatter ObjectFormatter} returned by
	 * {@link ObjectFormatterFactory#getEmptyFormatter
	 * ObjectFormatterFactory.getEmptyFormatter}.
	 */
	public void testEmptyObjectFormatter()
	{
		final ObjectFormatter frm = ObjectFormatterFactory.getEmptyFormatter();
		assertEquals( "empty formatter should contain no format methods",
					  null, frm.getFormatMethodForClass( Object.class ) );
		final ObjectFormatMethod ofm = 
			FormatMethodFactory.getDefaultObjectFormatMethod();
		frm.setFormatMethodForClass( Vector.class, ofm, false );
		assertEquals( "getFormatMethodForClass() should only return a " +
					  "formatter for an exact class match",
					  null, frm.getFormatMethodForClass( Stack.class ) );
		assertEquals( "getFormatMethodForClass() should return format method",
					  ofm, frm.getFormatMethodForClass( Vector.class ) );
	}

	/**
	 * Exercises the {@link ObjectFormatter ObjectFormatter} returned by
	 * {@link ObjectFormatterFactory#getPlainFormatter
	 * ObjectFormatterFactory.getPlainFormatter}.
	 */
	public void testPlainObjectFormatter()
	{
		final ObjectFormatter frm = ObjectFormatterFactory.getPlainFormatter();
		assertEquals( "plain object formatter should be equivalent to " +
					  "obj.toString()", 
					  FUNNY.toString(), frm.format( FUNNY ) );
	}

	/**
	 * Exercises the {@link ObjectFormatter ObjectFormatter} returned by
	 * {@link ObjectFormatterFactory#getPrettyFormatter
	 * ObjectFormatterFactory.getPrettyFormatter}.
	 */
	public void testPrettyObjectFormatter()
	{
		final ObjectFormatter frm = 
			ObjectFormatterFactory.getPrettyFormatter();
		final PrimitiveFormatter pfrm = 
			PrimitiveFormatterFactory.getPrettyFormatter();
		assertEquals( "byte formatted incorrectly by pretty object formatter",
					  pfrm.format( BYTE ), frm.format( new Byte( BYTE ) ) );
		assertEquals( "char formatted incorrectly by pretty object formatter",
					  pfrm.format( CHAR ), 
					  frm.format( new Character( CHAR ) ) );
		assertEquals( "short formatted incorrectly by pretty object formatter",
					  pfrm.format( SHORT ), frm.format( new Short( SHORT ) ) );
		assertEquals( "int formatted incorrectly by pretty object formatter",
					  pfrm.format( INT ), frm.format( new Integer( INT ) ) );
		assertEquals( "long formatted incorrectly by pretty object formatter",
					  pfrm.format( LONG ), frm.format( new Long( LONG ) ) );
		assertEquals( "float formatted incorrectly by pretty object formatter",
					  pfrm.format( FLOAT ), frm.format( new Float( FLOAT ) ) );
		assertEquals
			( "double formatted incorrectly by pretty object formatter",
			  pfrm.format( DOUBLE ), frm.format( new Double( DOUBLE ) ) );
		final Date now = new Date();
		final DateFormatMethod dfm = 
			FormatMethodFactory.getDefaultDateFormatMethod( null );
		assertEquals
			( "date formatted incorrectly by pretty object formatter",
			  dfm.format( now ), frm.format( now ) );
		assertEquals( "pretty object formatter should have no default " +
					  "formatter for Object", 
					  null, frm.getFormatMethodForClass( Object.class ) );
	}

	/**
	 * Tests the logic used to determine which formatter (if any) should
	 * handle a particular object when there's no exact class match.
	 */
	public void testFormatterSelection()
	{
		final ObjectFormatter frm = ObjectFormatterFactory.getEmptyFormatter();
		final ObjectFormatMethod object_format = 
			FormatMethodFactory.getDefaultObjectFormatMethod();
		final ObjectFormatMethod vector_format = 
			new TestFormatter( Vector.class );
		frm.setFormatMethodForClass( Vector.class, object_format, true );
		assertEquals
			( "getFormatMethodForClass should only return exact class match", 
			  null, frm.getFormatMethodForClass( Stack.class ) );
		assertEquals
			( "getFormatMethodForClass should return Vector formatter",
			  object_format, frm.getFormatMethodForClass( Vector.class ) );
		frm.unsetFormatMethodForClass( Vector.class );
		assertEquals
			( "formatter should be removed by unsetFormatMethodForClass", 
			  null, frm.getFormatMethodForClass( Vector.class ) );
		frm.setFormatMethodForClass( Vector.class, object_format, true );
		frm.setFormatMethodForClass( Vector.class, vector_format, true );
		assertEquals
			( "later calls to setFormatMethodForClass w/ same class should " +
			  "overwrite results of previous calls", 
			  vector_format, frm.getFormatMethodForClass( Vector.class ) );

		final ObjectFormatMethod collection_format = 
			new TestFormatter( Collection.class );
		final ObjectFormatMethod list_format = new TestFormatter( List.class );
		final ObjectFormatMethod random_access_format = 
			new TestFormatter( RandomAccess.class );
		frm.setFormatMethodForClass
			( Collection.class, collection_format, true );
		frm.setFormatMethodForClass( List.class, list_format, true );
		frm.setFormatMethodForClass
			( RandomAccess.class, random_access_format, true );
		final LinkedList ll = new LinkedList();
		final ArrayList al = new ArrayList();
		assertEquals( "linked list should be formatted with list formatter",
					  list_format.format( ll ), frm.format( ll ) );
		try
		{
			frm.format( al );
			fail( "ArrayList should not be formated due to ambiguous format " +
				  "selection" );
		}
		catch ( final IllegalArgumentException ex )
		{
		}

		frm.setFormatMethodForClass
			( RandomAccess.class, random_access_format, false );
		assertEquals( "ArrayList should be formatted using List formatter",
					  list_format.format( al ), frm.format( al ) );
	}

	/**
	 * Exercises the {@link PrimitiveFormatter PrimitiveFormatter} returned by
	 * {@link PrimitiveFormatterFactory#getPlainFormatter
	 * PrimitiveFormatterFactory.getPlainFormatter}.
	 */
	public void testPlainPrimitiveFormatter()
	{
		final PrimitiveFormatter pfrm = 
			PrimitiveFormatterFactory.getPlainFormatter();
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( BYTE ), pfrm.format( BYTE ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( CHAR ), pfrm.format( CHAR ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( SHORT ), pfrm.format( SHORT ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( INT ), pfrm.format( INT ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( LONG ), pfrm.format( LONG ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( FLOAT ), pfrm.format( FLOAT ) );
		assertEquals( "plain primitive formatter should be equivalent to " +
					  "String.valueOf", 
					  String.valueOf( DOUBLE ), pfrm.format( DOUBLE ) );
	}

	/**
	 * Exercises the {@link PrimitiveFormatter PrimitiveFormatter} returned by
	 * {@link PrimitiveFormatterFactory#getPrettyFormatter
	 * PrimitiveFormatterFactory.getPrettyFormatter}.
	 */
	public void testPrettyPrimitiveFormatter()
	{
		// the specification leaves the exact output of the pretty primitive
		// formatter up to the implementor, so we can't really test it
	}

	/**
	 * Exercises the {@link PrimitiveFormatter PrimitiveFormatter} returned by
	 * {@link PrimitiveFormatterFactory#getFormatter
	 * PrimitiveFormatterFactory.getFormatter}.
	 */
	public void testCustomPrimitiveFormatter()
	{
		internalTestPrimitiveFormatter( "00,000.00", null );
		internalTestPrimitiveFormatter( "00,000.00", new DecimalFormat() );
		internalTestPrimitiveFormatter
			( null, new DecimalFormat( "00,000.00" ) );
	}

	private void internalTestPrimitiveFormatter
		( String format_str, DecimalFormat df )
	{
		DecimalFormat ref_df = df;
		if ( ref_df == null )
		{
			ref_df = new DecimalFormat();
		}
		if ( format_str != null )
		{
			ref_df.applyPattern( format_str );
		}

		final PrimitiveFormatter pfrm = 
			PrimitiveFormatterFactory.getFormatter( format_str, df );
		assertEquals( ref_df.format( BYTE ), pfrm.format( BYTE ) );
		assertEquals( ref_df.format( CHAR ), pfrm.format( CHAR ) );
		assertEquals( ref_df.format( SHORT ), pfrm.format( SHORT ) );
		assertEquals( ref_df.format( INT ), pfrm.format( INT ) );
		assertEquals( ref_df.format( LONG ), pfrm.format( LONG ) );
		assertEquals( ref_df.format( FLOAT ), pfrm.format( FLOAT ) );
		assertEquals( ref_df.format( DOUBLE ), pfrm.format( DOUBLE ) );		
	}

	/**
	 * Returns a test suite containing all accuracy tests.
	 *
	 * @return
	 *    a test suite containing all accuracy tests
	 */
	public static Test suite()
	{
		return new TestSuite( AccuracyTests.class );
	}

	/**
	 * A main method that runs this test suite.
	 *
	 * @param args the command-line arguments
	 */
	public static void main( final String[] args )
	{
		junit.textui.TestRunner.run( suite() );
	}
}
