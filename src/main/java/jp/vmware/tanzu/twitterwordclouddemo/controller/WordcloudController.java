package jp.vmware.tanzu.twitterwordclouddemo.controller;

import jp.vmware.tanzu.twitterwordclouddemo.model.VueWordCloudEntity;
import jp.vmware.tanzu.twitterwordclouddemo.repository.TweetTextRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WordcloudController {

	private final TweetTextRepository tweetTextRepository;

	public WordcloudController(TweetTextRepository tweetTextRepository) {
		this.tweetTextRepository = tweetTextRepository;
	}

	@GetMapping("/tweet")
	public ResponseEntity<List<VueWordCloudEntity>> string() {
		List<TweetTextRepository.TextCount> listTextCount = tweetTextRepository.listTextCount();
		List<VueWordCloudEntity> vueWordCloudEntities = new ArrayList<>();
		for (TweetTextRepository.TextCount textCount : listTextCount) {
			VueWordCloudEntity vueWordCloudEntity = new VueWordCloudEntity();
			vueWordCloudEntity.setName(textCount.getTxt());
			vueWordCloudEntity.setValue(textCount.getCount());
			vueWordCloudEntities.add(vueWordCloudEntity);
		}
		return new ResponseEntity<>(vueWordCloudEntities, HttpStatus.OK);
	}

}
