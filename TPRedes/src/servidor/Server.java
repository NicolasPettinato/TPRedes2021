// Chat Server runs at port no. 9020
import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;

public class Server
{
    ArrayList<String> users = new ArrayList<>();                        //Array de names
    ArrayList<HandleClient> clients = new ArrayList<HandleClient>();    //Array de Threads client

    int PORT = 3000;
    int NumClients = 10;

    public void process() throws Exception
    {
        ServerSocket server = new ServerSocket(PORT,NumClients);
        out.println("Server Connected...");
        // waitConnections escucha constantemente nuevos clientes
        WaitConnections wc = new WaitConnections(server);
        // ServerChatter habilita mensajes desde el servidor un cliente especifico o a todos.
        new ServerChatter().start();
    }

    // Hilo que sigue esperando conexiones
    class WaitConnections extends Thread    //----------- CLASE ANIDADA ------------
    {
        ServerSocket server;

        public WaitConnections(ServerSocket server){
            this.server = server;
            start();
        }

        @Override
        public void run(){
            while( true)
            {
                Socket client = null;
                try {
                    client = this.server.accept();  //
                    out.println("server > [ Cliente nuevo ]");
                    HandleClient c = null;
                    c = new HandleClient(client);
                    clients.add(c);
                } catch (IOException e) {
                    out.println("Conection closed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Cada cliente que entra es un hilo
    class HandleClient extends Thread       //----------- CLASE ANIDADA ------------
    {
        String name = "";
        BufferedReader input;
        PrintWriter output;

        public HandleClient(Socket client) throws Exception
        {
            input = new BufferedReader(new InputStreamReader(client.getInputStream())) ;
            output = new PrintWriter (client.getOutputStream(),true);
            output.println("Welcome to UTN's Chat Server! : Please Enter a User Name ");
            name  = input.readLine();

            out.println("** NOMBRE: " + name);
            users.add(name);
            output.println("Welcome "+ name +" we hope you enjoy your chat today");
            start();
        }

        public void sendMessage(String userName,String  msg)
        {
            output.println( "["+userName + "] :" + msg);
        }

        public String getUserName()
        {
            return name;
        }

        public void run()
        {
            String line;
            out.println("-- WAITING");
            try
            {
                while(true)
                {
                    line = input.readLine();
                    if (line.equals(null)) {
                        out.println();
                        break;
                    }
                    out.println("server > [ "+name+" ]: " +line);
                    if("x".equals(line))
                    {
                        clients.remove(this);
                        users.remove(name);
                        System.out.println(name+" Disconected");
                        break;
                    }
                    else if(name.equals(line))
                    {
                        output.println("OK");
                    }
                    broadcast(name,line);
                }
            }
            catch(Exception e)
            {
                clients.remove(this);
                users.remove(name);
                System.out.println(name+" Disconected");
            }
        }
    }

    // ServerChatter habilita mensajes desde el servidor un cliente especifico o a todos.
    class ServerChatter extends Thread      //----------- CLASE ANIDADA ------------
    {
        String message ="";
        String user ="";
        Scanner scanner = new Scanner(System.in);

        @Override
        public void run() {

            // escribir "sm" para abrir consola de mensajes
            while(true) {
                message = scanner.nextLine();
                if(message.equals("us")) {
                    System.out.println(" [ USERS ]: " + users);
                }
                if(message.equals("sm")) {
                    createMessage();
                }
                if (message.equals("x")) {
                    out.println("Server Disconected");
                    System.exit(0);
                }
                message="";

            }
        }

        private void createMessage(){
            System.out.printf(" [ DESTINATION ] (Hint: write * to send to every user) *> ");
            user = scanner.nextLine();
            if (users.contains(user) || user.equals("*")) {
                System.out.printf(" [ MESSAGE ] *> ");
                message = scanner.nextLine();
                serverBroadcast(user, message);}
            else{
                out.println("[USER NOT FOUND]");
            }
        }
    }

    //---------------------------------------------------------------------------------------

    public void serverBroadcast(String user, String message)
    {
        if(user.equals("*")){
            //manda a todos
            broadcast("SERVER", message);
        } else {
            for (HandleClient c : clients) {
                if (c.getUserName().equals(user)) {
                    c.sendMessage(" PRIVATE FROM SERVER ",message);
                }
            }
            out.println("[MESSAGE SENT]");
        }
    }

    public void broadcast(String user, String message)
    {
        for (HandleClient c : clients)
            c.sendMessage(user,message);
    }

}