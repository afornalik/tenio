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
package com.tenio.network.mina;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.tenio.configuration.BaseConfiguration;
import com.tenio.configuration.constant.LogicEvent;
import com.tenio.event.EventManager;
import com.tenio.network.Connection;

/**
 * /** Use <a href="https://mina.apache.org/">Apache Mina</a> to handle message.
 * Base on the messages' content. You can handle your own logic here.
 * 
 * @author kong
 * 
 */
public abstract class BaseMinaHandler extends IoHandlerAdapter {

	/**
	 * Retrieve a connection by its session
	 * 
	 * @param session @see {@link IoSession}
	 * @return Returns a connection
	 */
	protected Connection _getConnection(IoSession session) {
		return (MinaConnection) session.getAttribute(MinaConnection.KEY_THIS);
	}

	/**
	 * When a client is disconnected from your server for any reason, you can handle
	 * it in this event
	 * 
	 * @param session                the session @see {@link IoSession}
	 * @param keepPlayerOnDisconnect this value can be configured in your
	 *                               configurations @see {@link BaseConfiguration}.
	 *                               If the value is set to true, when the client is
	 *                               disconnected, it's player can be held for an
	 *                               interval time (you can configure this interval
	 *                               time in your configurations)
	 */
	protected void _sessionClosed(IoSession session, boolean keepPlayerOnDisconnect) {
		// get the connection first
		var connection = _getConnection(session);
		EventManager.getLogic().emit(LogicEvent.CONNECTION_CLOSE, connection, keepPlayerOnDisconnect);
		connection = null;
	}

	/**
	 * Record the exceptions
	 * 
	 * @param session the session @see {@link IoSession}
	 * @param cause   the exception will occur
	 */
	protected void _exceptionCaught(IoSession session, Throwable cause) {
		// get the connection first
		var connection = _getConnection(session);
		var id = String.valueOf(session.getId());
		EventManager.getLogic().emit(LogicEvent.CONNECTION_EXCEPTION, id, connection, cause);
	}
	
}
