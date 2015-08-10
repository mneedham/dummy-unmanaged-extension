package org.neo4j.unmanaged;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Path("/dummy")
public class DummyResource {
    private final GraphDatabaseService db;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public DummyResource(@Context GraphDatabaseService db) {
        this.db = db;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/all-nodes")
    public Response allNodes() throws IOException {
        StreamingOutput stream = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                JsonGenerator jg = OBJECT_MAPPER.getJsonFactory().createJsonGenerator(os, JsonEncoding.UTF8);
                jg.writeStartObject();
                jg.writeFieldName("people");
                jg.writeStartArray();

                writeQueryResultTo("MATCH (n:Person) RETURN n.name AS name", jg);

                jg.writeEndArray();
                jg.writeEndObject();
                jg.flush();
                jg.close();
            }
        };

        return Response.ok().entity(stream).type(MediaType.APPLICATION_JSON).build();

    }

    private void writeQueryResultTo(String query, JsonGenerator jg) throws IOException {
        try (Result result = db.execute(query)) {
            while (result.hasNext()) {
                Map<String, Object> row = result.next();

                jg.writeStartObject();
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    jg.writeFieldName(entry.getKey());
                    jg.writeString(entry.getValue().toString());
                }
                jg.writeEndObject();
            }
        }
    }
}
