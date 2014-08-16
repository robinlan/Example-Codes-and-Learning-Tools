class Int {
private:
  int val;
public:
  Int(int input=0) { val = input; }
  
  int key() const { return val; }
  
  operator= (int input) { val = input; }
};


ostream& operator<<(ostream& s, const Int& i)
  { return s << i.key(); }
ostream& operator<<(ostream& s, const Int* i)
  { return s << i->key(); }
