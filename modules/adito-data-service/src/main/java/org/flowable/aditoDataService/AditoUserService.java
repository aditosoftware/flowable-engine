package org.flowable.aditoDataService;

import com.google.gson.Gson;
import org.flowable.aditoDataService.model.UserGroupModel;
import org.flowable.aditoDataService.model.UserModel;
import org.flowable.aditoDataService.model.UserPrivilegeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AditoUserService
{
    @Autowired
    protected RestClientService restClientService;

    private final Logger logger = LoggerFactory.getLogger(AditoUserService.class);

    public List<UserModel> getUsers (Map<String, List<String>> params, Map<String,String> headers)
    {
        List<UserModel> users = new ArrayList<>();

        try {
            logger.debug("GET " + RestURLs.USERS);
            String wsResult = restClientService.get(RestURLs.USERS, new LinkedMultiValueMap<>(params), headers);
            Gson gson = new Gson();
            UserModel[] wsUsers = gson.fromJson(wsResult, UserModel[].class);
            users.addAll(Arrays.asList(wsUsers));
        } catch (Exception e) {
            logger.error("Failed to load users", e);
        }

        return users;
    }

    public List<UserGroupModel> getGroups (Map<String, List<String>> params, Map<String,String> headers)
    {
        List<UserGroupModel> groups = new ArrayList<>();

        try {
            logger.debug("GET " + RestURLs.ROLES);
            String wsResult = restClientService.get(RestURLs.ROLES, new LinkedMultiValueMap<>(params), headers);
            Gson gson = new Gson();
            UserGroupModel[] wsGroups = gson.fromJson(wsResult, UserGroupModel[].class);
            groups.addAll(Arrays.asList(wsGroups));
        } catch (Exception e) {
            logger.error("Failed to load user roles", e);
        }

        return groups;
    }

    public List<UserPrivilegeModel> getPrivileges ()
    {
        return null;
    }
}
