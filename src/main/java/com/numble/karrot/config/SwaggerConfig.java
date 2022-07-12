package com.numble.karrot.config;

import java.util.Arrays;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

	// TODO : 내꺼는 JSESSIONID 방식으로 바꿔야 할듯?
	@Bean
	public OpenApiCustomiser customOpenApi() {
		return openApi -> {
			String bearer = "bearer";
			openApi
				.info(new Info().title("karrot"))
				.components(
					openApi.getComponents().addSecuritySchemes(
						bearer,
						new SecurityScheme()
							.type(SecurityScheme.Type.HTTP)
							.scheme(bearer)
							.bearerFormat("bearer")
							.in(SecurityScheme.In.HEADER)
							.name("Authorization")
					)
				)
				.addSecurityItem(
					new SecurityRequirement()
						.addList(bearer, Arrays.asList("read", "write"))
				);
		};
	}

}
