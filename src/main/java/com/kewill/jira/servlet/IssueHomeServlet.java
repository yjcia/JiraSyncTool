package com.kewill.jira.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by YanJun on 2016/7/4.
 */
public class IssueHomeServlet extends IssueBaseServlet {

    private Logger issueHomeLog = LoggerFactory.getLogger(IssueHomeServlet.class);
    public void show(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        issueHomeLog.debug("invoke IssueHomeServlet.show()");
        response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");
    }
}
