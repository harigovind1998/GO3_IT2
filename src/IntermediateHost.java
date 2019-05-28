import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class IntermediateHost {
	
	DatagramSocket recieveSocket, clientSendSocket, sendSocket;
	DatagramPacket clientRecievePacket, clientSendPacket, serverSendPacket, serverRecievePacket;
	ComFunctions com;
	workerThread externalThread;
	int serverPort = 69;
	int clientPort;
	int mode;
	private int interHostPort = 23;
	
	private static int packetNumber;
	private static int packetDelay;
	private static int simulation;
	private static int dup;
	private int packetCounter = 1;
	
	/**
	 * Waits to recieve a message from the client and passes that on to the server
	 */
	public void recieveMessage(){
		DatagramPacket tempPacket = null;
		byte[] blockNum = null;
		int packet = 0;
		int tempPort = 0;
		//while(true) {
			switch (simulation) {
				case 0: 
					while(true) {
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(recieveSocket, 516);
					tempPort = clientRecievePacket.getPort();
					System.out.println(tempPort);
					if(packet == 0) {
						clientPort = tempPort;
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
						}
					}else if(!(tempPort == clientPort)) {
						serverPort = tempPort;
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from server", clientRecievePacket));
						}
					}else if(tempPort == clientPort) {
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
						}
					}
					packet ++;
					if(tempPort == clientPort) {
						serverSendPacket = com.createPacket(clientRecievePacket.getData(), serverPort);
						if(mode == 1) {
							System.out.println(com.verboseMode("Send to Server", clientRecievePacket));
						}
					}else if(tempPort == serverPort) {
						serverSendPacket = com.createPacket(clientRecievePacket.getData(), clientPort);
						if(mode == 1) {
							System.out.println(com.verboseMode("Send to Client", clientRecievePacket));
						}
					}
									
					
					com.sendPacket(serverSendPacket, sendSocket);
					}
				
				case 1:
					
					while(true) {
						//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
						clientRecievePacket = com.recievePacket(recieveSocket, 516);
						tempPort = clientRecievePacket.getPort();
						System.out.println(tempPort);
						if(packet == 0) {
							clientPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
							}
						}else if(!(tempPort == clientPort)) {
							serverPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from server", clientRecievePacket));
							}
						}else if(tempPort == clientPort) {
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
							}
						}
						packet ++;
						if(tempPort == clientPort) {
							serverSendPacket = com.createPacket(clientRecievePacket.getData(), serverPort);
							
						}else if(tempPort == serverPort) {
							serverSendPacket = com.createPacket(clientRecievePacket.getData(), clientPort);
							
						}
					
						if (!(packetCounter == packetNumber)) {
							
							com.sendPacket(serverSendPacket, sendSocket);
							packetCounter++;
							if((mode == 1)&& (tempPort == clientPort)) {
								System.out.println(com.verboseMode("Send to Server", clientRecievePacket));
							}
							
							if((mode == 1) && (tempPort == serverPort)) {
								System.out.println(com.verboseMode("Send to Client", clientRecievePacket));
							}
						}else {
							System.out.println("Simulating Lost Packet...");
							packetCounter++;
						}
										
						
							
						}
					
				case 2:
					System.out.println("Simulating Delayed Packet...");
					
					//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
					clientRecievePacket = com.recievePacket(recieveSocket, 516);
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", clientRecievePacket));
					}
					
					byte[] dataArr2 = clientRecievePacket.getData();
					
					serverSendPacket = com.createPacket(clientRecievePacket.getData(), serverPort);
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientRecievePacket));
					}
					
					if (dataArr2[1] == 3 || dataArr2[1] == 4) {
						blockNum = com.intToByte(packetNumber);
						if(dataArr2[2] == blockNum[0] && dataArr2[3] == blockNum[1]) {
								byte[] holdData = dataArr2;
								holdData[2] = blockNum[0];
								holdData[3] = blockNum[1];
								tempPacket = com.createPacket(holdData, serverPort);
								Long delay = new Long(packetDelay);
								externalThread = new workerThread(tempPacket, sendSocket, delay);
						}
						
					} else {
						com.sendPacket(serverSendPacket, sendSocket);
					}
					
					com.sendPacket(serverSendPacket, sendSocket);
					
					//Listens to the server response, and forwards that on to the client in the reverse manner, printing each each of the messages
					serverRecievePacket = com.recievePacket(sendSocket,516);
					
					serverPort = serverRecievePacket.getPort();
					
		
					if(mode == 1) {
						System.out.println(com.verboseMode("Recieve", serverRecievePacket));
					}
					
					clientSendPacket = com.createPacket(serverRecievePacket.getData(), clientRecievePacket.getPort());
					
					if(mode == 1) {
						System.out.println(com.verboseMode("Send", clientSendPacket));
					}
					com.sendPacket(clientSendPacket, sendSocket);
					break;
				case 3:
					while(true) {
						//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
						clientRecievePacket = com.recievePacket(recieveSocket, 516);
						tempPort = clientRecievePacket.getPort();
						System.out.println(tempPort);
						if(packet == 0) {
							clientPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
							}
						}else if(!(tempPort == clientPort)) {
							serverPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from server", clientRecievePacket));
							}
						}else if(tempPort == clientPort) {
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", clientRecievePacket));
							}
						}
						
						packet ++;
						if(tempPort == clientPort) {
							serverSendPacket = com.createPacket(clientRecievePacket.getData(), serverPort);
							
						}else if(tempPort == serverPort) {
							serverSendPacket = com.createPacket(clientRecievePacket.getData(), clientPort);
							
						}
										
						if(packetCounter != packetNumber) {
							com.sendPacket(serverSendPacket, sendSocket);
							packetCounter++;
							if((mode == 1)&& (tempPort == clientPort)) {
								System.out.println(com.verboseMode("Send to Server", clientRecievePacket));
							}
							
							if((mode == 1) && (tempPort == serverPort)) {
								System.out.println(com.verboseMode("Send to Client", clientRecievePacket));
							}
						}else {
							for(int i = 0; i< dup; i ++) {
								com.sendPacket(serverSendPacket, sendSocket);
								if((mode == 1)&& (tempPort == clientPort)) {
									System.out.println(com.verboseMode("Duplicate send to Server", clientRecievePacket));
								}
								
								if((mode == 1) && (tempPort == serverPort)) {
									System.out.println(com.verboseMode("duplicate Send to Client", clientRecievePacket));
								}
							}
							packetCounter++;
							
						}
					}
			//}
		}
	}
	
	
	public IntermediateHost() {
		// TODO Auto-generated constructor stub
		Scanner sc1 = new Scanner(System.in);
		System.out.println("Select Mode : Quiet [0], Verbose [1]");
		mode = sc1.nextInt();
		
		
		Scanner sc2 = new Scanner(System.in);
		System.out.println("Select Mode : Normal [0], Lost Packet [1], Delayed Packet [2], Duplicate Packet [3]");
		simulation = sc2.nextInt();
		
		
		if(simulation != 0) {
			Scanner sc3 = new Scanner(System.in);
			System.out.println("Which packet would you like to simulate the error");
			packetNumber = sc3.nextInt();
			
			
			if(simulation == 2) {
				Scanner sc4 = new Scanner(System.in);
				System.out.println("After how many milliseconds would you like to send the delayed one?");
				packetDelay = sc4.nextInt();
				
			} else if (simulation == 3) {
				Scanner sc5 = new Scanner(System.in);
				System.out.println("How many times would you like to duplicate this packet?");
				dup = sc5.nextInt();
				
			}
		}
		sc1.close();
		
		
		com = new ComFunctions();
		clientSendSocket = com.startSocket();
		recieveSocket = com.startSocket(interHostPort);
		sendSocket = com.startSocket();
	}
	
	public static void main(String[] args) {		
		IntermediateHost host = new IntermediateHost();
		host.recieveMessage();
	}
}
