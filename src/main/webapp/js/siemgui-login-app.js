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
 * AngularJS module for the login page of the SIEM GUI system.
 */
var app = angular.module('SiemGuiLogin', ['ui.bootstrap']);

app.controller('LoginController', ['$scope', '$location', function($scope, $location) {
	// Determine if the login page was called due to login error or logout
	var logout = ($location.absUrl().indexOf('logout=true') > -1);
	var error = ($location.absUrl().indexOf('error=true') > -1);

	$scope.alert = {};

	// Only show alert if page was called by login error or logout
	$scope.alert.show = (logout || error);

	if(logout) {
		$scope.alert.type = 'success';
		$scope.alert.text = 'Sie wurden erfolgreich abgemeldet.';
	}
	else if(error) {
		$scope.alert.type = 'danger';
		$scope.alert.text = 'Benutzername oder Passwort falsch! Bitte korrigieren Sie Ihre Eingaben und versuchen Sie es erneut!';
	}
	else {
		$scope.alert.type = '';
		$scope.alert.text = '';
	}

	/**
	 * Hide the alert. This function has no use if no alert is shown.
	 *
	 * @returns {undefined}
	 */
	$scope.hideAlerts = function() {
		$scope.alert.show = false;
	};
}]);