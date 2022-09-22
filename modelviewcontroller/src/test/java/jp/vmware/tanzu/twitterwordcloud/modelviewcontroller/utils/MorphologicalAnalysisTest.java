package jp.vmware.tanzu.twitterwordcloud.modelviewcontroller.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MorphologicalAnalysisTest {

	private MorphologicalAnalysis morphologicalAnalysis;

	@BeforeEach
	void setUp() {
		morphologicalAnalysis = new MorphologicalAnalysis();
	}

	@Test
	void getToken() {
		final String testString = "私は、テストです。";
		List<String> tokens = morphologicalAnalysis.getToken(testString);

		assertEquals("私", tokens.get(0));
		assertEquals("は", tokens.get(1));
		assertEquals("、", tokens.get(2));
		assertEquals("テスト", tokens.get(3));
		assertEquals("です", tokens.get(4));
		assertEquals("。", tokens.get(5));

	}

}