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
	.controller("ForceLogoutController", ['$scope', '$interval', '$window', '$log', 'msg', function($scope, $interval, $window, $log, msg) {
		var promise;

		$scope.$on('$destroy', function() {
			if(angular.isDefined(promise)) {
				$interval.cancel(promise);
			}
		});

		$scope.time = msg.time;
		$scope.text = msg.text;
		$scope.secondsToClose = 10;

		$log.debug($scope.time + " " + $scope.text);

		promise = $interval(logoutCountdown, 1000);


		/**
		 * Function to be called by an interval to implement the logout countdown.
		 *
		 * @returns {undefined}
		 */
		function logoutCountdown() {
			if($scope.secondsToClose === 0) {
				$interval.cancel(promise);

				$window.location.href = 'logout.html';
			}
			else {
				$scope.secondsToClose -= 1;
			}
		}
	}]);