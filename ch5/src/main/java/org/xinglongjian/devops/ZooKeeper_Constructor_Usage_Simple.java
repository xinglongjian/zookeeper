package org.xinglongjian.devops;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

/**
 * 创建会话,实例化一个ZooKeeper对象，建立会话。
 *
 */
public class ZooKeeper_Constructor_Usage_Simple implements Watcher
{
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main( String[] args ) throws Exception {
        ZooKeeper zooKeeper =
                new ZooKeeper(
                        "192.168.117.128:2181,192.168.117.128:2182,192.168.117.128:2183",
                        5000,
                        new ZooKeeper_Constructor_Usage_Simple());
        System.out.println(zooKeeper.getState());
        try{
            connectedSemaphore.await();
        }catch (InterruptedException e) {}
        System.out.println(zooKeeper.getState());
        System.out.println("ZooKeeper session established!");

    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        System.out.println("Receive watched event:"+watchedEvent);
        if(Event.KeeperState.SyncConnected == watchedEvent.getState()) {
            connectedSemaphore.countDown();
        }
    }
}
