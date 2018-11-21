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
import org.eurekaclinical.standardapis.dao.GroupDao;
import org.eurekaclinical.i2b2integration.client.comm.I2b2IntegrationUser;

/**
 *
 * @author Andrew Post
 */
@Path("/protected/users")
@Transactional
public class UserResource extends AbstractUserResource<I2b2IntegrationUser, UserEntity, RoleEntity> {

    private final RoleDao<RoleEntity> roleDao;
    private final GroupDao<GroupEntity> groupDao;

    @Inject
    public UserResource(UserDao<RoleEntity, UserEntity> inUserDao,
            RoleDao<RoleEntity> inRoleDao,
            GroupDao<GroupEntity> inGroupDao) {
        super(inUserDao);
        this.roleDao = inRoleDao;
        this.groupDao = inGroupDao;
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
