package org.neo4j.unmanaged;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.ResourceIterator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/dummy")
public class DummyResource
{
    private final GraphDatabaseService database;
    private final ExecutionEngine executionEngine;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public DummyResource( @Context GraphDatabaseService database )
    {
        this.database = database;
        this.executionEngine = new ExecutionEngine(database);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all-nodes")
    public Response uploadNodesFile(  ) throws IOException
    {
        ExecutionResult result = executionEngine.execute("START n = node(*) RETURN n.name");

        ObjectNode root = JsonNodeFactory.instance.objectNode();
        for (String column : result.columns()) {
            ResourceIterator<Object> rows = result.columnAs(column);

            ArrayNode resultRows = JsonNodeFactory.instance.arrayNode();
            while(rows.hasNext()) {
                Object row = rows.next();

                if(row != null)
                    resultRows.add(row.toString());
            }

            root.put(column, resultRows);
        }

        return Response.status( 200 )
                .entity(OBJECT_MAPPER.writeValueAsString(root))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
