package org.bluebank.api;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import org.bluebank.api.command.service.CommandControllerService;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import static com.codahale.metrics.health.HealthCheck.Result.unhealthy;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.common.util.concurrent.ServiceManager.Listener;

@Singleton
public class ServiceManagerProvider implements Provider<ServiceManager> {
    private final CommandControllerService controllerService;
    private final ServiceManagerHealthCheck serviceManagerHealthCheck;

    @Inject
    public ServiceManagerProvider(HealthCheckRegistry healthCheckRegistry, CommandControllerService controllerService) {
        this.serviceManagerHealthCheck = new ServiceManagerHealthCheck();
        this.controllerService = controllerService;
        healthCheckRegistry.register("Services", serviceManagerHealthCheck);
    }

    @Override
    public ServiceManager get() {
        ServiceManager serviceManager = new ServiceManager(newHashSet(controllerService));
        serviceManager.addListener(serviceManagerHealthCheck.serviceManagerListener);
        return serviceManager;
    }

    public static class ServiceManagerHealthCheck extends HealthCheck {
        private final ServiceManagerListener serviceManagerListener;
        private Result result = unhealthy("Services are not started");

        private ServiceManagerHealthCheck() {
            serviceManagerListener = new ServiceManagerListener();
        }

        public class ServiceManagerListener extends Listener {

            @Override
            public void healthy() {
                result = Result.healthy("All services are started");
            }

            @Override
            public void stopped() {
                result = unhealthy("All services are stopped");
            }

            @Override
            public void failure(Service service) {
                result = unhealthy("Service %s failed : %s", service, service.failureCause());
            }
        }

        @Override
        protected Result check() throws Exception {
            return result;
        }
    }
}
