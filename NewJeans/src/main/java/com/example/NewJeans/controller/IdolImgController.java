package com.example.NewJeans.controller;


import com.example.NewJeans.dto.request.CreateIdolImgRequestDTO;
import com.example.NewJeans.dto.request.ModifyIdolImgRequestDTO;
import com.example.NewJeans.dto.response.DetailIdolImgResponseDTO;
import com.example.NewJeans.dto.response.ListIdolImgResponseDTO;
import com.example.NewJeans.service.IdolImgService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/membership")
public class IdolImgController {

    private final IdolImgService idolImgService;

    //멤버쉽 이미지 등록
    @PostMapping
    public ResponseEntity<?> postImage(Model model, Authentication authentication,
                                      @Validated @RequestBody CreateIdolImgRequestDTO createIdolImgRequestDTO,
                                      BindingResult result){
//        authentication.getPrincipal(); //나중에 회원 권한 생기면 할것

        //CreateDTO가 잘못 입력된 경우 에러
        if(result.hasErrors()){
            log.warn("createImage 핸들러 메서드 에러발생 : {}", result.getFieldError());
            return ResponseEntity.badRequest().build();
        }

        try {
            DetailIdolImgResponseDTO detailIdolImgResponseDTO = idolImgService.create(createIdolImgRequestDTO);

            return new ResponseEntity<>(
                    detailIdolImgResponseDTO, HttpStatus.CREATED
            );
        } catch (RuntimeException e) {
            log.warn("idolImage POST 에러 : {}",e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //멤버쉽 이미지 보기
    @GetMapping
    public ResponseEntity<?> getImages(Model model, Authentication authentication,
                                       @RequestParam(name = "page", required = false, defaultValue = "1")int page,
                                       @RequestParam(name = "size", required = false, defaultValue = "10")int size,
                                       @RequestParam(name = "sort", required = false, defaultValue = "imgId")String sort){
//        authentication.getPrincipal(); //나중에 회원 권한 생기면 할것
        try {
            ListIdolImgResponseDTO listIdolImgResponseDTO = idolImgService.findIdolImgs(page,size,sort);
            return ResponseEntity.ok().body(listIdolImgResponseDTO);
        } catch (RuntimeException e) {
            log.warn("idolImage GET(리스트) 에러 : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 멤버쉽 이미지 상세 보기
    @GetMapping("/{image-id}")
    public ResponseEntity<?> getImage(Model model,
                           @Positive @PathVariable("image-id") Long imageId,
                           Authentication authentication){
//        authentication.getPrincipal(); //나중에 회원 권한 생기면 할것
        log.info("이미지 상세 보기 실행");


        try {
            DetailIdolImgResponseDTO detailIdolImgResponseDTO = idolImgService.findIdolImg(imageId);
            return ResponseEntity.ok().body(detailIdolImgResponseDTO);
        } catch (RuntimeException e) {
            log.warn("idolImage GET(상세) 에러 : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 관리자가 이미지 수정
    @RequestMapping(value="/{image-id}",method = {RequestMethod.PUT,RequestMethod.PATCH})
    public ResponseEntity<?> patchImage(Model model,
                              @Positive @PathVariable("image-id") Long imageId,
                              @Validated @RequestBody ModifyIdolImgRequestDTO modifyIdolImgRequestDTO,
                              Authentication authentication,
                              BindingResult result){
//        authentication.getPrincipal(); //나중에 회원 권한 생기면 할것

        //ModifyDTO가 잘못 입력된 경우 에러
        if(result.hasErrors()){
            log.warn("updateImage 핸들러 메서드 에러 발생 : {}",result.getFieldError());
            return ResponseEntity.badRequest().build();
        }

        try {
            DetailIdolImgResponseDTO detailIdolImgResponseDTO = idolImgService.update(imageId, modifyIdolImgRequestDTO);
            return ResponseEntity.ok().body(detailIdolImgResponseDTO);
        } catch (RuntimeException e) {
            log.warn("idolImage 수정 에러 : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 관리자가 이미지 삭제
    @DeleteMapping("/{image-id}")
    public ResponseEntity<?> deleteImage(Model model,
                              @Positive @PathVariable("image-id")Long imageId,
                              Authentication authentication){
//        authentication.getPrincipal(); //나중에 회원 권한 생기면 할것

        try {
            DetailIdolImgResponseDTO detailIdolImgResponseDTO = idolImgService.remove(imageId);
            return ResponseEntity.ok().body(detailIdolImgResponseDTO);
        } catch (RuntimeException e) {
            log.warn("idolImage 삭제 에러 : {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
