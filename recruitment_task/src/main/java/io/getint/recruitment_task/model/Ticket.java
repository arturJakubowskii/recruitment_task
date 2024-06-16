package io.getint.recruitment_task.model;

public class Ticket {

    private String projectKey;
    private String summary;
    private String description;

    public Ticket(String projectKey, String summary, String description) {
        this.projectKey = projectKey;
        this.summary = summary;
        this.description = description;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getSummary() {
        return summary;
    }


    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "projectKey='" + projectKey + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

