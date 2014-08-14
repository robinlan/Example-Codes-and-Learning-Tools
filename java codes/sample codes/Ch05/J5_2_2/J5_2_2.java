public class J5_2_2 {
  static void Sort(String data[]){
    int i,j;
    String t;
    for(i=data.length-2;i>=0;i--)
      for(j=0;j<=i;j++)
        if(data[j].compareTo(data[j+1])>0){
          t=data[j];
          data[j]=data[j+1];
          data[j+1]=t;
        }
  }  
        
  public static void main (String[] args) {
    String word[] = {"book","pen","desk","boy","girl"};
    int i;
    System.out.println("排序前 : ");
    for(i=0;i<=word.length-2;i++)
      System.out.print(word[i] + " , ");
    System.out.println(word[word.length-1]);               
    Sort(word);
    System.out.println("排序後 : ");
    for(i=0;i<=word.length-2;i++)
      System.out.print(word[i] + " , ");
    System.out.println(word[word.length-1]); 
  }
}

