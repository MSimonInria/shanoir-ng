/**
 * Shanoir NG - Import, manage and share neuroimaging data
 * Copyright (C) 2009-2019 Inria - https://www.inria.fr/
 * Contact us on https://project.inria.fr/shanoir/
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://www.gnu.org/licenses/gpl-3.0.html
 */

package org.shanoir.ng.role;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Magic repository for roles
 *
 * @author jlouis
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long>, RoleRepositoryCustom {

	/**
	 * Find role by its name.
	 *
	 * @param name
	 *            name.
	 * @return a role or null/
	 */
	Optional<Role> findByName(String name);

}
