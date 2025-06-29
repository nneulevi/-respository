package org.example.project2.Kingbase;

import org.example.project2.Controller.enterpriseController;
import org.example.project2.Mapper.enterpriseMapper;
import org.example.project2.entity.Enterprise;
import org.example.project2.entity.PageResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class enterpriseControllerTest {
    
    private enterpriseController enterpriseController;
    
    @Mock
    private enterpriseMapper enterpriseMapper;
    
    @BeforeEach
    void setUp() {
        enterpriseMapper = mock(enterpriseMapper.class);
        
        enterpriseController = new enterpriseController();
        // 使用反射注入依赖
        try {
            var enterpriseMapperField = enterpriseController.class.getDeclaredField("enterpriseMapper");
            enterpriseMapperField.setAccessible(true);
            enterpriseMapperField.set(enterpriseController, enterpriseMapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void testAdd_Register_Success() {
        // 准备测试数据
        Enterprise enterprise = new Enterprise(1L, "测试公司", "许可证号123", "联系人", "联系电话", 0);
        
        // 模拟Mapper返回
        when(enterpriseMapper.insert(any(Enterprise.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = enterpriseController.Add(enterprise);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        verify(enterpriseMapper).insert(argThat(ent -> ent.getAudiStatus() == 1));
    }
    
    @Test
    void testAdd_Register_Failure() {
        // 准备测试数据
        Enterprise enterprise = new Enterprise(1L, "测试公司", "许可证号123", "联系人", "联系电话", 0);
        
        // 模拟Mapper返回失败
        when(enterpriseMapper.insert(any(Enterprise.class))).thenReturn(0);
        
        // 执行测试
        ResponseEntity<Integer> response = enterpriseController.Add(enterprise);
        
        // 验证结果
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(0, response.getBody());
    }
    
    @Test
    void testSearch() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "公司1", "许可证号1", "联系人1", "联系电话1", 1),
            new Enterprise(2L, "公司2", "许可证号2", "联系人2", "联系电话2", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        PageResult<Enterprise> result = enterpriseController.search(1, 10, 1L, "公司", "电话", "许可证", "联系人", "联系电话");
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(2L, result.getTotal());
        assertEquals(2, result.getList().size());
    }
    
    @Test
    void testSearch_WithNullParameters() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "公司1", "许可证号1", "联系人1", "联系电话1", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试
        PageResult<Enterprise> result = enterpriseController.search(1, 10, null, null, null, null, null, null);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }
    
    @Test
    void testGet_GetEnterprises() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "公司1", "许可证号1", "联系人1", "联系电话1", 1),
            new Enterprise(2L, "公司2", "许可证号2", "联系人2", "联系电话2", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(2L);
        
        // 执行测试
        ResponseEntity<PageResult<Enterprise>> response = enterpriseController.get(1, 10, 1L, "公司", "电话", "许可证", "联系人", "联系电话");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getList().size());
        assertEquals(2L, response.getBody().getTotal());
    }
    
    @Test
    void testGet_GetEnterprises_DefaultParameters() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "公司1", "许可证号1", "联系人1", "联系电话1", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(1L);
        
        // 执行测试 - 使用默认参数
        ResponseEntity<PageResult<Enterprise>> response = enterpriseController.get(1, 10, null, null, null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
        assertEquals(1L, response.getBody().getTotal());
        
        // 验证分页参数
        assertEquals(1, response.getBody().getPageNum());
        assertEquals(10, response.getBody().getPageSize());
    }
    
    @Test
    void testGet_GetEnterprises_CustomPageSize() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "公司1", "许可证号1", "联系人1", "联系电话1", 1),
            new Enterprise(2L, "公司2", "许可证号2", "联系人2", "联系电话2", 1),
            new Enterprise(3L, "公司3", "许可证号3", "联系人3", "联系电话3", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(3L);
        
        // 执行测试 - 自定义分页大小
        ResponseEntity<PageResult<Enterprise>> response = enterpriseController.get(2, 5, null, null, null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getList().size());
        assertEquals(3L, response.getBody().getTotal());
        
        // 验证分页参数
        assertEquals(2, response.getBody().getPageNum());
        assertEquals(5, response.getBody().getPageSize());
    }
    
    @Test
    void testGet_GetEnterprises_WithSearchCriteria() {
        // 准备测试数据
        List<Enterprise> enterpriseList = Arrays.asList(
            new Enterprise(1L, "科技公司", "许可证号123", "张三", "13800138000", 1)
        );
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(eq(1L), eq("科技"), eq("138"), eq("123"), eq("张三"), eq("138"), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(eq(1L), eq("科技"), eq("138"), eq("123"), eq("张三"), eq("138")))
            .thenReturn(1L);
        
        // 执行测试 - 使用搜索条件
        ResponseEntity<PageResult<Enterprise>> response = enterpriseController.get(1, 10, 1L, "科技", "138", "123", "张三", "138");
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getList().size());
        assertEquals(1L, response.getBody().getTotal());
        
        // 验证搜索条件传递正确
        verify(enterpriseMapper).getAllEnterprise(eq(1L), eq("科技"), eq("138"), eq("123"), eq("张三"), eq("138"), eq(0), eq(10));
        verify(enterpriseMapper).count(eq(1L), eq("科技"), eq("138"), eq("123"), eq("张三"), eq("138"));
    }
    
    @Test
    void testGet_GetEnterprises_EmptyResult() {
        // 准备测试数据 - 空结果
        List<Enterprise> enterpriseList = Arrays.asList();
        
        // 模拟Mapper返回
        when(enterpriseMapper.getAllEnterprise(any(), any(), any(), any(), any(), any(), anyInt(), anyInt()))
            .thenReturn(enterpriseList);
        when(enterpriseMapper.count(any(), any(), any(), any(), any(), any()))
            .thenReturn(0L);
        
        // 执行测试
        ResponseEntity<PageResult<Enterprise>> response = enterpriseController.get(1, 10, null, "不存在的公司", null, null, null, null);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getList().size());
        assertEquals(0L, response.getBody().getTotal());
    }
    
    @Test
    void testAdd_Register_VerifyAuditStatusSet() {
        // 准备测试数据
        Enterprise enterprise = new Enterprise(1L, "测试公司", "许可证号123", "联系人", "联系电话", 0);
        
        // 模拟Mapper返回
        when(enterpriseMapper.insert(any(Enterprise.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = enterpriseController.Add(enterprise);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody());
        
        // 验证auditStatus被设置为1
        verify(enterpriseMapper).insert(argThat(ent -> {
            return ent.getAudiStatus() == 1;
        }));
    }
    
    @Test
    void testAdd_Register_VerifyAllFieldsPreserved() {
        // 准备测试数据
        Enterprise enterprise = new Enterprise(1L, "测试公司", "许可证号123", "联系人", "联系电话", 0);
        
        // 模拟Mapper返回
        when(enterpriseMapper.insert(any(Enterprise.class))).thenReturn(1);
        
        // 执行测试
        ResponseEntity<Integer> response = enterpriseController.Add(enterprise);
        
        // 验证结果
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        // 验证所有字段都被正确保存
        verify(enterpriseMapper).insert(argThat(ent -> {
            return ent.getId().equals(1L) &&
                   "测试公司".equals(ent.getName()) &&
                   "许可证号123".equals(ent.getLicenseNumber()) &&
                   "联系人".equals(ent.getContactPerson()) &&
                   "联系电话".equals(ent.getContactPhone()) &&
                   ent.getAudiStatus() == 1;
        }));
    }
} 