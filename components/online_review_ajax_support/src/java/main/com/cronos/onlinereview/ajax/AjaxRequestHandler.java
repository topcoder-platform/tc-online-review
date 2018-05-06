/*
 * Copyright (C) 2006-2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax;

/**
 * <p>
 * Defines the contract an Ajax request handler must implement in order to process Ajax requests.
 * This interface defines only one method which is "service".<br>
 * An Ajax request handler's "service" method will take an AjaxRequest,
 * and a user ID as parameter in order to process the request from
 * that user and produce the corresponding AjaxResponse object.<br><br>
 *
 * A successful servicing operation must return an AjaxResponse object with the "success" status,
 * and optionally the result data.<br><br>
 *
 * Any servicing error must be returned as an AjaxResponse object with an error specific status,
 * and optionally the error message or data.<br><br>
 *
 * Any successful or failed operation is must be logged
 * by the implementation classes using the Logging Wrapper component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * Any Ajax request handler implementation must be thread-safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @version 1.0.1
 */
public interface AjaxRequestHandler {

    /**
     * <p>
     * Service an Ajax request and produce an Ajax response.
     * The userId parameter could be null.
     * </p>
     *
     * @return the response to the request
     * @param request the request to service
     * @param userId the id of user who issued this request
     * @throws IllegalArgumentException if the request is null
     */
    public AjaxResponse service(AjaxRequest request, Long userId);
}


