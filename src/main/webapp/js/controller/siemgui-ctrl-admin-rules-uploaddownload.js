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
	.controller('AdminRulesUploadDownloadController', ['$scope', '$log', 'AdminService', 'entityId', 'dtoId', 'name', function($scope, $log, viewService, entityId, dtoId, name) {
		if(entityId > 0) {
			$scope.action = 'hochladen';
		}
		else if(dtoId > 0) {
			$scope.action = 'herunterladen';
		}
		else {
			$log.error('Could not determine window type, no provided ID was greater than 0');
		}

		$scope.name = name;


		$scope.cancel = function() {
			$scope.$close('Operation canceled');
		};


		$scope.submit = function() {
			if(entityId > 0) {
				viewService.uploadLocalRule(entityId, $scope.name);
			}
			else if(dtoId > 0) {
				viewService.downloadRemoteRule(dtoId, $scope.name);
			}
			else {
				$log.error('No operation executed, no provided ID was greater than 0');
			}

			$scope.$close('Operation submitted');
		};
	}]);