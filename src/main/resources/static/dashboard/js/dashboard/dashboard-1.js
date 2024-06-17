

(function($) {
    /* "use strict" */
	
 var dzChartlist = function(){
	
	var screenWidth = $(window).width();
	let draw = Chart.controllers.line.__super__.draw; //draw shadow
	
	
	var NewExperience = function(){
		var options = {
		  series: [
			{
				name: 'Net Profit',
				data: [100,300, 200, 250, 200, 240, 180,230,200, 250, 300],
				/* radius: 30,	 */
			}, 				
		],
			chart: {
			type: 'area',
			height: 40,
			//width: 400,
			toolbar: {
				show: false,
			},
			zoom: {
				enabled: false
			},
			sparkline: {
				enabled: true
			}
			
		},
		
		colors:['#fff'],
		dataLabels: {
		  enabled: false,
		},

		legend: {
			show: false,
		},
		stroke: {
		  show: true,
		  width: 2,
		  curve:'straight',
		  colors:['#fff'],
		},
		
		grid: {
			show:false,
			borderColor: '#eee',
			padding: {
				top: 0,
				right: 0,
				bottom: 0,
				left: -1

			}
		},
		states: {
                normal: {
                    filter: {
                        type: 'none',
                        value: 0
                    }
                },
                hover: {
                    filter: {
                        type: 'none',
                        value: 0
                    }
                },
                active: {
                    allowMultipleDataPointsSelection: false,
                    filter: {
                        type: 'none',
                        value: 0
                    }
                }
            },
		xaxis: {
			categories: ['Jan', 'feb', 'Mar', 'Apr', 'May', 'June', 'July','August', 'Sept','Oct'],
			axisBorder: {
				show: false,
			},
			axisTicks: {
				show: false
			},
			labels: {
				show: false,
				style: {
					fontSize: '12px',
				}
			},
			crosshairs: {
				show: false,
				position: 'front',
				stroke: {
					width: 1,
					dashArray: 3
				}
			},
			tooltip: {
				enabled: true,
				formatter: undefined,
				offsetY: 0,
				style: {
					fontSize: '12px',
				}
			}
		},
		yaxis: {
			show: false,
		},
		fill: {
		  opacity: 0.9,
		  colors:'#fff',
		  type: 'gradient', 
		  gradient: {
			colorStops:[ 
				
				{
				  offset: 0,
				  color: '#fff',
				  opacity: .5
				},
				{
				  offset: 0.6,
				  color: '#fff',
				  opacity: .5
				},
				{
				  offset: 100,
				  color: 'white',
				  opacity: 0
				}
			  ],
			  
			}
		},
		tooltip: {
			enabled:false,
			style: {
				fontSize: '12px',
			},
			y: {
				formatter: function(val) {
					return "$" + val + " thousands"
				}
			}
		}
		};

		var chartBar1 = new ApexCharts(document.querySelector("#NewExperience"), options);
		chartBar1.render();
	 
	}
	var chartBar = function(){
		var options = {
			  series: [
				{
					name: 'Running',
					data: [50, 90, 90],
					//radius: 12,	
				}, 
				{
				  name: 'Cycling',
				  data: [50, 60, 55]
				}, 
				
			],
				chart: {
				type: 'bar',
				height: 120,
				
				toolbar: {
					show: false,
				},
				
			},
			plotOptions: {
			  bar: {
				horizontal: false,
				columnWidth: '90%',
				endingShape: "rounded",
				borderRadius: 3,
			  },
			  
			},
			states: {
			  hover: {
				filter: 'none',
			  }
			},
			colors:['#FFFFFF', '#FFFFFF'],
			dataLabels: {
			  enabled: false,
			  offsetY: -10
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
			fill: {
			  
			  type: 'image',
				opacity:1,
			   image: {
				src: ['images/pt1.png'],
				  width: undefined,
				  height: undefined
			  },
			  /* pattern: {
				  style: 'slantedLines',
				  width: 20,
				  height: 15,
				  strokeWidth: 2,
				  
			  }, */
			}

			};

			var chartBar1 = new ApexCharts(document.querySelector("#chartBar"), options);
			chartBar1.render();
	}
	var expensesChart = function(){
		var options = {
			  series: [
				{
					name: 'Running',
					data: [50, 90, 90],
					//radius: 12,	
				}, 
				{
				  name: 'Cycling',
				  data: [50, 60, 55]
				}, 
				
			],
				chart: {
				type: 'bar',
				height: 120,
				
				toolbar: {
					show: false,
				},
				
			},
			plotOptions: {
			  bar: {
				horizontal: false,
				columnWidth: '90%',
				endingShape: "rounded",
				borderRadius: 3,
			  },
			  
			},
			states: {
			  hover: {
				filter: 'none',
			  }
			},
			colors:['#81A4F9', '#FFFFFF'],
			dataLabels: {
			  enabled: false,
			  offsetY: -10
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
			fill: {
			  type: 'image',
				opacity:1,
			   image: {
				src: ['images/pt-2.png'],
				  width: undefined,
				  height: undefined
			  },
			  /* pattern: {
				  style: 'slantedLines',
				  width: 20,
				  height: 15,
				  strokeWidth: 2,
				  
			  }, */
			}

			};

			var chartBar1 = new ApexCharts(document.querySelector("#expensesChart"), options);
			chartBar1.render();
	}
	var impressionChart = function(){
		var options = {
			  series: [
				{
					name: 'This Week',
					data: [50, 90, 90],
					//radius: 12,	
				}, 
				{
				  name: 'Last Week',
				  data: [50, 60, 55]
				}, 
				
			],
				chart: {
				type: 'bar',
				height: 180,
				
				toolbar: {
					show: false,
				},
				
			},
			plotOptions: {
			  bar: {
				horizontal: false,
				columnWidth: '90%',
				endingShape: "rounded",
				borderRadius: 3,
			  },
			  
			},
			states: {
			  hover: {
				filter: 'none',
			  }
			},
			colors:['#81A4F9', '#FFFFFF'],
			dataLabels: {
			  enabled: false,
			  offsetY: -10
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
			fill: {
			  type: 'image',
				opacity:1,
			   image: {
				src: ['images/pt-3.png'],
				  width: undefined,
				  height: undefined
			  },
			  /* pattern: {
				  style: 'slantedLines',
				  width: 20,
				  height: 15,
				  strokeWidth: 2,
				  
			  }, */
			}

			};

			var chartBar1 = new ApexCharts(document.querySelector("#impressionChart"), options);
			chartBar1.render();
	}

	
	var overiewChart = function(){
		 var options = {
          series: [{
          name: 'Number of Projects',
          type: 'column',
          data: [75, 85, 72, 100, 50, 100, 80, 75, 95, 35, 75,100]
		}, {
          name: 'Revenue',
          type: 'area',
          data: [44, 65, 55, 75, 45, 55, 40, 60, 75, 45, 50,42]
        }, {
          name: 'Active Projects',
          type: 'line',
          data: [30, 25, 45, 30, 25, 35, 20, 45, 35, 20, 35,20]
        }],
          chart: {
          height: 330,
          type: 'line',
          stacked: false,
		  toolbar: {
				show: false,
			},
        },
        stroke: {
          width: [0, 1, 1],
          curve: 'straight',
		  dashArray: [0, 0, 5]
        },
		legend: {
			fontSize: '13px',
			fontFamily: 'poppins',
			offsetX: 20,
			 labels: {
				  colors:'#888888', 
			 }
		},
        plotOptions: {
          bar: {
            columnWidth: '30%',
			 borderRadius:6	,
          },
		  
        },
        
        fill: {
          
		  type : 'gradient',
          gradient: {
            inverseColors: false,
            shade: 'light',
            type: "vertical",
			colorStops : [
				[
					{
					  offset: 0,
					  color: '#81A4F9',
					  opacity: 1
					},
					{
					  offset: 100,
					  color: '#81A4F9',
					  opacity: 1
					}
				],
				[
					{
					  offset: 0,
					  color: '#FF7C7C',
					  opacity: 1
					},
					{
					  offset: 0.4,
					  color: '#FF7C7C',
					  opacity: .15
					},
					{
					  offset: 100,
					  color: '#FF7C7C',
					  opacity: 0
					}
				],
				[
					{
					  offset: 0,
					  color: '#81A4F9',
					  opacity: 1
					},
					{
					  offset: 100,
					  color: '#81A4F9',
					  opacity: 1
					}
				],
			],
            stops: [0, 100, 100, 100]
          }
        },
		colors:["var(--primary)","#FF7C7C","#81A4F9"],
        labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul',
          'Aug', 'Sep', 'Oct', 'Nov', 'Dec'
        ],
        markers: {
          size: 0
        },
        xaxis: {
          type: 'month',
		  labels: {
			   style: {
				   fontSize: '13px',
				   colors:'#888888',
			   },
		  },
        },
        yaxis: {
          min: 0,
		  tickAmount: 4,
		  labels: {
			   style: {
				   fontSize: '13px',
				   colors:'#888888',
			   },
		  },
        },
        tooltip: {
          shared: true,
          intersect: false,
          y: {
            formatter: function (y) {
              if (typeof y !== "undefined") {
                return y.toFixed(0) + " points";
              }
              return y;
        
            }
          }
        }
        };
		


        var chart = new ApexCharts(document.querySelector("#overiewChart"), options);
        chart.render();
		
		$(".mix-chart-tab .nav-link").on('click',function(){
			var seriesType = $(this).attr('data-series');
			var columnData = [];
			var areaData = [];
			var lineData = [];
			switch(seriesType) {
				case "week":
					columnData = [75, 85, 72, 100, 50, 100, 80, 75, 95, 35, 75,100];
					areaData = [44, 65, 55, 75, 45, 55, 40, 60, 75, 45, 50,42];
					lineData = [30, 25, 45, 30, 25, 35, 20, 45, 35, 20, 35,20];
					break;
				case "month":
					columnData = [20, 50, 80, 52, 10, 80, 50, 30, 95, 10, 60,85];
					areaData = [40, 25, 85, 45, 85, 25, 95, 65, 45, 45, 20,12];
					lineData = [65, 45, 25, 65, 45, 25, 75, 35, 65, 75, 15,65];
					break;
				case "year":
					columnData = [30, 20, 80, 52, 10, 90, 50, 30, 95, 20, 60,85];
					areaData = [40, 25, 40, 45, 85, 25, 50, 65, 45, 60, 20,12];
					lineData = [65, 45, 30, 65, 45, 25, 75, 40, 65, 50, 15,65];
					break;
				case "all":
					columnData = [20, 50, 80, 60, 10, 80, 50, 40, 95, 20, 60,85];
					areaData = [40, 25, 30, 45, 85, 25, 95, 65, 50, 45, 20,12];
					lineData = [65, 45, 25, 65, 45, 25, 30, 35, 65, 75, 15,65];
					break;
				default:
					columnData = [75, 80, 72, 100, 50, 100, 80, 30, 95, 35, 75,100];
					areaData = [44, 65, 55, 75, 45, 55, 40, 60, 75, 45, 50,42];
					lineData = [30, 25, 45, 30, 25, 35, 20, 45, 35, 30, 35,20];
			}
			chart.updateSeries([
				{
					name: "Number of Projects",
					type: 'column',
					data: columnData
				},{
					name: 'Revenue',
					type: 'area',
					data: areaData
				},{
					name: 'Active Projects',
					type: 'line',
					data: lineData
				}
			]);
		})
	}
	var swiperreview = function() {
		
		var swiper = new Swiper('.mySwiper', {
			speed: 1500,
			parallax: true,
			slidesPerView: 4,
			spaceBetween: 20,
			autoplay: {
				delay: 1000,
			},
			navigation: {
				nextEl: ".swiper-button-next",
				prevEl: ".swiper-button-prev",
			},
			breakpoints: {
				
			  300: {
				slidesPerView: 1,
				spaceBetween: 20,
			  },
			  416: {
				slidesPerView: 2,
				spaceBetween: 20,
			  },
			   768: {
				slidesPerView: 2,
				spaceBetween: 20,
			  },
			   1280: {
				slidesPerView: 3,
				spaceBetween: 20,
			  },
			  1788: {
				slidesPerView: 3,
				spaceBetween: 20,
			  },
			},
		});
	}	
	var projectChart = function(){
		var options = {
			series: [30, 40, 20],
			 chart: {
				type: 'donut',
				width: 210,
			},

			plotOptions: {
				pie: {
				  donut: {
					size: '75%',
					labels: {
						show: true,
						name: {
							show: true,
							offsetY: 12,
						},
						value: {
							show: false,
							fontSize: '24px',
							fontFamily:'Arial',
							fontWeight:'500',
							offsetY: -17,
						},
						total: {
							show: false,
							fontSize: '11px',
							fontWeight:'500',
							fontFamily:'Arial',
							label: 'Total projects',
						   
							formatter: function (w) {
							  return w.globals.seriesTotals.reduce((a, b) => {
								return a + b
							  }, 0)
							}
						}
					}
				  }
				}
			},
			stroke: {
				show: true,
				curve: 'smooth',
				lineCap: 'butt',
				colors: '#000',
				width: 1,
				dashArray: 0,      
			},
			legend: {
				show: false,
			},
			colors: ['#FFF37A', 'var(--primary-light)', '#FFD0C5'],
			labels: ["Male", "Female","Other"],
			dataLabels: {
				enabled: false,
			},
		};
		var chartBar1 = new ApexCharts(document.querySelector("#projectChart"), options);
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
 
	/* Function ============ */
		return {
			init:function(){
			},
			
			
			load:function(){
				NewExperience();
				overiewChart();
				projectChart();
				handleWorldMap();
				chartBar();
				expensesChart();
				impressionChart();
				swiperreview();
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
