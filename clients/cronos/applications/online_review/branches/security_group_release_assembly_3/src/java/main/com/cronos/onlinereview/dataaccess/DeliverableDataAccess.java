/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>A simple DAO for deliverables backed up by Query Tool.</p>
 *
 * @author isv
 * @version 1.0
 */
public class DeliverableDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>DeliverableDataAccess</code> instance. This implementation does nothing.</p>
     */
    public DeliverableDataAccess() {
    }

    /**
     * <p>Gets the configuration for deliverables as set up in <code>deliverable_lu</code> database table.</p>
     *
     * @return a <code>Map</code> mapping the resource role IDs to maps mapping the project phase type IDs to IDs of
     *         deliverables.
     */
    public Map<Long, Map<Long, Long>> getDeliverablesList() {
        Map<String,ResultSetContainer> results = runQuery("tcs_deliverables", (String) null, null);

        Map<Long, Map<Long, Long>> deliverables = new HashMap<Long, Map<Long, Long>>();

        ResultSetContainer resourcesData = results.get("tcs_deliverables");
        int recordNum = resourcesData.size();
        for (int i = 0; i < recordNum; i++) {
            long roleId = resourcesData.getLongItem(i, "resource_role_id");
            long phaseTypeId = resourcesData.getLongItem(i, "phase_type_id");
            long deliverableId = resourcesData.getLongItem(i, "deliverable_id");

            if (!deliverables.containsKey(roleId)) {
                deliverables.put(roleId, new HashMap<Long, Long>());
            }
            deliverables.get(roleId).put(phaseTypeId, deliverableId);
        }

        return deliverables;
    }
}
