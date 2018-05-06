/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

/**
 * <p>
 * This is a ProjectStudioSpecification entity which represents the Project Studio Specification. It extends from
 * AuditableObject class. Added in version 1.2.
 * </p>
 * <p>
 * <strong>Thread-Safety:</strong> This class is mutable and not thread safe. But it will be used as entity so it'll not
 * cause any thread safe problem.
 * </p>
 *
 * @author flytoj2ee, TCSDEVELOPER
 * @version 1.2
 * @since 1.2
 */
@SuppressWarnings("serial")
public class ProjectStudioSpecification extends AuditableObject {
    /**
     * Represents the goals of ProjectStudioSpecification. The default value is null. It's changeable. It could not be
     * null or empty. It's accessed in setter and getter.
     */
    private String goals;

    /**
     * Represents the targetAudience of ProjectStudioSpecification. The default value is null. It's changeable. It could
     * not be null or empty. It's accessed in setter and getter.
     */
    private String targetAudience;

    /**
     * Represents the brandingGuidelines of ProjectStudioSpecification. The default value is null. It's changeable. It
     * not be null or empty. It's accessed in setter and getter.
     */
    private String brandingGuidelines;

    /**
     * Represents the dislikedDesignWebSites of ProjectStudioSpecification. The default value is null. It's changeable.
     * It could not be null or empty. It's accessed in setter and getter.
     */
    private String dislikedDesignWebSites;

    /**
     * Represents the otherInstructions of ProjectStudioSpecification. The default value is null. It's changeable. It
     * could not be null or empty. It's accessed in setter and getter.
     */
    private String otherInstructions;

    /**
     * Represents the winningCriteria of ProjectStudioSpecification. The default value is null. It's changeable. It
     * could not be null or empty. It's accessed in setter and getter.
     */
    private String winningCriteria;

    /**
     * Represents the submittersLockedBetweenRounds of ProjectStudioSpecification. The default value is false. It's
     * changeable. It could be any value. It's accessed in setter and getter.
     */
    private boolean submittersLockedBetweenRounds;

    /**
     * Represents the roundOneIntroduction of ProjectStudioSpecification. The default value is null. It's changeable. It
     * could not be null or empty. It's accessed in setter and getter.
     */
    private String roundOneIntroduction;

    /**
     * Represents the roundTwoIntroduction of ProjectStudioSpecification. The default value is null. It's changeable. It
     * could not be null or empty. It's accessed in setter and getter.
     */
    private String roundTwoIntroduction;

    /**
     * Represents the colors of ProjectStudioSpecification. The default value is null. It's changeable. It could not be
     * null or empty. It's accessed in setter and getter.
     */
    private String colors;

    /**
     * Represents the fonts of ProjectStudioSpecification. The default value is null. It's changeable. It could not be
     * null or empty. It's accessed in setter and getter.
     */
    private String fonts;

    /**
     * Represents the layoutAndSize of ProjectStudioSpecification. The default value is null. It's changeable. It could
     * not be null or empty. It's accessed in setter and getter.
     */
    private String layoutAndSize;

    /**
     * Represents the id of ProjectStudioSpecification. The default value is 0. It's changeable. It should be a positive
     * value. It's accessed in setter and getter.
     */
    private long id;

    /**
     * Empty constructor.
     */
    public ProjectStudioSpecification() {
    }

    /**
     * Returns the value of goals attribute.
     *
     * @return the value of goals.
     */
    public String getGoals() {
        return this.goals;
    }

    /**
     * Sets the given value to goals attribute.
     *
     * @param goals
     *            the given value to set.
     * @throws IllegalArgumentException if the argument is null or empty string
     */
    public void setGoals(String goals) {
        this.goals = goals;
    }

    /**
     * Returns the value of targetAudience attribute.
     *
     * @return the value of targetAudience.
     */
    public String getTargetAudience() {
        return this.targetAudience;
    }

    /**
     * Sets the given value to targetAudience attribute.
     *
     * @param targetAudience
     *            the given value to set.
     */
    public void setTargetAudience(String targetAudience) {
        this.targetAudience = targetAudience;
    }

    /**
     * Returns the value of brandingGuidelines attribute.
     *
     * @return the value of brandingGuidelines.
     */
    public String getBrandingGuidelines() {
        return this.brandingGuidelines;
    }

    /**
     * Sets the given value to brandingGuidelines attribute.
     *
     * @param brandingGuidelines
     *            the given value to set.
     */
    public void setBrandingGuidelines(String brandingGuidelines) {
        this.brandingGuidelines = brandingGuidelines;
    }

