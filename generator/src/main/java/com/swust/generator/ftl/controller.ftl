package com.swust.${module}.controller.admin;

import com.swust.server.dto.${Domain}Dto;
import com.swust.server.dto.PageDto;
import com.swust.server.dto.ResponseDto;
import com.swust.server.service.${Domain}Service;
import com.swust.server.util.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/admin/${domain}")
public class ${Domain}ManagementController {

    private static final Logger LOG = LoggerFactory.getLogger(${Domain}ManagementController.class);
    public static final String BUSINESS_NAME = "${tableNameCn}";

    @Resource
    private ${Domain}Service ${domain}Service;

    /**
     * 列表查询
     */
    @RequestMapping(value = "/query${domain}info",method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto list(@RequestBody PageDto pageDto) {
        ResponseDto responseDto = new ResponseDto();
        ${domain}Service.list(pageDto);
        responseDto.setContent(pageDto);
        return responseDto;
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    @PostMapping("/save${domain}info")
    public ResponseDto save(@RequestBody ${Domain}Dto ${domain}Dto) {
        // 保存校验
        <#list fieldList as field>
            <#if field.name != "id" && field.nameHump != "createdAt" && field.nameHump != "updatedAt" && field.nameHump != "sort">
                <#if !field.nullAble>
                    ValidatorUtil.require(${domain}Dto.get${field.nameBigHump}(),"${field.nameCn}");
                </#if>
                <#if (field.length > 0)>
                    ValidatorUtil.length(${domain}Dto.get${field.nameBigHump}(),"${field.nameCn}",1,${field.length?c});
                </#if>
            </#if>
        </#list>

        ResponseDto responseDto = new ResponseDto();
        ${domain}Service.save(${domain}Dto);
        responseDto.setContent(${domain}Dto);
        return responseDto;
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete${domain}info/{id}")
    public ResponseDto delete(@PathVariable String id) {
        ResponseDto responseDto = new ResponseDto();
        ${domain}Service.delete(id);
        return responseDto;
    }
}
