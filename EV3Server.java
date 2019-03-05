import java.io.*;
import java.net.*;
import lejos.hardware.Battery;
import lejos.hardware.lcd.LCD;

public class EV3Server
{

	public static final int port = 1234;
	private static DataOutputStream dOut;
	private static ObjectOutputStream oOut;
	
	public static void main(String[] args) throws IOException 
	{
		ServerSocket server = new ServerSocket(port);
		LCD.drawString("Awaiting client..", 0, 5);
		Socket client = server.accept();
		LCD.clear();
		LCD.drawString("CONNECTED", 0, 5);
		OutputStream out = client.getOutputStream();
		dOut = new DataOutputStream(out);
		// dOut.writeUTF("Battery: " + Battery.getVoltage());
		// dOut.flush();server.close();
		oOut = new ObjectOutputStream(out);
	}
	
	public void sendUTF(String string) throws IOException
	{
		dOut.writeUTF(string);
	}
	
	public void sendObj(Object obj) throws IOException
	{
		oOut.writeObject(obj);
	}
	
}
