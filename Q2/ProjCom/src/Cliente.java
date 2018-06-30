import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	public static void Client_ON_Put() throws UnknownHostException, IOException {

		Scanner read = new Scanner(System.in);
				
		Socket socket = new Socket("localhost", 8082);
		
		String dir = read.next();
		String filename = read.next();
		String usrdir = "imp2";
		File file = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/"+usrdir+"/"+filename);
		
		InputStream inputstream = new FileInputStream(file);
		OutputStream outputstream = socket.getOutputStream();
		
		DataOutputStream d = new DataOutputStream(outputstream);
		
        d.writeUTF(dir);
        d.writeUTF(filename);
		
		int len;
		byte[] buffer = new byte[1024];
		while((len = inputstream.read(buffer)) > 0){
			outputstream.write(buffer, 0, len);
		}
		
		inputstream.close();
		socket.close();
		read.close();
	}
	public static void Client_ON_Get() throws UnknownHostException, IOException {

		Scanner read = new Scanner(System.in);
				
		Socket socket = new Socket("localhost", 8082);
		int len; 
		byte[] buffer = new byte[1024];

		String usrdir = "imp2";
		String destdir = read.next();
		String filename = read.next();
		File file = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + usrdir + "/" + filename);
		file.createNewFile();
		
		OutputStream outstream = new FileOutputStream(file);
		InputStream intputStream = socket.getInputStream();
		OutputStream outputstream = socket.getOutputStream();
		
		DataOutputStream d = new DataOutputStream(outputstream);
		
        d.writeUTF(destdir);
        d.writeUTF(filename);

		while ((len = intputStream.read(buffer)) > 0) {
			outstream.write(buffer, 0, len);
		}
		
		outputstream.close();
		outstream.close();
		socket.close();
		read.close();
	}
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner read = new Scanner(System.in);
		int a = read.nextInt();
		if(a==1) {
			Client_ON_Get();
		}
		else {
			Client_ON_Put();
		}
		read.close();
	}
}
