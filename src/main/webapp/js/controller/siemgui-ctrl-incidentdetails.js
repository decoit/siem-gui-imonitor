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
	.controller("IncidentDetailsController", ['$scope', '$log', '$modal', 'IncidentsService', 'StompSubscriptionService', 'details', function($scope, $log, $modal, viewService, subService, details) {
		var stompChannels = [];
		$scope.history = [];
		$scope.details = details;
		$scope.timeToBook = 0;

		$log.debug($scope.details);

		stompChannels.push({
			name:  '/user/queue/incidents/history',
			callback: historyCallback
		});
		stompChannels.push({
			name: '/user/queue/incidents/timebooked',
			callback: timeBookedCallback
		});

		subService.subscribeChannels('IncidentDetailsController', stompChannels);

		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('IncidentDetailsController');
		});

		viewService.requestHistory(details.incDetails.id);


		/**
		 * Make the current user the owner of the specified incident.
		 * Only works for unclaimed incidents. This call is forwarded to the
		 * parent controller.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		$scope.takeIncident = function(incId) {
			// Forward the call to the parent controller
			$scope.$parent.takeIncident(incId);

			// Close the modal dialog
			$scope.$close('Incident taken');
		};


		/**
		 * Begin working on the specified incident. It will be marked as "IN_PROGRESS" after this operation.
		 * This operation can only be successfully completed if the active user is the owner of the ticket.
		 * This call is forwarded to the parent controller.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		$scope.beginWorkOnIncident = function(incId) {
			$scope.$parent.beginWorkOnIncident(incId);

			// Close the modal dialog
			$scope.$close('Started working on incident');
		};


		/**
		 * Finish working on the specified incident. It will be marked as "DONE" after this operation.
		 * This operation can only be successfully completed if the active user is the owner of the ticket.
		 * This call is forwarded to the parent controller.
		 *
		 * @param {Integer} incId Incident ID
		 * @returns {undefined}
		 */
		$scope.finishWorkOnIncident = function(incId) {
			$scope.$parent.finishWorkOnIncident(incId);

			// Close the modal dialog
			$scope.$close('Finished working on incident');
		};


		$scope.bookTime = function() {
			$log.debug('Book time on incident: ' + $scope.timeToBook + ' minutes');

			if($scope.timeToBook > 0) {
				viewService.bookTimeOnIncident($scope.details.incDetails.id, $scope.timeToBook);

				$scope.details.incDetails.ticket.timeWorked += parseInt($scope.timeToBook);
				$scope.timeToBook = 0;
			}
		};


		$scope.openCommentForm = function() {
			$log.debug('Close incident details');
			$scope.$close('Finished working on incident');

			$log.debug('Open comment form');
			$modal.open({
				templateUrl: 'templates/incidentcomment.html',
				controller: 'IncidentCommentFormController',
				scope: $scope.$parent,
				size: 'lg',
				resolve: {
					details: function() {
						return $scope.details;
					},
					history: function() {
						return $scope.history;
					},
					formatHistoryItem: function() {
						return $scope.formatHistoryItem;
					}
				}
			});
		};


		$scope.formatHistoryItem = function(item) {
			var src = '<div class="panel-heading">';

			src += item.title;
			src += '</div><div class="panel-body">';
			src += item.content;
			src += '</div>';

			return src;
		};


		function historyCallback(data) {
			$log.debug('historyCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('historyCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.history = fetchedResult.history;
		}


		function timeBookedCallback() {
			viewService.requestHistory(details.incDetails.id);
		}
	}]);