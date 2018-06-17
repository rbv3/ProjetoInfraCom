#!/usr/bin/env python

import socket


serverAddr = '127.0.0.1'
serverPort = 8080

mySocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
mySocket.bind((serverAddr, serverPort))
mySocket.listen(1)

conn, addr = mySocket.accept()
print ("Ouvindo de : " + str(addr))

while 1:
    data = conn.recv(1024)
    if data.decode('utf-8') == 'off':
    	print ("Conexão encerrada pelo cliente")
    	break
    print ("Mensagem recebida: " + data.decode('utf-8'))
    conn.sendall(data)

conn.close()