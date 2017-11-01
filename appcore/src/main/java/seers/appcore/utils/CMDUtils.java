package seers.appcore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

public class CMDUtils {

	public static CMDOutput executeCommand(String cmd) throws IOException, InterruptedException {

		Runtime rt = Runtime.getRuntime();
		Process process = rt.exec(cmd);

		String line = null;
		StringBuffer stdOutBuf = new StringBuffer();
		try (BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {

			while ((line = stdoutReader.readLine()) != null) {
				stdOutBuf.append(line);
				stdOutBuf.append("\n");
			}
			stdoutReader.close();
		} catch (Exception e) {
			stdOutBuf.append(e.getMessage());
		}

		StringBuffer stdErrBuf = new StringBuffer();
		try (BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));) {

			while ((line = stderrReader.readLine()) != null) {
				stdErrBuf.append(line);
				stdErrBuf.append("\n");
			}
			stderrReader.close();
		} catch (Exception e) {
			stdErrBuf.append(e.getMessage());
		}

		// -------------------

		int exit = process.waitFor();

		return new CMDOutput(exit, stdOutBuf.toString(), stdErrBuf.toString(), cmd);

	}

	public static String getStringCommand(String... subCmds) {

		if (subCmds == null) {
			return null;
		}

		return StringUtils.join(subCmds, ' ');
	}

	public static class CMDOutput {

		private String cmd;
		private int exit = -1;
		private String stdOut;
		private String stdErr;

		public CMDOutput(int exit, String stdOut, String stdErr, String cmd) {
			super();
			this.exit = exit;
			this.stdOut = stdOut;
			this.stdErr = stdErr;
			this.cmd = cmd;
		}

		public boolean success() {
			return 0 == exit;
		}

		@Override
		public String toString() {
			return "[cmd=" + cmd + ", exit=" + exit + ", stdOut=" + stdOut + ", stdErr=" + stdErr + "]";
		}

	}
}
