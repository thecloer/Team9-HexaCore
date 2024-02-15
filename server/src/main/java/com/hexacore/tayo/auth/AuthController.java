package com.hexacore.tayo.auth;

import com.hexacore.tayo.common.UriPath;
import com.hexacore.tayo.common.errors.ErrorCode;
import com.hexacore.tayo.common.errors.GeneralException;
import com.hexacore.tayo.common.response.Response;
import com.hexacore.tayo.user.dto.LoginRequestDto;
import com.hexacore.tayo.user.dto.LoginResponseDto;
import com.hexacore.tayo.user.dto.SignUpRequestDto;
import com.hexacore.tayo.user.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Response> signUp(@ModelAttribute SignUpRequestDto signUpRequestDto) {
        User user = authService.signUp(signUpRequestDto);

        if (user == null) {
            throw new GeneralException(ErrorCode.SERVER_ERROR);
        }

        return Response.of(HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);

        return Response.of(HttpStatus.OK, loginResponseDto);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Response> logOut(HttpServletRequest request, HttpServletResponse response) {
        authService.logOut((Long) request.getAttribute(USER_ID));

        return Response.of(HttpStatus.OK);
    }

    // 회원 탈퇴
    @DeleteMapping("/users")
    public ResponseEntity<Response> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        authService.delete((Long) request.getAttribute(USER_ID));

        return Response.of(HttpStatus.OK);
    }

    // 엑세스 토큰 재발급
    @GetMapping("/refresh")
    public ResponseEntity<Response> refresh(HttpServletRequest request, HttpServletResponse response) {

        String newAccessToken = authService.refresh((Long) request.getAttribute(USER_ID));

        return Response.of(HttpStatus.OK, newAccessToken);
    }

    @GetMapping("/login/test")
    public ResponseEntity<Response> loginTest(HttpServletRequest request) {
        String[] arr = new String[2];
        arr[0] = String.valueOf(request.getAttribute(USER_ID));
        arr[1] = (String) request.getAttribute(USER_NAME);

        return Response.of(HttpStatus.OK, arr);
    }
}
