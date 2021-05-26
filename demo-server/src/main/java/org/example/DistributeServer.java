package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * 分布式服务器例子
 */
@Slf4j
public class DistributeServer {

    private static ZooKeeper zkCli = null;

    public static void main(String[] args) throws Exception{
        //创建服务器,并连接到Zookeeper
        DistributeServer application =  new DistributeServer();
        log.info("正在连接至Zookeeper中...");
        zkCli = application.getConnect("127.0.0.1:2181");
        log.info("连接至Zookeeper:"+zkCli);

        //创建服务器节点 路径 数据 权限 节点类型(临时/编号)
        zkCli.create("/server/instance","instance".getBytes()
                , ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        //业务逻辑，这里用Sleep代替
        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * //连接到Zookeeper集群
     * @return
     * @throws IOException
     */
    public ZooKeeper getConnect(String connectString) throws IOException {
        return new ZooKeeper(connectString, 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {

            }
        });
    }


}
