package com.kewill.jira.common;


import com.kewill.jira.model.Issue;
import com.kewill.jira.util.ExcelUtil;
import com.kewill.jira.util.JiraSyncUtil;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: TARGenerator</p>
 * <p>Description: TARGenerator</p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: KEWILL-IPACS Pte Ltd</p>
 *
 * @author <hongsen.lu>
 * @date 2012-07-18
 */
public class TARGenerator {

    private final Issue issue;

    public static final String TEMPLATES_DIR = "J:\\Code\\JiraSyncTool\\src\\main\\resources\\";
    public static final String TAR_DIR = "J:\\Code\\JiraSyncTool\\tar";
    private final int TAR_SHEET_INDEX = 0;
    private final int COMMIT_LIST_SHEET_INDEX = 1;

    private final int COMMON_FIELD_COLUMN_INDEX = 6;
    private final int LARGE_FIELD_COLUMN_INDEX = 0;

    private final int TAR_NUMBER_FIELD_ROW_INDEX = 0;
    private final int INTERNAL_NUMBER_FIELD_ROW_INDEX = 1;
    private final int ORIGINATOR_FIELD_ROW_INDEX = 2;
    private final int DATE_SUBMITTED_FIELD_ROW_INDEX = 3;
    private final int FUNCTION_TITLE_FIELD_ROW_INDEX = 6;
    private final int PROBLEM_DETAILS_FIELD_ROW_INDEX = 10;
    private final int PROBLEM_ANALYSIS_FIELD_ROW_INDEX = 25;
    private final int RESOLUTION_FIELD_ROW_INDEX = 27;
    private final int DEVELOPER_FIELD_ROW_INDEX = 30;
    private final int QA_FIELD_ROW_INDEX = 31;
    private final int FIRST_TESTER_FIELD_ROW_INDEX = 38;
    private final int SECOND_TESTER_FIELD_ROW_INDEX = 39;

    private final int BRANCH_INVOLVED_COLUMN_INDEX = 13;
    private final int PUBLISH_DONE_COLUMN_INDEX = 21;
    private final int QA_DONE_COLUMN_INDEX = 22;
    private final int DBUPDATE_NO_COLUMN_INDEX = 2;
    private final int SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX = 3;
    private final int DBUPDATE_NO_TRUNK_ROW_INDEX = 8;
    private final int DBUPDATE_NO_RELEASE_ROW_INDEX = 9;

    private final String ISSUESTATUS_OPEN = "Open";
    private final String ISSUESTATUS_REOPENED = "Reopened";
    private final String ISSUESTATUS_RESOLVED = "Resolved";
    private final String ISSUESTATUS_IN_PROGRESS = "In Progress";
    private final String ISSUESTATUS_CLOSED = "Closed";
    private final String ISSUESTATUS_TESTED_IN_TRUNK = "Tested in Trunk";
    private final String ISSUESTATUS_PUBLISHED_TO_BRANCH = "Published to Branch";
    private final String ISSUESTATUS_TESTED_IN_BRANCH = "Tested in Branch";

    File tarDir = new File(TAR_DIR);

    public TARGenerator(Issue issue) {
        this.issue = issue;
        if (!tarDir.exists()) {
            tarDir.mkdirs();
        }
    }

