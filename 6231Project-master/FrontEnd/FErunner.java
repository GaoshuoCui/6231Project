package FrontEnd;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import remoteObject.EventSystemInterface;
import remoteObject.EventSystemInterfaceHelper;


public class FErunner {
	
	public static void main(String[] args) throws Exception {

		// create and initialize the ORB //// get reference to rootpoa &amp; activate the POAManager
		ORB orb = ORB.init(args, null);      
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

}
