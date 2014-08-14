public class J6_7_1 {
    public static void main(String[] args)  {
        int min = 50, max = 100, rnd_no = 5; 
        int tot_no = max - min + 1;
        int[] data = new int[tot_no];
          
        BBKMath OData = new BBKMath(); 

        OData.GenRnd(data, min, max, rnd_no);

        for(int i = 0; i < rnd_no; i++){
            System.out.println(i + "  :  " + data[i] );
        }     
    }
}
