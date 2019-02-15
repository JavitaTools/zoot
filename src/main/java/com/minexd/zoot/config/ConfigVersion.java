package com.minexd.zoot.config;

import com.minexd.zoot.config.impl.ConfigConversion1;
import com.minexd.zoot.config.impl.ConfigConversion2;
import com.minexd.zoot.config.impl.ConfigConversion3;
import com.minexd.zoot.config.impl.ConfigConversion4;
import com.minexd.zoot.config.impl.ConfigConversion5;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConfigVersion {

	VERSION_1(1, new ConfigConversion1()),
	VERSION_2(2, new ConfigConversion2()),
	VERSION_3(3, new ConfigConversion3()),
	VERSION_4(4, new ConfigConversion4()),
	VERSION_5(5, new ConfigConversion5());

	private int number;
	private ConfigConversion conversion;

}
