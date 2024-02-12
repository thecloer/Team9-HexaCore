package com.hexacore.tayo.user;

import com.hexacore.tayo.common.DataResponseDto;
import com.hexacore.tayo.common.ResponseDto;
import com.hexacore.tayo.user.dto.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Value("${jwt.refresh.cookie-name}")
    private String refreshTokenCookieName;

    @Value("${jwt.access.cookie-name}")
    private String accessTokenCookieName;

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signUp(@ModelAttribute SignUpRequestDto signUpRequestDto) {
        ResponseDto response = userService.signUp(signUpRequestDto);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    // 유저 정보 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseDto> getUserInfo(@PathVariable Long userId) {
        UserInfoResponseDto userInfoDto = userService.getUser(userId);

        ResponseDto responseDto = DataResponseDto.of(userInfoDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 유저 정보 수정
    @PutMapping("/users")
    public ResponseEntity<ResponseDto> updateUser(HttpServletRequest request, @ModelAttribute UserUpdateRequestDto updateRequestDto) {
        ResponseDto response = userService.update((Long) request.getAttribute("userId"), updateRequestDto);

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getCode()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        LoginResponseDto loginResponseDto = userService.login(loginRequestDto);

        response.addCookie(makeTokenCookie(accessTokenCookieName, loginResponseDto.getAccessToken(), "/"));
        response.addCookie(makeTokenCookie(refreshTokenCookieName, loginResponseDto.getRefreshToken(), "/refresh"));

        ResponseDto responseDto = DataResponseDto.of(loginResponseDto.getLoginUserInfo());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // 로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Void> logOut(HttpServletRequest request, HttpServletResponse response) {
        userService.logOut((Long) request.getAttribute("userId"));
        resetCookie(response);

        return ResponseEntity.ok().build();
    }

    // 회원 탈퇴
    @DeleteMapping("/users")
    public ResponseEntity<ResponseDto> deleteUser(HttpServletRequest request, HttpServletResponse response) {
        userService.delete((Long) request.getAttribute("userId"));
        resetCookie(response);

        return ResponseEntity.ok().build();
    }

    // 엑세스 토큰 재발급
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {

        String newAccessToken = userService.refresh((Long) request.getAttribute("userId"));
        response.addCookie(makeTokenCookie(accessTokenCookieName, newAccessToken, "/"));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login/test")
    public ResponseEntity<String[]> loginTest(HttpServletRequest request) {
        String[] arr = new String[2];
        arr[0] = String.valueOf(request.getAttribute("userId"));
        arr[1] = (String) request.getAttribute("userName");

        return new ResponseEntity<>(arr, HttpStatusCode.valueOf(200));
    }

    private Cookie makeTokenCookie(String cookieName, String token, String path) {
        Cookie tokenCookie = new Cookie(cookieName, token);
        tokenCookie.setPath(path);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(true);

        return tokenCookie;
    }

    private void resetCookie(HttpServletResponse response) {
        Cookie accessToken = makeTokenCookie(accessTokenCookieName, "", "/");
        accessToken.setMaxAge(0);
        Cookie refreshToken = makeTokenCookie(refreshTokenCookieName, "", "/refresh");
        refreshToken.setMaxAge(0);

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }
}
