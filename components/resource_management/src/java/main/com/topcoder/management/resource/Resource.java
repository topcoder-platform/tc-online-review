/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * The Resource class is the main modeling class in this component. It
 * represents any arbitrary resource. The Resource class is simply a container
 * for a few basic data fields. All data fields in this class are mutable and
 * have get and set methods.
 * </p>
 *
 * <p>
 * This class is mutable because its base class is mutable. Hence it is not
 * thread-safe.
 * </p>
 *
 * <p>Changes is version 1.1: Multiple submissions can be associated with a resource.
 * The submission field variable is a Set and it can be read and modified using the
 * provided utility methods.</p>
 *
 * @author aubergineanode, kinfkong, Rica, Xuchen
 * @version 1.1
 * @since 1.0
 */
public class Resource extends AuditableResourceStructure {

    /**
     * <p>
     * The value that the id field will have (and that the getId method will return)
     * when the id field has not been set in the constructor or through the setId method.
     * </p>
     *
     * This field is public, static, and final.
     *
     */
    public static final long UNSET_ID = -1;

    /**
     * <p>
     * The id of the Resource.  This field is set in the constructor and is
     * mutable.
     * </p>
     *
     * <p>
     * The value must always be &gt; 0 or UNSET_ID. The default value is
     * UNSET_ID. Once set to a value besides UNSET_ID, the id can not be set to
     * another value. This allows the class to have a Java Bean API but still
     * have an essentially unchangeable id. This field is set in the constructor
     * and setId method and is retrieved through the getId method.
     * </p>
     */
    private long id = UNSET_ID;

    /**
     * <p>
     * The role of the resource, as given by a ResourceRole instance.
     * This field is set in the constructor and is mutable.
     * </p>
     *
     * <p>
     * The value can be null or non-null. The default value is null, which indicates that
     * the role has not been set. This field is set in the setResourceRole method and
     * retrieved through the getResourceRole method.
     * </p>
     */
    private ResourceRole resourceRole;

    /**
     * <p>
     * The identifier of the project that the resource belongs to. The
     * value can be null or non-null.
     * </p>
     *
     * <p>
     * This field is mutable and its default is null. Null indicates that the
     * resource is not associated with a project. When non-null, the value of
     * this field will be &gt; 0.
     * This field is set in the setProject method and retrieved through the
     * getProject method.
     * </p>
     */
    private Long project = null;

    /**
     * <p>
     * The phase that the resource is associated with. The value can be
     * null or non-null.
     * </p>
     *
     * <p>
     * This field is mutable and its default value is null.
     * Null indicates that no phase is associated with the resource. When
     * non-null, the value of this field will be &gt; 0. This field is set in the
     * setPhase method and retrieved through the getPhase method.
     * </p>
     *
     */
    private Long phase = null;

    /**
     * <p>A set of submissions that the resource is associated with. It is initialized
     * with an empty Set and never changed afterwards. The contents can be changed.</p>
     *
     * <p>If no submissions are set yet, the submissions Set should be empty. Null values
     * and duplicates cannot be added to the submissions Set. Elements having the value
     * &lt;=0 cannot be added to the set.</p>
     *
     * <p>This field is added in version 1.1.</p>
     */
    private final Set submissions = new HashSet();

    /**
     * <p>
     * The map of properties that are associated with this resource.
     * The keys of the map are all required to be non-null String instances (no
     * other condition is enforced).
     * </p>
     *
     * <p>
     * The values in the map can be of any type.
     * However, persistence mechanisms are only required to support saving the
     * toString() string of each value. This field is immutable, but its
     * contents can be manipulated through the setProperty method. The values in
     * this map can be retrieved through the getProperty and getAllProperties
     * methods.
     * </p>
     */
    private Map properties = new HashMap();

    /**
     * <p>
     * The id of the user associated with the resource. The value can be null or non-null.
     * </p>
     *
     * <p>
     * This field is mutable and its default is null. Null indicates that the
     * resource is not associated with a user. When non-null, the value of
     * this field will be &gt; 0.
     * This field is set in the setUserId method and retrieved through the getUserId method.
     * </p>
     */
    private Long userId = null;

    /**
     * <p>
     * Creates a new Resource, initializing all constructor set fields to the default values.
     * </p>
     */
    public Resource() {
        // empty
    }

    /**
     * <p>
     * Creates a new Resource, setting the id field to the given value and the other
     * field is set to the default.
     * </p>
     *
     * @param id The id of the resource
     * @throws IllegalArgumentException If id is &lt;= 0
     */
    public Resource(long id) {
        setId(id);
    }

