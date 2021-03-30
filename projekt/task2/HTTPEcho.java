import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HTTPEcho {

  private static int SIZE = 1024;

  public static void main(String[] args) throws Exception {

    int port = Integer.parseInt(args[0]);
    ServerSocket serversocket = new ServerSocket(port);
    String header = "HTTP/1.1 200 OK\r\n\r\n";
    byte[] response = header.getBytes(StandardCharsets.UTF_8);
    byte[] buffer = new byte[SIZE];


    while(true){

      //wait for connection
      Socket connectionSocket = serversocket.accept();

      //Send HTTP header response
      connectionSocket.getOutputStream().write(response);

      while(true){

        //check number of bytes sent to buffer
        int sizeOfBuffer = connectionSocket.getInputStream().read(buffer);


        //if number of bytes is less than fixed size, copy to new array
        //and trim end to remove any null spaces.
        if(sizeOfBuffer < SIZE){
          byte [] array = new byte[sizeOfBuffer];
          int i = 0;

          while(i < sizeOfBuffer){
            array[i] = buffer[i];
            i++;
          }

          //write to OutpuStream, close connection and break while loop
          //so that we can wait for new connection
          connectionSocket.getOutputStream().write(array);
          connectionSocket.close();
          break;

        }
        else{

          //sizeOfBuffer is bigger than fixed size. Send whats in the
          //buffer array, reset array and read the rest.
          connectionSocket.getOutputStream().write(buffer);
          buffer = new byte[SIZE];
        }
      }
    }
  }
}
