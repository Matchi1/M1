package fr.uge.net.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;

public class HTTPReader {

    private final Charset ASCII_CHARSET = Charset.forName("ASCII");
    private final SocketChannel sc;
    private final ByteBuffer buffer;

    public HTTPReader(SocketChannel sc, ByteBuffer buffer) {
        this.sc = sc;
        this.buffer = buffer;
    }

    private boolean isFinished(ByteBuffer bb) {
        var base = bb.position();
        var position = bb.limit() - 2;
        bb.position(position);
        var letter = (char) bb.get();
        if(letter == '\r') {
            letter = (char) bb.get();
            if(letter == '\n') {
                bb.position(base);
                return true;
            }
        }
        return false;
    }

    /**
     * @return The ASCII string terminated by CRLF without the CRLF
     *         <p>
     *         The method assume that buffer is in write mode and leaves it in
     *         write mode The method process the data from the buffer and if necessary
     *         will read more data from the socket.
     * @throws IOException HTTPException if the connection is closed before a line
     *                     could be read
     */
    public String readLineCRLF() throws IOException {
        var chariot = false;
        var line = new StringBuilder();
        char letter1 = 0;
        char letter2 = 0;
        buffer.flip();
        // Read buffer until \r\n
        while(true) {
            letter2 = 0;
            // verify buffer state
            if(!buffer.hasRemaining()) {
                buffer.compact();
                if(sc.read(buffer) == -1) {
                    throw new HTTPException();
                }
                buffer.flip();
            }
            if(chariot) {
                letter2 = (char) buffer.get();
                if (letter2 == '\n') {
                    break;
                } else {
                    line.append(letter1);
                    line.append(letter2);
                    chariot = false;
                    continue;
                }
            } else {
                letter1 = (char) buffer.get();
            }

            if (letter1 == '\r') {
                if(buffer.hasRemaining()) {
                    letter2 = (char) buffer.get();
                    if(letter2 == '\n') {
                        break;
                    }
                } else {
                    chariot = true;
                    continue;
                }
            }
            line.append(letter1);
            if(letter2 != 0) {
                line.append(letter2);
            }
        }
        buffer.compact();
        return line.toString();
    }

    /**
     * @return The HTTPHeader object corresponding to the header read
     * @throws IOException HTTPException if the connection is closed before a header
     *                     could be read or if the header is ill-formed
     */
    public HTTPHeader readHeader() throws IOException {
        var response = readLineCRLF();
        var map = new HashMap<String, String>();
        while(true) {
            var line = readLineCRLF();
            if(line == "") {
                break;
            }
            var pair = line.split(":");
            var value = map.get(pair[0]);
            // TODO: verify pair length
            if(value != null) {
                map.put(pair[0], value + ";" + pair[1]);
            } else {
                map.put(pair[0], pair[1]);
            }

        }
        return HTTPHeader.create(response, map);
    }

    /**
     * @param size 
     * @return a ByteBuffer in write mode containing size bytes read on the socket
     *         <p>
     *         The method assume that buffer is in write mode and leaves it in
     *         write mode The method process the data from the buffer and if necessary
     *         will read more data from the socket.
     * @throws IOException HTTPException is the connection is closed before all
     *                     bytes could be read
     */
    public ByteBuffer readBytes(int size) throws IOException {
        var bb = ByteBuffer.allocateDirect(size);
        if(sc.read(bb) == -1) {
            throw new HTTPException();
        }
        bb.flip();
        return bb;
    }

    /**
     * @return a ByteBuffer in write-mode containing a content read in chunks mode
     * @throws IOException HTTPException if the connection is closed before the end
     *                     of the chunks if chunks are ill-formed
     */

    public ByteBuffer readChunks() throws IOException {
        // TODO
        return null;
    }

    public static void main(String[] args) throws IOException {
        var charsetASCII = Charset.forName("ASCII");
        var request = "GET / HTTP/1.1\r\n" + "Host: www.w3.org\r\n" + "\r\n";
        var sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("www.w3.org", 80));
        sc.write(charsetASCII.encode(request));
        var buffer = ByteBuffer.allocate(50);
        var reader = new HTTPReader(sc, buffer);
        System.out.println(reader.readLineCRLF());
        System.out.println(reader.readLineCRLF());
        System.out.println(reader.readLineCRLF());
        sc.close();

        buffer = ByteBuffer.allocate(50);
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("www.w3.org", 80));
        reader = new HTTPReader(sc, buffer);
        sc.write(charsetASCII.encode(request));
        System.out.println(reader.readHeader());
        sc.close();

        buffer = ByteBuffer.allocate(50);
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("www.w3.org", 80));
        reader = new HTTPReader(sc, buffer);
        sc.write(charsetASCII.encode(request));
        var header = reader.readHeader();
        System.out.println(header);
        var content = reader.readBytes(header.getContentLength());
        content.flip();
        System.out.println(header.getCharset().orElse(Charset.forName("UTF8")).decode(content));
        sc.close();

        buffer = ByteBuffer.allocate(50);
        request = "GET / HTTP/1.1\r\n" + "Host: www.u-pem.fr\r\n" + "\r\n";
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("www.u-pem.fr", 80));
        reader = new HTTPReader(sc, buffer);
        sc.write(charsetASCII.encode(request));
        header = reader.readHeader();
        System.out.println(header);
        content = reader.readChunks();
        content.flip();
        System.out.println(header.getCharset().orElse(Charset.forName("UTF8")).decode(content));
        sc.close();
    }
}