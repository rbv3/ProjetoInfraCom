
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
	public static void Server_ON_Put(ServerSocket server) throws IOException {
		Socket socket = server.accept();
		int len; 
		byte[] buffer = new byte[1024];

		InputStream intputStream = socket.getInputStream();
		DataInputStream d = new DataInputStream(intputStream);
		
		String dir = d.readUTF();
		String filename = d.readUTF();
		System.out.println(dir + " " + filename);
		

		File file = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + dir + "/" + filename);

		file.createNewFile();
		
		OutputStream outstream = new FileOutputStream(file);
		while ((len = intputStream.read(buffer)) > 0) {
			outstream.write(buffer, 0, len);
		}
		
		outstream.close();
		socket.close();
	}
	public static void Server_ON_Get(ServerSocket server) throws IOException {
		Socket socket = server.accept();
		
		OutputStream outputstream = socket.getOutputStream();
		InputStream intputStream = socket.getInputStream();
		DataInputStream d = new DataInputStream(intputStream);

		String dir = d.readUTF();
		String filename = d.readUTF();

		File file = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + dir + "/" + filename);

		InputStream inputstream = new FileInputStream(file);
		
		int len;
		byte[] buffer = new byte[1024];
		while((len = inputstream.read(buffer)) > 0){
			outputstream.write(buffer, 0, len);
		}
		
		inputstream.close();
		socket.close();
	}
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(8082);
		Scanner read = new Scanner(System.in);
		int entry;

		while(true) {
			// omp parallel 
			{
				Socket socket = server.accept();
				InputStream intputStream = socket.getInputStream();
				DataInputStream d = new DataInputStream(intputStream);
				
				String num = d.readUTF();
				entry = Integer.parseInt(num);
				socket.close();
				System.out.println(entry);
				if(entry==1) {
					Server_ON_Get(server);
				}
				else if(entry==2) {
					Server_ON_Put(server);
				}
				else{
					break;
				}
			}
		}
		read.close();
		server.close();
	}
}
