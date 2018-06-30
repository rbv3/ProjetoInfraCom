
import java.io.*;

public class FileTest {
	public static void main(String[] args)throws Exception
	  {
	  File ussPr = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/usrPss");
	 
	  BufferedReader br = new BufferedReader(new FileReader(ussPr));
	  BufferedWriter writer = new BufferedWriter(new FileWriter(ussPr, true));
	  writer.append("\n");
	  writer.append("aaaa");
	     
	  writer.close();
	 
	  String st;
	  while ((st = br.readLine()) != null) {
		  String[] parts = st.split("\\:"); // String array, each element is text between dots
		  String beforeFirstDot = parts[0];
		  String afterFirstDot = parts[1];
		  System.out.println("usr: " + beforeFirstDot + " pass: " + afterFirstDot);
	  
	  }
	 }
}
