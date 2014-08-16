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
  PROG();
}

// NTS = NT { sep NT }*
var repeat=function(f, sep) {
  f();
  while (isNext(sep)) {
    next(sep);
    f();
  }
}

// PROG = STMTS
var PROG=function() {
  repeat(STMT, ';');
}

// BLOCK = { STMTS }
var BLOCK=function() {
  next("{"); 
  if (!isNext("}")) repeat(STMT, ";");
  next("}");
}

// STMT = FOR | WHILE | IF | return EXP | ASSIGN
var STMT=function() {
  if (isNext("for")) {
    FOR();
  } else if (isNext("while")) {
    WHILE();
  } else if (isNext("if")) {
    IF();
  } else if (isNext("return")) {
    next("return");
    EXP();
  } else {
    ASSIGN();
  }
}

// FOR = for (STMT; EXP; STMT) BLOCK
var FOR=function() {
  next("for"); next("("); STMT(); next(";"); EXP(); next(";"); STMT(); next(")"); BLOCK();
}

// WHILE = while (EXP) BLOCK
var WHILE=function() {
  next("while"); next("("); EXP(); next(")"); BLOCK();
}

// IF = if (EXP) BLOCK (else if (EXP) BLOCK)* (else BLOCK)?
var IF=function() {
  next("if"); next("("); EXP(); next(")"); BLOCK();
  while (isNext("else")) {
    next("else"); 
    if (isNext("if")) {
      next("if"); next("("); EXP(); next(")"); BLOCK();
    } else {
      BLOCK();
    }
  }
}

// ASSIGN = (ID[++|--]?)?(=EXP)?
var ASSIGN=function() {
  var id="", op="";
  if (isNextType("id")) {
    id = next(null);
    if (isNext("++") || isNext("--"))
      op = next(null);
  }
  if (isNext("=")) {
    next("=");
    EXP();
  }
}

// EXP=TERM (op2 TERM)?
var EXP=function() {
  t1 = TERM();
  if (isNextType("op2")) {
    var op = next(null);
    TERM();
  }
}

// TERM=STRING | INTEGER | FLOAT | FUNCTION | ARRAY | TABLE | ID (TERMS)? | ( EXP )
var TERM=function() {
  if (isNextType("string|integer|float")) {
    return next(null);
  } else if (isNext("function")) {
    FUNCTION();
  } else if (isNext("[")) {
    ARRAY();
  } else if (isNext("{")) {
    TABLE();
  } else if (isNextType("id")) {
    id=next(null); 
  } else if (isNext("(")) {
    next("(");  EXP();  next(")");
    if (isNext("(")) { next("("); TERMS(); next(")"); }
  } else error();
}

// FUNCTION = function(IDS) BLOCK
var FUNCTION = function() {
  next("function"); next("("); 
  if (!isNext(")")) repeat(ID, ","); 
  next(")"); BLOCK();
}

// ARRAY = [ TERMS ];
var ARRAY = function() {
  next("["); 
  if (!isNext("]")) repeat(TERM, ",");
  next("]");
}

// TABLE = { PAIRS }
var TABLE = function() {
  next("{"); 
  if (!isNext("}")) repeat(PAIR, ",");
  next("}");
}

// PAIR = TERM:TERM
var PAIR = function() {
  TERM(); next(":"); TERM();
}

var source = fs.readFileSync("test.j1", "utf8");
compile(source);
compile(fs.readFileSync("test2.j1", "utf8"));