package org.gpstable.config;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.gpstable.shared.DeviceHashStrategy;
import org.gpstable.shared.StrategyManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = { "org.gpstable"})
public class AppConfig {
	private static final String MAPPING_FILE = "DTOMap.xml";

    /**
     * Configures dozer mapping and objectMapper
     * @return DozerBeanMapper
     */
    @Bean
    public DozerBeanMapper dozerMapper() {
        final DozerBeanMapper mapper = new DozerBeanMapper();
        final List<String> mappingFiles = new ArrayList<>();
        mappingFiles.add(MAPPING_FILE);
        mapper.setMappingFiles(mappingFiles);
        return mapper;
    }

    /**
     *Configures restTemplate for restRequest
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    
    @Bean
    @Scope("singleton")
    public StrategyManager strategyManager(){
    	StrategyManager strategyManager =new StrategyManager();
    	strategyManager.getStrategies().put("hash", new DeviceHashStrategy());
    	return strategyManager;
    }

}
