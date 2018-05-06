package com.topcoder.util.format.failuretests;

/**
 * This object is for testing for testing the failure case where
 * multiple ObjectFormatMethods compete for the same object.
 **/
public class AmbiguousObject extends Object
    implements SampleInterfaceOne, SampleInterfaceTwo
{

}
