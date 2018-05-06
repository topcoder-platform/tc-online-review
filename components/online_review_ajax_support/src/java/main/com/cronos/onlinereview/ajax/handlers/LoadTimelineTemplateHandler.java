/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.PhaseTemplate;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * Ajax request handler class which handles the "Load Timeline Template" Ajax operation.
 * this class extends the CommonHandler abstract class, and uses the resource manager defined in its parent class,
 * plus an instance of the PhaseTemplate class in order to implement the "Load Timeline Template" operation.
 *
 * Any successful or failed operation is logged using the Logging Wrapper component.
 *
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe. any manager class used by this handler is supposed to be thread safe.
 * </p>
 *
 * <p>
 * Version 1.0.2 (Milestone Support Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Updated the handler to sort the project phases before applying the template.</li>
 *   </ol>
 * </p>
 *
 * @author topgear
 * @author assistant, TCSDEVELOPER
 * @version 1.1
 */
public class LoadTimelineTemplateHandler extends CommonHandler {

    /**
     * Represents the status of success.
     */
    private static final String SUCCESS = "Success";

    /**
     * Represents the status of invalid template name.
     */
    private static final String INVALID_TEMPLATE_NAME_ERROR = "Invalid template name error";

    /**
     * Represents the status of business error.
     */
    private static final String BUSINESS_ERROR = "Business error";

    /**
     * Represents the status of role error.
     */
    private static final String ROLE_ERROR = "Role error";

    /**
     * Represents the status of login error.
     */
    private static final String LOGIN_ERROR = "Login error";

    /**
     * Represents the status of invalid parameter error.
     */
    private static final String INVALID_PARAMETER_ERROR = "Invalid parameter error";

    /**
     * <p>
     * The phase template used to generate timeline data.
     * This variable is immutable, it is initialized by the constructor to a not null PhaseTemplate object,
     * and used by the service method.
     * </p>
     */
    private final PhaseTemplate phaseTemplate;

    /**
     * <p>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public LoadTimelineTemplateHandler() throws ConfigurationException {
        phaseTemplate = (PhaseTemplate) AjaxSupportHelper.createObject(PhaseTemplate.class);
    }

    /**
     * <p>
     * Performs the "Load Timeline Template" operation and return the timeline data wrapped
     * in a success Ajax response for successful operation, or an error ajax response otherwise.
     * </p>
     *
     * @return the response to the request
     * @param request the request to service
     * @param userId the id of user who issued this request
     * @throws IllegalArgumentException if the request is null
     */
    public AjaxResponse service(AjaxRequest request, Long userId) {
        if (request == null) {
            throw new IllegalArgumentException("The request can't be null.");
        }

        // check all the required parameters are in the request object
        String name = request.getParameter("TemplateName");
        if (name == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The Template name is not set.", "LoadTimelineTemplate. " + "User id : " + userId);
        }
        if (name.trim().length() == 0) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The Template name should not be empty.", "LoadTimelineTemplate. " + "User id : " + userId);
        }
        Date start;
        try {
            start = request.getParameterAsDate("StartDate");
        } catch (ParseException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The StartDate parameter can't be parsed.", "LoadTimelineTemplate. " + "User id : " + userId, e);
        }

        // check the userId for validation
        if (userId == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), LOGIN_ERROR,
                    "Doesn't login or expired.", "LoadTimelineTemplate. " + "User id : " + userId);
        }

        try {
            // check that the user has the global manager role
            if (!checkUserHasGlobalManagerRole(userId)) {
                // if doesn't have the role, return an error response
                return AjaxSupportHelper.createAndLogError(request.getType(), ROLE_ERROR,
                        "User doesn't have the role.", "LoadTimelineTemplate. " + "User id : " + userId);
            }
        } catch (ResourceException e) {
            // if exception raised, return a business error response
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "User doesn't have the role.", "LoadTimelineTemplate. " + "User id : " + userId, e);
        }

        // find the template, if not found, return an error response
        String[] names = phaseTemplate.getAllTemplateNames();
        boolean found = false;
        for (String nameTmp : names) {
            if (nameTmp.equals(name)) {
                found = true;
                break;
            }
        }
        if (!found) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_TEMPLATE_NAME_ERROR,
                    "Can't find the template '" + name + "'.", "LoadTimelineTemplate. " + "User id : " + userId);

        }

        // apply the template
        Project project;
        try {
            if (start != null) {
                project = phaseTemplate.applyTemplate(name, start);
            } else {
                project = phaseTemplate.applyTemplate(name);
            }
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR, "Can't apply template",
                    "LoadTimelineTemplate. " + "User id : " + userId, e);
        }

        // generate the xml
        String xml = timelineToXml(project);

        return AjaxSupportHelper.createAndLogSuccess(request.getType(), SUCCESS, "succeed to load template",
                xml, "LoadTimelineTemplate. " + "User id : " + userId + " Start : " + start);
    }

    /**
     * <p>
     * Produces the XML representation of the timeline data as described in the TimelineData.xsd file.
     * </p>
     *
     * @return XML string representing the timeline data
     * @param project the project containing the timeline data
     * @throws IllegalArgumentException if project is null
     */
    public static String timelineToXml(Project project) {
        if (project == null) {
            throw new IllegalArgumentException("The project can't be null.");
        }

        // use a string buffer to compose the xml
        StringBuffer sb = new StringBuffer();
        sb.append("<timeline>");
        sb.append("<phases>");

        // loop over all the phases to generate the xml
        Phase[] phases = project.getAllPhases();
        Arrays.sort(phases, new ProjectPhaseComparator());

        for (Phase phase : phases) {
            phaseToXml(phase, sb);
        }

        sb.append("</phases>");
        sb.append("</timeline>");

        return sb.toString();
    }

    /**
     * Produces the XML representation of the phase data.
     *
     * @param phase the phase data
     * @param sb the string buffer to handle the xml
     */
    private static void phaseToXml(Phase phase, StringBuffer sb) {
        // start the element
        sb.append("<phase type=\"");
        sb.append(phase.getPhaseType().getName());
        sb.append("\" id=\"");
        sb.append(phase.getId());
        sb.append("\">");

        // start date
        sb.append("<start-date>");
        // start date and end date
        if (phase.getFixedStartDate() != null) {
            sb.append(AjaxSupportHelper.dateToString(phase.getFixedStartDate()));
        } else {
            sb.append(AjaxSupportHelper.dateToString(phase.calcStartDate()));
        }
        sb.append("</start-date>");

        // end date
        sb.append("<end-date>");
        sb.append(AjaxSupportHelper.dateToString(phase.calcEndDate()));
        sb.append("</end-date>");

        // the length
        sb.append("<length>");
        sb.append(phase.getLength());
        sb.append("</length>");

        // generate the dependencies if and only if the fixed start date is null
        if (phase.getFixedStartDate() == null) {
            sb.append("<dependencies>");

            Dependency[] dependencies = phase.getAllDependencies();
            for (Dependency dependency : dependencies) {
                dependencyToXml(dependency, sb);
            }
            sb.append("</dependencies>");
        }

        sb.append("</phase>");
    }

    /**
     * Convert dependency to an xml.
     *
     * @param dependency the dependency object
     * @param sb the string buffer used for convert
     */
    private static void dependencyToXml(Dependency dependency, StringBuffer sb) {
        sb.append("<dependency>");

        // append the dependency-phase-id
        sb.append("<dependency-phase-id>");
        sb.append(dependency.getDependency().getId());
        sb.append("</dependency-phase-id>");

        // append the dependent-phase-id
        sb.append("<dependent-phase-id>");
        sb.append(dependency.getDependent().getId());
        sb.append("</dependent-phase-id>");

        // append the dependency-phase-start
        sb.append("<dependency-phase-start>");
        sb.append(dependency.isDependencyStart());
        sb.append("</dependency-phase-start>");

        // append the dependent-phase-start
        sb.append("<dependent-phase-start>");
        sb.append(dependency.isDependentStart());
        sb.append("</dependent-phase-start>");

        // append the lag-time
        sb.append("<lag-time>");
        sb.append(dependency.getLagTime());
        sb.append("</lag-time>");

        sb.append("</dependency>");
    }

    /**
     * This class extends <code>PhaseDateComparator</code> class and is used to sort project
     * phases in array. It uses <code>compare</code> method from the
     * <code>PhaseDateComparator</code> class to sort phases. If it is impossible to determine the
     * order by simply using that method, it orders phases by their type using built-in (hardcoded)
     * type-order.
     */
    static public class ProjectPhaseComparator extends PhaseDateComparator {

        /**
         * This static member variable is an array that is used to determine the order of the phases
         * by their types.
         */
        final private static String[] phaseOrder = new String[] {
            "Specification Submission", "Specification Review",
            "Registration", "Milestone Submission", 
            "Milestone Screening", "Milestone Review", 
            "Submission", "Screening",
            "Review", "Appeals", "Appeals Response",
            "Aggregation", "Aggregation Review", "Final Fix",
            "Final Review", "Approval"
        };

        /**
         * This method compares its two arguments for order. This method expects that type of
         * objects passed as arguments is <code>Phase</code>. It then determines which of the
         * objects is smaller using <code>compare</code> method from the superclass first, and then
         * comparing phases' imaginary rankings if the first comparison does not let determine the
         * order.
         * <p>
         * This method extends the <code>compare</code> method from the
         * <code>PhaseDateComparator</code> class.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param o1
         *            the first object to be compared.
         * @param o2
         *            the second object to be compared.
         */
        public int compare(Object o1, Object o2) {
            // Try to compare objects using the method from the superclass
            Phase phase1 = (Phase) o1;
            Phase phase2 = (Phase) o2;

            boolean isPhase1PostMortem = phase1.getPhaseType().getName().equalsIgnoreCase("Post-Mortem");
            boolean isPhase2PostMortem = phase2.getPhaseType().getName().equalsIgnoreCase("Post-Mortem");

            if (isPhase1PostMortem || isPhase2PostMortem) {
                PhaseStatus phaseStatus1 = phase1.getPhaseStatus();
                PhaseStatus phaseStatus2 = phase2.getPhaseStatus();
                if (isPhase1PostMortem) {
                    if (phaseStatus2.getId() == PhaseStatus.SCHEDULED.getId()) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    if (phaseStatus1.getId() == PhaseStatus.SCHEDULED.getId()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
            
            boolean isPhase1Milestone = phase1.getPhaseType().getName().startsWith("Milestone");
            boolean isPhase2Milestone = phase2.getPhaseType().getName().startsWith("Milestone");
            if ((isPhase1Milestone || isPhase2Milestone) && !(isPhase1Milestone && isPhase2Milestone)) {
                final int ranking1 = getPhaseRanking((Phase) o1);
                final int ranking2 = getPhaseRanking((Phase) o2);
                return ranking1 - ranking2;
            }

            int comparison = super.compare(o1, o2);
            if (comparison != 0) {
                return comparison;
            }
            // Determine phases' imaginary ranking
            final int ranking1 = getPhaseRanking((Phase) o1);
            final int ranking2 = getPhaseRanking((Phase) o2);
            // Compare phases using the imaginary ranking
            return ranking1 - ranking2;
        }

        /**
         * This static method determines imaginary ranking of the phase passed as the parameter.
         * Phase's ranking is determined by the hard-coded array of phases' names (i.e. phase's
         * ranking depends on phase's type).
         *
         * @return an integer number equal or greater than zero that represents phase's imaginary
         *         ranking.
         * @param phase
         *            a phase to determine the imaginary ranking of.
         */
        private static int getPhaseRanking(Phase phase) {
            // Get phase's name
            final String phaseName = phase.getPhaseType().getName();
            // Search for this phase in the ranking array
            for (int i = 0; i < phaseOrder.length; i++) {
                if (phaseOrder[i].equalsIgnoreCase(phaseName)) {
                    return i;
                }
            }
            // If phase's type wasn't found in the array, return the highest ranking possible
            return phaseOrder.length;
        }
    }
}
