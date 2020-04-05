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
package com.tenio.examples.example5.systems;

import com.tenio.engine.ecs.api.IContext;
import com.tenio.engine.ecs.systems.AbstractSystem;
import com.tenio.engine.ecs.systems.IExecuteSystem;
import com.tenio.engine.ecs.systems.IInitializeSystem;
import com.tenio.examples.example5.components.Position;
import com.tenio.examples.example5.constants.Constants;
import com.tenio.examples.example5.context.GameComponents;
import com.tenio.examples.example5.context.GameEntity;

/**
 * @author kong
 */
public class MoveSystem extends AbstractSystem<GameEntity> implements IInitializeSystem, IExecuteSystem {

	public MoveSystem(IContext<GameEntity> context) {
		super(context);
	}

	@Override
	public void initialize() {

	}

	@Override
	public void execute(float deltaTime) {
		for (var entity : getContext().getEntities().values()) {
			if (entity.hasComponents(GameComponents.POSITION, GameComponents.MOTION)) {
				var position = (Position) entity.getComponent(GameComponents.POSITION);
				if (position.x < Constants.DESIGN_WIDTH) {
					position.x += deltaTime;
				} else {
					position.x = 0;
				}
			}
		}
	}

}
