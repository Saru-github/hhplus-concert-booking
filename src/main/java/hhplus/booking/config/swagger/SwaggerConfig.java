package hhplus.booking.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .version("v1.0.0") //버전
                .title("ConcertBooking API") //이름
                .description("콘서트 예약 관련 API"); //설명
        return new OpenAPI()
                .info(info);
    }

}
