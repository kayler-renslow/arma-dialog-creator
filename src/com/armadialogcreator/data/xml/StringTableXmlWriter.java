package com.armadialogcreator.data.xml;

import com.armadialogcreator.core.stringtable.*;
import com.armadialogcreator.util.XmlWriter;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 @author Kayler
 @since 12/25/2016 */
public class StringTableXmlWriter implements StringTableWriter {
	@Override
	public void writeTable(@NotNull StringTable table) throws IOException {
		StringTableXmlWriterWrapper writerWrapper = new StringTableXmlWriterWrapper(table);
		writerWrapper.writeTable();
	}

	private static class StringTableXmlWriterWrapper extends XmlWriter {

		private StringTable t;

		public StringTableXmlWriterWrapper(@NotNull StringTable table) throws IOException {
			super(table.getFile(), StringTableXmlConstants.PROJECT);
			this.t = table;
		}

		public void writeTable() {
			LinkedList<Package> packages = new LinkedList<>();
			LinkedList<StringTableKey> noPackages = new LinkedList<>();
			preloadKeys(packages, noPackages);

			getRootElement().setAttribute(StringTableXmlConstants.PROJECT_NAME, t.getStringTableProjectName());

			for (StringTableKey key : noPackages) {
				writeKey(key, getRootElement());
			}
			for (Package p : packages) {
				Element pakage = appendElementToRoot(StringTableXmlConstants.PACKAGE);
				pakage.setAttribute(StringTableXmlConstants.NAME, p.getName());

				for (StringTableKey key : p.getKeys()) {
					writeKey(key, pakage);
				}

				for (Container c : p.getContainers()) {
					writeContainer(c, pakage);
				}

			}
		}

		private void writeContainer(@NotNull Container container, @NotNull Element addTo) {
			Element containerEle = appendElementToElement(StringTableXmlConstants.CONTAINER, addTo);
			containerEle.setAttribute(StringTableXmlConstants.NAME, container.getName());

			for (StringTableKey key : container.getKeys()) {
				writeKey(key, containerEle);
			}

			for (Container c : container.getContainers()) {
				writeContainer(c, containerEle);
			}
		}

		private void preloadKeys(@NotNull LinkedList<Package> packages, @NotNull LinkedList<StringTableKey> noPackages) {
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

		private void writeKey(@NotNull StringTableKey key, @NotNull Element addTo) {
			Element keyEle = appendElementToElement(StringTableXmlConstants.KEY, addTo);
			keyEle.setAttribute(StringTableXmlConstants.ID, key.getId());

			for (Map.Entry<Language, String> entry : key.getLanguageTokenMap().entrySet()) {
				Element languageEle = appendElementToElement(entry.getKey().getName(), keyEle);
				appendTextNode(entry.getValue(), languageEle);
			}
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
