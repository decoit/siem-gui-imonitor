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
	.controller("IncidentCommentFormController", ['$scope', '$log', 'IncidentsService', 'StompSubscriptionService', 'details', 'history', 'formatHistoryItem', function($scope, $log, viewService, subService, details, history, formatHistoryItem) {
		$scope.details = details;
		$scope.history = history;
		$scope.formatHistoryItem = formatHistoryItem;
		$scope.commentText = '';

		$log.debug('Comment form, incident details:');
		$log.debug(details);
		$log.debug('Comment form, incident history:');
		$log.debug(history);

		var stompChannels = [];

		stompChannels.push({
			name:  '/user/queue/incidents/commentconfirmed',
			callback: commentConfirmedCallback
		});

		subService.subscribeChannels('IncidentCommentFormController', stompChannels);


		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('IncidentCommentFormController');
		});


		$scope.sendComment = function() {
			viewService.postComment($scope.details.incDetails.id, $scope.commentText);
		};


		$scope.validateComment = function() {
			return $scope.commentText.length < 10;
		};


		$scope.quoteComment = function(index) {
			$log.debug('Quote comment: ' + index);

			$scope.commentText.trim();
			if($scope.commentText.length > 0) {
				if($scope.commentText.charAt($scope.commentText.length-1) !== '\n') {
					$scope.commentText += '\n';
				}
			}

			$scope.commentText += '[bq]';
			$scope.commentText += $scope.history[index].text;
			$scope.commentText += '[/bq]\n';
		};


		function commentConfirmedCallback() {
			$log.debug('Comment posted successfully');

			$scope.$close('Comment posted');
			$scope.$parent.openIncidentDetails($scope.details.incIndex, $scope.details.active);
		}
	}]);