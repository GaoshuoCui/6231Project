package FrontEnd;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import ReplicaHost1RemoteObject.EventSystemInterface;
import ReplicaHost1RemoteObject.EventSystemInterfaceHelper;
import ReplicaHost1RemoteObject.EventSystemInterfacePOA;
import functions.FuntionMembers;



public class FrontEnd extends EventSystemInterfacePOA {
	
	
	public static void main(String[] args) throws Exception {

		// create and initialize the ORB //// get reference to rootpoa &amp; activate the POAManager
		ORB orb = ORB.init(new String[]{"-ORBInitialHost", "localhost", "-ORBInitialPort", "1050"}, null);   
		POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		rootpoa.the_POAManager().activate();

		FrontEnd fe = new FrontEnd(orb);
	

		// get object reference from the servant
		org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fe);
		EventSystemInterface href = EventSystemInterfaceHelper.narrow(ref);
		
		org.omg.CORBA.Object objRef =  orb.resolve_initial_references("NameService");
		NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

		NameComponent path[] = ncRef.to_name("FrontEnd");
		ncRef.rebind(path, href);
		System.out.println("FE Server is running");


		// wait for invocations from clients
		while (true){
			orb.run();
		}
		
	}
	

	private int targetPort=2019;
	private DatagramSocket aSocket = null;
	private DatagramSocket listenSocket = null;
	
	private String request=null;
	private ORB orb;
	private Map<String,String> resultSet=new HashMap<String,String>();
	private HashMap<String, Object> result=new HashMap<String, Object>();
	private String managerId="*";
	private String eventId="*";
	private String eventtype="*";
	private String capacity="*";
	private String customerID="*";
	private String neweventId="*";
	private String neweventtype="*";
	private String oldeventId="*";
	private String oldeventtype="*";
	
	public FrontEnd(ORB orb ) {
		this.orb=orb;
	}

	
    private HashMap<String, Object> udpListener(DatagramSocket listenSocket,  Map<String,String> resultSet) throws Exception {
    	listenSocket=new DatagramSocket(8001);
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
        	listenSocket.receive(packet);
            //String result = new String(packet.getData(), 0 , packet.getLength());
            
           
            result=(HashMap<String, Object>) FuntionMembers.byteArrayToObject(packet.getData());
            
            
            System.out.println("receive " + result.get("addEvent"));

            //String[] res = result.split(":");
            //resultSet.put(res[0], res[1]);

            listenSocket.close();
        } catch (SocketException e){

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    
	public void udpSender(String request) {
		try {
			aSocket = new DatagramSocket();
			
			byte[] sData=request.toString().getBytes();
			
			InetAddress address = InetAddress.getByName("localhost");
			DatagramPacket sendPacket=new DatagramPacket(sData,sData.length,address,targetPort);
			aSocket.send(sendPacket);
			System.out.println("send out request");
			
			aSocket.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}finally {if(aSocket != null) aSocket.close();}
	}
	

	
	
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
	
	@Override
	public boolean addEvent(String managerId, String eventId, String eventtype, int capacity) {
		// TODO Auto-generated method stub
		request=managerId.substring(0, 3)+":"+"addEvent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		
		System.out.println(request);
		HashMap<String, Object> listenResult=new HashMap<String, Object>();
		this.udpSender(request);
		
		listenResult.get("addEvent");
		try {
			listenResult=this.udpListener(listenSocket, resultSet);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		//TODO
		return (boolean) listenResult.get("addEvent");
	}

	@Override
	public boolean removeEvent(String managerId, String eventId, String eventtype) {
		// TODO Auto-generated method stub
		
		request=managerId.substring(0, 3)+":"+"removeEvent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		
		this.udpSender(request);
		return false;
	}

	@Override
	public Any listEventAvailability(String managerId, String eventtype) {
		// TODO Auto-generated method stub
		request=managerId.substring(0, 3)+":"+"listEventAvailability"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		System.out.println(request);
		this.udpSender(request);
		HashMap<String, Integer> result = new HashMap<>();
		return returnAny(result);
	}

	
	private Any returnAny(Object obj) {
		Any any = orb.create_any();
		any.insert_Value((Serializable) obj);
		return any;
	}
	
	@Override
	public Any bookevent(String customerId, String eventId, String eventtype) {
		// TODO Auto-generated method stub
		
		request=customerId.substring(0, 3)+":"+"bookevent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		this.udpSender(request);
		return null;
	}

	@Override
	public Any getbookingSchedule(String customerId) {
		// TODO Auto-generated method stub
		
		request=customerId.substring(0, 3)+":"+"getbookingSchedule"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		this.udpSender(request);
		return null;
	}

	@Override
	public Any dropevent(String customerId, String eventId) {
		// TODO Auto-generated method stub
		request=customerId.substring(0, 3)+":"+"dropevent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		this.udpSender(request);
		return null;
	}

	@Override
	public Any swapEvent(String customerId, String neweventId, String neweventtype, String oldeventId,
			String oldeventtype) {
		// TODO Auto-generated method stub
		
		request=customerId.substring(0, 3)+":"+"swapEvent"+"-"+managerId+"-"+eventId+"-"+eventtype+"-"+capacity+"-"+customerID+"-"+neweventId+"-"+neweventtype+"-"+oldeventId+"-"+oldeventtype;
		this.udpSender(request);
		
		return null;
	}

}
