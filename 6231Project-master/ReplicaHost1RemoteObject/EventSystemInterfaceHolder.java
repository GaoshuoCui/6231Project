package ReplicaHost1RemoteObject;

/**
* remoteObject/EventSystemInterfaceHolder.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from C:/Users/TLIN/eclipse-workspace/CORBAa2Lin/EventSystemInterface.idl
* Sunday, July 7, 2019 4:23:46 o'clock PM EDT
*/

public final class EventSystemInterfaceHolder implements org.omg.CORBA.portable.Streamable
{
  public ReplicaHost1RemoteObject.EventSystemInterface value = null;

  public EventSystemInterfaceHolder ()
  {
  }

  public EventSystemInterfaceHolder (ReplicaHost1RemoteObject.EventSystemInterface initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = ReplicaHost1RemoteObject.EventSystemInterfaceHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    ReplicaHost1RemoteObject.EventSystemInterfaceHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return ReplicaHost1RemoteObject.EventSystemInterfaceHelper.type ();
  }

}
