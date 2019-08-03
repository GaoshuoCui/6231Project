package FrontEnd;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.omg.CORBA.Any;

import remoteObject.EventSystemInterfacePOA;

public class FrontEnd extends EventSystemInterfacePOA {
	

	private int targetPort;
	private DatagramSocket aSocket = null;
	private String request=null;
	
	
	
	
	
    /**
    sendRequest(sb.append(City)
            .append(":")
        command[0]
            .append("addEvent")
        command[1]
        	ManagerID
        command[2]
				EventId
        command[3] 
        	event type;
       	command[4]
       		event compacity;
       	command[5]
       		customerID;
       	command[6]
       		new eventID;
       	command[7]
       		new eventtype;
       	command[8]
       		old eventID;
       	command[9]
       		old eventtype;               		
     */
    
	
	public void udpSender(String request) {
		try {
			aSocket = new DatagramSocket();
			
			byte[] sData=request.toString().getBytes();
			
			InetAddress address = InetAddress.getByName("localhost");
			DatagramPacket sendPacket=new DatagramPacket(sData,sData.length,address,targetPort);
			aSocket.send(sendPacket);
			
			
			aSocket.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {if(aSocket != null) aSocket.close();}
	}
	

	@Override
	public boolean addEvent(String managerId, String eventId, String eventtype, int capacity) {
		// TODO Auto-generated method stub
		request="addEvent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity;
		
		this.udpSender(request);
		
		
		
		//TODO
		return false;
	}

	@Override
	public boolean removeEvent(String managerId, String eventId, String eventtype) {
		// TODO Auto-generated method stub
		
		request="removeEvent"+managerId+"-"+eventId+"-"+eventtype;
		
		this.udpSender(request);
		return false;
	}

	@Override
	public Any listEventAvailability(String managerId, String eventtype) {
		// TODO Auto-generated method stub
		request="listEventAvailability"+managerId+"-"+eventtype;
		
		return null;
	}

	@Override
	public Any bookevent(String customerId, String eventId, String eventtype) {
		// TODO Auto-generated method stub
		
		request="bookevent"+customerId+"-"+eventId+"-"+eventtype;
		
		return null;
	}

	@Override
	public Any getbookingSchedule(String customerId) {
		// TODO Auto-generated method stub
		
		request="getbookingSchedule"+customerId;
		
		return null;
	}

	@Override
	public Any dropevent(String customerId, String eventId) {
		// TODO Auto-generated method stub
		request="dropevent"+customerId+"-"+eventId;
		
		return null;
	}

	@Override
	public Any swapEvent(String customerId, String neweventId, String neweventtype, String oldeventId,
			String oldeventtype) {
		// TODO Auto-generated method stub
		
		request="swapEvent"+customerId+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		
		
		return null;
	}

}
