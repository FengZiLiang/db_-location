package cn.td.aiot.common.config;

import cn.td.aiot.common.properties.TdProperties;
import org.springframework.context.annotation.Configuration;


/**
 * 梯度Swagger2 Api配置类
 *
 * @author YeYouGui
 */
@Configuration
public class TiduConfig {
    private final TdProperties properties;

    public TiduConfig(TdProperties properties) {
        this.properties = properties;
    }

//    @Bean
//    public Docket swaggerApi() {
//        SwaggerProperties swagger = properties.getSwagger();
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage(swagger.getBasePackage()))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo(swagger));
//    }
//
//    private ApiInfo apiInfo(SwaggerProperties swagger) {
//        return new ApiInfo(
//                swagger.getTitle(),
//                swagger.getDescription(),
//                swagger.getVersion(),
//                null,
//                new Contact(swagger.getAuthor(), swagger.getUrl(), swagger.getEmail()),
//                swagger.getLicense(), swagger.getLicenseUrl(), Collections.emptyList());
//    }
}
