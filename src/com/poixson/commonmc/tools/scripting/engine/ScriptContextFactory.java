package com.poixson.commonmc.tools.scripting.engine;

import java.util.concurrent.atomic.AtomicBoolean;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;

import com.poixson.tools.Keeper;


public class ScriptContextFactory extends ContextFactory {

	protected static final AtomicBoolean inited = new AtomicBoolean(false);



	public static void init() {
		if (inited.compareAndSet(false, true)) {
			final ScriptContextFactory factory = new ScriptContextFactory();
			ContextFactory.initGlobal(factory);
			Keeper.add(factory);
		}
	}



	@Override
	protected Context makeContext() {
		final Context context = super.makeContext();
		context.setLanguageVersion(Context.VERSION_ES6);
		context.setOptimizationLevel(9);
		return context;
	}



	@Override
	protected boolean hasFeature(final Context context, final int featureIndex) {
		switch (featureIndex) {
		case Context.FEATURE_STRICT_MODE:
		case Context.FEATURE_STRICT_VARS:
		case Context.FEATURE_STRICT_EVAL:
		case Context.FEATURE_V8_EXTENSIONS:
		case Context.FEATURE_ENABLE_JAVA_MAP_ACCESS:
		case Context.FEATURE_INTEGER_WITHOUT_DECIMAL_PLACE:
		case Context.FEATURE_ENABLE_XML_SECURE_PARSING:
		case Context.FEATURE_LOCATION_INFORMATION_IN_ERROR:
		case Context.FEATURE_WARNING_AS_ERROR:
		case Context.FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME:
		case Context.FEATURE_PARENT_PROTO_PROPERTIES:
		case Context.FEATURE_OLD_UNDEF_NULL_THIS:
			return true;
		case Context.FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER:
		case Context.FEATURE_TO_STRING_AS_SOURCE:
		case Context.FEATURE_DYNAMIC_SCOPE:
		case Context.FEATURE_ENHANCED_JAVA_ACCESS:
		case Context.FEATURE_ENUMERATE_IDS_FIRST:
		case Context.FEATURE_THREAD_SAFE_OBJECTS:
		case Context.FEATURE_LITTLE_ENDIAN:
			return false;
		default: break;
		}
		return super.hasFeature(context, featureIndex);
	}



}
