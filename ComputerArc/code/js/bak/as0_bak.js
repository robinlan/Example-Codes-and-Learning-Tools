var fs = require("fs");
var c = require("./ccc");
var util = require("util");
var opTable = require("./cpu0").opTable;
var Memory = require("./memory");

var as0 = function() {
 this.symTable = {};
 
 this.assemble = function(asmFile, objFile) {                    // 組譯器的主要函數
  c.log("Assembler:asmFile=%s objFile=%s", asmFile, objFile); // 輸入組合語言、輸出目的檔
  c.log("===============Assemble=============");

  var text = fs.readFileSync(asmFile, "utf8");                   // 讀取檔案到 text 字串中
  this.lines = text.split(/[\r\n]+/);                            // 將組合語言分割成一行一行
  c.log(this.lines);
  this.pass1(this.lines);                                        // 第一階段：計算位址
  c.log("===============SYMBOL TABLE=========");              // 印出符號表
  for (s in this.symTable) {
    c.log("%s %s", c.fill(' ',s,8), c.hex(this.symTable[s].address, 4));
  }
  this.pass2(this.codes);                                        // 第二階段：建構目的碼
  this.saveObjFile(objFile);                                     // 輸出目的檔
 }

 this.pass1 = function(lines) {                         // 第一階段的組譯
  var address = 0;
  c.log("=================PASS1================");
  this.codes = [];
  for (i in lines) {                                    // 對於每一行
    try {
      var code = new Code(lines[i]);                      // 剖析並建立 code 物件
      code.address = address;                             // 設定該行的位址
      if (code.label.length != "")                        // 如果有標記符號
        this.symTable[code.label] = code;                 //    加入符號表中
      this.codes.push(code);
      code.print();
      address += code.size();                             //  計算下一個指令位址
    } catch (err) {
      this.error(i, lines[i], err);
    }
  }
 }

 this.pass2 = function() {                       // 組譯器的第二階段
  c.log("=============PASS2==============");
  for (i in this.codes) {                        // 對每一個指令
    try {
      var code = this.codes[i];
      this.translate(code);                        //   進行編碼動作
      code.print();
    } catch (err) {
      this.error(i, this.lines[i], err);
    }
  }
 }
 
 this.translate = function(code) {               // 指令的編碼函數
  var ra=0, rb=0, rc=0, cx=0;
  var pc = code.address + 4;                     // 提取後PC為位址+4
  var args = code.args;
  var labelCode = null;
  var symTable = this.symTable;
  switch (code.op.type) {                        // 根據指令型態
    case 'J' :                                   // 處理 J 型指令
      switch (code.op.name) {
        case "RET": case "IRET" : 
            break;
        case "SWI" : 
            cx = parseInt(args[0]);
            break;
        default :
            labelCode = symTable[args[0]];           //   取得符號位址
            cx = labelCode.address - pc;             //   計算 cx 欄位
            break;
      }
      code.obj = c.hex(code.op.id,2)+c.hex(cx, 6); //   編出目的碼(16進位)
      break;
    case 'L' :
      ra = this.parseR(args[0]);
      switch (code.op.name) {
        case "LDI" : 
            cx = parseInt(args[1]);
            break;
        case "LDR": case "LBR": case "STR": case "SBR":
            rb = this.parseR(args[1]);
            rc = this.parseR(args[2]);
            break;
        default :
            if (args[1].match(/^[a-zA-Z]/)){ // LABEL
              labelCode = symTable[args[1]];
              rb = 15; // R[15] is PC
              cx = labelCode.address - pc;
            } else {
              rb = 0;
              cx = parseInt(args[2]);
            }
            break;
      }
      code.obj = c.hex(code.op.id, 2)+c.hex(ra, 1)+c.hex(rb, 1)+c.hex(cx, 4);
      break;
    case 'A' :                                   // 處理 A 型指令
      ra = this.parseR(args[0]);
      switch (code.op.name) {
        case "CMP": case "MOV" : 
            rb = this.parseR(args[1]);
            break;
        case "SHL": case "SHR": case "ADDI":
            rb = this.parseR(args[1]);
            cx = parseInt(args[2]);
            break;
        case "PUSH": case "POP": case "PUSHB": case "POPB" : 
            break;
        default : 
            rb = this.parseR(args[1]);
            rc = this.parseR(args[2]); 
            break;
      }
      code.obj = c.hex(code.op.id, 2)+c.hex(ra, 1)+c.hex(rb, 1)+c.hex(rc,1)+c.hex(cx, 3);
      break;
    case 'D' : { // 我們將資料宣告  RESW, RESB, WORD, BYTE 也視為一種指令，其形態為 D
      var unitSize = 1;
      switch (code.op.name) {                    
        case "RESW":  case "RESB": // 如果是 RESW 或 RESB
          code.obj = c.dup('0', code.size()*2);
          break;                            
        case "WORD":                             // 如果是 WORD ，佔 4 個 byte
          unitSize = 4;
        case "BYTE": {                           // 如果是 BYTE ，佔 1 個 byte
          code.obj = "";
          for (i in args) {
            if (args[i].match(/^\".*?\"$/)) {  // 字串，例如： "Hello!" 轉為 68656C6C6F21
              var str = args[i].substring(1, args[i].length-1);
              code.obj += c.str2hex(str);
            } else if (args[i].match(/^\d+$/)) {  // 常數
              code.obj += c.hex(parseInt(args[i]), unitSize*2);
            } else {                               // 標記
              labelCode = symTable[args[i]];
              code.obj += c.hex(labelCode.address, unitSize*2);
            }
          }
          break;
        } // case BYTE:
      } // switch
      break;
    } // case 'D'
  }
 }

 this.saveObjFile = function(objFile) {
  c.log("=================SAVE OBJ FILE================");
  var obj = "";
  for (i in this.codes)
    obj += this.codes[i].obj;
  var m = new Memory(1);
  m.loadhex(obj);
  m.dump();
  m.save(objFile);
 }
 
 this.parseR = function(str) {
  var rmatch = /R(\d+)/.exec(str);
  if (rmatch == null)
    return NaN;
  return parseInt(rmatch[1]);
 }

 this.error = function(i, line, err) {
   c.log("line %d : %s ==> error !", i, line);
   c.log("Error : (%s):%s", err.name, err.message);
   console.log(err.stack);
 }
}

var Code = function(line) {
  this.print = function() {
    c.log(this.toString());
  }

  this.toString = function() {
    return util.format("%s %s %s %s %s %s %s", c.hex(this.address, 4), 
      c.fill(' ',this.label,8), c.fill(' ',this.op.name, 8), 
      c.fill(' ',this.args, 16), this.op.type, c.hex(this.op.id,2), this.obj);
  }
  
  this.size = function() {
    var len = 0, unitSize = 1;
    switch (this.op.name) {                             // 根據運算碼 op
      case "RESW" : return 4 * parseInt(this.args[0]);  // 如果是 RESW, 大小為 4*保留量(參數 0)
      case "RESB" : return 1 * parseInt(this.args[0]);  // 如果是 RESB, 大小為 1*保留量(參數 0)
      case "WORD" : 
        unitSize = 4;
      case "BYTE" : 
        for (i in this.args) {
          if (this.args[i].match(/^\".*?\"$/))
            len += (this.args[i].length - 2) * unitSize;
          else
            len += unitSize;
        }
        return len;        // 如果是BYTE, 大小是 1*參數個數
      case "" :     return 0;                           // 如果只是標記, 大小為 0
      default :     return 4;                           // 其他情形 (指令), 大小為 4
    }
  }
  
  var labCmd = /^((\w+):)?\s*([^;]*)/;
  var parts  = labCmd.exec(line);           // 分割出標記與命令
  this.label = c.nonull(parts[2]);          // 取出標記 (\w+)
  var tokens = parts[3].trim().split(/[ ,\t\r]+/); // 將命令分割成基本單元
  var opName = tokens[0];                   // 取出指令名稱
  this.args  = tokens.slice(1);             // 取出參數部份
  this.op    = opTable[opName];
  this.obj   = "";
}

new as0().assemble(process.argv[2], process.argv[3]);

module.exports = as0;