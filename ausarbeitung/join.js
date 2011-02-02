dojo.require("dojox.grid.DataGrid");
dojo.require("dojo.data.ItemFileReadStore");
/* required modules for the basic chart */
//Main 2d chart class
dojo.require("dojox.charting.Chart2D");
dojo.require("dojox.charting.action2d.Magnify");
dojo.require("dojox.charting.action2d.Tooltip");
//Class to create a legend for our chart
dojo.require("dojox.charting.widget.Legend");
//A theme for our chart
dojo.require("dojox.charting.themes.PlotKit.green");

dojo.provide("my.charter.Join");
dojo.declare("my.charter.Join", null, {
	createPlot: function(node) {
		var node = new dojox.charting.Chart2D(node);
		//add the default plot
		node.addPlot("default", {
				//type of chart
				type: "Lines",
				//show markers at number points?
				markers: true,
				//curve the lines on the plot?
				tension: "S",
				//show lines?
				lines: true,
				//fill in areas?
				areas: false,
				//offset position for label
				labelOffset: -30,
		});
		node.addAxis("x", {
			natural: true,
			includeZero:true,
			labels: [
				{value: 0, text: "Strings"},
				{value: 1, text: "10"},
				{value: 2, text: "50"},
				{value: 3, text: "100"},
				{value: 4, text: "500"},
				{value: 5, text: "1000"},
			]
		});
		node.addAxis("y", { 
			vertical:true,
			includeZero:true,
			labels: [
				{value: 0, text: "ms"}
			]
		});
		var magnify = new dojox.charting.action2d.Magnify(node, "default");
		var tip = new dojox.charting.action2d.Tooltip(node, "default");
		return node;
	},

	renderPlot: function(node,data,title) {
		dojo.create("h3",{innerHTML:title}, node);
		var div = dojo.create("div",null, node);
		var node = this.createPlot(div);
		node.addSeries("ship as a whole",data["ship"], { stroke: "green"});
		node.addSeries("semi join v1",data["semi1"], { stroke: "blue"});
		node.addSeries("semi join v2",data["semi2"], { stroke: "yellow"});
		node.addSeries("semi join v3",data["semi3"], { stroke: "magenta"});
		node.addSeries("semi join v4",data["semi4"], { stroke: "cyan"});
		node.render();
		return node;
	},
				
	createGrid: function(node,data) {
		var table = dojo.create("table",{border:1}, node);
		var row = dojo.create("tr",null,table);
		dojo.create("th",{innerHTML:""},row);
		dojo.create("th",{innerHTML:"10"},row);
		dojo.create("th",{innerHTML:"50"},row);
		dojo.create("th",{innerHTML:"100"},row);
		dojo.create("th",{innerHTML:"500"},row);
		dojo.create("th",{innerHTML:"1000"},row);
		dojo.create("th",{innerHTML:"Strings"},row);

		for(var method in data){
			var row = dojo.create("tr",null,table);
			dojo.create("td",{innerHTML:method},row);
			dojo.forEach(data[method], function(point) {
				dojo.create("td",{innerHTML:point},row);
			});
			dojo.create("td",{innerHTML:"ms"},row);
		}
		

	}
});

dojo.ready(function() {
	var charter = new my.charter.Join();

	var distJoin = {
		ship:  [2 , 3 , 9 , 31 , 68],
		semi1: [13, 22, 61, 115, 222],
		semi2: [11, 43, 35, 153, 177],
		semi3: [60, 88, 79, 204, 325],
		semi4: [5 , 13, 11, 15 , 4],
		};
	var node = charter.renderPlot("joinchartDistJoin", distJoin, "Join Zeit");
	charter.createGrid(node.node,distJoin);

	var distNetwork = {
		ship:  [25, 33, 51, 150, 228],
		semi1: [26, 45, 56, 153, 211],
		semi2: [16, 26, 58, 110, 129],
		semi3: [49, 69, 89, 159, 270],
		semi4: [49, 76, 83, 165, 162],
		};
	var node = charter.renderPlot("joinchartDistNetwork", distNetwork, "Netzwerk Traffic");
	charter.createGrid(node.node,distNetwork);

	var distAll = {
		ship:  [34 , 43 , 68 , 188, 303],
		semi1: [45 , 73 , 134, 274, 441],
		semi2: [31 , 79 , 96 , 266, 323],
		semi3: [94 , 123, 134, 301, 440],
		semi4: [100, 163, 131, 303, 315],
		};
	var node = charter.renderPlot("joinchartDistAll", distAll, "Komplette Dauer");
	charter.createGrid(node.node,distAll);

	var fetchAsNeeded = {
		join:    [474, 5786, 12180, 186689, 0],
		network: [273, 3078, 6395 , 99010 , 0],
		all:     [562, 5930, 12278, 186848, 0],
		};
	var node = charter.createPlot("joinchartFetchAsNeeded");
	node.addSeries("fetchAsNeeded join",fetchAsNeeded["join"], { stroke: "red"});
	node.addSeries("fetchAsNeeded network",fetchAsNeeded["network"], { stroke: "indigo"});
	node.addSeries("fetchAsNeeded all",fetchAsNeeded["all"], { stroke: "green"});
	node.render();
	charter.createGrid(node.node,fetchAsNeeded);

});

