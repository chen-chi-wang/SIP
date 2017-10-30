# SIP

## Requirement

**1. Implement a SIP Registrar (registration server) which can send back a "SIP 200 OK" response and store the mapping of `Contact` and `To` header.**

**2. Implement a SIP redirect server for accepting SIP `INVITE` request, and send back "SIP 302 Moved Temporarily" response with the `To` address of callee.**

## SIP Overview
The Session Initiation Protocol (SIP) is a signaling protocol for controlling multimedia communication sessions.

It is widely adopted in the voice and video calls of Internet telephony.

**Features**
- Application layer protocols, based on UDP by default
- Plain text

**Packet format**
- Header Fields
  - Methods, ex: `REGISTER`, `INVITE`, `ACK`, `BYE`..
- Message mody
  - Parameters, ex: Media type, codec..

<img src="https://i.imgur.com/JdJSjUz.png" width="540">


**Types of server**
- `Registration server`: Store client's address
- `Redirect server`: Resolve callee's address for caller, help to redirect the `INVITE` message
- `Proxy server`: A relay server
    
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://i.imgur.com/xijUTUs.png" width="500">

## Data Structures and Functions
Implement a infinite loop for listening to incoming request.

If comes a `REGISTER` request, store the `To` and `Contact` in the header fields, which is the mapping of a client's identity.

If comes a `INVITE` request, search the callee's address (which is `Contact` field of the callee), and send back a "SIP 302 Moved Temporarily" response to caller.

```
while (true) {

    Server listens for incoming packet
    
    If it is a "REGISTER" packet
    then 
        response "SIP/2.0 200 OK"
        store the mapping of "Contact" and "To" field
        
    If it is a "INVITE" packet
    then 
        search for "To" address of callee
        response "SIP 302 Moved Temporarily"
}    
```

**Functions**
- `DatagramSocket` - A UDP service socket for sending and receiving datagram packets
```c
/*Server is going to receive on UDP port 2000*/
DatagramSocket serverSocket = new DatagramSocket(2000);
```
```c
/*Server receives a datagram packet from this socket*/
serverSocket.receive(receivePacket);
```
```c
/*Server sends a datagram packet from this socket*/
serverSocket.send(sendPacket);
```

-  `DatagramPacket` - A UDP service packet

- `InetAddress` - Represent an Internet Protocol (IP) address

## Demo 1 : Registration Server
User settings on CCLUA.

<img src="https://i.imgur.com/Ciny4Ch.png" width="250">


Server settings on CCLUA.

<img src="https://i.imgur.com/EWcxY4I.png" width="250">


Client Bob is going to register.

<img src="https://i.imgur.com/YmNHpCg.png" width="360">


Registration server receives `REGISTER` packet, here shows the content.

<img src="https://i.imgur.com/OtY2PBI.png" width="490">

After receiving it, the server sendbacks `SIP/2.0 200 OK` to Bob.


Registration done.

<img src="https://i.imgur.com/xoEgaaB.png" width="360">

## Demo 2 : Redirect Server
Client CCF is going to call Bob. So he inputs Bob's `To` address, which is the public address of Bob.

<img src="https://i.imgur.com/sI0ziX9.png" width="720">


Redirect server receives the `INVITE` packet from CCF.

<img src="https://i.imgur.com/3RQJlZh.png" width="500">


The server sends redirct packet to CCF. That packet contains Bob's `Contact` address.

<img src="https://i.imgur.com/dWboz3r.png" width="500">

After receiving it, CCF then calls Bob directly.


Bob alerts, and CCF ringbacks.

<img src="https://i.imgur.com/RXNij8L.png" width="720">


Once Bob answers, the media streaming can begin.

<img src="https://i.imgur.com/uxP7Vk1.png" width="720">
