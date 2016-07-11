package com.kewill.jira.model;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by YanJun on 2016/6/30.
 */
public class Issue {
    private String title;
    private String link;
    private int projectId;
    private String project;
    private String description;
    private Long keyId;
    private String key;
    private String summary;
    private String type;
    private String status;
    private String resolution;
    private String assignee;
    private String reporter;
    private Date created;
    private Date updated;
    private Date resolved;
    private String fixVersion;
    private List<String> componentList;
    private List<Map<String,String>> customFieldList;
    private int votes;
    private int watches;
    private String priority;


    public List<Map<String, String>> getCustomFieldList() {
        return customFieldList;
    }

    public void setCustomFieldList(List<Map<String, String>> customFieldList) {
        this.customFieldList = customFieldList;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getResolved() {
        return resolved;
    }

    public void setResolved(Date resolved) {
        this.resolved = resolved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
        this.keyId = keyId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }


    public String getFixVersion() {
        return fixVersion;
    }

    public void setFixVersion(String fixVersion) {
        this.fixVersion = fixVersion;
    }

    public List<String> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<String> componentList) {
        this.componentList = componentList;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getWatches() {
        return watches;
    }

    public void setWatches(int watches) {
        this.watches = watches;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Issue{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", projectId=" + projectId +
                ", project='" + project + '\'' +
                ", description='" + description + '\'' +
                ", keyId=" + keyId +
                ", key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", resolution='" + resolution + '\'' +
                ", assignee='" + assignee + '\'' +
                ", reporter='" + reporter + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", resolved=" + resolved +
                ", fixVersion='" + fixVersion + '\'' +
                ", componentList=" + componentList +
                ", customFieldList=" + customFieldList +
                ", votes=" + votes +
                ", watches=" + watches +
                ", priority='" + priority + '\'' +
                '}';
    }
}
