import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class SampleP2
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

      // 新增根元素
      Element root = doc.createElement("水果列表");
      doc.appendChild(root);

      // 新增元素
      Element fruit = doc.createElement("水果");
      root.appendChild(fruit);

      Element elm1 = doc.createElement("名稱");
      Text txt1 = doc.createTextNode("橘子");
      elm1.appendChild(txt1);
      fruit.appendChild(elm1);

      Element elm2 = doc.createElement("進貨店家");
      Text txt2 = doc.createTextNode("青山商店");
      elm2.appendChild(txt2);
      fruit.appendChild(elm2);

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