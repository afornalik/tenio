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
package com.tenio.ecs.model;

import com.tenio.ecs.model.components.Animation;
import com.tenio.ecs.model.components.Motion;
import com.tenio.ecs.model.components.Position;
import com.tenio.ecs.model.components.View;

/**
 * @author kong
 */
public final class GameComponents {

	public static byte ANIMATION = 0;
	public static byte MOTION = 1;
	public static byte POSITION = 3;
	public static byte VIEW = 4;

	private static int __numberComponents = 5;
	private static String __componentNames[] = { "Animation", "Motion", null, "Position", "View" };
	private static Class<?> __componentTypes[] = { Animation.class, Motion.class, null, Position.class, View.class };

	public static int getNumberComponents() {
		return __numberComponents;
	}

	public static String[] getComponentNames() {
		return __componentNames;
	}

	public static Class<?>[] getComponentTypes() {
		return __componentTypes;
	}

}
