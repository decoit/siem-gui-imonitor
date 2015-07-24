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
	.controller('AdminRulesController', ['$scope', '$log', '$modal', 'AdminService', 'StompSubscriptionService', function($scope, $log, $modal, viewService, subService) {
		var stompChannels = [];

		stompChannels.push({
			name: '/user/queue/admin/rules/local/list',
			callback: localRuleListCallback
		});
		stompChannels.push({
			name: '/user/queue/admin/rules/remote/list',
			callback: remoteRuleListCallback
		});

		subService.subscribeChannels('AdminRulesController', stompChannels);

		$scope.activeRule = {
			local: null,
			remote: null
		};
		$scope.activeRemoteIndex = -1;
		$scope.activeLocalIndex = -1;
		$scope.backup = {};
		$scope.rules = {
			local: null,
			remote: null
		};

		viewService.requestLocalRuleList();
		viewService.requestRemoteRuleList();


		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('AdminRulesController');
		});


		$scope.showLocalRuleDetails = function(index) {
			if($scope.activeRule.local !== null) {
				revertLocalRuleChanges();
			}

			$log.debug('Show rule details for index: ' + index);

			$scope.activeLocalIndex = index;
			$scope.activeRule.local = $scope.rules.local[index];
			$scope.backup = {
				name: $scope.activeRule.local.name,
				description: $scope.activeRule.local.description,
				condition: {
					query: $scope.activeRule.local.condition.query
				},
				action: {
					incidentName: $scope.activeRule.local.action.incidentName,
					risk: $scope.activeRule.local.action.risk,
					query: $scope.activeRule.local.action.query.slice(),
					updateQuery: $scope.activeRule.local.action.updateQuery.slice(),
					recommendation: $scope.activeRule.local.action.recommendation.slice(),
					explanation: $scope.activeRule.local.action.explanation.slice()
				}
			};
		};


		$scope.closeLocalDetails = function() {
			revertLocalRuleChanges();
			clearLocalDetails();
		};


		$scope.storeLocalDetails = function() {
			viewService.storeLocalRule($scope.activeRule.local);
			clearLocalDetails();
		};


		$scope.addQuery = function() {
			if($scope.activeRule.local !== null) {
				$scope.activeRule.local.action.query.push('');
			}
		};


		$scope.removeQuery = function(index) {
			if($scope.activeRule.local !== null) {
				var removedQuery = $scope.activeRule.local.action.query.splice(index, 1);
				$log.debug('Removed query:');
				$log.debug(removedQuery);
			}
		};


		$scope.addUpdateQuery = function() {
			if($scope.activeRule.local !== null) {
				$scope.activeRule.local.action.updateQuery.push('');
			}
		};


		$scope.removeUpdateQuery = function(index) {
			if($scope.activeRule.local !== null) {
				var removedQuery = $scope.activeRule.local.action.updateQuery.splice(index, 1);
				$log.debug('Removed update query:');
				$log.debug(removedQuery);
			}
		};


		$scope.addRecommendation = function() {
			if($scope.activeRule.local !== null) {
				$scope.activeRule.local.action.recommendation.push('');
			}
		};


		$scope.removeRecommendation = function(index) {
			if($scope.activeRule.local !== null) {
				var removedRecommendation = $scope.activeRule.local.action.recommendation.splice(index, 1);
				$log.debug('Removed recommendation:');
				$log.debug(removedRecommendation);
			}
		};


		$scope.addExplanation = function() {
			if($scope.activeRule.local !== null) {
				$scope.activeRule.local.action.explanation.push('');
			}
		};


		$scope.removeExplanation = function(index) {
			if($scope.activeRule.local !== null) {
				var removedExplanation = $scope.activeRule.local.action.explanation.splice(index, 1);
				$log.debug('Removed explanation:');
				$log.debug(removedExplanation);
			}
		};


		$scope.createNewRule = function() {
			$scope.closeLocalDetails();

			$scope.activeRule.local = {
				dtoId: 0,
				entityId: 0,
				name: 'Neue Regel',
				description: '',
				condition: {
					query: ''
				},
				action: {
					incidentName: '',
					risk: 0,
					query: [],
					updateQuery: [],
					recommendation: [],
					explanation: []
				}
			};
		};


		$scope.deleteLocalRule = function() {
			viewService.deleteLocalRule($scope.activeRule.local.entityId);

			$scope.closeLocalDetails();
		};


		$scope.showRemoteRuleOperations = function(index) {
			if($scope.activeRule.remote !== null) {
				clearRemoteDetails();
			}

			$scope.activeRemoteIndex = index;
			$scope.activeRule.remote = $scope.rules.remote[index];
		};


		$scope.closeRemoteDetails = function() {
			clearRemoteDetails();
		};


		$scope.openUploadDialog = function() {
			openUploadDownloadDialog('upload');
		};


		$scope.openDownloadDialog = function() {
			openUploadDownloadDialog('download');
		};


		function openUploadDownloadDialog(mode) {
			if(mode === 'upload' || mode === 'download') {
				$modal.open({
					templateUrl: 'templates/ruleuploaddownload.html',
					controller: 'AdminRulesUploadDownloadController',
					resolve: {
						entityId: function() {
							if(mode === 'upload') {
								return $scope.activeRule.local.entityId;
							}
							else {
								return 0;
							}
						},
						dtoId: function() {
							if(mode === 'download') {
								return $scope.activeRule.remote.dtoId;
							}
							else {
								return 0;
							}
						},
						name: function() {
							if(mode === 'upload') {
								return $scope.activeRule.local.name;
							}
							else if(mode === 'download') {
								return $scope.activeRule.remote.name;
							}
							else {
								return null;
							}
						}
					}
				});
			}
			else {
				$log.error('openUploadDownloadDialog called with undefined mode: ' + mode);
			}
		}


		function revertLocalRuleChanges() {
			if($scope.activeLocalIndex >= 0) {
				$log.debug('Reverting changes to rule');

				$scope.activeRule.local.name = $scope.backup.name;
				$scope.activeRule.local.description = $scope.backup.description;
				$scope.activeRule.local.condition = $scope.backup.condition;
				$scope.activeRule.local.action = $scope.backup.action;
			}
		}


		function clearLocalDetails() {
			$scope.activeLocalIndex = -1;
			$scope.activeRule.local = null;
			$scope.backup = {};
		}


		function clearRemoteDetails() {
			$scope.activeRemoteIndex = -1;
			$scope.activeRule.remote = null;
		};


		function localRuleListCallback(data) {
			$scope.closeLocalDetails();
			$scope.closeRemoteDetails();

			$scope.rules.local = [];

			$log.debug('AdminRulesController, localRuleListCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('AdminRulesController, localRuleListCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.rules.local = fetchedResult.ruleList;
		}


		function remoteRuleListCallback(data) {
			$scope.closeLocalDetails();
			$scope.closeRemoteDetails();

			$scope.rules.remote = [];

			$log.debug('AdminRulesController, remoteRuleListCallback, data:');
			$log.debug(data);

			var fetchedResult = JSON.parse(data.body);

			$log.debug('AdminRulesController, remoteRuleListCallback, fetchedResult:');
			$log.debug(fetchedResult);

			$scope.rules.remote = fetchedResult.ruleList;
		}
	}]);