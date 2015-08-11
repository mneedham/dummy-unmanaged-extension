package org.neo4j.unmanaged;

import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

import java.io.File;
import java.io.IOException;

//import org.neo4j.server.helpers.CommunityServerBuilder;

public class DummyServer {
    public static void main(String[] args) throws IOException {

        ServerControls server = TestServerBuilders
                .newInProcessBuilder(new File("/Users/markneedham/repos/dummy-unmanaged-extension/neo4j-community-2.2.3/data/graph.db"))
                .withExtension("/unmanaged", ExampleResource.class)
                .newServer();


//        CommunityNeoServer server = CommunityServerBuilder
//                .server()
//                .onPort(7474)
//                .persistent()
//                .usingDatabaseDir("/Users/markneedham/repos/dummy-unmanaged-extension/neo4j-community-2.2.3/data/graph.db")
//                .withThirdPartyJaxRsPackage("org.neo4j.unmanaged", "/unmanaged")
//                .build();
//
//        server.start();
    }


}