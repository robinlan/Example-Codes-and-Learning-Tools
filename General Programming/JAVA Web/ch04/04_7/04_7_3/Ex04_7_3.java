class Study {
     private int credit=0; 
	
     public void addcredit(int i) { credit += i; } 
     public int  totalcredit() { return credit; } 
}

class Ex04_7_3 {	
     public static void main(String[] args) {
           Study joe = new Study();  
           Study george = new Study();
		
           joe.addcredit(12);  
           george.addcredit(9);

           joe.addcredit(6);  
           george.addcredit(3);
		
           System.out.println("joe studied: "+joe.totalcredit()+"credites");
           System.out.println("george studied: "+george.totalcredit()+"credites");  
      }
}
