package PostService.service.feign;

import PostService.dto.person.PersonDTO;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("person-service")
public interface PersonService {
    PersonDTO getPersonDTOByUsername(String userName);
}
