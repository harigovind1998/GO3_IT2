import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class IntermediateHost {
	
	DatagramSocket sendRecieveSocket;
	DatagramPacket recievePacket, sendPacket;
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
					recievePacket = com.recievePacket(sendRecieveSocket, 516);
					tempPort = recievePacket.getPort();
					
					if(packet == 0) {
						clientPort = tempPort;
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from client", recievePacket));
						}
					}else if(!(tempPort == clientPort)) {
						serverPort = tempPort;
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from server", recievePacket));
						}
					}else if(tempPort == clientPort) {
						if(mode == 1) {
							System.out.println(com.verboseMode("Recieve from client", recievePacket));
						}
					}
					packet ++;
					if(tempPort == clientPort) {
						sendPacket = com.createPacket(recievePacket.getData(), serverPort);
						if(mode == 1) {
							System.out.println(com.verboseMode("Send to Server", recievePacket));
						}
					}else if(tempPort == serverPort) {
						sendPacket = com.createPacket(recievePacket.getData(), clientPort);
						if(mode == 1) {
							System.out.println(com.verboseMode("Send to Client", recievePacket));
						}
					}
									
					
					com.sendPacket(sendPacket, sendRecieveSocket);
					}
				
				case 1:
					
					while(true) {
						//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
						recievePacket = com.recievePacket(sendRecieveSocket, 516);
						tempPort = recievePacket.getPort();
						
						if(packet == 0) {
							clientPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}else if(!(tempPort == clientPort)) {
							serverPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from server", recievePacket));
							}
						}else if(tempPort == clientPort) {
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}
						packet ++;
						if(tempPort == clientPort) {
							sendPacket = com.createPacket(recievePacket.getData(), serverPort);
							
						}else if(tempPort == serverPort) {
							sendPacket = com.createPacket(recievePacket.getData(), clientPort);
							
						}
					
						if (!(packetCounter == packetNumber)) {
							
							com.sendPacket(sendPacket, sendRecieveSocket);
							packetCounter++;
							if((mode == 1)&& (tempPort == clientPort)) {
								System.out.println(com.verboseMode("Send to Server", recievePacket));
							}
							
							if((mode == 1) && (tempPort == serverPort)) {
								System.out.println(com.verboseMode("Send to Client", recievePacket));
							}
						}else {
							System.out.println("Simulating Lost Packet...");
							packetCounter++;
						}
										
						
							
						}
					
				case 2:
					while(true) {
						//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
						recievePacket = com.recievePacket(sendRecieveSocket, 516);
						tempPort = recievePacket.getPort();
						
						if(packet == 0) {
							clientPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}else if(!(tempPort == clientPort)) {
							serverPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from server", recievePacket));
							}
						}else if(tempPort == clientPort) {
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}
						packet ++;
						if(tempPort == clientPort) {
							sendPacket = com.createPacket(recievePacket.getData(), serverPort);
						}else if(tempPort == serverPort) {
							sendPacket = com.createPacket(recievePacket.getData(), clientPort);
						}
										
						if(packetCounter != packetNumber) {
							com.sendPacket(sendPacket, sendRecieveSocket);
							packetCounter++;
							if((mode == 1)&& (tempPort == clientPort)) {
								System.out.println(com.verboseMode("Send to Server", recievePacket));
							}
							
							if((mode == 1) && (tempPort == serverPort)) {
								System.out.println(com.verboseMode("Send to Client", recievePacket));
							}
						}else{
								packetCounter++;
								System.out.println("Delaying packet...");
								delaySimulator delay  = new delaySimulator(recievePacket, (long)packetDelay);
								
							}
						
						
					}
				case 3:
					while(true) {
						//Recieving a message to from the client, prints the message, created a new packet to send to the server, prints that message for clarification and sends it the server
						recievePacket = com.recievePacket(sendRecieveSocket, 516);
						tempPort = recievePacket.getPort();
						
						if(packet == 0) {
							clientPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}else if(!(tempPort == clientPort)) {
							serverPort = tempPort;
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from server", recievePacket));
							}
						}else if(tempPort == clientPort) {
							if(mode == 1) {
								System.out.println(com.verboseMode("Recieve from client", recievePacket));
							}
						}
						
						packet ++;
						if(tempPort == clientPort) {
							sendPacket = com.createPacket(recievePacket.getData(), serverPort);
							
						}else if(tempPort == serverPort) {
							sendPacket = com.createPacket(recievePacket.getData(), clientPort);
							
						}
										
						if(packetCounter != packetNumber) {
							com.sendPacket(sendPacket, sendRecieveSocket);
							packetCounter++;
							if((mode == 1)&& (tempPort == clientPort)) {
								System.out.println(com.verboseMode("Send to Server", recievePacket));
							}
							
							if((mode == 1) && (tempPort == serverPort)) {
								System.out.println(com.verboseMode("Send to Client", recievePacket));
							}
						}else {
							for(int i = 0; i< dup; i ++) {
								com.sendPacket(sendPacket, sendRecieveSocket);
								if((mode == 1)&& (tempPort == clientPort)) {
									System.out.println(com.verboseMode("Duplicate send to Server", recievePacket));
								}
								
								if((mode == 1) && (tempPort == serverPort)) {
									System.out.println(com.verboseMode("duplicate Send to Client", recievePacket));
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
		sendRecieveSocket = com.startSocket(interHostPort);
		
	}
	
	public static void main(String[] args) {		
		IntermediateHost host = new IntermediateHost();
		host.recieveMessage();
	}
}
