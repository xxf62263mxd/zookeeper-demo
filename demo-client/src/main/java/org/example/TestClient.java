package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * 测试服务器，监听注册中心中的服务器列表
 */
@Slf4j
public class TestClient {

    private static ZooKeeper zkCli = null;
    private static List<String>  serverList = null;
    public static void main(String[] args) throws Exception {
        //创建客户端,并连接到Zookeeper
        TestClient application =  new TestClient();

        log.info("正在连接至Zookeeper中...");
        zkCli = application.getConnect("127.0.0.1:2181");
        log.info("连接至Zookeeper:"+zkCli);

        //监听服务器列表节点
        log.info("开始监视节点");
        zkCli.getChildren("/server",true);
        //业务逻辑，这里用打印服务器列表代替
        Thread.sleep(Long.MAX_VALUE);
    }



    /**
     * //连接到Zookeeper集群
     * @return
     * @throws IOException
     */
    public ZooKeeper getConnect(String connectString) throws IOException {
        return new ZooKeeper(connectString, 2000, new Watcher() {
            /**
             * listener线程的回调函数，在被监听的节点或其子节点发生变化时触发
             * @param watchedEvent
             */
            @Override
            public void process(WatchedEvent watchedEvent) {
                //重新监听服务器列表，并打印变化后的服务器列表
                try {
                    serverList = zkCli.getChildren("/server",true);
                    printList(serverList);
                    log.info("重新开始监视节点");
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private static void printList(List<String> list){
        if(list !=null && !list.isEmpty())
        for(String elem : list){
            log.info(elem);
        }
    }

}
