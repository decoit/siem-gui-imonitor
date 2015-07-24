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
	.controller("ChartsController", ['$scope', '$log', 'ChartsService', 'StompSubscriptionService', function($scope, $log, viewService, subService) {
		var stompChannels = [];
		var chartPlaceholder = {
			options: {},
			series: [],
			loading: true,
			title: {
				text: "Lade Daten..."
			},
			size: {
				height: 300
			}
		};

		// Message channel to receive the event list
		stompChannels.push({
			name:  '/user/queue/charts',
			callback: chartsCallback
		});

		subService.subscribeChannels('ChartsController', stompChannels);

		// Unsubscribe from STOMP message channels when this scope is destroyed
		$scope.$on('$destroy', function() {
			subService.unsubscribeChannels('ChartsController');
		});

		$scope.$parent.navpath = 'charts';
		$scope.charts = [];

		$scope.charts[0] = chartPlaceholder;
		$scope.charts[1] = chartPlaceholder;
		$scope.charts[2] = chartPlaceholder;

		viewService.requestEventsTodayChart(0);
		viewService.requestEventsTodayPerSensorChart(1);
		viewService.requestIncidentsByThreatLevelChart(2);


		/**
		 * Callback for the STOMP message containing the event list.
		 * Stores the message contents in the $scope.
		 *
		 * @param {Object} data STOMP message data
		 * @returns {undefined}
		 */
		function chartsCallback(data) {
			$log.debug('chartsCallback, data:');
			$log.debug(data);

			var fetchedChart = JSON.parse(data.body);

			$log.debug('chartsCallback, fetchedChart:');
			$log.debug(fetchedChart);

			var chart = {
				options: {
					chart: {
						type: fetchedChart.type
					},
					xAxis: fetchedChart.xAxis,
					yAxis: fetchedChart.yAxis,
					legend: {
						enabled: false
					},
					plotOptions: {
						bar: {
							dataLabels: {
								enabled: true
							}
						}
					}
				},
				series: fetchedChart.series,
				title: {
					text: fetchedChart.title
				},
				size: {
					height: 300
				}
			};

			$scope.charts[fetchedChart.chartIndex] = chart;
		}
	}]);