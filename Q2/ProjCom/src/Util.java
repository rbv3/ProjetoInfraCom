
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;


public class Util {

	public static void createFolder(String folderName){
		File folder = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + folderName);
		folder.mkdirs();
	}
	
	public static void deleteFile(String folderName, String fileName){
		File file = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + folderName +"/" + fileName);
		file.delete();
	}

	public static int listFiles(String folderName){
		File folder = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/" + folderName);
		File[] files = folder.listFiles();
		
		for (File f : files){
			System.out.println(f.getName());
		}
		return files.length;
	}
	
	public static void ListEveryThing() {
		File folder = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/");
		File[] allFolders = folder.listFiles();
		
		for (File f : allFolders) {
			System.out.println("Files in " + f.getName() + ":");
			File[] localFiles = f.listFiles();
			for (File g : localFiles) {
				System.out.println(g.getName());				
			}
		}
		
	}
	
	public static void listFolders(){
		File folder = new File("/home/rbv3/eclipse-workspace/ProjCom/usr");
		File[] files = folder.listFiles();

		for (File f : files){
			System.out.println(f.getName());
		}
	}
	
	public static boolean userCheck(String username) {
		boolean a = false; //flag
		ArrayDeque<String> users = new ArrayDeque<String>();
		File folder = new File("/home/rbv3/eclipse-workspace/ProjCom/usr/");
		File[] allUsers = folder.listFiles();
		for (File f : allUsers){
			users.add(f.getName());
		}
		/* checking content
		Iterator<String> itr= users.iterator();
		while(itr.hasNext()){
			System.out.println(itr.next());
		}*/
		a = users.contains(username);
		//System.out.println(a);
		
		return a;
		
	}
	
	public static void Client_ON_Put(String usrdir, String dir, String filename) throws UnknownHostException, IOException {
				
		Socket socket = new Socket("localhost", 8082);
		
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
	}

	public static void Client_ON_Get(String usrdir, String destdir, String filename) throws UnknownHostException, IOException {
				
		Socket socket = new Socket("localhost", 8082);
		int len; 
		byte[] buffer = new byte[1024];

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
	}
	
	public static void main(String[] args)throws Exception {		
		Scanner read = new Scanner(System.in);
		
		//file with users n passwords
		File ussPr = new File("/home/rbv3/eclipse-workspace/ProjCom/usrPss");		 
		BufferedReader br = new BufferedReader(new FileReader(ussPr));
		
		//structure to be used in verifying
		Map<String, String> userPass = new HashMap<String, String>();
		
		String st;
		while ((st = br.readLine()) != null) {
			String[] parts = st.split("\\:"); // String array, each element is text between :
			String beforeFirstDot = parts[0];
			String afterFirstDot = parts[1];
			userPass.put(beforeFirstDot, afterFirstDot);  
		}	
		
		
		System.out.print("User: ");
		String username = read.next();
		if(!userCheck(username)) {
			createFolder(username);//create folder if user is new
			
			System.out.println("Hello, " + username);
			System.out.print("Choose a password: ");
			String password = read.next();
			userPass.put(username, password);
			BufferedWriter writer = new BufferedWriter(new FileWriter(ussPr, true));
			writer.append("\n");
			String st1 ="";
			st1 += username+":"+password;
			System.out.println(st1);
			writer.append(st1);
			writer.close();   
			
			
		}
		else {
			boolean flag = false;
			while(!flag) {
				System.out.print("Pass: ");
				String password = read.next();
				
				if(password.equals(userPass.get(username))) {
					System.out.println("Success");
					flag = true;
				}
				else {
					System.out.println("Wrong Password, please retry");
					flag = false;
				}
			}
		}
		
		int entry;

		System.out.println("Choose 1 to delete a file");
		System.out.println("Choose 2 to list all files and folders");
		System.out.println("Choose 3 PUT, choose directory and filename to copy from your folder");
		System.out.println("Choose 4 GET, choose directory and filename to copy to your folder");
		System.out.println("Choose 0 to exit");
		
		while((entry = Integer.parseInt(read.next())) != 0){ //menu			
			if (entry == 1) {
				int size = listFiles(username);
				if(size > 0) {
					System.out.println("Choose a file to delete, write an invalid name to delete nothing");
					deleteFile(username, read.next());
				}
				else {
					System.out.println("There is no file in here");
				}
			}
			else if (entry == 2) {
				ListEveryThing();
			}
			else if (entry ==3) {
				Socket socket = new Socket("localhost", 8082);
				OutputStream outputstream = socket.getOutputStream();
				
				DataOutputStream d = new DataOutputStream(outputstream);
				
		        d.writeUTF("2");
				String dir = read.next();
				String filename = read.next();
				outputstream.close();
				socket.close();
				Client_ON_Put(username, dir, filename);
			}
			else if (entry == 4) {
				Socket socket = new Socket("localhost", 8082);
				OutputStream outputstream = socket.getOutputStream();
				
				DataOutputStream d = new DataOutputStream(outputstream);
				
		        d.writeUTF("1");
				outputstream.close();
				socket.close();
				String destdir = read.next();
				String filename = read.next();
				Client_ON_Get(username, destdir, filename);
			}
			/*if (entry == 5){
				(new Thread(new MockThread())).start();
				(new Thread(new MockThread())).start();
				(new Thread(new MockThread())).start();
			}*/
		}
		Socket socket = new Socket("localhost", 8082);
		OutputStream outputstream = socket.getOutputStream();
		
		DataOutputStream d = new DataOutputStream(outputstream);
		
        d.writeUTF("0");
		outputstream.close();
		socket.close();
		
		read.close();
		br.close();

	}
}
