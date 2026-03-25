package com.erp.product.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.erp.common.utils.PageResult;
import com.erp.common.utils.Result;
import com.erp.product.entity.Product;
import com.erp.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/product")
@Tag(name = "商品管理", description = "提供商品信息管理、库存管理等功能")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    /**
     * 分页查询商品列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询商品", description = "支持多条件分页查询")
    public Result<PageResult<Product>> getPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数") @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "商品名称（模糊查询）") @RequestParam(required = false) String productName,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "状态（0-下架，1-上架）") @RequestParam(required = false) Integer status) {
        Page<Product> pageResult = productService.findPage(page, size, productName, categoryId, status);
        
        PageResult<Product> result = new PageResult<>(
                pageResult.getTotal(),
                pageResult.getRecords(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
        
        return Result.success(result);
    }
    
    /**
     * 根据ID查询商品
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询商品", description = "根据ID查询商品详情")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        return Result.success(product);
    }
    
    /**
     * 根据编码查询商品
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "根据编码查询商品", description = "根据商品编码查询商品")
    public Result<Product> getByCode(@PathVariable String code) {
        Product product = productService.findByCode(code);
        return Result.success(product);
    }
    
    /**
     * 查询所有上架商品
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有上架商品", description = "查询所有状态为上架的商品")
    public Result<List<Product>> getAll() {
        List<Product> products = productService.lambdaQuery()
                .eq(Product::getStatus, 1)
                .orderByAsc(Product::getSort)
                .list();
        return Result.success(products);
    }
    
    /**
     * 添加商品
     */
    @PostMapping
    @Operation(summary = "添加商品", description = "添加新商品")
    public Result<Void> add(@RequestBody Product product) {
        boolean result = productService.save(product);
        return result ? Result.success("添加成功", null) : Result.error("添加失败");
    }
    
    /**
     * 更新商品信息
     */
    @PutMapping
    @Operation(summary = "更新商品", description = "修改商品信息")
    public Result<Void> update(@RequestBody Product product) {
        if (product.getId() == null) {
            return Result.error("商品ID不能为空");
        }
        Product existProduct = productService.getById(product.getId());
        if (existProduct == null) {
            return Result.error("商品不存在");
        }
        product.setProductCode(null);
        boolean result = productService.updateById(product);
        return result ? Result.success("更新成功", null) : Result.error("更新失败");
    }
    
    /**
     * 修改商品状态
     */
    @PutMapping("/status/{id}")
    @Operation(summary = "修改商品状态", description = "上架或下架商品")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        productService.updateStatus(id, status);
        return Result.success("状态更新成功", null);
    }
    
    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品", description = "根据ID删除商品")
    public Result<Void> delete(@PathVariable Long id) {
        boolean result = productService.removeById(id);
        return result ? Result.success("删除成功", null) : Result.error("删除失败");
    }
    
    /**
     * 扣减库存
     */
    @PutMapping("/stock/deduct")
    @Operation(summary = "扣减库存", description = "扣减商品库存（负数为增加库存）")
    public Result<Void> deductStock(
            @Parameter(description = "商品ID") @RequestParam Long productId,
            @Parameter(description = "数量（负数为增加）") @RequestParam Integer quantity) {
        productService.updateStock(productId, quantity);
        return Result.success("库存更新成功", null);
    }
}
