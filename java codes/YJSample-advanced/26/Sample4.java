import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class Sample4
{
   public static void main(String[] args) throws Exception
   {
      // 進行DOM的準備
      DocumentBuilderFactory dbf
         = DocumentBuilderFactory.newInstance();
      DocumentBuilder db
         = dbf.newDocumentBuilder();

      // 建立新文件
      Document doc = db.newDocument();

      // 建立根元素
      Element root = doc.createElement("車子列表");
      doc.appendChild(root);

      // 準備CSV文件
      BufferedReader br = new BufferedReader(new FileReader("Sample.csv"));

      // 儲存CSV文件的標題列
      ArrayList<String> colname = new ArrayList<String>();
      String line = br.readLine();
      StringTokenizer stt = new StringTokenizer(line, ",");
      while(stt.hasMoreTokens()){
         colname.add(stt.nextToken());
      }

      // 轉換CSV文件
      while((line = br.readLine()) != null){
         StringTokenizer std = new StringTokenizer(line, ",");
         Element car = doc.createElement("車子");
         root.appendChild(car);

         for(int i=0; i<colname.size(); i++){
            Element elm = doc.createElement((String)colname.get(i));
            Text txt = doc.createTextNode(std.nextToken());
            elm.appendChild(txt);
            car.appendChild(elm);
         }

      }
      br.close();

      // 輸出文件
      TransformerFactory tff
         = TransformerFactory.newInstance();
      Transformer tf
         = tff.newTransformer();
      tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      tf.transform(new DOMSource(doc), new StreamResult("result.xml"));
      System.out.println("輸出到result.xml了。");
   }
}