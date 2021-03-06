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
package com.tenio.example.example6;

import com.tenio.AbstractApp;
import com.tenio.configuration.constant.TEvent;
import com.tenio.entity.element.TArray;
import com.tenio.example.server.Configuration;
import com.tenio.extension.AbstractExtensionHandler;
import com.tenio.extension.IExtension;
import com.tenio.utility.MathUtility;

/**
 * This class shows how a server handle 1000 players and communications
 * 
 * @author kong
 *
 */
public final class TestServerStress extends AbstractApp {

	/**
	 * The entry point
	 */
	public static void main(String[] args) {
		var game = new TestServerStress();
		game.start();
	}

	@Override
	public IExtension getExtension() {
		return new Extenstion();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Configuration getConfiguration() {
		return new Configuration("TenIOConfig.xml");
	}

	/**
	 * Your own logic handler class
	 */
	private final class Extenstion extends AbstractExtensionHandler implements IExtension {

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
				_playerApi.login(new PlayerStress(username), connection);

				return null;
			});

			_on(TEvent.DISCONNECT_CONNECTION, args -> {
				var connection = _getConnection(args[0]);

				info("DISCONNECT CONNECTION", connection.getAddress());

				return null;
			});

			_on(TEvent.PLAYER_IN_SUCCESS, args -> {
				// The player has login successful
				var player = this.<PlayerStress>_getPlayer(args[0]);
				player.setIgnoreTimeout(true);

				info("PLAYER_IN_SUCCESS", player.getName());

				// Now you can send messages to the client
				// Sending, the data need to be packed
				var data = _messageApi.getArrayPack();
				_messageApi.sendToPlayer(player, PlayerStress.MAIN_CHANNEL, "p", player.getName(), "d",
						data.put("H").put("3").put("L").put("O").put(true)
								.put(TArray.newInstance().put("Sub").put("Value").put(100)));

				return null;
			});

			_on(TEvent.RECEIVED_FROM_PLAYER, args -> {
				var player = this.<PlayerStress>_getPlayer(args[0]);

				var pack = __getSortRandomNumberArray();
				// Sending, the data need to be packed
				var data = _messageApi.getArrayPack();
				for (int i = 0; i < pack.length; i++) {
					data.put(pack[i]);
				}

				_messageApi.sendToPlayer(player, PlayerStress.MAIN_CHANNEL, "p", player.getName(), "d", data);

				return null;
			});

			_on(TEvent.PLAYER_TIMEOUT, args -> {
				var player = this.<PlayerStress>_getPlayer(args[0]);

				info("PLAYER TIMEOUT", player.getName());

				return null;
			});

			_on(TEvent.DISCONNECT_PLAYER, args -> {
				var player = this.<PlayerStress>_getPlayer(args[0]);

				info("DISCONNECT PLAYER", player.getName());

				return null;
			});

			_on(TEvent.CCU, args -> {
				var ccu = _getInt(args[0]);

				info("CCU", ccu);

				return null;
			});

			_on(TEvent.BANDWIDTH, args -> {
				long lastReadThroughput = _getLong(args[0]);
				long lastWriteThroughput = _getLong(args[1]);
				long realWriteThroughput = _getLong(args[2]);
				long currentReadBytes = _getLong(args[3]);
				long currentWrittenBytes = _getLong(args[4]);
				long realWrittenBytes = _getLong(args[5]);

				var bandwidth = String.format(
						"lastReadThroughput=%dKB/s;lastWriteThroughput=%dKB/s;realWriteThroughput=%dKB/s;currentReadBytes=%dKB;currentWrittenBytes=%dKB;realWrittenBytes=%dKB",
						lastReadThroughput, lastWriteThroughput, realWriteThroughput, currentReadBytes,
						currentWrittenBytes, realWrittenBytes);

				info("BANDWIDTH", bandwidth);

				return null;
			});

		}

		private int[] __getSortRandomNumberArray() {
			int[] arr = new int[10];
			for (int i = 0; i < arr.length; i++) {
				// storing random integers in an array
				arr[i] = MathUtility.randInt(0, 100);
			}
			// bubble sort
			int n = arr.length;
			int temp = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 1; j < (n - i); j++) {
					if (arr[j - 1] > arr[j]) {
						// swap elements
						temp = arr[j - 1];
						arr[j - 1] = arr[j];
						arr[j] = temp;
					}
				}
			}

			return arr;
		}

	}

}
