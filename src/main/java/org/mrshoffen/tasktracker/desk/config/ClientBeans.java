package org.mrshoffen.tasktracker.desk.config;

import org.mrshoffen.tasktracker.desk.util.client.WorkspaceClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientBeans {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WorkspaceClient workspaceClient(WebClient.Builder webClientBuilder) {
        return new WorkspaceClient(webClientBuilder.baseUrl("http://workspace-manager-rs") .build());
    }
}
