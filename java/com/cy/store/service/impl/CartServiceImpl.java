package com.cy.store.service.impl;
import com.cy.store.entity.Cart;
import com.cy.store.entity.Product;
import com.cy.store.mapper.CartMapper;
import com.cy.store.service.ICartService;
import com.cy.store.service.IProductService;
import com.cy.store.service.ex.*;
import com.cy.store.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/** 处理购物车数据的业务层实现类 */
@Service
public class CartServiceImpl implements ICartService {
	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private IProductService productService;
	
	@Override
	public void addToCart(Integer uid, Integer pid, Integer amount, String username) {
		Cart result = cartMapper.findByUidAndPid(uid, pid);
		
		//判断是否为null
		//null 表示该用户并未将该商品添加到购物车
		if (result == null){
			Long price = productService.findById(pid).getPrice();
			//封装cart对象
			Cart cart = new Cart();
			cart.setUid(uid);
			cart.setPid(pid);
			cart.setNum(amount);
			cart.setPrice(price);
			//封装日志
			cart.setCreatedTime(new Date());
			cart.setModifiedTime(new Date());
			cart.setCreatedUser(username);
			cart.setModifiedUser(username);
			
			Integer rows = cartMapper.insert(cart);
			if (rows != 1){
				throw new InsertException("添加购物车时发生未知异常");
			}
		}else {
			//该用户的购物车中已有该商品,执行修改
			Integer cid = result.getCid();
			//修改商品数量num
			Integer num = result.getNum() + amount;
			//执行修改
			Integer rows = cartMapper.updateNumByCid(cid, num, username, new Date());
			if (rows != 1){
				throw new UpdateException("添加购物车时发生未知异常");
			}
		}
	
	}
	
	@Override
	public List<CartVO> getVOByUid(Integer uid) {
		return cartMapper.findVOByUid(uid);
	}
	
	@Override
	public Integer addNum(Integer cid, Integer uid, String username) {
		Cart cart = cartMapper.findByCid(cid);
		//判断是否查询到此条购物车数据
		if (cart == null){
			throw new CartNotFoundException("购物车数据不存在");
		}
		
		//判断uid是否相同
		if (!cart.getUid().equals(uid)){
			throw new AccessDeniedException("非法访问");
		}
		
		//num加1
		Integer num = cart.getNum()+1;
		
		Integer rows = cartMapper.updateNumByCid(cid,num,username,new Date());
		
		if (rows != 1){
			throw new InsertException("修改购物车商品数量时发生异常");
		}
		
		return num;
	}
	
	@Override
	public Integer subNum(Integer cid, Integer uid, String username) {
		Cart cart = cartMapper.findByCid(cid);
		//判断是否查询到此条购物车数据
		if (cart == null){
			throw new CartNotFoundException("购物车数据不存在");
		}
		
		//判断uid是否相同
		if (!cart.getUid().equals(uid)){
			throw new AccessDeniedException("非法访问");
		}
		
		//num加1
		Integer num = cart.getNum()-1;
		
		Integer rows = cartMapper.updateNumByCid(cid,num,username,new Date());
		
		if (rows != 1){
			throw new InsertException("修改购物车商品数量时发生异常");
		}
		
		return num;
	}
	
	@Override
	public List<CartVO> getVOByCids(Integer uid, Integer[] cids) {
		List<CartVO> list = cartMapper.findVOByCids(cids);
		
		Iterator<CartVO> it = list.iterator();
		while (it.hasNext()) {
			CartVO cart = it.next();
			if (!cart.getUid().equals(uid)) {
				it.remove();
			}
		}
		return list;
	}
	
	@Override
	public void deleteByCid(Integer uid, Integer cid) {
		Cart cart = cartMapper.findByCid(cid);
		if (cart == null){
			throw new CartNotFoundException("未找到此购物车数据");
		}
		if (!cart.getUid().equals(uid)){
			throw new AccessDeniedException("非法访问!!!");
		}
		
		//开始删除
		Integer rows = cartMapper.deleteByCid(cid);
		if (rows != 1){
			throw new DeleteException("删除时发生未知的异常");
		}
	}
}