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
package de.decoit.siemgui.exception;


/**
 * This exception can be used to send a custom error message to the frontend.
 * Controllers that support it must declare a specific @MessageExceptionHandler
 * method. The method should log the error including the Exception stack trace
 * and then send a MsgErrorNotification including the Exception message to the
 * frontend.
 *
 * @author Thomas Rix (rix@decoit.de)
 */
public class ErrorMsgToFrontendException extends RuntimeException {
	public ErrorMsgToFrontendException(String msg) {
		super(msg);
	}


	public ErrorMsgToFrontendException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
