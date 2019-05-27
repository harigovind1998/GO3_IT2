import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class IntermediateHost {
	
	DatagramSocket clientRecieveSocket, clientSendSocket, sendRecieveSocket;
	DatagramPacket clientRecievePacket, clientSendPacket, serverSendPacket, serverRecievePacket;
	ComFunctions com;
	int port = 69;
	int mode;
	private int interHostPort = 23;
	
	private static int packetNumber;
	private static int packetDelay;
	private static int simulation;
	private static int dup;
	/**
	 * Waits to recieve a message from the client and passes that on to the server
	 */
	public void recieveMessage(){
		while(true) {
			switch (simulation) {
				case 0: 
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(clientRecieveSocket, 516);
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", clientRecievePacket));
					}
					
					serverSendPacket = com.createPacket(clientRecievePacket.getData(), port);
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientRecievePacket));
					}
					
					com.sendPacket(serverSendPacket, sendRecieveSocket);
					
					//Listens to the server response, and forwards that on to the client in the reverse manner, printing each each of the messages
					serverRecievePacket = com.recievePacket(sendRecieveSocket,516);
					
					port = serverRecievePacket.getPort();
					
		
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", serverRecievePacket));
					}
					
					clientSendPacket = com.createPacket(serverRecievePacket.getData(), clientRecievePacket.getPort());
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientSendPacket));
					}
					com.sendPacket(clientSendPacket, clientSendSocket);
				case 1:
					System.out.println("Simulating Lost Packet...");
					
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(clientRecieveSocket, 516);
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", clientRecievePacket));
					}
					
					byte[] dataArr = clientRecievePacket.getData();
					
					if (dataArr[1] == 3 || dataArr[1] == 4) {
						byte[] blockNum = com.intToByte(packetNumber);
						if(dataArr[2] == blockNum[0] && dataArr[3] == blockNum[1]) {
							//do nothing
						}
						
					} else {
						serverSendPacket = com.createPacket(clientRecievePacket.getData(), port);
					}
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientRecievePacket));
					}
					
					com.sendPacket(serverSendPacket, sendRecieveSocket);
					
					//Listens to the server response, and forwards that on to the client in the reverse manner, printing each each of the messages
					serverRecievePacket = com.recievePacket(sendRecieveSocket,516);
					
					port = serverRecievePacket.getPort();
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", serverRecievePacket));
					}
					
					clientSendPacket = com.createPacket(serverRecievePacket.getData(), clientRecievePacket.getPort());
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientSendPacket));
					}
					com.sendPacket(clientSendPacket, clientSendSocket);
				case 2:
					System.out.println("Simulating Delayed Packet...");
					
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(clientRecieveSocket, 516);
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", clientRecievePacket));
					}
					
					byte[] dataArr2 = clientRecievePacket.getData();
					
					serverSendPacket = com.createPacket(clientRecievePacket.getData(), port);
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientRecievePacket));
					}
					
					if (dataArr2[1] == 3 || dataArr2[1] == 4) {
						byte[] blockNum = com.intToByte(packetNumber);
						if(dataArr2[2] == blockNum[0] && dataArr2[3] == blockNum[1]) {
							if(packetDelay == 0 ) {
								com.sendPacket(serverSendPacket, sendRecieveSocket);
							}
						}
						
					} else {
						com.sendPacket(serverSendPacket, sendRecieveSocket);
					}
					
					com.sendPacket(serverSendPacket, sendRecieveSocket);
					
					//Listens to the server response, and forwards that on to the client in the reverse manner, printing each each of the messages
					serverRecievePacket = com.recievePacket(sendRecieveSocket,516);
					
					port = serverRecievePacket.getPort();
					
		
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", serverRecievePacket));
					}
					
					clientSendPacket = com.createPacket(serverRecievePacket.getData(), clientRecievePacket.getPort());
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientSendPacket));
					}
					com.sendPacket(clientSendPacket, clientSendSocket);
					packetDelay--;
				case 3:
					System.out.println("Simulating Duplicate Packet...");
					
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(clientRecieveSocket, 516);
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", clientRecievePacket));
					}
					
					byte[] dataArr3 = clientRecievePacket.getData();
					
					if (dataArr3[1] == 3 || dataArr3[1] == 4) {
						byte[] blockNum = com.intToByte(packetNumber);
						if(dataArr3[2] == blockNum[0] && dataArr3[3] == blockNum[1]) {
							for(int i = 0; i < dup; i++) {
								serverSendPacket = com.createPacket(clientRecievePacket.getData(), port);
							}
						}
						
					} else {
						serverSendPacket = com.createPacket(clientRecievePacket.getData(), port);
					}
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientRecievePacket));
					}
					
					com.sendPacket(serverSendPacket, sendRecieveSocket);
					
					//Listens to the server response, and forwards that on to the client in the reverse manner, printing each each of the messages
					serverRecievePacket = com.recievePacket(sendRecieveSocket,516);
					
					port = serverRecievePacket.getPort();
					
		
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", serverRecievePacket));
					}
					
					clientSendPacket = com.createPacket(serverRecievePacket.getData(), clientRecievePacket.getPort());
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientSendPacket));
					}
					com.sendPacket(clientSendPacket, clientSendSocket);
			}
		}
	}
	
	
	public IntermediateHost() {
		// TODO Auto-generated constructor stub
		Scanner sc1 = new Scanner(System.in);
		System.out.println("Select Mode : Quiet [0], Verbose [1]");
		mode = sc1.nextInt();
		sc1.close();
		
		Scanner sc2 = new Scanner(System.in);
		System.out.println("Select Mode : Normal [0], Lost Packet [1], Delayed Packet [2], Duplicate Packet [3]");
		simulation = sc2.nextInt();
		sc2.close();
		
		if(simulation != 0) {
			Scanner sc3 = new Scanner(System.in);
			System.out.println("Which packet would you like to simulate the error");
			packetNumber = sc3.nextInt();
			sc3.close();
			
			if(simulation == 2) {
				Scanner sc4 = new Scanner(System.in);
				System.out.println("After how many packets would you like to send the delayed one?");
				packetDelay = sc4.nextInt();
				sc4.close();
			} else if (simulation == 3) {
				Scanner sc5 = new Scanner(System.in);
				System.out.println("How many times would you like to duplicate this packet?");
				dup = sc5.nextInt();
				sc5.close();
			}
		}
		
		com = new ComFunctions();
		clientSendSocket = com.startSocket();
		clientRecieveSocket = com.startSocket(interHostPort);
		sendRecieveSocket = com.startSocket();
	}
	
	public static void main(String[] args) {		
		IntermediateHost host = new IntermediateHost();
		host.recieveMessage();
	}
}
