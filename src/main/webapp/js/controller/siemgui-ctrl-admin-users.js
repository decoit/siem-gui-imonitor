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
	.controller('AdminUsersController', ['$scope', '$log', 'AdminService', 'StompSubscriptionService', function($scope, $log, viewService, subService) {
		var stompChannels = [];

		stompChannels.push({
			name: '/user/queue/admin/users/list',
			callback: userListCallback
		});
		stompChannels.push({
			name: '/user/queue/admin/users/storesuccess',
			callback: storeSuccessCallback
		});

		subService.subscribeChannels('AdminUsersController', stompChannels);

		$scope.user = null;
		$scope.activeIndex = -1;
		$scope.userlist = null;
		$scope.backup = {};

		viewService.requestUserList();


		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('AdminUsersController');
		});


		$scope.showDetails = function(index) {
			if($scope.user !== null) {
				revertUserChanges();
			}

			$log.debug('Show user details for index: ' + index);

			$scope.activeIndex = index;
			$scope.user = $scope.userlist[index];
			$scope.backup = {
				privileged: $scope.user.privileged,
				siemAuthorized: $scope.user.siemAuthorized,
				siemAdminAuthorized: $scope.user.siemAdminAuthorized
			};
		};


		$scope.storeDetails = function() {
			viewService.storeUserDetails($scope.user.id, $scope.user.siemAuthorized, $scope.user.siemAdminAuthorized);
			clearDetails();
		};


		$scope.closeDetails = function() {
			revertUserChanges();
			clearDetails();
		};


		$scope.siemAuthorizedClicked = function() {
			// This function is called BEFORE status change of the checkbox, so we have to check for 'true' instead of 'false'
			if($scope.user.siemAuthorized === true) {
				$scope.user.siemAdminAuthorized = false;
			}
		};


		function revertUserChanges() {
			$log.debug('Reverting changes to user');

			$scope.user.privileged = $scope.backup.privileged;
			$scope.user.siemAuthorized = $scope.backup.siemAuthorized;
			$scope.user.siemAdminAuthorized = $scope.backup.siemAdminAuthorized;
		}


		function clearDetails() {
			$scope.activeIndex = -1;
			$scope.user = null;
			$scope.backup = {};
		}


		function userListCallback(data) {
			$log.debug('AdminUsersController, userListCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('AdminUsersController, userListCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.userlist = fetchedResult.userlist;
		}


		function storeSuccessCallback(data) {
			$scope.user = null;
			$scope.activeIndex = -1;
			$scope.userlist = null;
			$scope.backup = {};

			viewService.requestUserList();
		}
	}]);