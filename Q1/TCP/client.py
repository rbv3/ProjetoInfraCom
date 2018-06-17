#!/usr/bin/env python

import socket


serverAddr = '127.0.0.1'
serverPort = 8080

mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mySocket.connect((serverAddr, serverPort))


data = ""
while str(data).upper().find("SAIR") < 0:

    myMessage = input('Digite a msg e\\ou \"sair\" para sair\n')
    mySocket.sendall(myMessage.encode('utf-8'))
    print("-----------------")

    data = mySocket.recv(1024)
    print (data.decode('utf-8'))
    print()

mySocket.send("off".encode('utf-8'))
mySocket.close()