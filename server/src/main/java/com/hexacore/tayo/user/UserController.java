package com.hexacore.tayo.user;

import com.hexacore.tayo.car.dto.GetCarResponseDto;
import com.hexacore.tayo.common.response.Response;
import com.hexacore.tayo.user.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 내 정보 조회
    @GetMapping
    public ResponseEntity<Response> getMyInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        GetUserInfoResponseDto userInfoDto = userService.getUser(userId);
        return Response.of(HttpStatus.OK, userInfoDto);
    }

    // 유저 정보 조회
    @GetMapping("/{userId}")
    public ResponseEntity<Response> getUserInfo(HttpServletRequest request,
            @PathVariable Long userId) {
        GetUserInfoResponseDto userInfoDto = userService.getUser(userId);
        return Response.of(HttpStatus.OK, userInfoDto);
    }

    // 유저 정보 수정
    @PatchMapping
    public ResponseEntity<Response> updateUser(HttpServletRequest request,
            @ModelAttribute UpdateUserRequestDto updateRequestDto) {
        userService.update((Long) request.getAttribute("userId"), updateRequestDto);

        return Response.of(HttpStatus.OK);
    }

    // TODO: #138 이슈
//    @GetMapping("/cars")
//    public ResponseEntity<Response> getUserCar(HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        GetCarResponseDto getCarResponseDto = userService.getUserCar(userId);
//        return Response.of(HttpStatus.OK, getCarResponseDto);
//    }
}
