package tcpclient;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class TCPClient {

    private static int SO_TIMEOUT = 5000;   //timeout in miliseconds

    public static String askServer(String hostname, int port, String ToServer)
    throws IOException {


      //Create a new Socket and set a timeout
      Socket clientSocket = new Socket(hostname,port);
      clientSocket.setSoTimeout(SO_TIMEOUT);
      InputStream stream = clientSocket.getInputStream();

      // If clause is used if ToServer is beeing used as an argument
      if(ToServer != null){

        //Convert string ToServer into a byte array
        byte [] encoded = ToServer.getBytes(StandardCharsets.UTF_8);

        //Write encoded ToServer string to Outputstream
        clientSocket.getOutputStream().write(encoded);
      }

      //Used to store result
      StringBuilder sb = new StringBuilder();

      try{

        //read a byte from stream, int representation received
        int ch = stream.read();

        //stores UTF_8 symbol that can be up to 2 bytes per symbol
        byte[] array = new byte[4];

        //while loop reads until server returns -1 -> no more data
        while(ch != -1){

          //checks if byte is extended ASCII (this clause handles UTF_8 for 2 bytes)
          if(ch > 127){

            int counter = 1;

            //for loop stores UTF_8 symbol in byte array
            for(int j = 0; j < 4; j++){
              array[j] = (byte)ch;
              ch = stream.read();

              //break if
              if(ch < 128 | j == 3 ){
                break;}

              counter++;
            }

            //a new byte array in size of counter
            byte [] newarray = new byte[counter];

            //copy to new byte array to avoid possible "null" in old byte array
            for(int j = 0; j < counter; j++){
              newarray[j] = array[j];}

            //convert new byte array with StandardCharsets.UTF_8
            //and append to stringbuilder
            String string = new String(newarray, StandardCharsets.UTF_8);
            sb.append(string);
          }
          else{
            sb.append((char)ch);
            ch = stream.read();
          }
        }

      }
      catch(java.net.SocketTimeoutException e){
          System.out.println("Timeout reached!");
        }

      //Close socket
      clientSocket.close();

      //convert stringbuilder to string
      String result = sb.toString();

      return result;

    }


    public static String askServer(String hostname, int port)
    throws IOException {

      //Calls function above with a "null" string to bypass
      //the If clause
      String string = null;
      return askServer(hostname, port, string);

    }
}
