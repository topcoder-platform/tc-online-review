/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import com.topcoder.onlinereview.migration.dto.newschema.scorecard.Scorecard;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardGroup;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardQuestionNew;
import com.topcoder.onlinereview.migration.dto.newschema.scorecard.ScorecardSectionNew;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.QuestionTemplate;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScSectionGroup;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScorecardSectionOld;
import com.topcoder.onlinereview.migration.dto.oldschema.scorecard.ScorecardTemplate;

import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


/**
 * The Transformer which is used to transform scorecard data.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardTransformer {
    private static final String SCORECARD_ID_SEQ_NAME = "scorecard_id_seq";
    private static final String SCORECARD_GROUP_ID_SEQ_NAME = "scorecard_group_id_seq";
    private static final String SCORECARD_SECTION_ID_SEQ_NAME = "scorecard_section_id_seq";
    private static final String SCORECARD_QUESTION_ID_SEQ_NAME = "scorecard_question_id_seq";
    private static final float MIN_SCORE = (float) 75.0;
    private static final float MAX_SCORE = (float) 100.0;
    private static final String CREATE_USER = "Converter";
    private static final String MODIFY_USER = "Converter";
    private IDGenerator scorecardIdGenerator = null;
    private IDGenerator scorecardGroupIdGenerator = null;
    private IDGenerator scorecardSectionIdGenerator = null;
    private IDGenerator scorecardQuestionIdGenerator = null;
    
    private Properties templateIdProperties = null;
    private Log logger = LogFactory.getLog();

    /**
     * Creates a new ScorecardTransformer object.
     *
     * @throws Exception if error occurs while get idgenerator
     */
    public ScorecardTransformer() throws Exception {
        scorecardIdGenerator = IDGeneratorFactory.getIDGenerator(SCORECARD_ID_SEQ_NAME);
        scorecardGroupIdGenerator = IDGeneratorFactory.getIDGenerator(SCORECARD_GROUP_ID_SEQ_NAME);
        scorecardSectionIdGenerator = IDGeneratorFactory.getIDGenerator(SCORECARD_SECTION_ID_SEQ_NAME);
        scorecardQuestionIdGenerator = IDGeneratorFactory.getIDGenerator(SCORECARD_QUESTION_ID_SEQ_NAME);
        templateIdProperties = new Properties();
        if (MapUtil.propertieFile.exists()) {
        	InputStream input = new FileInputStream(MapUtil.propertieFile);
        	templateIdProperties.load(input);
        	input.close();
        }
    }

    /**
     * Transform ScorecardTemplate to Scorecard
     *
     * @param inputs the ScorecardTemplate data
     *
     * @return Scorecard data
     *
     * @throws Exception
     */
    public List transformScorecardTemplate(List inputs)
        throws Exception {
        List list = new ArrayList(inputs.size());

        long startTime = Util.startMain("transformScorecardTemplate");
        for (Iterator iter = inputs.iterator(); iter.hasNext();) {
            ScorecardTemplate input = (ScorecardTemplate) iter.next();
            logger.log(Level.DEBUG, "transformScorecardTemplate, template_id:" + input.getTemplateId() + " name:" + input.getTemplateName());
            
            Scorecard output = new Scorecard();
            // active 1, inactive 2 default_ind should used to indicate if it's active or not
            output.setScorecardStatusId(input.getStatusId());
            // 1 maps to 'Screening' and 2 maps to 'Review' same
            output.setScorecardTypeId(input.getScorecardType());
            
            // 1 maps to 'Component Design' and 2 maps to 'Component Development' same
            output.setProjectCategoryId(input.getProjectType());

            // Example Original Design Screening Scorecard v.6
            // Component Design Review Scorecard v1.1
            // Application Development Screening Scorecard
            String[] parsed = parseName(input.getTemplateName());
            output.setName(parsed[0]);
            output.setVersion(parsed[1]);
            output.setMinScore(MIN_SCORE);
            output.setMaxScore(MAX_SCORE);
            output.setCreateUser(CREATE_USER);
            output.setCreateDate(new Date());
            output.setModifyUser(MODIFY_USER);
            output.setModifyDate(new Date());

            int scorecardId = 0;
            int templateId = input.getTemplateId();
            String temp = templateIdProperties.getProperty(String.valueOf(templateId));
            if (temp != null && temp.trim().length() > 0) {
            	scorecardId = Integer.parseInt(temp);
            } else {
            	scorecardId = (int) scorecardIdGenerator.getNextID();
            	templateIdProperties.setProperty(String.valueOf(templateId), String.valueOf(scorecardId));
            }
        	output.setScorecardId(scorecardId);
        	logger.log(Level.DEBUG, "transformScorecardTemplate, template_id:" + templateId + " scorecard_id:" + scorecardId);
        	float totalWeight = getTotalWeight(input.getGroups());
            output.setGroups(transformScSectionGroup(output.getScorecardId(), input.getGroups(), totalWeight));
            list.add(output);
        }
        Util.logMainAction(inputs.size(), "transformScorecardTemplate", startTime);
        OutputStream out = new FileOutputStream(MapUtil.propertieFile);
        templateIdProperties.store(out, "template_id scorecard_id");
        out.close();
        return list;
    }

    private static float getTotalWeight(Collection groups) {
    	float totalWeight = 0;
        for (Iterator iter = groups.iterator(); iter.hasNext();) {
            ScSectionGroup input = (ScSectionGroup) iter.next();
            for (Iterator sectionIter = input.getSections().iterator(); sectionIter.hasNext();) {
                ScorecardSectionOld section = (ScorecardSectionOld) sectionIter.next();
                totalWeight += section.getSectionWeight();
            }
        }
        return totalWeight;
    }

    /**
     * Transform ScSectionGroup to ScorecardGroup
     *
     * @param scorecardId the scorecardId
     * @param inputs the input group data
     *
     * @return ScorecardGroup data
     *
     * @throws IDGenerationException
     */
    private Collection transformScSectionGroup(int scorecardId, Collection inputs, float totalWeight)
        throws IDGenerationException {
    	long startTime = Util.start("transformScSectionGroup");
        List list = new ArrayList(inputs.size());

        for (Iterator iter = inputs.iterator(); iter.hasNext();) {
            ScSectionGroup input = (ScSectionGroup) iter.next();
            ScorecardGroup output = new ScorecardGroup();

            // resolved by question_template and scorecard_section table
            output.setScorecardId(scorecardId);

            // calculated by distributing the section weights within a scorecard
            if ((int) totalWeight > 0) {
            	float weight = getGroupWeight(input.getSections()) * 100 / totalWeight;
            	output.setWeight(weight);
            } else {
            	output.setWeight(0);
            }
            output.setName(input.getGroupName());
            output.setSort(input.getGroupSeqLoc());
            output.setCreateUser(CREATE_USER);
            output.setCreateDate(new Date());
            output.setModifyUser(MODIFY_USER);
            output.setModifyDate(new Date());
            output.setScorecardGroupId((int) scorecardGroupIdGenerator.getNextID());
            output.setSections(transformScorecardSection(output.getScorecardGroupId(), input.getSections(), output.getWeight()));
            list.add(output);
        }

        Util.logAction(inputs.size(), "transformScSectionGroup", startTime);
        return list;
    }

    /**
     * Return total weigth in a group.
     * 
     * @param sections the sections
     * @return group weight
     */
    private static int getGroupWeight(Collection sections) {
    	int weight = 0;
    	for (Iterator iter = sections.iterator(); iter.hasNext();) {
    		weight += ((ScorecardSectionOld) iter.next()).getSectionWeight();
    	}
    	return weight;
    }
    
    /**
     * Transform ScorecardSectionOld to ScorecardSectionNew
     *
     * @param scorecardGroupId the scorecardGroupId
     * @param inputs the idGenerator data
     *
     * @return ScorecardSectionNew data
     *
     * @throws IDGenerationException
     */
    private Collection transformScorecardSection(int scorecardGroupId, Collection inputs, float groupWeight)
        throws IDGenerationException {
    	long startTime = Util.start("transformScorecardSection");
        List list = new ArrayList(inputs.size());

        for (Iterator iter = inputs.iterator(); iter.hasNext();) {
            ScorecardSectionOld input = (ScorecardSectionOld) iter.next();
            ScorecardSectionNew output = new ScorecardSectionNew();

            output.setScorecardGroupId(scorecardGroupId);

            // calculated by normalizing the weights in a group
            float weight = 0;
            if (((int) groupWeight) > 0) {
            	weight = (float) input.getSectionWeight() * 100 / groupWeight;
            }
            output.setWeight(weight);
            output.setName(input.getSectionName());
            output.setSort(input.getSectionSeqLoc());
            output.setCreateUser(CREATE_USER);
            output.setCreateDate(new Date());
            output.setModifyUser(MODIFY_USER);
            output.setModifyDate(new Date());
            output.setScorecardSectionId((int) scorecardSectionIdGenerator.getNextID());
            output.setQuestions(transformScorecardQuestion(output.getScorecardSectionId(), input.getQuestions()));
            list.add(output);
        }

        Util.logAction(inputs.size(), "transformScorecardSection", startTime);
        return list;
    }

    /**
     * Transform QuestionTemplate to ScorecardQuestionNew
     *
     * @param scorecardSectionId the scorecardSectionId
     * @param inputs the idGenerator data
     *
     * @return ScorecardQuestionNew data
     *
     * @throws IDGenerationException
     */
    private Collection transformScorecardQuestion(int scorecardSectionId, Collection inputs) throws IDGenerationException {
        List list = new ArrayList(inputs.size());
        long startTime = Util.start("transformScorecardQuestion");

        for (Iterator iter = inputs.iterator(); iter.hasNext();) {
            QuestionTemplate input = (QuestionTemplate) iter.next();
            ScorecardQuestionNew output = new ScorecardQuestionNew();

            output.setScorecardSectionId(scorecardSectionId);

            // parsed from question_text
            String[] texts = parseQuesetionText(input.getQuestionText());
            output.setDescription(texts[0]);
            output.setGuideline(texts[1]);

            // 2 maps to 'Scale 1-4', 3 maps to 'Test Case' and 4 maps to 'Yes/No'
            // 1, 'Scale (1-4)'
            // 3, 'Test Case'
            // 4, 'Yes/No'
            int questionType = input.getQuestionType();
            int scorecardQuestionTypeId = 1;
            switch (questionType) {
            case 2:
            	scorecardQuestionTypeId = 1;
            	break;
            case 3:
            	scorecardQuestionTypeId = 3;
            	break;
            case 4:
            	scorecardQuestionTypeId = 4;
            	break;
            }
            output.setScorecardQuestionTypeId(scorecardQuestionTypeId);
            output.setWeight(input.getQuestionWeight());
            output.setSort(input.getQuestionSecLoc());
            output.setUploadDocument(false);
            output.setUploadDocumentRequired(false);
            output.setCreateUser(CREATE_USER);
            output.setCreateDate(new Date());
            output.setModifyUser(MODIFY_USER);
            output.setModifyDate(new Date());
            
            // would like to reuse q_template_v_id            
            output.setScorecardQuestionId(input.getQTemplateVid());
            list.add(output);
        }

        Util.logAction(inputs.size(), "transformScorecardQuestion", startTime);
        return list;
    }

    /**
     * Psrse description and guideline from question text.
     *
     * @param input the question text
     *
     * @return description and guideline
     */
    private static String[] parseQuesetionText(String input) {
        if (input == null) {
            return new String[] { "", "" };
        }

        input = input.trim();

        int index = input.indexOf('.');

        if (index < 0) {
            return new String[] { input, "" };
        }

        return new String[] { input.substring(0, index + 1).trim(), input.substring(index + 1).trim() };
    }

    /**
     * Parse and return name and version.
     *
     * @param input the input name which contains name and version
     *
     * @return name, version array
     */
    private static String[] parseName(String input) {
        if (input == null) {
            return new String[] { "", "" };
        }

        input = input.trim();

        // Locate the last space
        int lastIndex = input.lastIndexOf(' ');

        if (lastIndex < 0) {
            return new String[] { input, "" };
        }

        if (isVersionPart(input.substring(lastIndex + 1))) {
            // the last part is version part
            return new String[] { input.substring(0, lastIndex), input.substring(lastIndex + 2) };
        }

        return new String[] { input, "" };
    }

    /**
     * Check if given input is version part.
     *
     * @param input the input
     *
     * @return true if it's version part, false otherwise
     */
    private static boolean isVersionPart(String input) {
        // version part is like v.1 v1.1 etc
        // size is large than 2
        if (input.length() <= 2) {
            // Not version part
            return false;
        }

        // should be start with v. or v1 
        if (input.charAt(0) != 'v') {
            // not version part
            return false;
        }

        for (int i = 1; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (!Character.isDigit(ch) && (ch != '.')) {
                return false;
            }
        }

        return true;
    }
}
