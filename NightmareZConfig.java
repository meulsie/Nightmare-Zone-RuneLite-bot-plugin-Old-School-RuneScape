/*
 * Copyright (c) 2019, Patryk Sitko
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.nightmareZ;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/**
 *
 * @author Patryk Sitko
 */
@ConfigGroup("NightmareZ")
public interface NightmareZConfig extends Config{
    
	@ConfigItem(
		keyName = "Use Protect from Melee",
		name = "Protect from Melee",
		description = "Turn on if you want to use Protect from melee",
		position = 1
	)
	default boolean useProtectMelee()
	{
		return false;
	}
    
	@ConfigItem(
		keyName = "Just gruzle",
		name = "Always Gruzle",
		description = "Turn on if you want to always stay at hp lvl 2",
		position = 2
	)
	default boolean gruzleAnyway()
	{
		return false;
	}
    
	@ConfigItem(
		keyName = "Use rapid head to prevent hp regeneration.",
		name = "Freeze HP",
		description = "Turn on if you want to freeze the hp regeneraton.",
		position = 3
	)
	default boolean freezeHP()
	{
		return true;
	}
}
