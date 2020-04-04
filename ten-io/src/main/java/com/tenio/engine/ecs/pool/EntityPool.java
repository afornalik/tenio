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
package com.tenio.engine.ecs.pool;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import com.tenio.configuration.constant.Constants;
import com.tenio.engine.ecs.ContextInfo;
import com.tenio.engine.ecs.Entity;
import com.tenio.engine.ecs.common.IEntity;
import com.tenio.exception.NullElementPoolException;
import com.tenio.logger.AbstractLogger;
import com.tenio.pool.IElementPool;

/**
 * @see {@link IElementPool}
 * 
 * @author kong
 * 
 */
public final class EntityPool extends AbstractLogger implements IElementPool<IEntity> {

	private IEntity[] __pool;
	private boolean[] __used;
	private Class<? extends Entity> __clazz;
	private ContextInfo __contextInfo;

	public EntityPool(Class<? extends Entity> clazz, ContextInfo contextInfo) {
		__clazz = clazz;
		__contextInfo = contextInfo;
		__pool = new IEntity[Constants.BASE_ELEMENT_POOL];
		__used = new boolean[Constants.BASE_ELEMENT_POOL];

		for (int i = 0; i < __pool.length; i++) {
			try {
				var entity = __clazz.getDeclaredConstructor().newInstance();
				entity.setId(UUID.randomUUID());
				entity.setContextInfo(__contextInfo);
				__pool[i] = entity;
				__used[i] = false;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				error("EXCEPTION CREATE", "component", e);
			}
		}
	}

	@Override
	public IEntity get() {
		for (int i = 0; i < __used.length; i++) {
			if (!__used[i]) {
				__used[i] = true;
				return __pool[i];
			}
		}
		// If we got here, then all the Elements are in use. We will
		// increase the number in our pool by @ADD_ELEMENT_POOL (arbitrary value for
		// illustration purposes).
		var oldUsed = __used;
		__used = new boolean[oldUsed.length + Constants.ADD_ELEMENT_POOL];
		System.arraycopy(oldUsed, 0, __used, 0, oldUsed.length);

		var oldPool = __pool;
		__pool = new IEntity[oldPool.length + Constants.ADD_ELEMENT_POOL];
		System.arraycopy(oldPool, 0, __pool, 0, oldPool.length);

		for (int i = oldPool.length; i < __pool.length; i++) {
			try {
				var entity = __clazz.getDeclaredConstructor().newInstance();
				entity.setId(UUID.randomUUID());
				entity.setContextInfo(__contextInfo);
				__pool[i] = entity;
				__used[i] = false;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				error("EXCEPTION CREATE", "component", e);
			}
		}

		info("COMPONENT POOL",
				buildgen("Increase the number of elements by ", Constants.ADD_ELEMENT_POOL, " to ", __used.length));

		// and allocate the last old ELement
		__used[oldPool.length - 1] = true;
		return __pool[oldPool.length - 1];
	}

	@Override
	public void repay(IEntity element) {
		try {
			for (int i = 0; i < __pool.length; i++) {
				if (__pool[i] == element) {
					__used[i] = false;
					element.reset();
					return;
				}
			}
			throw new NullElementPoolException();
		} catch (NullElementPoolException e) {
			error("EXCEPTION REPAY", "component", e);
		}
	}
	
	@Override
	public void cleanup() {
		for (int i = 0; i < __pool.length; i++) {
			__pool[i] = null;
		}
		__used = null;
		__pool = null;
	}

}
