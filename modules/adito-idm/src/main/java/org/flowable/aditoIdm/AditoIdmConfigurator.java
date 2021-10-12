package org.flowable.aditoIdm;

import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.configurator.IdmEngineConfigurator;

public class AditoIdmConfigurator extends IdmEngineConfigurator
{
    protected AditoIdmEngineConfiguration aditoIdmEngineConfiguration;

    @Override
    public void beforeInit(AbstractEngineConfiguration engineConfiguration)
    {
        // Nothing to do
    }

    @Override
    public void configure(AbstractEngineConfiguration engineConfiguration)
    {
        this.idmEngineConfiguration = new AditoIdmEngineConfiguration();

        super.configure(engineConfiguration);

        getIdmEngineConfiguration(engineConfiguration)
                .setIdmIdentityService(new AditoIdentityService());
    }

    // Getters and Setters //////////////////////////////////////////////////

    public AditoIdmEngineConfiguration getAditoIdmEngineConfiguration()
    {
        return aditoIdmEngineConfiguration;
    }

    public void setAditoIdmEngineConfiguration(AditoIdmEngineConfiguration aditoIdmEngineConfiguration)
    {
        this.aditoIdmEngineConfiguration = aditoIdmEngineConfiguration;
    }

    protected static IdmEngineConfiguration getIdmEngineConfiguration(AbstractEngineConfiguration engineConfiguration)
    {
        return (IdmEngineConfiguration) engineConfiguration.getEngineConfigurations().get(EngineConfigurationConstants.KEY_IDM_ENGINE_CONFIG);
    }
}
