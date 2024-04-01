package top.ddupan.realworld.users.infrastructure.user;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.UUID;

@HttpExchange
interface UserInfoService {
    @GetExchange
    UserinfoVO getUserinfo(UUID uuid);

    @GetExchange
    UserinfoVO getUserinfoByUsername(String username);
}
