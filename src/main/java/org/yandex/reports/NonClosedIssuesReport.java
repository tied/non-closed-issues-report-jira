package org.yandex.reports;

import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.filter.SearchRequestService;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.search.SearchRequest;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.plugin.report.impl.AbstractReport;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.util.SimpleErrorCollection;
import com.atlassian.jira.web.action.ProjectActionSupport;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yandex.reports.date.utils.DateRange;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shagbark
 *         16.07.2017
 */
public class NonClosedIssuesReport extends AbstractReport {

    private static final String FILTER_ID_KEY = "filterId";
    private static final String PERIOD_KEY = "periodId";
    private static final long   MAX_FILTER_RESULTS_COUNT = 1000;

    private final Logger logger = LoggerFactory.getLogger(NonClosedIssuesReport.class);

    private final SearchRequestService searchRequestService;
    private final JiraAuthenticationContext authContext;
    private final SearchService searchService;

    public NonClosedIssuesReport(SearchRequestService searchRequestService,
                                 JiraAuthenticationContext authContext,
                                 SearchService searchService) {
        this.searchRequestService = searchRequestService;
        this.authContext = authContext;
        this.searchService = searchService;
    }

    @SuppressWarnings("Java8ReplaceMapGet")
    @Override
    public String generateReportHtml(ProjectActionSupport projectActionSupport, Map map) throws Exception {
        String filterIdAsString = (String)map.get(FILTER_ID_KEY);
        long filterId = Long.parseLong(filterIdAsString);

        SearchRequest request = searchRequestService.getFilter(
                new JiraServiceContextImpl(authContext.getUser(),new SimpleErrorCollection()),
                filterId);
        String period = (String)map.get(PERIOD_KEY);

        //noinspection unchecked
        map.put("issues",  generateReportData(request.getQuery(), period));
        //noinspection unchecked
        return getDescriptor().getHtml("view", map);
    }

    @Override
    public void validate(ProjectActionSupport action, Map params) {
        String filterIdAsString = (String)params.get(FILTER_ID_KEY);
        // If no filter selected, print error and exit from method.
        if (filterIdAsString == null || filterIdAsString.isEmpty()) {
            action.addError("filterId", authContext.getI18nHelper()
                    .getText("jira.dynamics.non.closed.issues.report.error.empty.filterId"));
            return;
        }
        long filterId = Long.parseLong(filterIdAsString);

        SearchRequest request = searchRequestService.getFilter(
                new JiraServiceContextImpl(authContext.getUser(),new SimpleErrorCollection()),
                filterId);
        long count = searchCount(authContext.getUser().getDirectoryUser(), request.getQuery());

        if (count > MAX_FILTER_RESULTS_COUNT) {
            // Show error, if filter contains more than 1.000 issues
            action.addError("filterId", authContext.getI18nHelper()
                    .getText("jira.dynamics.non.closed.issues.report.error.filter.has.big.number.of.issues"));
        } else if (count == -1) {
            // If error occurred during search - then, not to to show huge stacktrace,
            // print to him error message.
            action.addError("filterId", authContext.getI18nHelper()
                    .getText("jira.dynamics.non.closed.issues.report.error.with.filterId"));
        }
    }

    /**
     * Returns a count of issues which searched by query.
     *
     * @param user user, who generates the report
     * @param query query
     * @return count of issues or -1 if some error occurred during the search
     */
    private long searchCount(User user, Query query) {
        try {
            return searchService.searchCount(user, query);
        } catch (SearchException e) {
            logger.error("Error occurred during searching issues. Error message = " + e.getMessage());
            return -1L;
        }
    }

    /**
     * Generates report data, which will be displayed on the page.
     *
     * @param filterQuery search request - it contains the filter query
     * @param periodId selected period
     * @return collection of {@link ReportData} data
     */
    private Collection<ReportData> generateReportData(Query filterQuery, String periodId) {
        DateRange range = DateRange.newDateRange(periodId);
        Query query = createQuery(filterQuery, range);

        List<Issue> filteredIssues = executeQuery(authContext.getUser().getDirectoryUser(), query);
        return generateReportData(filteredIssues, range);
    }


    @SuppressWarnings("Java8ReplaceMapGet")
    private Collection<ReportData> generateReportData(List<Issue> filteredIssues, DateRange range) {
        // This map contains data of report - the key is project key, the value - report data,
        // which contains project key, summary and count of open and closed issues.
        Map<String, ReportData> data = new HashMap<>();
        for (Issue issue: filteredIssues) {
            Project project = issue.getProjectObject();
            ReportData reportData = data.get(project.getKey());
            if (reportData == null) {
                reportData = new ReportData(project.getName());
                data.put(project.getKey(), reportData);
            }

            // Check, if issue resolved in selected period
            if (issue.getResolutionDate() != null) {
                reportData.incrementClosed();
            }
            // Check, if issue created in selected period
            if (issue.getCreated().after(range.getLower()) &&
                    issue.getCreated().before(range.getUpper())) {
                reportData.incrementOpen();
            }

        }
        return data.values();
    }

    /**
     * Creates query from filter query and adds date conditions:
     *      1. createdDate condition
     *      2. resolutionDate condition
     *
     * @param filterQuery filter query
     * @param range selected {@link DateRange} range
     * @return result query
     */
    private Query createQuery(Query filterQuery, DateRange range) {
        return JqlQueryBuilder.newClauseBuilder(filterQuery)
                .and()
                .sub()
                .sub()
                .addDateRangeCondition("createdDate", range.getLower(), range.getUpper())
                .endsub()
                .or()
                .addDateRangeCondition("resolutionDate", range.getLower(), range.getUpper())
                .endsub()
                .buildQuery();
    }

    /**
     * Method searchers for issues by passed query.
     *
     * @param user user, who generates the report
     * @param query query
     * @return list of found issues
     */
    private List<Issue> executeQuery(User user, Query query) {
        try {
            return searchService.search(user, query, PagerFilter.getUnlimitedFilter())
                    .getIssues();
        } catch (SearchException e) {
            throw new RuntimeException(e);
        }
    }

}
