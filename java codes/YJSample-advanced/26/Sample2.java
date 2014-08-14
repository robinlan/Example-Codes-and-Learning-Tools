import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class Sample2
{
   public static void main(String[] args) throws Exception
   {
      // 進行DOM的準備
      DocumentBuilderFactory dbf
         = DocumentBuilderFactory.newInstance();
      DocumentBuilder db
         = dbf.newDocumentBuilder();

      // 讀入文件
      Document doc
         = db.parse(new FileInputStream("Sample.xml"));

      // 建立新文件
      Document doc2 = db.newDocument();

      // 新增根元素
      Element root = doc2.createElement("cars");
      doc2.appendChild(root);

      // 取出元素
      NodeList lst = doc.getElementsByTagName("name");

      for(int i=0; i<lst.getLength(); i++){
         Node n = lst.item(i);
         for(Node ch = n.getFirstChild();
                  ch != null;
                  ch = ch.getNextSibling()){

            Element elm = doc2.createElement("name");
            Text txt = doc2.createTextNode(ch.getNodeValue());
            elm.appendChild(txt);
            root.appendChild(elm);
         }
      }

      // 輸出文件
      TransformerFactory tff
         = TransformerFactory.newInstance();
      Transformer tf
         = tff.newTransformer();
      tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      tf.transform(new DOMSource(doc2), new StreamResult("result.xml"));
      System.out.println("輸出到result.xml了。");
   }
}