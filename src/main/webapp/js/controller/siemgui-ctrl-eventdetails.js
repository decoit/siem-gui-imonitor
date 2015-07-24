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
	.controller("EventDetailsController", ['$scope', '$log', '$sce', '$sanitize', 'details', function($scope, $log, $sce, $sanitize, details) {
		$scope.details = details;

		$log.debug($scope.details);


		/**
		 * Format an array of port numbers to a string.
		 * The resulting string will be something like port1, port2, port3, ...
		 *
		 * @param {Array} array Array of port numbers
		 * @returns {String} Formatted string
		 */
		$scope.formatNmapPortList = function(array) {
			var list = '';
			var first = true;

			array.sort(function(a, b){return a-b;});

			angular.forEach(array, function(value) {
				if(!first) {
					list += ', ';
				}
				else {
					first = false;
				}

				list += value;
			});

			return list;
		};


		/**
		 * Format the result of a Nmap scan for displaying it in the event details.
		 * The resulting string is returnes as trusted HTML to allow &lt;br&gt; tags inside.
		 *
		 * @param {Array} closed Array of invalid closed ports
		 * @param {Array} open Array of invalid open ports
		 * @returns {String} Formatted string as trusted HTML
		 */
		$scope.formatNmapScanResult = function(closed, open) {
			var rv = '';

			if(open.length === 0 && closed.length === 0) {
				rv = 'Keine Probleme gefunden';
			}
			else {
				if(closed.length > 0) {
					rv += 'Geschlossene Ports: ' + closed.length;
					rv += '<br>&gt; ' + $scope.formatNmapPortList(closed);
				}

				if(open.length > 0) {
					if(rv.length > 0) {
						rv += '<br><br>';
					}
					rv += 'Offene Ports: ' + open.length;
					rv += '<br>&gt; ' + $scope.formatNmapPortList(open);
				}
			}

			return $sce.trustAsHtml(rv);
		};
	}]);