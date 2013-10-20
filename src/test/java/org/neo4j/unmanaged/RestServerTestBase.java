package org.neo4j.unmanaged;


import java.net.URISyntaxException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.GraphDatabaseAPI;
import org.neo4j.kernel.impl.core.GraphProperties;
import org.neo4j.kernel.impl.core.NodeManager;

import static junit.framework.Assert.assertEquals;


public abstract class RestServerTestBase {

    public static final String HOSTNAME = "localhost";
    public static final int PORT = 7374;
    private static LocalTestServer neoServer;
    public static final String SERVER_ROOT = "http://" + HOSTNAME + ":" + PORT;
    protected static final String SERVER_ROOT_URI = SERVER_ROOT + "/db/data/";

    static {
        initServer();
    }

    protected static Client client;
    protected static WebResource rootResource;

    protected static void initServer() {
        if (neoServer!=null) {
            neoServer.stop();
        }
        neoServer = new LocalTestServer(HOSTNAME,PORT);
    }

    @BeforeClass
    public static void startDb() throws Exception {
        neoServer.start();
        client = Client.create();
        rootResource = client.resource(SERVER_ROOT);
        tryConnect();
    }

    private static void tryConnect() throws InterruptedException {
        int retryCount = 3;
        for (int i = 0; i < retryCount; i++) {
            try {
                ClientResponse response = rootResource.path("db/data/").get(ClientResponse.class);
                assertEquals(200, response.getStatus());
                System.err.println("Successful HTTP connection to "+SERVER_ROOT_URI);
                return;
            } catch (Exception e) {
                System.err.println("Error retrieving ROOT URI " + e.getMessage());
                Thread.sleep(500);
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        neoServer.cleanDb();
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void shutdownDb() {
        neoServer.stop();
    }

    protected GraphDatabaseService getGraphDatabase() {
        return neoServer.getGraphDatabase();
    }
}