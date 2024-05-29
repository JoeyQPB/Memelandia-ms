package com.joey.memelandiaapigateway.routing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

@Configuration
public class ConfigRoutes {

    @Value("${user.url}")
    private String userUrl;

    @Value("${user.predicate}")
    private String userPredicate;

    @Value("${category.url}")
    private String categoryUrl;

    @Value("${category.predicate}")
    private String categoryPredicate;

    @Value("${meme.url}")
    private String memeUrl;

    @Value("${meme.predicate}")
    private String memePredicate;

    @Value("${kafka.url}")
    private String kafkaUrl;

    @Value("${kafka.predicate}")
    private String kafkaPredicate;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // Users
                .route("create_user", r -> r.path("/user/create")
                        .filters(f -> f.rewritePath("/user/create", userPredicate))
                        .uri(userUrl))
                .route("user_getAll", r -> r.path("/user/get_all")
                        .filters(f -> f.rewritePath("/user/get_all", userPredicate))
                        .uri(userUrl))
                .route("user_get_id", r -> r.path("/user/get_by_id/**")
                        .filters(f -> f.rewritePath("/user/get_by_id/(?<segment>.*)", userPredicate+"/${segment}"))
                        .uri(userUrl))
                .route("user_get_email", r -> r.path("/user/get_by_email/**")
                        .filters(f -> f.rewritePath("/user/get_by_email/(?<segment>.*)", userPredicate+"/qe=${segment}"))
                        .uri(userUrl))
                .route("user_update", r -> r.path("/user/update/**")
                        .filters(f -> f.rewritePath("/user/update/(?<segment>.*)", userPredicate+"/${segment}"))
                        .uri(userUrl))
                .route("user_delete", r -> r.path("/user/delete/**")
                        .filters(f -> f.rewritePath("/user/delete/(?<segment>.*)", userPredicate+"/${segment}"))
                        .uri(userUrl))
                // Category
                .route("create_category", r -> r.path("/category/create")
                        .filters(f -> f.rewritePath("/category/create", categoryPredicate))
                        .uri(categoryUrl))
                .route("category_getAll", r -> r.path("/category/get_all")
                        .filters(f -> f.rewritePath("/category/get_all", categoryPredicate))
                        .uri(categoryUrl))
                .route("category_get_id", r -> r.path("/category/get_by_id/**")
                        .filters(f -> f.rewritePath("/category/get_by_id/(?<segment>.*)", categoryPredicate+"/${segment}"))
                        .uri(categoryUrl))
                .route("category_get_name", r -> r.path("/category/get_by_name/**")
                        .filters(f -> f.rewritePath("/category/get_by_name/(?<segment>.*)", categoryPredicate+"/qe=${segment}"))
                        .uri(categoryUrl))
                .route("category_update", r -> r.path("/category/update/**")
                        .filters(f -> f.rewritePath("/category/update/(?<segment>.*)", categoryPredicate+"/${segment}"))
                        .uri(categoryUrl))
                .route("category_delete", r -> r.path("/category/delete/**")
                        .filters(f -> f.rewritePath("/category/delete/(?<segment>.*)", categoryPredicate+"/${segment}"))
                        .uri(categoryUrl))
                // Meme
                .route("create_meme", r -> r.path("/meme/create")
                        .filters(f -> f.rewritePath("/meme/create", memePredicate))
                        .uri(memeUrl))
                .route("create_meme_with_path", r -> r.path("/meme/create/{segment1}/{segment2}")
                        .filters(f -> f.rewritePath("/meme/create/(?<segment1>.*)/(?<segment2>.*)", memePredicate+"/${segment1}/${segment2}"))
                        .uri(memeUrl))
                .route("meme_getAll", r -> r.path("/meme/get_all")
                        .filters(f -> f.rewritePath("/meme/get_all", memePredicate))
                        .uri(memeUrl))
                .route("meme_getAllByCategory", r -> r.path("/meme/get_all_by_category/{segment1}")
                        .filters(f -> f.rewritePath("/meme/get_all_by_category/(?<segment1>.*)", memePredicate+"/qCategory=${segment1}"))
                        .uri(memeUrl))
                .route("meme_getAllByCreator", r -> r.path("/meme/get_all_by_creator/{segment1}")
                        .filters(f -> f.rewritePath("/meme/get_all_by_creator/(?<segment1>.*)", memePredicate+"/qCreator=${segment1}"))
                        .uri(memeUrl))
                .route("meme_get_id", r -> r.path("/meme/get_by_id/**")
                        .filters(f -> f.rewritePath("/meme/get_by_id/(?<segment>.*)", memePredicate+"/${segment}"))
                        .uri(memeUrl))
                .route("meme_get_name", r -> r.path("/meme/get_by_name/**")
                        .filters(f -> f.rewritePath("/meme/get_by_name/(?<segment>.*)", memePredicate+"/qe=${segment}"))
                        .uri(memeUrl))
                .route("meme_update", r -> r.path("/meme/update/**")
                        .filters(f -> f.rewritePath("/meme/update/(?<segment>.*)", memePredicate+"/${segment}"))
                        .uri(memeUrl))
                .route("meme_update_creator", r -> r.path("/meme/update_creator/**")
                        .filters(f -> f.rewritePath("/meme/update_creator/(?<segment>.*)", memePredicate+"/creator/${segment}"))
                        .uri(memeUrl))
                .route("meme_update", r -> r.path("/meme/update_category/**")
                        .filters(f -> f.rewritePath("/meme/update_category/(?<segment>.*)", memePredicate+"/category/${segment}"))
                        .uri(memeUrl))
                .route("meme_delete", r -> r.path("/meme/delete/**")
                        .filters(f -> f.rewritePath("/meme/delete/(?<segment>.*)", memePredicate+"/${segment}"))
                        .uri(memeUrl))
                .route("meme_of_the_day", r -> r.path("/meme/meme_of_the_day")
                        .filters(f -> f.rewritePath("/meme/meme_of_the_day", memePredicate+"/meme_of_the_day"))
                        .uri(memeUrl))
                // kafka service
                .route("kafka_update_user", r -> r.path("/update_micro_service/user")
                        .filters(f -> f.rewritePath("/update_micro_service/user", kafkaPredicate+"/user"))
                        .uri(kafkaUrl))
                .route("kafka_update_category", r -> r.path("/update_micro_service/category")
                        .filters(f -> f.rewritePath("/update_micro_service/category", kafkaPredicate+"/category"))
                        .uri(kafkaUrl))
                .build();
    }
}
