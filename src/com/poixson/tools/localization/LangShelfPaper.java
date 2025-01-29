package com.poixson.tools.localization;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.entity.Player;

import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.abstractions.Tuple;
import com.poixson.tools.indexselect.IndexSelect;
import com.poixson.vitalcore.VitalCoreDefines;


public class LangShelfPaper extends LangShelf {

	protected final AtomicReference<xJavaPlugin<?>> plugins = new AtomicReference<xJavaPlugin<?>>(null);



	public LangShelfPaper() {
		this(VitalCoreDefines.Lang);
	}
	public LangShelfPaper(final LangShelf parent) {
		super(parent);
	}



	public xJavaPlugin<?> getPlugin() {
		return this.plugins.get();
	}
	public LangShelfPaper setPlugin(final xJavaPlugin<?> plugin) {
		this.plugins.set(plugin);
		this.ref(plugin.getClass());
		return this;
	}



	// -------------------------------------------------------------------------------
	// language phrases



	// phrase
	public String getPhrase(final Player player, final String key, final String...args) {
		return (
			player == null
			? super.getDefaultPhrase(key, args)
			: this.getPhrase(player.getUniqueId(), key, args)
		);
	}
	public String getPhrase(final UUID player, final String key, final String...args) {
//TODO: get player language from profile
return super.getDefaultPhrase(key, args);
	}



	// phrases
	public Tuple<String[], IndexSelect> getPhrases(final Player player, final String key) {
//TODO: get player language from profile
return super.getDefaultPhrases(key);
	}



}
