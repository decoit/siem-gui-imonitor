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
	.controller('PushNotificationController', ['$scope', '$log', '$http', 'StompSubscriptionService', function($scope, $log, $http, subService) {
		var stompChannels = [];

		stompChannels.push({
			name:'/topic/push',
			callback:pushNotificationCallback
		});
		stompChannels.push({
			name:'/topic/heartbeat',
			callback:heartbeatCallback
		});

		/**
		 * Remove all notifications from the list in the view
		 * @returns {undefined}
		 */
		$scope.clearNotifications = function() {
			$log.debug('Push notifications cleared');

			$scope.notifications = [];
		};

		$scope.notifications = [];

		subService.subscribeChannels('PushNotificationController', stompChannels);


		/**
		 * Callback to be bound to the push notification STOMP message channel. Will add a received
		 * notification to the notification list in the view. New notifications will be shown first.
		 *
		 * @param {Object} data Received STOMP message
		 * @returns {undefined}
		 */
		function pushNotificationCallback(data) {
			$log.debug('Push notification received:');
			$log.debug(data);

			var content = JSON.parse(data.body);

			$log.debug('pushNotificationCallback, content:')
			$log.debug(content);

			$scope.notifications.unshift(content);
		}


		/**
		 * Callback to be registered to incoming heartbeat messages.
		 * It will process the incoming data and send an alive notification to the backend.
		 *
		 * @param {Object} data Received STOMP message
		 * @returns {undefined}
		 */
		function heartbeatCallback(data) {
			$log.debug('Heartbeat received:');
			$log.debug(data);

			var heartbeat = JSON.parse(data.body);
			var payload = {
				heartbeatId: heartbeat.heartbeatId
			};


			$http.post('heartbeat/alive', payload);
			$log.debug('Alive notification sent');
		}
	}]);