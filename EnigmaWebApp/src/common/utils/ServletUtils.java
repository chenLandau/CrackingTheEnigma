package common.utils;

import Engine.Engine;
import Engine.EngineManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import usersManager.UsersManager;

import static common.constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
	private static final String ENGINE_MANAGER_ATTRIBUTE_NAME = "engineManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object engineManagerLock = new Object();

	public static EngineManager getEngineManager(ServletContext servletContext) {
		synchronized (engineManagerLock) {
			if (servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME, new Engine());
			}
		}
		return (EngineManager) servletContext.getAttribute(ENGINE_MANAGER_ATTRIBUTE_NAME);
	}

	public static int getIntParameter(HttpServletRequest request, String name) {
		String value = request.getParameter(name);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException numberFormatException) {
			}
		}
		return INT_PARAMETER_ERROR;
	}
}
