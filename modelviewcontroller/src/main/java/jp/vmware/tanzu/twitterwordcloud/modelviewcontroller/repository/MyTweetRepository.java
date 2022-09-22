package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.repository;

import jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model.MyTweet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MyTweetRepository extends CrudRepository<MyTweet, Integer> {

	List<MyTweet> findAllByOrderByTweetIdDesc();

	long deleteByTweetId(String tweetId);

}
