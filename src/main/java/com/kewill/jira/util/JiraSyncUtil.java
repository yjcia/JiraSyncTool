package com.kewill.jira.util;

import com.kewill.jira.common.ActionBeanHandler;
import com.kewill.jira.common.IssueBeanHandler;
import com.kewill.jira.common.JiraAttribute;
import com.kewill.jira.model.ActionDefinition;
import com.kewill.jira.model.Issue;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by YanJun on 2016/6/30.
 */
public class JiraSyncUtil {

    private static String queryId;
    private static String osUserName;
    private static String osPwd;
    private static SAXParserFactory factory;
    private static SAXParser parser;
    private static XMLReader reader;

    static{
        try {
            factory = SAXParserFactory.newInstance();
            parser = factory.newSAXParser();
            reader = parser.getXMLReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
    public static void init(String query_Id,String os_UserName,String os_Pwd){
        queryId = query_Id;
        osUserName = os_UserName;
        osPwd = os_Pwd;
    }

    public static String getFullQueryUrl(){
        StringBuilder url = new StringBuilder();
        url.append(JiraAttribute.QUERY_URL);
        url.append(JiraAttribute.SEARCH_REQUEST.replace(JiraAttribute.JIRA_QUERY_ID,queryId));
        url.append(JiraAttribute.TEMP_MAX+"="+JiraAttribute.TEMP_MAX_VALUE+"&");
        url.append(JiraAttribute.OS_USERNAME+"="+osUserName+"&");
        url.append(JiraAttribute.OS_PASSWORD+"="+osPwd);
        return url.toString();
    }

    public static InputStream getJiraQueryStream(){
        InputStream is = null;
        CloseableHttpClient httpClient = CloseableHttpClientUtil.createSSLClientDefault();
        String requestUrl = JiraSyncUtil.getFullQueryUrl();
        System.out.println(requestUrl);
        try {
            HttpGet httpGet = new HttpGet(requestUrl);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public static List<Issue> analysisIssueXml(){
        List<Issue> issueList = new ArrayList<Issue>();
        try {

            IssueBeanHandler handler = new IssueBeanHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(getJiraQueryStream()));
            issueList = handler.getIssuesList();
            //System.out.println(issueList);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return issueList;

    }

    public static List<ActionDefinition> analysisActionXml(){
        List<ActionDefinition> actionDefinitionList = new ArrayList<ActionDefinition>();
        try {
            ActionBeanHandler handler = new ActionBeanHandler();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(JiraSyncUtil.class.getClassLoader().getResourceAsStream("action.xml")));
            actionDefinitionList = handler.getActionDefinitionList();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return actionDefinitionList;

    }

    public static String getComponentStr(Issue issue){
        List<String> componentList = issue.getComponentList();
        String component = "";
        if(componentList != null && componentList.size() > 0){
            for(String comp:componentList){
                component += (comp + " ");
            }
        }
        return component;
    }


    public static String getCustomFieldsStr(Issue issue) {
        List<Map<String,String>> customFieldMapList = issue.getCustomFieldList();
        StringBuilder customFieldBuilder = new StringBuilder();
        for(Map<String,String> customMap : customFieldMapList){
            Set<String> keySet = customMap.keySet();
            for(String key:keySet){
                customFieldBuilder.append(key + "->" + customMap.get(key) + "&");
            }
        }
        return customFieldBuilder.toString();
    }

    public static String getCustomFieldFromIssue(Issue issue,String customFieldKey){
        List<Map<String,String>> customFieldMapList = issue.getCustomFieldList();
        String customFieldValue = "";
        for(Map<String,String> customMap : customFieldMapList){
            Set<String> keySet = customMap.keySet();
            for(String key:keySet){
                if(customFieldKey.equals(key)){
                    customFieldValue = customMap.get(customFieldKey);
                }
                break;
            }

        }
        return customFieldValue;
    }

    public static List<Map<String,String>> getCustomFieldListFromStr(String customeFieldsStr){
        List<Map<String,String>> customFieldList = new ArrayList<Map<String, String>>();
        Map<String,String> customMap = new HashMap<String, String>();
        String[] customFieldArr = customeFieldsStr.split("&");
        if(customFieldArr.length > 1){
            for(String customMapper:customFieldArr){
                String[] mapper = customMapper.split("->");
                if(mapper.length > 1){
                    customMap.put(mapper[0],mapper[1]);
                    customFieldList.add(customMap);
                }
            }
        }
        return customFieldList;
    }
}
