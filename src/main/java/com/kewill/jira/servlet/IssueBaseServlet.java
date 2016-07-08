package com.kewill.jira.servlet;

import com.kewill.jira.model.ActionDefinition;
import com.kewill.jira.util.JiraSyncUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by YanJun on 2016/7/4.
 */
public class IssueBaseServlet extends HttpServlet {

    private static List<ActionDefinition> actionMappingList;

    static{
        initActionMapping(actionMappingList);
    }

    private static void initActionMapping(List<ActionDefinition> actionMappingList) {

    }

    private String getPathInfo(HttpServletRequest request){
        return request.getServletPath().substring(1);
    }

    private String getActionName(String pathInfo){
        return pathInfo.substring(0,pathInfo.indexOf("."));
    }

    private String getMethodName(HttpServletRequest request){
        return request.getParameter("method");
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Invoke BaseServlet Do Post ...");
        String requestPath = getPathInfo(request);
        String requestType = request.getMethod();
        if(requestPath.contains("action")) {
            String actionName = getActionName(requestPath);
            String methodName = getMethodName(request);
            System.out.println("action --> " + actionName + ";" + " method --> " + methodName);
            try {
                ActionDefinition actionDefinition = getActionDefinitionByActionName(actionName,methodName,requestType);
                Class clazz = Class.forName(actionDefinition.getActionClassName());
                Object obj = clazz.newInstance();
                Method[] methodArr = obj.getClass().getDeclaredMethods();
                for (Method method : methodArr) {
                    if (method.getName().equals(methodName) &&
                            requestType.equals(actionDefinition.getType().toUpperCase())) {
                        method.invoke(obj, request, response);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    public void show(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{

    }

    private ActionDefinition getActionDefinitionByActionName(String actionName,String methodName,String requestType) {
        List<ActionDefinition> actionDefinitionList = JiraSyncUtil.analysisActionXml();
        for(ActionDefinition actionDefinition:actionDefinitionList){
            if(actionDefinition.getActionName().equals(actionName)
                    && actionDefinition.getActionMethodName().equals(methodName)
                    && actionDefinition.getType().equals(requestType.toLowerCase())){
                return actionDefinition;
            }
        }
        return null;
    }
}
