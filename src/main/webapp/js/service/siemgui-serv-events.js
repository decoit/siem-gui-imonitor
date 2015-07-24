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
	.factory('EventsService', ['$log', '$filter', 'WebSocketService', function($log, $filter, wsService) {
		// We return this object to anything injecting our service
		var Service = {};


		/**
		 * Public service function to send a message to the backend to request all events in the specified ID range.
		 *
		 * @param {Date} startDate Begin of the event list boundaries
		 * @param {Date} endDate End of the event list boundaries
		 * @param {Integer} pageNumber Page number to load
		 * @returns {undefined}
		 */
		Service.requestEventsList = function(startDate, endDate, pageNumber) {
			$log.debug('Sending message to request event list');

			var payload = {
				from: $filter('date')(startDate, 'yyyy-MM-dd HH:mm:ss'),
				to: $filter('date')(endDate, 'yyyy-MM-dd HH:mm:ss'),
				pageIndex: pageNumber-1
			};

			wsService.send('/app/events/page', payload);
		};


		Service.requestEventPagination = function(startDate, endDate) {
			var payload = {
				from: $filter('date')(startDate, 'yyyy-MM-dd HH:mm:ss'),
				to: $filter('date')(endDate, 'yyyy-MM-dd HH:mm:ss')
			};

			wsService.send('/app/events/pagination', payload);
		};


		return Service;
	}]);