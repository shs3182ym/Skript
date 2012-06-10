/*
 *   This file is part of Skript.
 *
 *  Skript is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Skript is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Skript.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 * Copyright 2011, 2012 Peter Güttinger
 * 
 */

package ch.njol.skript.variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.TriggerFileLoader;
import ch.njol.skript.api.LoopVar;
import ch.njol.skript.lang.ExprParser.ParseResult;
import ch.njol.skript.lang.Variable;
import ch.njol.skript.variables.base.VarVariable;

/**
 * used to access a loop's current value.
 * 
 * @author Peter Güttinger
 * 
 */
public class VarLoopValue extends VarVariable<Object> {
	
	static {
		Skript.registerVariable(VarLoopValue.class, Object.class, "[the] loop-<\\S+>");
	}
	
	private String name;
	
	@Override
	public boolean init(final Variable<?>[] vars, final int matchedPattern, final ParseResult parser) {
		name = parser.expr;
		String s = parser.regexes.get(0).group();
		int i = 1;
		final Matcher m = Pattern.compile("^(.+)-(\\d+)$").matcher(s);
		if (m.matches()) {
			s = m.group(1);
			i = Integer.parseInt(m.group(2));
		}
		for (final LoopVar<?> v : TriggerFileLoader.currentLoops) {
			if (v.isLoopOf(s)) {
				if (i > 1) {
					i--;
					continue;
				}
				var = v;
				return true;
			}
		}
		Skript.error("there's no loop that matches " + name);
		return false;
	}
	
	@Override
	public String getDebugMessage(final Event e) {
		if (e == null)
			return name;
		return var.getDebugMessage(e);
	}
	
}