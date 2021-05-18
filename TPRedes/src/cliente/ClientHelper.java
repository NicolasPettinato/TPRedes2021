import java.util.Scanner;

public class ClientHelper {
    public static String clientAddress;
    public static Integer clientPort;

    public static String getClientAddress() {
        System.out.println("Insert IP address:");
        Scanner scanner = new Scanner( System. in);
        clientAddress = scanner. nextLine();
        if (!clientAddress.equals("127.0.0.1")) {
            System.out.println("Address not valid");
        }
        while (!clientAddress.equals("127.0.0.1")) {
            System.out.println("Insert IP address:");
            clientAddress = scanner. nextLine();
            if (!clientAddress.equals("127.0.0.1")) {
                System.out.println("Address not valid");
            }
        }
        return clientAddress;
    }
    public static Integer getClientPort() {
        Scanner scanner = new Scanner( System. in);
        System.out.println("Insert Port:")   ;
        // TODO: 6/20/19 fix string input
        if ( scanner.hasNextInt()) {
            clientPort = scanner.nextInt();
            if (clientPort == null) {
                System.out.println("Port not valid");
            }
        }else {
            System.out.println("Port not valid");
            getClientPort();
        }

        return clientPort;
    }

}
