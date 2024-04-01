package top.ddupan.realworld.users.application.rest;


import top.ddupan.realworld.users.application.service.UserDto;

public record ProfileResponse(UserDto profile) {
}
