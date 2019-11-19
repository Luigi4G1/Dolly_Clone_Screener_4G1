package persistence;

import java.io.File;

import utils.FileUtils4G1;

public class System_Version {
	
	public System_Version(String system_name, String system_version, File directory, boolean saved) {
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);
		
		if(system_version == null)
			system_version = "";
		system_version = system_version.trim();
		
		this.system_name = system_name;
		this.system_version = system_version;
		this.directory = directory;
		this.saved = saved;
	}
	
	public String getSystem_name() {
		return system_name;
	}
	public void setSystem_name(String system_name) {
		if(system_name == null)
			system_name = "";
		system_name = system_name.trim();
		system_name = FileUtils4G1.filePathNormalize(system_name);
		while(system_name.startsWith(File.separator))
			system_name = system_name.substring(1);
		while(system_name.endsWith(File.separator))
			system_name = system_name.substring(0, system_name.length()-1);

		this.system_name = system_name;
	}
	public String getSystem_version() {
		return system_version;
	}
	public void setSystem_version(String system_version) {
		if(system_version == null)
			system_version = "";
		system_version = system_version.trim();

		this.system_version = system_version;
	}
	public File getDirectory() {
		return directory;
	}
	public void setDirectory(File directory) {
		this.directory = directory;
	}
	public boolean isSaved() {
		return saved;
	}
	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((directory == null) ? 0 : directory.hashCode());
		result = prime * result + (saved ? 1231 : 1237);
		result = prime * result + ((system_name == null) ? 0 : system_name.hashCode());
		result = prime * result + ((system_version == null) ? 0 : system_version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		System_Version other = (System_Version) obj;
		if (directory == null) {
			if (other.directory != null)
				return false;
		} else if (!directory.equals(other.directory))
			return false;
		if (saved != other.saved)
			return false;
		if (system_name == null) {
			if (other.system_name != null)
				return false;
		} else if (!system_name.equals(other.system_name))
			return false;
		if (system_version == null) {
			if (other.system_version != null)
				return false;
		} else if (!system_version.equals(other.system_version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "System_Version [system_name=" + system_name + ", system_version=" + system_version + ", directory="
				+ directory + ", saved=" + saved + "]";
	}

	private String system_name = null;
	private String system_version = null;
	private File directory = null;
	private boolean saved;

}
