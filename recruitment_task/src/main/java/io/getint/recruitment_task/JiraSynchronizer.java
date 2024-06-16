package io.getint.recruitment_task;

import io.getint.recruitment_task.config.ConfigurationLoader;
import io.getint.recruitment_task.http.JiraApiClient;
import io.getint.recruitment_task.model.Ticket;

import java.util.List;

public class JiraSynchronizer {
    /**
     * Search for 5 tickets in one project, and move them
     * to the other project within same Jira instance.
     * When moving tickets, please move following fields:
     * - summary (title)
     * - description
     * - priority
     * Bonus points for syncing comments.
     */
    public void moveTasksToOtherProject() {
        ConfigurationLoader configLoader = new ConfigurationLoader();
        JiraApiClient jiraApiClient = new JiraApiClient(configLoader);

        String sourceProjectKey = configLoader.getSourceProjectKey();
        String targetProjectKey = configLoader.getTargetProjectKey();

        if (sourceProjectKey == null || targetProjectKey == null) {
            System.err.println("Source or target project key is not configured.");
            return;
        }

        try {
            List<Ticket> tickets = jiraApiClient.getAllIssues(sourceProjectKey);

            for (Ticket ticket : tickets) {
                jiraApiClient.createTask(targetProjectKey, ticket.getSummary(), ticket.getDescription());
            }

            System.out.println("Tasks migrated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to migrate tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        JiraSynchronizer synchronizer = new JiraSynchronizer();
        synchronizer.moveTasksToOtherProject();
    }

}





