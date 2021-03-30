import java.net.*;
import java.io.*;
import tcpclient.TCPClient;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MyRunnable implements Runnable{

    private static int SIZE = 1024;
    Socket connectionSocket;

    public MyRunnable(Socket connectionSocket){
      this.connectionSocket = connectionSocket;
    }

    public void run(){

      try{

          String r200 = "HTTP/1.1 200 OK\r\n\r\n";
          String r400 = "HTTP/1.1 400 Bad Request\r\n\r\n" + "ERROR 400 Bad Request\n";
          String r404 = "HTTP/1.1 404 Not Found\r\n\r\n" + "ERROR 404 Not Found\n";
          String [] array;
          byte [] resp;
          byte[] buffer = new byte[SIZE];

          //check number of bytes sent to buffer
          int sizeOfBuffer = connectionSocket.getInputStream().read(buffer);

          //if more in buffer, copy to new array,resize, and keep reading
          if(sizeOfBuffer == SIZE){

            int size = 1;
            int pos = 0;
            int pos2;
            int j = 0;
            int ceiling = 0;
            byte [] tempArray1 = new byte[(4*size)*SIZE];
            byte [] tempArray2;


            while(true){

              j = 0;
              while(j<buffer.length){
                tempArray1[pos] = buffer[j];
                pos++;
                j++;
              }


              ceiling = 3*SIZE;
              buffer = new byte[ceiling];
              sizeOfBuffer = connectionSocket.getInputStream().read(buffer);
              size++;

              tempArray2 = tempArray1;

              if(sizeOfBuffer<ceiling){

                j = 0;
                while(j<buffer.length){
                  tempArray2[pos] = buffer[j];
                  pos++;
                  j++;
                }

                buffer = tempArray2;
                break;
              }

              pos2 = pos;
              tempArray1 = new byte[(4*size)*SIZE];

              j = 0;
              while(j<pos2){
                tempArray1[j] = tempArray2[j];
                j++;
              }
            }
          }

          //Conver buffarray to a string and split it.
          String substring = new String(buffer, StandardCharsets.UTF_8);
          String[] result = substring.split("\r\n");

          //array[0] cotains first header line
          array = result[0].split("[ =&?]");
          int arrayLength = array.length;

          String fromTcpClient = null;
          String hostname = null;
          int portNumb = -1;
          String argument = null;
          String http = null;


          //while loop used to check if there is a hostnamne, port number,
          //string argument and correct http version.
          int x = 0;

          while(x < arrayLength){

            if(array[x].equals("hostname")){
              if( (x+1)<arrayLength && !(array[x+1].equals("port")) ){
                hostname = array[x+1];
              }
            }

            if(array[x].equals("port")){
              if( (x+1)<arrayLength && !(array[x+1].equals("string")) ){
                try{
                  portNumb = Integer.parseInt(array[x+1]);}
                catch(NumberFormatException e){
                  System.err.println("Error: string 'portNumb' cannot be parsed to an Int");
                }
              }
            }

            if(array[x].equals("string")){
                if( (x+1)<arrayLength && !(array[x+1].equals("HTTP/1.1")) ){
                  argument = array[x+1] + "\n";
                }
            }

            if(array[x].equals("HTTP/1.1")){
              http = array[x];
            }
            x++;
          }


          //Clauses check tre conditions and give a correspodning respond
          if(array[1].equals("/favicon.ico")){
            resp = r404.getBytes(StandardCharsets.UTF_8);
            connectionSocket.getOutputStream().write(resp);
          }
          else if(array[0].equals("GET") && array[1].matches("/ask")
          && hostname != null && http!=null && portNumb!=-1){

            try{

                if(argument != null){
                  fromTcpClient = TCPClient.askServer(hostname,portNumb,argument);
                }
                else{
                  fromTcpClient = TCPClient.askServer(hostname,portNumb);
                }


                resp = r200.getBytes(StandardCharsets.UTF_8);
                connectionSocket.getOutputStream().write(resp);

                byte [] response = fromTcpClient.getBytes(StandardCharsets.UTF_8);
                connectionSocket.getOutputStream().write(response);

            }
            catch (Exception ex) {
                System.err.println("Exception catched: " + ex);
                resp = r404.getBytes(StandardCharsets.UTF_8);
                connectionSocket.getOutputStream().write(resp);
            }
          }
          else{
            resp = r400.getBytes(StandardCharsets.UTF_8);
            connectionSocket.getOutputStream().write(resp);
          }

          connectionSocket.close();
      }
      catch(IOException ex){
        System.err.println("Exception catched: " + ex);
      }
    }
}