    /**
     * <p>
     * Creates a new Resource, setting all fields to the given values.
     * </p>
     *
     * @param id The id of the resource
     * @param resourceRole The role of the resource
     *
     * @throws IllegalArgumentException If id is &lt;= 0 or the resourceRole is null
     */
    public Resource(long id, ResourceRole resourceRole) {
        Helper.checkNull(resourceRole, "resourceRole");
        this.resourceRole = resourceRole;
        setId(id);
    }

    /**
     * <p>
     * Sets the id of the resource. The setId method only allows the id
     * to be set once. After that the id value is locked in and can not be
     * changed.
     * </p>
     *
     * @param id The id for the resource
     *
     * @throws IllegalArgumentException If id is &lt;= 0
     * @throws IdAlreadySetException If the id has already been set (i.e. the id field is not UNSET_ID)
     */
    public void setId(long id) {
        Helper.checkLongPositive(id, "id");
        if (this.id != UNSET_ID) {
            throw new IdAlreadySetException("The id has been set to be:" + this.id + " and cannot be re-set.");
        }
        this.id = id;
    }

    /**
     * <p>
     * Gets the id of the resource. The return is either UNSET_ID  or &gt; 0.
     * </p>
     *
     * @return The id of the resource
     */
    public long getId() {
        return this.id;
    }

    /**
     * <p>
     * Sets the role of the resource. The argument value can be
     * null or non-null.
     * </p>
     *
     * @param resourceRole The role of the resource
     */
    public void setResourceRole(ResourceRole resourceRole) {
        this.resourceRole = resourceRole;
    }

    /**
     * <p>
     * Gets the role of the resource. The return can be null or
     * non-null.
     * </p>
     *
     * @return The role of the resource.
     */
    public ResourceRole getResourceRole() {
        return this.resourceRole;
    }

    /**
     * <p>
     * Sets the project that the resource is associated with. The
     * value can be null or non-null. If non-null, it must be &gt; 0.
     * </p>
     *
     * @param project The identifier of the project the resource is associate with (or null)
     *
     * @throws IllegalArgumentException If project is a wrapper of a value &lt= 0
     */
    public void setProject(Long project) {
        if (project != null) {
            Helper.checkLongPositive(project.longValue(), "project");
        }
        this.project = project;
    }

    /**
     * <p>
     * Gets the project that the resource is associated with. The
     * return will either be null or &gt 0.
     * </p>
     *
     * @return The identifier of the project the resource is associated with
     */
    public Long getProject() {
        return this.project;
    }

    /**
     * <p>
     * Sets the phase that the resource is associated with. The value
     * can be null or non-null. If non-null, it must be &gt; 0.
     * </p>
     *
     * @param phase The identifier of the phase the resource is associated with (or null)
     *
     * @throws IllegalArgumentException If phase is a wrapper of a value &lt;= 0
     */
    public void setPhase(Long phase) {
        if (phase != null) {
            Helper.checkLongPositive(phase.longValue(), "phase");
        }
        this.phase = phase;
    }

    /**
     * <p>
     * Gets the identifier of the phase that the resource is
     * associated with. The return will either be null or &gt; 0.
     * </p>
     *
     * @return The identifier of the phase the resource is associated with
     */
    public Long getPhase() {
        return this.phase;
    }

    /**
     * <p>Sets the submissions that the resource is associated with.</p>
     *
     * <p>In version 1.1, this method is set with an array of submissions instead of a single one.</p>
     *
     * @param submissions the array ids of submissions to be set
     *
     * @throws IllegalArgumentException if the submissions array is null, or any of the submissions entries
     * has value &lt;= 0, or any of the submissions entries is null
     *
     * @since 1.1
     */
    public void setSubmissions(Long[] submissions) {
        Helper.checkNull(submissions, "submissions");

        // clear original submissions set first
        this.submissions.clear();

        // add each one to set
        for (int i = 0; i < submissions.length; i++) {
            Helper.checkLongPositive(submissions[i], "element of submissions");

            this.submissions.add(submissions[i]);
        }
    }

    /**
     * <p>Returns the submissions set formatted as an array of Long objects. If no submissions are set yet an
     * empty array will be returned.</p>
     *
     * <p>In version 1.1, it retrieves an array of submissions instead of a single one.</p>
     *
     * @return the array ids of associated submissions
     * @since 1.1
     */
    public Long[] getSubmissions() {
        return (Long[]) submissions.toArray(new Long[submissions.size()]);
    }

