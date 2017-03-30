package sample;

import java.io.IOException;
import java.net.*;

/**
 * Created by gsamadova on 3/11/2016.
 */
public class NetworkChecker implements Runnable {
    static int count=0;
    @Override
    public void run() {
        SocketAddress prodAddr = new InetSocketAddress("10.25.0.95",1526);
        SocketAddress testAddr = new InetSocketAddress("10.25.1.136",1526);
        Socket prodSocket;
        Socket testSocket;



        while(Utils.isStageOpen) {
            //System.out.println(count++);
            prodSocket=new Socket();
            testSocket=new Socket();

            try {
                prodSocket.connect(prodAddr,8);
                testSocket.connect(testAddr, 8);
                Utils.online=true;
            } catch(IOException ex) {
                System.out.println(ex.toString());
                if (!ex.toString().contains("connect timed out")) Utils.online=false;
            }finally {
                try {
                    prodSocket.close();
                    testSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!Utils.online){
                System.out.println("OFFLINE: Reconnecting..");
            } else {
            }

            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
