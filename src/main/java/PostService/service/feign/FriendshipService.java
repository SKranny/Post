package PostService.service.feign;

import java.util.List;

public interface FriendshipService {
    List<Long> SubscriptionsIDs(String userName);
}
