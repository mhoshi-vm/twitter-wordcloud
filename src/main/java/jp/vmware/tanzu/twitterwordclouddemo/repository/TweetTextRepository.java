package jp.vmware.tanzu.twitterwordclouddemo.repository;

import jp.vmware.tanzu.twitterwordclouddemo.model.TweetText;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TweetTextRepository extends CrudRepository<TweetText, Integer> {

	@Query("select txt as txt, count(txt) as count from TweetText group by txt")
	List<TextCount> listTextCount();

	interface TextCount {

		String getTxt();

		Long getCount();

	}

}
