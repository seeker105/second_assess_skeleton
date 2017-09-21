package com.cooksys.tweeter.embedded;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Experiment {

	public static void main(String[] args) {
		String content = "This is a #awesome, #fan2tastic, #cra-zy day. It's amazing!";
		
		Pattern p = Pattern.compile("#\\w*");
		Matcher m = p.matcher(content);
		while(m.find()){
			System.out.println(m.group().substring(1));
		}

	}

}
