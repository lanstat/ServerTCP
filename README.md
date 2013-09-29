[ServerTCP](http://sugarscope.net) - Programar sockets y no morir en el intento
=========
ServerTCP es una libreria que permite crear un servidor TCP, abstrayendo al programador de la administracion,
de las conexiones activas como tambien de la recepcion de paquetes.

Implementacion
--------------------------------------

Servidor
--------------------------------------

Es necesario crear una clase que implemente la interfaz `IHandler`. Esta clase sera la encargada de recepcionar los
paquetes y procesarlos.
Esta clase debe tener un constructor por defecto.

```java
public class MiManejador implements IHandler{
  public MiManejador(){
  }
  
  public void handleMessage(Packet receivePacket){}
}
```

En el metodo `handleMessage` recibe el paquete que envia el cliente.
Luego se procede a instanciar el servidor y registrar el manejador.

```java
ServerTCP server = new ServerTCP(6000);
server.registerHandler(MiManejador.class);
server.start();
```

Cliente
--------------------------------------

```java
  Client.getInstance().connect("127.0.0.1", 6000);
```

Para enviar un paquete se usa la siguiente estructura

```java
Packet packet = new Packet(0);
packet.setData(true, "prueba", 1);
Client.getInstance().sendPackage(packet);
```

Para poder recepcionar la respuesta del servidor, se debe implementar la interfaz observer.

```java
public class Receptor implements Observer{
...
Client.getInstance().getReader().addObserver(Receptor.this);
...
}
```

