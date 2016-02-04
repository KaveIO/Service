/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.core.repository;

import nl.kpmg.af.service.data.core.Role;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author mhoekstra
 */
@Document(collection = "proxy")
public interface RoleRepository extends CrudRepository<Role, Long> {

    public Role findOneByName(String name);

}
