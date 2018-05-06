/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>
 * The DatabaseAbstractor class permits a factory type pattern to be used for
 * creation of CustomResultSets. This class is simply a storage facility for a
 * mapper and an on-demand mapper, both of which it passes to any
 * CustomResultSets that is creates.
 * </p>
 * <p>
 * In version 1.1, instance of OnDemandMapper is added to covert the mapping
 * when original or mapped value is not of the desired type to get.
 * </p>
 * <p>
 * <strong>Changes in 2.0:</strong>
 * <ol>
 * <li>Throws IllegalArgumentException instead of NullPointerException when
 * argument is null.</li>
 * </ol>
 * </p>
 * <p>
 * <strong>Thread Safety:</strong> This class is mutable, and not thread-safe.
 * </p>
 * <p>
 * Typical usage:<br>
 *
 * <pre>
 *    ResultSet rs = &lt;&lt;Get Database ResultSet&gt;&gt;;
 *    DatabaseAbstractor databaseAbstractor = new DatabaseAbstractor();
 *    Mapper mapper = &lt;&lt;Create Mapper&gt;&gt;
 *    OnDemandMapper onDemandMapper = OnDemandMapper.createDefaultOnDemandMapper();
 *    databaseAbstractor.setMapper(mapper);
 *    databaseAbstractor.setOnDemandMapper(onDemandMapper);
 *    CustomResultSet customRS = databaseAbstractor.convertResultSet(resultSet);
 *    while(customRS.next()){
 *        customRS.getLong(&quot;columnName&quot;);
 *        customRS.getString(&quot;columnName2&quot;);
 *    }
 * </pre>
 *
 * </p>
 *
 * @author argolite, aubergineanode, saarixx, WishingBone, justforplay,
 *         suhugo
 * @version 2.0
 * @since 1.0
 */
public class DatabaseAbstractor {

    /**
     * <p>
     * The mapper that is applied when a CustomResultSet is created using the
     * DatabaseAbstractor. This field is set in the constructor, is mutable, and
     * can be null. The value can be manipulated through the get/setMapper
     * methods, and this field is also used in the convertResultSet methods.
     * </p>
     */
    private Mapper dataTypeMapper;

    /**
     * The mapper for doing on-demand conversions of values in the
     * CustomResultSet. This field is provided to any CustomResultSet created by
     * this DatabaseAbstractor. This field is initialized in the constructor,
     * can be accessed by the get/setOnDemandMapper methods, and can be null.
     *
     * @since 1.1
     */
    private OnDemandMapper onDemandMapper;

    /**
     * <p>
     * Creates a new DatabaseAbstractor which does no mapping.
     * </p>
     */
    public DatabaseAbstractor() {
        // do nothing
    }

    /**
     * <p>
     * Create a new DatabaseAbstractor that uses the given mapper.
     * </p>
     *
     * @param mapper
     *            The provider of explicit mapper - may be null
     */
    public DatabaseAbstractor(Mapper mapper) {
        this(mapper, null);
    }

    /**
     * Creates a new DatabaseAbstractor that uses the given onDemandMapper for
     * doing dynamic conversion of data and the given mapper for explicit column
     * value conversion.
     *
     * @param mapper
     *            The explicit mappers to apply. Can be null
     * @param onDemandMapper
     *            the on-demand conversion provider. Can be null
     * @since 1.1
     */
    public DatabaseAbstractor(Mapper mapper, OnDemandMapper onDemandMapper) {
        dataTypeMapper = mapper;
        this.onDemandMapper = onDemandMapper;
    }

    /**
     * <p>
     * Convert result set to a CustomResultSet. Create a new CustomResultset
     * using resultSet, and the dataTypeMapper and onDemandMapper fields.
     * </p>
     * <p>
     * In version1.1, pre-initialized OnDemandMapper is used to do dynamic
     * conversion.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter rs to resultSet to meet TopCoder standards.</li>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * resultSet is null.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            the result set to be converted
     * @return the custom result set
     * @throws IllegalArgumentException
     *             If resultSet is null.
     * @throws IllegalMappingException
     *             when mapping is illegal
     * @throws SQLException
     *             if SQL exception takes place
     */
    public CustomResultSet convertResultSet(ResultSet resultSet) throws IllegalMappingException, SQLException {
        // no need to check dataTypeMapper.
        return new CustomResultSet(resultSet, dataTypeMapper, onDemandMapper);
    }

    /**
     * <p>
     * Convert result set with specified mapper. Create a new CustomResultSet
     * using the resultSet and mapper parameters and the onDemandMapper field.
     * </p>
     * <p>
     * In version1.1, pre-initialized OnDemandMapper is used to do dynamic
     * conversion.
     * </p>
     * <p>
     * <strong>Changes in 2.0:</strong>
     * <ol>
     * <li>Renamed parameter rs to resultSet to meet TopCoder standards.</li>
     * <li>Throws IllegalArgumentException instead of NullPointerException when
     * resultSet is null.</li>
     * </ol>
     * </p>
     *
     * @param resultSet
     *            the result set to be converted
     * @param mapper
     *            the mapper specified
     * @return the custom result set
     * @throws IllegalArgumentException
     *             If resultSet is null.
     * @throws IllegalMappingException
     *             when mapping is illegal
     * @throws SQLException
     *             if SQL exception takes place
     */
    public CustomResultSet convertResultSet(ResultSet resultSet, Mapper mapper) throws IllegalMappingException,
            SQLException {
        return new CustomResultSet(resultSet, mapper, onDemandMapper);
    }

    /**
     * Get the mapper for explicit conversions. Null indicates that no specific
     * conversions will be done.
     *
     * @return the mapper
     */
    public Mapper getMapper() {
        return dataTypeMapper;
    }

    /**
     * Set the explicit mapping mapper to the given value. Null indicates that
     * no mapper should be used in the future.
     *
     * @param mapper
     *            The mapper, which may be null
     */
    public void setMapper(Mapper mapper) {
        dataTypeMapper = mapper;
    }

    /**
     * Set the on-demand mapping provider to the given value. Null indicates
     * that created CustomResultSets should not support on-demand conversion.
     *
     * @param onDemandMapper
     *            The mapper, which may be null
     * @since 1.1
     */
    public void setOnDemandMapper(OnDemandMapper onDemandMapper) {
        this.onDemandMapper = onDemandMapper;
    }

    /**
     * Gets the on-demand mapping provider. A return of null indicates that no
     * on-demand mapping will be done in created result sets.
     *
     * @return The on demand mapper
     * @since 1.1
     */
    public OnDemandMapper getOnDemandMapper() {
        return onDemandMapper;
    }
}
