package com.kewill.jira.test;

import com.kewill.jira.model.ActionDefinition;
import com.kewill.jira.util.JiraSyncUtil;
import org.junit.Test;

import java.util.List;

/**
 * Created by YanJun on 2016/7/6.
 */
public class ActionTest {

    @Test
    public void testAnalysisActionXml(){
        List<ActionDefinition> actionDefinitionList = JiraSyncUtil.analysisActionXml();
        System.out.println(actionDefinitionList);
    }
}
