import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
public class ServerExitListener extends Thread {
	
	public ServerExitListener(String name) {
		super(name);
	}
	
	public void run(){
		boolean keepRunning = true;
		String cmd = "";
		BufferedReader input = null;
		
		input = new BufferedReader(new InputStreamReader(System.in));
		try {
			cmd = input.readLine();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if(cmd.equals("exit")) {
			System.out.println("Server shutting down");
			System.exit(0);
		}
//        BufferedReader reader =
//                new BufferedReader(new InputStreamReader(System.in));
//        String name = "";
//		try {
//			name = reader.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        System.out.println(name);       
//		
//	}

	}
}
