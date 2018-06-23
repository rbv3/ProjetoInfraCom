import time
import http.server
from urllib.parse import urlparse, parse_qs

HOST_NAME = 'localhost' # !!!REMEMBER TO CHANGE THIS!!!
PORT_NUMBER = 8080 # Maybe set this to 9000.


class MyHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(s):
        """Respond to a GET request."""
        query_components = parse_qs(urlparse(s.path).query)
        s.send_response(200)
        s.send_header("Content-type", "text/html")
        s.end_headers()
        s.wfile.write("<html><head><title>Echo</title></head>".encode('utf-8'))
        s.wfile.write(("<body><p>"+str(list(query_components.keys()))+" "+str(list(query_components.values()))+"</p>").encode('utf-8'))
        # If someone went to "http://something.somewhere.net/foo/bar/",
        # then s.path equals "/foo/bar/".
        # s.wfile.write("<p>You accessed path: %s</p>" % s.path)
        s.wfile.write("</body></html>".encode('utf-8'))

if __name__ == '__main__':
    server_class = http.server.HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print ((time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, PORT_NUMBER)))
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print ((time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, PORT_NUMBER)))