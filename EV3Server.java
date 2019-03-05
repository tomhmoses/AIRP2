import java.io.*;
import java.net.*;
import lejos.hardware.lcd.LCD;
 
public class EV3Server
{
 
    public static final int port = 1234;
    private static ObjectOutputStream oOut;
   
    public EV3Server(Object obj) throws IOException
    {
        ServerSocket server = new ServerSocket(port);
        LCD.drawString("Awaiting client..", 0, 5);
        Socket client = server.accept();
        LCD.clear();
        LCD.drawString("CONNECTED", 0, 5);
        OutputStream out = client.getOutputStream();
        oOut = new ObjectOutputStream(out);
        oOut.writeObject(obj);
        //dOuflush(); TODO: what was this dOutflush thing?
        server.close();
    }
}