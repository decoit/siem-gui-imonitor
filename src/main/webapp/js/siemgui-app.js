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

/*
 * Main AngularJS module for the SIEM GUI system. This module is loaded after successful authentication.
 */
var app = angular.module('SiemGui', ['ngSanitize', 'ui.bootstrap', 'ui.router', 'highcharts-ng']);

app.config(['$stateProvider', '$urlRouterProvider', '$logProvider', function($stateProvider, $urlRouterProvider, $logProvider) {
	// Enable/disable debug logging
	$logProvider.debugEnabled(true);

	// Setup routing
	$urlRouterProvider.otherwise('/overview');

	$stateProvider
		.state('overview', {
			url: '/overview',
			templateUrl: 'templates/overview.html',
			controller: 'OverviewController',
			resolve: {
				userdetails: ['UserDetailsService', function(UserDetailsService) {
					return UserDetailsService.promise;
				}]
			}
		})
		.state('charts', {
			url: '/charts',
			templateUrl: 'templates/charts.html',
			controller: 'ChartsController',
			resolve: {
				userdetails: ['UserDetailsService', function(UserDetailsService) {
					return UserDetailsService.promise;
				}]
			}
		})
		.state('events', {
			url: '/events',
			templateUrl: 'templates/events.html',
			controller: 'EventsController',
			resolve: {
				userdetails: ['UserDetailsService', function(UserDetailsService) {
					return UserDetailsService.promise;
				}]
			}
		})
		.state('incidents', {
			url: '/incidents',
			templateUrl: 'templates/incidents.html',
			controller: 'IncidentsController',
			resolve: {
				userdetails: ['UserDetailsService', function(UserDetailsService) {
					return UserDetailsService.promise;
				}]
			}
		})
		.state('admin', {
			url: '/admin',
			templateUrl: 'templates/admin.html',
			controller: 'AdminController',
			resolve: {
				userdetails: ['UserDetailsService', function(UserDetailsService) {
					return UserDetailsService.promise;
				}]
			}
		})
		.state('admin.users', {
			url: '/users/{userId}',
			templateUrl: 'templates/admin.users.html',
			controller: 'AdminUsersController'
		})
		.state('admin.rules', {
			url: '/rules/{ruleId}',
			templateUrl: 'templates/admin.rules.html',
			controller: 'AdminRulesController'
		});
}]);

app.run(['$rootScope', '$log', '$location', 'WebSocketService', function($rootScope, $log, $location, wsService) {
	// Define global functions

	/**
	 * Take the JSON representation of a Java LocalDateTime object and format it into a date string.
	 *
	 * @param {Object} dateObj JSON object of a Java LocalDateTime
	 * @returns {String} Formatted date string
	 */
	$rootScope.formatLocalDateTime = function(dateObj) {
		if(dateObj) {
			var date = dateObj.year + "-";

			if(dateObj.monthValue < 10) {
				date += "0";
			}
			date += dateObj.monthValue + "-";

			if(dateObj.dayOfMonth < 10) {
				date += "0";
			}
			date += dateObj.dayOfMonth + " ";

			if(dateObj.hour < 10) {
				date += "0";
			}
			date += dateObj.hour + ":";

			if(dateObj.minute < 10) {
				date += "0";
			}
			date += dateObj.minute + ":";

			if(dateObj.second < 10) {
				date += "0";
			}
			date += dateObj.second;

			return date;
		}
		else {
			return "Invalid date Object";
		}
	};


	// Disconnect the WebSocket if $rootScope is destroyed
	$rootScope.$on('$destroy', function() {
		wsService.disconnect();
	});

	$log.debug('Host: ' + $location.host());
	$log.debug('Port: ' + $location.port());

	$log.debug('Starting WebSocket connection attempt');

	// Start connection attempt to the WebSocket with debugging
	wsService.connect('/siem-gui/gui-stomp', function(){}, {}, true);

	// Start connection attempt to the WebSocket without debugging
	//wsService.connect('/siem-gui/gui-stomp', function(){});
}]);