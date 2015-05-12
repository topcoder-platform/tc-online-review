/*
 * Copyright (C) 2006 - 2013 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.cronos.onlinereview.Constants;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.payment.ProjectPayment;
import com.topcoder.management.project.Prize;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Review;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;
import com.topcoder.project.phases.PhaseStatus;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * This class is used to group all comparator-classes in one file. It contains several inner-classes
 * that implement <code>Comparator</code> interface, and are used throughout application's code to
 * compare different objects (mostly for sorting purposes).
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public final class Comparators {
    /**
     * Empty private constructor of the class to prevent class's instantiation (thus making this
     * class 'static').
     */
    private Comparators() {
        // Empty constructor
    }

    /**
     * This class extends <code>PhaseDateComparator</code> class and is used to sort project
     * phases in array. It uses <code>compare</code> method from the
     * <code>PhaseDateComparator</code> class to sort phases. If it is impossible to determine the
     * order by simply using that method, it orders phases by their type using built-in (hardcoded)
     * type-order.
     */
    public static class ProjectPhaseComparer extends PhaseDateComparator {
        /**
         * This static member variable is an array that is used to determine the order of the phases
         * by their types.
         */
        final private static String[] phaseOrder = new String[] {
            Constants.SPECIFICATION_SUBMISSION_PHASE_NAME, Constants.SPECIFICATION_REVIEW_PHASE_NAME,
            Constants.REGISTRATION_PHASE_NAME, Constants.CHECKPOINT_SUBMISSION_PHASE_NAME, 
            Constants.CHECKPOINT_SCREENING_PHASE_NAME, Constants.CHECKPOINT_REVIEW_PHASE_NAME, 
            Constants.SUBMISSION_PHASE_NAME, Constants.ITERATIVE_REVIEW_PHASE_NAME, Constants.SCREENING_PHASE_NAME,
            Constants.REVIEW_PHASE_NAME, Constants.APPEALS_PHASE_NAME, Constants.APPEALS_RESPONSE_PHASE_NAME,
            Constants.AGGREGATION_PHASE_NAME, Constants.AGGREGATION_REVIEW_PHASE_NAME, Constants.FINAL_FIX_PHASE_NAME,
            Constants.FINAL_REVIEW_PHASE_NAME, Constants.APPROVAL_PHASE_NAME
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

            boolean isPhase1Checkpoint = phase1.getPhaseType().getName().startsWith("Checkpoint");
            boolean isPhase2Checkpoint = phase2.getPhaseType().getName().startsWith("Checkpoint");

            if ((isPhase1Checkpoint || isPhase2Checkpoint) && !(isPhase1Checkpoint && isPhase2Checkpoint)) {
                final int ranking1 = getPhaseRanking((Phase) o1);
                final int ranking2 = getPhaseRanking((Phase) o2);
                return ranking1 - ranking2;
            }

            // iterative review phase is always listed after submission phase
            boolean isPhase1IterativeReview = phase1.getPhaseType().getName()
                    .equalsIgnoreCase(Constants.ITERATIVE_REVIEW_PHASE_NAME);
            boolean isPhase2IterativeReview = phase2.getPhaseType().getName()
                    .equalsIgnoreCase(Constants.ITERATIVE_REVIEW_PHASE_NAME);
            if ((isPhase1IterativeReview || isPhase2IterativeReview)
                    && !(isPhase1IterativeReview && isPhase2IterativeReview)) {
                final int ranking1 = getPhaseRanking(phase1);
                final int ranking2 = getPhaseRanking(phase2);
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

    /**
     * This class implements <code>Comparator</code> interface and is used to sort ProjectTypes
     * in array. It orders Project Types by their name, in ascending order.
     */
    public static class ProjectTypeComparer implements Comparator<ProjectType> {
        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>ProjectType</code>. It then determines which of
         * the objects is smaller taking their names and comparing them using natural alphabetical
         * order.
         * <p>
         * This method implements the <code>compare</code> method from the <code>Comparator</code>
         * interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param pt1
         *            the first object to be compared.
         * @param pt2
         *            the second object to be compared.
         */
        public int compare(ProjectType pt1, ProjectType pt2) {
            // Compare project types by their name using natural alphabetic order
            return pt1.getName().compareTo(pt2.getName());
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort projects by
     * their names in ascending order.
     */
    public static class ProjectNameComparer implements Comparator<Project> {
        /**
         * This method compares its two arguments for order. This method expects that type of the
         * objects passed as arguments is <code>Project</code>. It then determines which of the
         * objects is smaller taking their names and comparing them using natural alphabetical
         * order. For projects that have identical names, their order is determined by their version
         * numbers. The case of the names does not matter (the comparison is case-insensitive).
         * <p>
         * This method implements the <code>compare</code> method from the <code>Comparator</code>
         * interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param project1
         *            the first object to be compared.
         * @param project2
         *            the second object to be compared.
         */
        public int compare(Project project1, Project project2) {
            // Get the names of the projects
            String strName1 = (String) project1.getProperty("Project Name");
            String strName2 = (String) project2.getProperty("Project Name");

            if (strName1 == null) {
                strName1 = "";
            }

            if (strName2 == null) {
                strName2 = "";
            }

            // Compare project names using natural alphabetic order
            int result = strName1.compareToIgnoreCase(strName2);

            if (result != 0) {
                return result;
            }

            /*
             * In case names of the projects are identical, comparison by versions is needed
             */

            // Get versions of the projects
            String strVersion1 = (String) project1.getProperty("Project Version");
            String strVersion2 = (String) project2.getProperty("Project Version");

            if (strVersion1 == null) {
                strVersion1 = "";
            }

            if (strVersion2 == null) {
                strVersion2 = "";
            }

            // Split version strings into array of sub-versions (assuming that separator is a dot)
            final String[] versions1 = strVersion1.split("\\.");
            final String[] versions2 = strVersion2.split("\\.");

            // Versions can be badly formatted, so this section is guarded by try-catch
            try {
                for (int i = 0; i < versions1.length && i < versions2.length; ++i) {
                    // Get every subversion number and try to convert it to number
                    final int subVer1 = Integer.parseInt(versions1[i], 10);
                    final int subVer2 = Integer.parseInt(versions2[i], 10);

                    // If sub-versions differ, that's how order is determined
                    if (subVer1 != subVer2) {
                        return (subVer1 - subVer2);
                    }
                }
            } catch (NumberFormatException nfe) {
                // Compare versions by their text representation
                return strVersion1.compareTo(strVersion2);
            }

            // Versions can have different number of parts (subversions)
            return (versions1.length - versions2.length);
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort Submissions in
     * array. It sorts Submissions either by the time they were submitted (starting from the most
     * recent ones), or by the place submission took up.
     */
    public static class SubmissionComparer implements Comparator<Submission> {
        /**
         * This member variable contains resources (submitters) assigned to this comparator class.
         * They are stored in the map data structure, where each submitter's ID is matched to that
         * submitter's record, to speed up the access operation.
         */
        private Map<Long, Resource> submitters = null;

        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>Submission</code>.
         * <p>
         * This method implements the <code>compare</code> method from the
         * <code>Comparator</code> interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param submission1
         *            the first object to be compared.
         * @param submission2
         *            the second object to be compared.
         */
        public int compare(Submission submission1, Submission submission2) {
            if (submitters == null) {
                throw new IllegalStateException("Submitters must be assigned before sorting operation.");
            }

            Double finalScore1 = submission1.getFinalScore() == null ? 0 : submission1.getFinalScore(); 
            Double finalScore2 = submission2.getFinalScore() == null ? 0 : submission2.getFinalScore();

            // Compare submissions by their final scores,
            // or by their upload times, which are the creation times of their respective uploads
            return ((finalScore1.compareTo(finalScore2) != 0)
                    ? finalScore2.compareTo(finalScore1)
                    : submission1.getUpload().getCreationTimestamp().compareTo(
                            submission2.getUpload().getCreationTimestamp()));
        }

        /**
         * This method assigns submitters to this comparator class for their use in later comparison
         * operation.
         *
         * @param submitters
         *            an array of resources, each element of which has Submitter role. This
         *            parameter can be <code>null</code>, but if it is not <code>null</code>,
         *            it must not contain <code>null</code> elements or contain resources that are
         *            not Submitters.
         * @throws IllegalArgumentException
         *             if parameter <code>submitters</code> is not <code>null</code> and
         *             contains <code>null</code> elements or any of the resources stored in it do
         *             not have a Submitter role.
         */
        public void assignSubmitters(Resource[] submitters) {
            this.submitters = new HashMap<Long, Resource>();

            if (submitters == null) {
                return;
            }

            for (Resource submitter : submitters) {
                if (submitter == null) {
                    throw new IllegalArgumentException("Parameter 'submitters' must not contain null elements.");
                }
                if (!submitter.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    throw new IllegalArgumentException("Parameter 'submitters' must contain Submitters only.");
                }
                this.submitters.put(submitter.getId(), submitter);
            }
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort reviews in
     * array. It sorts reviews by their creation time, from the least recent to the most recent
     * ones.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class ReviewComparer implements Comparator<Review> {
        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>Review</code>.
         * <p>
         * This method implements the <code>compare</code> method from the
         * <code>Comparator</code> interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param review1
         *            the first object to be compared.
         * @param review2
         *            the second object to be compared.
         */
        public int compare(Review review1, Review review2) {
            // Compare reviews by their creation times
            return review1.getCreationTimestamp().compareTo(review2.getCreationTimestamp());
        }
    }

    /**
     * <p>This class implements <code>Comparator</code> interface and is used to sort late deliverables in
     * the list. It sorts late deliverables by their creation date, from the least recent to the most recent ones.</p>
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class LateDeliverableComparer implements Comparator<LateDeliverable> {
        /**
         * <p>This method compares its two arguments for order. This method expects that type of the objects passed as
         * arguments is <code>LateDeliverable</code>.</p>
         *
         * @param lateDeliverable1 the first late deliverable to be compared.
         * @param lateDeliverable2 the second late deliverable to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second respectively.
         */
        public int compare(LateDeliverable lateDeliverable1, LateDeliverable lateDeliverable2) {
            return lateDeliverable1.getCreateDate().compareTo(lateDeliverable2.getCreateDate());
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort Checkpoint Submissions in
     * array. It sorts Submissions either by the time they were submitted (starting from the most
     * recent ones), or by the place submission took up.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class CheckpointSubmissionComparator implements Comparator<Submission> {
        /**
         * This member variable contains resources (submitters) assigned to this comparator class.
         * They are stored in the map data structure, where each submitter's ID is matched to that
         * submitter's record, to speed up the access operation.
         */
        private Map<Long, Resource> submitters = null;

        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>Submission</code>.
         * <p>
         * This method implements the <code>compare</code> method from the
         * <code>Comparator</code> interface.
         * </p>
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less
         *         than, equal to, or greater than the second respectively.
         * @param submission1 the first object to be compared.
         * @param submission2 the second object to be compared.
         */
        public int compare(Submission submission1, Submission submission2) {
            if (submitters == null) {
                throw new IllegalStateException("Submitters must be assigned before sorting operation.");
            }

            Double finalScore1 = submission1.getInitialScore() == null ? 0 : submission1.getInitialScore(); 
            Double finalScore2 = submission2.getInitialScore() == null ? 0 : submission2.getInitialScore();
            
            if (submission1.getInitialScore() != null || submission2.getInitialScore() != null) {
                return finalScore2.compareTo(finalScore1);
            }

            Double screeningScore1 = submission1.getScreeningScore() == null ? 0 : submission1.getScreeningScore(); 
            Double screeningScore2 = submission2.getScreeningScore() == null ? 0 : submission2.getScreeningScore();
            
            if (submission1.getScreeningScore() != null || submission2.getScreeningScore() != null) {
                return screeningScore2.compareTo(screeningScore1);
            }

            return submission2.getUpload().getCreationTimestamp().compareTo(
                submission1.getUpload().getCreationTimestamp());
        }

        /**
         * This method assigns submitters to this comparator class for their use in later comparison
         * operation.
         *
         * @param submitters
         *            an array of resources, each element of which has Submitter role. This
         *            parameter can be <code>null</code>, but if it is not <code>null</code>,
         *            it must not contain <code>null</code> elements or contain resources that are
         *            not Submitters.
         * @throws IllegalArgumentException
         *             if parameter <code>submitters</code> is not <code>null</code> and
         *             contains <code>null</code> elements or any of the resources stored in it do
         *             not have a Submitter role.
         */
        public void assignSubmitters(Resource[] submitters) {
            this.submitters = new HashMap<Long, Resource>();

            if (submitters == null) {
                return;
            }

            for (Resource submitter : submitters) {
                if (submitter == null) {
                    throw new IllegalArgumentException("Parameter 'submitters' must not contain null elements.");
                }

                if (!submitter.getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    throw new IllegalArgumentException("Parameter 'submitters' must contain Submitters only.");
                }

                this.submitters.put(submitter.getId(), submitter);
            }
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort Prize
     * in array. It orders Prize by their place, in ascending order.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class PrizePlaceComparator implements Comparator<Prize> {
        /**
         * <p>This method compares its two arguments for order. This method expects that type of the
         * objects passed as arguments is <code>Prize</code>.</p>
         *
         * @param prize1 the first Prize to be compared.
         * @param prize2 the second Prize to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second respectively.
         */
        public int compare(Prize prize1, Prize prize2) {
            return prize1.getPlace() - prize2.getPlace();
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort Project Payment
     * in array. It orders Project Payment by their id, in ascending order.
     *
     * @author TCSASSEMBLER
     * @version 2.0
     */
    public static class ProjectPaymentComparator implements Comparator<ProjectPayment> {
        /**
         * <p>This method compares its two arguments for order. This method expects that type of the
         * objects passed as arguments is <code>ProjectPayment</code>.</p>
         *
         * @param payment1 the first Project Payment to be compared.
         * @param payment2 the second Project Payment to be compared.
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second respectively.
         */
        public int compare(ProjectPayment payment1, ProjectPayment payment2) {
            long diff = payment1.getProjectPaymentId() - payment2.getProjectPaymentId();
            return diff < 0 ? -1 : (diff == 0 ? 0 : 1);
        }
    }
}
