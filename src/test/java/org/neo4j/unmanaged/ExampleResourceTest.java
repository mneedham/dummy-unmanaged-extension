package org.neo4j.unmanaged;


import org.codehaus.jackson.JsonNode;
import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

import java.net.URI;
import java.util.List;

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
}