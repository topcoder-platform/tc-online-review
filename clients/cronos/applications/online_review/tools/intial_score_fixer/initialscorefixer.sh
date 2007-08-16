#!/bin/bash

CP=dist/base_exception.jar:dist/class_associations.jar:dist/command_line_utility.jar:\
	dist/configuration_manager.jar:dist/data_validation.jar:dist/database_abstraction.jar:\
	dist/db_connection_factory.jar:dist/id_generator.jar:dist/logging_wrapper.jar:\
	dist/object_factory.jar:dist/review_data_structure.jar:dist/review_management.jar:\
	dist/review_management_persistence.jar:dist/review_score_calculator.jar:\
	dist/scorecard_data_structure.jar:dist/scorecard_management.jar:\
	dist/scorecard_management_persistence.jar:dist/search_builder.jar:\
	dist/simple_cache.jar:dist/typesafe_enum.jar:dist/weighted_calculator.jar:\
	dist/ifx-jdbc.jar:build/classes:conf

MAIN_CLASS=com.topcoder.management.review.scorefixer.InitialScoreFixer

java -CP $CP $MAIN_CLASS $*
