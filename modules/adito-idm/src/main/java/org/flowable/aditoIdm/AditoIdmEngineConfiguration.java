package org.flowable.aditoIdm;

import org.flowable.idm.engine.IdmEngineConfiguration;

public class AditoIdmEngineConfiguration extends IdmEngineConfiguration
{
    public AditoIdmEngineConfiguration()
    {
        setUsingRelationalDatabase(false);
    }

    @Override
    public void initDataManagers()
    {
        // No need to initialize data managers when using ldap
    }

}