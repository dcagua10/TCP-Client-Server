package server;
import java.io.*;
import java.net.*;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

class TCPServer 
{

	// CONSTANTES
	// Puerto
	final static int port = 9876;

	// Manejo de clientes
	final static int numclients=1;

	// Datos relacionados a los archivos
	final static String fileName = "sendFile.txt";
	final static String route = "D:\\"+fileName;
	final static int fileSize = 20002;

	// Constructor
	public static void main(String argv[]) throws Exception 
	{
		// Variables para el log
		String state="No";
		double time = 0;
		int id = 0;
		int totalp = 0;
		int numpack = 0;
		int bs = 0;
		int br = 0;
		int rp = 0;
		String head = "";

		ServerSocket welcomeSocket = new ServerSocket(port,25);
		System.out.println("Server Socket Created using port: " +port);
		
		while (true) 
		{
			// Manejo de Socket
			System.out.println("Waiting for conecctions..");
			Socket connectionSocket = welcomeSocket.accept();
			System.out.println("Socket Accepted");

			// Transferencia de Archivo
			System.out.println("Start Sending File..");
			StartTime timer = new StartTime();
			System.out.println("Sending File from: " + route);
			
			byte[]b = new byte [fileSize];
			System.out.println("Buffer Size: " +fileSize);
			int count;//
			System.out.println("Start generating Server Hash using SHA-256");
			MessageDigest digest = MessageDigest.getInstance("SHA-256");//
			
			FileInputStream fis = new FileInputStream(route);
			BufferedInputStream bis = new BufferedInputStream (fis);//
			while ((count = bis.read(b))>0) // While Añadido
			{
				digest.update(b,0,count);
			}
			bis.close();
			byte[] hash = digest.digest();
			System.out.println("Server Hash: " + new BASE64Encoder().encode(hash));
			
			//fis.read(b, 0, b.length);
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			outToClient.write(b, 0, b.length);
			
			time=timer.getTimeElapsed(); //ms
			System.out.println("File Sended.");

			// Generar un Log
			// Archivo por prueba, Fecha y Hora de la prueba, Nombre archivo enviado
			// cada cliente realiza la transf de archivos, entrega exitosa?, tiempo de transferencia
			// paquetes enviados, numero paquetes, valor total bytes transmitidos, recibidos, paquetes retransmitidos y los encabezados
			System.out.println("Generating log.txt");
			PrintWriter writer = new PrintWriter("log.txt");

			String fileInfo = "File Name:"+fileName+ ",¿Succesfull?:" + state + ",Time:" + time + " ms";
			String clientInfo = "Client id:" + id;
			String packageInfo = "Total packages:" + totalp + ",Packages:" + numpack + ",Bytes sended:" + bs + ",Bytes received:" + br + ",Resend Packages:" + rp + ",Headers:" + head;
			writer.println(fileInfo);
			writer.println(clientInfo);
			writer.println(packageInfo);
			writer.close();

		}
	}
}