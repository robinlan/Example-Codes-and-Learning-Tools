var util = require("util");

function Parent() {
  this.age = 40;
}

Parent.prototype.toStr = function() {
  return "Parent:toStr age="+this.age;
}

Parent.prototype.callf = function() {
  console.log("Parent:callf");
  return this.f();
}

function Child() {
  this.age = 10;
}

util.inherits(Child, Parent);

Child.prototype.toStr = function() {
    return "Child:toStr age="+this.age;
}

Child.prototype.f = function() {
   console.log("Child:f");
}

Child.prototype.print = function() {
  return console.log(this.toStr());
}

parent = new Parent();
console.log(parent.toStr());
// parent.print();
child = new Child();
console.log(child.toStr());
child.print();
child.callf();
