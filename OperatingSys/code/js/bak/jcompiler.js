var fs = require("fs");
var c  = require("./ccc");

var tokens = [];
var tokenIdx = 0;
var end = "$END";

var funcName = "main";
var funcStack = [ funcName ];
var symTable = {};
symTable[funcName] = { pcodes:[] };

var printf = c.printf;
var log    = c.log;
var filltail = c.filltail;

var scan=function(text) { 
  var re = new RegExp(/(\/\*[\s\S]*?\*\/)|(\/\/[^\r\n])|(".*?")|(\d+(\.\d*)?)|([a-zA-Z]\w*)|([>=<!\+\-\*\/&%|]+)|(\s+)|(.)/gm);
  var types = [ "", "COMMENT", "COMMENT", "STRING", "INTEGER", "FLOAT", "ID", "OP2", "SPACE", "CH" ];
  tokens = [];
  tokenIdx = 0;
  var lines = 1, m;
  while((m = re.exec(text)) !== null) {
    var token = m[0], type;
    for (i=1; i<=9; i++) { 
      if (m[i] !== undefined)
        type = types[i];
    }
    if (!token.match(/^[\s\r\n]/) && type!="COMMENT") {
      tokens.push({ "token":token, "type":type, "lines":lines });
      log("token="+token+" type="+type+" lines="+lines);
    }
    lines += token.split(/\n/).length-1;
  }
  tokens.push({ "token": end, "type":end, "lines":lines });
  return tokens;
}

var error=function(expect) {  
  var token = tokens[tokenIdx];
  printf("Error: line=%d token (%s) do not match expect (%s)!\n", token.lines, token.token, expect); 
  console.log(new Error().stack);
  process.exit(1);
}

var skip=function(o) { if (isNext(o)) next(o); }

var next=function(o) {
  if (o==null || isNext(o)) {
//    printf("next : %j\n", tokens[tokenIdx]);
    return tokens[tokenIdx++].token;
  }
  error(o);
}

var isNext=function(o) {
  if (tokenIdx >= tokens.length) 
    return false;
  var token = tokens[tokenIdx].token;
  if (o instanceof RegExp) {
    return token.match(o);
  } else
    return (token == o);
}

var nextType=function(o) {
  if (o==null || isNextType(o)) {
//    printf("next : %j\n", tokens[tokenIdx]);
    return tokens[tokenIdx++].token;
  }
  error(o);
}

var isNextType=function(pattern) {
  var type = tokens[tokenIdx].type;
  return (("|"+pattern+"|").indexOf("|"+type+"|")>=0);
}

var fix=function(s) {
  if (s == undefined) s = "";
  return filltail(' ', s, 8);
}

var pcode=function(label, op, p, p1, p2) {
  symTable[funcName].pcodes.push({"label":label, "op":op, "p":p, "p1":p1, "p2":p2});
  printf("%s %s %s %s %s %s\n", fix(funcName), fix(label), fix(op), fix(p), fix(p1), fix(p2));
}

var tempIdx = 1;
var nextTemp=function() { return "T"+tempIdx++; }

var labelIdx = 1;
var nextLabel=function() { return "L"+labelIdx++; }

var compile=function(text) {
  printf("text=%s\n", text);
  scan(text);
  printf("tokens=%j\n", tokens);
  PROG();
}

// PROG = STMTS
var PROG=function() {
  STMTS();
}

var STMTS=function() {
  while (!isNext("}") && !isNext(end))
    STMT();
}

// BLOCK = { STMTS }
var BLOCK=function() {
  next("{");
  STMTS();
  next("}");
}

// STMT = FOR | WHILE | IF | FUNCTION | return EXP ; | ASSIGN ; | BLOCK
var STMT=function() {
  if (isNext("for")) {
    FOR();
  } else if (isNext("while")) {
    WHILE();
  } else if (isNext("if")) {
    IF();
  } else if (isNext("function")) {
    FUNCTION();
  } else if (isNext("return")) {
    next("return");
    var e = EXP();
    pcode("", "RET", e, "", "");
    next(";");
  } else if (isNext("{")) {
    BLOCK();
  } else {
    ASSIGN();
    next(";");
  }
}

