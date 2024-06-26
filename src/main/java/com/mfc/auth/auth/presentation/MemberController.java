package com.mfc.auth.auth.presentation;

import static com.mfc.auth.common.response.BaseResponseStatus.*;

import org.modelmapper.ModelMapper;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mfc.auth.auth.application.MemberService;
import com.mfc.auth.auth.dto.resp.AuthInfoResponse;
import com.mfc.auth.auth.dto.req.ModifyPasswordReqDto;
import com.mfc.auth.auth.vo.req.ModifyPasswordReqVo;
import com.mfc.auth.auth.vo.resp.MemberNameRespVo;
import com.mfc.auth.common.exception.BaseException;
import com.mfc.auth.common.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "members", description = "회원 서비스 컨트롤러 (토큰 필요)")
public class MemberController {
	private final MemberService memberService;
	private final ModelMapper modelMapper;

	@PostMapping("/resign")
	@Operation(summary = "회원 탈퇴 API", description = "유저/파트너 한 번에 탈퇴 (삭제)")
	public BaseResponse<Void> resign(@RequestHeader(value = "UUID", defaultValue = "") String uuid) {
		if(!StringUtils.hasText(uuid)) {
			throw new BaseException(NO_REQUIRED_HEADER);
		}

		memberService.resign(uuid);
		return new BaseResponse<>();
	}

	@PutMapping("/password")
	@Operation(summary = "비밀번호 수정 API", description = "유저/파트너 공통 적용")
	public BaseResponse<Void> modifyPassword(
			@RequestHeader(name = "UUID", defaultValue = "") String uuid,
			@RequestBody @Validated ModifyPasswordReqVo vo) {

		if(!StringUtils.hasText(uuid)) {
			throw new BaseException(NO_REQUIRED_HEADER);
		}

		memberService.modifyPassword(uuid, modelMapper.map(vo, ModifyPasswordReqDto.class));
		return new BaseResponse<>();
	}

	@GetMapping("/name")
	@Operation(summary = "회원 이름 조회 API", description = "회원 이름(실명) 조회")
	public BaseResponse<MemberNameRespVo> getName(
			@RequestHeader(name = "UUID", defaultValue = "") String uuid) {
		return new BaseResponse<>(modelMapper.map(
				memberService.getName(uuid), MemberNameRespVo.class));
	}

	@GetMapping("/birth-gender/{uuid}")
	@Operation(summary = "회원 나이성(성별) 조회 API", description = "회원 나이성(성별) 조회")
	public BaseResponse<AuthInfoResponse> getBirthGender(
			@PathVariable("uuid") String uuid) {
		return new BaseResponse<>(modelMapper.map(
				memberService.getBirthGender(uuid), AuthInfoResponse.class));
	}
}
