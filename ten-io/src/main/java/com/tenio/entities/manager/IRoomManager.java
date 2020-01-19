/*
The MIT License

Copyright (c) 2016-2019 kong <congcoi123@gmail.com>

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
package com.tenio.entities.manager;

import java.util.Map;

import com.tenio.api.RoomApi;
import com.tenio.entities.AbstractPlayer;
import com.tenio.entities.AbstractRoom;

/**
 * Manage all your rooms @see {@link AbstractRoom} on the server. It is a
 * singleton pattern class, which can be called anywhere. But it's better that
 * you use the {@link RoomApi} interface for easy management.
 * 
 * @author kong
 * 
 */
public interface IRoomManager {

	/**
	 * @return Returns the number of rooms in your server
	 */
	int count();

	/**
	 * @return Returns all the current rooms in your server
	 */
	Map<String, AbstractRoom> gets();

	/**
	 * Remove all rooms
	 */
	void clear();

	/**
	 * Retrieve a room by its ID.
	 * 
	 * @param roomId the unique ID
	 * @return Returns a room's instance if it has existed, <code>null</code>
	 *         otherwise
	 */
	AbstractRoom get(final String roomId);

	/**
	 * Determine if the room has existed or not.
	 * 
	 * @param roomId the unique ID
	 * @return Returns <code>true</code> if the room has existed, <code>null</code>
	 *         otherwise
	 */
	boolean contain(final String roomId);

	/**
	 * Add a new room to your server. You need create your own room first.
	 * 
	 * @param room that is added @see {@link AbstractRoom}
	 */
	void add(final AbstractRoom room);

	/**
	 * Remove a room from your server.
	 * 
	 * @param room that is removed @see {@link AbstractRoom}
	 */
	void remove(final AbstractRoom room);

	/**
	 * Request one player to join a room. This request can be refused with some
	 * reason. You can handle these results in the corresponding events.
	 * 
	 * @param room   the desired room @see {@link AbstractRoom}
	 * @param player the current player @see {@link AbstractPlayer}
	 */
	void playerJoinRoom(final AbstractRoom room, final AbstractPlayer player);

	/**
	 * Allow a player to leave his current room. You can handle your own logic in
	 * the corresponding events.
	 * 
	 * @param player that will be left his current room @see {@link AbstractPlayer}
	 * @param force  it's set <code>true</code> if you want to force the player
	 *               leave. Otherwise, it's set <code>false</code>
	 */
	void playerLeaveRoom(final AbstractPlayer player, final boolean force);

}