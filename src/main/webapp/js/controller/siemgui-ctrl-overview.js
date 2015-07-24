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
	.controller('OverviewController', ['$scope', '$log', 'OverviewService', 'StompSubscriptionService', function($scope, $log, viewService, subService) {
		var stompChannels = [];

		$scope.info = {
			tickets: {
				ticketsNew: 'laden...',
				ticketsOpen: 'laden...',
				ticketsResolved: 'laden...',
				myTicketsNew: 'laden...',
				myTicketsOpen: 'laden...',
				myTicketsResolved: 'laden...'
			},
			incidents: {
				incidentsNew: 'laden...',
				incidentsInProgress: 'laden...',
				incidentsUnknown: 'laden...',
				incidentsLowRisk: 'laden...',
				incidentsMediumRisk: 'laden...',
				incidentsHighRisk: 'laden...'
			},
			events: {
				eventsToday: 'laden...',
				eventsYesterday: 'laden...',
				eventsThisWeek: 'laden...',
				eventsLastWeek: 'laden...'
			}
		};

		// Message channel to receive overview information summary
		stompChannels.push({
			name:  '/user/queue/overview/reqinfo/tickets',
			callback: overviewInfoTicketsCallback
		});
		stompChannels.push({
			name:  '/user/queue/overview/reqinfo/incidents',
			callback: overviewInfoIncidentsCallback
		});
		stompChannels.push({
			name:  '/user/queue/overview/reqinfo/events',
			callback: overviewInfoEventsCallback
		});

		subService.subscribeChannels('OverviewController', stompChannels);

		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('OverviewController');
		});

		$scope.$parent.navpath = 'overview';

		// TODO: Just for testing purposes
		$scope.$parent.dynamic = Math.round(Math.random() * 100);

		viewService.requestOverviewTicktsInfo();
		viewService.requestOverviewIncidentsInfo();
		viewService.requestOverviewEventsInfo();


		/**
		 * This callback is called when ticket summary for the overview is received via
		 * the WebSocket.
		 *
		 * @param {Object} data Received STOMP message object
		 * @returns {undefined}
		 */
		function overviewInfoTicketsCallback(data) {
			$log.debug('overviewInfoTicketsCallback, data:');
			$log.debug(data);

			var ticketsSummary = JSON.parse(data.body);

			$log.debug('overviewInfoTicketsCallback, ticketsSummary:');
			$log.debug(ticketsSummary);

			$scope.info.tickets = ticketsSummary;
		}


		/**
		 * This callback is called when incident summary for the overview is received via
		 * the WebSocket.
		 *
		 * @param {Object} data Received STOMP message object
		 * @returns {undefined}
		 */
		function overviewInfoIncidentsCallback(data) {
			$log.debug('overviewInfoIncidentsCallback, data:');
			$log.debug(data);

			var incidentsSummary = JSON.parse(data.body);

			$log.debug('overviewInfoIncidentsCallback, incidentsSummary:');
			$log.debug(incidentsSummary);

			$scope.info.incidents = incidentsSummary;
		}


		/**
		 * This callback is called when event summary for the overview is received via
		 * the WebSocket.
		 *
		 * @param {Object} data Received STOMP message object
		 * @returns {undefined}
		 */
		function overviewInfoEventsCallback(data) {
			$log.debug('overviewInfoEventsCallback, data:');
			$log.debug(data);

			var eventsSummary = JSON.parse(data.body);

			$log.debug('overviewInfoEventsCallback, eventsSummary:');
			$log.debug(eventsSummary);

			switch(eventsSummary.timeframe) {
				case 'TODAY':
					$scope.info.events.eventsToday = eventsSummary.count;
					break;
				case 'YESTERDAY':
					$scope.info.events.eventsYesterday = eventsSummary.count;
					break;
				case 'THIS_WEEK':
					$scope.info.events.eventsThisWeek = eventsSummary.count;
					break;
				case 'LAST_WEEK':
					$scope.info.events.eventsLastWeek = eventsSummary.count;
					break;
				default:
					$log.debug('Unknown request timeframe: ' + eventsSummary.timeframe);
			}
		}
	}]);