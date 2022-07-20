package com.example.ApiGateway;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class ApiGatewayConfiguration {
    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder){
//        creating a simple route function using lambda: redirects any get to that uri
        return builder.routes()
                .route(p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("MyHeader", "MyURI")
        //                        Can add auth headers here
                                .addRequestParameter("Param", "MyValue"))
                        .uri("http://httpbin.org:80"))
                .route(p -> p.path("/currency-exchange/**")
//                        talk to eureka, find the location of the service load balance instances
                        .uri("lb://currency-exchange"))
                .route(p -> p.path("/currency-conversion/**")
//                        talk to eureka, find the location of the service load balance instances
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-feign/**")
//                        talk to eureka, find the location of the service load balance instances
                        .uri("lb://currency-conversion"))
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath("currency-conversion-new/(?<segment>.*)","currency-conversion-feign/${segment}"))
//                        talk to eureka, find the location of the service load balance instances
                        .uri("lb://currency-conversion"))
                .build();
    }
}
