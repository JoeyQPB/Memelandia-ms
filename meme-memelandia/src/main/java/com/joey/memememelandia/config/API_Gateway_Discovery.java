package com.joey.memememelandia.config;

import com.joey.memememelandia.domain.CategoryDTO;
import com.joey.memememelandia.domain.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class API_Gateway_Discovery {

    @FeignClient(name = "memelandia-api-gateway")
    private interface IAPI_Gateway {

        @RequestMapping(path = "/user/get_by_id/{id}", method = RequestMethod.GET)
        @ResponseBody
        ResponseEntity<UserDTO> getUserById(@PathVariable String id);

        @RequestMapping(path = "/user/get_by_email/{email}", method = RequestMethod.GET)
        @ResponseBody
        ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email);

        @RequestMapping(path = "/category/get_by_id/{id}", method = RequestMethod.GET)
        @ResponseBody
        ResponseEntity<CategoryDTO> getCategoryById(@PathVariable String id);

        @RequestMapping(path = "/category/get_by_name/{name}", method = RequestMethod.GET)
        @ResponseBody
        ResponseEntity<CategoryDTO> getCategoryByName(@PathVariable String name);
    }

    @Autowired
    private IAPI_Gateway apiGatewayService;

    public UserDTO getUserByIdFromUserService (String userId) {
        return this.apiGatewayService.getUserById(userId).getBody();
    }

    public UserDTO getUserByEmailFromUserService (String userEmail) {
        return this.apiGatewayService.getUserByEmail(userEmail).getBody();
    }

    public CategoryDTO getCategoryByIdFromCategoryService (String id) {
        return this.apiGatewayService.getCategoryById(id).getBody();
    }

    public CategoryDTO getCategoryByNameFromCategoryService (String categoryName) {
        return this.apiGatewayService.getCategoryByName(categoryName).getBody();
    }
}
