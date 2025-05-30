package org.mrshoffen.tasktracker.desk;

import org.mrshoffen.tasktracker.desk.client.PermissionsClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class DeskBeansConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public PermissionsClient permissionsClient(WebClient.Builder webClientBuilder) {
        return new PermissionsClient(webClientBuilder.baseUrl("http://user-permission-rs").build());
    }

}
