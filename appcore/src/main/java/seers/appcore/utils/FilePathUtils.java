package seers.appcore.utils;

import java.io.File;

import org.apache.commons.lang3.StringUtils;


public class FilePathUtils {

	public static File getFile(File baseFolder, String... subfolders) {
		StringBuilder builder = new StringBuilder(baseFolder.getAbsolutePath());

		builder.append(File.separator);
		builder.append(joinSubFolders(subfolders));

		return new File(builder.toString());
	}

	public static String joinSubFolders(String... subfolders) {
		return StringUtils.join(subfolders, File.separator);
	}

	public static File getFileFromObjects(File baseFolder, Object... objs) {
		String separator = "_";
		return getFileFromObjects(baseFolder, separator, objs);
	}

	private static File getFileFromObjects(File baseFolder, String separator, Object... objs) {
		StringBuilder builder = new StringBuilder(baseFolder.getAbsolutePath());
		builder.append(File.separator);

		int numObjs = 0;
		for (Object obj : objs) {
			if (obj != null) {
				builder.append(obj.toString());
				builder.append(separator);
				numObjs++;
			}
		}

		if (numObjs > 0) {
			builder.delete(builder.length() - separator.length(), builder.length());
		}

		return new File(builder.toString());
	}

}
