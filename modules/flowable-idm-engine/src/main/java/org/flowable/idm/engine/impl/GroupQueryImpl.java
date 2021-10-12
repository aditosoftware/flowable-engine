/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flowable.idm.engine.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.query.QueryCacheValues;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.common.engine.impl.query.AbstractQuery;
import org.flowable.idm.api.Group;
import org.flowable.idm.api.GroupQuery;
import org.flowable.idm.api.GroupQueryProperty;
import org.flowable.idm.api.User;
import org.flowable.idm.engine.impl.persistence.entity.GroupEntityImpl;
import org.flowable.idm.engine.impl.persistence.entity.UserEntityImpl;
import org.flowable.idm.engine.impl.util.CommandContextUtil;
import org.flowable.idm.engine.impl.ws.GroupWrapper;
import org.flowable.idm.engine.impl.ws.UserWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

/**
 * @author Joram Barrez
 */
@Component
public class GroupQueryImpl extends AbstractQuery<GroupQuery, Group> implements GroupQuery, QueryCacheValues {

    private static final long serialVersionUID = 1L;
    protected String id;
    protected List<String> ids;
    protected String name;
    protected String nameLike;
    protected String nameLikeIgnoreCase;
    protected String type;
    protected String userId;
    protected List<String> userIds;

    @Value("${aditoUrl}")
    private String aditoUrl;

    public GroupQueryImpl() {
    }

    public GroupQueryImpl(CommandContext commandContext) {
        super(commandContext);
    }

    public GroupQueryImpl(CommandExecutor commandExecutor) {
        super(commandExecutor);
    }

    @Override
    public GroupQuery groupId(String id) {
        if (id == null) {
            throw new FlowableIllegalArgumentException("Provided id is null");
        }
        this.id = id;
        return this;
    }

    @Override
    public GroupQuery groupIds(List<String> ids) {
        if (ids == null) {
            throw new FlowableIllegalArgumentException("Provided id list is null");
        }
        this.ids = ids;
        return this;
    }

    @Override
    public GroupQuery groupName(String name) {
        if (name == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.name = name;
        return this;
    }

    @Override
    public GroupQuery groupNameLike(String nameLike) {
        if (nameLike == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.nameLike = nameLike;
        return this;
    }

    @Override
    public GroupQuery groupNameLikeIgnoreCase(String nameLikeIgnoreCase) {
        if (nameLikeIgnoreCase == null) {
            throw new FlowableIllegalArgumentException("Provided name is null");
        }
        this.nameLikeIgnoreCase = nameLikeIgnoreCase.toLowerCase();
        return this;
    }

    @Override
    public GroupQuery groupType(String type) {
        if (type == null) {
            throw new FlowableIllegalArgumentException("Provided type is null");
        }
        this.type = type;
        return this;
    }

    @Override
    public GroupQuery groupMember(String userId) {
        if (userId == null) {
            throw new FlowableIllegalArgumentException("Provided userId is null");
        }
        this.userId = userId;
        return this;
    }

    @Override
    public GroupQuery groupMembers(List<String> userIds) {
        if (userIds == null) {
            throw new FlowableIllegalArgumentException("Provided userIds is null");
        }
        this.userIds = userIds;
        return this;
    }

    // sorting ////////////////////////////////////////////////////////

    @Override
    public GroupQuery orderByGroupId() {
        return orderBy(GroupQueryProperty.GROUP_ID);
    }

    @Override
    public GroupQuery orderByGroupName() {
        return orderBy(GroupQueryProperty.NAME);
    }

    @Override
    public GroupQuery orderByGroupType() {
        return orderBy(GroupQueryProperty.TYPE);
    }

    // results ////////////////////////////////////////////////////////

    @Override
    public long executeCount(CommandContext commandContext) {
        return executeList(commandContext).size();
    }

    @Override
    public List<Group> executeList(CommandContext commandContext) {
        List<Group> groups = new ArrayList<>();

        if (aditoUrl != null && !aditoUrl.isEmpty()) {
            try {
                SslContext sslContext = SslContextBuilder
                        .forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)
                        .build();
                HttpClient httpClient = HttpClient.create().secure(t -> t.sslContext(sslContext));


                WebClient.Builder clientBuilder = WebClient.builder()
                        .baseUrl(aditoUrl)
                        .clientConnector(new ReactorClientHttpConnector(httpClient))
                        .defaultHeaders(headers -> headers.setBasicAuth("flowableIdmService", "HczABCxBEUKSmwQEnT8vbmkE8Bj1hcXOKSbsLWBg"))
                        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

                Gson gson = new Gson();

                clientBuilder.defaultHeader("Id", id);
                clientBuilder.defaultHeader("Ids", gson.toJson(ids));
                clientBuilder.defaultHeader("Name", name);
                clientBuilder.defaultHeader("Namelike", nameLike);
                clientBuilder.defaultHeader("Namelikeignorecase", nameLikeIgnoreCase);
                clientBuilder.defaultHeader("Userid", userId);
                clientBuilder.defaultHeader("Userids", gson.toJson(userIds));
                clientBuilder.defaultHeader("Type", type);

                WebClient.RequestHeadersSpec<?> spec = clientBuilder.build().get()
                        .uri("/services/rest/workflowRoles_rest");
                String wsResult = spec.retrieve().bodyToMono(String.class).block();

                GroupWrapper[] wsGroups = gson.fromJson(wsResult, GroupWrapper[].class);
                groups = Arrays.stream(wsGroups).map(wsGroup -> {
                    Group group = new GroupEntityImpl();
                    group.setId(wsGroup.getId());
                    group.setName(wsGroup.getName());
                    group.setType(wsGroup.getType());
                    return group;
                }).collect(Collectors.toList());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return groups;
    }

    // getters ////////////////////////////////////////////////////////

    @Override
    public String getId() {
        return id;
    }

    public List<String> getIds() {
        return ids;
    }

    public String getName() {
        return name;
    }

    public String getNameLike() {
        return nameLike;
    }

    public String getNameLikeIgnoreCase() {
        return nameLikeIgnoreCase;
    }

    public String getType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

}
