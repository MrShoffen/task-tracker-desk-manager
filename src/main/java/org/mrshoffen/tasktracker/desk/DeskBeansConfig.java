package org.mrshoffen.tasktracker.desk;

import org.mrshoffen.tasktracker.desk.client.WorkspaceClient;
import org.mrshoffen.tasktracker.desk.model.dto.links.DeskDtoLinksInjector;
import org.springframework.beans.factory.annotation.Value;
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
    public WorkspaceClient workspaceClient(WebClient.Builder webClientBuilder) {
        return new WorkspaceClient(webClientBuilder.baseUrl("http://workspace-manager-rs").build());
    }

    @Bean
    DeskDtoLinksInjector deskDtoLinksInjector(@Value("${app.gateway.api-prefix}") String apiPrefix) {
        return new DeskDtoLinksInjector(apiPrefix);
    }
}
