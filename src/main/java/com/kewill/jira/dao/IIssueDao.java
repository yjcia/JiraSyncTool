package com.kewill.jira.dao;

import com.kewill.jira.model.Issue;

import java.util.List;

/**
 * Created by YanJun on 2016/6/30.
 */
public interface IIssueDao {
    int insertIssue(Issue issue);
    int deleteIssueById(Long keyId);
    int updateIssueById(Long keyId,Issue issue);
    Issue loadIssueById(Long keyId);
    List<Issue> loadAllIssues();
    List<Issue> loadIssuesByCondition(String condition);
}
