package top.ddupan.realworld;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import top.ddupan.realworld.sensitive.HelloGrpc;
import top.ddupan.realworld.sensitive.HelloReply;
import top.ddupan.realworld.sensitive.HelloRequest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class HelloGrpcServiceTest {
    @GrpcClient
    HelloGrpc helloGrpc;

    @Test
    void testHello() {
        HelloReply reply = helloGrpc
                .sayHello(HelloRequest.newBuilder().setName("Neo").build()).await().atMost(Duration.ofSeconds(5));
        assertEquals("Hello Neo!", reply.getMessage());
    }

}
