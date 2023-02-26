package com.cy.store.controller;

import com.cy.store.entity.Address;
import com.cy.store.entity.Product;
import com.cy.store.service.IAddressService;
import com.cy.store.service.IProductService;
import com.cy.store.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

//@Controller
@RestController //等效于@Controller+@ResponseBody
@RequestMapping("products")
public class ProductController extends BaseController{
	@Autowired
	private IProductService productService;
	
	@RequestMapping("{title}/search")
	public JsonResult<List<Product>> searchProductByTitle(@PathVariable("title") String title) {
		//处理title
		String t = "%"+title+"%";
		List<Product> data = productService.searchProductByTitle(t);
		//处理image与price
		for (Product p : data){
			p.setImage(".."+p.getImage()+"collect.png");
		}
		return new JsonResult<List<Product>>(OK,data);
	}
	
	@RequestMapping("hot_list")
	public JsonResult<List<Product>> getHotList() {
		List<Product> data = productService.findHotList();
		return new JsonResult<List<Product>>(OK, data);
	}
	
	@GetMapping("{id}/details")
	public JsonResult<Product> getById(@PathVariable("id") Integer id) {
		// 调用业务对象执行获取数据
		Product data = productService.findById(id);
		// 返回成功和数据
		return new JsonResult<Product>(OK, data);
	}
	
	@RequestMapping("lately_list")
	public JsonResult<List<Product>> getLatelyList() {
		List<Product> data = productService.findLatelyList();
		return new JsonResult<List<Product>>(OK, data);
	}
}
