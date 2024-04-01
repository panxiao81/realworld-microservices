package top.ddupan.realworld.sensitive;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class SensitiveGrpcService implements SensitiveGrpc {
    private final SensitiveWordBs sensitiveWordBs;

    public SensitiveGrpcService(SensitiveWordBs sensitiveWordBs) {
        this.sensitiveWordBs = sensitiveWordBs;
    }

    @Override
    public Uni<CheckSensitiveReply> checkSensitive(CheckSensitiveRequest request) {
        return Uni.createFrom().item(CheckSensitiveReply.newBuilder())
                .onItem().transform(item -> item.setIsSensitive(sensitiveWordBs.contains(request.getContent())))
                .onItem().transform(item -> item.setContent(sensitiveWordBs.replace(request.getContent())))
                .onItem().transform(CheckSensitiveReply.Builder::build);

    }
}
