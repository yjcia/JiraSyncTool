package com.kewill.jira.dao;

import com.kewill.jira.model.Issue;
import com.kewill.jira.util.DateUtil;
import com.kewill.jira.util.DbConnectionUtil;
import com.kewill.jira.util.JiraSyncUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by YanJun on 2016/6/30.
 */
public class IssueDaoImpl implements IIssueDao {
    private DbConnectionUtil dbConnectionPool = DbConnectionUtil.getInstance();
    public int insertIssue(Issue issue) {
        Connection conn  = dbConnectionPool.getConnection();

        String sql = "INSERT INTO jira.issue " +
                "(id, title, link, project_id, project_value, description, key_value, summary, type, " +
                "status, resolution, assignee, reporter, created, updated, resolved, fix_version, component, " +
                "votes, watches,priority,custom_fields) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        int updateCount = 0;
        try {
            conn.setAutoCommit(false);
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setLong(1,issue.getKeyId());
            psmt.setString(2,issue.getTitle());
            psmt.setString(3,issue.getLink());
            psmt.setInt(4,issue.getProjectId());
            psmt.setString(5,issue.getProject());
            psmt.setString(6,issue.getDescription());
            psmt.setString(7,issue.getKey());
            psmt.setString(8,issue.getSummary());
            psmt.setString(9,issue.getType());
            psmt.setString(10,issue.getStatus());
            psmt.setString(11,issue.getResolution());
            psmt.setString(12,issue.getAssignee());
            psmt.setString(13,issue.getReporter());
            psmt.setTimestamp(14, DateUtil.toSqlDateTime(issue.getCreated()));
            psmt.setTimestamp(15,DateUtil.toSqlDateTime(issue.getUpdated()));
            psmt.setTimestamp(16,DateUtil.toSqlDateTime(issue.getResolved()));
            psmt.setString(17,issue.getFixVersion());
            psmt.setString(18, JiraSyncUtil.getComponentStr(issue));
            psmt.setInt(19,issue.getVotes());
            psmt.setInt(20,issue.getWatches());
            psmt.setString(21,issue.getPriority());
            psmt.setString(22,JiraSyncUtil.getCustomFieldsStr(issue));
            updateCount = psmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }finally {
            closeConnection(conn);
        }

        return updateCount;
    }

    public int deleteIssueById(Long keyId) {
        return 0;
    }

    public int updateIssueById(Long keyId, Issue issue) {
        return 0;
    }

    public Issue loadIssueById(Long keyId) {
        Issue issue = new Issue();
        Connection conn  = dbConnectionPool.getConnection();
        String sql = "select id,title,link,project_id,project_value,description,key_value,summary,type, " +
                " status,resolution,assignee,reporter,created,updated,resolved,fix_version,component," +
                " votes,watches,priority,custom_fields from issue where id = ?";
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setLong(1,keyId);
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                issue.setKeyId(rs.getLong(1));
                issue.setTitle(rs.getString(2));
                issue.setLink(rs.getString(3));
                issue.setProjectId(rs.getInt(4));
                issue.setProject(rs.getString(5));
                issue.setDescription(rs.getString(6));
                issue.setKey(rs.getString(7));
                issue.setSummary(rs.getString(8));
                issue.setType(rs.getString(9));
                issue.setStatus(rs.getString(10));
                issue.setResolution(rs.getString(11));
                issue.setAssignee(rs.getString(12));
                issue.setReporter(rs.getString(13));
                issue.setCreated(rs.getTimestamp(14));
                issue.setUpdated(rs.getTimestamp(15));
                issue.setResolved(rs.getTimestamp(16));
                issue.setFixVersion(rs.getString(17));
                issue.setComponentList(Arrays.asList(rs.getString(18).split(" ")));
                issue.setVotes(rs.getInt(19));
                issue.setWatches(rs.getInt(20));
                issue.setPriority(rs.getString(21));
                issue.setCustomFieldList(JiraSyncUtil.getCustomFieldListFromStr(rs.getString(22)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn);
        }
        return issue;
    }

    public List<Issue> loadAllIssues() {
        List<Issue> issueList = new ArrayList<Issue>();
        Connection conn  = dbConnectionPool.getConnection();
        String sql = "select id,title,link,project_id,project_value,description,key_value,summary,type, " +
                " status,resolution,assignee,reporter,created,updated,resolved,fix_version,component," +
                " votes,watches,priority,custom_fields from issue";
        try {
            PreparedStatement psmt = conn.prepareStatement(sql);
            ResultSet rs = psmt.executeQuery();
            while(rs.next()){
                Issue issue = new Issue();
                issue.setKeyId(rs.getLong(1));
                issue.setTitle(rs.getString(2));
                issue.setLink(rs.getString(3));
                issue.setProjectId(rs.getInt(4));
                issue.setProject(rs.getString(5));
                issue.setDescription(rs.getString(6));
                issue.setKey(rs.getString(7));
                issue.setSummary(rs.getString(8));
                issue.setType(rs.getString(9));
                issue.setStatus(rs.getString(10));
                issue.setResolution(rs.getString(11));
                issue.setAssignee(rs.getString(12));
                issue.setReporter(rs.getString(13));
                issue.setCreated(rs.getTimestamp(14));
                issue.setUpdated(rs.getTimestamp(15));
                issue.setResolved(rs.getTimestamp(16));
                issue.setFixVersion(rs.getString(17));
                issue.setComponentList(Arrays.asList(rs.getString(18).split(" ")));
                issue.setVotes(rs.getInt(19));
                issue.setWatches(rs.getInt(20));
                issue.setPriority(rs.getString(21));
                issue.setCustomFieldList(JiraSyncUtil.getCustomFieldListFromStr(rs.getString(22)));
                issueList.add(issue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            closeConnection(conn);
        }
        return issueList;
    }

    public List<Issue> loadIssuesByCondition(String condition) {
        return null;
    }

    private void closeConnection(Connection conn){
        if(conn != null){
            dbConnectionPool.releaseConnection(conn);
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
