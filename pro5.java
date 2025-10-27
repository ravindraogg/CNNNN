Sender Side (slidsender.java)

import java.net.*;
import java.io.*;

public class slidsender {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(10);
        Socket s = server.accept();
        DataInputStream inConsole = new DataInputStream(System.in);
        DataInputStream inSocket = new DataInputStream(s.getInputStream());
        PrintStream p = new PrintStream(s.getOutputStream());

        String sbuff[] = new String[8];
        int sptr = 0, sws = 8, nf, ackNo, i;
        String ch;

        do {
            System.out.print("Enter the number of frames: ");
            nf = Integer.parseInt(inConsole.readLine());
            p.println(nf);

            if (nf <= sws - 1) {
                System.out.println("Enter " + nf + " messages to be sent:");
                for (i = 1; i <= nf; i++) {
                    sbuff[sptr] = inConsole.readLine();
                    p.println(sbuff[sptr]);
                    sptr = ++sptr % 8;
                }
                sws -= nf;

                System.out.print("Acknowledgment received ");
                ackNo = Integer.parseInt(inSocket.readLine());
                System.out.println("for " + ackNo + " frames");
                sws += nf;
            } else {
                System.out.println("The number of frames exceeds window size");
                break;
            }

            System.out.print("\nDo you want to send more frames? (yes/no): ");
            ch = inConsole.readLine();
            p.println(ch);

        } while (ch.equals("yes"));

        s.close();
        server.close();
    }
}


Receiver Side (slidreceiver.java)

import java.net.*;
import java.io.*;

class slidreceiver {
    public static void main(String[] args) throws Exception {
        Socket s = new Socket(InetAddress.getLocalHost(), 10);
        DataInputStream inSocket = new DataInputStream(s.getInputStream());
        PrintStream p = new PrintStream(s.getOutputStream());

        int rptr = -1, nf, rws = 8;
        String rbuf[] = new String[8];
        String ch;

        do {
            nf = Integer.parseInt(inSocket.readLine());

            if (nf <= rws - 1) {
                for (int i = 1; i <= nf; i++) {
                    rptr = ++rptr % 8;
                    rbuf[rptr] = inSocket.readLine();
                    System.out.println("The received Frame " + rptr + " is: " + rbuf[rptr]);
                }

                rws -= nf;
                System.out.println("\nAcknowledgment sent\n");
                p.println(rptr + 1);
                rws += nf;
            } else {
                break;
            }

            ch = inSocket.readLine();
        } while (ch.equals("yes"));

        s.close();
    }
}
