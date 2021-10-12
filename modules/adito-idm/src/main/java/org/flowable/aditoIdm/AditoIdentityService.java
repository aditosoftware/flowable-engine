package org.flowable.aditoIdm;

import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.idm.api.*;
import org.flowable.idm.engine.IdmEngineConfiguration;
import org.flowable.idm.engine.impl.IdmIdentityServiceImpl;
import org.flowable.idm.engine.impl.NativeTokenQueryImpl;
import org.flowable.idm.engine.impl.cmd.*;
import org.flowable.idm.engine.impl.persistence.entity.IdentityInfoEntity;

import java.util.List;

public class AditoIdentityService extends IdmIdentityServiceImpl
{
    public AditoIdentityService()
    {
        super();
    }

    @Override
    public Group newGroup(String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public User newUser(String userId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void saveGroup(Group group)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void saveUser(User user)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void updateUserPassword(User user)
    {
        throw new FlowableException("nope");
    }

    @Override
    public UserQuery createUserQuery()
    {
        return new AditoUserQuery();
    }

    @Override
    public NativeUserQuery createNativeUserQuery()
    {
        throw new FlowableException("nope");
    }

    @Override
    public GroupQuery createGroupQuery()
    {
        return new AditoGroupQuery();
    }

    @Override
    public NativeGroupQuery createNativeGroupQuery()
    {
        throw new FlowableException("nope");
    }

    @Override
    public void createMembership(String userId, String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void deleteGroup(String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void deleteMembership(String userId, String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public boolean checkPassword(String userId, String password)
    {
        return true;
    }

    @Override
    public void setAuthenticatedUserId(String authenticatedUserId)
    {
        Authentication.setAuthenticatedUserId(authenticatedUserId);
    }

    @Override
    public void deleteUser(String userId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public Token newToken(String tokenId)
    {
        return commandExecutor.execute(new CreateTokenCmd(tokenId));
    }

    @Override
    public void saveToken(Token token)
    {
        commandExecutor.execute(new SaveTokenCmd(token));
    }

    @Override
    public void deleteToken(String tokenId)
    {
        commandExecutor.execute(new DeleteTokenCmd(tokenId));
    }

    @Override
    public TokenQuery createTokenQuery()
    {
        return commandExecutor.execute(new CreateTokenQueryCmd());
    }

    @Override
    public NativeTokenQuery createNativeTokenQuery()
    {
        return new NativeTokenQueryImpl(commandExecutor);
    }

    @Override
    public void setUserPicture(String userId, Picture picture)
    {
        throw new FlowableException("nope");
    }

    @Override
    public Picture getUserPicture(String userId)
    {
        return commandExecutor.execute(new GetUserPictureCmd(userId));
    }

    @Override
    public String getUserInfo(String userId, String key)
    {
        return commandExecutor.execute(new GetUserInfoCmd(userId, key));
    }

    @Override
    public List<String> getUserInfoKeys(String userId)
    {
        return commandExecutor.execute(new GetUserInfoKeysCmd(userId, IdentityInfoEntity.TYPE_USERINFO));
    }

    @Override
    public void setUserInfo(String userId, String key, String value)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void deleteUserInfo(String userId, String key)
    {
        throw new FlowableException("nope");
    }

    @Override
    public Privilege createPrivilege(String name)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void addUserPrivilegeMapping(String privilegeId, String userId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void deleteUserPrivilegeMapping(String privilegeId, String userId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void addGroupPrivilegeMapping(String privilegeId, String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public void deleteGroupPrivilegeMapping(String privilegeId, String groupId)
    {
        throw new FlowableException("nope");
    }

    @Override
    public List<PrivilegeMapping> getPrivilegeMappingsByPrivilegeId(String privilegeId)
    {
        return null;
    }

    @Override
    public void deletePrivilege(String id)
    {
        throw new FlowableException("nope");
    }

    @Override
    public PrivilegeQuery createPrivilegeQuery()
    {
        return null;
    }

    @Override
    public List<Group> getGroupsWithPrivilege(String name)
    {
        return new AditoGroupQuery().executeList(null);
    }

    @Override
    public List<User> getUsersWithPrivilege(String name)
    {
        return new AditoUserQuery().executeList(null);
    }
}
