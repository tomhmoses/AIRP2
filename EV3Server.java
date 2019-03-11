import java.io.*;
import java.net.*;
import lejos.hardware.lcd.LCD;
 
public class EV3Server
{
 
    public static final int port = 1234;
    private static ObjectOutputStream oOut;
   
    public EV3Server()
    {
        ServerSocket server;
		try
		{
			server = new ServerSocket(port);
	        LCD.drawString("Awaiting client..", 0, 5);
	        Socket client = server.accept();
	        LCD.clear();
	        LCD.drawString("CONNECTED", 0, 5);
	        OutputStream out = client.getOutputStream();
	        oOut = new ObjectOutputStream(out);
	        // TODO: what was this dOutflush thing?
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void send(Object obj) throws IOException
    {
        oOut.writeObject(obj);
        oOut.flush();
    }
}