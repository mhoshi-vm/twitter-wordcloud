package jp.vmware.tanzu.twitterwordclouddemo.service;

import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TweetTextService {

	private final TweetTextRepository tweetTextRepository;

	public static final int pageSize = 200;

	public TweetTextService(TweetTextRepository tweetTextRepository) {
		this.tweetTextRepository = tweetTextRepository;
	}

	public List<TweetTextRepository.TextCount> listTweetsPage() {
		return tweetTextRepository.listTextCount(PageRequest.of(0, pageSize));
	}

}
