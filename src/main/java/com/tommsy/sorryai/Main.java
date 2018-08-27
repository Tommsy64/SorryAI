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

package com.tommsy.sorryai;

import java.util.Collections;
import java.util.Scanner;

import com.google.devtools.common.options.OptionsParser;
import com.tommsy.sorryai.agent.AgentHandler;
import com.tommsy.sorryai.agent.RandomAgent;
import com.tommsy.sorryai.game.Game;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        OptionsParser parser = OptionsParser.newOptionsParser(Options.class);
        parser.parseAndExitUponError(args);
        Options options = parser.getOptions(Options.class);

        // if (options.help) {
        // printUsage(parser);
        // return;
        // }

        AgentHandler handler = new AgentHandler(new RandomAgent(), new RandomAgent(), new RandomAgent(), new RandomAgent());
        Game game = new Game(handler);
        game.runGame();

        scanner.close();
    }

    private static void printUsage(OptionsParser parser) {
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(), OptionsParser.HelpVerbosity.LONG));
    }
}
