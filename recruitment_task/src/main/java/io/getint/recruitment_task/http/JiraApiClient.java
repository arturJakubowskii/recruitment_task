package io.getint.recruitment_task.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.getint.recruitment_task.config.ConfigurationLoader;
import io.getint.recruitment_task.model.Ticket;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class JiraApiClient {

    private static String jiraUrl;
    private static String username;
    private static String apiToken;

    public JiraApiClient(ConfigurationLoader configurationLoader) {
        jiraUrl = configurationLoader.getProperty("jira.url");
        username = configurationLoader.getProperty("jira.username");
        apiToken = configurationLoader.getProperty("jira.api.token");
    }

    public List<Ticket> getAllIssues(String projectKey) throws Exception {
        String url = jiraUrl + "/rest/api/3/search?jql=project=" + projectKey + "&maxResults=100";

        HttpClient client = createHttpClient();
        HttpGet request = new HttpGet(url);

        // Add authentication headers
        String auth = username + ":" + apiToken;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

        HttpResponse response = client.execute(request);

        // Check response status
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonResponse = mapper.readTree(sb.toString());

            // Extract and return the issues as Task objects
            List<Ticket> issuesList = new ArrayList<>();
            JsonNode issues = jsonResponse.get("issues");
            if (issues.isArray()) {
                for (JsonNode issue : issues) {
                    String summary = issue.get("fields").get("summary").asText();
                    String issueType = issue.get("fields").get("issuetype").get("name").asText();
                    String description = issue.get("fields").get("description")
                            .get("content").get(0)
                            .get("content").get(0)
                            .get("text").asText();

                    Ticket ticket = new Ticket(projectKey, summary, description);
                    issuesList.add(ticket);
                }
            }
            return issuesList;
        } else {
            throw new RuntimeException("Failed to fetch issues, HTTP error code: " + statusCode);
        }
    }


    public void createTask(String projectKey, String summary, String description) throws Exception {
        String url = jiraUrl + "/rest/api/3/issue";

        HttpClient client = createHttpClient();
        HttpPost request = new HttpPost(url);

        // Add authentication headers
        String auth = username + ":" + apiToken;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");


        String jsonPayload = "{\n"
                + "  \"fields\": {\n"
                + "    \"project\": {\"key\": \"" + projectKey + "\"},\n"
                + "    \"summary\": \"" + summary + "\",\n"
                + "    \"description\": {\n"
                + "      \"version\": 1,\n"
                + "      \"type\": \"doc\",\n"
                + "      \"content\": [\n"
                + "        {\n"
                + "          \"type\": \"paragraph\",\n"
                + "          \"content\": [\n"
                + "            {\n"
                + "              \"type\": \"text\",\n"
                + "              \"text\": \"" + description + "\"\n"
                + "            }\n"
                + "          ]\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"issuetype\": {\"name\": \"Task\"}\n"
                + "  }\n"
                + "}";

        StringEntity entity = new StringEntity(jsonPayload);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);

        // Check response status
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != 201) {
            throw new RuntimeException("Failed to create issue, HTTP error code: " + statusCode);
        }
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }
}