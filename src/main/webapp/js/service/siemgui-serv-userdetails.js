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
	.factory('UserDetailsService', ['$http', '$log', function($http, $log) {
		// We return this object to anything injecting our service
		var Service = {};

		Service.activeUser = {};

		Service.promise = $http.get('get-active-user', {responseType:'json'});
		Service.promise.then(httpCallback);


		/**
		 * Private service function used as callback on the HTTP request.
		 * It stores the received data into the public member variable activeUser.
		 *
		 * @param {Object} response HTTP response
		 * @returns {undefined}
		 */
		function httpCallback(response) {
			$log.debug(response);

			Service.activeUser = response.data;
		}


		return Service;
	}]);