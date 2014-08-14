import java.io.*;
public class J4_2_1 {
  public static void main (String[] args) throws IOException {
    BufferedReader keyin = new BufferedReader(
    	                   new InputStreamReader(System.in));
    int[] data;
    int len, i;
    System.out.print("¿é¤J°}¦C¤j¤p : ");
    len = Integer.parseInt(keyin.readLine());
    data = new int[len];
    for(i = 0; i < len; i++){
      data[i] = (i + 10) * (i + 10);
      if(i == len - 1)
        System.out.print(data[i]);
      else
        System.out.print(data[i] + ", ");
    }
  }
}