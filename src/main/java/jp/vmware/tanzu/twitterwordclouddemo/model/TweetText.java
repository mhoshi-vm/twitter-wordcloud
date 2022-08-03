package jp.vmware.tanzu.twitterwordclouddemo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TweetText {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer id;

	public String tweetId;

	public String txt;

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getTxt() {
		return txt;
	}

	public void setTxt(String txt) {
		this.txt = txt;
	}

}
