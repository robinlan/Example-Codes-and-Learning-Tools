var path = require('path');
var fs = require("fs");
var qs = require('querystring');
var express = require("express");
var app = express();	
app.listen(80);

var response = function(res, type, text) {
	res.writeHead(200, {'Content-Type': type});
	res.write(text);
	console.log(text);
	res.end();
}

app.get('*', function(req, res){
  try {
	var path = '.' + req.url;
	pathStat = fs.statSync(path);
	if (pathStat.isFile()) {
		fs.readFile(path, "utf8", function(err, file) {
			response(res, "text/html", file);
		});
	} else if (pathStat.isDirectory()) {
		var html = "<html><body>";
		var files = fs.readdirSync(path);
		for (f in files) {
			fname = files[f];
			filePath = req.url+fname;
			html += "<li><a href='"+filePath+"'>"+filePath+"</a></li>\n";
		}
		html += "<body></html>"
		response(res, "text/html", html);
	} 
  } catch (err) {
	response(res, "text/plain", err.toString());
  }
});

console.log('start WebServer\n');