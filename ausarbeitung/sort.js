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

dojo.provide("my.charter.Sort");
dojo.declare("my.charter.Sort", null, {
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
				{value: 1, text: "1 * 10^3"},
				{value: 2, text: "5 * 10^3"},
				{value: 3, text: "7 * 10^3"},
				{value: 4, text: "1 * 10^4"},
				{value: 5, text: "3 * 10^4"},
				{value: 6, text: "5 * 10^4"},
				{value: 7, text: "7 * 10^4"},
				{value: 8, text: "1 * 10^5"},
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

	renderPlot: function(node,data) {
		var node = this.createPlot(node);
		node.addSeries("merge sort",data["merge"], { stroke: "green"});
		node.addSeries("distribution sort",data["dist"], { stroke: "blue"});
		node.render();
		return node;
	},
				
	createGrid: function(node,data) {
		var table = dojo.create("table",{border:1}, node);
		var row = dojo.create("tr",null,table);
		dojo.create("th",{innerHTML:""},row);
		dojo.create("th",{innerHTML:"1 * 10^3"},row);
		dojo.create("th",{innerHTML:"5 * 10^3"},row);
		dojo.create("th",{innerHTML:"7 * 10^4"},row);
		dojo.create("th",{innerHTML:"1 * 10^4"},row);
		dojo.create("th",{innerHTML:"3 * 10^4"},row);
		dojo.create("th",{innerHTML:"5 * 10^4"},row);
		dojo.create("th",{innerHTML:"7 * 10^4"},row);
		dojo.create("th",{innerHTML:"1 * 10^5"},row);
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
	var charter = new my.charter.Sort();
	var local = {
		local: [19, 25, 6, 15, 23, 38, 63, 96],
		};
	var node = charter.createPlot("chart1");
	node.addSeries("local sort",local["local"], { stroke: "red"});
	node.render();
	charter.createGrid(node.node,local);

	var run1 = {
		merge: [122, 330, 159, 93, 292, 692, 1337, 2644],
		dist: [120, 189, 161, 79, 267, 776, 1876, 4195]
		};
	var node = charter.renderPlot("chart2", run1);
	charter.createGrid(node.node,run1);

	var run2 = {
		merge: [93, 328, 335, 122, 498, 1261, 2321, 4748],
		dist: [169, 363, 131, 61, 797, 1367, 4240, 6579]
		};
	var node = charter.renderPlot("chart3", run2);
	charter.createGrid(node.node,run2);

	var run3 = {
		merge: [102, 463, 157, 128, 482, 1253, 2384, 4655],
		dist: [121, 271, 156, 98, 499, 2037, 3388, 4715]
		};
	var node = charter.renderPlot("chart4", run3);
	charter.createGrid(node.node,run3);

	var run4 = {
		merge: [132, 403, 199, 168, 559, 1377, 2554, 5009],
		dist: [172, 225, 131, 151, 694, 1433, 2665, 8981]
		};
	var node = charter.renderPlot("chart5", run4);
	charter.createGrid(node.node,run4);

});
