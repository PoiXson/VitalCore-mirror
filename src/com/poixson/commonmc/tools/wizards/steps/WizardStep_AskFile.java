package com.poixson.commonbukkit.tools.wizards.steps;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

import com.poixson.commonbukkit.tools.wizards.Wizard;
import com.poixson.utils.SanUtils;
import com.poixson.utils.Utils;


public abstract class WizardStep_AskFile extends WizardStep_Ask {

	protected final File path;
	protected final String filePattern;

	protected final AtomicReference<File> answerFile = new AtomicReference<File>(null);



	public WizardStep_AskFile(final Wizard wizard,
			final String logPrefix, final String chatPrefix,
			final String question, final File path, final String filePattern) {
		super(
			wizard,
			logPrefix,
			chatPrefix,
			question
		);
		if (!filePattern.contains("*"))
			throw new IllegalArgumentException("Invalid file pattern: "+filePattern);
		this.path = path;
		this.filePattern = filePattern;
	}



	@Override
	public boolean validateAnswer() {
		final String answer = this.getAnswer();
		if (Utils.notEmpty(answer)) {
			final String[] parts = this.filePattern.split("[*]");
			final String pathStr =
				(new StringBuilder())
				.append(parts[0])
				.append(SanUtils.FileName(answer))
				.append(parts[1])
				.toString();
			final File file = new File(this.path, pathStr);
			if (!file.isFile()) {
				this.sendMessage("File not found: " + file.getName());
				this.answer.set(null);
				return false;
			}
			if (!file.canRead()) {
				this.sendMessage("Cannot read file: " + file.getName());
				this.answer.set(null);
				return false;
			}
			this.answerFile.set(file);
			return true;
		}
		this.answer.set(null);
		return false;
	}



}
