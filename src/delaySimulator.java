import java.net.DatagramPacket;
import java.net.DatagramSocket;
public class delaySimulator extends Thread{
	private ComFunctions com;
	private DatagramPacket packet;
	private DatagramSocket socket;
	private long delay;
	public delaySimulator(DatagramPacket packet, long delay) {
		com = new ComFunctions();
		socket = com.startSocket();
		this.packet = packet;
		this.delay = delay;
	}
	
	public void run() {
		try{
			Thread.sleep(delay);
			com.sendPacket(packet, socket);
		}
		catch(InterruptedException e){
			System.out.println(e);
		}
	}
}
