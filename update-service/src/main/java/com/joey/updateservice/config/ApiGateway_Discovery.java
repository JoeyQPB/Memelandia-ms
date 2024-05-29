package com.joey.updateservice.config;

import com.joey.updateservice.exceptions.MissingFieldsException;
import com.joey.updateservice.dtos.Meme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class ApiGateway_Discovery {

    @FeignClient(name = "memelandia-api-gateway")
    private interface IAPI_Gateway {

        @RequestMapping(path = "/meme/get_all_by_creator/{userName}", method = RequestMethod.GET)
        @ResponseBody
        Iterable<Meme> getAllMemesByCreator(@PathVariable String userName);

        @RequestMapping(path = "/meme/get_all_by_category/{categoryName}", method = RequestMethod.GET)
        @ResponseBody
        Iterable<Meme> getAllMemesByCategory(@PathVariable String categoryName);

        @RequestMapping(path = "/meme/update_creator/{id}", method = RequestMethod.PUT)
        @ResponseBody
        Meme updateCreatorMeme(@PathVariable String id, @RequestBody String name);

        @RequestMapping(path = "/meme/update_category/{id}", method = RequestMethod.PUT)
        @ResponseBody
        Meme updateMemeCategory(@PathVariable String id, @RequestBody String categoryName);

    }

    @Autowired
    private IAPI_Gateway apiGatewayService;

    public Iterable<Meme> getAllMemesByCreator (String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new MissingFieldsException("userName is missing");
        }
        return this.apiGatewayService.getAllMemesByCreator(userName);
    }

    public Iterable<Meme> getAllMemesByCategory (String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            throw new MissingFieldsException("categoryName is missing");
        }
        return this.apiGatewayService.getAllMemesByCategory(categoryName);
    }

    public Meme updateMemeCreator(String id, String name) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty()) {
            throw new MissingFieldsException("MissingFieldsException");
        }
        return this.apiGatewayService.updateCreatorMeme(id, name);
    }

    public Meme updateMemeCategory(String id, String categoryName) {
        if (id == null || id.isEmpty() || categoryName == null || categoryName.isEmpty()) {
            throw new MissingFieldsException("MissingFieldsException");
        }
        return this.apiGatewayService.updateMemeCategory(id, categoryName);
    }
}
