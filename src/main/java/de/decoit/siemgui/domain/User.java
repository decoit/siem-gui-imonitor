/* 
 * Copyright (C) 2015 DECOIT GmbH
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.decoit.siemgui.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



/**
 * An object of this class represents a User read from the user management system.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = { "id" })
public class User {
	private final long id;
	private final String username;
	private String sessionId = null;
	private String realName;
	private boolean enabled;
	private boolean privileged;
	private boolean siemAuthorized;
	private boolean siemAdminAuthorized;


	/**
	 * Create a new User object.
	 *
	 * @param id User ID
	 * @param username Username
	 * @param realName User's real name
	 * @param enabled User is enabled or not
	 * @param privileged User has priviledged flag set or not
	 * @param siemAccess User can access the SIEM GUI or not
	 * @param siemAdmin User can access the SIEM GUI admin area or not
	 */
	public User(long id, String username, String realName, boolean enabled, boolean privileged, boolean siemAccess, boolean siemAdmin) {
		this.id = id;
		this.username = username;
		this.realName = realName;
		this.enabled = enabled;
		this.privileged = privileged;
		this.siemAuthorized = siemAccess;
		this.siemAdminAuthorized = siemAdmin;
	}
}