// FOR = for (ID in EXP) BLOCK
var FOR=function() {
  var startLabel = nextLabel(), exitLabel = nextLabel();
  next("for"); 
  next("("); 
  var id = nextType("ID");
  pcode("", "=", id, "0", "");
  next("in");
  var e=EXP(); 
  next(")");
  var t = nextTemp();
  pcode(startLabel, "<", t, id, e+".length");
  pcode("", "if0", t, exitLabel, "");
  BLOCK(); 
  pcode("", "goto", startLabel, "", "");
  pcode(exitLabel, "", "", "", "");
}

// WHILE = while (EXP) BLOCK
var WHILE=function() {
  var startLabel = nextLabel(), exitLabel=nextLabel();
  pcode(startLabel, "", "", "", "");
  next("while"); 
  next("("); 
  var e = EXP(); 
  next(")"); 
  pcode("", "if0", e, exitLabel, "");
  BLOCK();
  pcode("", "goto", startLabel, "", "");
  pcode(exitLabel, "", "", "", "");
}

// IF = if (EXP) STMT (else STMT)?
var IF=function() {
  next("if"); 
  next("("); 
  var e = EXP(); 
  next(")"); 
  var elseLabel = nextLabel();
  pcode("", "if0", e, elseLabel, "");
  STMT();
  if (isNext("else")) {
    next("else");
    pcode(elseLabel, "", "", "", "");
    STMT();
  }
}

// ASSIGN = ID[++|--]?(=EXP?)?
var ASSIGN=function() {
  var id, op, hasNext = false;
  if (isNextType("ID")) {
    id = nextType("ID");
    symTable[id] = "";
    if (isNext("++") || isNext("--")) {
      var op = next(null);
      pcode("", op, id, "", "");
    }
    hasNext = true;
  }
  if (isNext("=")) {
    next("=");
    var e = EXP();
    if (id != undefined)
      pcode("", "=", id, e, "");
    hasNext = true;
  }
  if (!hasNext)
    return EXP();
}

// EXP=TERM (OP2 TERM)?
var EXP=function() {
  t1 = TERM();
  if (isNextType("OP2")) {
    var op2 = next(null);
    t2 = TERM();
    var t = nextTemp();
    pcode("", op2, t, t1, t2);
    t1 = t;
  }
  return t1;
}

// TERM=STRING | INTEGER | FLOAT | ARRAY | TABLE | ID (TERMS)? | ID [TERMS]?| ( EXP )
var TERM=function() {
  if (isNextType("STRING|INTEGER|FLOAT")) {
    return next(null);
  } else if (isNext("[")) {
    return ARRAY();
  } else if (isNext("{")) {
    return TABLE();
  } else if (isNextType("ID")) { // function call
    var id = next(null);
    if (isNext("(")) { 
      next("("); 
      while (!isNext(")")) {
        TERM(); 
        skip(",");
      }
      next(")");
      var ret = nextTemp();
      pcode("", "call", ret, id, "");
      return ret;
    }
    var array = id;
    if (isNext("[")) { 
      next("["); 
      while (!isNext("]")) {
        var idx = TERM();
        var t = nextTemp();
        pcode("", "[]", t, array, idx);
        skip(",");
        array = t;
      }
      next("]");
      return array;
    }
    return id;
  } else if (isNext("(")) {
    next("(");  
    var e = EXP();  
    next(")");
    return e;
  } else error();
}

// FUNCTION = function ID(ARGS) BLOCK
var FUNCTION = function() {
  next("function");
  funcName = nextType("ID");
  funcStack.push(funcName);
  symTable[funcName] = { "pcodes": [] };
  pcode(funcName, "function", "", "", "");
  next("(");
  while (!isNext(")")) {
    var arg=nextType("ID");
    pcode("", "param", arg, "", "");
    skip(",");
  }
  next(")"); 
  BLOCK();
  funcStack.pop();
  funcName = funcStack[funcStack.length-1];
}

// ARRAY = [ TERMS ];
var ARRAY = function() {
  next("[");
  var array = nextTemp();
  pcode(array, "array", "", "", "");
  while (!isNext("]")) {
    var t = TERM();
    pcode("", "push", array, t, "");
    skip(",");
  }
  next("]");
  return array;
}

// TABLE = { (TERM:TERM)* }
var TABLE = function() {
  next("{"); 
  var table = nextTemp();
  pcode(table, "table", "", "", "");
  while (!isNext("}")) {
    var key = TERM(); 
    next(":"); 
    var value = TERM();
    skip(",");
    pcode("", "map", table, key, value);
  }
  next("}");
  return table;
}

var source = fs.readFileSync("test.j", "utf8");
compile(source);
