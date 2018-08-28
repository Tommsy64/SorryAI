/*
* Sorry AI
* Copyright (C) 2018  Tommsy64
*
* Sorry AI is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* Sorry AI is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Sorry AI.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.tommsy.sorryai.game.dice;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Rolls calculated by {@link Random}
 */
public class RandomDiceRoller implements DiceRoller {

    private final Random rand;

    public RandomDiceRoller() {
        this(new SecureRandom());
    }

    public RandomDiceRoller(Random rand) {
        this.rand = rand;
    }

    @Override
    public int roll() {
        return rand.nextInt(6) + 1;
    }
}
