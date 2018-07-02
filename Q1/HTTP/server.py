import http.server
from urllib.parse import urlparse, parse_qs

HOST_NAME = 'localhost'
PORT_NUMBER = 8080


class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(s): # Respond to a GET request.
        query_components = parse_qs(urlparse(s.path).query)
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()
        s.wfile.write("<html><head><title>Echo</title></head>".encode('utf-8'))
        s.wfile.write(("<body><p>"+str(list(query_components.keys()))+" "+str(list(query_components.values()))+"</p>").encode('utf-8'))
        s.wfile.write("</body></html>".encode('utf-8'))

if __name__ == '__main__':
    server_class = http.server.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print ("Server started")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print ("Server stopped")