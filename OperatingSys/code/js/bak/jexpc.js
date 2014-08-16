// STMTS = {} | STMT {; STMT_LIST}?
// STMT = ASSIGN | FOR | WHILE | IF | return E;
// ASSIGN = (ID[++|--]?)?(=E)?
// E=T ([+|-|*|/|%|&|^|||&&||||>>|<<|==|<=|>=|<|>] F)?
// T=STRING | INTEGER | FLOAT | '(' E ')' | ID ('('ARGS')')?
// ARGS = {} | E {, ARGS}?

var c = require("./ccc");

var symTable = {};

var tempIdx = 0;
var nextTemp=function() { return "T"+tempIdx++; }

var stack = [];
var push=function(o) { stack.push(o); }
var pop=function() { return stack.pop(); }

var tokens = [];
var tokenIdx = 0;
// 本來應該用 .*? 來比對 /*...*/ 註解的，但 javascript 的 . 並不包含 \n, 因此用 \s\S 代替 . 就可以了。
var retok = /(\/\*[\s\S]*?\*\/)|(\/\/[^\r\n])|(\d+)|([a-zA-Z]\w*)|(\r?\n)|(.)/gm; // *?, +? non greedy, m for multiline

var source  = "";
var scan=function(text) { 
  tokenIdx = 0;
  tokens = text.match(retok); 
  return tokens; 
}

var error=function(o) {  printf("Error: %j\n", o); }
var pcode = function(op, t, t1, t2) { printf("%s %s %s %s\n", op, t, t1, t2); }

var next=function(o) {
  if (o==null || isNext(o))
    return tokens[tokenIdx++];
  error(token);
}

var isNext=function(o) {
  if (tokenIdx >= tokens.length) 
    return false;
  var token = tokens[tokenIdx];
  if (o instanceof RegExp) {
    return token.match(o);
  } else {
    return (token == o);
  }
}

var compile=function(text) {
  scan(text);
  printf("text=%s\n", text);
  printf("tokens=%j\n", tokens);
  E();
  printf("symTable=%j\n", symTable);
}

// STMT_LIST = {} | STMT {; STMT_LIST}?
var STMT_LIST=function() {
  STMT();
  if (isNext(';')) STMTS_LIST();
  return t1;
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

// ASSIGN = (ID[++|--]?)?(=E)?
var ASSIGN=function() {
  var id="", op="";
  if (isNextType("ID")) {
    id = next(null);
    if (isNext("++") || isNext("--"))
      op = next(null);
  }
  if (isNext("=")) {
    E();
  }
}

// E=T ([+|-|*|/|%|&|^|||&&||||>>|<<|==|<=|>=|<|>] T)?
var E=function() {
  var op = "";
  t1 = T();
  if (isNext(/([\+\-\*\/%&^|<>])|(&&)|(||)|(>>)|(<<)|(<=)|(>=)/))
    op = next(null);
    t2 = T();
    t = nextTemp();
    pcode(op, t, t1, t2);
  }
}

// T=STRING | INTEGER | FLOAT | '(' E ')' | ID ('('ARGS')')?
var T=function() {
  if (isNextType("STRING|INTEGER|FLOAT")) {
    return next(null);
  } else if (isNext("(")) {
    next("(");  E();  next(")");
  } else if (isNextType("ID")) {
    id=next(null); next("("); ARGS(); next(")");
    pcode("call", nextTemp(), id, "");
  }
}

printf("=== EBNF Grammar =====\n");
printf("E=T ([+-] T)*\n");
printf("T=F ([*/] F)*\n");
printf("F=NUMBER | ID | '(' E ')'\n");
compile("32+5*(182+degree*4-20)");
// printf("typeof(/\w+/)=%s /\w+/ instanceof RegExp=%s\n", typeof(/\w+/), /\w+/ instanceof RegExp);
// printf("typeof(str)=%s\n", typeof("str"));

