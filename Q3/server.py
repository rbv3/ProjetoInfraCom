import socket
import sys, select
 
def getLine():
    i,o,e = select.select([sys.stdin],[],[],0.0001)
    for s in i:
        if s == sys.stdin:
            input = sys.stdin.readline()
            return input
    return False
 
serverHost = "192.168.1.255"
serverPort = 8080
clientId = 0;
gamePort = 8081
playerCount = 0

send_address = (serverHost, serverPort)
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # Make Socket Reusable
serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1) # Allow incoming broadcasts
serverSocket.setblocking(False) # Set socket to non-blocking mode
serverSocket.bind(('', serverPort)) #Accept Connections on port
print ("Accepting connections on port ", serverPort)

while 1:
    try:
        message, address = serverSocket.recvfrom(8192)
        if message.decode('utf-8') == "hi":
            message, address = serverSocket.recvfrom(8192)
            if message.decode('utf-8') == "0":
                print("New client connected, given id ", clientId)
                serverSocket.sendto(clientId, send_address)
                serverSocket.sendto(gamePort, send_address)
                playerCount += 1
                if playerCount>= 4:
                    gamePort += 1
                    playerCount = 0
                clientId += 1
            else:
                print("Client requesting new game match")
                serverSocket.sendto(gamePort, send_address)
                playerCount += 1
                if playerCount>= 4:
                    gamePort += 1
                    playerCount = 0

    except:
        pass