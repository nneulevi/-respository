package org.example.project2.Controller;

import org.apache.ibatis.annotations.Param;
import org.example.project2.Mapper.NewsMapper;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.enterpriseMapper;
import org.example.project2.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class UserController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    enterpriseMapper enterpriseMapper;

    @RequestMapping("/login")
    public boolean check(@RequestBody User_d user){
        if(user.getStatus()==0){
            Enterprise enterprise1 = enterpriseMapper.findByid(user.getEnterprise());
            if(enterprise1==null) return false;
            if(enterprise1.getAudiStatus()==0) return false;
        }
        User_d u=userMapper.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if(u==null) return false;
        return true;
    }

    @RequestMapping("/adduser")//传User_d的九个属性
    public ResponseEntity<Integer> Add(@RequestBody User_d user){
        int result = userMapper.insert(user);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/getUserinfo")
    public User get(@Param("username") String username){
        User_d user = userMapper.findByUsername(username);
        List<News> list = newsMapper.selectByauthor(username);
        return new User(
                user.getUsername(),
                user.getPassword(),
                user.getPhone(),
                user.getEmail(),
                user.getGender(),
                user.getStatus(),
                user.getCreatTime(),
                user.getAvatar_url(),
                user.getEnterprise(),
                list
        );
    }

    public PageResult<User_d> search(int pageNum, int pageSize, String username, String phone, String email, String gender, Date startDate, Date endDate){
        int offset = (pageNum - 1) * pageSize;
        List<User_d> users = userMapper.findByKeyword(username, phone, email, gender, startDate, endDate, offset, pageSize);
        long total = userMapper.count(username,phone,email,gender,startDate,endDate);
        return new PageResult<>(pageNum, pageSize, total, users);
    }

    @RequestMapping("/getUserlist")
    public ResponseEntity<PageResult<User_d>> get(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Date startDate,
            @RequestParam(required = false) Date endDate
    ){
        PageResult<User_d> result = search(pageNum, pageSize, username, phone, email, gender, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/changeUserinfo")//当前登录中的人，要修改的人，修改信息
    public ResponseEntity<Integer> change(@RequestParam(required = false) String now_username, @RequestParam(required = false) String username, @RequestBody User_d user){
        User_d now = userMapper.findByUsername(now_username);
        int id = now.getStatus();
        if(id==0 && !now_username.equals(username)) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(-1);
        User_d old = userMapper.findByUsername(username);
        if(user.getUsername()!=null) old.setUsername(user.getUsername());
        if(user.getPassword()!=null) old.setPassword(user.getPassword());
        if(user.getEmail()!=null) old.setEmail(user.getEmail());
        if(user.getPhone()!=null) old.setPhone(user.getPhone());
        if(user.getAvatar_url()!=null) old.setAvatar_url(user.getAvatar_url());
        if(id==1 && user.getStatus()!=null) old.setStatus(user.getStatus());
        if(user.getEnterprise()!=null) old.setEnterprise(user.getEnterprise());
        int result = userMapper.change_info(old,username);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }



}
