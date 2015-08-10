package org.neo4j.unmanaged;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;

import java.io.IOException;
import java.net.ServerSocket;

import static junit.framework.Assert.assertEquals;

public class DummyServer {
    public static void main(String[] args) throws IOException {
//        ServerSocket serverSocket = new ServerSocket(0);

        GraphDatabaseAPI db;
        CommunityNeoServer server;

        server = CommunityServerBuilder
                .server()
                .onPort(7474)
                .withThirdPartyJaxRsPackage("org.neo4j.unmanaged", "/unmanaged")
                .build();

        server.start();
    }


}