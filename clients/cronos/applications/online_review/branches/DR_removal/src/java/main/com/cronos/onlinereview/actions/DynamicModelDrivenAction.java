/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.cronos.onlinereview.model.DynamicModel;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.NoParameters;


/**
 * This is the base class for all struts actions classes which are based on the dynamic model.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public abstract class DynamicModelDrivenAction extends BaseServletAwareAction
    implements NoParameters, ModelDriven<DynamicModel> {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -4508729927641721828L;

    /**
      * Represents the dynamic model used by the action class.
     */
    private DynamicModel model;

    /**
      * Getter of model.
      * @return the model
      */
    public DynamicModel getModel() {
        return model;
    }

    /**
      * Setter of model.
      * @param model the model to set
      */
    public void setModel(DynamicModel model) {
        this.model = model;
    }
}
