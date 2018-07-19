package org.eurekaclinical.i2b2integration.service.resource;

/*-
 * #%L
 * Eureka! Clinical User Agreement Service
 * %%
 * Copyright (C) 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
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
 * #L%
 */
import com.google.inject.persist.Transactional; 
import java.util.ArrayList;
import java.util.List; 
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest; 
import javax.ws.rs.Path; 
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.dao.RoleDao;
import org.eurekaclinical.common.resource.AbstractUserResource;
import org.eurekaclinical.i2b2integration.service.entity.GroupEntity;
import org.eurekaclinical.i2b2integration.service.entity.RoleEntity;
import org.eurekaclinical.i2b2integration.service.entity.UserEntity;
import org.eurekaclinical.i2b2integration.service.entity.UserTemplateEntity;
import org.eurekaclinical.standardapis.dao.GroupDao;
import org.eurekaclinical.i2b2integration.client.comm.I2b2IntegrationUser;
import org.eurekaclinical.standardapis.dao.UserTemplateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/users")
@Transactional
public class UserResource extends AbstractUserResource<I2b2IntegrationUser, UserEntity, RoleEntity> {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(UserResource.class);

    private static final AutoAuthCriteriaParser AUTO_AUTH_CRITERIA_PARSER = new AutoAuthCriteriaParser();

    private final RoleDao<RoleEntity> roleDao;
    private final GroupDao<GroupEntity> groupDao;
    private final UserTemplateDao<UserTemplateEntity> userTemplateDao;
    private final UserDao<UserEntity> userDao;

    @Inject
    public UserResource(UserDao<UserEntity> inUserDao,
            RoleDao<RoleEntity> inRoleDao,
            GroupDao<GroupEntity> inGroupDao,
            UserTemplateDao<UserTemplateEntity> inUserTemplateDao) {
        super(inUserDao);
        this.userDao = inUserDao;
        this.roleDao = inRoleDao;
        this.groupDao = inGroupDao;
        this.userTemplateDao = inUserTemplateDao;
    }

    @Override
    protected I2b2IntegrationUser toComm(UserEntity userEntity, HttpServletRequest req) {
        I2b2IntegrationUser user = new I2b2IntegrationUser();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        List<Long> roles = new ArrayList<>();
        for (RoleEntity roleEntity : userEntity.getRoles()) {
            roles.add(roleEntity.getId());
        }
        user.setRoles(roles);
        List<Long> groups = new ArrayList<>();
        for (GroupEntity groupEntity : userEntity.getGroups()) {
            groups.add(groupEntity.getId());
        }
        user.setGroups(groups);
        return user;
    }

    @Override
    protected UserEntity toEntity(I2b2IntegrationUser user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        List<RoleEntity> roleEntities = this.roleDao.getAll();
        for (Long roleId : user.getRoles()) {
            for (RoleEntity roleEntity : roleEntities) {
                if (roleEntity.getId().equals(roleId)) {
                    userEntity.addRole(roleEntity);
                }
            }
        }
        List<GroupEntity> groupEntities = this.groupDao.getAll();
        for (Long groupId : user.getGroups()) {
            for (GroupEntity groupEntity : groupEntities) {
                if (groupEntity.getId().equals(groupId)) {
                    userEntity.addGroup(groupEntity);
                }
            }
        }
        return userEntity;
    }

}
