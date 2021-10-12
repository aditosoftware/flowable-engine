package org.flowable.aditoIdm;

import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AditoFlowableConfiguration
{
    @Bean
    public EngineConfigurationConfigurer<IdmEngineConfiguration> idmProcessEngineConfigurationConfigurer()
    {
        return idmEngineConfiguration -> idmEngineConfiguration.setIdmIdentityService(new AditoIdentityService());
    }
}
