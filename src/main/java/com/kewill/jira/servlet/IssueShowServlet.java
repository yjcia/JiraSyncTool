package com.kewill.jira.servlet;

import com.kewill.jira.dao.IIssueDao;
import com.kewill.jira.dao.IssueDaoImpl;
import com.kewill.jira.model.Issue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by YanJun on 2016/7/4.
 */
public class IssueShowServlet extends IssueBaseServlet {
    private Logger issueShowLogger = LoggerFactory.getLogger(IssueShowServlet.class);

    @Override
    public void show(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        issueShowLogger.debug("invoke IssueShowServlet.show()");
        response.sendRedirect(request.getContextPath() + "/jsp/issues.jsp");
    }

    public void loadAll(HttpServletRequest request, HttpServletResponse response){
        issueShowLogger.debug("invoke IssueShowServlet.loadAll()");
        response.setCharacterEncoding("UTF-8");
        try {
            IIssueDao issueDao = new IssueDaoImpl();
            List<Issue> issueList = issueDao.loadAllIssues();
            PrintWriter out = response.getWriter();
            JSONArray jsonArray = new JSONArray();
            for(Issue issue:issueList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("keyId",issue.getKeyId());
                jsonObject.put("title",issue.getTitle());
                jsonObject.put("summary",issue.getSummary());
                jsonObject.put("type",issue.getType());
                jsonObject.put("status",issue.getStatus());
                jsonObject.put("resolution",issue.getResolution());
                jsonObject.put("assignee",issue.getAssignee());
                jsonObject.put("reporter",issue.getReporter());
                jsonObject.put("fixVersion",issue.getFixVersion());
                jsonArray.put(jsonObject);
            }
            out.write(jsonArray.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
