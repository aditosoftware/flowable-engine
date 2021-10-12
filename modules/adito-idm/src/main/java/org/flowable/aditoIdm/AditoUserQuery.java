package org.flowable.aditoIdm;

import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.UserQueryImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;

import java.util.ArrayList;
import java.util.List;

public class AditoUserQuery extends UserQueryImpl
{
    @Override
    public long executeCount(CommandContext commandContext)
    {
        return 1;
    }

    @Override
    public List<User> executeList(CommandContext commandContext)
    {
        User dummy = new UserEntityImpl();
        dummy.setEmail("admin@domain.local");
        dummy.setFirstName("Tim");
        dummy.setLastName("Admin");
        dummy.setPassword("a");
        dummy.setId("_____USER_0555b87b-5708-4748-aa88-064e33cab002");
        dummy.setDisplayName("admin");
        List<User> users = new ArrayList<>();
        users.add(dummy);
        return users;
    }
}
