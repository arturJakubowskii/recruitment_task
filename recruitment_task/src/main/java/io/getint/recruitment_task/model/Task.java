package io.getint.recruitment_task.model;

public class Task {

    private String projectKey;
    private String summary;
    //private String issueType;
   // private String description;

    public Task(String projectKey, String summary) {
        this.projectKey = projectKey;
        this.summary = summary;
       // this.issueType = issueType;
        //this.description = description;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getSummary() {
        return summary;
    }

//    public String getIssueType() {
//        return issueType;
//    }
//
//
//    public String getDescription() {
//        return description;
//    }

    @Override
    public String toString() {
        return "Task{" +
                "projectKey='" + projectKey + '\'' +
                ", summary='" + summary + '\'' +
                //", issueType='" + issueType + '\'' +
                //", description='" + description + '\'' +
                '}';
    }
}

