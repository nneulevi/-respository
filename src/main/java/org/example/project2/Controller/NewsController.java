package org.example.project2.Controller;

import org.example.project2.Mapper.NewsMapper;
import org.example.project2.Mapper.UserMapper;
import org.example.project2.Mapper.comentsMapper;
import org.example.project2.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin
public class NewsController {
    @Autowired
    NewsMapper newsMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    comentsMapper comentsMapper;
    @RequestMapping("/submit")//基于普通用户，插到ready_news中，传news的所有属性，下同
    public ResponseEntity<Integer> Add(@RequestBody News news){
        int result = newsMapper.submit(news);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/addNews")//基于管理员，通过审核或者管理员发布
    public ResponseEntity<Integer> Insert(@RequestBody News news){
        int result = newsMapper.insert(news);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    public PageResult<News> search(int pageNum, int pageSize, Long id, String title, String summary, String content, String coverImage, String author, String tags, LocalDateTime startDate, LocalDateTime endDate) {
        int offset = (pageNum - 1) * pageSize;
        List<News> news = newsMapper.findNewsByKeyword(id, title, summary, content, coverImage, author, tags, startDate, endDate, offset, pageSize);
        Long total = newsMapper.count(id,title,summary,content,coverImage,author,tags,startDate,endDate);
        return new PageResult<>(pageNum,pageSize,total,news);
    }

    public PageResult<News> searchByTime(int pageNum, int pageSize, Long id, String title, String summary, String content, String coverImage, String author, String tags, LocalDateTime startDate, LocalDateTime endDate) {
        int offset = (pageNum - 1) * pageSize;
        List<News> news = newsMapper.findNewsByKeyword(id, title, summary, content, coverImage, author, tags, startDate, endDate, offset, pageSize);
        Long total = newsMapper.count(id,title,summary,content,coverImage,author,tags,startDate,endDate);
        news.sort((a, b) -> b.getPublishTime().compareTo(a.getPublishTime()));
        return new PageResult<>(pageNum,pageSize,total,news);
    }

    public PageResult<News> searchBycount(int pageNum, int pageSize, Long id, String title, String summary, String content, String coverImage, String author, String tags, LocalDateTime startDate, LocalDateTime endDate) {
        int offset = (pageNum - 1) * pageSize;
        List<News> news = newsMapper.findNewsByKeyword(id, title, summary, content, coverImage, author, tags, startDate, endDate, offset, pageSize);
        Long total = newsMapper.count(id,title,summary,content,coverImage,author,tags,startDate,endDate);
        news.sort((a, b) -> b.getViewCount().compareTo(a.getViewCount()));
        return new PageResult<>(pageNum,pageSize,total,news);
    }

    @RequestMapping("/getNews")//同样模糊查询
    public ResponseEntity<PageResult<News>> get(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
     ){
        PageResult<News> result = search(pageNum, pageSize, id, title, summary, content, coverImage, author, tags, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/get_by_time")//支持按时间排序，模糊查询，注意参数顺序
    public ResponseEntity<PageResult<News>> get_by_time(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        PageResult<News> result = searchByTime(pageNum, pageSize, id, title, summary, content, coverImage, author, tags, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/get_by_count")//按点击量排序
    public ResponseEntity<PageResult<News>> get_by_count(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String summary,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String coverImage,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String tags,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        PageResult<News> result = searchBycount(pageNum, pageSize, id, title, summary, content, coverImage, author, tags, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/get_newsdetail")//点击标题后展示具体内容，参数为新闻标题，新增评论,加载时获得基础用户信息，随后调用coments相关接口加载评论
    public ResponseEntity<News> getDetail(@RequestParam(required = false) String title){
        News news = newsMapper.selectBytitle(title);
        return ResponseEntity.ok(news);
    }

    @RequestMapping("/check")//管理员审核界面，传分页属性
    public ResponseEntity<PageResult<News>> ready_to_check(@RequestParam(defaultValue = "1") int pageNum,
                                                           @RequestParam(defaultValue = "10") int pageSize){
        int offset = (pageNum - 1) * pageSize;
        List<News> news = newsMapper.selectAll(offset, pageSize);
        Long total = newsMapper.count2();
        return ResponseEntity.ok(new PageResult<>(pageNum,pageSize,total,news));
    }

    @RequestMapping("/modify_view")//使用get_newsdetail后随即使用，增加点击量
    public ResponseEntity<Integer> modify(@RequestParam(required = false) String title){
        News news = newsMapper.selectBytitle(title);
        int view = news.getViewCount();
        view++;
        news.setViewCount(view);
        Integer result = newsMapper.update(view,title);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/changeNewsInfo")//当前登录用户，修改新闻id,修改内容
    public ResponseEntity<Integer> changeNewsInfo(
            @RequestParam String now_username,
            @RequestParam Long news_id,
            @RequestBody News news) {

        // 获取当前登录用户
        User_d now = userMapper.findByUsername(now_username);
        if (now == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(-3); // 用户不存在或未登录
        }

        int status = now.getStatus(); // 管理员=1，普通用户=0

        // 获取要修改的新闻
        News oldNews = newsMapper.selectById(news_id);
        if (oldNews == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(-2); // 新闻不存在
        }

        // 权限校验：普通用户不能修改别人写的新闻
        if (status == 0 && !oldNews.getAuthor().equals(now_username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(-1);
        }

        // 按非空字段合并更新
        if (news.getId() != null) oldNews.setId(news.getId());
        if (news.getTitle() != null) oldNews.setTitle(news.getTitle());
        if (news.getSummary() != null) oldNews.setSummary(news.getSummary());
        if (news.getContent() != null) oldNews.setContent(news.getContent());
        if (news.getCoverImage() != null) oldNews.setCoverImage(news.getCoverImage());
        if (news.getAuthor() != null) oldNews.setAuthor(news.getAuthor());
        if (news.getTags() != null) oldNews.setTags(news.getTags());

        int result = newsMapper.modify(oldNews, news_id);

        return ResponseEntity.status(result > 0 ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    @RequestMapping("/addComments")//其中就包含新闻id
    public ResponseEntity<Integer> addComment(@RequestBody coments coments){
        int result = comentsMapper.insert(coments);
        return ResponseEntity.status(result > 0 ? 200 : 500).body(result);
    }

    @RequestMapping("/getCommentsByTime")//传所属新闻id，分页属性
    public ResponseEntity<PageResult<coments>> getCommentsByTime(
            @RequestParam(required = false) long news_id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<coments> list = comentsMapper.findByNewsIdOrderByTime(news_id, pageSize, offset);
        Long total = comentsMapper.countByNewsId(news_id);
        return ResponseEntity.ok(new PageResult<>(pageNum, pageSize, total, list));
    }

    @RequestMapping("/getCommentsByLikes")//可以默认调用Likes，所属新闻id
    public ResponseEntity<PageResult<coments>> getCommentsByLikes(
            @RequestParam long news_id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<coments> list = comentsMapper.findByNewsIdOrderByLikes(news_id, pageSize, offset);
        Long total = comentsMapper.countByNewsId(news_id);
        return ResponseEntity.ok(new PageResult<>(pageNum, pageSize, total, list));
    }

    @RequestMapping("/Likes")//传评论的id
    public ResponseEntity<Integer> likeComment(@RequestParam long id) {
        int result = comentsMapper.likeComment(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("deleteComments")//检查所传id的author是否与当前登录中的人相同
    public ResponseEntity<Integer> deleteComment(@RequestParam long id) {
        int result = comentsMapper.deleteById(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteCommentsByNews")
    public ResponseEntity<Integer> deleteCommentsByNewsId(@RequestParam long news_id) {
        int result = comentsMapper.deleteByNewsId(news_id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/resourseOfComments")
    public ResponseEntity<String> resourseOfComments(@RequestParam long id){
        String result = comentsMapper.findAuthorById(id);
        return ResponseEntity.ok(result);
    }

    @RequestMapping("/deleteNews")//删除新闻的操作，检查删除新闻作者与当前登录账号是否一致(若非管理员)
    public ResponseEntity<Integer> deleteNews(@RequestParam long id) {
        int result = newsMapper.deleteById(id);
        int res = comentsMapper.deleteByNewsId(id);
        int totalDeleted = result + res;
        if (result > 0) {
            return ResponseEntity.ok(totalDeleted);
        } else {
            return ResponseEntity.status(500).body(0);
        }
    }
}
