package com.minexd.zoot.chat.filter.impl;

import com.minexd.zoot.Zoot;
import com.minexd.zoot.chat.filter.ChatFilter;

public class ContainsFilter extends ChatFilter {

	private final String phrase;

	public ContainsFilter(Zoot zoot, String phrase) {
		this(zoot, phrase, null);
	}

	public ContainsFilter(Zoot zoot, String phrase, String command) {
		super(zoot, command);
		this.phrase = phrase;
	}

	@Override
	public boolean isFiltered(String message, String[] words) {
		for (String word : words) {
			if (word.contains(this.phrase)) {
				return true;
			}
		}

		return false;
	}

}
