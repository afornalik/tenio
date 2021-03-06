/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.configuration.constant;

import com.tenio.pool.IElementPool;

/**
 * All base constants' values for the server are defined here. This class should
 * not be modified.
 * 
 * @author kong
 * 
 */
public final class Constants {

	private Constants() {
	}

	/**
	 * In TCP, because of the stream transmission, it's necessary to know a data
	 * package's length for extracting the number of bytes of its content (divide
	 * stream data into smaller package data). Therefore, we need a data length
	 * value, which should be attached in the header of each package. All TCP
	 * connections which connect to our server must follow this rule.
	 */
	public static final int HEADER_BYTES = 2;
	/**
	 * It is used in the FSM pattern in which all entities can communicate with
	 * others by message in some delay time. The value below describes one entity
	 * can send a message immediately for others. It is also used for communication
	 * between a heart-beat with outside.
	 */
	public static final double SEND_MSG_IMMEDIATELY = 0;

	/**
	 * The number of elements in a bulk those created for the first time.
	 * 
	 * @see IElementPool
	 */
	public static final int BASE_ELEMENT_POOL = 32;

	/**
	 * When the desired number of elements exceeded the first configuration. The new
	 * number of elements will be added.
	 * 
	 * @see IElementPool
	 */
	public static final int ADD_ELEMENT_POOL = 10;

	/**
	 * A unique key for the CCU scan schedule.
	 */
	public static final String KEY_SCHEDULE_CCU_SCAN = "t.schedule.ccu.scan";

	/**
	 * A unique key for the Empty Room scan schedule.
	 */
	public static final String KEY_SCHEDULE_EMPTY_ROOM_SCAN = "t.schedule.empty.room.scan";

	/**
	 * A unique key for the Time Out scan schedule.
	 */
	public static final String KEY_SCHEDULE_TIME_OUT_SCAN = "t.schedule.time.out.scan";

	public static final String KEY_SCHEDULE_HTTP_MANAGER = "t.schedule.http.manager";

	/**
	 * 0 or a limit in bytes/s
	 */
	public static final long TRAFFIC_COUNTER_WRITE_LIMIT = 0L;

	/**
	 * 0 or a limit in bytes/s
	 */
	public static final long TRAFFIC_COUNTER_READ_LIMIT = 0L;

	/**
	 * The delay between two computations of performances for channels or 0 if no
	 * stats are to be computed.
	 */
	public static final long TRAFFIC_COUNTER_CHECK_INTERVAL = 1000;

	/**
	 * The HTTP response with UTF-8 encoding
	 */
	public static final String UTF_8 = "UTF-8";

	/**
	 * The HTTP response with content type in JSON
	 */
	public static final String CONTENT_TYPE_JSON = "application/json";

	/**
	 * The HTTP response with content type in text
	 */
	public static final String CONTENT_TYPE_TEXT = "text/html";

	/**
	 * The default URI path when a HTTP server was started (To confirm if the server
	 * was started or not)
	 */
	public static final String PING_PATH = "/ping";

}