    public File doExecute() throws Exception {
        File tarTemplate = new File(TEMPLATES_DIR.concat(File.separator).concat("TAR_Template.xls"));
        File tar =
            new File(TAR_DIR.concat(File.separator).concat("TAR_".concat(issue.getKey().split("-")[1])).concat(".xls"));
        if (!tarTemplate.exists()) {

        }

        if (tar.exists()) {
            tar.delete();
        }

        InputStream templateIn = new FileInputStream(tarTemplate);
        OutputStream tarOut = new FileOutputStream(tar);

        Workbook tarWB = new HSSFWorkbook(templateIn);

        Sheet tarSheet = tarWB.getSheetAt(TAR_SHEET_INDEX);
        Sheet commitListSheet = tarWB.getSheetAt(COMMIT_LIST_SHEET_INDEX);

        // Fill 'TAR Number' cell's value and add a hyperlink to it
        ExcelUtil.fillCellWithValue(tarSheet, TAR_NUMBER_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, issue.getKey());
        //Hyperlink issueAddr = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
        //tarSheet.getRow(TAR_NUMBER_FIELD_ROW_INDEX).getCell(COMMON_FIELD_COLUMN_INDEX).setHyperlink(issueAddr);

        // Fill 'Internal Number' cell's value
        Object fpnoObj = JiraSyncUtil.getCustomFieldFromIssue(issue,"FP No");
        Object customerRefNo = JiraSyncUtil.getCustomFieldFromIssue(issue,"Customer Ref No");
        String nos =
            (fpnoObj == null ? "" : fpnoObj.toString()) + " " + (customerRefNo == null ? "" : customerRefNo.toString());
        ExcelUtil.fillCellWithValue(tarSheet, INTERNAL_NUMBER_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, nos);

        // Fill 'Name of Originator'cell's value
        Object customerObj = JiraSyncUtil.getCustomFieldFromIssue(issue,"Customer");
        ExcelUtil.fillCellWithValue(tarSheet, ORIGINATOR_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, customerObj);

        // Fill 'Date Submitted' cell's value
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        ExcelUtil.fillCellWithValue(
            tarSheet, DATE_SUBMITTED_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, sdf.format(new Date()));

        // Fill 'Function Title/Report Title' cell's value
        //List<String> components = issue.getComponentList();
        String componentDesc = JiraSyncUtil.getComponentStr(issue);
        Object menuPathObj =  JiraSyncUtil.getCustomFieldFromIssue(issue,"Menu Path");
        String menuPath = "";
        if (menuPathObj != null) {
            menuPath = menuPathObj.toString().concat("\r\n");
        }
        ExcelUtil.fillCellWithValue(
            tarSheet, FUNCTION_TITLE_FIELD_ROW_INDEX, LARGE_FIELD_COLUMN_INDEX, menuPath + componentDesc);

        // Fill 'Reasons for Change' cell's value
        ExcelUtil.fillCellWithValue(
            tarSheet, PROBLEM_DETAILS_FIELD_ROW_INDEX, LARGE_FIELD_COLUMN_INDEX,
            issue.getSummary() + "\r\n" + issue.getDescription());

        // Fill 'Findings/Analysis' cell's value
        Object bugCause = JiraSyncUtil.getCustomFieldFromIssue(issue,"Bug Cause");
        ExcelUtil.fillCellWithValue(tarSheet, PROBLEM_ANALYSIS_FIELD_ROW_INDEX, LARGE_FIELD_COLUMN_INDEX, bugCause);

        // Fill 'Resolution/Recommendation' cell's value
        Object releaseNotes = JiraSyncUtil.getCustomFieldFromIssue(issue,"Release Notes");
        ExcelUtil.fillCellWithValue(tarSheet, RESOLUTION_FIELD_ROW_INDEX, LARGE_FIELD_COLUMN_INDEX, releaseNotes);

        // Fill 'Developer' cell's value
        ExcelUtil.fillCellWithValue(
            tarSheet, DEVELOPER_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, issue.getAssignee());

        // Fill 'QA' cell's value
        String qaName = JiraSyncUtil.getCustomFieldFromIssue(issue,"QA");
        ExcelUtil.fillCellWithValue(tarSheet, QA_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, qaName);
        // Fill '2nd Tester' cell's value
        ExcelUtil.fillCellWithValue(tarSheet, SECOND_TESTER_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX, qaName);
        // Fill '1st Tester' cell's value
        ExcelUtil.fillCellWithValue(
            tarSheet, FIRST_TESTER_FIELD_ROW_INDEX, COMMON_FIELD_COLUMN_INDEX,
            issue.getAssignee());


        /** Fill commit list sheet*/
        // Fill source code branch involved field
//        Collection involvedVersions =
//            (Collection) issue.getCustomFieldValue(cfm.getCustomFieldObjectByName("Publish Target"));
//        Collection publishedVersions =
//            (Collection) issue.getCustomFieldValue(cfm.getCustomFieldObjectByName("Publish Done"));
//        Collection testedVersions =
//            (Collection) issue.getCustomFieldValue(cfm.getCustomFieldObjectByName("QA Test Done"));
//        Status trunkStatus = issue.getStatusObject();
//        if (trunkStatus != null) {
//            String status = trunkStatus.getName();
//            if (!ISSUESTATUS_OPEN.equals(status) && !ISSUESTATUS_REOPENED.equals(status) && !ISSUESTATUS_IN_PROGRESS
//                .equals(status)) {
//                ExcelUtil.fillCellWithValue(
//                    commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX - 1, PUBLISH_DONE_COLUMN_INDEX, "Y");
//                if (!ISSUESTATUS_RESOLVED.equals(status)) {
//                    ExcelUtil.fillCellWithValue(
//                        commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX - 1, QA_DONE_COLUMN_INDEX, "Y");
//                }
//            }
//        }
//        if (involvedVersions != null) {
//            Object[] versionObjects = involvedVersions.toArray();
//            for (int i = 0; i < versionObjects.length; i++) {
//                Object obj = versionObjects[i];
//                String v = obj.toString();
//                String branch = ((v.replaceAll("KFF", "RB_")).replaceAll("\\.", "_")).replaceAll(" ", "");
//                if (i > 1) {
//                    ExcelUtil.copyRow(
//                        commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX,
//                        SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX + i);
//                }
//
//                ExcelUtil.fillCellWithValue(commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX + i, 0, i + 2);
//                ExcelUtil.fillCellWithValue(
//                    commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX + i, BRANCH_INVOLVED_COLUMN_INDEX, branch);
//
//                if (publishedVersions != null) {
//                    for (Object o : publishedVersions) {
//                        if (v.equals(o.toString())) {
//                            ExcelUtil.fillCellWithValue(
//                                commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX + i, PUBLISH_DONE_COLUMN_INDEX,
//                                "Y");
//                        }
//                    }
//                }
//                if (testedVersions != null) {
//                    for (Object o : testedVersions) {
//                        if (v.equals(o.toString())) {
//                            ExcelUtil.fillCellWithValue(
//                                commitListSheet, SOURCE_CODE_BRANCH_INVOLVED_ROW_INDEX + i, QA_DONE_COLUMN_INDEX, "Y");
//                        }
//                    }
//                }
//
//            }
//        }

        // Fill dbupdate branch involved field
        int offset = 0;
//        if (involvedVersions != null)
//        {
//            if (involvedVersions.size() > 2) {
//                offset = involvedVersions.size() - 2;
//            }
//        }

        Object trunkDBUpdateNoObj = JiraSyncUtil.getCustomFieldFromIssue(issue,"Trunk DBUpdate No");

        ExcelUtil.fillCellWithValue(
            commitListSheet, DBUPDATE_NO_TRUNK_ROW_INDEX + offset, DBUPDATE_NO_COLUMN_INDEX, trunkDBUpdateNoObj);

        Object releaseDBUpdateNoObj = JiraSyncUtil.getCustomFieldFromIssue(issue,"Release DBUpdate No");

        ExcelUtil.fillCellWithValue(
            commitListSheet, DBUPDATE_NO_RELEASE_ROW_INDEX + offset, DBUPDATE_NO_COLUMN_INDEX, releaseDBUpdateNoObj);

        tarWB.write(tarOut);
        templateIn.close();
        tarOut.close();
        tar.deleteOnExit();
        return tar;

    }


}
