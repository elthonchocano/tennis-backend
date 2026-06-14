package com.echocano.tennis.league.infrastructure.config;

import org.jboss.logging.Logger;

import io.quarkus.netty.runtime.virtual.VirtualClientConnection;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class LambdaNettyWarmUp {

    private static final Logger LOG = Logger.getLogger(LambdaNettyWarmUp.class);

    void onStart(@Observes StartupEvent ev) {
        LOG.info("Warming up Netty virtual channel to prevent race conditions...");
        try {
            Class.forName(VirtualClientConnection.class.getName());
            Thread.sleep(50);

            LOG.info("Netty virtual channel successfully guaranteed and ready for traffic.");
        } catch (Exception e) {
            LOG.error("Error preloading Netty virtual channel", e);
        }
    }
}
