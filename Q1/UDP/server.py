#!/usr/bin/env python3

import socket

mySocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

serverAddr = '0.0.0.0'
serverPort = 8080

server = (serverAddr, serverPort)
mySocket.bind(server)
print("Listening on " + serverAddr + ":" + str(serverPort))

while True:
	payload, clientAddr = mySocket.recvfrom(1024)
	print("Echoing data \"" + str(payload.decode('utf-8')) +"\" back to " + str(clientAddr))
	sent = mySocket.sendto(payload, clientAddr)