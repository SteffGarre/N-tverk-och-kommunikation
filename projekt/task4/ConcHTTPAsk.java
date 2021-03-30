import java.net.*;
import java.io.*;

public class ConcHTTPAsk {


  public static void main( String[] args) throws IOException {

    int port = Integer.parseInt(args[0]);
    ServerSocket serversocket = new ServerSocket(port);

    try{
      while(true){

        Socket connectionSocket = serversocket.accept();
        MyRunnable object = new MyRunnable(connectionSocket);
        new Thread(object).start();
      }
    }
    catch(IOException ex){
      System.err.println("Exception catched: " + ex);
    }
  }
}
