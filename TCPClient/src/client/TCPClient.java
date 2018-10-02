package client;

import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

class TCPClient 
{
	// Constantes que definen la direccion ip y el puerto
	final static String direccion = "localhost";
	final static int puerto = 9876;
	
	// Constantes de transferencia de archivos
	final static int fileSize = 20002;
	final static String clientRoute = "D:\\receivedFile.txt";

	public static void main(String argv[]) throws Exception 
	{
		double time = 0;
		
		// Manejo de Socket
		System.out.println("Creating Socket..");
		Socket clientSocket = new Socket(direccion, puerto);
		System.out.println("Socket with direction: " +direccion+ " and port: "+puerto);
		
		// Transferencia de Archivos
		System.out.println("Start Receiving File");
		StartTime timer = new StartTime();
		System.out.println("Client: Destination route " + clientRoute);
		
		byte b[]= new byte[fileSize];
		System.out.println("Client: Byte Array with Buffer Size: "+fileSize);
		int count;
		System.out.println("Start generating Client Hash using SHA-256");
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		
		InputStream is = clientSocket.getInputStream();
		BufferedInputStream bis = new BufferedInputStream(is);
		System.out.println("Llega aca?");
		/*while ((count = bis.read(b))>0) // While Añadido
		{
			System.out.println(count);
			digest.update(b,0,count);
			System.out.println("0");
		}
		System.out.println("1");*/
		bis.close();
		System.out.println("2");
		byte[] hash = digest.digest();
		System.out.println("Client Hash: " + new BASE64Encoder().encode(hash));
		
		FileOutputStream fos = new FileOutputStream(clientRoute);
		//is.read(b, 0, b.length);
		fos.write(b, 0, b.length);
		
		time=timer.getTimeElapsed(); //ms
		System.out.println("File Received.");
		
		// Cierre de las conexiones
		System.out.println("Client Socket Closing..");
		fos.close();
		clientSocket.close();
		System.out.println("Client Socket Closed.");
	}
}