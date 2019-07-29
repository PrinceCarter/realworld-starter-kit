package io.openliberty.api;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Path;

import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import io.openliberty.core.user.User;



@Health
@ApplicationScoped
public class MPHealthEndpoint implements HealthCheck {


	@Override
	public HealthCheckResponse call() {
		System.out.println("Server is healthy.");
        return HealthCheckResponse.named("MPHealthEndpoint")
                .withData("key1", "val1")
                .withData("key2", "val2")
                .up()
                .build();
	}
	
	
}
