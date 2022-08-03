package jp.vmware.tanzu.twitterwordclouddemo.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MyTweet {

	@Id
	public String tweetId;

	public String text;

	public String username;

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
