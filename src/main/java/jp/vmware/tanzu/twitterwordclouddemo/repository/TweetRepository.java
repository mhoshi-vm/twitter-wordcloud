package jp.vmware.tanzu.twitterwordclouddemo.repository;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<MyTweet, Integer> {

}
