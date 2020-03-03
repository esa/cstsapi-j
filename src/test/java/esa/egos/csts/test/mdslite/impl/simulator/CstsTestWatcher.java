package esa.egos.csts.test.mdslite.impl.simulator;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CstsTestWatcher extends TestWatcher {
	protected enum EventType {
		STARTED, PASSED, FAILED
	};

	protected static int CAPACITY = 100;

	@Override
	protected void starting(Description description) {
		logBanner(EventType.STARTED, description, null);
		super.starting(description);
	}

	@Override
	protected void succeeded(Description description) {
		logBanner(EventType.PASSED, description, null);
		super.succeeded(description);
	}

	@Override
	protected void failed(Throwable e, Description description) {
		logBanner(EventType.FAILED, description, e);
		super.failed(e, description);
	}

	protected void logBanner(EventType event, Description description, Throwable e) {
		String testMethodName = description.getDisplayName();

		if (null != testMethodName) {
			StringBuffer sb = new StringBuffer(CAPACITY);
			sb.append("* ");
			sb.append(testMethodName);
			sb.append(" ");

			switch (event) {
			case PASSED:
				sb.append(EventType.PASSED.name());
				break;
			case FAILED:
				sb.append(EventType.FAILED.name());
				break;
			default:
				break;
			}

			System.out.println();
			System.out.println("************************************************************************************");
			System.out.println(sb.toString());
			System.out.println("************************************************************************************");

			if (null != e) {
				e.printStackTrace(System.out);
			}
		}
	}
}