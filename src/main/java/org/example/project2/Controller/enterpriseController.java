package org.example.project2.Controller;

import org.example.project2.Mapper.enterpriseMapper;
import org.example.project2.entity.Enterprise;
import org.example.project2.entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class enterpriseController {
    @Autowired
    enterpriseMapper enterpriseMapper;

    @RequestMapping("/Register")//传所有公司的属性
    public ResponseEntity<Integer> Add(@RequestBody Enterprise enterprise) {
        enterprise.setAudiStatus(1);//参数按enterprise的属性传，audisstatus的位置设置为0即可
        int result = enterpriseMapper.insert(enterprise);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }


    public PageResult<Enterprise> search(int pageNum, int pageSize, Long id, String name, String phone,String licenseNumber,String contactPerson,String contactPhone) {
        int offset = (pageNum - 1) * pageSize;

        List<Enterprise> enterprises = enterpriseMapper.getAllEnterprise(id,name,phone,licenseNumber,contactPerson,contactPhone,offset, pageSize);
        Long total = enterpriseMapper.count(id,name,phone,licenseNumber,contactPerson,contactPhone);

        return new PageResult<>(pageNum, pageSize, total, enterprises);
    }

    @RequestMapping("/Enterprises")//查找公司的分页与模糊查询，注意参数顺序
    //@Cacheable(value = "contests", keyGenerator = "submissionKeyGenerator")
    public ResponseEntity<PageResult<Enterprise>> get(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String licenseNumber,
            @RequestParam(required = false) String contactPerson,
            @RequestParam(required = false) String contactPhone
    ) {

        PageResult<Enterprise> result = search(pageNum, pageSize,id,name,phone, licenseNumber,contactPerson,contactPhone);
        return ResponseEntity.ok(result);
    }
}
