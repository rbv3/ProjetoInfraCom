import socket
import sys, select
import time

def stopwatch(seconds):
    start = time.time()
    time.clock()    
    elapsed = 0
    while elapsed < seconds:
        elapsed = time.time() - start
        print ("Waiting",seconds,"...")
        time.sleep(0.5)

def getLine():
	i,o,e = select.select([sys.stdin],[],[],0.0001)
	for s in i:
		if s == sys.stdin:
			input = sys.stdin.readline()
			return input
	return False

address = ""
message = ""
serverHost = "192.168.1.255"
serverPort = 8080
playerCount = 0
 
serverAddr = (serverHost, serverPort)
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # Make Socket Reusable
serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1) # Allow incoming broadcasts
serverSocket.setblocking(False) # Set socket to non-blocking mode
serverSocket.bind(('', serverPort)) #Accept Connections on port
print ("Connected to", serverPort)

setupMsg = "hi"


serverSocket.sendto(setupMsg.encode('utf-8'), serverAddr)

try:
	(data, address) = serverSocket.recvfrom(8192)
except:
	print("No response from server, trying again...")
	pass
	
recv = data.decode('utf-8').split()
gamePort = recv[1]
myId = recv[0]

gameAddr = (serverHost, gamePort)
gameSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
gameSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # Make Socket Reusable
gameSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1) # Allow incoming broadcasts
gameSocket.setblocking(False) # Set socket to non-blocking mode
gameSocket.bind(('', gamePort)) #Accept Connections on port
print ("Connecting to", gamePort)


print("Connected to game port", gamePort, "with ID", myId)

while 1:
	while 1:
		gameSocket.sendto((str(playerCount)).encode('utf-8'), gameAddr)
		gameSocket.setblocking(True)    
		message, address = gameSocket.recvfrom(8192)

		if message.decode('utf-8') == "0":
			playerCount += 1
			break
		elif message:
			playerCount = int(message.decode('utf-8'))
		else:
			stopwatch(2)
			print("Waiting for players...")
	while 1:
		print("Starting game...")
		print("Input a number between 1 and 10: ")

		while(True):
			input = getLine()

			if not (1 <= int(input) <= 10):
				print("Please input a number between 1 and 10")
				input = getLine()
			else:
				break

		results[myId] = input
		gameSocket.sendto((myId," ",input).encode('utf-8'), gameAddr)
		print(myId, ":", input)

		try:
			gameSocket.settimeout(2)
			for x in range(playerCount):
				message, address = gameSocket.recvfrom(8192)

				if message.decode('utf-8') == "0":
					playerCount += 1
					gameSocket.sendto(playerCount.encode('utf-8'), gameAddr)
					x -= 1
				elif int(message.decode('utf-8')) == playerCount:
					x -= 1

				recv = message.decode(utf-8).split()
				print(recv[0],":",recv[1])
				results[recv[0]]=int(recv[1])

			print("Starting counting from player 0")
			result = sum(recv.values())
			winner = (sum % (playerCount+1))
			print("Player", winner, "wins!")
			print("Starting a new game...")

		except socket.timeout:
			print("A player disconnected")
			playerCount -= 1
			if playerCount>=2:
				print("Restarting game...")
			else:
				print("Not enough players for a match, finding new game...")
				gameSocket.close()
				try:
					(data, address) = serverSocket.recvfrom(8192)
				except:
					print("No response from server, trying again...")
					pass
					
				recv = data.decode('utf-8').split()
				gamePort = recv[1]
				myId = recv[0]

				gameAddr = (serverHost, gamePort)
				gameSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
				gameSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1) # Make Socket Reusable
				gameSocket.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1) # Allow incoming broadcasts
				gameSocket.setblocking(False) # Set socket to non-blocking mode
				gameSocket.bind(('', gamePort)) #Accept Connections on port
				print("Connecting to", gamePort)
				print("Connected to game port", gamePort, "with ID", myId)
				break