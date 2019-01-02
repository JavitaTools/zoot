package com.minexd.zoot.bootstrap;

import com.minexd.zoot.Zoot;
import lombok.Getter;

@Getter
public class Bootstrapped {

	protected final Zoot zoot;

	public Bootstrapped(Zoot zoot) {
		this.zoot = zoot;
	}

}
