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

angular.module('SiemGui')
	.controller('UserDetailsController', ['$scope', '$rootScope', '$log', 'UserDetailsService', function($scope, $rootScope, $log, viewService) {
		viewService.promise.then(ownUserDetailsCallback);

		/**
		 * Callback to be bound to the push notification STOMP message channel. Will add a received
		 * notification to the notification list in the view. New notifications will be shown first.
		 *
		 * @returns {undefined}
		 */
		function ownUserDetailsCallback() {
			//$log.debug(response);
			$log.debug(viewService.activeUser);

			$scope.userdetails = viewService.activeUser;
			$rootScope.username = viewService.activeUser.username;
			$rootScope.userIsAdmin = (viewService.activeUser.roles.indexOf('ROLE_SIEM_ADMIN') !== -1);
		}
	}]);