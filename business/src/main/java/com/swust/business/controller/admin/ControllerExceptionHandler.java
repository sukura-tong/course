package com.swust.business.controller.admin;

import com.swust.server.dto.ResponseDto;
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
public class ControllerExceptionHandler {

    /***
     * 当代码抛出ValException异常时，代码自动拦截处理，不需要额外的try-catch
     * @param exception
     * @return
     */
    @ExceptionHandler(value = ValidatorException.class)
    @ResponseBody
    public ResponseDto validatorExceptionHandler(ValidatorException exception){
        ResponseDto reponseDto = new ResponseDto();
        reponseDto.setSuccess(false);
        reponseDto.setMessage(exception.getMessage());
        return reponseDto;
    }
}
