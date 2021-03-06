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

package com.tommsy.sorryai.agent;

import com.tommsy.sorryai.game.Game;
import com.tommsy.sorryai.game.Player;

/**
 * Used by a {@link Game} to match {@link Player Players} to {@link Agent Agents}.
 */
public class AgentHandler {
    private final Agent[] agents;

    /**
     * @param agents Array of 4 agents
     * @throws IllegalArgumentException if length of array is not 4
     */
    public AgentHandler(Agent... agents) {
        if (agents == null || agents.length != 4)
            throw new IllegalArgumentException("There must be exactly 4 agents.");
        this.agents = agents;
    }

    public void setAgent(int index, Agent agent) {
        agents[index] = agent;
    }

    public Agent getAgent(int index) {
        return agents[index];
    }
}
