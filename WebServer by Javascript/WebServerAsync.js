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
	fs.stat(path, function(err, pathStat) {
		if (err) {
			response(res, "text/plain", err.toString());
			return;
		}
		if (pathStat.isFile()) {
			fs.readFile(path, "utf8", function(err, file) {
				response(res, "text/html", file);
			});
		} else if (pathStat.isDirectory()) {
			var dirPath = req.url;
			if (dirPath.substring(-1)!=="/") 
				dirPath = dirPath+"/";
			var html = "<html><body><h1>"+req.url+"</h1>\n";
			fs.readdir(path, function(err, files) {
				for (f in files) {
					fname = files[f];
					filePath = dirPath+fname;
					html += "<li><a href='"+filePath+"'>"+fname+"</a></li>\n";
				}
				html += "<body></html>";
				response(res, "text/html", html);
			});
		} 
	});	
  } catch (err) {
	response(res, "text/plain", err.toString());
  }
});

console.log('start WebServer\n');