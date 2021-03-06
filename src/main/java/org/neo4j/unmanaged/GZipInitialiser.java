package org.neo4j.unmanaged;

import org.apache.commons.configuration.Configuration;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.servlets.GzipFilter;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.AbstractNeoServer;
import org.neo4j.server.NeoServer;
import org.neo4j.server.plugins.Injectable;
import org.neo4j.server.plugins.SPIPluginLifecycle;
import org.neo4j.server.web.WebServer;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.util.Collection;
import java.util.Collections;

public class GZipInitialiser implements SPIPluginLifecycle {
    private WebServer webServer;

    @Override
    public Collection<Injectable<?>> start(NeoServer neoServer) {
        webServer = getWebServer(neoServer);
        GzipFilter filter = new GzipFilter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {
                super.init(filterConfig);
                // by default GZipFilter just gets applied to GET requests
                _methods.add(HttpMethod.POST.asString());
            }
        };
        webServer.addFilter(filter, "/*");
        return Collections.emptyList();
    }

    private WebServer getWebServer(final NeoServer neoServer) {
        if (neoServer instanceof AbstractNeoServer) {
            return ((AbstractNeoServer) neoServer).getWebServer();
        }
        throw new IllegalArgumentException("expected AbstractNeoServer");
    }

    @Override
    public Collection<Injectable<?>> start(GraphDatabaseService graphDatabaseService, Configuration configuration) {
        throw new IllegalAccessError();
    }

    @Override
    public void stop() {

    }
}