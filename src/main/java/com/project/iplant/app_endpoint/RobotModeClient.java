package com.project.iplant.app_endpoint;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Basic Echo Client Socket
 */
@WebSocket(maxTextMessageSize = 1024 * 1024)
public class RobotModeClient
{
    private final CountDownLatch closeLatch;
    @SuppressWarnings("unused")
    private Session session;

    public RobotModeClient()
    {
        this.closeLatch = new CountDownLatch(1);
    }


    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException
    {
        return this.closeLatch.await(duration, unit);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown(); // trigger latch
    }

    @OnWebSocketConnect
    public synchronized void onConnect(Session session)
    {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
        notifyAll();
        /*
        try
        {
            Future<Void> fut;
            fut = session.getRemote().sendStringByFuture("Hello");
            fut.get(2, TimeUnit.SECONDS); // wait for send to complete.

            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
            fut.get(2, TimeUnit.SECONDS); // wait for send to complete.
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }

         */
    }


    public void onMessage(String msg)
    {

        System.out.printf("Got msg: %s%n", msg);
        if (msg.contains("Thanks"))
        {
            session.close(StatusCode.NORMAL, "I'm done");
        }
    }

    @OnWebSocketError
    public void onError(Throwable cause)
    {
        System.out.print("WebSocket Error: ");
        cause.printStackTrace(System.out);
    }

    public synchronized void sendMessage(String str) {
        try {
            wait();
            session.getRemote().sendString(str);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}