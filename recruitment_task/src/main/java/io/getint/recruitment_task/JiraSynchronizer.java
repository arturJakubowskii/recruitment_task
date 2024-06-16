package io.getint.recruitment_task;

import io.getint.recruitment_task.config.ConfigurationLoader;
import io.getint.recruitment_task.http.JiraApiClient;
import io.getint.recruitment_task.model.Task;

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
    public void moveTasksToOtherProject() throws Exception {
        ConfigurationLoader configLoader = new ConfigurationLoader();
        JiraApiClient jiraApiClient = new JiraApiClient(configLoader);

        String sourceProjectKey = configLoader.getSourceProjectKey();
        String targetProjectKey = configLoader.getTargetProjectKey();

        if (sourceProjectKey == null || targetProjectKey == null) {
            System.err.println("Source or target project key is not configured.");
            return;
        }

        try {
            // Retrieve tasks from the source project
            List<Task> tasks = jiraApiClient.getAllIssues(sourceProjectKey);
            for (Task task  : tasks){
                System.out.println(task.toString());
            }

            // Post tasks to the target project
//            for (Task task : tasks) {
//                jiraApiClient.createTask(task.getProjectKey(), task.getSummary());
//            }

            System.out.println("Tasks migrated successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to migrate tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        JiraSynchronizer synchronizer = new JiraSynchronizer();
        synchronizer.moveTasksToOtherProject();
    }

}





