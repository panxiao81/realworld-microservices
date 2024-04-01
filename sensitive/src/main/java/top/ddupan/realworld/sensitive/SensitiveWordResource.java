package top.ddupan.realworld.sensitive;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;

@Path("/api/v1/sensitive-check")
public class SensitiveWordResource {
    private final SensitiveWordBs sensitiveWordBs;

    public SensitiveWordResource(SensitiveWordBs sensitiveWordBs) {
        this.sensitiveWordBs = sensitiveWordBs;
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    public SensitiveWordResult checkSensitive(SensitiveWordRequest request) {
        return new SensitiveWordResult(sensitiveWordBs.contains(request.content()), sensitiveWordBs.replace(request.content()));
    }
}
