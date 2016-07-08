package com.kewill.jira.common;

import com.kewill.jira.model.Issue;
import com.kewill.jira.util.DateUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.naming.directory.Attribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by YanJun on 2016/6/30.
 */
public class IssueBeanHandler extends DefaultHandler {


    List<Issue> issueList = new ArrayList<Issue>();
    List<String> componentList = new ArrayList<String>();
    StringBuilder descriptionValue = new StringBuilder();
    StringBuilder summaryValue = new StringBuilder();
    StringBuilder titleValue = new StringBuilder();
    String[] stringFieldArr = new String[]{
            "link","project","key","type","status","resolution",
            "assignee","reporter","fixVersion"};
    String[] integerFieldArr = new String[]{"votes","watches"};
    String[] gmtDateFieldArr = new String[]{"created","updated","resolved"};
    private String currentTag;
    private Issue issue;

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentTag = qName;
        if("item".equals(currentTag)) {
            issue = new Issue();
        }
        if("project".equals(currentTag)){
            String projectId = attributes.getValue("id");
            issue.setProjectId(Integer.parseInt(projectId));
        }
        if("key".equals(currentTag)){
            String keyId = attributes.getValue("id");
            issue.setKeyId(new Long(keyId));
        }


    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        try {
            if (issue != null) {
                Method[] methodsArr = issue.getClass().getDeclaredMethods();
                for (Method method : methodsArr) {
                    String methodName = method.getName();
                    String methodFieldName = methodName.substring(3,4).toLowerCase()+methodName.substring(4);
                    for (String strFieldName : stringFieldArr) {
                        if (methodName.startsWith("set") && strFieldName.equals(methodFieldName)
                                && strFieldName.equals(currentTag)) {
                            String inputValue = new String(ch, start, length);
                            method.invoke(issue, inputValue);
                        }
                    }
                    for (String integerFieldName : integerFieldArr) {
                        if (methodName.startsWith("set") && integerFieldName.equals(methodFieldName)
                                && integerFieldName.equals(currentTag)) {
                            Integer inputValue = Integer.parseInt(new String(ch, start, length));
                            method.invoke(issue, inputValue);
                        }
                    }
                    for (String gmtDateFieldName : gmtDateFieldArr) {
                        if (methodName.startsWith("set") && gmtDateFieldName.equals(methodFieldName)
                                && gmtDateFieldName.equals(currentTag)) {
                            String inputValue = new String(ch, start, length);
                            Date dateValue = DateUtil.dateFormatter(inputValue);
                            method.invoke(issue, dateValue);
                        }
                    }
                }
                if("description".equals(currentTag)) {
                    String descValue = new String(ch,start,length);
                    descriptionValue.append(descValue);
                }
                if("summary".equals(currentTag)) {
                    String summary = new String(ch,start,length);
                    summaryValue.append(summary);
                }
                if("title".equals(currentTag)) {
                    String title = new String(ch,start,length);
                    titleValue.append(title);
                }
                if("component".equals(currentTag)) {
                    String componentValue = new String(ch,start,length);
                    componentList.add(componentValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if("item".equals(qName)) {
            issueList.add(issue);
            reset();
        }
        if("component".equals(qName)){
            issue.setComponentList(componentList);
        }
        if("description".equals(qName)){
            if(!descriptionValue.toString().equals("")){
                issue.setDescription(descriptionValue.toString());
            }
        }
        if("summary".equals(qName)){
            if(!summaryValue.toString().equals("")){
                issue.setSummary(summaryValue.toString());
            }
        }
        if("title".equals(qName)){
            if(!titleValue.toString().equals("")){
                issue.setTitle(titleValue.toString());
            }
        }
        currentTag = null;

    }

    public List<Issue> getIssuesList() {
        return issueList;
    }

    public void reset(){
        issue = null;
        componentList = new ArrayList<String>();
        descriptionValue = new StringBuilder();
        summaryValue = new StringBuilder();
        titleValue = new StringBuilder();
    }

}
