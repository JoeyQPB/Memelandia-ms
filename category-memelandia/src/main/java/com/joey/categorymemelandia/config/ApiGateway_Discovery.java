package com.joey.categorymemelandia.config;

import com.joey.categorymemelandia.exceptions.MissingFieldsException;
import com.joey.categorymemelandia.records.CategoryServiceRequestUpdateDTO;
import com.joey.categorymemelandia.utils.CategoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@EnableFeignClients
@EnableDiscoveryClient
public class ApiGateway_Discovery {

    @FeignClient(name = "memelandia-api-gateway")
    interface IServiceUpdate {

        @RequestMapping(path = "/update_micro_service/category", method = RequestMethod.POST)
        @ResponseBody
        public Boolean updateCategoryPublisher(@RequestBody CategoryServiceRequestUpdateDTO requestUpdateDTO);
    }

    @Autowired
    private IServiceUpdate serviceUpdate;

    public Boolean updateCategoryPublisher(CategoryServiceRequestUpdateDTO userServiceRequestUpdateDTO) {
        if (CategoryUtils.categoryServiceRequestUpdateDTOHasAnyFieldNullOrEmpty(userServiceRequestUpdateDTO)) {
            throw new MissingFieldsException("Missing fields on CategoryServiceRequestUpdateDTO!");
        }
        return this.serviceUpdate.updateCategoryPublisher(userServiceRequestUpdateDTO);
    }
}
