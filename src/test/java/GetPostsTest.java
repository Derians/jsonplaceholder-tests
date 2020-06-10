import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GetPostsTest {
    CloseableHttpClient closeableHttpClient;
    CloseableHttpResponse httpResponse;
    ObjectMapper objectMapper;
    JsonNode postSchema;
    JsonNode postArraySchema;
    JsonSchemaFactory factory;
    JsonSchema schema;
    JsonSchema arraySchema;
    protected final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";

    @Before
    public void setUp() {
        closeableHttpClient = HttpClients.createDefault();
        objectMapper = new ObjectMapper();
        try {
            postSchema = objectMapper.readTree(
                    getClass().getClassLoader().getResourceAsStream("postSchema.json"));
            postArraySchema = objectMapper.readTree(
                    getClass().getClassLoader().getResourceAsStream("postArraySchema.json"));

            factory = JsonSchemaFactory.byDefault();
            schema = factory.getJsonSchema(postSchema);
            arraySchema = factory.getJsonSchema(postArraySchema);
        } catch (IOException | ProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetPosts() throws IOException, ProcessingException {
//      Send request for get all posts
        httpResponse = closeableHttpClient.execute(new HttpGet(BASE_URL));

//      Check response status code. Expected: 200
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

//      Check response content type. Expected: "application/json; charset=utf-8"
        assertThat(
                httpResponse.getLastHeader("content-type").getValue(),
                equalTo("application/json; charset=utf-8"));

//      Validate json schema. Expected: json array of objects. json is valid
        ProcessingReport report = arraySchema.validate(objectMapper.readTree(
                EntityUtils.toString(httpResponse.getEntity())));

        System.out.println(report);
        assertTrue(report.isSuccess());

        httpResponse.close();
    }

    @Test
    public void testGetPostsByUserId() throws IOException, ProcessingException {
//      Send request for get all user posts with userId = 1
        httpResponse = closeableHttpClient.execute(new HttpGet(BASE_URL + "?userId=1"));

//      Check response status code. Expected: 200
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

//      Check response content type. Expected: "application/json; charset=utf-8"
        assertThat(
                httpResponse.getLastHeader("content-type").getValue(),
                equalTo("application/json; charset=utf-8"));

//      Validate json schema. Expected: json array of objects. json is valid
        ProcessingReport report = arraySchema.validate(objectMapper.readTree(
                EntityUtils.toString(httpResponse.getEntity())));

        System.out.println(report);
        assertTrue(report.isSuccess());

        httpResponse.close();
    }

    @Test
    public void testGetPostById() throws IOException, ProcessingException {
//      Send request for get post with id = 2
        httpResponse = closeableHttpClient.execute(new HttpGet(BASE_URL + "/2"));

//      Check response status code. Expected: 200
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

//      Check response content type. Expected: "application/json; charset=utf-8"
        assertThat(
                httpResponse.getLastHeader("content-type").getValue(),
                equalTo("application/json; charset=utf-8"));

//      Validate json schema. Expected: json object. json is valid
        ProcessingReport report = schema.validate(objectMapper.readTree(
                EntityUtils.toString(httpResponse.getEntity())));

        System.out.println(report);
        assertTrue(report.isSuccess());

        httpResponse.close();
    }

    @Test
    public void testGetPostByUserIdAndTitle() throws IOException, ProcessingException {
//      Send request for get post by userId = 1 and title = "qui est esse"
        httpResponse = closeableHttpClient.execute(new HttpGet(BASE_URL + "?userId=1&title=qui%20est%20esse"));

//      Check response status code. Expected: 200
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

//      Check response content type. Expected: "application/json; charset=utf-8"
        assertThat(
                httpResponse.getLastHeader("content-type").getValue(),
                equalTo("application/json; charset=utf-8"));

//      Validate json schema. Expected: json array of objects. json is valid
        ProcessingReport report = arraySchema.validate(objectMapper.readTree(
                EntityUtils.toString(httpResponse.getEntity())));

        System.out.println(report);
        assertTrue(report.isSuccess());

        httpResponse.close();
    }
}
