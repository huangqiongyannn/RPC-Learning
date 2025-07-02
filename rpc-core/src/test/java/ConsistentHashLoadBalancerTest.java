

import com.hqy.loadBalance.impl.ConsistentHashLoadBalancer;
import com.hqy.register.impl.ZKServiceRegister;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ConsistentHashLoadBalancerTest {

    private ConsistentHashLoadBalancer loadBalancer;
    private List<String> nodes = Arrays.asList("127.0.0.1:8001", "127.0.0.1:8002", "127.0.0.1:8003");
    private ZKServiceRegister register = ZKServiceRegister.getInstance();
    private String serviceName = "com.hqy.api.OrderService";

    @BeforeEach
    public void setUp() {
        loadBalancer = new ConsistentHashLoadBalancer();
    }

    @Test
    public void testInitAndSelect() {
        for (String node: nodes) {
            register.registry(serviceName, node);
        }

        register.print(serviceName);

        // 第一次调用会初始化
        String selected1 = loadBalancer.select("requestA", serviceName, nodes);
        String selected2 = loadBalancer.select("requestB", serviceName, nodes);
        String selected3 = loadBalancer.select("requestC", serviceName, nodes);

        System.out.println("Request A -> " + selected1);
        System.out.println("Request B -> " + selected2);
        System.out.println("Request C -> " + selected3);

        assertNotNull(selected1);
        assertNotNull(selected2);
        assertNotNull(selected3);
    }

    @Test
    public void testAddNode() {
        testInitAndSelect();
        // 模拟添加新节点
        String newNodePath = "127.0.0.1:8888";
        register.registry(serviceName, newNodePath);
        register.print(serviceName);
//        ChildData newNode = new ChildData("/" + serviceName + "/" + newNodePath, null, null);
//        loadBalancer.addNode(serviceName, newNode);
        String request = "request K";
        // 重新选择节点看看结果是否包含新节点
        String selected = loadBalancer.select(request, serviceName, nodes);
        System.out.println(request + " after adding node -> " + selected);
        assertNotNull(selected);
    }

    @Test
    public void testDelNode() {
        String removePath = "127.0.0.1:8002";
        register.offline(serviceName, removePath);
//        testInitAndSelect();
//        register.subscribeWatcherForCHLoadBalancer(serviceName);
        // 模拟删除节点
        register.print(serviceName);
//        Thread.sleep(3000);
//        ChildData removeNode = new ChildData(removePath, null, null);
//        loadBalancer.delNode(removeNode);
//        Thread.sleep(5000);
        String selected = loadBalancer.select("requestA", serviceName, nodes);
        System.out.println("Request A after deleting node -> " + selected);
//        assertNotEquals("127.0.0.1:8002", selected); // 最好不是删除的节点
    }

    @Test
    public void testHashDistribution() {
        testInitAndSelect();

        Map<String, Integer> distribution = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String reqKey = "request-" + i;
            String selected = loadBalancer.select(reqKey, nodes);
            distribution.put(selected, distribution.getOrDefault(selected, 0) + 1);
        }

        System.out.println("请求分布情况: " + distribution);
        assertEquals(3, distribution.size());
    }
}
