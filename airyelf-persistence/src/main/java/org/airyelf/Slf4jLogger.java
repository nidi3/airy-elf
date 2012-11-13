package org.airyelf;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EclipseLink logger implementation delegating to SLF4J. Setting the log level
 * using persistence unit properties is not supported by this implementation to
 * prevent confusion of which configuration is actually used. Use your logging
 * implementation (e. g. log4j) to configure the log levels. Use the following
 * scheme for logger names to set the log for EclipseLink's logging categories
 * as defined in {@link SessionLog}:<br>
 * org.eclipse.persistence.<strong>categoryName</strong>.<br>
 * <br>
 * Example: Set the logger 'org.eclipse.persistence.sql' to DEBUG to enable SQL
 * statement logging.
 * <p/>
 * Currently doesn't support logging of connection, session and thread
 * information. That information hardly makes sense using the JPA interface and
 * external connection pooling.
 * <p/>
 * The log levels are mapped as following:
 * <table>
 * <tr>
 * <th>EclipseLink</th>
 * <th>SLF4J</th>
 * </tr>
 * <tr>
 * <td>ALL<br>
 * FINEST</td>
 * <td>TRACE</td>
 * </tr>
 * <tr>
 * <td>FINER<br>
 * FINE</td>
 * <td>DEBUG</td>
 * </tr>
 * <tr>
 * <td>CONFIG<br>
 * INFO</td>
 * <td>INFO</td>
 * </tr>
 * <tr>
 * <td>WARNING</td>
 * <td>WARN</td>
 * </tr>
 * <tr>
 * <td>SEVERE</td>
 * <td>ERROR</td>
 * </tr>
 * </table>
 *
 * @author Jaro Kuruc
 * @author Adrian Gygax
 * @since 1.0.0
 */
public class Slf4jLogger extends AbstractSessionLog {

    private enum SLF4JLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }

    /**
     * The base name for EclipseLink {@link Logger}s.
     */
    public static final String ECLIPSELINK_NAMESPACE = "org.eclipse.persistence";

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(ECLIPSELINK_NAMESPACE);

    /**
     * Stores eagerly initialised loggers for EclipseLink categories.
     */
    private static final Map<String, Logger> CATEGORY_LOGGERS = new HashMap<>();

    static {
        for (String loggerCategory : loggerCatagories) {
            String loggerNameSpace = ECLIPSELINK_NAMESPACE + "." + loggerCategory;
            CATEGORY_LOGGERS.put(loggerCategory, LoggerFactory.getLogger(loggerNameSpace));
        }
    }

    private Logger getLogger(String category) {
        Logger logger = CATEGORY_LOGGERS.get(category);
        if (logger == null) {
            logger = DEFAULT_LOGGER;
        }

        return logger;
    }

    private void logEntry(SessionLogEntry logEntry, LoggerCallback callback) {
        if (logEntry.hasException()) {
            if (shouldLogExceptionStackTrace()) {
                callback.log(null, logEntry.getException());
            } else {
                callback.log("{}", logEntry.getException().toString());
            }
        } else {
            callback.log("{}", formatMessage(logEntry));
        }
    }

    @Override
    public void log(final SessionLogEntry logEntry) {
        if (logEntry != null) {
            final Logger logger = getLogger(logEntry.getNameSpace());

            SLF4JLevel slf4jLevel = getSLF4JLevel(logEntry.getLevel());

            switch (slf4jLevel) {
                case TRACE:
                    if (logger.isTraceEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.trace(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.trace(format, arg);
                            }
                        });
                    }
                    break;
                case DEBUG:
                    if (logger.isDebugEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.debug(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.debug(format, arg);
                            }
                        });
                    }
                    break;
                case INFO:
                    if (logger.isInfoEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.info(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.info(format, arg);
                            }
                        });
                    }
                    break;
                case WARN:
                    if (logger.isWarnEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.warn(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.warn(format, arg);
                            }
                        });
                    }
                    break;
                case ERROR:
                    if (logger.isErrorEnabled()) {
                        logEntry(logEntry, new LoggerCallback() {
                            @Override
                            public void log(String msg, Throwable t) {
                                logger.error(msg, t);
                            }

                            @Override
                            public void log(String format, String arg) {
                                logger.error(format, arg);
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private SLF4JLevel getSLF4JLevel(int eclipselinkLevel) {
        switch (eclipselinkLevel) {
            case ALL:
            case FINEST:
                return SLF4JLevel.TRACE;
            case FINER:
            case FINE:
                return SLF4JLevel.DEBUG;
            case CONFIG:
            case INFO:
                return SLF4JLevel.INFO;
            case WARNING:
                return SLF4JLevel.WARN;
            case SEVERE:
                return SLF4JLevel.ERROR;
        }

        throw new IllegalArgumentException("Unknown EclipseLink log level: "
                + eclipselinkLevel);
    }

    @Override
    public boolean shouldLog(int level, String category) {

        final SLF4JLevel slf4jLevel = getSLF4JLevel(level);
        final Logger logger = getLogger(category);

        switch (slf4jLevel) {
            case TRACE:
                return logger.isTraceEnabled();
            case DEBUG:
                return logger.isDebugEnabled();
            case INFO:
                return logger.isInfoEnabled();
            case WARN:
                return logger.isWarnEnabled();
            case ERROR:
                return logger.isErrorEnabled();
        }

        throw new IllegalStateException("Unhandled SLF4J loglevel detected: "
                + slf4jLevel);
    }

    @Override
    public void setLevel(int level, String category) {
        throw new UnsupportedOperationException(
                "Setting the loglevel is not supported. Do not set the 'showSql' attribute "
                        + "of EclipseLinkJpaVendorAdapter and do not specify an "
                        + "'eclipselink.logging.level' persistence unit property. "
                        + "Simply use your logger implementation (e. g. log4j) to set the loglevel.");
    }

    @Override
    public void setLevel(int level) {
        setLevel(level, null);
    }

    private interface LoggerCallback {
        void log(String msg, Throwable t);

        void log(String format, String arg);
    }

}