    /**
     * Returns the value of dislikedDesignWebSites attribute.
     *
     * @return the value of dislikedDesignWebSites.
     */
    public String getDislikedDesignWebSites() {
        return this.dislikedDesignWebSites;
    }

    /**
     * Sets the given value to dislikedDesignWebSites attribute.
     *
     * @param dislikedDesignWebSites
     *            the given value to set.
     */
    public void setDislikedDesignWebSites(String dislikedDesignWebSites) {
        this.dislikedDesignWebSites = dislikedDesignWebSites;
    }

    /**
     * Returns the value of otherInstructions attribute.
     *
     * @return the value of otherInstructions.
     */
    public String getOtherInstructions() {
        return this.otherInstructions;
    }

    /**
     * Sets the given value to otherInstructions attribute.
     *
     * @param otherInstructions
     *            the given value to set.
     */
    public void setOtherInstructions(String otherInstructions) {
        this.otherInstructions = otherInstructions;
    }

    /**
     * Returns the value of winningCriteria attribute.
     *
     * @return the value of winningCriteria.
     */
    public String getWinningCriteria() {
        return this.winningCriteria;
    }

    /**
     * Sets the given value to winningCriteria attribute.
     *
     * @param winningCriteria
     *            the given value to set.
     */
    public void setWinningCriteria(String winningCriteria) {
        this.winningCriteria = winningCriteria;
    }

    /**
     * Returns the value of submittersLockedBetweenRounds attribute.
     *
     * @return the value of submittersLockedBetweenRounds.
     */
    public boolean isSubmittersLockedBetweenRounds() {
        return this.submittersLockedBetweenRounds;
    }

    /**
     * Sets the given value to submittersLockedBetweenRounds attribute.
     *
     * @param submittersLockedBetweenRounds
     *            the given value to set.
     */
    public void setSubmittersLockedBetweenRounds(boolean submittersLockedBetweenRounds) {
        this.submittersLockedBetweenRounds = submittersLockedBetweenRounds;
    }

    /**
     * Returns the value of roundOneIntroduction attribute.
     *
     * @return the value of roundOneIntroduction.
     */
    public String getRoundOneIntroduction() {
        return this.roundOneIntroduction;
    }

    /**
     * Sets the given value to roundOneIntroduction attribute.
     *
     * @param roundOneIntroduction
     *            the given value to set.
     */
    public void setRoundOneIntroduction(String roundOneIntroduction) {
        this.roundOneIntroduction = roundOneIntroduction;
    }

    /**
     * Returns the value of roundTwoIntroduction attribute.
     *
     * @return the value of roundTwoIntroduction.
     */
    public String getRoundTwoIntroduction() {
        return this.roundTwoIntroduction;
    }

    /**
     * Sets the given value to roundTwoIntroduction attribute.
     *
     * @param roundTwoIntroduction
     *            the given value to set.
     */
    public void setRoundTwoIntroduction(String roundTwoIntroduction) {
        this.roundTwoIntroduction = roundTwoIntroduction;
    }

    /**
     * Returns the value of colors attribute.
     *
     * @return the value of colors.
     */
    public String getColors() {
        return this.colors;
    }

    /**
     * Sets the given value to colors attribute.
     *
     * @param colors
     *            the given value to set.
     */
    public void setColors(String colors) {
        this.colors = colors;
    }

    /**
     * Returns the value of fonts attribute.
     *
     * @return the value of fonts.
     */
    public String getFonts() {
        return this.fonts;
    }

    /**
     * Sets the given value to fonts attribute.
     *
     * @param fonts
     *            the given value to set.
     */
    public void setFonts(String fonts) {
        this.fonts = fonts;
    }

    /**
     * Returns the value of layoutAndSize attribute.
     *
     * @return the value of layoutAndSize.
     */
    public String getLayoutAndSize() {
        return this.layoutAndSize;
    }

    /**
     * Sets the given value to layoutAndSize attribute.
     *
     * @param layoutAndSize
     *            the given value to set.
     */
    public void setLayoutAndSize(String layoutAndSize) {
        this.layoutAndSize = layoutAndSize;
    }

    /**
     * Returns the value of id attribute.
     *
     * @return the value of id.
     */
    public long getId() {
        return this.id;
    }

    /**
     * Sets the given value to id attribute.
     *
     * @param id
     *            the given value to set.
     */
    public void setId(long id) {
        this.id = id;
    }
}
