import java.io.IOException;

public class ClientApplication {

	public static void main(String[] args) {
		try {
			Client.process(args);
		} catch (IOException e) {
			e.printStackTrace();
		};
	}

}
