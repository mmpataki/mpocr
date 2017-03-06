var mdiv;
var debug = false;

function convert_to_png(svg) {

	var
		xml = new XMLSerializer().serializeToString(svg),
		data = "data:image/svg+xml;base64," + btoa(xml),
		img = new Image()

	img.setAttribute('src', data)
	document.body.appendChild(img);
}

window.onresize = window.onload = function dgs() {
	mdiv = document.getElementById("mdiv");
	var xs = document.getElementsByClassName("mchart");
	for (var i = 0; i < xs.length; i++) {

		if(xs[i].children[0] == null) {
			var ele = document.createElement("div");
			ele.style.display = "block";
			ele.style.height = "100%";
			xs[i].appendChild(ele);
		}

		draw_chart(xs[i].children[0], JSON.parse(xs[i].getAttribute("gdata")));
	}
}

function draw_chart(ele, gdata) {

	/* find the max and minimum from the data */
	var max = min = gdata.datasets[0].data[0];
	for (var i = 0; i < gdata.datasets.length; i++) {
		for (var j = 0; j < gdata.datasets[i].data.length; j++) {
			var x = gdata.datasets[i].data[j];
			max = (max < x) ? x : max;
			min = (min > x) ? x : min;
		}
	}

	gdata.background = gdata.background == null ? "transparent" : gdata.background;

	for (var i = 0; i < gdata.datasets.length; i++) {
		var data = gdata.datasets[i];
		data.stroke = data.stroke == null ? "#000" : data.stroke;
		data.fill = data.fill == null ? "red" : data.fill;
		data.opacity = data.opacity == null ? "0.6" : data.opacity;
		data.cradius = data.cradius == null ? 3 : data.cradius;
		data.cfill = data.cfill == null ? "white" : data.cfill;
	}

	var ht = +ele.offsetHeight;
	var wd = +ele.offsetWidth;
	var dtemp = "", ctemp = "", points = "";
	var xoff = 30;
	var yoff = 20;
	var yaspect = (ht - (2 * yoff)) / max;
	var xaspect = (wd - (1 * xoff)) / (gdata.datasets[0].data.length + (gdata.type == "bar" ? 0 : -1));
	var yh = ht / gdata.ydivs;
	var xw = (wd + xoff) / gdata.xdivs;

	ht -= yoff;

	var glines = "";
	var glabels = "";

	/*  horizontal grids */
	if (gdata.hgrids) {
		for (var tmp = ht; tmp > 0; tmp -= yh) {
			glines += `<line x1="${xoff}" y1="${tmp}" x2="${wd}" y2="${tmp}" style="stroke:lightgray; stroke-width: 1px;"/>`;
			glabels += `<text x='${xoff-10}' y='${tmp}' text-anchor='end'>${Math.round((ht - tmp) / yaspect)}</text>`;
		}
	}
	/*  vertical grids */
	if (gdata.vgrids) {
		for (var tmp = xoff; tmp < wd; tmp += xw) {
			glines += `<line x1="${tmp}" y1="${0}" x2="${tmp}" y2="${ht}" style="stroke:lightgray; stroke-width: 1px;"/>`;
			glabels += `<text x='${tmp}' y='${ht+yoff}' text-anchor='middle'>${Math.round(tmp / xaspect)}</text>`;
		}
	}

	var x, y;
	if (gdata.type == "bar") {

		for (var i = 0; i < gdata.datasets.length; i++) {
			var dataset = gdata.datasets[i];
			for (var j = 0; j < dataset.data.length; j++) {
				x = xoff + (j * xaspect);
				y = (ht - (yaspect * dataset.data[j]));
				dtemp += `<rect x='${x}' y='${y}' width='${xaspect}' height='${ht-y}' style='fill: ${dataset.fill}; opacity: ${dataset.opacity}'><title>${dataset.data[j]}</title></rect>`;
			}
		}
	} else if(gdata.type == "dot" || gdata.type == "region") {

		for (var i = 0; i < gdata.datasets.length; i++) {

			var dataset = gdata.datasets[i];

			var scolor = (gdata.type == "dot") ? dataset.stroke : "transparent";
			var cfcolor = (gdata.type == "dot") ? dataset.cfill : "transparent";

			for (var j = 0; j < dataset.data.length; j++) {
				x = xoff + (j * xaspect);
				y = (ht - (yaspect * dataset.data[j]));
				points += x + "," + y + " ";
				ctemp  += `<circle cx="${x}" cy="${y}" r="${dataset.cradius}" stroke="${scolor}" stroke-width="2" fill="${cfcolor}"><title>${dataset.data[j]}</title></circle>`;
			}

			if(gdata.type == "dot") {
				dtemp += `<polyline style='fill:none; stroke:${dataset.stroke}; stroke-width:2px;' points='${points}'/>${ctemp}`;
			} else {
				dtemp += `<polygon style='fill: ${dataset.fill}; stroke:${dataset.fill}; opacity: ${dataset.opacity}; stroke-width:2px;' points='${points} ${wd},${y} ${wd},${ht} ${xoff},${ht} ${xoff},${(ht-(yaspect*dataset.data[0]))} ${xoff}'/>${ctemp}`;
			}

			ctemp = "";
			points = "";
		}
	}
	ele.innerHTML = `<svg height='${ht + yoff}' width='${wd}' style='background: ${gdata.background};'>${glines}${dtemp}${glabels}</svg>`;

	if(debug) {
		mdiv.innerHTML = "";
		wkv("height", ht);
		wkv("width", wd);
		wkv("xoffset", xoff);
		wkv("yoffset", yoff);
		wkv("max", max);
		wkv("min", min);
		wkv("x-aspect ratio", xaspect);
		wkv("y-aspect ratio", yaspect);
		wkv("grid-y-rowheight", yh);
		wkv("grid-x-colwidth", xw);
		wkv("data", JSON.stringify(gdata.datasets));
	}

}

function wkv(key, value) {
	mdiv.innerHTML += `<tr><td style="width: 200px;"><b>${key}</b></td><td>${value}</td></tr>`;
}
