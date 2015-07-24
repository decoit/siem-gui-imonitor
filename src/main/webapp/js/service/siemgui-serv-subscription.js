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
	.factory('StompSubscriptionService', ['$log', 'WebSocketService', function($log, wsService) {
		// We return this object to anything injecting our service
		var Service = {};
		var subscriptions = {};


		/**
		 * Private factory function for a subscription callback. The returned callback can be used for
		 * callbacks sent to WebSocketService.subscribe() to handle pending subscriptions.
		 *
		 * @param {String} serviceName Name of the service subscribing channels
		 * @returns {Function} Callback function
		 */
		function subscriptionCallbackFactory(serviceName) {
			if(typeof subscriptions[serviceName] === 'undefined') {
				subscriptions[serviceName] = {};
			}

			var cb = function(channel, sub) {
				$log.debug('Subsciption callback for: ' + serviceName + ' -> ' + channel);

				subscriptions[serviceName][channel] = sub;
			};

			return cb;
		}


		/**
		 * Public service function to subscribe to all listed channels and register callbacks for received messages.
		 * The channels array must be of the form [{name:'channelName',callback:messageCallback}, ...]
		 *
		 * @param {String} serviceName Name of the service subscribing channels
		 * @param {Array} channels Array containing channel names and callbacks
		 * @returns {undefined}
		 */
		Service.subscribeChannels = function(serviceName, channels) {
			$log.debug(serviceName + ' is subscribing to channels...');

			if(typeof subscriptions[serviceName] === 'undefined') {
				subscriptions[serviceName] = {};
			}

			angular.forEach(channels, function(channel) {
				var name = channel.name;
				var cb = channel.callback;
				var subCb = subscriptionCallbackFactory(serviceName);

				wsService.subscribe(name, cb, subCb);
			});

			$log.debug('...done!');
		};


		/**
		 * Public service function to unsubscribe from all previously registered channels.
		 *
		 * @param {String} serviceName Name of the service unsubscribing channels
		 * @returns {undefined}
		 */
		Service.unsubscribeChannels = function(serviceName) {
			$log.debug(serviceName + ' unsubscribing from channels...');

			angular.forEach(subscriptions[serviceName], function(sub) {
				sub.unsubscribe();
			});

			subscriptions[serviceName] = {};

			$log.debug('...done!');
		};


		return Service;
	}]);