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
package com.tenio.example.example3;

import java.io.IOException;

import org.json.simple.JSONObject;

import com.tenio.AbstractApp;
import com.tenio.configuration.constant.RestMethod;
import com.tenio.configuration.constant.TEvent;
import com.tenio.entity.element.TObject;
import com.tenio.example.server.Configuration;
import com.tenio.extension.AbstractExtensionHandler;
import com.tenio.extension.IExtension;

/**
 * This class shows how a server handle messages that came from a client
 * 
 * @author kong
 *
 */
public final class TestServerAttach extends AbstractApp {

	/**
	 * The entry point
	 */
	public static void main(String[] args) {
		var game = new TestServerAttach();
		game.start();
	}

	@Override
	public IExtension getExtension() {
		return new Extenstion();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Configuration getConfiguration() {
		return new Configuration("TenIOConfig.attach.xml");
	}

	/**
	 * Your own logic handler class
	 */
	private final class Extenstion extends AbstractExtensionHandler implements IExtension {

		@SuppressWarnings("unchecked")
		@Override
		public void initialize() {
			_on(TEvent.CONNECTION_SUCCESS, args -> {
				var connection = _getConnection(args[0]);
				var message = _getTObject(args[1]);

				info("CONNECTION", connection.getAddress());

				// Allow the connection login into server (become a player)
				String username = message.getString("u");
				// Should confirm that credentials by data from database or other services, here
				// is only for testing
				_playerApi.login(new PlayerAttach(username), connection);

				return null;
			});

			_on(TEvent.DISCONNECT_CONNECTION, args -> {
				var connection = _getConnection(args[0]);

				info("DISCONNECT CONNECTION", connection.getAddress());

				return null;
			});

			_on(TEvent.PLAYER_IN_SUCCESS, args -> {
				// The player has login successful
				var player = this.<PlayerAttach>_getPlayer(args[0]);

				info("PLAYER IN", player.getName());

				// Now you can allow the player make a UDP connection request
				_messageApi.sendToPlayer(player, PlayerAttach.MAIN_CHANNEL, "c", "udp");

				return null;
			});

			_on(TEvent.RECEIVED_FROM_PLAYER, args -> {
				var player = this.<PlayerAttach>_getPlayer(args[0]);
				int index = _getInt(args[1]);
				var message = _getTObject(args[2]);

				info("PLAYER RECV ", message);

				_messageApi.sendToPlayer(player, index, "hello",
						"from server > " + index + " > " + player.getConnection(index).getAddress());

				return null;
			});

			_on(TEvent.PLAYER_TIMEOUT, args -> {
				var player = this.<PlayerAttach>_getPlayer(args[0]);

				info("PLAYER TIMEOUT", player.getName());

				return null;
			});

			_on(TEvent.DISCONNECT_PLAYER, args -> {
				var player = this.<PlayerAttach>_getPlayer(args[0]);

				info("DISCONNECT PLAYER", player.getName());

				return null;
			});

			_on(TEvent.ATTACH_CONNECTION_REQUEST, args -> {
				var message = _getTObject(args[1]);
				String name = message.getString("u");

				// It should be ...
				// 1. check if player has sub connection
				// 2. confirm with player's name and main connection

				// But now temporary returns a player by his name
				return _playerApi.get(name);
			});

			_on(TEvent.ATTACH_CONNECTION_SUCCESS, args -> {
				var index = _getInt(args[0]);
				var player = this.<PlayerAttach>_getPlayer(args[1]);

				info("ATTACH CONNECTION SUCCESS", player.getName() + " " + player.getConnection(0).getAddress() + " "
						+ player.getConnection(index).getAddress());

				_messageApi.sendToPlayer(player, PlayerAttach.MAIN_CHANNEL, "c", "udp-done");

				return null;
			});

			_on(TEvent.ATTACH_CONNECTION_FAILED, args -> {
				var reason = _getString(args[2]);

				info("ATTACH UDP FAILED", reason);

				return null;
			});

			_on(TEvent.HTTP_REQUEST, args -> {
				var method = _getRestMethod(args[0]);
				// var request = _getHttpServletRequest(args[1]);
				var response = _getHttpServletResponse(args[2]);

				if (method.equals(RestMethod.DELETE)) {
					var json = new JSONObject();
					json.putAll(TObject.newInstance().add("status", "failed").add("message", "not supported"));
					try {
						response.getWriter().println(json.toString());
					} catch (IOException e) {
						error(e, "request");
					}
					return response;
				}

				return null;
			});

			_on(TEvent.HTTP_HANDLER, args -> {
				// var method = _getRestMethod(args[0]);
				// var request = _getHttpServletRequest(args[1]);
				var response = _getHttpServletResponse(args[2]);

				var json = new JSONObject();
				json.putAll(TObject.newInstance().add("status", "ok").add("message", "handler"));
				try {
					response.getWriter().println(json.toString());
				} catch (IOException e) {
					error(e, "handler");
				}

				return null;
			});

		}

	}

}
