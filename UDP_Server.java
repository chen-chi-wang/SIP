import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class UDP_Server {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DatagramSocket serverSocket = new DatagramSocket(2000);

		byte[] receiveData = new byte[1024];
		String content = "";

		//REGISTER
		String register_lines_7="";								//To
		String register_lines_2="";								//Contact
		String invite_lines_0="SIP/2.0 302 Moved Temporarily";	//302
		String invite_lines_6="";								//eat Contact
		//Map
		Map<String, String> map =  new HashMap<String, String>();//http://openhome.cc/Gossip/JavaGossip-V2/HashMap.htm
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); // socket要用某種資料結構，所以先創好這種資料結構
			serverSocket.receive(receivePacket); 												// socket把receive到的東西放到這種資料結構

			content = new String(receivePacket.getData()); 										// 也放一份副本到一般的String，print出來看看
			InetAddress IPAddress = receivePacket.getAddress(); 								// 還想知道它從哪裡來
			int port =  receivePacket.getPort();												//ex: 5060 (for Bob)
			System.out.printf("content: \n" + content); 										// debug		
			String lines[] = content.split("\\r?\\n|\\r");										//切割字串，\r\n for Windows，\n for Linux，\r for old Macs
			String words[] = lines[0].split("\\s+");											//空白字元
			if(words[0].equals("REGISTER")){
				//Do register
				System.out.println("Come a REGISTER");
				map.put(lines[7], lines[2]);
				lines[0] = "SIP/2.0 200 OK";
				// send back
				byte[] sendData = new byte[1024];													//準備回傳
				String content3 = "";
				content3 += lines[0];
				content3 += "\r\n";
				int i;
				for (i = 1; i < lines.length; i++) {
					if (i == lines.length - 1) {
						content3 += lines[i];
					} else {
						content3 += lines[i];
						content3 += "\r\n";
					}

				}
				
				sendData = content3.getBytes();
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5060);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);

			}
			else if (words[0].equals("INVITE")){
				//Do invite
				System.out.println("Come an INVITE");
				
				System.out.println(map.get("To:"+words[1]));//Contact
				
				// send back
				byte[] sendData = new byte[1024];													//準備回傳
				String content3 = "";
				
				content3+="SIP/2.0 302 Moved Temporarily";
				content3 += "\r\n";
				content3+=lines[9];
				content3 += "\r\n";
				content3+=lines[6];
				content3 += "\r\n";
				content3+=lines[8];
				content3 += "\r\n";
				content3+=lines[1];
				content3 += "\r\n";
				content3+=lines[5];
				content3 += "\r\n";
				content3+=map.get("To:"+words[1]);
				content3 += "\r\n";
				System.out.printf("content3: \n" + content3); 
				sendData = content3.getBytes();
				//DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5065);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
				serverSocket.send(sendPacket);
				serverSocket.close();
				break;
			}
		}
	}
}
