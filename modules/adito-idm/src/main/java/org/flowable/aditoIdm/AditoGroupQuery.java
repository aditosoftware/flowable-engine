package org.flowable.aditoIdm;

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.Group;
import org.flowable.idm.engine.impl.GroupQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;

import java.util.ArrayList;
import java.util.List;

public class AditoGroupQuery extends GroupQueryImpl
{
    @Override
    public long executeCount(CommandContext commandContext)
    {
        return 1;
    }

    @Override
    public List<Group> executeList (CommandContext commandContext)
    {
        List<Group> groups = new ArrayList<>();
        Group dummy = new GroupEntityImpl();
        dummy.setName("Dummy");
        dummy.setType("Dummy");
        groups.add(dummy);

        return groups;
    }
}
