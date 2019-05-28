import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;

public class ServerWorker extends Thread {
	private final int BLOCK_SIZE = 516;
	private DatagramPacket initialPacket, RecievedResponse, SendingResponse;
	private int clientPort;
	private String fileName;
	private DatagramSocket SendRecieveSocket; 
	private ComFunctions com;
	private int job, mode;

	//private byte[] fileByteReadArray, fileByteWriteArray;

	
	/**
	 * Gets the name of the file that is being written into or read from
	 */
	private void getFileName() {
		byte[] data = initialPacket.getData();
		int[] secondZero = {3,0,0};
		int track = 1;
		for(int i = 3; i<data.length ; i ++) {
			if(data[i] == 0) {
				secondZero[track] = i;
				track++;
				if (track == 3) {
					break;
				}
			}
		}
		byte[] file = Arrays.copyOfRange(data, 2 , secondZero[1]);
		//byte[] mode = Arrays.copyOfRange(data, secondZero[1]+1, secondZero[2]);
		this.fileName = new String(file);
		//this.mode = new String(mode);
	}
	
	/**
	 * Decodes the incoming packet to get the necessary information, namely the file name and weather the its a read or write request
	 */
	private void decodePacket() {
		job = initialPacket.getData()[1]; //format of the message has been checked so second bit will determine if the request is a read or write
		clientPort = initialPacket.getPort();
		
		getFileName();
	}
	
	
	/**
	 * Sends the contents over to the client
	 */
	private void readServe() {
		
		byte [] fileByteReadArray = com.readFileIntoArray("./Server/" + fileName);
		int blockNum = 1;
		mainLoop:
		while(true){
			
			byte[] msg = com.generateDataPacket(com.intToByte(blockNum), com.getBlock(blockNum, fileByteReadArray));
			RecievedResponse = com.createPacket(100);
			SendingResponse = com.createPacket(msg, clientPort);
			
			outterSend:
				while(true) {
					com.sendPacket(SendingResponse, SendRecieveSocket);
					if(mode == 1) {
						System.out.println(com.verboseMode("Sent", SendingResponse));
					}
					try {
						innerSend:
							while(true) {
								SendRecieveSocket.receive(RecievedResponse);
								if(mode == 1) {
									System.out.println(com.verboseMode("Recieve", RecievedResponse));
								}
								if(com.CheckAck(RecievedResponse, blockNum)) {
									break innerSend;
								}else {
									System.out.println("Wrong block recieved");
								}
							}
						break outterSend;
					} catch (IOException e) {
						if(mode == 1) {
							
							System.out.println(com.verboseMode("Preparing packet for Resend", SendingResponse));
						}
					}
				}
			if(SendingResponse.getData()[SendingResponse.getLength() -1] == 0){
				System.out.println("End of file reached");
				break mainLoop;
			}
			blockNum ++ ;
		}
	}
	
	
	private void writeServe(){
		File yourFile = new File("./Server/" + fileName);
		try {
			yourFile.createNewFile();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(0);
		}
		byte[] ackMsg = null;
		int blockNum = 0;
		byte[] incomingBlock = new byte[2];
		int last;
		RecievedResponse = com.createPacket(BLOCK_SIZE);
		
		SendingResponse = com.createPacket(com.generateAckMessage(com.intToByte(blockNum)), clientPort);
		int expectedBlock = 1;
		blockNum++;
		outterLoop:
		while (true) {
			mainLoop:
				while(true) {
					if(mode == 1) {
						System.out.println(com.verboseMode("Sent", SendingResponse));
					}
					com.sendPacket(SendingResponse, SendRecieveSocket);
					try {
						innerLoop:
							while(true) {
								SendRecieveSocket.receive(RecievedResponse);
								if(mode == 1) {
									System.out.println(com.verboseMode("Recieve", RecievedResponse));
								}
						
								incomingBlock[0] = RecievedResponse.getData()[2];
								incomingBlock[1] = RecievedResponse.getData()[3];
								if( (blockNum == ByteBuffer.wrap(incomingBlock).getShort())) {
									com.writeArrayIntoFile(com.parseBlockData(RecievedResponse.getData()), Paths.get("./Server/" + fileName));
									last = RecievedResponse.getData()[RecievedResponse.getLength() -1];
									ackMsg = com.generateAckMessage(com.intToByte(blockNum));
									SendingResponse = com.createPacket(ackMsg, clientPort);
									if(last == 0){
										com.sendPacket(SendingResponse, SendRecieveSocket);
										if(mode == 1) {
											System.out.println(com.verboseMode("Sent", SendingResponse));
										}
										System.out.println("End of file reached");
										break outterLoop;
									}
									break innerLoop;
								
								}else {
									System.out.println("Wrong data packet recieved");
									System.exit(0);
								}
						
							}
						break mainLoop;
					} catch (Exception e) {
						// TODO: handle exception
						if(mode == 1) {
							System.out.println(com.verboseMode("ReSending", SendingResponse));
						}
					}
				}
			
//			incomingBlock[0] = RecievedResponse.getData()[2];
//			incomingBlock[1] = RecievedResponse.getData()[3];
//			if(! (blockNum == ByteBuffer.wrap(incomingBlock).getShort())) {
//				System.out.println("Wrong data packet recieved");
//				System.exit(0);
//			}
//			com.writeArrayIntoFile(com.parseBlockData(RecievedResponse.getData()), Paths.get("./Server/" + fileName));
//			last = RecievedResponse.getData()[RecievedResponse.getLength() -1];
//			ackMsg = com.generateAckMessage(com.intToByte(blockNum));
//			SendingResponse = com.createPacket(ackMsg, clientPort);
//			if(last == 0){
//				com.sendPacket(SendingResponse, SendRecieveSocket);
//				if(mode == 1) {
//					System.out.println(com.verboseMode("Sent", SendingResponse));
//				}
//				System.out.println("End of file reached");
//				break outterLoop;
//			}
			++blockNum;
		}
	}
	
	/**
	 * decodes and then performs the necessary task
	 */
	public void run() {
		
		decodePacket();
		if(job == 1) {
			readServe();
		}else if (job ==2) {
			writeServe();
		}
	}
	
	public ServerWorker(String name, DatagramPacket packet, int mode) {
		// TODO Auto-generated constructor stub
		super(name);
		com = new ComFunctions();
		SendRecieveSocket = com.startSocket();
		try {
			SendRecieveSocket.setSoTimeout(1500);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		initialPacket = packet;
		this.mode = mode;
	}
	
}
