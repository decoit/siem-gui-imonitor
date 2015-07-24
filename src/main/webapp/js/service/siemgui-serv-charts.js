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
	.factory('ChartsService', ['$log', 'WebSocketService', function($log, wsService) {
		// We return this object to anything injecting our service
		var Service = {};


		/**
		 * Public service function the request data for a chart.
		 * This function requests the chart of today's events per hour.
		 *
		 * @param {Integer} chartIndex Index of the chart element in the view
		 * @returns {undefined}
		 */
		Service.requestEventsTodayChart = function(chartIndex) {
			$log.debug('Sending message to request chart information about today\'s events');

			var payload = {
				chartIndex: chartIndex
			};

			wsService.send('/app/charts/eventstoday', payload);
		};


		/**
		 * Public service function the request data for a chart.
		 * This function requests the chart of today's events per sensor.
		 *
		 * @param {Integer} chartIndex Index of the chart element in the view
		 * @returns {undefined}
		 */
		Service.requestEventsTodayPerSensorChart = function(chartIndex) {
			$log.debug('Sending message to request chart information about today\'s events per sensor');

			var payload = {
				chartIndex: chartIndex
			};

			wsService.send('/app/charts/eventstodaypersensor', payload);
		};


		/**
		 * Public service function the request data for a chart.
		 * This function requests the chart of active incidents per threat level.
		 *
		 * @param {Integer} chartIndex Index of the chart element in the view
		 * @returns {undefined}
		 */
		Service.requestIncidentsByThreatLevelChart = function(chartIndex) {
			$log.debug('Sending message to request chart information about active incidents by threat level');

			var payload = {
				chartIndex: chartIndex
			};

			wsService.send('/app/charts/incidentsbythreatlevel', payload);
		};


		return Service;
	}]);