/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.web.ejb.termsofuse.TermsOfUse;
import com.topcoder.web.ejb.termsofuse.TermsOfUseBean;
import com.topcoder.web.ejb.termsofuse.TermsOfUseEntity;

/**
 * <p>An implementation of {@link TermsOfUse} interface which provides the library-call style for API of <code>Terms Of
 * Use EJB</code>.</p>
 * 
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author isv
 * @version 1.0 (TopCoder Online Review Switch To Local Calls)
 */
public class TermsOfUseLibrary extends BaseEJBLibrary implements TermsOfUse {

    /**
     * <p>A <code>TermsOfUseBean</code> which is delegated the processing of the calls to methods of this class.</p>
     */
    private TermsOfUseBean bean;

    /**
     * <p>Constructs new <code>TermsOfUseLibrary</code> instance.</p>
     */
    public TermsOfUseLibrary() {
        this.bean = new TermsOfUseBean();
    }

    /**
     * <p>Creates/updates terms of use entity.</p>
     *
     * <p>If the object contains an id, the method will attempt an update, otherwise it will calculate an id and attempt
     * an insertion.</p>
     *
     * @param terms a <code>TermsOfUseEntity</code> containing required information for creation/update.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return a <code>TermsOfUseEntity</code> with updated id attribute.
     */
    public TermsOfUseEntity updateTermsOfUse(TermsOfUseEntity terms, String dataSource) {
        return this.bean.updateTermsOfUse(terms, dataSource);
    }

    /**
     * <p>Retrieves a terms of use entity from the database.</p>
     *
     * @param termsOfUseId a <code>long</code> containing the terms of use id to retrieve.
     * @param dataSource a <code>String</code> containing the datasource.
     * @return a <code>TermsOfUseEntity</code> with the requested terms of use or null if not found.
     */
    public TermsOfUseEntity getEntity(long termsOfUseId, String dataSource) {
        return this.bean.getEntity(termsOfUseId, dataSource);
    }

    /**
     * <p>Gets the ID of type for specified terms of use.</p>
     *
     * @param termsOfUseId a <code>long</code> providing the terms of use id to retrieve.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return a <code>long</code> providing the ID for type of requested terms of use.
     * @deprecated Use {@link #getEntity(long, String)} method instead.
     */
    public long getTermsOfUseTypeId(long termsOfUseId, String dataSource) {
        return this.bean.getTermsOfUseTypeId(termsOfUseId, dataSource);
    }

    /**
     * <p>Sets the ID of type for specified terms of use.</p>
     *
     * @param termsOfUseId a <code>long</code> providing the terms of use id to retrieve.
     * @param termsOfUseTypeId a <code>long</code> providing the ID for type.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @deprecated Use {@link #updateTermsOfUse(TermsOfUseEntity, String)} method instead.
     */
    public void setTermsOfUseTypeId(long termsOfUseId, long termsOfUseTypeId, String dataSource) {
        this.bean.setTermsOfUseTypeId(termsOfUseId, termsOfUseTypeId, dataSource);
    }

    /**
     * <p>Gets the text for specified terms of use.</p>
     *
     * @param termsOfUseId a <code>long</code> providing the terms of use id to retrieve.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @return a <code>String</code> providing the text of requested terms of use.
     * @deprecated Use {@link #getEntity(long, String)} method instead.
     */
    public String getText(long termsOfUseId, String dataSource) {
        return this.bean.getText(termsOfUseId, dataSource);
    }

    /**
     * <p>Sets the text for specified terms of use.</p>
     *
     * @param termsOfUseId a <code>long</code> providing the terms of use id to retrieve.
     * @param text a a <code>String</code> providing the text of requested terms of use.
     * @param dataSource a <code>String</code> referencing the datasource to be used for establishing connection to
     *        target database.
     * @deprecated Use {@link #updateTermsOfUse(TermsOfUseEntity, String)} method instead.
     */
    public void setText(long termsOfUseId, String text, String dataSource) {
        this.bean.setText(termsOfUseId, text, dataSource);
    }
}
