package com.kewill.jira.servlet;

import com.kewill.jira.common.TARGenerator;
import com.kewill.jira.dao.IIssueDao;
import com.kewill.jira.dao.IssueDaoImpl;
import com.kewill.jira.model.Issue;
import com.kewill.jira.util.ServletUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by YanJun on 2016/7/4.
 */
public class IssueTarDownloadServlet extends IssueBaseServlet {


    public void downloadTar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("invoke IssueShowServlet.downloadTar()");
        String issueId = request.getParameter("id");
        IIssueDao issueDao = new IssueDaoImpl();
        Issue issue = issueDao.loadIssueById(new Long(issueId));
        TARGenerator tar = new TARGenerator(issue);
        try {
            File newTarFile = tar.doExecute();
            ServletUtil.doDownload(newTarFile,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
