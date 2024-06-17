

(function($) {
    /* "use strict" */
	
 var dzChartlist = function(){
	
	var screenWidth = $(window).width();
	/* let draw = Chart.controllers.line.__super__.draw; //draw shadow */

	var marketChart = function(){
		 var options = {
          series: [{
          name: 'series1',
          data: [200, 400, 300, 400, 200, 400]
        },
		{
          name: 'series3',
          data: [100, 200, 500, 300, 100, 300]
        },{
          name: 'series2',
          data: [500, 300, 400, 200, 500, 200]
        }],
          chart: {
          height: 190,
          type: 'line',
		  toolbar:{
			  show:false
		  }
        },
		colors:["var(--primary-light)","#f5a792","#d7e2ff"],
        dataLabels: {
          enabled: false,
		   style: {
			    colors: ['#000'],
		   }
        },
        stroke: {
          curve: 'smooth',
		  width:3,
		  colors:["var(--primary-light)","var(--secondary)", "#d7e2ff"],
        },
		legend:{
			show:false
		},
		grid:{
			show:false,
			strokeDashArray: 6,
			borderColor: '#dadada',
		},
		yaxis: {
		  labels: {
			style: {
				colors: '#B5B5C3',
				fontSize: '12px',
				fontFamily: 'Poppins',
				fontWeight: 400
				
			},
			formatter: function (value) {
			  return value + "k";
			}
		  },
		},
        xaxis: {
          categories: ["Week 01","Week 02","Week 03","Week 04","Week 05","Week 06"],
		  labels:{
			  style: {
				colors: '#B5B5C3',
				fontSize: '12px',
				fontFamily: 'Poppins',
				fontWeight: 400
				
			},
		  }
        },
		fill:{
			type:'solid',
			opacity:1
		},
        tooltip: {
          x: {
            format: 'dd/MM/yy HH:mm'
          },
        },
        };

        var chart = new ApexCharts(document.querySelector("#marketChart"), options);
        chart.render();
	}	
	var chartBar = function(){
		
		var options = {
			  series: [
				{
					name: 'Net Profit',
					data: [15, 55, 90, 80, 25, 15, 70,25, 15, 70],
					//radius: 12,	
				}, 
				{
				  name: 'Revenue',
				  data: [60, 65, 15, 35, 30, 40, 30,35, 30, 5]
				}, 
				
			],
				chart: {
				type: 'bar',
				height: 220,
				
				toolbar: {
					show: false,
				},
				
			},
			plotOptions: {
			  bar: {
				horizontal: false,
				columnWidth: '50%',
				endingShape: 'rounded'
			  },
			},
			colors:['#f9c35c', 'var(--primary-light)'],
			dataLabels: {
			  enabled: false,
			},
			markers: {
			shape: "circle",
			},
		
		
			legend: {
				show: false,
				fontSize: '12px',
				labels: {
					colors: '#000000',
					
					},
				markers: {
				width: 18,
				height: 18,
				strokeWidth: 0,
				strokeColor: '#fff',
				fillColors: undefined,
				radius: 12,	
				}
			},
			stroke: {
			  show: true,
			  width: 1,
			  colors: ['transparent']
			},
			
			x:{
			
			  categories: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
			  grid: {
						color: "rgba(233,236,255,0.5)",
						drawBorder: true
					},
			  labels: {
			   style: {
				  colors: '#787878',
				  fontSize: '13px',
				  fontFamily: 'poppins',
				  fontWeight: 100,
				  cssClass: 'apexcharts-xaxis-label',
				},
			  },
			  crosshairs: {
				show: false,
			  }
			},
			y:{
				
				labels: {
				   style: {
					  colors: '#787878',
					  fontSize: '13px',
					   fontFamily: 'poppins',
					  fontWeight: 100,
					  cssClass: 'apexcharts-xaxis-label',
				  },
				},
			},
			fill: {
			  opacity: 1
			},
			tooltip: {
			  y: {
				formatter: function (val) {
				  return "$ " + val + " thousands"
				}
			  }
			}
			};

			var chartBar1 = new ApexCharts(document.querySelector("#chartBar"), options);
			chartBar1.render();
	}
	
	var customerChart = function(){
		 var options = {
          series: [{
          name: 'series1',
          data: [200, 400, 300, 400]
        }, {
          name: 'series2',
          data: [500, 300, 400, 200]
        }],
          chart: {
          height: 250,
          type: 'area',
		  toolbar:{
			  show:false
		  }
        },
		colors:["var(--primary-light)","#DCDFE5"],
        dataLabels: {
          enabled: false
        },
        stroke: {
          curve: 'smooth',
		  width:3,
		  colors:["var(--primary-light)","#DCDFE5"],
        },
		legend:{
			show:false
		},
		grid:{
			show:false,
			strokeDashArray: 6,
			borderColor: '#dadada',
		},
		yaxis: {
		  labels: {
			style: {
				colors: '#B5B5C3',
				fontSize: '12px',
				fontFamily: 'Poppins',
				fontWeight: 400
				
			},
			formatter: function (value) {
			  return value + "k";
			}
		  },
		},
        xaxis: {
          categories: ["Week 01","Week 02","Week 03","Week 04"],
		  labels:{
			  style: {
				colors: '#B5B5C3',
				fontSize: '12px',
				fontFamily: 'Poppins',
				fontWeight: 400
				
			},
		  }
        },
		fill:{
			type:'solid',
			opacity:0.9
		},
        tooltip: {
          x: {
            format: 'dd/MM/yy HH:mm'
          },
        },
        };

        var chart = new ApexCharts(document.querySelector("#customerChart"), options);
        chart.render();
	}
	
	var realityChart = function(){
		var options = {
			  series: [
				{
					name: 'Running',
					data: [50, 90, 90,50],
					//radius: 12,	
				}, 
				{
				  name: 'Cycling',
				  data: [50, 60, 55,40]
				}, 
				
			],
				chart: {
				type: 'bar',
				height: 230,
				
				toolbar: {
					show: false,
				},
				
			},
			plotOptions: {
			  bar: {
				horizontal: false,
				columnWidth: '80%',
				endingShape: "rounded",
				borderRadius: 8,
			  },
			  
			},
			states: {
			  hover: {
				filter: 'none',
			  }
			},
			colors:['#F8B940', 'var(--primary-light)'],
			dataLabels: {
			  enabled: false,
			  offsetY: -30
			},
			
			legend: {
				show: false,
				fontSize: '12px',
				labels: {
					colors: '#000000',
					
					},
				markers: {
				width: 18,
				height: 18,
				strokeWidth: 8,
				strokeColor: '#fff',
				fillColors: undefined,
				radius: 12,	
				}
			},
			stroke: {
			  show: true,
			  width:14,
			  curve: 'smooth',
			  lineCap: 'round',
			  colors: ['transparent']
			},
			grid: {
				show: false,
				xaxis: {
					lines: {
						show: false,
					}
				},
				 yaxis: {
						lines: {
							show: false
						}
					},  				
			},
			xaxis: {
				categories: ['JAN', 'FEB', 'MAR', 'APR', 'MAY'],
				labels: {
					show: false,
					style: {
						colors: '#A5AAB4',
						fontSize: '14px',
						fontWeight: '500',
						fontFamily: 'poppins',
						cssClass: 'apexcharts-xaxis-label',
					},
				},
				crosshairs: {
					show: false,
				},
				axisBorder: {
					show: false,
				},
				axisTicks: {
					show: false,
				}, 			
			},
			yaxis: {
				labels: {
				show: false,
					offsetX:-16,
				   style: {
					  colors: '#000000',
					  fontSize: '13px',
					   fontFamily: 'poppins',
					  fontWeight: 100,
					  cssClass: 'apexcharts-xaxis-label',
				  },
			  },
			},
			};

			var chartBar1 = new ApexCharts(document.querySelector("#realityChart"), options);
			chartBar1.render();
	}
	var handleWorldMap = function(trigger = 'load'){
		var vmapSelector = $('#world-map');
		if(trigger == 'resize')
		{
			vmapSelector.empty();
			vmapSelector.removeAttr('style');
		}
		
		vmapSelector.delay( 300 ).unbind().vectorMap({ 
			map: 'world_en',
			backgroundColor: 'transparent',
			borderColor: 'rgb(239, 242, 244)',
			borderOpacity: 0.25,
			borderWidth: 1,
			color: 'var(--primary-light)',
			enableZoom: true,
			hoverColor: 'rgba(239, 242, 244 0.9)',
			hoverOpacity: null,
			normalizeFunction: 'linear',
			scaleColors: ['#b6d6ff', '#005ace'],
			selectedColor: 'rgba(239, 242, 244 0.9)',
			selectedRegions: null,
			showTooltip: true,
			onRegionClick: function(element, code, region)
			{
				var message = 'You clicked "'
					+ region
					+ '" which has the code: '
					+ code.toUpperCase();
		 
				alert(message);
			}
		});
	}
	var volumeChart = function(){
		var options = {
			series: [{
				name: 'Sent',
				data: [40, 55, 15,40,55, 15,40,50]
			}, {
				name: 'Answered',
				data: [55, 55, 35,60,55, 35,60,30]
			}, {
				name: 'Hired',
				data: [20, 17, 55, 45,17, 55, 45,20]
			}],
			chart: {
				type: 'bar',
				height: 250,
				stacked: true,
				toolbar: {
					show: false,
				}
			},
			
			plotOptions: {
				bar: {
					horizontal: false,
					columnWidth: '40%',
					endingShape: "rounded",
					startingShape: "rounded",
					backgroundRadius: 10,
					borderRadius: 5,
					colors: {
						backgroundBarColor: '#fff',
						backgroundBarOpacity: 1,
						backgroundBarRadius: 10,
					},
				},
				
			},
			stroke:{
				width:5,
				colors:["#fff"]
			},
			colors:['var(--secondary)', 'var(--primary-light)', '#58bad7'],
			xaxis: {
				show: false,
				axisBorder: {
					show: false,
				},
				
				labels: {
					show: false,	
					style: {
						colors: '#828282',
						fontSize: '14px',
						fontFamily: 'Poppins',
						fontWeight: 'light',
						cssClass: 'apexcharts-xaxis-label',
					},
				},
				axisTicks: {
					show: false,
				},
				crosshairs: {
					show: false,
				},
				
			},
			yaxis: {
				show: false,
				labels: {
					style: {
						colors: '#828282',
						fontSize: '14px',
						fontFamily: 'Poppins',
						fontWeight: 'light',
						cssClass: 'apexcharts-xaxis-label',
					},
				},
			},
			grid: {
				show: false,
				borderColor: '#DBDBDB',
				strokeDashArray: 10,
				position: 'back',
				xaxis: {
					lines: {
						show: false
					}
				},   
				yaxis: {
					lines: {
						show: true
					}
				},  
			},
			toolbar: {
				enabled: false,
			},
			dataLabels: {
			  enabled: false
			},
			legend: {
				show:false
			},
			fill: {
				opacity: 1
			},
			responsive: [{
				breakpoint: 1601,
				options: {
					plotOptions: {
						bar: {
							columnWidth: '60%',
						},
						
					},
				},
			}]
		};

		var chart = new ApexCharts(document.querySelector("#volumeChart"), options);
		chart.render();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
	/* Function ============ */
		return {
			init:function(){
			},
			
			
			load:function(){
				marketChart();
				chartBar();
				customerChart();
				realityChart();
				handleWorldMap();
				volumeChart();
			
				
			},
			
			resize:function(){
				handleWorldMap();
				
				
			}
		}
	
	}();

	
		
	jQuery(window).on('load',function(){
		setTimeout(function(){
			dzChartlist.load();
		}, 1000); 
		
	});

     

})(jQuery);
