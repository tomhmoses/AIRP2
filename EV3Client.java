import java.io.*;
import java.net.*;
 
public class EV3Client
{
    public EV3Client() {
        // does nothing when instantiated
    }
   
    public Maze getMaze() throws IOException, ClassNotFoundException {
        String ip = "10.0.1.1"; // BT
        Socket sock = new Socket(ip, EV3Server.port);
        System.out.println("Connected");
        InputStream in = sock.getInputStream();
        ObjectInputStream oIn = new ObjectInputStream(in);
        Maze obj = (Maze) oIn.readObject();
        sock.close();
        return obj;
    }
}