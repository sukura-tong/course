package com.swust.system.controller;

import com.swust.server.dto.ResponseDto;
import com.swust.server.exceptions.BusinessException;
import com.swust.server.exceptions.ValidatorException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * controller异常处理层
 */
@ControllerAdvice
public class SystemControllerExceptionHandler {

    /***
     * 当代码抛出BusinessException异常时，代码自动拦截处理，不需要额外的try-catch
     * @param exception
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public ResponseDto validatorExceptionHandler(BusinessException exception){
        ResponseDto reponseDto = new ResponseDto();
        reponseDto.setSuccess(false);
        reponseDto.setMessage(exception.getCode().getDesc());
        return reponseDto;
    }
}
