var fs = require("fs");
var util = require("util");
var c = require("./ccc");
var opTable = require("./cpu1").opTable;
var Memory = require("./memory");

var parseR = function(str) {
  var rmatch = /R(\d+)/.exec(str);
  if (rmatch == null)
    return NaN;
  return parseInt(rmatch[1]);
}

var Assembler = function() {
 
 this.symTable = {};
 
 this.assemble = function(asmFile, objFile) {                    // 組譯器的主要函數
  c.log("\nAssembler:asmFile=%s objFile=%s\n", asmFile, objFile); // 輸入組合語言、輸出目的檔
  c.log("===============Assemble=============\n");

  var text = fs.readFileSync(asmFile, "utf8");                   // 讀取檔案到 text 字串中
  this.lines = text.split(/[\r\n]+/);                            // 將組合語言分割成一行一行
  c.log(this.lines);

  this.pass1(this.lines);                                        // 第一階段：計算位址
  c.log("===============SYMBOL TABLE=========\n");              // 印出符號表
  for (s in this.symTable) {
    c.log("%s %s\n", c.fill(' ',s,8), c.hex(this.symTable[s].address, 4));
  }
  this.pass2(this.codes);                                        // 第二階段：建構目的碼
  this.saveObjFile(objFile);                                     // 輸出目的檔
 }

 this.pass1 = function(lines) {                         // 第一階段的組譯
  var address = 0;
  c.log("\n=================PASS1================\n");
  this.codes = [];
  for (i in lines) {                                    // 對於每一行
    if (lines[i].length == 0) continue;
    var code = new Code(lines[i]);                      // 剖析並建立 code 物件
    code.address = address;                             // 設定該行的位址
    if (code.label.length != "")                        // 如果有標記符號
      this.symTable[code.label] = code;                 //    加入符號表中
    this.codes.push(code);
    code.print();
    address += code.size();                             //  計算下一個指令位址
  }
 }

 this.pass2 = function() {                       // 組譯器的第二階段
  c.log("=============PASS2==============\n");
  for (i in this.codes) {                        // 對每一個指令
    var code = this.codes[i];
    this.translate(code);                        //   進行編碼動作
    code.print();
  }
 }
 
 this.translate = function(code) {               // 指令的編碼函數
  var ra=0, rb=0, rc=0, cx=0;
  var pc = code.address + 4;                     // 提取後PC為位址+4
  var args = code.args;
  var labelCode = null;
  var symTable = this.symTable;
  if (code.op.type == "N") {
    code.obj = c.hex(code.op.id, 2) + c.hex(0, 6);
  } else if (code.op.type == "C") {
    ra = parseR(args[0]);
    rb = parseR(args[1]);
    cx = parseInt(args[2]);
    code.obj = c.hex(code.op.id, 2)+c.hex(ra, 1)+c.hex(rb, 1)+c.hex(cx, 4);
  } else if (code.op.type == "L") {
    ra = parseR(args[0]);
    rb = 15;
    labelCode = symTable[args[1]];
    cx = labelCode.address - pc;
    code.obj = c.hex(code.op.id, 2)+c.hex(ra, 1)+c.hex(rb, 1)+c.hex(cx, 4);
  } else if (code.op.type == "D") {
      var unitSize = 1;
      switch (code.op.name) {
        case "RESW":                             // 如果是 RESW
        case "RESB":                             //     或 RESB
          code.obj = c.dup('0', code.size()*2);
          break;                            
        case "WORD":                             // 如果是 WORD:
          unitSize = 4;
        case "BYTE": {                           // 如果是 BYTE : 輸出格式為 %2x
          code.obj = "";
          for (i in args) {
            if (args[i].match(/\d+/))            // 常數
              code.obj += c.hex(parseInt(args[i]), unitSize*2);
            else {                               // 標記
              labelCode = symTable[args[i]];
              code.obj += c.hex(labelCode.address, unitSize*2);
            }
          }
          break;
        } // case BYTE:
      } // switch
    } // if
 }

 this.saveObjFile = function(objFile) {
  c.log("\n=================SAVE OBJ FILE================\n");
  var obj = "";
  for (i in this.codes) {
    obj += this.codes[i].obj;
  }
  c.log("%s\n", obj);
  
  var m = new Memory(1);
  m.loadhex(obj);
  m.save(objFile);
 }
}

var Code = function(line) {
  this.print = function() {
    c.log(this.toString());
  }

  this.toString = function() {
    return util.format("%s %s %s %s %s %s", c.hex(this.address,-4), 
      c.fill(' ',this.label,8), c.fill(' ',this.op.name, 8), 
      c.fill(' ',this.args, 16), c.hex(this.op.id,2), this.obj);
  }
  
  this.size = function() {
    switch (this.op.name) {                             // 根據運算碼 op
      case "RESW" : return 4 * parseInt(this.args[0]);  // 如果是 RESW, 大小為 4*保留量(參數 0)
      case "RESB" : return 1 * parseInt(this.args[0]);  // 如果是 RESB, 大小為 1*保留量(參數 0)
      case "WORD" : return 4 * this.args.length;        // 如果是WORD, 大小是 4*參數個數
      case "BYTE" : return 1 * this.args.length;        // 如果是BYTE, 大小是 1*參數個數
      case "" :     return 0;                           // 如果只是標記, 大小為 0
      default :     return 4;                           // 其他情形 (指令), 大小為 4
    }
  }
  
  var labCmd = /^((\w+):)?\s*([^;]*)/;
  var parts  = labCmd.exec(line);           // 分割出標記與命令
  this.label = c.nonull(parts[2]);            // 取出標記 (\w+)
  var tokens = parts[3].split(/[ ,\t\r]+/); // 將命令分割成基本單元
  var opName = tokens[0];                   // 取出指令名稱
  this.args  = tokens.slice(1);             // 取出參數部份
  this.op    = opTable[opName];
  this.obj   = "";
}

var argv = process.argv;
var a = new Assembler();
a.assemble(argv[2], argv[3]);

module.exports = Assembler;