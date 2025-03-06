package com.poixson.tools.chat;


public enum ChatDelivery {
	DELIVER_LOCAL,
	DELIVER_RADIO;



	public static String ToString(final ChatDelivery delivery) {
		return (delivery==null ? null : delivery.toString());
	}



};
