/**
 * CompressUtilityTestCase.java
 * Tests the creation of a codec, wich is the only method
 * of CompressionUtility not tested by AbstractCodecTestCase
 * @author TCSREVIEWER
 * @version 1.0
 */
package com.topcoder.util.compression.accuracytests;

import com.topcoder.util.compression.Codec;
import com.topcoder.util.compression.CompressionUtility;
import com.topcoder.util.compression.DefaultCodec;
import com.topcoder.util.compression.LZ77Codec;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;


public class CompressionUtilityTestCase extends TestCase {

    public void testCreateCodec() throws Exception {
        Codec c = CompressionUtility.createCodec(DefaultCodec.class.getName());
        assertNotNull(c);
        assertTrue(c instanceof DefaultCodec);

        c = CompressionUtility.createCodec(LZ77Codec.class.getName());
        assertNotNull(c);
        assertTrue(c instanceof LZ77Codec);
    }

    public static Test suite() {
        return new TestSuite(CompressionUtilityTestCase.class);
    }
}









