package jp.vmware.tanzu.twitterwordclouddemo.utils;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MorphologicalAnalysis {

	public List<String> getToken(String in) {
		Tokenizer tokenizer = new Tokenizer();

		List<String> tokens = new ArrayList<>();
		for (Token token : tokenizer.tokenize(in)) {
			tokens.add(token.getSurface());
		}
		return tokens;
	}

}
