package org.neo4j.unmanaged;



import org.codehaus.jackson.JsonNode;
import org.junit.Test;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

import static junit.framework.Assert.assertEquals;

import org.neo4j.test.server.HTTP;

public class ExampleResourceTest {

    @Test
    public void shouldReturnAllTheNodes() {
        // Given
        try ( ServerControls server = TestServerBuilders.newInProcessBuilder()
                .withExtension( "/unmanaged", ExampleResource.class )
                .newServer() )
        {
            // When
            HTTP.Response response = HTTP.GET( server.httpURI().resolve( "myExtension" ).toString() );

            // Then
            assertEquals( 200, response.status() );
        }




//        assertEquals("Dave", response.get("n.name").get(0).asText());
//        assertEquals("Mark", response.get("n.name").get(1).asText());
    }
}