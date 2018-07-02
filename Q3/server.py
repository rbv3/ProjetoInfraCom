import socket
import sys, select
 
serverHost = "192.168.1.255"
serverPort = 8080
gamePort = 8081
playerCount = 0

breadcastAddr = (serverHost, serverPort)
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
			print("New client connected, given id", playerCount)
			payload = (playerCount," ",gamePort)
			serverSocket.sendto(payload.encode('utf-8'), breadcastAddr)
			print("sent payload")
			playerCount += 1
			if playerCount>= 4:
				gamePort += 1
				playerCount = 0
	except:
		pass