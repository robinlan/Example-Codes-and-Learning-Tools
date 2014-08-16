var fs = require("fs");
require("./ccc");

var tokens = [];
var tokenIdx = 0;

var scan=function(text) { 
  var re = new RegExp(/(\/\*[\s\S]*?\*\/)|(\/\/[^\r\n])|(".*?")|(\d+(\.\d*)?)|([a-zA-Z]\w*)|([>=<!\+\-\*\/&%|]+)|(\s+)|(.)/gm);
  var types = [ "", "blockcomment", "linecomment", "string", "integer", "float", "id", "op2", "space", "ch" ];
  tokens = [];
  tokenIdx = 0;
  var lines = 1, m;
  while((m = re.exec(text)) !== null) {
    var token = m[0], type;
    for (i=1; i<=9; i++) { 
      if (m[i] !== undefined)
        type = types[i];
    }
    if (!token.match(/^[\s\r\n]/)) {
      tokens.push({ "token":token, "type":type, "lines":lines });
      log("token="+token+" type="+type+" lines="+lines);
    }
    lines += token.split(/\n/).length-1;
  }
  return tokens;
}

var error=function() {  printf("Error: token=%j\n", tokens[tokenIdx]); }

var next=function(o) {
  if (o==null || isNext(o)) {
    printf("next : %j\n", tokens[tokenIdx]);
    return tokens[tokenIdx++].token;
  }
  error();
}

var isNext=function(o) {
  if (tokenIdx >= tokens.length) 
    return false;
  var token = tokens[tokenIdx].token;
  if (o instanceof RegExp) {
    return token.match(o);
  } else {
    return (token == o);
  }
}

var isNextType=function(pattern) {
  var type = tokens[tokenIdx].type;
  return (("|"+pattern+"|").indexOf("|"+type+"|")>=0);
}

var compile=function(text) {
  printf("text=%s\n", text);
  scan(text);
  printf("tokens=%j\n", tokens);
  STMT_LIST();
}

// BLOCK = { STMT_LIST }
var BLOCK=function() {
  next("{"); STMT_LIST(); next("}");
}

// STMT_LIST = {} | STMT {; STMT_LIST}?
var STMT_LIST=function() {
  STMT();
  if (isNext(';')) { next(';'); STMT_LIST(); }
}

// STMT = FOR | WHILE | IF | return E | ASSIGN
var STMT=function() {
  if (isNext("for")) {
    FOR();
  } else if (isNext("while")) {
    WHILE();
  } else if (isNext("if")) {
    IF();
  } else if (isNext("return")) {
    next("return");
    E();
  } else {
    ASSIGN();
  }
}

// FOR = for (STMT; E; STMT) BLOCK
var FOR=function() {
  next("for"); next("("); STMT(); next(";"); E(); next(";"); STMT(); next(")"); BLOCK();
}

// WHILE = while (E) BLOCK
var WHILE=function() {
  next("while"); next("("); E(); next(")"); BLOCK();
}

// IF = if (E) BLOCK (else if (E) BLOCK)* (else BLOCK)?
var IF=function() {
  next("if"); next("("); E(); next(")"); BLOCK();
  while (isNext("else")) {
    next("else"); 
    if (isNext("if")) {
      next("if"); next("("); E(); next(")"); BLOCK();
    } else {
      BLOCK();
    }
  }
}

// ASSIGN = (ID[++|--]?)?(=E)?
var ASSIGN=function() {
  var id="", op="";
  if (isNextType("id")) {
    id = next(null);
    if (isNext("++") || isNext("--"))
      op = next(null);
  }
  if (isNext("=")) {
    next("=");
    E();
  }
}

// E=T ([+|-|*|/|%|&|^|||&&||||>>|<<|==|<=|>=|<|>] T)?
var E=function() {
  t1 = T();
  if (isNextType("op2")) {
    var op = next(null);
    T();
  }
}

// T=STRING | INTEGER | FLOAT | ( E ) | FUNCTION | ID (ARGS)?
var T=function() {
  if (isNextType("string|integer|float")) {
    return next(null);
  } else if (isNext("(")) {
    next("(");  E();  next(")");
  } else if (isNext("function")) {
    FUNCTION();
  } else if (isNextType("id")) {
    id=next(null); 
    if (isNext("(")) { next("("); ARGS(); next(")"); }
  } else error();
}

// FUNCTION = function(ARGS) BLOCK
var FUNCTION = function() {
  next("function"); next("("); ARGS(); next(")"); BLOCK();
}

var source = fs.readFileSync("test.j1", "utf8");
compile(source);
