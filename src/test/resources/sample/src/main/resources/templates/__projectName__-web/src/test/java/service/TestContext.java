package ${package}.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
 
@Configuration
@PropertySource({"classpath:app.properties", "classpath:buildinfo.properties"})
public class TestContext {

    @Bean
    public ExampleServiceGetApi<?,?> getExampleServiceGetApi() {
    	return new ExampleServiceGetApi<>();
    }
}