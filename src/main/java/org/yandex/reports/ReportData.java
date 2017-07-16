package org.yandex.reports;

/**
 * @author Shagbark
 *         16.07.2017
 */
public class ReportData {

    private String projectSummary;
    private int createdIssuesCount;
    private int closedIssuesCount;

    ReportData(String projectSummary) {
        this.projectSummary = projectSummary;
    }

    void incrementOpen() {
        createdIssuesCount++;
    }

    void incrementClosed() {
        closedIssuesCount++;
    }

    public String getProjectSummary() {
        return projectSummary;
    }

    public int getCreatedIssuesCount() {
        return createdIssuesCount;
    }

    public int getClosedIssuesCount() {
        return closedIssuesCount;
    }

}
