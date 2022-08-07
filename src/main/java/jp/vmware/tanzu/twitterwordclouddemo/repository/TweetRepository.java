package jp.vmware.tanzu.twitterwordclouddemo.repository;

import jp.vmware.tanzu.twitterwordclouddemo.model.MyTweet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TweetRepository extends CrudRepository<MyTweet, Integer> {

	List<MyTweet> findAllByOrderByTweetIdDesc();

}
