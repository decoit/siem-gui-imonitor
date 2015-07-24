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
	.controller("EventsController", ['$scope', '$log', '$modal', 'EventsService', 'StompSubscriptionService', function($scope, $log, $modal, viewService, subService) {
		var stompChannels = [];

		// Message channel to receive the event list
		stompChannels.push({
			name:  '/user/queue/events/page',
			callback: eventsPageCallback
		});
		stompChannels.push({
			name:  '/user/queue/events/pagination',
			callback: eventsPaginationCallback
		});

		subService.subscribeChannels('EventsController', stompChannels);

		$scope.$parent.navpath = 'events';

		$scope.eventsList = [];
		$scope.totalEvents = 0;
		$scope.currentPage = 1;
		$scope.showPagination = false;
		$scope.itemsPerPage = 1;
		$scope.currentStartDate = null;
		$scope.currentEndDate = null;
		$scope.displayTimeframeInfo = false;

		// Datepickers are hidden on load
		$scope.dpStartOpened = false;
		$scope.dpEndOpened = false;

		// Initialize datepicker boundaries
		$scope.maxDpStartDate = new Date();
		$scope.maxDpStartDate.setHours(0);
		$scope.maxDpStartDate.setMinutes(0);
		$scope.maxDpStartDate.setSeconds(0);
		$scope.maxDpStartDate.setMilliseconds(0);

		$scope.maxDpEndDate = new Date();
		$scope.maxDpEndDate.setHours(23);
		$scope.maxDpEndDate.setMinutes(59);
		$scope.maxDpEndDate.setSeconds(59);
		$scope.maxDpEndDate.setMilliseconds(0);

		// Initialize date values
		$scope.startDate = $scope.maxDpStartDate;
		$scope.endDate = $scope.maxDpEndDate;

		// Global options for all datepickers
		$scope.dateOptions = {
			startingDay: 1
		};


		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('EventsController');
		});


		/**
		 * Request new event list of page has changed
		 *
		 * @returns {undefined}
		 */
		$scope.pageChanged = function() {
			$log.debug("Page changed to " + $scope.currentPage);

			viewService.requestEventsList($scope.currentStartDate, $scope.currentEndDate, $scope.currentPage);
		};


		/**
		 * Determine the type of an event and return it as a string.
		 *
		 * @param {String} type Event type string from backend
		 * @returns {String} Event type string
		 */
		$scope.determineEventType = function(type) {
			switch(type) {
				case 'GENERAL':
					return 'Allgemein';
				case 'SNORT':
					return 'Snort';
				case 'NMAP':
					return 'Nmap';
				case 'OPEN_VAS':
					return 'OpenVAS';
				case 'FIM':
					return 'FIM';
				case 'TSAD':
					return 'TSAD';
				case 'ANDROID':
					return 'Android';
				case 'SERVICE_STATUS':
					return 'Status';
				default:
					$log.debug('Unknown event type detected' + type);
					return 'Unbekannt';
			}
		};


		/**
		 * Build an info string about the content of an event.
		 *
		 * @param {Object} evt Event object
		 * @returns {String} Info string about event content
		 */
		$scope.formatEventContent = function(evt) {
			if(typeof evt === 'object') {
				switch(evt.type) {
					case 'SNORT':
						return '' + evt.data['sensor-id'] + ':' + evt.data['sensor-name'] + ' - ' + evt.message;
					case 'NMAP':
						return formatNmapContent(evt);
					case 'OPEN_VAS':
						return evt.message;
					case 'GENERAL':
						return evt.data;
					case 'FIM':
						return '' + evt.ipsrc + ': ' + '"' + evt.message + '"';
					case 'TSAD':
						return evt.message;
					case 'ANDROID':
						return evt.message;
					case 'SERVICE_STATUS':
						return 'Host ' + evt.ipsrc + ' - ' + evt.message;
					default:
						$log.debug('Unknown event content type detected: ' + evt.type);
						return 'Unbekannt';
				}
			}
			else {
				$log.debug('Unknown event content data type detected');

				return 'Unbekannt';
			}
		};


		/**
		 * Check whether the event content contains further details ('is an object').
		 *
		 * @param {Object} evt Event object
		 * @returns {Boolean} Event content has further details (true) or not (false)
		 */
		$scope.eventDetailsAvailable = function(evt) {
			return (typeof evt === 'object' && evt.type !== 'GENERAL' && evt.type !== 'SERVICE_STATUS');
		};


		/**
		 * Open the modal dialog for displaying event content details.
		 *
		 * @param {Integer} id Array index of the event
		 * @returns {undefined}
		 */
		$scope.openEventDetails = function(id) {
			$log.debug('Open event details for ID: ' + id);

			$modal.open({
				templateUrl: 'templates/eventdetails.html',
				controller: 'EventDetailsController',
				scope: $scope,
				resolve: {
					details: function() {
						return $scope.eventsList[id];
					}
				}
			});
		};


		/**
		 * Toggle display of the start date datepicker.
		 *
		 * @param {Object} $event AngularJS event object
		 * @returns {undefined}
		 */
		$scope.toggleDpStart = function($event) {
			$log.debug('Toggle datepicker (start)');

			$event.preventDefault();
			$event.stopPropagation();

			$scope.dpStartOpened = !$scope.dpStartOpened;
		};


		/**
		 * Toggle display of the end date datepicker.
		 *
		 * @param {Object} $event AngularJS event object
		 * @returns {undefined}
		 */
		$scope.toggleDpEnd = function($event) {
			$log.debug('Toggle datepicker (end)');

			$event.preventDefault();
			$event.stopPropagation();

			$scope.dpEndOpened = !$scope.dpEndOpened;
		};


		/**
		 * Event callback for the change event on the start date datepicker.
		 * May toggle display of the long timeframe info alert.
		 *
		 * @returns {undefined}
		 */
		$scope.startDateChanged = function() {
			$log.debug('Start date changed');
		};


		/**
		 * Event callback for the change event on the end date datepicker.
		 * May toggle display of the long timeframe info alert and may alter
		 * the start date to prevent invalid start dates (for example later start than end).
		 *
		 * @returns {undefined}
		 */
		$scope.endDateChanged = function() {
			$log.debug('End date changed');

			if($scope.endDate < $scope.startDate) {
				$scope.startDate = $scope.endDate;
			}

			$scope.maxDpStartDate = $scope.endDate;
		};


		/**
		 * Fetch events in the specified timeframe from the backend.
		 *
		 * @returns {undefined}
		 */
		$scope.fetchEventPagination = function() {
			var now = new Date();

			$scope.showPagination = false;

			$scope.eventsList = [];
			$scope.currentStartDate = $scope.startDate;

			if($scope.endDate > now) {
				$scope.currentEndDate = now;
			}
			else {
				$scope.currentEndDate = $scope.endDate;
			}

			viewService.requestEventPagination($scope.currentStartDate, $scope.currentEndDate);
		};


		// Initially fetch events
		$scope.fetchEventPagination();


		/**
		 * Callback for the STOMP message containing the event list.
		 * Stores the message contents in the $scope.
		 *
		 * @param {Object} data STOMP message data
		 * @returns {undefined}
		 */
		function eventsPageCallback(data) {
			$log.debug('eventsPageCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('eventsPageCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.eventsList = fetchedResult.eventlist;

			$log.debug("Event list size: " + fetchedResult.eventlist.length);
		}


		/**
		 * Callback for the STOMP message containing the pagination information.
		 * After parsing the pagination information this method will request
		 * events for page 1 of this pagination.
		 *
		 * @param {Object} data STOMP message data
		 * @returns {undefined}
		 */
		function eventsPaginationCallback(data) {
			$log.debug('eventsBoundariesCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('eventsBoundariesCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.totalEvents = fetchedResult.total;
			$scope.itemsPerPage = fetchedResult.pageSize;
			$scope.currentPage = 1;

			$scope.showPagination = true;

			$scope.pageChanged();
		}


		/**
		 * Format the result of a Nmap event for display in the event list.
		 *
		 * @param {Object} evt Nmap event object
		 * @returns {String} Result message
		 */
		function formatNmapContent(evt) {
			var rv = evt['status'];

			if(evt.status === 'OK') {
				rv += ' - Keine Probleme gefunden';
			}
			else if(evt.status === 'WARNING') {
				rv += ' - Geschlossene Ports: ' + evt.data['invalid-closed-ports'].length;
			}
			else if(evt.status === 'CRITICAL') {
				rv += ' - Geschlossene Ports: ' + evt.data['invalid-closed-ports'].length;
				rv += ' - Offene Ports: ' + evt.data['invalid-open-ports'].length;
			}
			else {
				rv += ' - Unbekannter Status';
			}

			return rv;
		}
	}]);