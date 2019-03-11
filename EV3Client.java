import java.io.*;
import java.net.*;
 
public class EV3Client
{
	ObjectInputStream oIn;
	
    public EV3Client() {
    	
        try
		{
        	System.out.println("waiting for connection");
            String ip = "10.0.1.1"; // BT
            Socket sock = new Socket(ip, EV3Server.port);
            System.out.println("Connected");
            InputStream in = sock.getInputStream();
			oIn = new ObjectInputStream(in);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
   
    public Maze getMaze() throws ClassNotFoundException, IOException {
    	Maze obj = (Maze) oIn.readObject();
        return obj;
    }
}