package com.kewill.jira.servlet;

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

    public void show(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("invoke IssueHomeServlet.show()");
        //RequestDispatcher dispatcher=request.getRequestDispatcher("/WEB-INF/jsp/home.jsp");
        //dispatcher.forward(request, response);
        response.sendRedirect(request.getContextPath() + "/jsp/home.jsp");
    }
}
