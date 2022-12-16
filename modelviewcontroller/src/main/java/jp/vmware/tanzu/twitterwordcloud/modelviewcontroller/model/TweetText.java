package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TweetText {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String tweetId;

	public String text;

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getText() {
		return text;
	}

	public void setText(String txt) {
		this.text = txt;
	}

}
