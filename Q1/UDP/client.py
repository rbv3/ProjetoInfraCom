#!/usr/bin/env python3

import sys
from socket import *

serverAddr = "127.0.0.1"
serverPort = 8080

mySocket = socket( AF_INET, SOCK_DGRAM )

data = ""
while str(data).upper().find("SAIR") < 0:

    myMessage = input('Digite a msg e\\ou \"sair\" para sair\n')
    mySocket.sendto(myMessage.encode('utf-8'),(serverAddr,serverPort))
    print("-----------------")

    (data,addr) = mySocket.recvfrom(1024)
    print (data.decode('utf-8'))
    print()

mySocket.sendto("saiu".encode('utf-8'),(serverAddr,serverPort))

