package lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class Disp{
	public static void writeDocument(Document document,String path) throws Exception{
		File file=new File(path+".tmp");
		FileOutputStream fos=new FileOutputStream(file);
		StreamResult result=new StreamResult(fos);
		
		// Transformer�t�@�N�g���𐶐�
		TransformerFactory transFactory=TransformerFactory.newInstance();
		
		// Transformer���擾
		Transformer transformer=transFactory.newTransformer();
		// �G���R�[�h�FUTF-8�A�C���f���g������w��
		transformer.setOutputProperty("encoding","UTF-8");
		transformer.setOutputProperty("indent","yes");
		// transformer�ɓn���\�[�X�𐶐�
		DOMSource source=new DOMSource(document);
		
		// �o�͎��s
		transformer.transform(source,result);
		fos.close();
		
		try{
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(path)));
			
			try{
				BufferedReader br=new BufferedReader(new FileReader(path+".tmp"));
				String s;
				
				while((s=br.readLine())!=null){
					//if(Pattern.compile("").matcher(s).find());
				}
				br.close();
			}
			catch(FileNotFoundException e){System.err.println("It can\'t be opened");}
			catch(IOException e){System.err.println("It can\'t be read");}
			
			pw.close();
		}catch(IOException e){e.printStackTrace();}
	}
}
