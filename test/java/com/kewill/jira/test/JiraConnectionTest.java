package com.kewill.jira.test;

import com.kewill.jira.dao.IIssueDao;
import com.kewill.jira.dao.IssueDaoImpl;
import com.kewill.jira.model.Issue;
import com.kewill.jira.util.JiraSyncUtil;
import com.kewill.jira.util.PropertyUtil;
import org.junit.Test;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by YanJun on 2016/6/30.
 */
public class JiraConnectionTest {

    @Test
    public void testGetIssueXmlByUrl(){
        String queryId = "22400";
        String userName = "yidong.jin";
        String password = "jira2012";
        JiraSyncUtil.init(queryId,userName,password);
        JiraSyncUtil.analysisIssueXml();

    }

    @Test
    public void testDateFormatter(){
        try {
            String stringDate = "Tue, 28 Jun 2016 18:00:02 +0800";
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.US);
            Date date = sdf.parse(stringDate);
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIssueAdd(){
        IIssueDao issueDao = new IssueDaoImpl();

        //issueDao.insertIssue()
    }

    @Test
    public void testPropUtil(){
        System.out.println(PropertyUtil.getPropValue("url"));
    }

    @Test
    public void testInsert(){
        String queryId = "22400";
        String userName = "yidong.jin";
        String password = "jira2012";
        JiraSyncUtil.init(queryId,userName,password);
        List<Issue> issueList = JiraSyncUtil.analysisIssueXml();
        IIssueDao issueDao = new IssueDaoImpl();
        //Issue issue = issueList.get(0);
        for(Issue issue:issueList){
            issueDao.insertIssue(issue);
            System.out.println("cloud jira sync --> " + issue.getKeyId() + " " + issue.getTitle());
        }
    }

}
