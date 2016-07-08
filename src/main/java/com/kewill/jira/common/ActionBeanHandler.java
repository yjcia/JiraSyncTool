package com.kewill.jira.common;

import com.kewill.jira.model.ActionDefinition;
import com.kewill.jira.model.Issue;
import com.kewill.jira.util.DateUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by YanJun on 2016/6/30.
 */
public class ActionBeanHandler extends DefaultHandler {

    List<ActionDefinition> actionDefinitionList = new ArrayList<ActionDefinition>();
    private String currentTag;
    private ActionDefinition actionDefinition;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        currentTag = qName;
        if("action".equals(currentTag)) {
            actionDefinition = new ActionDefinition();
            String actionName = attributes.getValue("name");
            String methodName = attributes.getValue("method");
            String actionClassName = attributes.getValue("class");
            String actionType = attributes.getValue("type");
            actionDefinition.setActionName(actionName);
            actionDefinition.setActionMethodName(methodName);
            actionDefinition.setActionClassName(actionClassName);
            actionDefinition.setType(actionType);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if("action".equals(qName)) {
            actionDefinitionList.add(actionDefinition);
            actionDefinition = null;
        }
    }

    public List<ActionDefinition> getActionDefinitionList() {
        return actionDefinitionList;
    }

}
