package com.example.demo.domain.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.share.OptionQueried;

@SpringBootTest
class OptionServiceTest {

	@Autowired
	private OptionService optionService;

//	@Test
	void testGetSettingTypes() {
		List<OptionQueried> options = optionService.getSettingTypes("ROLE");
		assertTrue(!options.isEmpty());
	}

	

}
