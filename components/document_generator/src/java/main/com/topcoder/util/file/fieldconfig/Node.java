/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

/**
 * <p>
 * This interface is part of the API for entering data for template fields. It
 * designates a node which can be a simple field or a composite loop node. The
 * field node represents data about a template field. The loop node represents
 * data about a loop in the template and can contain other loop and field
 * nodes, following the composite pattern.
 * </p>
 *
 * <p>
 * Thread-safety: Implementations are not required to be thread-safe.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public interface Node {
}
