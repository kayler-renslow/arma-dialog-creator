package com.kaylerrenslow.armaDialogCreator.data.xml;

import com.kaylerrenslow.armaDialogCreator.arma.stringtable.Language;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTable;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableKey;
import com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableWriter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.kaylerrenslow.armaDialogCreator.arma.stringtable.StringTableXmlConstants.*;

/**
 @author Kayler
 @since 12/25/2016 */
public class StringTableXmlWriter implements StringTableWriter {
	@Override
	public void writeTable(@NotNull StringTable table) throws IOException {
		StringTableXmlWriterWrapper writerWrapper = new StringTableXmlWriterWrapper(table);
		writerWrapper.writeTable();
	}

	private static class StringTableXmlWriterWrapper extends XmlWriterOutputStream {

		private StringTable t;

		public StringTableXmlWriterWrapper(@NotNull StringTable table) throws IOException {
			super(table.getFile());
			this.t = table;
		}

		public void writeTable() throws IOException {
			LinkedList<Package> packages = new LinkedList<>();
			LinkedList<StringTableKey> noPackages = new LinkedList<>();
			preloadKeys(packages, noPackages);

			writeDefaultProlog();
			write(NEW_LINE);
			writeBeginTag(String.format("%s %s=\"%s\"", PROJECT, PROJECT_NAME, t.getStringTableProjectName()));
			write(NEW_LINE);

			for (StringTableKey key : noPackages) {
				writeKey(key, 1);
			}
			for (Package p : packages) {
				byte[] tab = tab(1);
				write(tab);
				writeBeginTag(String.format("%s %s=\"%s\"", PACKAGE, NAME, p.getName()));
				write(NEW_LINE);

				for (StringTableKey key : p.getKeys()) {
					writeKey(key, 2);
				}

				for (Container c : p.getContainers()) {
					writeContainer(c, 2);
				}

				write(tab);
				writeCloseTag(PACKAGE);
				write(NEW_LINE);
			}
			writeCloseTag(PROJECT);

		}

		private void writeContainer(@NotNull Container container, int tabAmount) throws IOException {
			byte[] tab = tab(tabAmount);
			write(tab);
			writeBeginTag(String.format("%s %s=\"%s\"", CONTAINER, NAME, container.getName()));
			write(NEW_LINE);

			for (StringTableKey key : container.getKeys()) {
				writeKey(key, tabAmount + 1);
			}

			for (Container c : container.getContainers()) {
				writeContainer(c, tabAmount + 1);
			}

			write(tab);
			writeCloseTag(CONTAINER);
			write(NEW_LINE);
		}

		private void preloadKeys(LinkedList<Package> packages, LinkedList<StringTableKey> noPackages) {
			for (StringTableKey key : t.getKeys()) {
				if (key.getPath().noPackageName()) {
					noPackages.add(key);
					continue;
				}
				Package matchPackage = null;
				for (Package p : packages) {
					if (p.getName().equals(key.getPath().getPackageName())) {
						matchPackage = p;
						break;
					}
				}
				if (matchPackage == null) {
					matchPackage = new Package(key.getPath().getPackageName());
					packages.add(matchPackage);
				}
				if (key.getPath().noContainer()) {
					matchPackage.getKeys().add(key);
					continue;
				}
				LinkedList<String> containers = new LinkedList<>();
				containers.addAll(key.getPath().getContainers());
				Container insertContainer = null;

				while (containers.size() > 0) {
					String container = containers.removeFirst();
					if (insertContainer == null) {
						for (Container c : matchPackage.getContainers()) {
							if (c.getName().equals(container)) {
								insertContainer = c;
								break;
							}
						}
						if (insertContainer == null) {
							insertContainer = new Container(container);
							matchPackage.getContainers().add(insertContainer);
						}
					} else {
						Container matchedContainer = null;
						for (Container c : insertContainer.getContainers()) {
							if (c.getName().equals(container)) {
								matchedContainer = c;
								break;
							}
						}
						if (matchedContainer == null) {
							matchedContainer = new Container(container);
							insertContainer.getContainers().add(matchedContainer);
						}
					}
				}
				if (insertContainer == null) {
					throw new IllegalStateException("insertContainer shouldn't be null");
				}
				insertContainer.getKeys().add(key);

			}
		}

		private void writeKey(@NotNull StringTableKey key, int tabAmount) throws IOException {
			byte[] tab = tab(tabAmount);
			write(tab);
			writeBeginTag(String.format("%s %s=\"%s\"", KEY, ID, key.getId()));
			write(NEW_LINE);

			byte[] tab1 = tab(tabAmount + 1);
			for (Map.Entry<Language, String> entry : key.getLanguageTokenMap().entrySet()) {
				write(tab1);
				writeBeginTag(entry.getKey().getName());
				write(entry.getValue());
				writeCloseTag(entry.getKey().getName());
				write(NEW_LINE);
			}

			write(tab);
			writeCloseTag(KEY);
			write(NEW_LINE);
		}

		private byte[] tab(int amount) {
			byte[] tabs = new byte[amount];
			Arrays.fill(tabs, (byte) '\t');
			return tabs;
		}
	}

	private static class Package {
		private final String name;
		private final List<Container> containers = new LinkedList<>();
		private final List<StringTableKey> keys = new LinkedList<>();

		public Package(@NotNull String name) {
			this.name = name;
		}

		@NotNull
		public String getName() {
			return name;
		}

		@NotNull
		public List<Container> getContainers() {
			return containers;
		}

		@NotNull
		public List<StringTableKey> getKeys() {
			return keys;
		}

		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof Package) {
				Package other = (Package) o;
				return getName().equals(other.getName());
			}
			return false;
		}
	}

	private static class Container {
		private final String name;
		private final List<StringTableKey> keys = new LinkedList<>();
		private final List<Container> containers = new LinkedList<>();

		public Container(@NotNull String name) {
			this.name = name;
		}

		@NotNull
		public String getName() {
			return name;
		}

		@NotNull
		public List<StringTableKey> getKeys() {
			return keys;
		}


		@NotNull
		public List<Container> getContainers() {
			return containers;
		}


		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o instanceof Container) {
				Container other = (Container) o;
				return getName().equals(other.getName());
			}
			return false;
		}
	}
}
