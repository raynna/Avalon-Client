/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.account;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.events.SessionClose;
import net.runelite.client.events.SessionOpen;

@Singleton
@Slf4j
public class SessionManager
{
	@Getter
	private AccountSession accountSession;

	private final EventBus eventBus;
	private final ConfigManager configManager;
	private final File sessionFile;
	private final Gson gson;

	@Inject
	private SessionManager(
		@Named("sessionfile") File sessionfile,
		ConfigManager configManager,
		EventBus eventBus,
		Gson gson)
	{
		this.configManager = configManager;
		this.eventBus = eventBus;
		this.sessionFile = sessionfile;
		this.gson = gson;

		eventBus.register(this);
	}

	public void loadSession()
	{
		if (!sessionFile.exists())
		{
			log.info("No session file exists");
			return;
		}

		AccountSession session;

		try (FileInputStream in = new FileInputStream(sessionFile))
		{
			session = gson.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), AccountSession.class);

			log.debug("Loaded session for {}", session.getUsername());
		}
		catch (Exception ex)
		{
			log.warn("Unable to load session file", ex);
			return;
		}

		openSession(session);
	}

	private void saveSession()
	{
		if (accountSession == null)
		{
			return;
		}

		try (Writer fw = new OutputStreamWriter(new FileOutputStream(sessionFile), StandardCharsets.UTF_8))
		{
			gson.toJson(accountSession, fw);

			log.debug("Saved session to {}", sessionFile);
		}
		catch (IOException ex)
		{
			log.warn("Unable to save session file", ex);
		}
	}

	private void deleteSession()
	{
		sessionFile.delete();
	}

	/**
	 * Set the given session as the active session and open a socket to the
	 * server with the given session
	 *
	 * @param session session
	 */
	private void openSession(AccountSession session)
	{
		accountSession = session;

		if (session.getUsername() != null)
		{
			// Initialize config for new session
			// If the session isn't logged in yet, don't switch to the new config
			configManager.switchSession(session);
		}

		eventBus.post(new SessionOpen());
	}

	private void closeSession()
	{
		if (accountSession == null)
		{
			return;
		}

		log.debug("Logging out of account {}", accountSession.getUsername());

		accountSession = null; // No more account

		// Restore config
		configManager.switchSession(null);

		eventBus.post(new SessionClose());
	}

	public void logout()
	{
		closeSession();
		deleteSession();
	}
}
