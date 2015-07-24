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
	.factory('IncidentsService', ['$log', 'WebSocketService', function($log, wsService) {
		// We return this object to anything injecting our service
		var Service = {};


		/**
		 * Public service function to request a list of all active incidents.
		 *
		 * @returns {undefined}
		 */
		Service.requestActiveIncidents = function() {
			$log.debug('Sending message to request list of active incidents');

			var payload = {};

			wsService.send('/app/incidents/listactive', payload);
		};


		/**
		 * Public service function to request a list of all resolved incidents.
		 *
		 * @returns {undefined}
		 */
		Service.requestResolvedIncidents = function() {
			$log.debug('Sending message to request list of resolved incidents');

			var payload = {};

			wsService.send('/app/incidents/listresolved', payload);
		};


		/**
		 * Public service funtion to request a list of history items for the specified incident.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		Service.requestHistory = function(incId) {
			$log.debug('Sending message to request the history of incident with ID ' + incId);

			var payload = {
				incidentId: incId
			};

			wsService.send('/app/incidents/requesthistory', payload);
		};


		/**
		 * Public service function to request takeover of a specific incident.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		Service.takeIncident = function(incId) {
			$log.debug('Sending message to take the incident with ID ' + incId);

			var payload = {
				incidentId: incId
			};

			wsService.send('/app/incidents/take', payload);
		};


		/**
		 * Public service function to begin work on a specific incident.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		Service.beginWorkOnIncident = function(incId) {
			$log.debug('Sending message to start working on incident with ID ' + incId);

			var payload = {
				incidentId: incId
			};

			wsService.send('/app/incidents/beginwork', payload);
		};


		/**
		 * Public service function to finish work on a specific incident.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		Service.finishWorkOnIncident = function(incId) {
			$log.debug('Sending message to finish working on incident with ID ' + incId);

			var payload = {
				incidentId: incId
			};

			wsService.send('/app/incidents/finishwork', payload);
		};


		/**
		 * Public service function to post a comment on a specified incident.
		 *
		 * @param {Integer} incId Incident ID
		 * @param {String} text Content of the comment
		 * @returns {undefined}
		 */
		Service.postComment = function(incId, text) {
			$log.debug('Sending message to post comment on incident with ID ' + incId);

			var payload = {
				incidentId: incId,
				commentText: text
			};

			wsService.send('/app/incidents/postcomment', payload);
		};


		Service.bookTimeOnIncident = function(incId, time) {
			$log.debug('Sending message to book ' + time + ' minutes on incident with ID ' + incId);

			var payload = {
				incidentId: incId,
				minutes: time
			};

			wsService.send('/app/incidents/booktime', payload);
		};


		return Service;
	}]);