package com.app.productservice;

import com.app.productservice.mapper.DataModelMapper;
import com.app.productservice.mapper.DefaultDataModelMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;


@Configuration
public class ModelMapperConfig {
    @Bean
    public DataModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);
        return new DefaultDataModelMapper(mapper);
    }
}
