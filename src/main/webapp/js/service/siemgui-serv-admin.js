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
	.factory('AdminService', ['$log', 'WebSocketService', function($log, wsService) {
		// We return this object to anything injecting our service
		var Service = {};


		Service.requestUserList = function() {
			$log.debug('Sending message to request user list');

			wsService.send('/app/admin/users/list', {});
		};


		Service.storeUserDetails = function(userId, siemAuthorized, siemAdminAuthorized) {
			$log.debug('Sending message to request user list');

			var payload = {
				userId: userId,
				siemAuthorized: siemAuthorized,
				siemAdminAuthorized: siemAdminAuthorized
			};

			wsService.send('/app/admin/users/store', payload);
		};


		Service.requestLocalRuleList = function() {
			$log.debug('Sending message to request list of local rules');

			wsService.send('/app/admin/rules/local/list', {});
		};


		Service.requestRemoteRuleList = function() {
			$log.debug('Sending message to request list of remote rules');

			wsService.send('/app/admin/rules/remote/list', {});
		};


		Service.storeLocalRule = function(rule) {
			$log.debug('Sending message to store a local rule');

			var payload = {
				rule: rule
			};

			wsService.send('/app/admin/rules/local/store', payload);
		};


		Service.deleteLocalRule = function(entityId) {
			$log.debug('Sending message to delete a local rule');

			var payload = {
				entityId: entityId
			};

			wsService.send('/app/admin/rules/local/delete', payload);
		};


		Service.uploadLocalRule = function(entityId, newName) {
			$log.debug('Sending message to upload a local rule');

			var payload = {
				entityId: entityId,
				uploadedRuleName: newName
			};

			wsService.send('/app/admin/rules/local/upload', payload);
		};


		Service.downloadRemoteRule = function(dtoId, newName) {
			$log.debug('Sending message to download a remote rule');

			var payload = {
				dtoId: dtoId,
				downloadedRuleName: newName
			};

			wsService.send('/app/admin/rules/remote/download', payload);
		};


		return Service;
	}]);