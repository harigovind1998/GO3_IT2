import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;

public class ServerWorker extends Thread {
	private final int BLOCK_SIZE = 512;
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
		while(true){
			
			byte[] msg = com.generateDataPacket(com.intToByte(blockNum), com.getBlock(blockNum, fileByteReadArray));
			
			SendingResponse = com.createPacket(msg, clientPort);
			if(mode == 1) {
				System.out.println(com.verboseMode("Sent", SendingResponse));
			}
			com.sendPacket(SendingResponse, SendRecieveSocket);
			RecievedResponse = com.recievePacket(SendRecieveSocket, 100);
			if(mode == 1) {
				System.out.println(com.verboseMode("Recieve", RecievedResponse));
			}
			if(!com.CheckAck(RecievedResponse, blockNum)) {
				System.out.println("Wrong block recieved");
			}
			if(SendingResponse.getData()[SendingResponse.getLength() -1] == 0){
				System.out.println("End of file reached");
				break;
			}
			blockNum ++ ;
		}
	}
	
	private void writeServe(){
		File yourFile = new File("./Server" + fileName);
		try {
			yourFile.createNewFile();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(0);
		}
		int blockNum = 0;
		byte[] incomingBlock = new byte[2];
		SendingResponse = com.createPacket(com.generateAckMessage(com.intToByte(blockNum)), clientPort);
		if(mode == 1) {
			System.out.println(com.verboseMode("Sent", SendingResponse));
		}
		com.sendPacket(SendingResponse, SendRecieveSocket);
		byte[] ackMsg = null;
		DatagramPacket ackPacket = null;
		blockNum++;
		while (true) {
			RecievedResponse = com.recievePacket(SendRecieveSocket, BLOCK_SIZE);
			if(mode == 1) {
				System.out.println(com.verboseMode("Recieve", RecievedResponse));
			}
			incomingBlock[0] = RecievedResponse.getData()[2];
			incomingBlock[1] = RecievedResponse.getData()[3];
			if(! (blockNum == ByteBuffer.wrap(incomingBlock).getShort())) {
				System.out.println("Wrong data packet recieved");
				System.exit(0);
			}
			com.writeArrayIntoFile(com.parseBlockData(RecievedResponse.getData()), Paths.get("./Server/" + fileName));
			ackMsg = com.generateAckMessage(com.intToByte(blockNum));
			ackPacket = com.createPacket(ackMsg, clientPort);
			if(mode == 1) {
				System.out.println(com.verboseMode("Send", ackPacket));
			}
			com.sendPacket(ackPacket, SendRecieveSocket);
//			if (!com.CheckData(RecievedResponse, blockNum)) {
//				System.out.println("Wrong block received");
//			}
			if(RecievedResponse.getData()[SendingResponse.getLength() -1] == 0){
				System.out.println("End of file reached");
				break;
			}
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
		initialPacket = packet;
		this.mode = mode;
	}
	
}
