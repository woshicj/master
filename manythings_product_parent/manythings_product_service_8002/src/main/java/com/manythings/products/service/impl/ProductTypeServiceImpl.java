package com.manythings.products.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.manythings.constants.GlobelConstants;
import com.manythings.domain.ProductType;
import com.manythings.products.mapper.ProductTypeMapper;
import com.manythings.products.service.IProductTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.manythings.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品目录 服务实现类
 * </p>
 *
 * @author CJJ
 * @since 2019-02-28
 */
@Service
public class ProductTypeServiceImpl extends ServiceImpl<ProductTypeMapper, ProductType> implements IProductTypeService {
    @Autowired
    private ProductTypeMapper productTypeMapper;

    //注入RedisClient:
    @Autowired
    private RedisClient redisClient;
    @Override
    public List<ProductType> treeData() {
        //先根据key,从redis获取:我是producttype的服务提供者,我要调用公共服务的redis,则是redis的消费者:
        //java内部的服务的调用,就应该使用feign或者ribbon:选中feign:
        //feign的使用:是在消费者,注入接口,就象调用本地接口一样

        //判断是否有结果:有就直接返回,没有就从数据库获取,存入redis,并返回
        String jsonArrStr = redisClient.get(GlobelConstants.REDIS_PRODUCTTYPE_KEY);
        if(StringUtils.isEmpty(jsonArrStr)){
            //没有就从数据库获取,存入redis,并返回
            List<ProductType> productTypes = treeDataLoop();
            jsonArrStr= JSONArray.toJSONString(productTypes);
            //redis存入
            redisClient.set(GlobelConstants.REDIS_PRODUCTTYPE_KEY,jsonArrStr );
            System.out.println("from========db===============");
            return productTypes;
        }else{
            //有:有就直接返回
            //json的数组字符串--->json数组
            System.out.println("from========cache===============");
            return JSONArray.parseArray(jsonArrStr, ProductType.class);
        }

        }






    private List<ProductType> treeDataLoop() {
        //1:获取所有的数据:
        List<ProductType> allProductType = productTypeMapper.selectList(null);
        //2:用于存在每一个对象和他的一个标识的 Long:id
        Map<Long,ProductType> map = new HashMap<>();
        for (ProductType productType : allProductType) {
            map.put(productType.getId(),productType);
        }
        //得到最终想要的结果
        List<ProductType> result = new ArrayList<>();
        //遍历
        for (ProductType productType : allProductType) {
            Long pid = productType.getPid();
                if(pid==0){
                    result.add(productType);
                }else{
                    // 找自己的老子,把自己添加到老子的儿子中
                    ProductType parent=map.get(pid);// where id =pid
               /* //我老子的儿子
                List<ProductType> children = parent.getChildren();
                //把我自己放入老子的儿子中
                children.add(productType);*/
                    parent.getChildren().add(productType);
                }
            }
            return result;




    }


}
