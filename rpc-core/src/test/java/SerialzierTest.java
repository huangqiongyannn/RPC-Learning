import com.hqy.pojo.User;
import com.hqy.serializer.impl.HessionSerializer;
import com.hqy.serializer.impl.JsonSerializer;
import com.hqy.serializer.impl.KryoSerializer;
import com.hqy.serializer.impl.ProtostuffSerialzier;
import org.junit.jupiter.api.Test;

public class SerialzierTest {

    @Test
    public void testSerializeDeserialize() {
        User user = new User("Alice", 30);

        // Protostuff
        ProtostuffSerialzier protostuff = new ProtostuffSerialzier();
        byte[] pBytes = protostuff.serialize(user);
        User pUser = protostuff.deserialize(pBytes, User.class);
        System.out.println("[Protostuff] 字节长度: " + pBytes.length);
        System.out.println("[Protostuff] 反序列化对象: " + pUser);

        // JSON
        JsonSerializer json = new JsonSerializer();
        byte[] jBytes = json.serialize(user);
        User jUser = json.deserialize(jBytes, User.class);
        System.out.println("[JSON] 字节长度: " + jBytes.length);
        System.out.println("[JSON] 反序列化对象: " + jUser);

        // Kryo
        KryoSerializer kryo = new KryoSerializer();
        byte[] kBytes = kryo.serialize(user);
        User kUser = kryo.deserialize(kBytes, User.class);
        System.out.println("[Kryo] 字节长度: " + kBytes.length);
        System.out.println("[Kryo] 反序列化对象: " + kUser);

        // Hessian
        HessionSerializer hessian = new HessionSerializer();
        byte[] hBytes = hessian.serialize(user);
        User hUser = hessian.deserialize(hBytes, User.class);
        System.out.println("[Hessian] 字节长度: " + hBytes.length);
        System.out.println("[Hessian] 反序列化对象: " + hUser);
    }
}
