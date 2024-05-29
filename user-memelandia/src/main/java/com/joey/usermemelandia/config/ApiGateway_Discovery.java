package com.joey.usermemelandia.config;

import com.joey.usermemelandia.exceptions.MissingFieldsException;
import com.joey.usermemelandia.records.UserServiceRequestUpdateDTO;
import com.joey.usermemelandia.utils.UserUtils;
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

        @RequestMapping(path = "/update_micro_service/user", method = RequestMethod.POST)
        @ResponseBody
        public Boolean updateUserPublisher(@RequestBody UserServiceRequestUpdateDTO requestUpdateDTO);
    }

    @Autowired
    private IServiceUpdate serviceUpdate;

    public Boolean updateUserPublisher(UserServiceRequestUpdateDTO userServiceRequestUpdateDTO) {
        if (UserUtils.userServiceRequestUpdateDTOHasAnyFieldNullOrEmpty(userServiceRequestUpdateDTO)) {
            throw new MissingFieldsException("Missing fields on userServiceRequestUpdateDTO!");
        }
        return this.serviceUpdate.updateUserPublisher(userServiceRequestUpdateDTO);
    }
}
