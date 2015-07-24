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
	.factory('OverviewService', ['$log', 'WebSocketService', function($log, wsService) {
		// We return this object to anything injecting our service
		var Service = {};


		/**
		 * Public service function to send a STOMP message to request the overview events summary information.
		 *
		 * @returns {undefined}
		 */
		Service.requestOverviewEventsInfo = function() {
			$log.debug('Sending message to request overview events summary information');

			// Request event count for today, yesterday, this week and last week
			wsService.send('/app/overview/reqinfo/events', {timeframe: 'TODAY'});
			wsService.send('/app/overview/reqinfo/events', {timeframe: 'YESTERDAY'});
			wsService.send('/app/overview/reqinfo/events', {timeframe: 'THIS_WEEK'});
			wsService.send('/app/overview/reqinfo/events', {timeframe: 'LAST_WEEK'});
		};


		/**
		 * Public service function to send a STOMP message to request the overview tickets summary information.
		 *
		 * @returns {undefined}
		 */
		Service.requestOverviewTicktsInfo = function() {
			$log.debug('Sending message to request overview tickets summary information');

			wsService.send('/app/overview/reqinfo/tickets', {});
		};


		/**
		 * Public service function to send a STOMP message to request the overview incidents summary information.
		 *
		 * @returns {undefined}
		 */
		Service.requestOverviewIncidentsInfo = function() {
			$log.debug('Sending message to request overview incidents summary information');

			wsService.send('/app/overview/reqinfo/incidents', {});
		};


		return Service;
	}]);