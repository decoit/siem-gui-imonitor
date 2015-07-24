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
	.controller('ThreatLevelController', ['$scope', '$log', 'StompSubscriptionService', 'ThreatLevelService', function($scope, $log, subService, tlService) {
		var stompChannels = [];

		$scope.threatlevel = [];

		stompChannels.push({
			name:'/topic/push',
			callback:pushNotificationCallback
		});
		stompChannels.push({
			name:'/topic/threatlevel',
			callback: threatLevelCallback
		});

		subService.subscribeChannels('ThreatLevelController', stompChannels);

		tlService.requestThreatLevel();


		/**
		 * Callback to be bound to the push notification STOMP message channel.
		 *
		 * @returns {undefined}
		 */
		function pushNotificationCallback() {
			$log.debug('Push notification received, updating threat level');

			tlService.requestThreatLevel();
		}


		/**
		 * Callback to be bound to the threat level STOMP message channel.
		 * Will parse the threat level data and update the view accordingly.
		 *
		 * @param {Object} data Received STOMP message
		 * @returns {undefined}
		 */
		function threatLevelCallback(data) {
			$log.debug('threatLevelCallback, data');
			$log.debug(data);

			var content = JSON.parse(data.body);

			$log.debug('threatLevelCallback, content');
			$log.debug(content);

			$scope.threatlevel = [];

			if(content.lowRiskDisplay > 0) {
				$scope.threatlevel.push({
					value: content.lowRiskDisplay,
					type: 'success'
				});
			}

			if(content.mediumRiskDisplay > 0) {
				$scope.threatlevel.push({
					value: content.mediumRiskDisplay,
					type: 'warning'
				});
			}

			if(content.highRiskDisplay > 0) {
				$scope.threatlevel.push({
					value: content.highRiskDisplay,
					type: 'danger'
				});
			}
		}
	}]);