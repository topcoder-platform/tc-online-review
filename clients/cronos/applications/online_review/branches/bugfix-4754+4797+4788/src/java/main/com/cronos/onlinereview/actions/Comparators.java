/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Comparator;

import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;

/**
 * This class is used to group all comparator-classes in one file. It contains several inner-classes
 * that implement <code>Comparator</code> interface, and are used throughout application's code to
 * compare different objects (mostly for sorting purposes).
 *
 * @author George1
 * @author real_vg
 * @version 1.0
 */
final class Comparators {

    /**
     * Emtpy private constructor of the class to prevent class's instantiation (thus making this
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
    static public class ProjectPhaseComparer extends PhaseDateComparator {

        /**
         * This static member variable is an array that is used to determine the order of the phases
         * by their types.
         */
        final private static String[] phaseOrder = new String[] {
            Constants.REGISTRATION_PHASE_NAME, Constants.SUBMISSION_PHASE_NAME, Constants.SCREENING_PHASE_NAME,
            Constants.REVIEW_PHASE_NAME, Constants.APPEALS_PHASE_NAME, Constants.APPEALS_RESPONSE_PHASE_NAME,
            Constants.AGGREGATION_PHASE_NAME, Constants.AGGREGATION_REVIEW_PHASE_NAME, Constants.FINAL_FIX_PHASE_NAME,
            Constants.FINAL_REVIEW_PHASE_NAME, Constants.APPROVAL_PHASE_NAME
        };

        /**
         * This method compares its two arguments for order. This method expects that type of
         * objects passed as arguments is <code>Phase</code>. It then detemines which of the
         * objects is smaller using <code>compare</code> method from the superclas first, and then
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
    static public class ProjectTypeComparer implements Comparator {

        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>ProjectType</code>. It then detemines which of
         * the objects is smaller taking their names and comparing then using natural alphabetical
         * order.
         * <p>
         * This method implements the <code>compare</code> method from the <code>Comparator</code>
         * interface.
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
            // Cast the passed parameters to the appropriate type
            ProjectType pt1 = (ProjectType)o1;
            ProjectType pt2 = (ProjectType)o2;
            // Compare project types by their name using natural alphabetic order
            return pt1.getName().compareTo(pt2.getName());
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort Uploads in
     * array. It sorts Uploads by their creation time, from the least recent to the most recent
     * ones.
     */
    static class UploadComparer implements Comparator {

        /**
         * This method compares its two arguments for order. This method expects that type of
         * the objects passed as arguments is <code>Upload</code>.
         * <p>
         * This method implements the <code>compare</code> method from the
         * <code>Comparator</code> interface.
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
            // Cast the passed parameters to the appropriate type
            Upload up1 = (Upload)o1;
            Upload up2 = (Upload)o2;
            // Compare uploads by their creation times
            return up1.getCreationTimestamp().compareTo(up2.getCreationTimestamp());
        }
    }

    /**
     * This class implements <code>Comparator</code> interface and is used to sort reviews in
     * array. It sorts reviews by their creation time, from the least recent to the most recent
     * ones.
     */
    static class ReviewComparer implements Comparator {

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
         * @param o1
         *            the first object to be compared.
         * @param o2
         *            the second object to be compared.
         */
        public int compare(Object o1, Object o2) {
            // Cast the passed parameters to the appropriate type
            Review review1 = (Review)o1;
            Review review2 = (Review)o2;
            // Compare reviews by their creation times
            return review1.getCreationTimestamp().compareTo(review2.getCreationTimestamp());
        }
    }
}
