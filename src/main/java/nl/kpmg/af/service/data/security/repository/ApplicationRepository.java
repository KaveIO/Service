/*
 * Copyright 2015 KPMG N.V. (unless otherwise stated).
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */
package nl.kpmg.af.service.data.security.repository;

import nl.kpmg.af.service.data.security.Application;
import org.springframework.data.repository.Repository;

/**
 * @author mhoekstra
 */
public interface ApplicationRepository extends Repository<Application, Long> {

    public Application findOneByName(String name);

}
