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
	.controller('ErrorMessagesController', ['$scope', '$modal', '$log', 'StompSubscriptionService', function($scope, $modal, $log, subService) {
		var stompChannels = [];
		var logoutForced = false;

		$scope.alerts = [];

		stompChannels.push({
			name:  '/user/queue/errors',
			callback: errorNotificationCallback
		});

		subService.subscribeChannels('ErrorMessagesController', stompChannels);

		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('ErrorMessagesController');
		});


		/**
		 * Close an error message.
		 *
		 * @param {Integer} index Index of the notification
		 * @returns {undefined}
		 */
		$scope.closeAlert = function(index) {
			$scope.alerts.splice(index, 1);
		};


		/**
		 * Callback function to be registered to incoming error notifications.
		 * It processes the data of the notification and displays an error message. If
		 * a forced logout is required it will open a modal dialog preventing further
		 * interaction with the user interface.
		 *
		 * @param {Object} data Message data
		 * @returns {undefined}
		 */
		function errorNotificationCallback(data) {
			$log.debug('errorNotificationCallback, data:');
			$log.debug(data);

			var errorMsg = JSON.parse(data.body);

			$log.debug('errorNotificationCallback, errorMsg:');
			$log.debug(errorMsg);

			if(!logoutForced) {
				if(errorMsg.forceLogout) {
					logoutForced = true;

					$modal.open({
						templateUrl: 'templates/forcelogout.html',
						controller: 'ForceLogoutController',
						scope: $scope,
						backdrop: 'static',
						resolve: {
							msg: function() {
								return errorMsg;
							}
						}
					});
				}
				else {
					$scope.alerts.push(errorMsg);
				}
			}
			else {
				$log.debug('Error forcing logout is being processed, discarding error message');
			}
		};
	}]);