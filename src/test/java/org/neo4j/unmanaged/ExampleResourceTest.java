package org.neo4j.unmanaged;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.helpers.collection.Iterables;
import org.neo4j.test.server.HTTP;

import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.URI;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static junit.framework.Assert.assertEquals;

public class ExampleResourceTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withFixture("CREATE (:Person {name: 'Mark'})")
            .withFixture("CREATE (:Person {name: 'Nicole'})")
            .withExtension( "/unmanaged", ExampleResource.class );



    @Test
    public void shouldReturnAllTheNodes() {
        // Given
        URI serverURI = neo4j.httpURI();
        // When
        HTTP.Response response = HTTP.GET(serverURI.resolve("/unmanaged/example/people").toString());

        // Then
        assertEquals(200, response.status());
        List content = response.content();

        assertEquals(2, content.size());
    }

    @Test
    public void shouldReturnAllTheNodesWithGzip() throws IOException {
        // Given
        URI serverURI = neo4j.httpURI();
        Client client = Client.create();
        WebResource resource = client.resource(serverURI).path("/unmanaged/example/people");

        // When
        ClientResponse response = resource.header("Accept-Encoding", "gzip,deflate").get(ClientResponse.class);
        byte[] content = IOUtils.toByteArray(response.getEntityInputStream());
        String uncompressed = gunzip(content);

        // then
        assertEquals(200, response.getStatus());
        assertEquals("gzip", Iterables.single(response.getHeaders().get("Content-Encoding")));
        assertEquals("[{\"name\":\"Mark\"},{\"name\":\"Nicole\"}]", uncompressed);
    }

    @Test
    public void shouldCypherEndpointUseGzip() throws IOException {
        // Given
        URI serverURI = neo4j.httpURI();
        Client client = Client.create();
        WebResource resource = client.resource(serverURI).path("/db/data/transaction/commit");

        // When
        Object requestBody = "{\n" +
                "  \"statements\" : [ {\n" +
                "    \"statement\" : \"MATCH (n:Person) RETURN n.name\"\n" +
                "  } ]\n" +
                "}";
        ClientResponse response = resource
                .header("Accept-Encoding", "gzip,deflate")
                .accept(MediaType.APPLICATION_JSON)
                .header("Content-Type", "application/json")
                .post(ClientResponse.class, requestBody);
        byte[] content = IOUtils.toByteArray(response.getEntityInputStream());
        String uncompressed = gunzip(content);

        // then
        assertEquals(200, response.getStatus());
        assertEquals("gzip", Iterables.single(response.getHeaders().get("Content-Encoding")));
        assertEquals("[{\"name\":\"Mark\"},{\"name\":\"Nicole\"}]", uncompressed);
    }

    private String gunzip(byte[] compressed) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressed));
        return IOUtils.toString(gis);
   }
}