    /**
     * <p>Adds a submission id to be associated with this resource.</p>
     *
     * @param submission the id of the submission to be added
     * @throws IllegalArgumentException if the argument is null, or given submission is a wrapper of a value &lt;= 0
     * @since 1.1
     */
    public void addSubmission(Long submission) {
        Helper.checkLongPositive(submission, "submission");

        submissions.add(submission);
    }

    /**
     * <p>Removes the submission id from the submissions set associated with this resource.</p>
     *
     * @param submission the submission id to be removed
     * @throws IllegalArgumentException if the argument is null, or given submission is a wrapper of a value &lt;= 0
     * @since 1.1
     */
    public void removeSubmission(Long submission) {
        Helper.checkLongPositive(submission, "submission");

        submissions.remove(submission);
    }

    /**
     * <p>Checks whether given submission id is associated with this resource.</p>
     *
     * @param submission the submission id to check
     * @return true if given submission id is associated with this resource; false otherwise
     * @throws IllegalArgumentException if the argument is null, or given submission is a wrapper of a value &lt;= 0
     * @since 1.1
     */
    public boolean containsSubmission(Long submission) {
        Helper.checkLongPositive(submission, "submission");

        return submissions.contains(submission);
    }

    /**
     * <p>Checks whether there is some submission associated with this resource.</p>
     *
     * @return true if there is some submission associated with this resource; false otherwise
     * @since 1.1
     */
    public boolean hasSubmissions() {
        return !submissions.isEmpty();
    }

    /**
     * <p>Returns the number of submissions associated with this resource.</p>
     *
     * @return the number of submissions associated with this resource.
     * @since 1.1
     */
    public int countSubmissions() {
        return submissions.size();
    }

    /**
     * <p>Removes all submission ids associated with this resource.</p>
     * @since 1.1
     */
    public void clearSubmissions() {
        submissions.clear();
    }

    /**
     * <p>
     * Sets a property associated with the resource. If there is
     * already a value associated with the name, it will be replaced.
     * </p>
     *
     * @param name The name of the property to set.
     * @param value The property value to associate with the name.
     *
     * @throws IllegalArgumentException If name is null.
     */
    public void setProperty(String name, Object value) {
        Helper.checkNull(name, "name");
        // set it in the properties
        if (value == null) {
            this.properties.remove(name);
        } else {
            this.properties.put(name, value);
        }
    }

    /**
     * <p>
     * Tells if a property has been set for this Resource. Returns
     * true if there is an entry in the properties map for name.
     * </p>
     *
     * @param name The name of the property
     *
     * @return True if there is a value associated with the property.
     *
     * @throws IllegalArgumentException If name is null.
     */
    public boolean hasProperty(String name) {
        Helper.checkNull(name, "name");
        // check if the name exists
        return this.properties.containsKey(name);
    }

    /**
     * <p>
     * Gets the property value for the given name. Return the entry
     * in the properties map for name.
     * </p>
     *
     * @param name The name of the property.
     *
     * @return The value associated with the property name. Null if no value is associated with the property.
     *
     * @throws IllegalArgumentException If name is null.
     */
    public Object getProperty(String name) {
        Helper.checkNull(name, "name");
        // find it in the properties
        return this.properties.get(name);
    }

    /**
     * <p>
     * Gets all the properties that are associated with the
     * Resource. The returned map has all non-null String keys. The values can
     * be any type, but none of them will be null.
     * </p>
     *
     * @return The map of properties associated with the resource.
     */
    public Map getAllProperties() {
        // we need not to remove the null values or null keys explicitly, because
        // the properties is only accessed by the method: setProperties, and
        // the null key and null value is not allowed in that method.

        // copy the properties, since map is mutable
        return new HashMap(this.properties);
    }

    /**
     * <p>
     * Sets the user ID that the resource is associated with. The
     * value can be null or non-null. If non-null, it must be &gt; 0.
     * </p>
     *
     * @param userId The identifier of the user the resource is associate with (or null)
     *
     * @throws IllegalArgumentException If project is a wrapper of a value &lt= 0
     */
    public void setUserId(Long userId) {
        if (userId != null) {
            Helper.checkLongPositive(userId, "userId");
        }
        this.userId = userId;
    }

    /**
     * <p>
     * Gets the user ID that the resource is associated with. The return will either be null or &gt 0.
     * </p>
     *
     * @return The identifier of the user the resource is associated with
     */
    public Long getUserId() {
        return this.userId;
    }
}
