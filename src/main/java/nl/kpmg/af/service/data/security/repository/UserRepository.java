/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.security.repository;

import nl.kpmg.af.service.data.security.User;
import org.springframework.data.repository.Repository;

/**
 *
 * @author mhoekstra
 */
public interface UserRepository extends Repository<User, Long> {

    public User findOneByUsername(String username);

}
