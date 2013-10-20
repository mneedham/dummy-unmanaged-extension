package org.neo4j.unmanaged;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import static junit.framework.Assert.assertEquals;

public class DummyResourceTest extends RestServerTestBase {
    @Test
    public void shouldReturnAllTheNodes() {
        GraphDatabaseService db = getGraphDatabase();
        Transaction tx = db.beginTx();
        Node node = db.createNode();
        node.setProperty("name", "Mark");
        tx.success(); tx.close();

        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add( JacksonJsonProvider.class );
        Client client = Client.create(defaultClientConfig);

        JsonNode response = client.resource(SERVER_ROOT + "/unmanaged/dummy/all-nodes")
                .get(ClientResponse.class)
                .getEntity(JsonNode.class);

        assertEquals("Mark", response.get("n.name").get(0).asText());
    }
}
